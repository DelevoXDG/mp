// Maksim Zdobnikau - 2

import java.util.Scanner;

// Wyjasnenie zlozonosci

class Helpers {			// Klasa zaweira funkcje pomocnicze
	public static <T> void swap(T[] arr, final int A, final int B) {
		// zamiana elementow o podanych indeksach mejscami w tablicy  
		T temp = arr[A];
		arr[A] = arr[B];
		arr[B] = temp;
	}
}

class Song {					// Klasa plyty
	private String[] dataArr;	// Obiekty zawieraja metadane aktualnej plyty
	private boolean nextR;		// I indykator, czy musimy posortowac podablice [... nextR-1 nextR], konczanca na nextR

	@Override public String toString() {				// Zwraca sformatowane metadane plyty
		StringBuilder	result	= new StringBuilder(""); //Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
		int				N		= dataArr.length;		// Ilosc metadanych
		for (int i = 0; i < N - 1; i++) {
			result.append(dataArr[i]).append(",");		// Dodajemy przecinki miedzy kolejnymi metdanymi
		}
		if (N >= 1) {
			result.append(dataArr[N - 1]);
		}
		return result.toString(); // Zwaracamy wynik
	}

	public boolean isNextR() {
		return nextR;			// Zwraca, czy dana plyta jest ostatnia plyta w podtablice, ktora musimy posortowac
	}

	public void toggleNextR() {	// Odwrocenie stanu indefikatora nextR
		this.nextR = !this.nextR;
	}

	Song(final String[] fields) {		// Konstruktor plyty 
		this.dataArr = fields;	// Ustawenie przekazanych metadanych
		nextR = false;			// Domyslnie ne musimy sortowac podtablice, ostatnim elementem ktorej jest aktualna plyta 
	}

	public String getData(final int i) { // Zwraca metadane o podanym indeksie
		return this.dataArr[i];
	}

	public void setFields(final String[] fields) {
		this.dataArr = fields;				// Ustawenie metadanych
	}
}

class musicCollection {			// Klasa reprezentujaca kolekcje plyt Pana Melomana
	private String[] labels;	// Nazwy kolumn
	private Song[] songs;		// Tablica zawierajaca obiekty piosenek
	private Order order;		// Porzadek, zgodnie z ktorym jest posortowana tablica
	private int sortBy;			// Indkes kolumny, po ktorej musimy posortowac metadane

	public musicCollection(final String[] labels, final Song[] songs, final int sortBy, final int orderNo) {
		// Konstruktor, ustawia wszystkie pola klasy
		this.labels = labels;
		this.songs = songs;
		this.sortBy = sortBy;
		this.order = findOrder(orderNo); 	// Znalezenie porzadku dla podanej kolumny i identyfikatora porzadku
		this.quickSort();					// Wywolanie iteracyjnej funkcji quicksort
	}
	private static boolean isNaturalNum(String str) {		// Zwraca, czy napis jest liczba naturalna
		for (char sym : str.toCharArray()) {
			if (Character.isDigit(sym) == false) {
				return false;						// Jezeli jest symbol, ktory nie jest cyfra, to napis nie jest liczba naturalna
			}
		}
		return true;								// Jest liczba naturalna
	}

	private Order findOrder(int orderNo) {	// Zwraca klase porzadku dla danego typu danych w kolumnie i przekazanego orderNo
		// orderNo == 1 zwykla kolejnosc, orderNo == -1 odwrotna kolejnosc
		if (songs.length == 0) {			// Sprawzamy, czy kolumna nie jest pusta
			return null;
		}
		if (isNaturalNum(songs[0].getData(sortBy))) {	// Spawdzamy, czy metadane w danej kolumnie (sortBy) jest lizba naturalna
			switch (orderNo) {
				case 1:
					return new NormalNum();		// Zwykla kolejnosc, liczby naturalne
				case -1:
					return new ReverseNum();	// Odwrotna kolejnosc, liczba naturalna
				default:
					throw new IllegalArgumentException("Undefined order");
			}
		} else {
			switch (orderNo) {
				case 1:
					return new NormalStr();		// Zwykla kolejnosc, napis
				case -1:
					return new ReverseStr();	// Odwrotna kolejnosc, napis
				default:
					throw new IllegalArgumentException("Undefined order");
			}
		}
	}

