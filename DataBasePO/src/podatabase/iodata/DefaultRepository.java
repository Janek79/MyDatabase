package podatabase.iodata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import podatabase.exceptions.InvalidValues;
import podatabase.exceptions.TableAlreadyExistsException;
import podatabase.exceptions.TableDoesntExist;
import podatabase.queries.Condition;
import podatabase.tables.Field;
import podatabase.tables.Record;
import podatabase.tables.Table;
import podatabase.tables.Value;

public class DefaultRepository implements Repository<File> {

	Adapter<String> adapter = new DefaultStringAdapter();

	@Override
	public void saveTable(Table table, File source) {
		try (FileWriter writer = new FileWriter(source, true);
				BufferedWriter bWriter = new BufferedWriter(writer);
				PrintWriter out = new PrintWriter(bWriter)) {

			if (!doesTableExist(table.getTableName(), source)) {
				out.println(adapter.tableToFormat(table));
			} else {
				throw new TableAlreadyExistsException();
			}

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	@Override
	public Table getTable(String tableName, File source) {

		try (BufferedReader reader = new BufferedReader(new FileReader(source))) {

			String line = reader.readLine();
			while (line != null) {
				if (adapter.isThisTable(line)) {
					Table table = adapter.formatToTable(line);
					if (table.getTableName().equals(tableName)) {
						return table;
					}
				}
				line = reader.readLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean doesTableExist(String tableName, File source) {
		try (BufferedReader reader = new BufferedReader(new FileReader(source))) {

			String line = reader.readLine();
			while (line != null) {
				if (adapter.isThisTable(line)) {
					if (adapter.formatToTable(line).getTableName().equals(tableName)) {
						return true;
					}
				}
				line = reader.readLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void saveRecord(Record record, File source) {

		Table table = getTable(record.getTableName(), source);

		if (table == null) {
			throw new TableDoesntExist(record.getTableName());
		}

		List<Value> orderedValues = new ArrayList<>();
		for (Field f : table.getFields()) {
			boolean inserted = false;

			for (Value v : record.getValues()) {
				if (v.getValue() != null) {
					if (f.getFieldName().equals(v.getFieldName()) && f.getType().equals(v.getValue().getClass())) {
						orderedValues.add(v);
						inserted = true;
						break;
					}
				}
			}

			if (!inserted) {
				if (f.isNullable()) {
					orderedValues.add(new Value(f.getFieldName(), null)); // TODO niebezpiecznie tu
				} else {
					throw new InvalidValues("Invalid values to save");
				}
			}
		}

		Record orderedRecord = new Record(table.getTableName(), orderedValues);

		try (FileWriter writer = new FileWriter(source, true);
				BufferedWriter bWriter = new BufferedWriter(writer);
				PrintWriter out = new PrintWriter(bWriter)) {

			out.println(adapter.recordToFormat(orderedRecord));

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	@Override
	public List<Record> getAllRecordList(String tableName, File source) {

		Table table = getTable(tableName, source);

		if (table == null) {
			throw new TableDoesntExist(tableName);
		}

		List<Record> records = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(source))) {

			String line = reader.readLine();
			while (line != null) {
				if (adapter.isThisRecord(line)) {
					if (adapter.belongsToTable(line, table)) {
						records.add(adapter.formatToRecord(line, table));
					}
				}
				line = reader.readLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return records;
	}
	
	public void dropTable(String tableName, File source) {
		System.out.println(tableName);
		
		Table table = getTable(tableName, source);

		if (table == null) {
			throw new TableDoesntExist(tableName);
		}
		
		
		try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
			StringBuffer text = new StringBuffer();
			
			String line;
			
			while ((line = reader.readLine()) != null) {
				if(!adapter.belongsToTable(line, table)) {
					text.append(line + "\n");
				}
			}
						
			try(FileOutputStream fileOut = new FileOutputStream(source)){
				fileOut.write(text.toString().getBytes());
			}		

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void deleteRecords(String tableName, Map<String, Condition> conditions, File source) {
		
		Table table = getTable(tableName, source);

		if (table == null) {
			throw new TableDoesntExist(tableName);
		}
		
		
		try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
			StringBuffer text = new StringBuffer();
			
			String line;
			
			while ((line = reader.readLine()) != null) {
				if(adapter.isThisRecord(line) && adapter.belongsToTable(line, table)) {
					Record r = adapter.formatToRecord(line, table);
					if(!conditions.keySet().stream().allMatch((s) -> r.getValue(s).getValue() == null || conditions.get(s).doesMeetCondition(r.getValue(s).getValue()))) {
						text.append(line + "\n");
					}
				} else {
					text.append(line + "\n");
				}
			}
			
			try(FileOutputStream fileOut = new FileOutputStream(source)){
				fileOut.write(text.toString().getBytes());
			}		

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
