package podatabase.exceptions;

public class ValueCannotBeNull extends RuntimeException {
	public ValueCannotBeNull(String fieldName) {
		super("You have tried to insert null value into not nullable field " + fieldName);
	}
}