	public static interface Order {				// Interface porzadku, zgodnie z ktorym trzeba posortowac metadane
		public boolean isBigger(String a, String b);

		public boolean isBigger(Song A, Song B, int sortBy);		// Ma funkcje porownywania, rozna dla kazdego porzadku, sprawdza czy metadana o kolumnie sortBy plyty A jest wieksza od metadanej plyty B w sensie porzadku Order
	}

	public static class NormalStr implements Order {			// Zwykla kolejnosc, napis
		public boolean isBigger(String A, String B) {
			return A.compareTo(B) > 0;
		}
		public boolean isBigger(Song A, Song B, int sortBy) {
			return A.getData(sortBy).compareTo(B.getData(sortBy)) > 0;						// Porownyanie napisow leksykograficznie
		}
	}

	public static class ReverseStr implements Order {		// Odwrotna kolejnosc, napis
		public boolean isBigger(String A, String B) {
			return A.compareTo(B) < 0;
		}
		public boolean isBigger(Song A, Song B, int sortBy) {
			return A.getData(sortBy).compareTo(B.getData(sortBy)) < 0;						// Porownyanie napisow leksykograficznie
		}
	}
	public static class NormalNum implements Order {		// Zwykla kolejnosc, liczba naturalna
		public boolean isBigger(String A, String B) {
			return Integer.parseInt(A) > Integer.parseInt(B);
		}
		public boolean isBigger(Song A, Song B, int sortBy) {
			return Integer.parseInt(A.getData(sortBy)) > Integer.parseInt(B.getData(sortBy));	// Zparsowanie metadanych i porownyanie
		}
	}
	public static class ReverseNum implements Order {		// Odwrotna kolejnosc, liczba naturalna
		public boolean isBigger(String A, String B) {
			return Integer.parseInt(A) < Integer.parseInt(B);
		}
		public boolean isBigger(Song A, Song B, int sortBy) {
			return Integer.parseInt(A.getData(sortBy)) < Integer.parseInt(B.getData(sortBy));	// Zparsowanie metadanych i porownyanie
		}
	}
	private int findR(int L) {		// Szukamy ostatni element podtablicy zaczynajac od R
		int i = L - 1;
		while (++i < songs.length) {
			if (songs[i].isNextR() == true) { 	// Sprawdzenie czy element o indeksie i jest zaznaczony
				break;
			}
		}
		songs[i].toggleNextR();	// Zwracamy indeks i odznaczamy element, podtablica konczaca na i juz zostanie posortowana 
		return i;
	}
	private void swapSongs(final int A, final int B) {
		Helpers.swap(this.songs, A, B);		// zamiana elementow o podanych indeksach mejscami w tablicy plyt  
	}

	public int partition(final int L, final int R) { 			// Partition Hoare
		int		i		= L - 1;					// Indeks, ktory przeszukuje indeks dla pivot
		int		j		= R;						// Prawy indeks, sprawdzajacy kolejnosc elementow
		Song	pivot	= songs[R];

		do {
			while (order.isBigger(pivot, songs[++i], sortBy)) {}
			// po wyjsciu z petli i-ty element jest wiekszy badz rowny od pivot
			while (j > L && order.isBigger(songs[--j], pivot, sortBy)) {} // j=R-1 po --j
			// po wysciu z petli j-ty element jest rowny elementowi o indeksie L lub jest mniejszy badz rowny od pivot
			if (i < j) {		// w przypadku gdy zostala zmieniona kolejnosc indeksow
				swapSongs(i, j); // zamieniamy i-ty i j-ty elementy 	
			}
		} while (i < j);		// Petla dziala, poki kolejnosc indeksow jest poprawna

		swapSongs(i, R); 	// zamieniamy elementy o indeksach i i j
		return i; 			// zwracamy polozenie elementu pivot
	}
	void selectionSort(final int L, final int R) {	// Sortowanie selectionSort dla podtablic mniejszych badz rownych od 5
		for (int i = L; i < R; i++) {				// Przechodzimy przez wszystkie elementy
			int min = i;
			for (int j = i + 1; j < R + 1; j++) {	// Szukamy element mniejszy od aktualnego sposrod elementow podtablicy [i+1 ... R] 
				if (order.isBigger(songs[min], songs[j], sortBy)) {	// porownywanie elementow w sensie porzadku Order
					min = j;										// Zmiana namnieszego elementu
				}
			}
			swapSongs(i, min);	// Ustawenie najmniejszego elementu na miejsce i 
		}
	}

