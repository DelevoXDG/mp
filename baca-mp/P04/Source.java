// Maksim Zdobnikau - 2

import java.util.Scanner;

// Idea rozwiazania:
// Rozwiazania opera sie na przedstawione w tresci struktury danych:
// Jednostronna podwojnie wiazana lista cykliczna, z referencja na pierwszy element listy wagonow
// Lista pojedyncza z referencja na pierwszy element listy pociagow

// Zlozonosc:
// O(1) dla wszystkich operacji poza Display oraz Trains, nie liczac pomocniczych operacji, wyszykiwajacych pociag
// O(n) dla operacji Trains, Display
// Zlozonosc niektorych funkcji jest dodatkowo wyjasniona w komentarzach do tej funckji
// Section Todo
// [ ] add checks when inserting

// Section Struktury
class Car {					// tak zwany "Node" jednostronnej dwukierunkowej listy wagonow
	private String _name;	// nazwa wagonu
	private Car _next;		// referencja do nastepnego wagonu
	private Car _prev;		// referencja do poprzedniego wagonu

	public Car(String name, Car prev, Car next) { 	// Konstruktor standardowy wagonu	
		// Ustawia nazwe, nastepny wagon, poprzedni wagon
		this._name = name;
		this._next = next;
		this._prev = prev;
	}
	public Car(String name) {		// Konstruktor standardowy pierwszego wagonu dla danego pociagu
		// Poniewaz jest to jedyny element listy dwukierunkowej, next i prev wskazuju na ten sam wagon 
		this._name = name;
		this._next = this;
		this._prev = this;
	}
	// Gettery
	public String getName() {
		return _name;
	}
	public Car getPrev() {
		return _prev;
	}
	public Car getNext() {
		return _next;
	}
	// Settery
	public void hookNext(Car next) {
		_next = next;
	}
	public void hookPrev(Car prev) {
		_prev = prev;
	}

	// Metody
	public void reverseHooks() { // Zmiana kierunku danego elementu
		// Ustawiamy prev na poprzedni next, next na poprzedni prev
		Car tmp = _prev;
		_prev = _next;
		_next = tmp;
	}
}

class Train {
	// Jednostronna podwojnie wiazana lista cykliczna, z referencja na pierwszy element listy wagonow
	// oraz "node" listy pojedynczja wiazanej, reprezentujacej liste pociagow
	private String _name;	// 	nazwa pociagu
	private Train _next;	// 	referencja do nastepnego pociagu
	private Car _first;		//	referencja do pierwszego elementu listy wagonow		

	public Train(String trainName, String carName, Train next) {	// Konstruktor pociagu
		// Ustawia nowy pociag jako pierwszy w liscie reprezentujacej pociagi
		this._name = trainName;			// Ustawia nazwe tego pociagu
		this._first = new Car(carName);	// Ustawia pierwszy element listy wagonow, tworzac nowy wagon o nazwie carName
		this._next = next;
	}
	public boolean isEqual(String trainName) {	//Sprawdza, czy podana nazwa jest rowna nazwie danego wagonu
		return this._name.equals(trainName);
	}
	public boolean isSingleCar() {
		return this._first.getNext() == this._first && this.getLast().getPrev() == this._first;
	}
	// Gettery
	public String getName() {
		return _name;
	}
	public Train getNext() {
		return _next;
	}
	public Car getFirst() {
		return this._first;
	}
	public Car getLast() {
		// Note: zalezy od pierwszego!!!
		return this._first.getPrev();
	}
	public String getCarListStr() {
		// Metoda, zwracajaca string z elementow listy wagonow
		StringBuilder	carListString	= new StringBuilder(""); 	// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania nowych wagonow

		Car				curCar			= this._first;	// Aktualny wagon 
		Car				prevCar			= null;			// Wagon, dodany do napisu poprzednio 
		// Metoda polega na sprawzeniu, czy nastepny element danego pociagu wskazuje na wypisany poprzenio obiekt
		// Jezeli pociag jest odwrocony, next aktualnego wagonu wskazuje na wagon, wypisany poprzednio
		// Znamy to, poniważ pierwszy i ostatni wagon obroconego pociagu maja zamienione next i prev
		// A po wagonach pomiedzy pierwszy a ostatnim elementem przechodzimy po prevach, zatem next wskazuje na poprzednio wypisany wagon
		do {
			carListString.append(" ").append(curCar.getName());		// Dodajemy do napisu wejsciowego aktualny wagon
			// Iterujemy po nextach lub prevachw zaleznosci od tego, czy przechodzimy po elementach zwyklego lub odwroconego pociagu
			// Zgodnie z opisanym powyzej niezmiennikiem
			if (curCar.getNext() != prevCar) {
				prevCar = curCar;
				curCar = curCar.getNext();
			} else {
				prevCar = curCar;
				curCar = curCar.getPrev();
			}
		} while (curCar != this._first);	// Poki nie dojdziemy do pierwdzego elementa, ktory w cyklicznej liscie oznaczna, ze przeszlismy przez wszystki elementy

		return carListString.toString();	// Zwrazamy wynik
	}
	// Settery
	public void hookNext(Train next) {
		_next = next;
	}
	// Metody
	public void insertLastCar(String carName) {		// Wstawia nowy wagon na koniec listy cyklicznej 
		Car insertedCar = new Car(carName, this.getLast(), this._first);
		this.getLast().hookNext(insertedCar);
		this._first.hookPrev(insertedCar);
	}
	public void insertFirstCar(String carName) {  // Wstawia nowy wagon na poczatek listy cyklicznej 
		Car insertedCar = new Car(carName, this.getLast(), this._first);
		this.getLast().hookNext(insertedCar);
		this._first.hookPrev(insertedCar);
		this._first = insertedCar;
	}

