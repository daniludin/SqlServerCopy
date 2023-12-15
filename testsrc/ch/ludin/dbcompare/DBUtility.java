package ch.ludin.dbcompare;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtility {
    private static final String[] TYPES = {"TABLE", "VIEW"};
    public static void getTableMetadata(Connection jdbcConnection, String tableNamePattern, String schema, String catalog, boolean isQuoted) throws Exception {
            try {
                DatabaseMetaData meta = jdbcConnection.getMetaData();
                ResultSet rs = null;
                try {
                    if ( (isQuoted && meta.storesMixedCaseQuotedIdentifiers())) {
                        rs = meta.getTables(catalog, schema, tableNamePattern, TYPES);
                    } else if ( (isQuoted && meta.storesUpperCaseQuotedIdentifiers())
                        || (!isQuoted && meta.storesUpperCaseIdentifiers() )) {
                        rs = meta.getTables(
                        		toUpperCase(catalog),
                        		toUpperCase(schema),
                        		toUpperCase(tableNamePattern),
                                TYPES
                            );
                    }
                    else if ( (isQuoted && meta.storesLowerCaseQuotedIdentifiers())
                            || (!isQuoted && meta.storesLowerCaseIdentifiers() )) {
                        rs = meta.getTables( 
                                toLowerCase(catalog),
                                toLowerCase(schema),
                                toLowerCase(tableNamePattern),
                                TYPES 
                            );
                    }
                    else {
                        rs = meta.getTables(catalog, schema, tableNamePattern, TYPES);
                    }

                    while ( rs.next() ) {
                        String tableName = rs.getString("TABLE_NAME");
                        System.out.println("table = " + tableName);
                    }



                }
                finally {
                    if (rs!=null) rs.close();
                }
            }
            catch (SQLException sqlException) {
                // TODO 
                sqlException.printStackTrace();
            }

    }

    public static String toUpperCase(String s) {
    	if (s != null) {
			return s.toUpperCase();
		}
    	return null;
    }
    public static String toLowerCase(String s) {
    	if (s != null) {
			return s.toLowerCase();
		}
    	return null;
    }
    
    public static void main(String[] args) {
        Connection jdbcConnection;
        try {
            jdbcConnection = DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433/Northwind;useCursors=true", "sa", "sa");
            //jdbcConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/project_template", "postgres", "postgres");
            getTableMetadata(jdbcConnection, "%", null, null, false);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
