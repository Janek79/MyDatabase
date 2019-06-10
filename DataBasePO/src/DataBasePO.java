import java.io.File;
import java.util.List;

import podatabase.DatabaseTXT;
import podatabase.queries.Query;

public class DataBasePO {

	public static void main(String[] args) {

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

		//db.query().insert("Samochody").addValue("Id", 10).addValue("Marka", "Szewrolet").addValue("Model", "Nieznany").addNullValue("Rok produkcji").execute();
		
		db.query().select("Samochody").withField("Marka").whereIntFieldIsEqual("Id", 10).withField("Model").execute().forEach(System.out::println);
		
		//db.query().create("DwaID").addStringField("ID1").addStringField("ID2").id().execute();
		
	}

}
