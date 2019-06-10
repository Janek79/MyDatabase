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
import java.util.stream.Collectors;

import podatabase.User;
import podatabase.exceptions.DatabaseConnectionException;
import podatabase.exceptions.InvalidValues;
import podatabase.exceptions.TableAlreadyExistsException;
import podatabase.exceptions.TableDoesntExist;
import podatabase.exceptions.ValueCannotBeNull;
import podatabase.exceptions.ValueMustBeUnique;
import podatabase.queries.Condition;
import podatabase.tables.Record;
import podatabase.tables.Table;
import podatabase.tables.Value;

public class DefaultRepository implements Repository<File> {

	private Adapter<String> adapter = new DefaultStringAdapter();
	private User user;
	private File source;
	
	public DefaultRepository(User user, File source) {
		this.user = user;
		this.source = source;
		if(!isUserAuth()) {
			throw new DatabaseConnectionException("Unauthorized access to repository");
		}
	}
	
	public boolean isUserAuth() {
		return this.getUser() == null || this.getUser().equals(this.user);
	}

	@Override
	public void saveTable(Table table) {
		try (FileWriter writer = new FileWriter(source, true);
				BufferedWriter bWriter = new BufferedWriter(writer);
				PrintWriter out = new PrintWriter(bWriter)) {

			if (!doesTableExist(table.getTableName())) {
				out.println(adapter.tableToFormat(table));
			} else {
				throw new TableAlreadyExistsException();
			}

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	@Override
	public Table getTable(String tableName) {

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
	public boolean doesTableExist(String tableName) {
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

	// for saveRecord method
	private boolean doesRecordExist(Table table, String fieldName, Object value) {

		List<Record> records = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(source))) {

			String line = reader.readLine();
			while (line != null) {
				if (adapter.isThisRecord(line)) {
					if (adapter.belongsToTable(line, table)) {
						Record r = adapter.formatToRecord(line, table);
						if (r.getValue(fieldName).getValue().equals(value)) {
							return true;
						}
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
	public void saveRecord(Record record) {

		Table table = getTable(record.getTableName());

		if (table == null) {
			throw new TableDoesntExist(record.getTableName());
		}

		// are inputed values correct (fields names)
		record.getValues().stream().map((v) -> v.getFieldName()).forEach((n) -> {

			// if user type in wrong field name
			if (table.getField(n) == null) {
				throw new InvalidValues("There aren't any field with name " + n + " in table " + table.getTableName());
			}

		});

		// are inputed values correct (nullable and unique)
		table.getFields().stream().map((f) -> f.getFieldName()).forEach((n) -> {

			// if field isn't nullable and user didn't input any value or input null
			if (!table.getField(n).isNullable()
					&& (record.getValue(n) == null || record.getValue(n).getValue() == null)) {
				throw new ValueCannotBeNull(n);
			}

			// if field is unique, user input not null value and record with such value
			// already exists
			if (table.getField(n).isUnique() && !(record.getValue(n) == null || record.getValue(n).getValue() == null)
					&& doesRecordExist(table, n, record.getValue(n).getValue())) {
				throw new ValueMustBeUnique(n);
			}

		});

		// sorting inputed values (in order like in table)
		List sortedValues = table.getFields().stream()
				.map((f) -> record.getValue(f.getFieldName()) != null ? record.getValue(f.getFieldName())
						: new Value(f.getFieldName(), null))
				.collect(Collectors.toList());

		Record orderedRecord = new Record(table.getTableName(), sortedValues);

		try (FileWriter writer = new FileWriter(source, true);
				BufferedWriter bWriter = new BufferedWriter(writer);
				PrintWriter out = new PrintWriter(bWriter)) {

			out.println(adapter.recordToFormat(orderedRecord));

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	@Override
	public List<Record> getAllRecordList(String tableName) {

		Table table = getTable(tableName);

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

	public List<Record> getRecordsList(String tableName, Map<String, Condition> conditions) {

		Table table = getTable(tableName);

		if (table == null) {
			throw new TableDoesntExist(tableName);
		}

		List<Record> records = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
			String line = reader.readLine();
			while (line != null) {
				if (adapter.isThisRecord(line) && adapter.belongsToTable(line, table)) {
					Record record = adapter.formatToRecord(line, table);
					if(conditions.isEmpty() || record.getValues().stream().allMatch((v) -> v.getValue() == null  || !conditions.containsKey(v.getFieldName()) || conditions.get(v.getFieldName()).doesMeetCondition(v.getValue()))) {
						records.add(record);
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

	public void dropTable(String tableName) {

		Table table = getTable(tableName);

		if (table == null) {
			throw new TableDoesntExist(tableName);
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
			StringBuffer text = new StringBuffer();

			String line;

			while ((line = reader.readLine()) != null) {
				if (!adapter.belongsToTable(line, table)) {
					text.append(line + "\n");
				}
			}

			try (FileOutputStream fileOut = new FileOutputStream(source)) {
				fileOut.write(text.toString().getBytes());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteRecords(String tableName, Map<String, Condition> conditions) {

		Table table = getTable(tableName);

		if (table == null) {
			throw new TableDoesntExist(tableName);
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
			StringBuffer text = new StringBuffer();

			String line;

			while ((line = reader.readLine()) != null) {
				if (adapter.isThisRecord(line) && adapter.belongsToTable(line, table)) {
					Record r = adapter.formatToRecord(line, table);
					if (!conditions.keySet().stream().allMatch((s) -> r.getValue(s).getValue() == null
							|| !conditions.get(s).doesMeetCondition(r.getValue(s).getValue()))) {
						text.append(line + "\n");
					}
				} else {
					text.append(line + "\n");
				}
			}

			try (FileOutputStream fileOut = new FileOutputStream(source)) {
				fileOut.write(text.toString().getBytes());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public User getUser() {
		try (BufferedReader reader = new BufferedReader(new FileReader(source))) {

			String line = reader.readLine();
			while (line != null) {
				if (adapter.isThisUser(line)) {
					return adapter.formatToUser(line);
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
	public boolean saveUser(User u) {

		try (FileWriter writer = new FileWriter(source, true);
				BufferedWriter bWriter = new BufferedWriter(writer);
				PrintWriter out = new PrintWriter(bWriter)) {

			out.println(adapter.userToFormat(u));

		} catch (IOException e) {

			e.printStackTrace();

		}
		
		return true;
	}
	
}
