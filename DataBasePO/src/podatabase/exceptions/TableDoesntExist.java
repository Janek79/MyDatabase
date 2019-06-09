package podatabase.exceptions;

public class TableDoesntExist extends RuntimeException {
	public TableDoesntExist(String tableName) {
		super("Table " + tableName + " doesn't exist");
	}
}
