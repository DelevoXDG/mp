// Maksim Zdobnikau - 2

import java.util.Scanner;

class Source {
	public static Scanner sc = new Scanner(System.in); // Scanner do wczytywania danych wejsciowych

	public static void main(String[] args) {
		int		test_num	= 0; 					// Liczba zestawow, pozostalych do swprawdzenia
		int		nz			= 0; 					// Numer zestawu
		int		rows		= 0;					// Liczba wierszy
		int		columns		= 0;					// Liczba kolumn
		int[][]	arr;								// Tablica danych

		test_num = sc.nextInt(); 				// Liczba zestawow
		while (test_num-- > 0) { 				// Petla do wczytywania zestawow
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
			int		cur_sum				= 0; 	// Aktualna suma
			int		max_sum				= -1; 	// najwieksza suma
			int		cur_row_start		= 0;
			int		cur_row_end			= 0;
			int		cur_row_size		= 0;
			int		cur_column_start	= 0;
			int		cur_column_end		= 0;

			int		max_row_start		= 0;
			int		max_row_end			= 0;
			int		max_row_size		= 0;
			int		max_column_start	= -1;
			int		max_column_end		= 0;
			int		cur_column_size		= 0;
			int		max_column_size		= 0;

			int[]	arr_extended		= new int[columns];
			//Section Algorithm itself
			for (int i = 0; i < rows; i++) {	// O(n^2)
				for (int j = i; j < columns; j++) {
					arr_extended[j] = 0;		// Wyzerowanie pomocniczej tablicy
				}
			}
			for (int i = 0; i < rows; i++) {
				for (int j = i; j < rows; j++) {
					for (int k = 0; k < columns; k++) {
						arr_extended[k] = arr_extended[k] + arr[j][k];
						// Roszerzamy sumowany "fragment"
						// Szukamy algorytmem kadane najwieksza sume elementow danego wiersza + nastepnych j wierszy  
					}
					//Section Kadane
					int	k_sum		= -1;
					int	k_max_sum	= -1;

					for (int k = 0; k < columns; k++) {
						cur_column_end = k;
						if (sum <= 0) {
							cur_column_start = cur_column_end;
						}
					}
				}

			}

		}

	}

	static int Kadane(int[] a, int N) {
		int	cur_sum			= 0;
		int	cur_sum_start	= 0;
		int	cur_sum_end		= 0;
		int	cur_size		= 0;

		int	max_sum			= 0;
		int	max_sum_start	= 0;
		int	max_sum_end		= 0;
		int	best_size		= 0;

		for (int i = 0; i < N; i++) {
			cur_sum_end = i;
			if (cur_sum > 0) { // Dodajemy elementy do najlepszego fragmentu tylko jezeli zwiekszaja sume
				cur_sum= cur_sum+ a[i];
				cur_size++;
			}
			else {
				cur_sum_start = cur_sum_end;
				cur_size=1;
				cur_sum = a[i];
			}

			// cur_sum = cur_sum + a[i];
			// if (cur_sum <= 0) {
			// 	cur_sum = 0;
			// 	cur_sum_start = i + 1;
			// } else if (cur_sum > max_sum || (cur_sum == max_sum && (cur_sum != 0 || max_sum < 0))) {
			// 	max_sum = cur_sum;
			// 	maxSum_start_index = cur_sum_start;
			// 	maxSum_end_index = i;
			}
		}break;return 0;
}}