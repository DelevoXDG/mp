// Maksim Zdobnikau - 2

import java.util.Scanner;

// Ogolona idea rozwiazania
// Rozwiazanie opiera sie na metodzie rekurencyjnej "Dziel i zwyciezaj"
// Dla kazdej tablicy T:
// 1) Wyznaczamy indeks M ostatniego elementu lewej polowy tablicy
// 2) dzielimy tablice na dwie podtablice tabA = {T0, T1, ..., TM} i tabB = {TM+1, ..., TN}
// 3) Potem dzielimy na polowe tabA -> [tabA1,tabA2] i tabB -> [tabB1, tabB2],
// 4) I zamieniamy tabA2 i tabB1 (przez swapowanie, czyli "na miejscu"), dostajemy tabA -> [tabA1,tabB1] tabB -> [tabA2, tabB2],
// 5) Teraz musimy rekurencyjnie potasowac analogicznie tablice tabA, tabB
// ... Poki nie dojdziemy do przypadku podtablicy z dwoch elementow, taka podtablica juz jest potasowana
// 6) Dzielimy teraz na podtablice jednoelementowe i rozwazamy jedynie najwiekszy wspolny prefiks
//
// Tez musimy uwzglednic nastepne przypadki, bo nie mozemy w taki sposob swapowac tablicy o nieparzystej liczbie elementow:
// 1) gdy indeks ostatniego elementu tablicy tabA jest parzysty i liczba elementow jest nieparzysta, czyli nie da sie podzielic tabT na rowne tabA i tabB
// 2) gdy indeks ostatniego elementu tablicy tabA jest nieparzysty, ale liczba elementow jest parzysta, czyli
// nie da sie podzielic tabA i tabB na rowne [tabA1, tabA2], [tabB1, tabB2]
// W 1) przyapdku:
// musimy ostatni element tablicy tabA (czyli ten, ktory musi ostatnim elementem tablicy piosenek z pierwszej plyty), ustawic na koniec calej tablicy tabT
// czyli w miejsce, w ktorym ten element musi byc w potasowanej tablicy piosenek wedlug tresci
// Zatem musimy potasowac tablice tabT' (tabT bez ostatniego elementu), czyli zmniejszamy indeks ostatniego elementu, wyznaczamy ponownie indeks ostatniego
// elementu tabA i teraz mozemy poprawnie podzielic tabT' na tabA i tabB
// W 2) przypadku:
// mozemy podzielic tabT na dwie podtablicy, ale beda to podtablicy o nieparzystej liczbie elementow, ktore nie mozemy podzielic na tabA1, tabA2, tabB1, tabB2
// Zatem, mozemy przeniesc ostatni element pierwszej podtablicy na przedostatnie mejsce w calej tablicy T
// Wtedy mamy potasowane dwa ostatnie elementy w calej tablicy tabT
// Zatem musimy potasowac tablice tabT' (tabT bez ostatnich dwuch elementow), czyli zmniejszamy indeks ostatniego elementu, wyznaczamy ponownie indeks
// ostatniego elementu tabA i teraz mozemy poprawnie podzielic tabA i tabB na [tabA1, tabA2], [tabB1, tabB2]

// Wyjasnenie zlozonosci
// Rozwiazanie opiera sie na metodzie rekurencyjnej "dziel i zwyciezaj"
// Kazda tablicy dzielimy na dwie prawie rowne (+-1 element) podtablice
// 1 poziom - co najwyzej 2 podtablicy
// 2 poziom - co najwyzej 4 podtablicy
// 3 poziom - co najwyzej 8 podtablic
// ...

// N poziom - co najwyzej log(N) podtablic
// Jest to standardowa zlozonosc drzewa binarnego, w algorytmach, w kazdym wywolaniu dzieliacych aktualny problem na dwa rowne podproblemy
// poniewaz swapowanie w kazdym poziomie drzewa jest co najwyzej w czasie O(n) cala zlozonosc tasowania jest w O(N*logN)
// zlozonosc wyszukiwania prefiksu ma zlozonosc O(n), bo dlugosc piosenek jest skonczona
// Zatem zlozonosc calej funkcji jest O(N*logN)

class musicCollection {					// Klasa dla przechowywania informacji o kolekcji piosenek
	private String[] songs;				// Tablica piosenek
	private String longestPrefix;		// Nadluzszy wspolny prefiks

