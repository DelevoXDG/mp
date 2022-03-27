// Maksim Zdobnikau - 2

import java.util.Scanner;

public class Source {
	public static Scanner sc = new Scanner(System.in); // Scanner do wczytywania danych wejsciowych

	public static void main(String[] args) {
		//Section Input values
		int		test_num	= 0; 		// Liczba zestawow, pozostalych do swprawdzenia
		int		nz			= 0; 		// Numer zestawu
		int		rows		= 0;		// Liczba wierszy
		int		columns		= 0;		// Liczba kolumn
		int[][]	arr;					// Tablica danych

		test_num = sc.nextInt(); 			// Wczytujemy liczbe zestawow
		while (test_num-- > 0) { 			// Petla do wczytywania i przetwarzania zestawow
			// Section Reading input
			nz = sc.nextInt(); 				// Wczytujemy aktualny numer zestawu
			sc.findInLine(" : ");			// Pomijamy niepotrzenme znaki
			rows = sc.nextInt();			// Wczytujemy liczbe wierszy
			columns = sc.nextInt(); 		// Wczytujemy liczbe kolumn

			arr = new int[rows][columns]; 			// Inicjalizacja tablicy dla aktualneg o zestawu
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					arr[i][j] = sc.nextInt();		// Wypelnenie tablicy danymi dla danego zestawu
				}
			}
			// Section Variables required
			int		cur_sum				= -1; 	// Aktualna suma

			int		cur_upp				= 0;	// Indeks pierwszego wiersza fragmenta o aktualnej sumie
			int		cur_low				= 0;	// Indeks ostatniego wiersza fragmenta o aktualnej sumie
			int		cur_row_count		= 0;	// Dlugosc aktualnego fragmenta wzgledem wierszy  

			int		cur_left			= 0;	// Indeks pierwszej kolumny fragmenta o aktualnej sumie
			int		cur_right			= 0;	// Indeks ostatniej kolumny fragmenta o aktualnej sumie
			int		cur_column_count	= 0;	// Dlugosc aktualnego fragmenta wzgledem kolumn

			int		max_sum				= -1; 	// Najwieksza suma

			int		max_upp				= 0;	// Indeks piewszego wiersza fragmenta o najwiekszej sumie
			int		max_low				= 0;	// Indeks ostatniego wiersza fragmenta o najwiekszej sumie
			int		max_row_count		= 0;	// Dlugosc fragmenta o najwiekszej sumie wzgledem wierszy

			int		max_left			= 0;	// Indeks pierwszej kolumny fragmenta o najwiekszej sumie
			int		max_right			= 0;	// Indeks ostatnie kolumny fragmenta o najwiekszej sumie
			int		max_column_count	= 0;	// Dlugosc fragmenta o najwiekszej sumie wzgledem kolumn

			int[]	arr_extended		= new int[columns]; // Pomocnicza tablica, przechowuje w kazdej kolumnie sume wartosci elementow kolumny danego fragmenta   
			boolean	has_non_negatives	= false;			// Zmienna logiczna "czy sa w tablicy elementy o wartosciach nieujemnych"

