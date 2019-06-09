package podatabase.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import podatabase.iodata.Repository;

public class DeleteRecordQuery<T> implements Query<Boolean> {

	private String tableName;
	private Map<String, Condition> conditions = new HashMap<>();;
	
	private Repository rep;
	private T source;

	
	public DeleteRecordQuery(String tableName, Repository rep,
			T source) {
		this.tableName = tableName;
		this.rep = rep;
		this.source = source;
	}
	
	public DeleteRecordQuery where(String fieldName, Condition condition){
		conditions.put(fieldName, condition);
		return this;
	}

	@Override
	public Boolean execute() {
		rep.deleteRecords(tableName, conditions, source);
		return true;
	}

}