	musicCollection(String[] array) {	// Konstruktor, ktory tez uruchamia funkcje tasowania piosenek i wyszukiwania prefiksu
		songs = array;					// ustawiamy referencje na podana argumentem tablice
		if (array.length != 0) {
			longestPrefix = songs[0];	// Ustawiamy domyslny prefiks na pierwsza nazwe piosenki
		}
		this.arrangeAndFindPrefix(0, songs.length - 1);	// Uruchamiamy funckje, wymagana w tresci
	}
	private static boolean isOdd(final int num) {	// Zwraca, czy liczba jest nieparzysta
		return num % 2 != 0;
	}
	private static boolean isEven(final int num) {	// Zwraca, czy liczba jest parzysta
		return num % 2 == 0;
	}
	private static void shiftLeftArray(String[] arr, final int start, final int end, final int offset) {
		// Przesowanie elementow tablicy wlewo o offset w przedziale [start, end-1]
		for (int i = start; i < end - offset; i++) {
			arr[i] = arr[i + offset];
		}
	}
	private static int getFloor(final double num) {	// Zwraca podloge liczby typy double
		return (int) Math.round(Math.floor(num));
	}

	private void swap(String[] arr, int a, int b) {	//Zamiana elemetnow w tablicy
		String tmp = arr[a];
		arr[a] = arr[b];
		arr[b] = tmp;
	}
	public void arrangeAndFindPrefix(int begin, int end) {	// Wymagana funkcja rekurenyjna, przyjmuje jako argumenty indeksy pierwszego i ostatniego elementow tablicy
		int curLen = end - begin + 1;		// Dlugosc aktualnie tasowanej tablicy
		if (curLen <= 0) {	// Jezeli probujemy potasowac tablice, w ktorej nie ma elementow
			return;
		}
		if (curLen == 1) {	//Jezeli liczba elementow tablicy == 1
			// Znalezenie najdluzszego wspolnego prefiksu
			String	song		= songs[begin];	// Sring aktualnej piosenki
			int		maxLength	= Math.min(song.length(), longestPrefix.length()); // Prefiks jest <= nazwy aktualnej piosenki lub najdluzszego prefiksu

			for (int i = 0; i < maxLength; i++) {	//Przechodzimy po wszystkich symbolach aktualnej piosenki 
				if (song.charAt(i) != longestPrefix.charAt(i)) { 	//Porownywujemy z juz istniejacym nadluzszym prefiksem
					longestPrefix = longestPrefix.substring(0, i);	//Jezeli symboli nie sa rowne wyciagamy jak nowy najwiekszy prefiks wszystkie symboli o mniejszych indeksach od tego, na ktorym wystapiala nierownosc
					return;											// Prefiks zostal zaktualizowany
				}
			}
			longestPrefix = longestPrefix.substring(0, maxLength);
			return;		// Prefiks sie nie zmienil
		}
		if (curLen == 2) {	// Nie trezeba tasowac piosenek, jedynie pozostalo sprawdzic nadluzszy wspolny prefiks
			arrangeAndFindPrefix(begin, begin); // dla piosenkiA
			arrangeAndFindPrefix(end, end);		// i dla piosenkiB
			return;
		}
		int last_tabA = getFloor((begin + end) / 2d);	// Indeks ostatniego elementu lewej podtablicy tabA
		if (isEven(last_tabA)) {						// Jezeli indeks jest parzysty
			// Rozwazamy dwa przypadki, opisane w ogolnej idee rozwiazania
			String tmp = songs[last_tabA];	// Musimy przenisc ostatni element lewej podtablicy tabA ...

			if (isOdd(curLen)) {
				shiftLeftArray(songs, last_tabA, end + 1, 1);
				songs[end] = tmp;			// ... Na koniec tablicy tabT
				// Mamy juz na prawidlowym miejscu ostatni element tabT
				// Ale musimy sprawdzic prefiks tego elementu
				arrangeAndFindPrefix(end, end);
				end = end - 1;
				// Musimy teraz potasowac tablice o jeden element mniejsza 
			} else {
				shiftLeftArray(songs, last_tabA, end, 1);
				songs[end - 1] = tmp;		// ... Na predostatnie miejscie w tablicy tabT
				// Mamy juz potasowane ostatnie dwa elementy tabT
				// Ale musimy sprawdzic prefiks tych elementow
				arrangeAndFindPrefix(end - 1, end - 1);
				arrangeAndFindPrefix(end, end);
				end = end - 2;
				// Musimy teraz potasowac tablice o dwa elementy mniejsza
			}
			last_tabA--;				// Odpowiednio aktualizujemy indeks ostatniego elementu tabA
		}
		int	first_tabB1	= last_tabA + 1;				// Znajac indeks ostatniego elementu tabA, mozemy znalezc indeks pierwszego elementu tabB 
		int	first_tabA2	= (begin + first_tabB1) / 2; 	// Teraz mozemy "podzielic tablice tabA na tabA1, tabA2" : znalezc indeks pierwzego elementu tablicy tabA2 
		int	len_tabA2	= last_tabA - first_tabA2 + 1;	// Tez potrzebna jest dlugosc tablicy tabA2

		for (int i = 0; i < len_tabA2; i++) {				// Swapujemy tablicy tabA2 i tabB1
			swap(songs, first_tabA2 + i, first_tabB1 + i);	// Swapujemy kolejne elementu tablic tabA2 i tabB1
		}
		// Zamiast tabT = [tabA1,tabA2, tabB1, tabB2]
		// Mamy teraz tabT = [tabA1, tabB1, tabA2, tabB2]

		// Teraz, poki w kazdej podtabicy nie pozostalo dwa elemnty 
		arrangeAndFindPrefix(begin, last_tabA);  	// Wykonujemy tasowanie dla tabA = [tabA1, tabB1]
		arrangeAndFindPrefix(last_tabA + 1, end);	// Wykonujemy tasowanie dla tabB = [tabA2, tabB2]
	}
	// void arrangeAndFindPrefix() {
	// 	arrangeAndFindPrefix(0, songs.length - 1);
	// }