	public void reverse() {
		// Poniewaz musimy odwrocic liste wagonow w O(1), zamieniamy kierunek next/prev dla pierwszego i ostatniego elementow listy
		// Pozniej to da nam mozliwosc przechodzic po nextach lub po prevach w zaleznosci od tego, czy mamy odwrocony pociag 
		Car last = this.getLast();
		last.reverseHooks();		// Zamiana next i prev dla ostatniego 
		this._first.reverseHooks();	// Zamiana next i prev dla pierwszego

		this._first = last;			// Ustawenie ostatniego elementu listy jako pierwszego, bo pociag teraz zaczyna sie od ostatniego elementu
	}
	public String deleteLastCar() {	// Usuwamy ostatni wagon
		Car	oldLast	= this.getLast();	// Ostatni wagon
		Car	newLast	= oldLast.getPrev();// Przedostatni wagon, ktory po usunieciu zostanie ostatnim

		if (oldLast.getPrev().getNext() != oldLast) {	// Sprawdzamy, czy ostatni wagon jest jest elementem pociagu odwroconego
			newLast.hookPrev(newLast.getNext());		// Wtedy modyfikujemy przedostatni wagon w taki sposob, zeby nadal wskazywal na to, ze nowy ostatni wagon jest jest elementem pociagu odwroconego
		}

		newLast.hookNext(_first);		// Naprawiamy cykliznosc, ustawiajac powiazanie obustronne pomiedzy predostatnim a pierwszym wagonem
		this._first.hookPrev(newLast);

		return oldLast.getName();	// Zwraca nazwe usunietego wagonu
	}
	public String deleteFirstCar() {
		Car	oldFirst	= this.getFirst();	// Pierwszy wagon
		Car	last		= this.getLast();	// Ostatni wagon
		Car	newFirst	= _first.getNext();	// Drugin wagon (zostanie pierwszym)

		if (oldFirst.getNext().getPrev() != oldFirst) {	// Sprawdzamy, czy pierwszy wagon jest jest elementem pociagu odwroconego
			newFirst.hookNext(newFirst.getPrev());		// Wtedy modyfikujemy drugi wagon w taki sposob, zeby nadal wskazywal na to, ze nowy pierwzsy wagon jest jest elementem pociagu odwroconego
		}
		// [x] case 1 car
		last.hookNext(newFirst); 	// Naprawiamy cykliznosc, ustawiajac powiazanie obustronne pomiedzy drugim a ostatnim wagonem
		newFirst.hookPrev(last);
		_first = newFirst;			// Ustawiamy referencje pierwszego elementu na nowy pierwszy element 
		return oldFirst.getName();// Zwraca nazwe usunietego wagonu
	}

}

