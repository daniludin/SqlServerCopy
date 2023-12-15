package ch.ludin.dbcompare.model;

public interface DatabaseStructureReader {
	StringBuffer readDbStructure(String url, String username, String password) throws CustomSQLException;
}
