package podatabase.queries;

public interface Condition<V> {
	public boolean doesMeetCondition(V val);
}
