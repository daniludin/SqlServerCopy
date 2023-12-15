package ch.ludin.dbcompare.model;

import java.sql.Connection;

public class ConnectionFactory {

	public static void createDriver(String url) throws CustomSQLException {
		String driverClassName = null;
		if (url.startsWith("jdbc:postgres")) {
			driverClassName = "org.postgresql.Driver";
			}
		if (url.startsWith("jdbc:jtds:sqlserver")) {
			driverClassName = "net.sourceforge.jtds.jdbc.Driver";
		}
		if (url.startsWith("jdbc:sqlserver")) {
			driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}
		
		if (driverClassName == null) {
			throw new CustomSQLException("Kein Driver für diese JDBC-Url gefunden. Möglich sind jdbc:postgres|jdbc:jtds:sqlserver|jdbc:sqlserver");
		}
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			CustomSQLException myEx = new CustomSQLException(e.getMessage());
			throw myEx;
		}
	}
}
