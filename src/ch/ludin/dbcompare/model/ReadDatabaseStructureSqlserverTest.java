package ch.ludin.dbcompare.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;

/**
 * This program demonstrates how to get structural information of a database.
 *
 * @author www.codejava.net
 */
public class ReadDatabaseStructureSqlserverTest extends ReadDatabaseStructure {
	public ReadDatabaseStructureSqlserverTest(Shell activeShell) {
		super(activeShell);
		// TODO Auto-generated constructor stub
	}

	private static String NL = "\n";

	public static void main(String[] args) {
		ReadDatabaseStructureSqlserverTest rds = new ReadDatabaseStructureSqlserverTest(null);
		String url = "jdbc:jtds:sqlserver://127.0.0.1:1433/Northwind;useCursors=true";
		String username = "sa";
		String password = "sa";

		try {
			System.out.println(rds.readDbStructure(url, username, password));
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error connecting to url:  " + url + "\t" + e.getMessage());
		}

	}


	@Override
	public StringBuffer readDbStructure(String url, String username, String password) throws CustomSQLException {
		StringBuffer result = new StringBuffer();
		Connection conn = null;
		try {
			// Class.forName("org.postgresql.Driver");
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			CustomSQLException myEx = new CustomSQLException(e.getMessage());
			throw myEx;
		}

		// System.out.println("PostgreSQL JDBC Driver Registered!");
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
			CustomSQLException myEx = new CustomSQLException(e.getMessage());
			throw myEx;
		}

		try {

			DatabaseMetaData meta = (DatabaseMetaData) conn.getMetaData();

			String catalog = null;
			String schemaPattern = null;
			String tableNamePattern = null;
			String[] types = { "TABLE", "VIEW" };

			result.append(listDatabaseName(url));
			result.append(listGeneralMetadata(meta));

			ResultSet rsTables = meta.getTables(catalog, schemaPattern, tableNamePattern, types);
			while (rsTables.next()) {
				String tableName = rsTables.getString(3);
				String tableSchema = rsTables.getString("TABLE_SCHEM");
				String tableType = rsTables.getString("TABLE_TYPE");

				if (tableSchema.contains("sys") || tableSchema.contains("INFORMATION_SCHEMA")) {
					continue;
				}
				System.out.println("============ " + tableName +" ===============");
				result.append(NL);
				result.append(tableSchema + "." + tableName).append(NL);
				result.append(listAttributesSorted(meta, catalog, schemaPattern, tableName));
				if (tableType.equals("TABLE")) {
					result.append(listAll(meta, catalog, schemaPattern, tableName));
				}

			}

			rsTables.close();

		} catch (SQLException ex) {
			// System.out.println(ex.getMessage());
			ex.printStackTrace();
			result.append(ex.getMessage());
			CustomSQLException myEx = new CustomSQLException(ex.getMessage());
			throw myEx;

		}
		return result;

	}

	public String listAll(DatabaseMetaData meta,  String catalog, String tableSchema, String tableName)
			throws SQLException {
		StringBuffer result = new StringBuffer();
		
		ResultSet rs = meta.getExportedKeys(catalog, tableSchema, tableName);
		result.append(listAllColumns(rs));
		System.out.println();
		System.out.println("---getExportedKeys----------");
		System.out.println(listAllColumns(meta.getExportedKeys(catalog, tableSchema, tableName)).toString());

		rs = meta.getImportedKeys(catalog, tableSchema, tableName);
		result.append(listAllColumns(rs));
		System.out.println();
		System.out.println("---getImportedKeys----------");
		System.out.println(listAllColumns(meta.getImportedKeys(catalog, tableSchema, tableName)).toString());

		rs = meta.getIndexInfo(null, tableSchema, tableName, true, true);
		result.append(listAllColumns(rs));
		System.out.println();
		System.out.println("---getIndexInfo----------");
		System.out.println(listAllColumns(meta.getIndexInfo(null, tableSchema, tableName, true, true)).toString());

		return result.toString();
	}

	public StringBuffer listViews(Connection conn) throws SQLException {
		StringBuffer result = new StringBuffer();
		String sql = "select view_definition from information_schema.views";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rsInd = ps.executeQuery();
		while (rsInd.next()) {
			result.append(rsInd.getString(1));
		}
		
		return result;
	}
	
	public String listUniqueConstraints(DatabaseMetaData meta, Connection conn, String catalog, String tableSchema,
			String tableName) throws SQLException {
		StringBuffer result = new StringBuffer();
//		String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS " + 
//				"WHERE TABLE_SCHEMA= ? " + 
//				"AND TABLE_NAME= ?" + 
//				"and CONSTRAINT_TYPE = 'UNIQUE'";
//
//			PreparedStatement ps = conn.prepareStatement(sql);
//			ps.setString(1, tableSchema);
//			ps.setString(2, tableName);
//			ResultSet rsInd = ps.executeQuery();
//			if (rsInd.next()) {
//
//				System.out.println("" + rsInd.getString("CONSTRAINT_NAME"));				
//			}
//			ps.close();
//			rsInd.close();

		ResultSet rsMeta = meta.getIndexInfo(null, tableSchema, tableName, true, true);
//			while (rsMeta.next()) {
//				System.out.println("" + rsMeta.getString("TABLE_CAT"));				
//				System.out.println("" + rsMeta.getString("TABLE_SCHEM"));				
//				System.out.println("" + rsMeta.getString("TABLE_NAME"));				
//				System.out.println("" + rsMeta.getString("INDEX_NAME"));				
//				System.out.println("" + rsMeta.getString("COLUMN_NAME"));				
//				System.out.println("" + rsMeta.getString("NON_UNIQUE"));				
//				System.out.println("" + rsMeta.getString("TYPE"));				
//				 
//			}
		System.out.println(listAllColumns(rsMeta).toString());

		return result.toString();
	}

	public static List<UniqueConstraint> getUniqueConstraints(DatabaseMetaData dm, String schema, String table)
			throws SQLException {
		Map<String, UniqueConstraint> constraints = new HashMap<>();

		ResultSet rs = dm.getIndexInfo(null, schema, table, false, false);
		while (rs.next()) {
			String indexName = rs.getString("index_name");
			String columnName = rs.getString("column_name");

			UniqueConstraint constraint = new UniqueConstraint();
			constraint.table = table;
			constraint.name = indexName;
			constraint.columns.add(columnName);

			constraints.compute(indexName, (key, value) -> {
				if (value == null) {
					return constraint;
				}
				value.columns.add(columnName);
				return value;
			});
		}
		rs.close();

		return new ArrayList<>(constraints.values());
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

	public static Connection createConnection() {

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in
			// your library path!");
			e.printStackTrace();
		}

		// System.out.println("PostgreSQL JDBC Driver Registered!");

		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres",
					"postgres");
		} catch (SQLException e) {
			// System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}
		return connection;
	}



}