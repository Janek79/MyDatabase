package podatabase.exceptions;

public class TableAlreadyExistsException extends RuntimeException {
	public TableAlreadyExistsException() {
		super("Table already exists");
	}
}
