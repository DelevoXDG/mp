// Maksim Zdobnikau - 2

import java.util.Scanner;

// Idea rozwiazania:
// 1) Sortujemy padane z wejscia dane algorytmem Quicksort, pesymistyczna zlozonosc ktorego O(n^2)
// 2) Przechodzimy przez prawie kazde dwa odcinka, potrzebujemy dla tego dwie petli, zatem juz mamy zlozonosc co najmniej O(n^2)
// 2.1) Wewnatrz drugiej petli uzywanie modyfikowany algorytm przeszukiwanie binarnego
// Zamiast znalezenia indeksu elementu o podanej wartosci, algorytm wyszukuje indeks ostatniego wystapenia najwiekszego elementu mniejszego od podanej wartosci
// 2.2) Rezsta operacji nie zalezy od danych wejsciowych
// W tym petla, bo bedzie wykonana co najwyzej 10 razy
// 2.2) Zatem, zlozonosc obliczeniowa wewnatrz drugie petli jest O(log2(n))

// Uproszczajac, mamy:
// 1) Sortowanie o zlonosci pesymistycznej O(n^2)
// 2) Sprawdzenie odcinkow (elementow tablicy) o zlozonosci pesymistycznej O((n^2)*log2(n))
// Z tego mamy asymptomatyczna zlozonosc obliczeniowa O((n^2)*log2(n))

public class Source {
	// Section Helper Functions
	public static Scanner sc = new Scanner(System.in); 	 // Scanner do wczytywania danych wejsciowych

	private static void QS_Swap(int[] arr, final int a_i, final int b_i) {
		// Funkcja zamienia mejscami elementy tablicy
		int tmp = arr[a_i];
		arr[a_i] = arr[b_i];
		arr[b_i] = tmp;
	}

	private static int QS_Partition(int[] arr, final int left, final int right) {
		// Znalezenie pozycji dla podanej wartosci (tzw. pivot)
		final int	pivot	= arr[right]; //Bierzemt dowolny element jako pivot (w danej implementacji ostatni)
		// Musimy znalezc indeks dla wartosci pivot w taki sposob, ze po lewej stronie sa wartosci mniejsze od pivot, po prawej - wieksze od pivot
		int			i		= left - 1; // Liczba elemetnow, mniejszych od pivot
		for (int j = left; j < right; j++) { //Petla przechodzi przez kazdy element w przedzile [left, right]
			if (arr[j] <= pivot) {	//Jesli element jest mniejszy od piwot
				i++; // Zwiekszamy liczbe elementow, mnejszych badz rownych od pivot
				QS_Swap(arr, i, j);	// Po lewej stronie tablicy zapisujemy elementy o wartosciach mniejszych badz rownych pivot 
			}
		}
		// Znalezlismy pozycje pivot
		QS_Swap(arr, i + 1, right);  	// Musimy teraz ustawic pivot na tej pozycji
		return i + 1; 					// i zrocic ta pozycje
	}

	private static void QS_quicksort(int[] arr, final int left, final int right) {
		if (left < right) {
			final int pivot_pos = QS_Partition(arr, left, right); // Znalezenie indeksu dla podanej wartosci 
			QS_quicksort(arr, left, pivot_pos - 1); 	// Sortowanie lewej czesci tablicy przed indeksem pivot
			QS_quicksort(arr, pivot_pos + 1, right);	// Sortowanie prawej czesci tablicy po indeksie pivot
		}
	}

	public static int[] Quicksort(int[] arr) { 	// Inteface agorytmyu sortowania quicksort
		QS_quicksort(arr, 0, arr.length - 1);	// Pierwsze wywolanie rekurencejnego algorytmu
		return arr;								// Zwraca referncje do posortowanej tablicy
	}

	public static int min(final int a, final int b) { // Funkcja, ktora zwraca mniejsza wartosc z dwoch podanych
		if (a < b)
			return a;
		else
			return b;
	}

