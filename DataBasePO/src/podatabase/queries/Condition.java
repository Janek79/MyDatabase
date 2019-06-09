package podatabase.queries;

import podatabase.tables.Value;

public interface Condition<V> {
	public boolean doesMeetCondition(V val);
}
