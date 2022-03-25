// Maksim Zdobnikau - 2

import java.util.Scanner;

public class Source {
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
			int		cur_column_size		= 0;

			int		max_row_start		= 0;
			int		max_row_end			= 0;
			int		max_row_size		= 0;
			int		max_column_start	= -1;
			int		max_column_end		= 0;

			int		max_column_size		= 0;

			int[]	arr_extended		= new int[columns];
			//Section Algorithm itself
			for (int i = 0; i < rows; i++) {	// O(n^2)
				for (int k = 0; k < columns; k++) {
					arr_extended[k] = 0;		// Wyzerowanie pomocniczej tablicy
				}

				cur_row_start = i; // Poczatek aktualnie sprawdzanego podfragmentu

				for (int j = i; j < rows; j++) {
					cur_row_end = j; // Koniec aktualnie sprawdzanego podfragmentu
					cur_row_size = cur_row_end - cur_row_start; // Rozmiar aktualne sprawdzanego fragmentu

					for (int k = 0; k < columns; k++) {
						arr_extended[k] = arr_extended[k] + arr[j][k];
						// Roszerzamy sumowany "fragment"
						// Szukamy algorytmem kadane najwieksza sume elementow danego wiersza + nastepnych j wierszy  
					}
					//Section Kadane

					int[] kadane_results = Kadane(arr_extended, columns);
					cur_sum = kadane_results[0];
					cur_column_start = kadane_results[1];
					cur_column_end = kadane_results[2];
					cur_column_size = kadane_results[3];
					//Section Kadane End

					if ((cur_sum > max_sum) // Zamieniamy fragment najlepszej sumy jesli suma elementow tego fragmentu jest silnie wieksza 
							|| ((cur_sum == max_sum)														// Lub jest rowny
									&& (cur_column_size * cur_row_size < max_column_size * max_row_size)) 	// ale ma mniejszy rozmiar
					) {
						// if ((cur_sum > max_sum) || (cur_sum == max_sum && cur_row_size < max_row_size)) {
						// 	if (cur_column_size < max_column_size || cur_row_size < max_row_size || max_sum < cur_sum) {

						max_sum = cur_sum;

						max_column_start = cur_column_start;
						max_column_end = cur_column_end;
						max_column_size = cur_column_size;

						max_row_start = cur_row_start;
						max_row_end = cur_row_end;
						max_row_size = cur_row_size;
					}

				}
			}
			if (max_sum < 0) {
				System.out.println("empty");
			} else {
				String result = nz + ": n = "
						+ rows + " m = " + columns
						+ ", s = " + max_sum
						+ ", mst = a[" + max_row_start + ".." + max_row_end
						+ "][" + max_column_start + ".." + max_column_end + "]";
				System.out.println(result);
			}
		}
	}

	static int[] Kadane(int[] arr, // Tablica wejsciowa
			int N) // Rozmiar tablicy
	{
		int	cur_sum				= -1;
		int	cur_sum_start		= 0;
		// int	cur_sum_end			= 0;
		int	cur_sum_size		= 0;

		int	max_sum				= -1;
		int	max_sum_start		= 0;
		int	max_sum_end			= 0;
		int	max_sum_min_size	= 0;

		for (int i = 0; i < N; i++) {

			if (cur_sum <= 0) {
				cur_sum = arr[i];
				cur_sum_start = i;
				cur_sum_size = 1;
			} else { // Dodajemy elementy do najlepszego fragmentu tylko jezeli zwiekszaja sume
				cur_sum = cur_sum + arr[i];
				cur_sum_size++; // W takim przypadku jest rowniez zwiekszony rozmiar fragmentu
			}
			if (cur_sum > max_sum // Zamieniamy fragment najlepszej sumy jesli suma elementow tego fragmentu jest silnie wieksza 
					|| (cur_sum == max_sum && cur_sum_size < max_sum_min_size) // Lub rozmiar fragmentu jest mniejszy
			) {
				max_sum = cur_sum;
				max_sum_start = cur_sum_start;
				max_sum_end = i;
				max_sum_min_size = cur_sum_size;
			}

		}
		int[] wrapper = new int[] { max_sum, max_sum_start, max_sum_end, max_sum_min_size };
		return wrapper;
	}

	public static int[] Kadane2(int[] dataSet, int dataSetSize) {
		int	sum				= -1;
		int	bestSum			= -1;
		int	columnStart		= 0;
		int	bestColumnStart	= 0;
		int	columnEnd;
		int	bestColumnEnd	= 0;
		int	size			= 0;
		int	bestSize		= 0;

		for (int i = 0; i < dataSetSize; i++) {
			columnEnd = i;
			if (sum <= 0) {
				sum = dataSet[i];
				columnStart = columnEnd;
				size = 1;
			} else {
				sum += dataSet[i];
				size++;
			}

			if ((bestSum < sum) || (bestSum == sum && size < bestSize)) {
				bestSize = size;
				bestSum = sum;
				bestColumnStart = columnStart;
				bestColumnEnd = columnEnd;
			}
		}

		return new int[] { bestSum, bestColumnStart, bestColumnEnd, bestSize };

	}
}