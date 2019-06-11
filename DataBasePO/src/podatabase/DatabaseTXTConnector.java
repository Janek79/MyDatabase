package podatabase;

import java.io.File;
import java.io.IOException;

import podatabase.exceptions.DatabaseConnectionException;
import podatabase.exceptions.InvalidDataSource;
import podatabase.iodata.DefaultRepository;
import podatabase.iodata.Repository;
import podatabase.queries.QueryBuilder;

public class DatabaseTXTConnector {

	private DatabaseTXTConnector() {}
	
	public static boolean doesDatabaseExist(File file) {
		return file.exists();
	}

	//creating TXTdatabase without security
	public static DatabaseTXT createDatabase(File file) {
		if(file.exists()) {
			throw new DatabaseConnectionException("Such database already exists");
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new DatabaseTXT(file, null, new DefaultRepository(null, file));
		}
	}
	
	//creating TXTdatabase with security
	public static DatabaseTXT createDatabase(File file, String username, String password) {
		if(file.exists()) {
			throw new DatabaseConnectionException("Such database already exists");
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			User user = new User(username, password);
			Repository repository = new DefaultRepository(user, file);
			repository.saveUser(user);
			return new DatabaseTXT(file, user, repository);
		}
	}
	
	//connecting with database without security
	public static DatabaseTXT getDatabase(File file) {
		if(!file.exists()) {
			throw new DatabaseConnectionException("Database " + file.getAbsolutePath() + " doesn't exist");
		}
		Repository repository = new DefaultRepository(null, file);
		if(repository.isUserAuth()) {
			return new DatabaseTXT(file, null, repository);
		} else {
			throw new DatabaseConnectionException("This database requires username and password");
		}
		
	}
	
	public static DatabaseTXT getDatabase(File file, String username, String password) {
		if(!file.exists()) {
			throw new DatabaseConnectionException("Database " + file.getAbsolutePath() + " doesn't exist");
		}
		User user = new User(username, password);
		Repository repository = new DefaultRepository(user, file);
		if(repository.isUserAuth()) {
			return new DatabaseTXT(file, null, repository);
		} else {
			throw new DatabaseConnectionException("Invalid username or password");
		}
		
	}
	
	private static class DatabaseTXT implements Database {
		private QueryBuilder builder;
		private File dataSource;
		private Repository repository;
		private User user;

		public DatabaseTXT(File dataSource, User user, Repository repository) { // TODO Czy zawsze .txt?
			if (!dataSource.getName().endsWith(".txt")) {
				throw new InvalidDataSource("Data source should have .txt extension");
			}
			this.builder = new QueryBuilder(repository, dataSource);
			this.dataSource = dataSource;
			this.user = user;
		}

		public QueryBuilder query() {
			return builder;
		}

	}
}
