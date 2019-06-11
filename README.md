# MyDatabase
Database created in pure Java

# Opis po polsku (polish description) 
Prezentowany projekt pozwala na tworzenie i korzystanie z niezabezpieczonych lub zabezpieczonych nazwą użytkownika i hasłem baz danych operujących na plikach tekstowych. 
Przy czym należy przyznać, że ochrona ta jest w zasadzie czysto iluzoryczna, więc nie należy pokładać w niej ufności :)

Punktem wejścia jest klasa DatabaseConnector. 
To właśnie dzięki niej i jej statycznym metodom możemy połączyć się z już istniejącą bazą lub utworzyć nową.
Obie metody są przeciążone w taki sposób, że istnieje możliwość wykonanie akcji z danymi uwierzytelniającymi (login i hasło) lub bez nich.
Daje to sposobność do stworzenia bazy danych zabezpieczonej lub uzyskanie łączności z takową.

Po poprawnym zastosowaniu metod z klasy DatabaseConnector otrzymamy obiekt implementujący interfejs Database, 
który aktualnie posiada jedną metodę [query() : QueryBuilder]. Za jej pomocą możemy otrzymać dostęp do budowniczego kwerend.

QueryBuilder zwraca natomiast, przy użyciu stosownych metod, obiekty, które są implementacjami interfejsu Query<T>. Ten natomiast nakazuje stworzenie metody wykonującej zapytanie [execute() : T].
W obecnej wersji można korzystać z kwerend takich jak: CreateQuery, InsertQuery, SelectQuery, DeleteQuery, TruncateQuery i DropQuery.
Każdy ze zwróconych obiektów jest już wykonywalną komendą, jednak użytkownik ma prawo pokusić się o dodanie zapytaniu dodatkowych właściwości.
  Przykład: 
(Database)db.query().select("Samochody").withField("Marka").withField("Model").whereIntFieldIsLowerThan(1970).execute();
Wynikiem tego programu będzie lista samochodów, które były produkowane przed 1970 rokiem, zaprezentowanych przez ich markę i model.

Każda z utworzonych kwerend korzysta z obiektu z interfejsu Repository, który to bezpośrednio modyfikuje bazę. 
Sama baza danych składa się z tabel, rekordów i ewentualnych informacji o użytkowniku. 
W Javie podane elementy reprezentowane są odpowiednio przez obiekty: Table, Record i User. 
Obiekt Table może posiadać obiekty klasy Field, które przechowują informacje o polach tabeli i ich właściwościach.
Podobnie w przypadku Recordu mamy do czynienia z listą obiektów Value zawierających wartości odpowiednich pól.
By przekształcać wymienione elementy z tekstu na obiekty w Javie i na odwrót, Repository posiłkuje się Adapterem.

