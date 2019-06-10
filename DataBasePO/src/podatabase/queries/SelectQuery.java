package podatabase.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import podatabase.iodata.Repository;
import podatabase.tables.Record;

public class SelectQuery<T> implements Query<List> {
	
	private String tableName;
	private List<String> fields = new ArrayList<>();
	private Map<String, Condition> conditions = new HashMap<>();
	
	private Repository rep;
	private T source;
	
	public SelectQuery(String tableName, Repository<T> rep, T source) { //, String... fields
		this.tableName = tableName;
		this.rep = rep;
		this.source = source;
		//this.fields.addAll(Arrays.asList(fields));
	}
	
	public SelectQuery where(String fieldName, Condition condition){
		conditions.put(fieldName, condition);
		return this;
	}
	
	public SelectQuery whereIntFieldIsBiggerThan(String fieldName, Integer x) {
		return this.where(fieldName, (v) -> (Integer)v > x);
	}
	
	public SelectQuery whereIntFieldIsLowerThan(String fieldName, Integer x) {
		return this.where(fieldName, (v) -> (Integer)v < x);
	}
	
	public SelectQuery whereIntFieldIsEqual(String fieldName, Integer x) {
		return this.where(fieldName, (v) -> ((Integer)v).equals(x));
	}
	
	public SelectQuery whereStringFieldIsEqual(String fieldName, String x) {
		return this.where(fieldName, (v) -> ((String)v).equals(x));
	}
	
	public SelectQuery withField(String fieldName) {
		fields.add(fieldName);
		return this;
	}
	
	@Override
	public List<Record> execute() {
//		List<Record> records = rep.getAllRecordList(tableName, source);
//		
//		if(records.isEmpty()) {
//			return records;
//		}
//		
//		List<String> notSelectedFields = records.get(0).getValues()
//				.stream()
//				.map((v) -> v.getFieldName())
//				.filter((n) -> !fields.contains(n))
//				.collect(Collectors.toList());
//		
//		
//		List<Record> results = records.stream()
//				.filter((r) -> conditions.keySet().stream()
//						.allMatch((n) -> r.getValue(n).getValue() != null && conditions.get(n).doesMeetCondition(r.getValue(n).getValue())))
//				.collect(Collectors.toList());
//		
//		if(!fields.isEmpty()) {
//			results.forEach((r) -> notSelectedFields.forEach((f) -> r.getValues().remove(r.getValue(f))));
//		}
		
		//return records;
		
		return !fields.isEmpty() ? 
				(List<Record>) rep.getRecordsList(tableName, conditions, source).stream()
				.map((r) -> new Record(tableName, ((Record) r).getValues().stream().filter((v) -> fields.contains(v.getFieldName())).collect(Collectors.toList())))
				.collect(Collectors.toList()) : 
					rep.getRecordsList(tableName, conditions, source);
	}

	
	
}
