package podatabase.iodata;

import java.util.List;
import java.util.Map;

import podatabase.User;
import podatabase.queries.Condition;
import podatabase.tables.Record;
import podatabase.tables.Table;

public interface Repository<T> {
	public void saveTable(Table table);
	public boolean doesTableExist(String tableName);
	public Table getTable(String tableName);
	
	public void saveRecord(Record record);
	public List<Record> getAllRecordList(String tableName);
	public List<Record> getRecordsList(String tableName, Map<String, Condition> condition);
	
	public void dropTable(String tableName);
	public void deleteRecords(String tableName, Map<String, Condition> conditions);
	
	public User getUser();
	public boolean saveUser(User u);
	public boolean isUserAuth();
}
