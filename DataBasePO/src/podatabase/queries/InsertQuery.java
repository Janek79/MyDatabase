package podatabase.queries;

import java.io.File;
import java.util.List;

import podatabase.iodata.Repository;
import podatabase.tables.Record;
import podatabase.tables.Value;

public class InsertQuery<T> implements Query<Boolean> {

	private Record record;
	
	private Repository rep;
	private T source;
	
		
	public InsertQuery(String tableName, Repository rep, T source) {
		this.record = new Record(tableName);
		this.rep = rep;
		this.source = source;
	}

	public <V> InsertQuery addValue(String fieldName, V value) {
		Value val = new Value(fieldName, value);
		this.record.addValue(val);
		return this;
	}

	public InsertQuery addNullValue(String fieldName) {
		return addValue(fieldName, null);
	}
	
	public InsertQuery addIntValue(String fieldName, Integer value) {
		return addValue(fieldName, value);
	}
	
	public InsertQuery addStringValue(String fieldName, String value) {
		return addValue(fieldName, value);
	}
	

	@Override
	public Boolean execute() {
		
		rep.saveRecord(record);
		
		return true;
	}

}
