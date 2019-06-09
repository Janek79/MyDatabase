package podatabase.tables;

import java.lang.reflect.Type;

public class ForeignKeyField extends Field{

	public ForeignKeyField(String fieldName, Type type) {
		super(fieldName, type);
	}

}
