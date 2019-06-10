package podatabase.exceptions;

public class ValueMustBeUnique extends RuntimeException {
	public ValueMustBeUnique(String fieldName) {
		super("Value in field " + fieldName + " must be unique");
	}
}
