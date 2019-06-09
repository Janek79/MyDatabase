package podatabase.tables;

import java.util.ArrayList;
import java.util.List;

public class Record {
	private String tableName;
	private List<Value> values;
	
	public Record(String tableName, List<Value> values) {
		this.tableName = tableName;
		this.values = values;
	}
	
	public Record(String tableName) {
		this(tableName, new ArrayList<>());
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<Value> getValues() {
		return values;
	}
	public void setValues(List<Value> values) {
		this.values = values;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(Value v: values) {
			builder.append("["+ v.getFieldName() + " : " + v.getValue() + "]");
		}
		return builder.toString();
	}
	
	public Value getValue(String fieldName) {
		for(Value v: this.getValues()) {
			if(v.getFieldName().equals(fieldName)) {
				return v;
			}
		}
		return null;
	}
	
	public void addValue(Value value) {
		if(getValue(value.getFieldName()) == null) {
			this.values.add(value);
		}
	}
	
}
