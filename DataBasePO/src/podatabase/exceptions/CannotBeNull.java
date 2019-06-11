package podatabase.exceptions;

public class CannotBeNull extends RuntimeException {
	public CannotBeNull(String sth) {
		super(sth + " cannot be null");
	}
}