			String	result				= "";				// Wynik tekstowy dla danego zestawu
			//Section Algorithm itself
			for (int i = 0; i < rows && has_non_negatives == false; i++) { // Wyszykiwanie elementow o wartosciach neujemnych
				for (int j = 0; j < columns && has_non_negatives == false; j++) {
					if (arr[i][j] >= 0) {
						has_non_negatives = true;  // Jezeli udalo znalezc element >= 0, zmieniami wartosc zmiennej i wychodzimy z petli
					}
				}
			}
			if (has_non_negatives == true) {				// Jezeli udalo znalezc element >= 0, algorytm dziala w zwyklym trybie
				for (int i = 0; i < rows; i++) {			// Petla przechodzi przez kazdy wierz wejsciowej i pomocniczej tablicy
					for (int k = 0; k < columns; k++) {		// Petla przechodzi przez kadza kolumne pomocniczej tablicy
						arr_extended[k] = 0;				// Wyzerowanie pomocniczej tablicy
					}
					cur_upp = i; 						// Indeks pierwszego (gornego) wiersza aktualnie sprawdzanego fragmentu
					for (int j = i; j < rows; j++) { 	// Petla przechodzi przez kazdy wierz poczynajac od wiersza o indeksie [i], 
						// za kazdym sprawdzajac wiekszy podfragment  
						cur_low = j; 					// Indeks ostatniego (dolnego) wiersza aktualnie sprawdzanego fragmentu
						cur_row_count = j - i; 			// Dlugosc aktualne sprawdzanego fragmentu wzgledem wierszy
						for (int k = 0; k < columns; k++) {// Petla przechodzi przez kazda kolumne danego podfragmenty
							arr_extended[k] = arr_extended[k] + arr[j][k]; // Sumujemy wartosci wszystkich elementow danej kolumny aktualnego podfragmentu
							// Ogolna idea
							// Roszerzamy sprawdzany fragment
							// Szukamy algorytmem kadane najwieksza sume kolumn danego wiersza + nastepnych j wierszy  
						}
						//Section Kadane
						int[] kadane_results = Kadane(arr_extended, columns); // Stosujemy algorytm Kadane o zlozonosci O(n), wyniki zwracamy jako tablice
						// Dostajemy z tablicy wynikow dane fragmentu o m najwiekszej sumie:
						cur_sum = kadane_results[0];			// Aktualnie sprawdzana suma = najwieksza suma fragmenta 
						cur_left = kadane_results[1];			// Aktualny indeks pierwszej kolumny = indeks pierwszego elementa najwiekszej sumy fragmenta
						cur_right = kadane_results[2];			// Aktualny indeks ostatniej kolumny = indeks ostatniego elementa najwiekszej sumy fragmenta
						cur_column_count = kadane_results[3];	// Aktualna dlugosc fragmentu wzgledem kolumn = dlugosc najlepszego fragmenta 
						//Section Kadane End
						if ((cur_sum > max_sum) // Zamieniamy fragment najlepszej sumy jesli suma elementow tego fragmentu jest silnie wieksza 
								|| ((cur_sum == max_sum)															// Lub jest rowny
										&& (cur_column_count * cur_row_count < max_column_count * max_row_count)) 	// ale ma mniejszy rozmiar
						) {
							// Aktualizacja wszystkich danych najlepszego fragmentu
							max_sum = cur_sum;

							max_left = cur_left;
							max_right = cur_right;
							max_column_count = cur_column_count;

							max_upp = cur_upp;
							max_low = cur_low;
							max_row_count = cur_row_count;
						}
					}
				}
				result = nz + ": n = " // Zapisujemy dane do wyniku testowego
						+ rows + " m = " + columns
						+ ", s = " + max_sum
						+ ", mst" + " = a[" + max_upp + ".." + max_low
						+ "][" + max_left + ".." + max_right + "]";
			} else { // Wszystkie elementy tablicy sa ujemne, tablica najwiekszej sumy zatem jest pusta
				max_sum = 0; // Zgodnie z trescia
				result = nz + ": n = "  // Zapisujemy dane do wyniku testowego dla tablicy pustej
						+ rows + " m = " + columns
						+ ", s = " + max_sum
						+ ", mst" + " is empty";
			}
			System.out.println(result); // Wypisywanie wyniku
		}
	}

	static int[] Kadane( 	// Algorytm Kadane, zlozonosc O(n)
			int[] arr, 		// Tablica wejsciowa
			int N) 			// Rozmiar tablicy
	{
		int	cur_sum				= -1;	// Aktualna suma
		int	cur_sum_start		= 0;	// Indeks pierwszego elementa podtablicy o aktualnej sumie
		int	cur_sum_size		= 0;	// Aktualna dlugosc podtablicy

		int	max_sum				= -1; 	// Najwieksza suma
		int	max_sum_start		= 0;	// Indeks pierwszego elementa podtablicy o najwiekszej sumie
		int	max_sum_end			= 0;	// Indeks ostatniego elementa podtablicy o najwiekszej sumie
		int	max_sum_min_size	= 0;	// Najmniejsza dlugosc najwiekszej podtablicy

		for (int i = 0; i < N; i++) {
			if (cur_sum > 0) { // Dodajemy elementy do podtablicy tylko jezeli zwiekszaja sume
				cur_sum = cur_sum + arr[i]; // Przypadek Kadane 1: aktualny element rozszerza podtablice
				cur_sum_size++; // W takim przypadku jest rowniez zwiekszony rozmiar podtablicy
			} else { // Zaczynamy now
				cur_sum = arr[i]; 	// Przyapadek Kdadne 2: zaczynamy nowa podtablice
				cur_sum_start = i; 	// Od aktualnego elemnentu
				cur_sum_size = 1;	// Rozmiar podtablicy wtedy jest rowny 1
			}
			if (cur_sum > max_sum // Zamieniamy fragment najlepszej sumy jesli suma elementow tego fragmentu jest silnie wieksza 
					|| (cur_sum == max_sum && cur_sum_size < max_sum_min_size)  // Lub rozmiar podtablicy jest mniejszy
			) {
				// Aktualizacja wszystkich danych najlepszej podtablicy
				max_sum = cur_sum;
				max_sum_start = cur_sum_start;
				max_sum_end = i;
				max_sum_min_size = cur_sum_size;
			}
		}
		int[] wrapper = new int[] { max_sum, max_sum_start, max_sum_end, max_sum_min_size };	// Pakujemy do objektu wyniki algorytmy
		return wrapper; 																		// Zwracamy te wyniki
	}
}

// test.00.in
// 10
// 1 : 2 5
// 0 1 3 -1 0
// -1 1 3 -9 8
// 2 : 2 4
// 1 1 -2 2
// 2 -1 -1 0
// 3 : 2 2 
// 0 0
// 0 0
// 4 : 4 5
// 0 -1 -2 -4 5
// 0 4 4 4 1
// 4 -4 -5 3 2
// -2 -1 -1 6 -1
// 5 : 3 3
// -1 -2 -3
// -4 -5 -6
// -7 -8 -9
// 6 : 5 5
// 0 1 0 0 0
// 0 -1 2 0 0
// 0 0 -1 3 0
// 0 0 3 -1 4
// 0 0 0 0 -1
// 7 : 4 3
// 5 4 0
// 0 0 1
// 0 0 0
// 0 -1 10
// 8 : 2 4
// -1 1 -1 1
// 1 -1 1 -1
// 9 : 2 2
// -1 -1
// 0 -1
// 10 : 3 2
// 5 -4
// -5 4
// 3 3

// test.00.out
// 1: n = 2 m = 5, s = 8, mst = a[1..1][4..4]
// 2: n = 2 m = 4, s = 3, mst = a[0..1][0..0]
// 3: n = 2 m = 2, s = 0, mst = a[0..0][0..0]
// 4: n = 4 m = 5, s = 16, mst = a[0..3][3..4]
// 5: n = 3 m = 3, s = 0, mst is empty
// 6: n = 5 m = 5, s = 10, mst = a[1..3][2..4]
// 7: n = 4 m = 3, s = 19, mst = a[0..3][0..2]
// 8: n = 2 m = 4, s = 1, mst = a[0..0][1..1]
// 9: n = 2 m = 2, s = 0, mst = a[1..1][0..0]
// 10: n = 3 m = 2, s = 7, mst = a[1..2][1..1]