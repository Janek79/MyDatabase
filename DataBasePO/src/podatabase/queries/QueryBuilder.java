package podatabase.queries;

import podatabase.iodata.Repository;

public class QueryBuilder<T> {
	
	private Repository<T> rep;
	private T source;	
	
	public QueryBuilder(Repository<T> rep, T source) {
		this.rep = rep;
		this.source = source;
	}

	public CreateTableQuery create(String tableName) {
		return new CreateTableQuery(tableName, rep, source);
	}
	
//	public SelectQuery select(String tableName, String... fields) {
//		return new SelectQuery(tableName, fields);
//	}
	
	public SelectQuery select(String tableName) {
		return new SelectQuery(tableName, rep, source);
	}
	
	public InsertQuery insert(String tableName) {
		return new InsertQuery(tableName, rep, source);
	}
	
	public DeleteRecordQuery delete(String tableName) {
		return new DeleteRecordQuery(tableName, rep, source);
	}
	
	public Object truncate() {
		return null;
	}
	
	public DropTableQuery drop(String tableName) {
		return new DropTableQuery(tableName, rep, source);
	}

}
