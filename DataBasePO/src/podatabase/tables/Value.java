package podatabase.tables;

public class Value<T> {
	private String fieldName;
	private T value;
	
	public Value(String fieldName, T value) {
		this.fieldName = fieldName;
		this.value = value;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	
	
}
