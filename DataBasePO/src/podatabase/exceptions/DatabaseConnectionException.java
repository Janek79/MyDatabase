package podatabase.exceptions;

public class DatabaseConnectionException extends RuntimeException {
	public DatabaseConnectionException(String msg) {
		super(msg);
	}
}
