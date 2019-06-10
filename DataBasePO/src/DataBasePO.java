import java.io.File;
import java.util.ArrayList;
import java.util.List;

import podatabase.DatabaseTXT;
import podatabase.iodata.DefaultRepository;
import podatabase.iodata.DefaultStringAdapter;
import podatabase.iodata.Repository;
import podatabase.queries.Condition;
import podatabase.queries.Query;
import podatabase.tables.Field;
import podatabase.tables.Record;
import podatabase.tables.Table;
import podatabase.tables.Value;

public class DataBasePO {

	public static void main(String[] args) {
		DefaultStringAdapter adapter = new DefaultStringAdapter();

		File loc = new File("dataBase.txt");
		DatabaseTXT db = new DatabaseTXT(loc);
		
		
		Query q = db.query().create("LudziePop")
			.addStringField("Imie")
			.addStringField("Nazwisko")
			.addIntField("Wiek").id();
		
//		String[] imiona = {"Maciej", "Jerzy", "Marcin", "Sławek", "Waldek", "Stan"};
//		String[] nazwiska = {"Taylor", "Schneider", "Szyba", "Nowak", "Kowalski", "Czarny"};
//		Integer[] lata = {34, 21, 26, 26, 46, 22};
//		
//		for(int i = 0; i < imiona.length; i++) {
//			db.getBuilder().insert("LudziePop")
//					.addIntValue("Wiek", lata[i])
//					.addStringValue("Imie", imiona[i])
//					.addStringValue("Nazwisko", nazwiska[i]).execute();
//		}
		
		Query<List> selector = db.query()
				.select("LudziePop")
				.withField("Imie")
				.withField("Wiek")
				.whereIntFieldIsBiggerThan("Wiek", 20);
		
		
		Query samochody = db.query().create("Samochody").addIntField("Id").id().addStringField("Model").notNull().addStringField("Marka").notNull().unique().addIntField("Rok produkcji");
		
		
//		Integer[] id = {1, 2, 3, 4, 5, 6};
//		String[] modele = {"Fiesta", "Octavia", "Focus", "Yaris", "M8", "6"};
//		String[] marki = {"Ford", "Skoda", "Ford", "Toyota", "BMW", "Mazda"};
//		Integer[] lata = {1932, 1922, 1453, 1410, 111, 2345};
//		
//		for(int i = 0; i < id.length; i++) {
//			db.query().insert("Samochody")
//					.addIntValue("Rok produkcji", lata[i])
//					.addStringValue("Model", modele[i])
//					.addStringValue("Marka", marki[i])
//					.addIntValue("Id", id[i]).execute();
//		}
		
		db.query().select("Samochody").withField("Model").withField("Rok produkcji").whereIntFieldIsBiggerThan("Rok produkcji", 1000).execute().forEach(System.out::println);

	}

}
