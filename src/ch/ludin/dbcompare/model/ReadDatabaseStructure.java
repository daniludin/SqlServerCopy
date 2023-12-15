package ch.ludin.dbcompare.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public abstract class ReadDatabaseStructure implements DatabaseStructureReader {
	protected static String NL = "\n";
	protected static String TAB = "\t";
	static Shell activeShell;

	public ReadDatabaseStructure(Shell activeShell) {
		super();
		this.activeShell = activeShell;
	}

	protected String listDatabaseName(String url) {
		StringBuffer result = new StringBuffer();
		result.append("===========================================================").append(NL);
		result.append(url).append(NL);
		result.append("===========================================================").append(NL);
		return result.toString();

	}

	public abstract StringBuffer readDbStructure(String url, String username, String password)
			throws CustomSQLException;

	abstract StringBuffer listViews(Connection conn) throws SQLException;

	abstract String listUniqueConstraints(DatabaseMetaData meta, Connection conn, String catalog, String tableSchema,
			String tableName) throws SQLException;

	public String listPrimaryKeys(DatabaseMetaData meta, String catalog, String schemaPattern, String tableName)
			throws SQLException {
		StringBuffer result = new StringBuffer();
		ResultSet rsPK = meta.getPrimaryKeys(catalog, schemaPattern, tableName);
		StringBuffer pkIds = new StringBuffer();
		String pkName = new String();

		while (rsPK.next()) {

			pkName = rsPK.getString("PK_NAME");
			String primaryKeyColumn = rsPK.getString("COLUMN_NAME");
			pkIds.append(primaryKeyColumn).append(",");
		}

		pkIds.append(")");
		rsPK.close();
		if (pkName.length() > 0) {

			result.append("\tCONSTRAINT " + pkName + " PRIMARY KEY (");
			result.append(pkIds.toString().replaceAll(",\\)", "\\)")).append(NL);
		}
		return result.toString();
	}

	public String listAttributesSorted(DatabaseMetaData meta, String catalog, String schemaPattern, String tableName)
			throws SQLException {
		Map<String, String> sortedAttrs = new HashMap<String, String>();
		ResultSet rsColumns = meta.getColumns(catalog, schemaPattern, tableName, null);

		while (rsColumns.next()) {
			StringBuffer sbAttr = new StringBuffer();
			String columnName = rsColumns.getString("COLUMN_NAME");
			String columnType = rsColumns.getString("TYPE_NAME").toUpperCase();
			int columnSize = rsColumns.getInt("COLUMN_SIZE");
			sbAttr.append("\t" + columnName + "\t" + columnType + "(" + columnSize + ")");
			if (rsColumns.getInt("NULLABLE") == 0) {
				sbAttr.append(" NOT NULL");
			}
			sortedAttrs.put(columnName, sbAttr.toString());
		}
		rsColumns.close();

		List<String> sortedKeys = new ArrayList<String>(sortedAttrs.keySet());
		Collections.sort(sortedKeys);
		StringBuffer result = new StringBuffer();
		for (String key : sortedKeys) {
			result.append(sortedAttrs.get(key));
			result.append(NL);
		}

		return result.toString();

	}

	public static String listGeneralMetadata(DatabaseMetaData meta) throws SQLException {
		StringBuffer result = new StringBuffer();
		result.append("Database Product Name: " + meta.getDatabaseProductName()).append(NL);
		result.append("Database Product Version: " + meta.getDatabaseProductVersion()).append(NL);
		result.append("Logged User: " + meta.getUserName()).append(NL);
		result.append("JDBC Driver: " + meta.getDriverName()).append(NL);
		result.append("Driver Version: " + meta.getDriverVersion()).append(NL);
		result.append(NL);
		return result.toString();
	}

	public static Connection createConnectionPostgres(String url, String username, String password)
			throws CustomSQLException {
		return createConnection(url, username, password, "org.postgresql.Driver");
	}

	public static Connection createConnectionSqlserver(String url, String username, String password)
			throws CustomSQLException {
		return createConnection(url, username, password, "net.sourceforge.jtds.jdbc.Driver");
	}

	public static Connection createConnection(String url, String username, String password, String driverClassName)
			throws CustomSQLException {

		Connection conn = null;
//		try {
//			Class.forName(driverClassName);
//		} catch (ClassNotFoundException e) {
//			CustomSQLException myEx = new CustomSQLException(e.getMessage());
//			throw myEx;
//		}

		ConnectionFactory.createDriver(url);

		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// e.printStackTrace();
			StringBuffer errMsg = new StringBuffer();
			errMsg.append(e.getMessage());
			errMsg.append(" Error Code (");
			errMsg.append(e.getErrorCode());
			errMsg.append(") ");
			if (!e.getMessage().equals(e.getLocalizedMessage())) {
				errMsg.append(e.getLocalizedMessage());
			}
			if (e.getCause() != null) {
				errMsg.append(e.getCause().getClass().getName());
				errMsg.append(": ");
			}
			if (e.getCause() != null && e.getCause().getMessage() != null) {
				errMsg.append(e.getCause().getMessage());
			}

			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					MessageDialog.openError(activeShell.getShell(), "Error", errMsg.toString());
				}
			});

			CustomSQLException myEx = new CustomSQLException(errMsg.toString());
			throw myEx;
		}

		return conn;
	}

	protected StringBuffer listAllColumns(ResultSet rs) throws SQLException {
		StringBuffer result = new StringBuffer();

		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();
		while (rs.next()) {
			for (int i = 1; i <= cols; i++) {
				result.append(rsmd.getColumnName(i)).append(": ");
				result.append(rs.getString(i)).append(NL);
			}
		}
		rs.close();
		result.append(NL);
		return result;
	}

	public String listForeignKeys(DatabaseMetaData meta, String catalog, String schemaPattern, String tableName)
			throws SQLException {
		StringBuffer result = new StringBuffer();
		ResultSet rsFk = meta.getImportedKeys(catalog, schemaPattern, tableName);

		while (rsFk.next()) {
			result.append("\tFOREIGN KEY  ");
			result.append(rsFk.getString("FK_NAME"));
			result.append(" (").append(rsFk.getString("FKCOLUMN_NAME")).append(") ").append(NL);

			result.append("\t\tREFERENCES " + rsFk.getString("PKTABLE_SCHEM")).append(".");
			result.append(rsFk.getString("PKTABLE_NAME"));
			result.append(" (").append(rsFk.getString("PKCOLUMN_NAME")).append(")").append(NL);

			if (rsFk.getInt("UPDATE_RULE") == DatabaseMetaData.importedKeyNoAction) {
				result.append("\t\t\tON UPDATE NO ACTION").append(NL);
			} else if (rsFk.getInt("UPDATE_RULE") == DatabaseMetaData.importedKeyCascade) {
				result.append("\t\t\tON UPDATE CASCADE").append(NL);
			} else if (rsFk.getInt("UPDATE_RULE") == DatabaseMetaData.importedKeyInitiallyDeferred) {
				result.append("\t\t\tON UPDATE INITIALLY DEFERRED").append(NL);
			} else if (rsFk.getInt("UPDATE_RULE") == DatabaseMetaData.importedKeyInitiallyImmediate) {
				result.append("\t\t\tON UPDATE INITIALLY IMMEDIATE").append(NL);
			} else if (rsFk.getInt("UPDATE_RULE") == DatabaseMetaData.importedKeyNotDeferrable) {
				result.append("\t\t\tON UPDATE IMPORTED KEY NOT DEFERRABLE").append(NL);
			} else if (rsFk.getInt("UPDATE_RULE") == DatabaseMetaData.importedKeyRestrict) {
				result.append("\t\t\tON UPDATE NO ACTION").append(NL); // same as importedKeyNoAction (for ODBC 2.x
																		// compatibility)
			} else if (rsFk.getInt("UPDATE_RULE") == DatabaseMetaData.importedKeySetDefault) {
				result.append("\t\t\tON UPDATE SET DEFAULT").append(NL);
			} else if (rsFk.getInt("UPDATE_RULE") == DatabaseMetaData.importedKeySetNull) {
				result.append("\t\t\tON UPDATE SET NULL").append(NL);
			}

			if (rsFk.getInt("DELETE_RULE") == DatabaseMetaData.importedKeyNoAction) {
				result.append("\t\t\tON DELETE NO ACTION").append(NL);
			} else if (rsFk.getInt("DELETE_RULE") == DatabaseMetaData.importedKeyCascade) {
				result.append("\t\t\tON DELETE CASCADE").append(NL);
			} else if (rsFk.getInt("DELETE_RULE") == DatabaseMetaData.importedKeyInitiallyDeferred) {
				result.append("\t\t\tON DELETE INITIALLY DEFERRED").append(NL);
			} else if (rsFk.getInt("DELETE_RULE") == DatabaseMetaData.importedKeyInitiallyImmediate) {
				result.append("\t\t\tON DELETE INITIALLY IMMEDIATE").append(NL);
			} else if (rsFk.getInt("DELETE_RULE") == DatabaseMetaData.importedKeyNotDeferrable) {
				result.append("\t\t\tON DELETE IMPORTED KEY NOT DEFERRABLE").append(NL);
			} else if (rsFk.getInt("DELETE_RULE") == DatabaseMetaData.importedKeyRestrict) {
				result.append("\t\t\tON DELETE NO ACTION").append(NL); // same as importedKeyNoAction (for ODBC 2.x
																		// compatibility)
			} else if (rsFk.getInt("DELETE_RULE") == DatabaseMetaData.importedKeySetDefault) {
				result.append("\t\t\tON DELETE SET DEFAULT").append(NL);
			} else if (rsFk.getInt("DELETE_RULE") == DatabaseMetaData.importedKeySetNull) {
				// result.append("\t\t\tON DELETE SET NULL").append(NL);
				result.append("\t\t\tON DELETE NO ACTION").append(NL);
			}

		}
		rsFk.close();
		return result.toString();
	}

	public String listAnzahlTabellen(int count) {
		return listAnzahl("Anzahl Tabellen", count);
	}

	public String listAnzahlViews(int count) {
		return listAnzahl("Anzahl Views", count);
	}

	public String listAnzahlFunktionen(int count) {
		return listAnzahl("Anzahl Funktionen", count);
	}

	public String listAnzahl(String label, int count) {
		StringBuffer result = new StringBuffer();
		result.append(NL);
		result.append("===============  ");
		result.append(label);
		result.append(": ");
		result.append(count);
		result.append("  ======================");
		result.append(NL);
		return result.toString();
	}

}