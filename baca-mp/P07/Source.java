// Maksim Zdobnikau - 2

import java.util.Scanner;

// Wyjasnenie zlozonosci

class Song {
	private String[] dataArr;
	private boolean sorted;

	@Override public String toString() {
		StringBuilder	result	= new StringBuilder(""); //Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
		int				N		= dataArr.length;
		for (int i = 0; i < N - 1; i++) {
			result.append(dataArr[i]).append(",");
		}
		if (N >= 1) {
			result.append(dataArr[N - 1]);
		}
		return result.toString();
	}

	public boolean isSorted() {
		return sorted;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}
	public void toggleSorted() {
		this.sorted = !this.sorted;
	}

	Song(String[] fields) {
		this.dataArr = fields;
		this.setSorted(false);
	}
	public String[] getFields() {
		return this.dataArr;
	}
	public String getData(int i) {
		return this.dataArr[i];
	}

	public void setFields(String[] fields) {
		this.dataArr = fields;
	}
}

class musicCollection {
	private String[] labels;
	private Song[] songs;
	private Order order;
	private int sortBy;
	public musicCollection(String[] labels, Song[] songs, int sortBy, int orderNo) {
		this.labels = labels;
		this.songs = songs;
		this.sortBy = sortBy;
		this.order = findOrder(orderNo);
		this.quickSort();
	}
	public static boolean isNaturalNum(String str) {
		for (char c : str.toCharArray()) {
			if (Character.isDigit(c) == false) {
				return false;
			}
		}
		return true;
	}

	private Order findOrder(int orderNo) {
		if (this.songs.length == 0) {
			return null;
		}
		if (isNaturalNum(songs[0].getData(sortBy))) {
			switch (orderNo) {
				case 1:
					return new NormalNum();
				case -1:
					return new ReverseNum();
				default:
					throw new IllegalArgumentException("Undefined order");
			}
		} else {
			switch (orderNo) {
				case 1:
					return new NormalStr();
				case -1:
					return new ReverseStr();
				default:
					throw new IllegalArgumentException("Undefined order");
			}

		}

	}

	public static interface Order {

		public boolean isBigger(String a, String b);
	}

	public static class NormalStr implements Order {
		public boolean isBigger(String a, String b) {
			int comp = a.compareTo(b);
			if (comp <= 0)
				return false;
			return true;
		}
	}
	public static class NormalNum implements Order {
		public boolean isBigger(String a, String b) {
			return Integer.parseInt(a) > Integer.parseInt(b);
		}
	}

	public static class ReverseStr implements Order {
		public boolean isBigger(String a, String b) {
			int comp = a.compareTo(b);
			if (comp >= 0)
				return false;
			return true;
		}
	}
	public static class ReverseNum implements Order {
		public boolean isBigger(String a, String b) {
			return Integer.parseInt(a) < Integer.parseInt(b);
		}
	}
	private int findNextR(int L) {
		int i = L;
		while (i < songs.length) {
			if (songs[i].isSorted() == true) {
				break;
			}
		}
		return i;
	}
	private void swapSongs(int a, int b) {
		Song tmp = songs[a];
		songs[a] = songs[b];
		songs[b] = tmp;
	}

	private int partition(int L, int R) {
		int		m		= (L + R) / 2;
		Song	pivot	= songs[m];
		while (L <= R) {
			while (order.isBigger(songs[R].getData(sortBy), pivot.getData(sortBy))) {
				R--;
			}
			while (order.isBigger(pivot.getData(sortBy), songs[L].getData(sortBy))) {
				L++;
			}
			if (L <= R) {
				swapSongs(L, R);
				L++;
				R--;
			}
		}
		return L;
	}
	void selectionSort(int L, int R) {
		int size = R - L + 1;
		for (int i = L; i < size - 1; i++) {
			int min_idx = i;
			for (int j = i + 1; j < size; j++) {
				// if (songs[j] < songs[min_idx])
				if (order.isBigger(songs[min_idx].getData(sortBy), songs[j].getData(sortBy))) {
					min_idx = j;
				}
			}
			swapSongs(i, min_idx);
		}
	}

	public void quickSort() {
		int	N		= songs.length;
		int	L		= 0;
		int	R		= 0;
		int	curR	= N - 1;

		while (true) {
			R--;
			while (L < curR) {
				int size = curR - L + 1;
				if (size <= 5) {
					selectionSort(L, curR);
					L = curR;
					break;
				}
				int q = partition(L, curR);
				songs[curR].toggleSorted();
				curR = q - 1;
				R++;
			}

			if (R < 0) {
				break;
			}
			L++;
			curR = findNextR(L);
			songs[curR].toggleSorted();
		}
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
		// if (songCount >= 1) {
		// 	result.append(songs[songCount - 1]).append("\n");
		// }

		return result.toString();	// zwracamy caly wynik
	}
}

public class Source {
	public static void swap(String[] arr, int a, int b) {
		String temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}

	public static void bringForward(String[] arr, int sortByCol) {
		for (int j = sortByCol; j >= 1; j--) {
			swap(arr, j, j - 1);
		}
	}

	public static Scanner sc = new Scanner(System.in);			// Scanner do wczytywania danych wejsciowych

	public static void main(String[] args) {

		StringBuilder	output		= new StringBuilder(""); 	 	// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
		int				test_count	= Integer.parseInt(sc.nextLine());				// Liczba zestawow testowych
		while (test_count-- > 0) {	 							// Przechodzimy przez kazdy zestaw
			String					paramsStr	= sc.nextLine();
			String					pars[]		= paramsStr.split(",");

			int						songCount	= Integer.parseInt(pars[0]);				// Liczba piosenek
			int						sortByCol	= Integer.parseInt(pars[1]) - 1;
			int						orderNo		= Integer.parseInt(pars[2]);
			musicCollection.Order	order		= null;
			// if (Integer.parseInt() == 1) {
			// 	order = new musicCollection.Normal();
			// } else {
			// 	order = new musicCollection.Reverse();
			// }

			Song[]					songs		= new Song[songCount];	// Tablica piosenek
			String[]				labels		= sc.nextLine().split(",");
			bringForward(labels, sortByCol);
			for (int i = 0; i < songs.length; i++) {
				String		metaStr	= sc.nextLine();							// Wczytujemy kolejne piosenki
				String[]	meta	= metaStr.split(",");
				bringForward(meta, sortByCol);
				songs[i] = new Song(meta);
			}

			musicCollection music = new musicCollection(labels, songs, sortByCol, orderNo);	// Inicjalizacja kolekcji piosenek, ktora tez tasuje piosenki i wyznacza najdluzszy wspolny prefiks
			// output.(music);
			System.out.println(music);								// Dodawanie wynikow na wyjscie
		}
		// System.out.print(output);				// Wypisywanie wyjscia

	}
}

// test.00.in