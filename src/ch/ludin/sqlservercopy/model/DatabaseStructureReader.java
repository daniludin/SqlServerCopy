package ch.ludin.sqlservercopy.model;

public interface DatabaseStructureReader {
	StringBuffer readDbStructure(String url, String username, String password) throws CustomSQLException;
}