// Section List
class TrainList {			// Lista pojedyncza, reprezentujaca pociagi, "Stancja"
	private Train _trains;	// referencja na pierwszy element listy pociagow

	public TrainList() {	// Konstruktor dla pustej stancji
		this._trains = null;
	}
	public TrainList(Train train) {	// Konstrukor, dodajacy pierwszy pociag
		this._trains = train;
	}
	public Train findTrain(String trainName) {
		// Funkcja, wyszukiwajaca pociag o podanej nazwie w jednym przejsciu po liscie
		for (Train curTrain = this._trains; curTrain != null; curTrain = curTrain.getNext()) { // Przechodzi przez wszystkie elementy listy pociagow
			if (curTrain.isEqual(trainName)) { 	// Jezeli znajdujemy odpowiedni pociag
				return curTrain;				// Zwracamy referencje do tego pociagu
			}
		}
		return null; // Jezeli nie udalo znalezc pociag o danej nazwie, zwracamy null
	}
	public Train[] findTwoTrainsAndTrainBefore(String tNameA, String tNameB) {
		// Funkcja, znajdujaca 
		// - pociag o nazwie tNameA
		// - pociag o nazwie tNameB
		// - pociag, poprzedni do pociaga o nazwie tNameB, poniewaz TrainList jest lista jednokierunkowa i potrzebujemy ten pociag, zeby usunac pociag o nazwie tNameB
		// w jednym przejsciu po liscie

		Train	trainA			= null;
		Train	trainBeforeB	= null;
		Train	trainB			= null;
		boolean	found			= false;

		if (this._trains != null && this._trains.isEqual(tNameB)) {	// trainB jest pierwszy
			trainBeforeB = null;			// Nie ma poprzedniego elementu listy, zaznaczamy to zwracajac null
			trainB = this._trains;			// trainB - referencja do pierwszego pociagu
		}
		for (Train curTrain = this._trains; // przechodzimy po wszystkich elementach listy pociagow
				curTrain != null && found == false; // Wyszykiwanie, poki nie znajdzimy pociag A oraz B lub skonczy sie lista
				curTrain = curTrain.getNext()) {
			if (curTrain.isEqual(tNameA)) {	// Sprawdzamy, czy aktualny pociag ma nazwe tNameA
				trainA = curTrain;
				if (trainB != null) { found = true; }	// Jezeli znalezlismy pociagi o obu nazwach, mozemy skonczyc wyskukiwanie
			}
			if (curTrain.getNext() != null && curTrain.getNext().isEqual(tNameB)) {	// Sprawdzamy, czy nastepujacy po aktualnym pociagu ma nazwe tNameB
				trainBeforeB = curTrain;
				trainB = curTrain.getNext();
				if (trainA != null) { found = true; }	// Jezeli znalezlismy pociagi o obu nazwach, mozemy skonczyc wyskukiwanie
			}
		}

		return new Train[] { trainA, trainBeforeB, trainB };	// Zwracamy tablice, zawierajaca opisane pociagi
	}

	public String New(String trainName, String carName) {	//O(1), nie liczac wyszukiwania pociagu
		// Wstawenie pociagu na poczatek listy, zeby spelnic zlozonosc O(1)

		if (this.findTrain(trainName) != null) {	// Jezeli juz istnieje pociag o podanej nazwie, zwracamy tekst odpowiedniego bledu
			return Error.getErrorStr(Error.trainExists, trainName);
		}
		this._trains = new Train(trainName, carName, this._trains);	// Ustawiamy nowy pociag jako pierwszy pociag w liscie pociagow

		return "";	// Nie ma bledow
	}
	public String InsertLast(String trainName, String carName) {	//O(1), nie liczac wyszukiwania pociagu
		Train curTrain = findTrain(trainName);

		if (curTrain == null) {		// Jezeli nie istnieje pociag o podanej nazwie, zwracamy tekst odpowiedniego bledu
			return Error.getErrorStr(Error.trainNotExists, trainName);
		}

		curTrain.insertLastCar(carName);	// Wowolujemy odpowiednia metode dla danego pociaga(listy wagonow)
		return "";		// Nie ma bledow
	}
	public String InsertFirst(String trainName, String carName) {	//O(1), nie liczac wyszukiwania pociagu
		Train curTrain = findTrain(trainName);

		if (curTrain == null) {		// Jezeli nie istnieje pociag o podanej nazwie, zwracamy tekst odpowiedniego bledu
			return Error.getErrorStr(Error.trainNotExists, trainName);
		}

		curTrain.insertFirstCar(carName);  // Wowolujemy odpowiednia metode dla danego pociaga(listy wagonow)
		return "";		// Nie ma bledow
	}

