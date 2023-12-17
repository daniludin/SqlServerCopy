package ch.ludin.sqlservercopy.model;

import org.eclipse.swt.widgets.Shell;

public class DatabaseStructureReaderFactory {

	public DatabaseStructureReader getDatabaseStructureReader(Shell activeShell, String url) throws CustomSQLException {
		if (url.toLowerCase().contains("postgres")) {
			return new ReadDatabaseStructurePostgres(activeShell);
		}
		if (url.toLowerCase().contains("sqlserver")) {
			return new ReadDatabaseStructureSqlserver(activeShell);
		}
		throw new CustomSQLException("Database type for " + url + " not supported!");
	}
}