	public void quickSort() {					// Implementacja quicksort iteracyjna bez stosu
		int		L			= -1;				// Indeks lewego elementa podtablicy aktualnego "wywolania" quickSort 
		int		curR		= songs.length - 1;	// Indeks prawego elementa podtablicy aktualnego "wywolania quickSort" aktualnej  	

		boolean	firstCall	= true;				// Zmienna okreslajaca czy jestesmy w pierwszym "wywolaniu" QuickSort
		int		toSort		= 1;				// Liczba wywolan quicksort, ktory jeszcze musimy wykonac

		songs[curR].toggleNextR();
		do {
			int curSize = curR - L + 1;			// Rozmiar aktualnej podtablicy 

			// if (firstCall == true) {			// Jezeli jestesmy w pierwszym wywolaniu, nie musimy wyszukiwac rozmiar tablicy
			// 	firstCall = false;
			// 	toSort--;
			// } else {
			do {
				L++;
				curR = findR(L);
				toSort--;
				curSize = curR - L + 1;
			} while (curSize <= 1 && toSort > 0);
			// }

			if (curSize > 1) {
				if (curSize <= 5) {
					selectionSort(L, curR);
					L = curR;
				} else {
					while (L < curR) {
						int part = partition(L, curR);
						songs[curR].toggleNextR();
						curR = part - 1;
						toSort++;
					}
				}
			}
		} while (toSort > 0);
	}

	@Override public String toString() {				// Zwraca sformatowany wynik tasowania i znalezenia prefiksu
		StringBuilder	result	= new StringBuilder(""); //Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
		int				N		= labels.length;
		for (int i = 0; i < N - 1; i++) {
			result.append(labels[i]).append(",");
		}
		if (N >= 1) {
			result.append(labels[N - 1]);
		}
		result.append("\n");
		int songCount = songs.length;
		for (int i = 0; i < songCount; i++) {
			result.append(songs[i]).append("\n");
		}

		return result.toString();	// zwracamy caly wynik
	}
}

public class Source {
	public static void bringForward(String[] arr, int sortByCol) {
		for (int j = sortByCol; j >= 1; j--) {
			Helpers.swap(arr, j, j - 1);
		}
	}

	public static Scanner sc = new Scanner(System.in);			// Scanner do wczytywania danych wejsciowych

	public static void main(String[] args) {

		StringBuilder	output		= new StringBuilder(""); 	 	// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
		int				test_count	= Integer.parseInt(sc.nextLine());				// Liczba zestawow testowych
		while (test_count-- > 0) {	 							// Przechodzimy przez kazdy zestaw
			String		paramsStr	= sc.nextLine();
			String		pars[]		= paramsStr.split(",");

			int			songCount	= Integer.parseInt(pars[0]);				// Liczba piosenek
			int			sortByCol	= Integer.parseInt(pars[1]) - 1;
			int			orderNo		= Integer.parseInt(pars[2]);

			Song[]		songs		= new Song[songCount];	// Tablica piosenek
			String[]	labels		= sc.nextLine().split(",");
			bringForward(labels, sortByCol);
			for (int i = 0; i < songs.length; i++) {
				String		metaStr	= sc.nextLine();							// Wczytujemy kolejne piosenki
				String[]	meta	= metaStr.split(",");
				bringForward(meta, sortByCol);
				songs[i] = new Song(meta);
			}
			sortByCol = 0;

			musicCollection music = new musicCollection(labels, songs, sortByCol, orderNo);	// Inicjalizacja kolekcji piosenek, ktora tez tasuje piosenki i wyznacza najdluzszy wspolny prefiks
			// output.(music);
			// System.out.println(music);								// Dodawanie wynikow na wyjscie
			output.append(music).append("\n");
		}
		System.out.print(output);				// Wypisywanie wyjscia

	}
}

// test.00.in