	public String GetTrainStr(String trainName) {		// O(n), musimy przejsc przez wszystkie pociagi
		Train curTrain = findTrain(trainName);
		if (curTrain == null) {		// Jezeli nie istnieje pociag o podanej nazwie, zwracamy tekst odpowiedniego bledu
			return Error.getErrorStr(Error.trainNotExists, trainName);
		}
		StringBuilder carList = new StringBuilder(curTrain.getName());		// Tworzenie obiektu klasy StringBuilder dla sprytnego tworzenia wynikowego napisy

		carList.append(":").append(curTrain.getCarListStr()).append("\n");	// Dodawanie do wyniku napisu z funkcji, zwracajacej napis, w ktorym wypisane po koleji elementy listy wagonow
		return carList.toString();											// Zwracaa wynikowy napis

	}
	public String Reverse(String trainName) {		//O(1), nie liczac wyszukiwania pociagu
		Train curTrain = findTrain(trainName);

		if (curTrain == null) {		// Jezeli nie istnieje pociag o podanej nazwie, zwracamy tekst odpowiedniego bledu
			return Error.getErrorStr(Error.trainNotExists, trainName);
		}
		curTrain.reverse();	// Wowolanie metody, owracajacej elementu pociagu O(1) + wyjasnienie w ciele funkcji

		return "";	// Nie ma bledow
	}
	public String Union(String tNameA, String tNameB) {	// Dodawanie wagonow pociagu o nazwie tNameB na koniec pociagu o nazwie tNameA
		Train[]	TrainArr		= findTwoTrainsAndTrainBefore(tNameA, tNameB);
		Train	trainA			= TrainArr[0];
		Train	trainBeforeB	= TrainArr[1];	// Bo biedziemy musieli usunac trainB
		Train	trainB			= TrainArr[2];
		if (trainA == null) {	// Jezeli nie istnieje pociag o podanej nazwie, zwracamy tekst odpowiedniego bledu
			return Error.getErrorStr(Error.trainNotExists, tNameA);
		}
		if (trainB == null) {	// Jezeli nie istnieje pociag o podanej nazwie, zwracamy tekst odpowiedniego bledu
			return Error.getErrorStr(Error.trainNotExists, tNameB);
		}

		Car	trainA_last	= trainA.getLast();				// Ostatni wagon pociagu trainA
		Car	trainB_last	= trainB.getLast();				// Ostatni wagon pociagu trainB
		trainA_last.hookNext(trainB.getFirst());		// Referencje next ostatniego elementa pociagu trainB ustawiamy na pierwszy element pociagu trainB 
		trainB.getFirst().hookPrev(trainA_last);		// Referencja prev pierwszego elementa pociagu trainB ustawiamy na ostatni element pociegu trainA
		trainA.getFirst().hookPrev(trainB_last);		// Naprawiamy cyklicznosc listy wagonow wzgledem prevow
		trainB_last.hookNext(trainA.getFirst());		// Naprawiamy cyklicznosc listy wagonow wzgledem nextow

		this.DeleteNext(trainBeforeB);					// Usuwamy pociag trainB
		return "";
	}

