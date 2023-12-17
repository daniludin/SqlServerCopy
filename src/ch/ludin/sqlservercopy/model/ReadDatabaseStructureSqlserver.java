package ch.ludin.sqlservercopy.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.eclipse.swt.widgets.Shell;

public class ReadDatabaseStructureSqlserver extends ReadDatabaseStructure {
	public ReadDatabaseStructureSqlserver(Shell activeShell) {
		super(activeShell);
		// TODO Auto-generated constructor stub
	}

	private static String NL = "\n";

	public static void main(String[] args) {
		ReadDatabaseStructureSqlserver rds = new ReadDatabaseStructureSqlserver(null);
//		String url = "jdbc:jtds:sqlserver://bgsdbtstmssql02.sec.begasoft.ch:1433/wmssav_dev;useCursors=true";
//		String username = "wmssav_dev_admin";
//		String password = "kvmw3uskxpzkDkxVdQkf";
		// jdbc:jtds:sqlserver://localhost:1433/NorthwindB;useCursors=true
//		String url = "jdbc:jtds:sqlserver://localhost:1433/NorthwindB;useCursors=true";
		
		
		
		String url = "jdbc:sqlserver://bgsdbtstmssql02.sec.begasoft.ch:1433;databaseName=wmsregfix_dev";
		//String url = "jdbc:jtds:sqlserver://bgsdbtstmssql02.sec.begasoft.ch:1433/wmsregfix_dev;useCursors=true";
		String username = "wmsregfix_dev_admin";
		String password = "dvmh6tGksl2Axyw99zqAyd3F";

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
		Connection conn = createConnectionSqlserver(url, username, password);
		
		try {
			for (SQLWarning warn = conn.getWarnings(); warn != null; warn = warn.getNextWarning()) {
				System.out.println("SQL Warning:");
				System.out.println("State  : " + warn.getSQLState());
				System.out.println("Message: " + warn.getMessage());
				System.out.println("Error  : " + warn.getErrorCode());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuffer titel = new StringBuffer();

		try {

			DatabaseMetaData meta = (DatabaseMetaData) conn.getMetaData();

			String[] types = { "TABLE" /*, "VIEW"*/ };

			titel.append(listDatabaseName(url));
			titel.append(listGeneralMetadata(meta));

			int counter = 0; 
			ResultSet rsTables = meta.getTables(null, null, null, types);
			while (rsTables.next()) {
				String tableName = rsTables.getString(3);
				String tableSchema = rsTables.getString("TABLE_SCHEM");

				if (tableSchema.contains("sys") || tableSchema.contains("INFORMATION_SCHEMA")) {
					continue;
				}
				counter++;
				result.append(NL);
				result.append(tableSchema + "." + tableName).append(NL);
				result.append(listAttributesSorted(meta, null, null, tableName));
				result.append(NL);
				result.append(listPrimaryKeys(meta, null, tableSchema, tableName));
				result.append(listUniqueConstraints(meta, conn, null, tableSchema, tableName));
				result.append(listForeignKeys(meta, null, tableSchema, tableName));
			
			}

			rsTables.close();

			titel.append(listAnzahlTabellen(counter));
			
			result.append(listViews(conn).toString());
			result.append(listFunctions(conn).toString());

			
		} catch (SQLException ex) {
			ex.printStackTrace();
			result.append(ex.getMessage());
			CustomSQLException myEx = new CustomSQLException(ex.getMessage());
			throw myEx;

		}
		return titel.append(result);

	}

	// not implemented in SQL Server jdbc driver!
	private String listFunctions(Connection conn) throws SQLException {

		StringBuffer titel = new StringBuffer();
		StringBuffer result = new StringBuffer();
		String sql = 
				"SELECT name, definition, type_desc " + 
				"FROM sys.sql_modules m  " + 
				"INNER JOIN sys.objects o  " + 
				"ON m.object_id=o.object_id  " + 
				"WHERE type_desc like '%function%'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rsFunctions = ps.executeQuery();
		int count = 0;
		while (rsFunctions.next()) {
			result.append(NL);
			result.append(rsFunctions.getString("definition"));
			result.append(NL);
			count++;
		}
		
		ps.close();
		rsFunctions.close();
		titel.append(listAnzahlFunktionen(count));
		return titel.append(result).toString();
	}
	
	@Override
	public StringBuffer listViews(Connection conn) throws SQLException {
		StringBuffer titel = new StringBuffer();
		StringBuffer result = new StringBuffer();
		String sql = "SELECT view_definition FROM information_schema.views ORDER BY table_schema, table_name";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rsInd = ps.executeQuery();
		int count = 0;
		while (rsInd.next()) {
			result.append(NL);
			result.append(rsInd.getString(1));
			count++;
		}

		ps.close();
		rsInd.close();
		titel.append(listAnzahlViews(count));
		titel.append(NL);
		return titel.append(result);
	}
	
	@Override
	public String listUniqueConstraints(DatabaseMetaData meta, Connection conn, String catalog, String tableSchema,
			String tableName) throws SQLException {
		StringBuffer result = new StringBuffer();

		// Achtung: bei Verwendung eines PreparedStatement zusammen mit dem jdbc:sqlserver Treiber
		// gibt es diesen Fehler: Must declare scalar variable "@P1and"
		// Das gilt aber nur, wenn auch wirklich ein Parameter gesetzt wird. ?!?
		
		
		//		String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS " + "WHERE TABLE_SCHEMA = ? "
//				+ "AND TABLE_NAME = ?" 
//				+ "and CONSTRAINT_TYPE = 'UNIQUE'";
//
//		PreparedStatement ps = conn.prepareStatement(sql);
//		ps.setString(1, tableSchema);
//		ps.setString(2, tableName);
//		ResultSet rsInd = ps.executeQuery();
		
		if (tableName.equals("AnwaltsEmpfehlung")) {
			System.out.println();
		}
		
		String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS " + "WHERE TABLE_SCHEMA = '" + tableSchema + "' "
				+ "AND TABLE_NAME = '" + tableName +"' "
				+ "and CONSTRAINT_TYPE = 'UNIQUE'";

		PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rsInd = ps.executeQuery();
		
		while (rsInd.next()) {

			String constraintName = rsInd.getString("CONSTRAINT_NAME");
			ResultSet rsMeta = meta.getIndexInfo(null, tableSchema, tableName, true, true);
			while (rsMeta.next()) {
				if (rsMeta.getString("INDEX_NAME") != null && rsMeta.getString("INDEX_NAME").equals(constraintName)) {
					result.append(TAB).append("CONSTRAINT ");
					result.append(rsMeta.getString("INDEX_NAME"));
					result.append(" (");
					result.append(rsMeta.getString("COLUMN_NAME"));
					result.append(" )");
					result.append(" UNIQUE");
					result.append(NL);
				}
			}
			rsMeta.close();
		}
		//ps.close();
		rsInd.close();

		return result.toString();
	}


}