package podatabase.queries;

import podatabase.iodata.Repository;

public class DropTableQuery<T> implements Query<Boolean>{

	private String tableName;
	
	private Repository rep;
	private T source;
	
	
	public DropTableQuery(String tableName, Repository rep, T source) {
		super();
		this.tableName = tableName;
		this.rep = rep;
		this.source = source;
	}



	@Override
	public Boolean execute() {
		rep.dropTable(tableName, source);
		return true;
	}

}