	public void DeleteNext(Train trainBefore) {			// Usuwa pociag, na ktory wskazuje referencja next podanego pociagu
		//O(1)
		if (trainBefore == null) {			// Przypadek, gdy usuwany element jest pierwszy ( nie ma "preva") 
			_trains = _trains.getNext();
			return;
		}
		trainBefore.hookNext(trainBefore.getNext().getNext());	// Ustawiamy next danego elementu, pomijajac usuwany elemnt
	}
	public String Trains() {					// Zwraca napis zawierajacy wypisane po koleji pociagi
		//O(n), musimy przezsc przez wszystki pociagi
		StringBuilder trainsSB = new StringBuilder("Trains:");		// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania nowych pociagow
		for (Train curTrain = _trains; curTrain != null; curTrain = curTrain.getNext()) { // Przedzimy przez wszystkie pociagi
			trainsSB.append(" ").append(curTrain.getName());
		}
		return trainsSB.append("\n").toString();	// Zwracamy wynik
	}
	public String delLast(String tNameA, String tNameB) {	// Usuwanie ostatniego wagonu z pociagu o nazwie tNameA, tworzenie nowego pociagu o nazwie tNameB
		Train[]	TrainArr		= findTwoTrainsAndTrainBefore(tNameB, tNameA);

		Train	trainBeforeA	= TrainArr[1];  // Bo biedziemy musieli usunac trainA
		Train	trainA			= TrainArr[2];
		Train	trainB			= TrainArr[0];

		if (trainB != null) {	// Jezeli istnieje juz pociag o podanej nazwie, zwracamy tekst odpowiedniego bledu
			return Error.getErrorStr(Error.trainExists, tNameB);
		}
		if (trainA == null) {	// Jezeli nie istnieje pociag o podanej nazwie, zwracamy tekst odpowiedniego bledu
			return Error.getErrorStr(Error.trainNotExists, tNameA);
		}

		if (trainA.isSingleCar()) {	// Przypadek, gdy mamy tylko jeden wagon w pociagu i musimy usunac caly pociag z listy
			String deletedName = trainA.getFirst().getName();
			this.DeleteNext(trainBeforeA);
			this._trains = new Train(tNameB, deletedName, this._trains);	// Tworzymy nowy pociag z wagonem o nazwie usuwanego
			// Usuwamy pociag trainA
			return "";	// Nie ma bledow
		}
		String deletedName = trainA.deleteLastCar();	// Zapisujemy nazwe usuwanego wagonu z pociagu o trainA
		this._trains = new Train(tNameB, deletedName, this._trains);				// Tworzymy nowy pociag z wagonem o nazwie usuwanego 
		return "";  // Nie ma bledow
	}
	public String delFirst(String tNameA, String tNameB) {
		Train[]	TrainArr		= findTwoTrainsAndTrainBefore(tNameB, tNameA);

		Train	trainBeforeA	= TrainArr[1];		// Bo biedziemy musieli usunac trainA
		Train	trainA			= TrainArr[2];
		Train	trainB			= TrainArr[0];

		if (trainB != null) {	// Jezeli istnieje juz pociag o podanej nazwie, zwracamy tekst odpowiedniego bledu
			return Error.getErrorStr(Error.trainExists, tNameB);
		}
		if (trainA == null) {	// Jezeli nie istnieje pociag o podanej nazwie, zwracamy tekst odpowiedniego bledu
			return Error.getErrorStr(Error.trainNotExists, tNameA);
		}

		if (trainA.isSingleCar()) {  // Przypadek, gdy mamy tylko jeden wagon w pociagu i musimy usunac caly pociag z listy
			String deletedName = trainA.getFirst().getName();
			this.DeleteNext(trainBeforeA);	// Usuwamy pociag trainA
			this._trains = new Train(tNameB, deletedName, this._trains);  // Tworzymy nowy pociag z wagonem o nazwie usuwanego
			return "";	// Nie ma bledow
		}
		String deletedName = trainA.deleteFirstCar();  // Zapisujemy nazwe usuwanego wagonu z pociagu o trainA
		this._trains = new Train(tNameB, deletedName, this._trains); 		// Tworzymy nowy pociag z wagonem o nazwie usuwanego 

		return "";						 	// Nie ma bledow
	}
}

enum Error { // Klasa bledu
	trainExists,	// Blad istenia pociagu o podanej nazwie
	trainNotExists;	// Blad nieistnienia pociagu o podanej nazwie

	public static String getErrorStr(Error errName, String trainName) { // Funkcja zwraca tekst odpowiedniego bledu dla pocigu o podanej nazwie
		switch (errName) {
			case trainExists:
				return "Train ".concat(trainName).concat(" already exists\n");
			case trainNotExists:
				return "Train ".concat(trainName).concat(" does not exist\n");
			default:

		}
		return "";
	}
}

