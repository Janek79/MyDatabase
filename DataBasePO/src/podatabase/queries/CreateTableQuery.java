package podatabase.queries;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import podatabase.exceptions.ExplicitId;
import podatabase.iodata.DefaultRepository;
import podatabase.iodata.Repository;
import podatabase.tables.Field;
import podatabase.tables.Table;

public class CreateTableQuery<T> implements Query<Boolean>{
	private Table table;
	private Field currentField;
	
	private Repository rep;
	private T source;
	
	public CreateTableQuery(String tableName, Repository<T> rep, T source) {
		this.table = new Table(tableName);
		this.rep = rep;
		this.source = source;
	}

	public CreateTableQuery addField(String name, Type type) {
		Field field = new Field(name, type);
		this.currentField = field;
		this.table.addField(field);
		return this;
	}
	
	public CreateTableQuery addIntField(String name) {
		return addField(name, Integer.class);
	}
	
	public CreateTableQuery addStringField(String name) {
		return addField(name, String.class);
	}
	
	public String getTableName() {
		return this.table.getTableName();
	}

	public CreateTableQuery setTableName(String tableName) {
		this.setTableName(tableName);
		return this;
	}

	public CreateTableQuery id() {
		if(table.getIdField() == null) {
			currentField.setId(true);
			return this;
		} else {
			throw new ExplicitId();
		}
	}
	
	public CreateTableQuery notNull() {
		currentField.setNullable(false);
		return this;
	}
	
	public CreateTableQuery unique() {
		currentField.setUnique(true);
		return this;
	}

	@Override
	public Boolean execute() {
		rep.saveTable(table);
		return true;
	}
	
	
}