	public static int Latest_Idx_LesserThan(int[] arr, int left_i, int right_i, final int searched_Value) {
		// Modyfikacja algorytmu przeszukiwania binarnego o zlozonosci O(log2(n))
		// Zamiast znalezenia indeksu elementu o podanej wartosci, algorytm wyszukuje indeks ostatniego wystapenia najwiekszego elementu mniejszego od podanej wartosci 	
		int	result_i	= -1;	// Ostatni indeks najwiekszego elementu mniejszego od podanej wartosci 
		// Zwracamy -1, jesli nie istneje element mniejszy od podanej wartosci np. wartosc 2 dla tablicy 4 5 8  
		int	mid_i		= 0;	// Indeks srodkowego elementa w sprawdzanym przedziale tablicy
		while (left_i <= right_i) { // Sprawdzamy zmienjszajacy przedzial poki ten przedzial istneje
			mid_i = left_i + (right_i - left_i + 1) / 2;// Szukamy sufit jako indeks elementu srodkowego
			if (arr[mid_i] >= searched_Value) {
				// Jesli element o indeksie mid_i jest wiekszy badz rowny od podanej wartosci, zachodzi nastepujace zdanie log2iczne:
				// Dla wszystkich i in [mid_i+1, right_i]: arr[i]>=searched_Value
				// Wtedy, indeks ostatniego najwieksego elementu mniejszego od podanej wartosc jest w przedziale [low, mid-1]
				right_i = mid_i - 1;  // Zatem, odpowiednio zmieniamy przedzial do przeszukiwania na [low, mid-1]
			} else { // if (arr[mid_i] < searched_Value) 
				// Jesli elememt o indeksie mid_i jest mniejszy od podanej wartosci, zachodzi nastepujace zdanie log2iczne:
				// Dla wszystkich i in [left_i, mid_i-1]: arr[i]<searched_Value
				// Wtedy, indeks ostatniego najwiekszego elementu mniejszego od podanej wartosci jest w przedziale [mid_i, right_i] 
				result_i = mid_i; 	// Zapisujemy indeks mid_i jako posredni wynik
				left_i = mid_i + 1; // Zmieniamy przedzial do przeszukiwania na [mid_i-1, right_i], bo szukamy OSTATNI indeks najwiekszego elementu mniejszego od podanej wartosci 
			}
		}
		return result_i; // Zwraca wynik, bedacy indeksem ostatniego wystapienia najwiekszego elementu mniejszego od podanej wartosci
	}

	// Section Triangle Problem
	public static void main(String[] args) {
		int test_count = sc.nextInt();  // Wczytujemy liczbe zestawow

		for (int test_num = 0; test_num < test_count; test_num++) {  // Petla dla wczytywania i przetwarzania zestawow
			int		arr_size		= sc.nextInt();			// Aktualny numer zestawu
			int[]	arr				= new int[arr_size];	// Aktualna tablica danych
			int		triangles_count	= 0;					// Liczba wszystkich trojkowtow dla danego zestawu danych   

			for (int i = 0; i < arr.length; i++) {
				arr[i] = sc.nextInt();						// Wypelnenie tablicy danymi dla danego zestawu
			}
			arr = Quicksort(arr);							// Sortowanie tablicy danych wejsciowych za pomoca algorytmu QuickSort o pesymistycznej zlozonosci obliczeniowej O(n^2)
			System.out.println((test_num + 1) + ": n= " + arr_size); // Wypisywanie numeru aktualnego zestawu wraz z iloscia odcinkow w danym zestawie
			int itr = 0; // Indeks pierwszego elementa w kazdej 25 elementowym wierszu, ktora zostanie wypisana   
			while (itr < arr.length) { // od zerowego od ostatniego 
				int top = min(arr.length - itr, 25); 		// Liczba elementow, ktore musimy wypisac w biezacym wierszu
				for (int j = itr; j < itr + top; j++) { 	// Petla, ktora wypisuje co najwyzej 25 elementow w wierszu
					System.out.print(arr[j] + " "); 		// Wypisywanie kolejnych elementow posortowanej tablicy odcinkow
				}
				itr += top; 								// Przechodzimy do pierwszego elementa nastepnego wierszu
				if (arr.length - itr > 1) {					// Wypisujemy znank nowej linii, jesli istneje kolejny wiersz	
					System.out.println();
				}
			}
			System.out.println(); // Wypisujemy znak nowej linii
			for (int i = 0; i < arr.length; i++) {
				// Petla przechodzi przez kazdy element tablicy
				// i - indeks odcinka na pierwszej pozycji w dowolnej sprawdzanej trojce (i,j,k) 
				for (int j = i + 1; j < arr.length; j++) {
					// Petla przechodzi pozostale (nie sprawdzone i nie bedaca aktualnym odcinkiem) elementy tablicy
					// j - indeks odcinka na drugiej pozycji w dowolnej sprawdzanej trojce (i,j,k) 
					int	last_checked_i		= j;	//Indeks ostatniego elementa, ktory juz nie moze byc trzecia strona trojkota, tworzonego przez odcinki arr[i], arr[j] 
					int	sum					= arr[i] + arr[j]; // Suma aktualne sprawdzanych "odcinkow"
					// Korzystamy z nierownosci trojkata:
					// arr[i] < arr[j] + trzecia_strona
					// arr[j] < arr[i] + trzecia_strona
					// trzecia_strona < arr[i] + arr[j]
					// Poniewaz tablica odcinkow jest posortowana i dla kazdego etapu juz znalelismy wszystkie mozliwe trojkaty dla elementow w przedzile (0, j),
					// oraz zaczynamy od elementow wiekszych od arr[i] i arr[j] 
					// nalezy sprawdzic tylko jedna nierownosc dla trzeciej strony, czyli `trzecia_strona < arr[i] + arr[j]`
					int	third_side_last_i	= Latest_Idx_LesserThan(arr, last_checked_i, arr.length - 1, sum);
					// Korzystamy z modyfikowanego algorytmu przeszukiwania binarnego, ktory znajduje indeks ostatniego wystapienia najwieksego elementu, mniejszego od podanego
					// W taki sposob znajdziemy indeks ostatniego wystapenia elementu, ktory spelnia nierownosc trojkata dla odcinkow arr[i] i arr[j] 
					int	cur_triangles_count	= third_side_last_i - last_checked_i;    // W taki sposob rowniez uzyskujemy liczbe elementow, ktore spelniaja nierownosc nierownosc trojkata dla odcinkow arr[i] i arr[j] 
					// Ta liczba jest rowna mozliwych trojkatow dla odcinkow arr[i] i arr[j]
					if (cur_triangles_count > 0) { // Jesli liczba mozliwych trojkatow dla odcinkow arr[i] i arr[j] jest wieksza od zera i nie wypisalismy 10 trojek, 
						// wypisujemy indeksy trojkatow utworzone przez odcinki arr[i] i arr[j] i trzeci odcinek  
						for (int printed = 0; printed < min(cur_triangles_count, 10 - triangles_count); printed++) { // Petla, wypisujaca indeksy znalezionych trojkatow leksykograficznie
							// Ta petla wykonuje nie wiecej niz 10 razy zgodnie z trescia, wiec jej zlozonosc asymptomatyczna jest O(1)
							//jest to realizonane przez funkcje min(), ktora przyjmuje liczbe minimalna wartosc z (liczby mozliwych trojkataw dla odcinkow arr[i] i arr[j],   
							// Wypisuje trojki w leksykograficznej kolejnosci, ze wzgledu na to, w jaki sposob algorytm przechodzi po elementach tablicy
							// i in [0, n-1]
							// 		j in [i, n-1]]
							//				k in [j+1, j+1+printed]
							System.out.print("(" + i + "," + j + "," + (j + 1 + printed) + ") "); // Wypisywanie trojek indeksow trojkatow
						}
					}
					triangles_count += cur_triangles_count; // Zwiekszamy liczbe wszystkich mozliwych trojkatow dla danego zestawu danych o liczbe mozliwych trojkatow dla odcinkow arr[i] i arr[j]
				}
			}
			if (triangles_count > 0) {  // Jesli liczba mozliwych trojkatow dla danego zestawu jest wieksza od zera
				System.out.println("\nNumber of triangles: " + triangles_count); // Wypisujemy lizcbe mozliwych trojkatow dla danego zestawu
			} else {					// Jesli nie mozna zbudowac trojkatow z podanych odcinkow
				System.out.println("Triangles cannot be built "); 				 // Wypisujemy odpowiedni kommunikat
			}
		}
	}
}

