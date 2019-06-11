package podatabase.tables;

import java.lang.reflect.Type;

public class Field {
	private String fieldName;
	private Type type;
	private boolean nullable = true;
	private boolean unique = false;
	private boolean id = false;
	
	public Field(String fieldName, Type type) {
		this.fieldName = fieldName;
		this.type = type;
	}
	

	public Field(String fieldName, Type type, boolean nullable, boolean unique, boolean id) {
		super();
		this.fieldName = fieldName;
		this.type = type;
		this.nullable = nullable;
		this.unique = unique;
		this.id = id;
	}



	@Override
	public int hashCode() {
		return fieldName.length();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		} else if (obj instanceof Field) {
			return this.fieldName.equals(((Field) obj).getFieldName());
		}
			
		return false;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
		if(id && nullable) {
			this.id = false;
		}
		
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
		if(id && !unique) {
			this.id = false;
		}
	}

	public boolean isId() {
		return id;
	}

	public void setId(boolean id) {
		this.id = id;
		if(id) {
			this.nullable = false;
			this.unique = true;
		}
	}
	
	
	
}
