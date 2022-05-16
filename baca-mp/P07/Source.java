// Maksim Zdobnikau - 2

import java.util.Scanner;

/* Ogolna idea rozwiazania
 * Rozwiazania opiera sie na modyfikacji algorytma quicksort, iteracyjnie i bez stosu
 * Dla tego przechodzimy po kolejnych podtablicach ("wywoaliach") quicksort i stosujemy zwykla metode partition
 * dla znalezenia indeksu dla elementa pivot.
 * Przechodzimy po podtablicach od lewej strony i zaznaczamy nastepne przedzialy podczas sortowania, ktore nalezy posortowac je w kolejnych "wywolaniach"
 *
 * Wyjasnenie zlozonosci
 * Stosujemy algorytm quicksort, ktore dzieli sortowanie podtablicy na podproblemy
 * Dzielenie na podproblemy w srednich oczekiwanych przypadkach (partition nie wybiera najmniejsze lub najwieksze elementy) powoduje O(logN) podzadan
 * Partition ma zlozonosc O(n)
 * Uzywamy dodwatkowej petli, zeby przeszukiwac indeksy kolejnej podtablicy, ktora nalezy posortowac
 * Ale 2*O(N) = O(N), O(N*logN + c*logN) = O(N*logN)
 * Zatem mamy zwykla dla algorytma quicksort zlozonosc O(N*logN) */

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

	@Override public String toString() {					// Zwraca sformatowane metadane plyty
		StringBuilder	result	= new StringBuilder(""); 	//Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
		int				N		= dataArr.length;			// Ilosc metadanych
		for (int i = 0; i < N - 1; i++) {
			result.append(dataArr[i]).append(",");			// Dodajemy przecinki miedzy kolejnymi metdanymi
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
	private Song[] songs;		// Tablica zawierajaca obiekty plyt
	private Order order;		// Porzadek, zgodnie z ktorym zostanie posortowana tablica
	private int sortBy;			// Indkes kolumny, po ktorej musimy posortowac metadane

	private int getMax(int L, int R) {
		int max = L + 0;

		for (int i = L + 1; i <= R; i++) {
			if (order.isBigger(songs[i], songs[max], sortBy)) {
				max = i;
			}
		}
		return max;
	}
	private int getMin(int L, int R) {
		int min = L + 0;

		for (int i = L + 1; i <= R; i++) {
			if (order.isBigger(songs[min], songs[i], sortBy)) {
				min = i;
			}
		}
		return min;
	}

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

	public void markR(int k, int q, int R) {
		Song tmp = songs[k];
		songs[k] = songs[q - 1];
		songs[q - 1] = songs[q];
		songs[q] = songs[R];
		songs[R] = tmp;
	}
	// public void unmarkR(int k, int q, int R) {
	// 	Song tmp = songs[R];
	// 	songs[R] = songs[q];
	// 	songs[q] = songs[q - 1];
	// 	songs[q - 1] = songs[k];
	// 	songs[k] = tmp;
	// }
	public void unmarkR(int L, int R) {
		Song tmp = songs[R];
		songs[R] = songs[L];
		songs[L] = songs[L - 1];
		songs[L - 1] = tmp;
	}
	private int findR(int L) {		// Szukamy ostatni element podtablicy zaczynajac od R
		int i = L - 1;			// Zaczynamy od lewego elementa
		while (++i < songs.length) {
			if (songs[i].isNextR() == true) { 	// Sprawdzenie czy element o indeksie i jest zaznaczony
				break;
			}
		}
		// songs[i].toggleNextR();	// Zwracamy indeks i odznaczamy element, podtablica konczaca na i juz zostanie posortowana 
		return i;
	}
	private int findNewR(int L) {		// Szukamy ostatni element podtablicy zaczynajac od R
		int	i	= L - 1;			// Zaczynamy od lewego elementa
		int	min	= L;
		// while (++i < songs.length) {
		// 	if (order.isBigger(songs[min], songs[i], sortBy)) { 	// Sprawdzenie czy element o indeksie i jest zaznaczony
		// 		min = i;
		// 	}
		// }
		while (++i < songs.length) {
			if (order.isBigger(songs[L - 1], songs[i], sortBy)) { 	// Sprawdzenie czy element o indeksie i jest zaznaczony
				// min = i;
				return i;
			}
		}
		// swapSongs(min, L);
		// songs[i].toggleNextR();	// Zwracamy indeks i odznaczamy element, podtablica konczaca na i juz zostanie posortowana 
		return min;
	}
	private void swapSongs(final int A, final int B) {
		Helpers.swap(this.songs, A, B);		// zamiana elementow o podanych indeksach mejscami w tablicy plyt  
	}

	public int partition(final int L, final int R) { 			// Partition Hoare
		int		i		= L - 1;					// Indeks, ktory przeszukuje indeks dla pivot
		int		j		= R;						// Prawy indeks, sprawdzajacy kolejnosc elementow
		Song	pivot	= songs[R];					// Pivot, dla ktorego szukamy indeks w tablicy [L ... R]

		do {
			while (order.isBigger(pivot, songs[++i], sortBy)) {}
			// po wyjsciu z petli i-ty element jest wiekszy badz rowny od pivot
			while (j > L && order.isBigger(songs[--j], pivot, sortBy)) {} // j=R-1 po --j
			// po wysciu z petli j-ty element jest rowny elementowi o indeksie L lub jest mniejszy badz rowny od pivot
			if (i < j) {		// w przypadku gdy zostala zmieniona kolejnosc indeksow
				swapSongs(i, j); // zamieniamy i-ty i j-ty elementy 	
			}
		} while (i < j);		// Petla dziala, poki kolejnosc indeksow jest poprawna

		swapSongs(i, R); 	// zamieniamy elementy o indeksach i, j
		return i; 			// zwracamy indeks elementu pivot
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

	// public void quickSort() {					// Implementacja quicksort iteracyjna bez stosu
	// 	int	L		= -1;				// Indeks lewego elementa podtablicy aktualnego "wywolania" quickSort 
	// 	int	R		= songs.length - 1;	// Indeks prawego elementa podtablicy aktualnego "wywolania quickSort" aktualnej  	
	// 	int	toSort	= 1;				// Liczba wywolan quicksort, ktory jeszcze musimy wykonac

	// 	// songs[R].toggleNextR();		// Zaznaczamy, ze w pierwszym "wywolaniu" trzeba posortowac tablice [L+1 ... N-1]
	// 	int	min		= getMin(0, R);
	// 	swapSongs(min, R);
	// 	do {
	// 		int curSize = R - L + 1;	// Rozmiar aktualnej podtablicy 

	// 		do {						// Szukamy dane kolejnego "wywolania" quicksort, element lewostronny i prawostronny, O(n)
	// 			L++;					// Podtablica [0 ... L] jest posortowana
	// 			R = findR(L);		// Szukamy indeks R kolejnej podtablice [L ... R] dla posortowania
	// 			toSort--;				// Zmniejszamy liczbe wywolan
	// 			curSize = R - L + 1;	// Obliczamy aktualny rozmiar tablicy
	// 		} while (curSize <= 1 && toSort > 0);	// Poki nie znajdziemy tablicy o przynajmnie dwoch elemetnow i nie wykonamy wszystkie wywolania

	// 		if (curSize > 1) {			// Jesli podtablica nie jest jedynym elementym (czyli juz jest posortowana)
	// 			if (curSize <= 5) {		// S ortujemy selectionSortem podtablice, rozmiar ktorych <= 5
	// 				selectionSort(L, R);
	// 				L = R;			// Ustawiamy L na curL, elementy [0 ... L] juz so posortowane
	// 			} else {
	// 				while (L < R) {	// Lub juicksortem sortujemy podtablice [L ... R] dla tablicy o rozmiarze > 5
	// 					int	part	= partition(L, R); 	// Indeks elementu, po po lewej strone ktorego sa mniesze, a po prawej wieksze elementy
	// 					int	max		= getMax(L, part - 1);
	// 					// songs[R].toggleNextR();		// Zaznaczamy, ze aktualny R musi byc ostatnim elementem podtablicy w innym "wywolaniu" 
	// 					markR(max, part, R);		// Zaznaczamy, ze aktualny R musi byc ostatnim elementem podtablicy w innym "wywolaniu" 
	// 					R = part - 1;				// Ustawiamy podtablice [... ... part-1] jako nastepna dla posortowana quicksortem								
	// 					toSort++;						// Zwiekszamy ilosc podtablic do posortowania
	// 				}
	// 			}
	// 		}
	// 	} while (toSort > 0);							// Poki mamy tablice do posortowania
	// }
	// public void quickSort() {					// Implementacja quicksort iteracyjna bez stosu
	// 	int	N	= songs.length;
	// 	int	L	= -1;				// Indeks lewego elementa podtablicy aktualnego "wywolania" quickSort 
	// 	int	R	= -1;	// Indeks prawego elementa podtablicy aktualnego "wywolania quickSort" aktualnej  	
	// 	int	j	= 1;				// Liczba wywolan quicksort, ktory jeszcze musimy wykonac

	// 	songs[N - 1].toggleNextR();		// Zaznaczamy, ze w pierwszym "wywolaniu" trzeba posortowac tablice [L+1 ... N-1]
	// 	int curSize = R - L + 1;	// Rozmiar aktualnej podtablicy 

	// 	do {						// Szukamy dane kolejnego "wywolania" quicksort, element lewostronny i prawostronny, O(n)
	// 		L = R + 2;
	// 		R = L;
	// 		while (songs[R].isNextR() == false) {
	// 			R++;
	// 		}					// Podtablica [0 ... L] jest posortowana
	// 		songs[R].toggleNextR();

	// 		// R = findR(L);		// Szukamy indeks R kolejnej podtablice [L ... R] dla posortowania
	// 		// j--;				// Zmniejszamy liczbe wywolan
	// 		// curSize = R - L + 1;	// Obliczamy aktualny rozmiar tablicy

	// 		while (R - L > 2) {
	// 			j = partition(L, R);
	// 			// int max = getMax(L, j - 1);
	// 			songs[R].toggleNextR();		//
	// 			// markR(max, j, R);				// Zaznaczamy, ze aktualny R musi byc ostatnim elementem podtablicy w innym "wywolaniu" 
	// 			R = j - 1;
	// 		}

	// 		selectionSort(L, R);
	// 		L = R;

	// 	} while (R != N);
	// }
	public void quickSort() {

		int	L	= 0;
		int	end	= songs.length - 1;
		int	q, i = 0;
		int	R	= end;
		while (true) {
			i--;
			while (L < R) {
				q = partition(L, R);
				// songs[tmpr].toggleNextR();
				int max = getMax(L, q - 1);
				// 			songs[R].toggleNextR();		//
				markR(max, q, R);
				R = q - 1;
				++i;
			}
			if (i < 0)
				break;
			L++;
			R = findNewR(L);
			// songs[R].toggleNextR();
			// swapSongs(R, L);
			unmarkR(L, R);

		}
	}

	@Override public String toString() {					// Zwraca sformatowany wynik sortowania metdadanych
		StringBuilder	result	= new StringBuilder(""); 	//Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
		int				N		= labels.length;
		for (int i = 0; i < N - 1; i++) {					// Wypisywanie nazw kolumn
			result.append(labels[i]).append(",");
		}
		if (N >= 1) {
			result.append(labels[N - 1]);
		}
		result.append("\n");
		int songCount = songs.length;
		for (int i = 0; i < songCount; i++) {				// Wypisywanie metadanych kolejnych plyt
			result.append(songs[i]).append("\n");
		}

		return result.toString();	// zwracamy caly wynik
	}
}

public class Source {
	public static Scanner sc = new Scanner(System.in); // Scanner do wczytywania danych wejsciowych
	public static void bringForward(String[] arr, int sortByCol) {	// Przenoszenie kolumny, po ktorej musimy posortowac na 0 indeks
		for (int j = sortByCol; j >= 1; j--) {
			Helpers.swap(arr, j, j - 1);
		}
	}

	public static void main(String[] args) {
		StringBuilder	output		= new StringBuilder(""); 	 	// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
		int				test_count	= Integer.parseInt(sc.nextLine());				// Liczba zestawow testowych
		while (test_count-- > 0) {	 							// Przechodzimy przez kazdy zestaw
			String		paramsStr	= sc.nextLine();			// Wszytujemy napis z parametrami danej kolekcji
			String		pars[]		= paramsStr.split(",");		// Dzielimy parametry osobne elementy tablicy

			int			songCount	= Integer.parseInt(pars[0]);		// Liczba wierszy
			int			sortByCol	= Integer.parseInt(pars[1]) - 1;	// Indeks kolumny, po ktorej musimy posortowac
			int			orderNo		= Integer.parseInt(pars[2]);		// Kolejnosc metadanych posortowanych		

			Song[]		songs		= new Song[songCount];				// Tablica plyt
			String[]	labels		= sc.nextLine().split(",");			// Wczytujemy nazwy kolumn
			bringForward(labels, sortByCol);							// Przenosimy kolumne o indeksie sortBy na pierwsza pozycje
			for (int i = 0; i < songs.length; i++) {
				String		metaStr	= sc.nextLine();					// Wczytujemy kolejne metadane 
				String[]	meta	= metaStr.split(",");				// Robimy tablice metadanych
				bringForward(meta, sortByCol);							// Przenosimy kolumne o indeksie sortBy na pierwsza pozycje
				songs[i] = new Song(meta);								// Tworzenie obiektu aktualnej plytu
			}
			sortByCol = 0;	// Sortujemy po pierwzej kolumnie, bo zamienilismy kolumne 0 i sortBy

			musicCollection music = new musicCollection(labels, songs, sortByCol, orderNo);	// Inicjalizacja kolekcji piosenek, ktora tez tasuje piosenki i wyznacza najdluzszy wspolny prefiks
			output.append(music).append("\n");				// Dodawanie wynikow na wyjscie
		}
		System.out.print(output);				// Wypisywanie wyjscia
	}
}

// test.00.in
// 5
// 3,3,-1
// asleep,prepare,strong,symbol
// number,74242,whispered,10437
// youth,30506,nearby,47714
// shoot,2975,myself,10225
// 5,2,-1
// break,hat,brain,kids,faster,bar
// remove,80002,16762,political,colony,62255
// difference,4276,10573,metal,tomorrow,43487
// truck,46265,99845,except,fast,46704
// similar,61068,31888,till,tide,46024
// whistle,67396,74583,learn,show,40012
// 4,2,1
// month,shape,grass
// success,finish,89949
// fence,firm,87728
// fly,sold,72275
// board,onto,19486
// 18,5,-1
// pony,tomorrow,respect,travel,though,play,bow
// 37635,45649,alive,20387,69922,jet,step
// 98753,30884,riding,7888,89009,weigh,nearest
// 18050,38861,having,19548,7168,each,young
// 86651,11942,chart,35780,70346,discovery,wore
// 83334,69400,ten,89062,79875,forth,magic
// 3067,62062,space,60456,15414,ten,dust
// 52168,66206,younger,11838,37439,writing,operation
// 88380,98186,glad,22021,61671,involved,mental
// 2236,55082,design,29460,47234,coach,wide
// 90477,75191,has,65607,41431,poetry,tomorrow
// 92867,66990,equally,33913,75644,burst,doll
// 38546,27847,tales,79256,90820,local,breakfast
// 11976,24918,steam,76648,31758,surface,original
// 88078,22039,basket,26723,3668,gather,gas
// 13798,40885,attempt,55765,51076,lesson,she
// 18516,10679,only,23121,40465,twice,mostly
// 26854,58002,roar,38587,98478,elephant,sun
// 84545,21407,no,32884,10611,bear,early
// 12,6,1
// daily,bite,fairly,finest,track,softly,built,sat
// 21151,including,88793,standard,90438,silent,29633,lay
// 55997,become,61298,rear,38646,journey,54729,writing
// 27429,brief,49215,coffee,14820,found,33032,master
// 19644,shot,90049,number,75927,caught,11841,draw
// 82427,somehow,61831,atomic,73922,yes,25952,swimming
// 72718,value,24246,require,56480,ago,61967,hunt
// 85763,subject,65616,strike,17879,whether,67548,jack
// 84496,valley,73417,parallel,97038,heading,9960,chance
// 63527,stretch,58807,post,67953,south,92481,language
// 81592,wolf,36501,repeat,56460,seven,93435,soft
// 63457,steam,51675,trap,12990,spin,77317,no
// 31453,tie,76432,guard,17946,himself,53314,swam

// test.00.out
// strong,asleep,prepare,symbol
// whispered,number,74242,10437
// nearby,youth,30506,47714
// myself,shoot,2975,10225

// hat,break,brain,kids,faster,bar
// 80002,remove,16762,political,colony,62255
// 67396,whistle,74583,learn,show,40012
// 61068,similar,31888,till,tide,46024
// 46265,truck,99845,except,fast,46704
// 4276,difference,10573,metal,tomorrow,43487

// shape,month,grass
// finish,success,89949
// firm,fence,87728
// onto,board,19486
// sold,fly,72275

// though,pony,tomorrow,respect,travel,play,bow
// 98478,26854,58002,roar,38587,elephant,sun
// 90820,38546,27847,tales,79256,local,breakfast
// 89009,98753,30884,riding,7888,weigh,nearest
// 79875,83334,69400,ten,89062,forth,magic
// 75644,92867,66990,equally,33913,burst,doll
// 70346,86651,11942,chart,35780,discovery,wore
// 69922,37635,45649,alive,20387,jet,step
// 61671,88380,98186,glad,22021,involved,mental
// 51076,13798,40885,attempt,55765,lesson,she
// 47234,2236,55082,design,29460,coach,wide
// 41431,90477,75191,has,65607,poetry,tomorrow
// 40465,18516,10679,only,23121,twice,mostly
// 37439,52168,66206,younger,11838,writing,operation
// 31758,11976,24918,steam,76648,surface,original
// 15414,3067,62062,space,60456,ten,dust
// 10611,84545,21407,no,32884,bear,early
// 7168,18050,38861,having,19548,each,young
// 3668,88078,22039,basket,26723,gather,gas

// softly,daily,bite,fairly,finest,track,built,sat
// ago,72718,value,24246,require,56480,61967,hunt
// caught,19644,shot,90049,number,75927,11841,draw
// found,27429,brief,49215,coffee,14820,33032,master
// heading,84496,valley,73417,parallel,97038,9960,chance
// himself,31453,tie,76432,guard,17946,53314,swam
// journey,55997,become,61298,rear,38646,54729,writing
// seven,81592,wolf,36501,repeat,56460,93435,soft
// silent,21151,including,88793,standard,90438,29633,lay
// south,63527,stretch,58807,post,67953,92481,language
// spin,63457,steam,51675,trap,12990,77317,no
// whether,85763,subject,65616,strike,17879,67548,jack
// yes,82427,somehow,61831,atomic,73922,25952,swimming