	@Override public String toString() {				// Zwraca sformatowany wynik tasowania i znalezenia prefiksu
		StringBuilder	result	= new StringBuilder(""); //Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
		// result.append(songs.length).append("\n");
		int				len		= songs.length;	// liczba piosenek
		for (int i = 0; i < len; i++) {
			result.append(songs[i] + " ");	// dodajemy kolejne piosenki
		}
		// if (len >= 1) {
		// 	result.append(songs[len - 1]);
		// }
		result.append("\n").append(longestPrefix).append("\n");	// dodajemy prefiks
		return result.toString();	// zwracamy caly wynik
	}
}

public class Source {
	public static Scanner sc = new Scanner(System.in);			// Scanner do wczytywania danych wejsciowych

	public static void main(String[] args) {
		StringBuilder	output		= new StringBuilder(""); 	 	// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
		int				test_count	= sc.nextInt();				// Liczba zestawow testowych
		while (test_count-- > 0) {	 							// Przechodzimy przez kazdy zestaw
			int			songCount	= sc.nextInt();				// Liczba piosenek

			String[]	songs		= new String[songCount];	// Tablica piosenek
			for (int i = 0; i < songs.length; i++) {
				songs[i] = sc.next();							// Wczytujemy kolejne piosenki
			}
			musicCollection music = new musicCollection(songs);	// Inicjalizacja kolekcji piosenek, ktora tez tasuje piosenki i wyznacza najdluzszy wspolny prefiks
			output.append(music);								// Dodawanie wynikow na wyjscie
		}
		System.out.print(output);				// Wypisywanie wyjscia
	}
}

// test.00.in
// 10
// 1
// songName
// 2
// trackA1 trackB1
// 3 
// S1 s2 S3
// 4
// trackA1 trackB1 trackA2 trackB2
// 5
// S1 s2 S3 s4 S5
// 6 
// trackA1 trackB1 trackA2 trackB2 trackA3 trackB3
// 7
// S1 s2 S3 s4 S5 s6 S7
// 8
// trackA1 trackB1 trackA2 trackB2 trackA3 trackB3 trackA4 trackB4
// 9
// S1 s2 S3 s4 S5 s6 S7 s8 S9
// 10
// trackA1 trackB1 trackA2 trackB2 trackA3 trackB3 trackA4 trackB4 trackA5 trackB5

// test.00.out
// 1
// songName
// songName
// 2
// trackA1 trackB1
// track
// 3
// S1 S3 s2

// 4
// trackA1 trackA2 trackB1 trackB2
// track
// 5
// S1 s4 s2 S5 S3

// 6
// trackA1 trackB2 trackB1 trackA3 trackA2 trackB3
// track
// 7
// S1 S5 s2 s6 S3 S7 s4

// 8
// trackA1 trackA3 trackB1 trackB3 trackA2 trackA4 trackB2 trackB4
// track
// 9
// S1 s6 s2 S7 S3 s8 s4 S9 S5

// 10
// trackA1 trackB3 trackB1 trackA4 trackA2 trackB4 trackB2 trackA5 trackA3 trackB5
// track

// 11
// 1
// 6
// song song song song song son