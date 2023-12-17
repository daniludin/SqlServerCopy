package ch.ludin.sqlservercopy.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;

public  class ReadDatabaseStructurePostgres extends ReadDatabaseStructure {
	public ReadDatabaseStructurePostgres(Shell activeShell) {
		super(activeShell);
	}


	private static String NL = "\n";

	
	public static void main(String[] args) {
		ReadDatabaseStructurePostgres rds = new ReadDatabaseStructurePostgres(null);
		String url = "jdbc:postgresql://localhost:5432/project_template";
		String username = "postgres";
		String password = "postgres";
		
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
		Connection conn = createConnectionPostgres(url, username, password);
		StringBuffer titel = new StringBuffer();

		try {

			DatabaseMetaData meta = (DatabaseMetaData) conn.getMetaData();

			String[] types = { "TABLE" /*, "VIEW" */};

			titel.append(listDatabaseName(url));
			titel.append(listGeneralMetadata(meta));

			int counter = 0; 
			ResultSet rsTables = meta.getTables(null, null, null, types);
			while (rsTables.next()) {
				String tableName = rsTables.getString(3);
				String tableSchema = rsTables.getString("TABLE_SCHEM");

				result.append(NL);
				result.append(tableSchema + "." + tableName).append(NL);
				result.append(listAttributesSorted(meta, null, null, tableName));
				result.append(NL);
				result.append(listPrimaryKeys(meta, null, tableSchema, tableName));					
				result.append(listUniqueConstraints(meta, conn, null, tableSchema, tableName));
				result.append(listForeignKeys(meta, null, null, tableName));
				counter++;
			}

			rsTables.close();
			
			titel.append(listAnzahlTabellen(counter));

			result.append(listViews(conn).toString());
			result.append(listFunctions(meta, conn, null, null));


		} catch (SQLException ex) {
			ex.printStackTrace();
			result.append(ex.getMessage());
			CustomSQLException myEx = new CustomSQLException(ex.getMessage());
			throw myEx;

		}
		return titel.append(result);

	}
	private String listFunctions(DatabaseMetaData meta, Connection conn, String catalog, String tableSchema) throws SQLException {
		StringBuffer titel = new StringBuffer();
		StringBuffer result = new StringBuffer();
		result.append(NL);
		ResultSet rsFunctions = meta.getFunctions("", null, null);
		int count = 0;
		while (rsFunctions.next()) {
			if (rsFunctions.getString("FUNCTION_SCHEM") == null || rsFunctions.getString("FUNCTION_SCHEM").equals("pg_catalog")) {
				continue;
			}
			
			String sql2 = 
					"select pg_get_functiondef(oid) " + 
					"from pg_proc " + 
					"where proname = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql2);
			ps.setString(1, rsFunctions.getString("FUNCTION_NAME"));

			ResultSet rsInd = ps.executeQuery();
			while (rsInd.next()) {
				
				result.append(NL).append(rsInd.getString(1));
				result.append(rsInd.getString(1)).append(NL);
				count++;
			}
		}
		rsFunctions.close();
		
		titel.append(listAnzahlFunktionen(count));
		return titel.append(result).toString();

	}

	@Override
	public StringBuffer listViews(Connection conn) throws SQLException {
		StringBuffer titel = new StringBuffer();
		StringBuffer result = new StringBuffer();
		String sql = "select view_definition, table_name, table_schema from information_schema.views where table_schema not in ('pg_catalog', 'information_schema') ORDER BY table_schema, table_name";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rsInd = ps.executeQuery();
		int count = 0;
		while (rsInd.next()) {
			result.append(NL).append(rsInd.getString(3) +"."+ rsInd.getString(2)).append(NL);
			result.append(rsInd.getString(1)).append(NL);
			count++;
		}
		ps.close();
		rsInd.close();
		titel.append(listAnzahlViews(count));
		return titel.append(result);
	}
	

	@Override
	public String listUniqueConstraints(DatabaseMetaData meta, Connection conn, String catalog, String tableSchema,
			String tableName) throws SQLException {
		StringBuffer result = new StringBuffer();
		String sql = "SELECT con.contype FROM pg_catalog.pg_constraint con "
				+ "INNER JOIN pg_catalog.pg_class rel ON rel.oid = con.conrelid "
				+ "INNER JOIN pg_catalog.pg_namespace nsp ON nsp.oid = connamespace " + "WHERE nsp.nspname = ?  "
				+ "AND rel.relname = ? " + "AND con.conname = ? ";

		List<UniqueConstraint> ucs = getUniqueConstraints(meta, tableSchema, tableName);
		for (UniqueConstraint uc : ucs) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tableSchema);
			ps.setString(2, tableName);
			ps.setString(3, uc.name);
			ResultSet rsInd = ps.executeQuery();
			if (rsInd.next()) {

				String type = rsInd.getString(1);
				if (type.equals("u")) {
					result.append("\tCONSTRAINT " + uc.name + " UNIQUE (");
					StringBuffer indexIds = new StringBuffer();

					for (String col : uc.columns) {
						indexIds.append(col).append(",");

					}
					indexIds.append(")");
					result.append(indexIds.toString().replaceAll(",\\)", "\\)")).append(NL);
				}
			} else {
				System.err.println("error: no entr in catalog for this Unique Constraint");
			}
			ps.close();
			rsInd.close();
		}
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



}