//test.00.in
// 10
// 9
// 1 2 3 3 3 3 5 7 8
// 4
// 6 1 4 1
// 50
// 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50
// 10
// 5 9 9 8 3 3 7 7 1 2
// 4
// 2 4 1 3
// 6
// 1 1 1 1 1 1
// 7
// 2 9 5 1 1 6 4
// 8
// 6 6 2 8 4 2 2 4
// 6
// 1 5 4 3 2 1
// 5
// 2 5 6 1 7

// test.00.out
// 1: n= 9
// 1 2 3 3 3 3 5 7 8 
// (0,2,3) (0,2,4) (0,2,5) (0,3,4) (0,3,5) (0,4,5) (1,2,3) (1,2,4) (1,2,5) (1,3,4) 
// Number of triangles: 32
// 2: n= 4
// 1 1 4 6 
// Triangles cannot be built 
// 3: n= 50
// 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 
// 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 
// (1,2,3) (1,3,4) (1,4,5) (1,5,6) (1,6,7) (1,7,8) (1,8,9) (1,9,10) (1,10,11) (1,11,12) 
// Number of triangles: 9500
// 4: n= 10
// 1 2 3 3 5 7 7 8 9 9 
// (0,2,3) (0,5,6) (0,8,9) (1,2,3) (1,5,6) (1,5,7) (1,6,7) (1,7,8) (1,7,9) (1,8,9) 
// Number of triangles: 55
// 5: n= 4
// 1 2 3 4 
// (1,2,3) 
// Number of triangles: 1
// 6: n= 6
// 1 1 1 1 1 1 
// (0,1,2) (0,1,3) (0,1,4) (0,1,5) (0,2,3) (0,2,4) (0,2,5) (0,3,4) (0,3,5) (0,4,5) 
// Number of triangles: 20
// 7: n= 7
// 1 1 2 4 5 6 9 
// (2,3,4) (2,4,5) (3,4,5) (3,5,6) (4,5,6) 
// Number of triangles: 5
// 8: n= 8
// 2 2 2 4 4 6 6 8 
// (0,1,2) (0,3,4) (0,5,6) (1,3,4) (1,5,6) (2,3,4) (2,5,6) (3,4,5) (3,4,6) (3,5,6) 
// Number of triangles: 16
// 9: n= 6
// 1 1 2 3 4 5 
// (2,3,4) (2,4,5) (3,4,5) 
// Number of triangles: 3
// 10: n= 5
// 1 2 5 6 7 
// (1,2,3) (1,3,4) (2,3,4) 
// Number of triangles: 3