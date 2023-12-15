package ch.ludin.dbcompare.model;

import java.sql.SQLException;

public class CustomSQLException extends SQLException {

	private static final long serialVersionUID = 9142691084356508376L;
	private String errorMessage;

	public CustomSQLException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
