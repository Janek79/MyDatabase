import java.io.File;
import java.util.ArrayList;
import java.util.List;

import podatabase.Database;
import podatabase.DatabaseTXTConnector;
import podatabase.queries.Query;

public class DataBasePO {

	public static void main(String[] args) {

		File loc = new File("dataBase.txt");
		Database db;
		if(DatabaseTXTConnector.doesDatabaseExist(loc)) {
			db = DatabaseTXTConnector.getDatabase(loc, "user", "123");
		} else {
			db = DatabaseTXTConnector.createDatabase(loc, "user", "123");
		}

		//Przykładowe kwarendy
		Query ludzie = db.query().create("Ludzie")
			.addStringField("Imie")
			.addStringField("Nazwisko")
			.addIntField("Wiek");
		
		
		
		List<Query> insertLudzie = new ArrayList<>();
		
		String[] imiona = {"Maciej", "Ania", "Jerzy", "Marcin", "Kasia", "Sławek", "Waldek", "Beata", "Stan"};
		String[] nazwiska = {"Taylor", "Trwała", "Schneider", "Szyba", "Ukrop", "Nowak", "Kowalski", "Albatros", "Czarny"};
		Integer[] lata = {34, 21, 26, 26, 46, 22, 54, 28, 44};
		
		for(int i = 0; i < imiona.length; i++) {
			
			insertLudzie.add(db.query().insert("Ludzie")
					.addIntValue("Wiek", lata[i])
					.addStringValue("Imie", imiona[i])
					.addStringValue("Nazwisko", nazwiska[i]));
			
		}
		
		Query<List> selectAllLudzie = db.query()
				.select("Ludzie");
		
		Query<List> selectSomeLudzie = db.query()
				.select("Ludzie")
				.withField("Imie")
				.withField("Wiek")
				.whereIntFieldIsBiggerThan("Wiek", 25);
		
		Query deleteAllLudzie = db.query().truncate("Ludzie");
		Query dropLudzie = db.query().drop("Ludzie");
		
		
		Query samochody = db.query()
				.create("Samochody")
				.addIntField("Id").id()
				.addStringField("Model").notNull().unique()
				.addStringField("Marka").notNull()
				.addIntField("Rok produkcji");
		
		
		List<Query> insertSamochody = new ArrayList<>();
		
		Integer[] id = {1, 2, 3, 4, 5, 6, 7};
		String[] modele = {"Fiesta", "Octavia", "Focus", "Yaris", "M8", "6", "KA"};
		String[] marki = {"Ford", "Skoda", "Ford", "Toyota", "BMW", "Mazda", "Ford"};
		Integer[] lataP = {1932, 1922, 1453, 1410, 111, 2345, 1312};
		
		for(int i = 0; i < id.length; i++) {
			insertSamochody.add(db.query().insert("Samochody")
					.addIntValue("Rok produkcji", lataP[i])
					.addStringValue("Model", modele[i])
					.addStringValue("Marka", marki[i])
					.addIntValue("Id", id[i]));
		}

		
		Query<List> selectAllSamochody = db.query().select("Samochody").withField("Model");
		Query<List> selectSomeSamochody = db.query().select("Samochody").whereStringFieldIsEqual("Marka", "Ford").where("Id", (x) -> (Integer)x*(Integer)x < 40);
		
		Query deleteSomeSamochody = db.query().delete("Samochody").where("Rok produkcji", (x) -> (Integer)x < 1500);
		Query dropSamochody = db.query().drop("Samochody");
		
//		ludzie.execute();
//		samochody.execute();
//		
//		insertLudzie.forEach(Query::execute);
//		insertSamochody.forEach(Query::execute);
		
//		selectAllLudzie.execute().forEach(System.out::println);
//		selectSomeLudzie.execute().forEach(System.out::println);
		
//		deleteAllLudzie.execute();
//		selectAllLudzie.execute().forEach(System.out::println);
		
//		selectAllSamochody.execute().forEach(System.out::println);
//		selectSomeSamochody.execute().forEach(System.out::println);
		
//		deleteSomeSamochody.execute();
//		selectAllSamochody.execute().forEach(System.out::println);
		
//		dropLudzie.execute();
//		dropSamochody.execute();
	}

}
