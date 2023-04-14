class zagadka {
	// Zagadka
	// Mamy ogromny (1GB) plik wypelniony przemieszanymi literami A-Z, a-z i cyframi 1-9.
	// Chcemy go posortowac i zapisac. Jak zrobic to aby sortowanie bylo jak najszybsze i jak zapisac wynikowy plik w jak najbardziej skompresowanej formie?

	// Idea rozwiazania
	// Możemy wziąc pod uwage to, że plik zawiera jedynie cyfry [kody ascii 48-57], literzy A-Z [65-90], a-z [97-122]
	// Wtedy jedynie musimy zliczyc liczbe wystapien kazdej litery lub cyfry w zakresie [48-57]u[48-57]u[97-122]
	// Przykladowa implementacja dla napisu podana ponizej w funkcji sortSrt, analogicznie możemy rozwiązać podany problem dla pliku z danymi.
	// Natomiast jeżeli chcemy zapisać wynik w jak najbardziej skompresowanej formie, możemy zamiast zapisywania posortowanego ciągu zapisać do pliku liczbe wystapien wszystkich liczb i liter
	// Złożoność w obu przypadkach wynosi O(n), bo zakres mozliwych wartosci jest ograniczony 

	static String sortStr(String str) {
		final int		N		= 123;
		StringBuilder	result	= new StringBuilder("");

		int				count[]	= new int[N];
		char[]			symbols	= str.toCharArray();
		for (char x : symbols) {
			count[x - '0']++;
		}

		for (int i = '0' - '0'; i <= '9' - '0'; i++) {
			for (int j = 0; j < count[i]; j++) {
				result.append((char) (i + '0'));
			}
		}
		for (int i = 'A' - '0'; i <= 'Z' - '0'; i++) {
			for (int j = 0; j < count[i]; j++) {
				result.append((char) (i + '0'));
			}
		}
		for (int i = 'a' - '0'; i <= 'z' - '0'; i++) {
			for (int j = 0; j < count[i]; j++) {
				result.append((char) (i + '0'));
			}
		}
		return result.toString();
	}

	static String countStr(String str) {
		final int		MAX_CHAR	= 123;
		StringBuilder	result		= new StringBuilder("");
		// Hash array to keep count of characters.
		int				count[]		= new int[MAX_CHAR];
		char[]			symbols		= str.toCharArray();
		for (char x : symbols) {
			count[x - '0']++;
		}

		for (int i = '0' - '0'; i <= '9' - '0'; i++) {
			if (count[i] != 0) {
				result.append(i).append(": ").append(count[i]).append("\n");
			}
		}
		for (int i = 'A' - '0'; i <= 'Z' - '0'; i++) {
			if (count[i] != 0) {
				result.append(i).append(": ").append(count[i]).append("\n");
			}
		}
		for (int i = 'a' - '0'; i <= 'z' - '0'; i++) {
			if (count[i] != 0) {
				result.append(i).append(": ").append(count[i]).append("\n");
			}
		}
		return result.toString();
	}

	public static void main(String[] args) {
		String	data	= "bol1AEajG2005BfoiszdjfNT2vasdfl93G2F5asj55dkfL1ujaKd8sPz";
		String	result	= sortStr(data);
		System.out.println(result);
		String compactResult = countStr(data);
		System.out.print(compactResult);
	}
}
