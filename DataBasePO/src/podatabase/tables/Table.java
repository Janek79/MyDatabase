package podatabase.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Table {
	private String tableName;
	private List<Field> fields = new ArrayList<>();
	
	public Table(String tableName) {
		this(tableName, new ArrayList<>());
	}
	
	public Table(String tableName, List<Field> fields) {
		this.tableName = tableName;
		this.fields = fields;
	}

	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public List<Field> getFields() {
		return fields;
	}
	
	public Field getField(String fieldName) {
		
		for(Field f: this.getFields()) {
			if(f.getFieldName().equals(fieldName)) {
				return f;
			}
		}
		
		return null;
	}
	
	public void addField(Field field) {
		if(this.getField(field.getFieldName()) == null) {
			this.fields.add(field);
		}
	}
	
	public Field getIdField() {
		Optional<Field> op = this.getFields().stream().filter((f) -> f.isId()).findFirst();
		return op.isPresent() ? op.get() : null;
	}

	
}
