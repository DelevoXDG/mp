// Maksim Zdobnikau - 2

import java.util.Scanner;

// Ogolna idea rozwiazania
// Kazdy ciag ma iterator - indeks, ktory przechowuje aktualna pozycje, ktora musimy nastepnie dodac do wynikowej tablicy
// Z wzystkich aktualnych "wartosci" (dla aktualnego iteratora) ciagow mozemy utworzyc kolumne
// Nastepnie z ciagow tworzymy kopiec typu min - zlozonosc tej operacji O(n)
// Korzystamy z tej wlasnosci kopca, ze ma najmniejszy element jako pierwszy, zatem ustawiamy go na pierwszy indeks tablicy wynikowej
// Przesuwamy iterator dla ciagu, element z ktorego juz zostal wyciagniety i dodany do wyniku
// Naprawiamy warunek kopca minimalnego dla kolumny - uzywajac operacji downHeap - zlozonosc O(logN)
// Jezeli juz przeszliszmy po wzystkich elementach ciagu iterowanego, "usuwamy" go z kopcu, tworzymy kolumne i naprawiamy warunek kopca bez tego elementa
// Poniewaz musimy wykonac tworzenie kopca - O(n), a potem operacje downHeap dla maksymalnie M kolumn o wysokosci co najwyzej M, otrzymujemy zlozonosc
// O(M*N*logN)

class Helpers { // Funkcje pomocnicze
	public static <T> void swap(T[] arr, int A, int B) {
		// Funkcja generyczna, zamiena elementy w tablicy dowolnego typu
		T temp = arr[A];
		arr[A] = arr[B];
		arr[B] = temp;
	}
}

class iterableArray {
	// Klasa tablicy z iteratorem
	// Pozwala uzywac wartosci tablicy dla tworzenia "kolumny" z tablic
	private int[] arr; 			// Tablica - ciag posortowanych elementow
	private int iterator;		// Iterator - aktualny indeks danej tablicy

	public iterableArray(int[] arr, int index) {
		// Konstruktor, ustawia tablice i indeks przekazane argumentami 
		this.arr = arr;
		this.iterator = index;
	}
	public int[] getArray() {
		// Funkcja zwraca referencje dla tablicy
		return this.arr;
	}
	public void setArray(int[] arr) {
		// Ustawia referencje na przekazana tablice
		this.arr = arr;
	}
	public int getIndex() {
		// Zwraca aktualny indeks iteratora
		return this.iterator;
	}

	public void setIndex(int index) {
		// Ustawia indeks iteratora
		this.iterator = index;
	}

	public int getValue() {
		// Zwraca wartosc tablicy dla aktualnego indeksu iteratora
		if (this.iterator >= this.arr.length) {
			// Blad jezeli wychodzimy poza zakres
			throw new ArrayIndexOutOfBoundsException("Index " + (this.iterator + 1) + " for size " + this.arr.length);
		}
		return this.arr[iterator];
	}

	public void moveRight(int offset) {
		// Przesuwa iterator o wartosc wskazana argumentem
		this.iterator += offset;
	}

	public boolean isEmpty() {
		// Sprawdza, czy juz przeszlismy po wszystkich elementach tego ciagu
		return iterator >= arr.length;
	}

	public int compareTo(iterableArray arr) {
		// Operator porownywania dla wartosci tablicy o indeksie aktualnego iteratora 
		if (this.getValue() == arr.getValue()) {
			return 0;
		}
		if (this.getValue() < arr.getValue()) {
			return -1;
		}
		return 1;
		// Zwracam wartosci jak standardowa funkcja compareTo w javie
	}
}

class minHeap {			// Klasa kopca typu MIN
	private iterableArray[] matrix;	// Zawiera macierz - tablice ciagow posortowanych z iteratorem
	private int N;					// Aktualna dlugosc kopca

	minHeap(int N) {				// Tworzenie tablicy dla kopca o dlugosci N
		this.matrix = new iterableArray[N];
		this.N = N;
	}

	void build() {					// Budowanie kopca z poprawnym warunkiem MIN
		for (int i = (N - 1) / 2; i >= 0; i--) {
			// Przenoszenie wielkich elementow, nie spelniajacych warunek kopca na odpowednie miejsce w tablicy
			downHeap(i);
		}
	}
	public iterableArray getMin() {
		// Zwraca najmniejszy element w tablicy
		// Z warunku kopca to jest pierwszy element
		return this.matrix[0];
	}

	private int getMinChildPos(final int pos) {
		// Zwraca indeks potomka o mniejszej wartosci
		int	L	= 2 * pos + 1;
		int	R	= 2 * pos + 2;
		if (R >= N || matrix[R].compareTo(matrix[L]) > 0) {
			return L;
		}
		return R;
	}
	private int getParentPos(final int pos) {
		return (pos - 1) / 2;
	}

