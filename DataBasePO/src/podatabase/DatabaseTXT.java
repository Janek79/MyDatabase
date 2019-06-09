package podatabase;

import java.io.File;

import podatabase.exceptions.InvalidDataSource;
import podatabase.iodata.DefaultRepository;
import podatabase.iodata.Repository;
import podatabase.queries.QueryBuilder;

public class DatabaseTXT {
	private QueryBuilder builder;
	private File dataSource;
	private Repository repository = new DefaultRepository();
	
	public DatabaseTXT(File dataSource) { //TODO Czy zawsze .txt?
		if(!dataSource.getName().endsWith(".txt")) {
			throw new InvalidDataSource("Data source should have .txt extension");
		}
		this.builder = new QueryBuilder(repository,  dataSource);
		this.dataSource = dataSource; 
	}

	public QueryBuilder query() {
		return builder;
	}
	
}
