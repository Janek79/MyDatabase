package podatabase.queries;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
	
//	public CreateTableQuery addForeignKey(String name, String tableName) {
//		return this.addField(new Field(name, String.class));
//	}
	
//	private CreateTableQuery addField(Field field) {
//		if(this.table.getField(field.getFieldName()) != null) {
//			this.table.getFields().add(field);
//		}
//		
//		return this;
//	}

	public String getTableName() {
		return this.table.getTableName();
	}

	public CreateTableQuery setTableName(String tableName) {
		this.setTableName(tableName);
		return this;
	}

	public CreateTableQuery id() {
		currentField.setId(true);
		return this;
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
		rep.saveTable(table, source);
		return true;
	}
	
	
}
