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
		
		Repository<File> rep = new DefaultRepository();

//		rep.saveRecord(r1, loc);
//		
//		records.forEach(System.out::println);
		
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
		
		//List wynik = selector.execute();
		//wynik.forEach(System.out::println);
		
		//db.getQueryBuilder().drop("Ludzie").execute();
		
		//db.query().insert("LudziePop").addStringValue("Nazwisko", "Bezimienny2").execute();

		//rep.getAllRecordList("LudziePop", loc).forEach(System.out::println);

		selector.execute().forEach(System.out::println);
		
//		db.query().delete("LudziePop").where("Wiek", (v) -> (Integer)v == 26).execute();
		
		//q.execute();
		//insert.execute();
	}

}