	public void upHeap(int pos) {
		// przechodzimy od wezla k do korzenia
		// indeks poprzednika elementu a[k]
		iterableArray goingUp = matrix[pos];
		for (int i = getParentPos(pos); // Przechodzimy od poprzednika argumenta po kolejnych poprzednikach
				pos > 0 && matrix[i].compareTo(goingUp) > 0; i = getParentPos(i)) {  // przejdz do poprzednika
			matrix[pos] = matrix[i];
			pos = i; // przeniesc wezel w dol
		} // end while
			// teraz element a[k] na swoje miejsce
		matrix[pos] = goingUp;
	}

	public void downHeap(int k)
	// podobnie jak wstawianie do listy w insertionsort
	// przechodzimy sciezka od wezla k do liscia
	{
		iterableArray	goingDown	= matrix[k];
		boolean			notBuilt	= (k < N / 2);
		while (notBuilt) {
			// int L = 2 * k + 1; // indeks lewego nastepnika a[k]
			// if (L < N - 1) {
			// if (a[L].compareTo(a[L + 1]) > 0) {
			// L++;
			// }
			// }
			int L = getMinChildPos(k);
			// wybierz wiekszy z nastepnikow
			// a[j]– wiekszy z nastepnikow wezla a[k]
			if (goingDown.compareTo(matrix[L]) > 0) {
				matrix[k] = matrix[L];
				k = L;
				notBuilt = (k < N / 2);
			} else {
				notBuilt = false; // warunek kopca OK
			}
			// wpp. przesun aktualny element do gory
		} // po zakonczeniu petli:
		matrix[k] = goingDown;
		// [ ] remove
		// int j;
		// while (k < N / 2) {
		// j = 2 * k + 1; // indeks lewego nastepnika a[k]
		// if (j < N - 1 && matrix[j].compareTo(matrix[j + 1]) < 0)
		// j++;
		// // wybierz wiekszy z nastepnikow
		// // a[j]– wiekszy z nastepnikow wezla a[k]
		// if (goingDown.compareTo(matrix[j]) >= 0) {
		// break; // warunek kopca OK
		// }
		// // wpp. przesun aktualny element do gory
		// matrix[k] = matrix[j];
		// k = j;
		// } // po zakonczeniu petli:
		// matrix[k] = goingDown;
	}

	public void setArr(int i, iterableArray arr) {
		this.matrix[i] = arr;
	}

	public int getLength() {
		return N;
	}

	public void setLength(int N) {
		this.N = N;
	}

	public iterableArray[] getArray() {
		return this.matrix;
	}

	public void setArray(iterableArray[] matrix) {
		this.matrix = matrix;
	}
}

class Concatenated {
	private int[] result;

	public Concatenated(int[][] arrs) {
		this.result = mergeSorted(arrs);
	}

	private static int[] mergeSorted(int arrs[][]) {
		int		N			= arrs.length;
		minHeap	heap		= new minHeap(N);
		int		totalCount	= 0;
		for (int i = 0; i < N; i++) {
			iterableArray arr = new iterableArray(arrs[i], 0);
			heap.setArr(i, arr);
			totalCount += arrs[i].length;
		}
		heap.build(); // O(N)

		int[] result = new int[totalCount];

		for (int i = 0; i < totalCount; i++) {
			iterableArray cur = heap.getMin();
			result[i] = cur.getValue();

			cur.moveRight(1);

			if (cur.isEmpty()) {
				Helpers.swap(heap.getArray(), 0, heap.getLength() - 1);
				heap.setLength(heap.getLength() - 1);
			}
			heap.downHeap(0);
		}
		return result;
	}

	@Override public String toString() {
		StringBuilder	sb	= new StringBuilder("");
		int				N	= result.length;
		for (int i = 0; i < N - 1; i++) {
			sb.append(result[i]).append(" ");
		}
		if (N > 0) {
			sb.append(result[N - 1]);
		}
		return sb.toString();
	}

} // Scanner do wczytywania danych wejsciowych

public class Source {
	public static Scanner sc = new Scanner(System.in); 	// Tworzenie obiektu klasy StringBuilder dla sprytnego
														// dopisywaniaia

	public static void main(String[] args) {
		StringBuilder	output		= new StringBuilder(""); // Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania
		// wynikow
		int				test_count	= sc.nextInt(); // Liczba zestawow testowych
		while (test_count-- > 0) { // Przechodzimy przez kazdy zestaw
			String	result		= "";
			int		arrCount	= sc.nextInt();
			int[]	lengths		= new int[arrCount];
			for (int i = 0; i < arrCount; i++) {
				lengths[i] = sc.nextInt();
			}
			int[][] arrs = new int[arrCount][];
			for (int i = 0; i < arrCount; i++) {
				arrs[i] = new int[lengths[i]];
				for (int j = 0; j < lengths[i]; j++) {
					arrs[i][j] = sc.nextInt();
				}
			}
			output.append(new Concatenated(arrs)).append("\n");
		}
		System.out.print(output); // Wypisywanie wyjscia
	}
}