public class Source {
	public static Scanner sc = new Scanner(System.in);	// Scanner do wczytywania danych wejsciowych

	public static void main(String[] args) {
		long test_count = 0;		// Liczba zestawow
		test_count = sc.nextLong();	// Wowolujemy liczbe zestawow
		StringBuilder output = new StringBuilder();	// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow funkcji
		while (test_count-- > 0) {	// Przechodzimy przez kazdy zestaw
			long		OP_count	= sc.nextLong();		// Liczba operacji

			TrainList	trainList	= new TrainList();		// Tworzenie obiektu klasy trainList, "stancji", "listy wszystkich pociagow"
			while (OP_count-- > 0) {
				String	OP_resultStr	= "";			// Wynik aktualnego polecenia
				String	OP				= sc.next(); // Wczytujemy kolejne polecenia i wykonujemy odpowiednie funkcje
				switch (OP) {
					case "New": {
						String	trainName	= sc.next();
						String	carName		= sc.next();

						OP_resultStr = trainList.New(trainName, carName);
						break;
					}
					case "InsertFirst": {
						String	trainName	= sc.next();
						String	carName		= sc.next();

						OP_resultStr = trainList.InsertFirst(trainName, carName);
						break;
					}
					case "InsertLast": {
						String	trainName	= sc.next();
						String	carName		= sc.next();

						OP_resultStr = trainList.InsertLast(trainName, carName);
						break;
					}
					case "Display": {
						String trainName = sc.next();

						OP_resultStr = trainList.GetTrainStr(trainName);
						break;
					}
					case "Trains": {

						OP_resultStr = trainList.Trains();
						break;
					}
					case "Reverse": {
						String trainName = sc.next();

						OP_resultStr = trainList.Reverse(trainName);
						break;
					}
					case "Union": {
						String	trainNameA	= sc.next();
						String	trainNameB	= sc.next();

						OP_resultStr = trainList.Union(trainNameA, trainNameB);
						break;
					}
					case "DelFirst": {
						String	trainNameA	= sc.next();
						String	trainNameB	= sc.next();

						OP_resultStr = trainList.delFirst(trainNameA, trainNameB);
						break;
					}
					case "DelLast":
						String trainNameA = sc.next();
						String trainNameB = sc.next();

						OP_resultStr = trainList.delLast(trainNameA, trainNameB);
						break;
				}
				// System.out.print(OP_resultStr);
				output.append(OP_resultStr);	// Dodajemy wynik aktualnego polecenia do napisu wyjsciowego
			}
		}
		System.out.print(output.toString()); // Wypisywanie wyniku wszyskich polecen
	}
}

// 2
// 24
// New T1 W1 
// InsertLast T1 W2
// InsertLast T1 W3
// InsertLast T1 W4
// New T2 R3
// InsertLast T2 R2
// InsertLast T2 R1
// Display T1
// Display T2
// Reverse T1
// Display T1
// Reverse T2
// New T3 A1
// Trains
// Union T1 T2
// InsertLast T3 A2
// InsertLast T3 A3
// Reverse T3
// Union T3 T1
// Display T3
// Reverse T3
// DelLast T3 A3
// Display T3
// Trains
// 17
// New T1 1
// InsertLast T1 2
// InsertLast T1 3
// New T2 6
// InsertLast T2 5
// InsertLast T2 4
// Display T1
// Display T2
// Reverse T2
// Union T1 T2
// DelLast T1 T3
// Display T1
// DelFirst T1 P
// DelFirst T1 M
// Trains
// Reverse T1
// Display T1

//test.00.out
// T1: W1 W2 W3 W4
// T2: R3 R2 R1
// T1: W4 W3 W2 W1
// Trains: T3 T2 T1
// T3: A3 A2 A1 W4 W3 W2 W1 R1 R2 R3
// T3: R3 R2 R1 W1 W2 W3 W4 A1 A2
// Trains: A3 T3
// T1: 1 2 3
// T2: 6 5 4
// T1: 1 2 3 4 5
// Trains: M P T3 T1
// T1: 5 4 3