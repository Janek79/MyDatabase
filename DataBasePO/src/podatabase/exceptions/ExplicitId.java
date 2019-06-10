package podatabase.exceptions;

public class ExplicitId extends RuntimeException {
	public ExplicitId() {
		super("Table cannot have more than 1 ID field");
	}
}
