package podatabase.iodata;

import java.util.List;
import java.util.Map;

import podatabase.queries.Condition;
import podatabase.tables.Record;
import podatabase.tables.Table;

public interface Repository<T> {
	public void saveTable(Table table, T source);
	public boolean doesTableExist(String tableName, T source);
	public Table getTable(String tableName, T source);
	
	public void saveRecord(Record record, T source);
	public List<Record> getAllRecordList(String tableName, T source);
	
	public void dropTable(String tableName, T source);
	public void deleteRecords(String tableName, Map<String, Condition> conditions, T source);
}
