// Maksim Zdobnikau - 2

import java.util.Scanner;

public class Source {
	// Section Helper Functions
	public static Scanner sc = new Scanner(System.in); 	 // Scanner do wczytywania danych wejsciowych

	private static void QS_Swap(int[] arr, final int a_i, final int b_i) {
		// Funkcjha zamienia mejscami elementy tablicy
		int tmp = arr[a_i];
		arr[a_i] = arr[b_i];
		arr[b_i] = tmp;
	}

	private static int QS_Partition(int[] arr, final int left, final int right) {
		final int	pivot	= arr[right];
		int			i		= left - 1;
		for (int j = left; j < right; j++) {
			if (arr[j] <= pivot) {
				i++;
				QS_Swap(arr, i, j);
			}
		}
		QS_Swap(arr, i + 1, right);
		return i + 1;
	}

	private static void QS_quicksort(int[] arr, final int left, final int right) {
		if (left < right) {
			final int pivot_pos = QS_Partition(arr, left, right);
			QS_quicksort(arr, left, pivot_pos - 1);
			QS_quicksort(arr, pivot_pos + 1, right);
		}
	}

	public static int[] Quicksort(int[] arr) {
		QS_quicksort(arr, 0, arr.length - 1);
		return arr;
	}

	public static int min(final int a, final int b) {
		if (a < b)
			return a;
		else
			return b;
	}

	public static int Latest_Idx_LesserThan(int[] arr, int left_i, int right_i, final int searched_Value) {
		// Modyfikacja algorytmu przeszukiwania binarnego o zlozonosci O(log(n))
		// Zamiast znalezenia indeksu elementu o podanej wartosci, algorytm wyszukuje indeks ostatniego wystapenia najwiekszego elementu mniejszego od podanej wartosci 	

		int	result_i	= -1;	// Ostatni indeks najwiekszego elementu mniejszego od podanej wartosci 
		// Zwracamy -1, jesli nie istneje element mniejszy od podanej wartosci np. wartosc 2 dla tablicy 4 5 8  
		int	mid_i		= 0;	// Indeks srodkowego elementa w sprawdzanym przedziale tablicy

		while (left_i <= right_i) {
			mid_i = left_i + (right_i - left_i + 1) / 2;// Szukamy sufit jako indeks elementu srodkowego
			if (arr[mid_i] >= searched_Value) {
				// Jesli element o indeksie mid_i jest wiekszy badz rowny od podanej wartosci, zachodzi nastepujace zdanie logiczne:
				// Dla wszystkich i in [mid_i+1, right_i]: arr[i]>=searched_Value
				// Wtedy, indeks ostatniego najwieksego elementu mniejszego od podanej wartosc jest w przedziale [low, mid-1]
				right_i = mid_i - 1;  // Zatem, odpowiednio zmieniamy przedzial do przeszukiwania na [low, mid-1]
			} else { // if (arr[mid_i] < searched_Value) 
				// Jesli elememt o indeksie mid_i jest mniejszy od podanej wartosci, zachodzi nastepujace zdanie logiczne:
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
					// if (cur_triangles_count > 0) { // Jesli liczba mozliwych trojkatow dla odcinkow arr[i] i arr[j] jest wieksza od zera i nie wypisalismy 10 trojek, 
					// wypisujemy indeksy trojkatow utworzone przez odcinki arr[i] i arr[j] i trzeci odcinek  
					for (int printed = 0; printed < min(cur_triangles_count, 10 - triangles_count); printed++) { // Petla, wypisujaca indeksy znalezionych trojkatow leksykograficznie
						// Ta petla wykonuje nie wiecej niz 10 razy zgodnie z trescia, wiec jej zlozonosc asymptomatyczna jest O(1)
						// * jest to realizonane przez funkcje min(), ktora przyjmuje liczbe minimalna wartosc z (liczby mozliwych trojkataw dla odcinkow arr[i] i arr[j],   
						// Wypisuje trojki w leksykograficznej kolejnosci, ze wzgledu na to, w jaki sposob algorytm przechodzi po elementach tablicy
						// i in [0, n-1]
						// 		j in [i, n-1]]
						//				k in [j+1, j+1+printed]
						System.out.print("(" + i + "," + j + "," + (j + 1 + printed) + ") "); // Wypisywanie trojek indeksow trojkatow
					}
					// }
					triangles_count += cur_triangles_count; // Liczba wszystkich mozliwych trojkatow dla danego zestawu danych  
				}
			}

			if (triangles_count > 0) {
				System.out.println("\nNumber of triangles: " + triangles_count);
			} else {
				System.out.println("Triangles cannot be built ");
			}
		}
	}
}