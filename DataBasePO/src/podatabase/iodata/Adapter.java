package podatabase.iodata;

import podatabase.User;
import podatabase.tables.Record;
import podatabase.tables.Table;

public interface Adapter<T> {
	public T tableToFormat(Table t);
	public Table formatToTable(T format);
	public boolean isThisTable(T format);
	
	public T recordToFormat(Record r);
	public Record formatToRecord(T format, Table t);
	public boolean isThisRecord(T format);
	
	public boolean belongsToTable(T format, Table t);
	
	public T userToFormat(User u);
	public User formatToUser(T format);
	public boolean isThisUser(T format);
}
