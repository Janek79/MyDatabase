package podatabase.iodata;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import podatabase.User;
import podatabase.exceptions.InvalidName;
import podatabase.tables.Field;
import podatabase.tables.Record;
import podatabase.tables.Table;
import podatabase.tables.Value;

public class DefaultStringAdapter implements Adapter<String> {

	private boolean containsImproperChars(String s) {
		return s.contains(";") || s.contains(",") || s.contains(":");
	}
	
	@Override
	public String tableToFormat(Table t) {
		if(containsImproperChars(t.getTableName())) {
			throw new InvalidName("Table name cannot contains characters like ':', ',' or ':'");
		}
		
		StringBuilder format = new StringBuilder();
		
		format.append("t:" + t.getTableName() + ":");
		for(Field f: t.getFields()) {
			if(containsImproperChars(f.getFieldName())) {
				throw new InvalidName("Field name " + f.getFieldName() + " cannot contains characters like ':', ',' or ':'");
			}
			String[] stringRecord = {f.getType().getTypeName(), f.getFieldName(), f.isId() ? "id" : "", !f.isNullable() ? "notNull" : "", f.isUnique() ? "unique" : ""};
			format.append(String.join(",", stringRecord) + ";");
		}
		
		return format.toString();
	}

	@Override
	public Table formatToTable(String format) {
		String[] split = format.split(":");
		
		String tableName = split[1];
		List<Field> fields = new ArrayList<>();
		
		for(String s: split[2].split(";")) {
			List<String> stringField = Arrays.asList(s.split(","));
			try {
				
				Class type = Class.forName(stringField.get(0));
				String fieldName = stringField.get(1);
				boolean isId = stringField.contains("id");
				boolean isNullable = !stringField.contains("notNull");
				boolean isUnique = stringField.contains("unique");
				
				Field field = new Field(fieldName, type, isNullable, isUnique, isId);
				fields.add(field);
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		Table table = new Table(tableName, fields);
		
		return table;
	}

	@Override
	public boolean isThisTable(String format) {
		return format.split(":")[0].equals("t");
	}

	@Override
	public String recordToFormat(Record r) {
		StringBuilder format = new StringBuilder();
		
		format.append("r:" + r.getTableName() + ":");
		for(Value v: r.getValues()) {
			if(containsImproperChars(v.getFieldName())) {
				throw new InvalidName("Field name " + v.getFieldName() + " cannot contains characters like ':', ',' or ':'");
			}
			format.append(v.getValue() + ";");
		}
		
		return format.toString();
	}

	@Override
	public Record formatToRecord(String format, Table t) {
		String[] split = format.split(":")[2].split(";");
		List<Field> fields = t.getFields();
		
		List<Value> values = new ArrayList<>();
		for(int i = 0; i < split.length; i++) {
			try {
				if(split[i].equals("null")) {
					values.add(new Value(fields.get(i).getFieldName(), null));
				} else {
					values.add(new Value(fields.get(i).getFieldName(), ((Class) fields.get(i).getType()).getDeclaredConstructor(String.class).newInstance(split[i])));
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return new Record(t.getTableName(), values);
	}

	@Override
	public boolean isThisRecord(String format) {
		return format.split(":")[0].equals("r");
	}

	@Override
	public boolean belongsToTable(String format, Table table) {
		return format.split(":")[1].equals(table.getTableName());
	}

	@Override
	public String userToFormat(User u) {
		return String.join(":", "u", u.getUsername(), u.getPassword());
	}

	@Override
	public User formatToUser(String format) {
		String[] userStrings = format.split(":");
		return new User(userStrings[1], userStrings[2]);
	}
	
	@Override
	public boolean isThisUser(String format) {
		return format.split(":")[0].equals("u");
	}
	
}
