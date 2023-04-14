class TowersApp {
	static int nDisks = 4;

	public static void main(String[] args) {
		Towers(nDisks, 'A', 'C', 'B', "");
	}

	//-----------------------------------------------------------
	public static void Towers(int n, char src, char inter, char dest, String recursiveTab) {
		System.out.println("(" + n + "dyski): " + src + ", " + inter + ", " + dest);
		if (n == 1)
			System.out.println(recursiveTab + "Dysk 1 z " + src + " na " + dest);
		else {
			Towers(n - 1, src, dest, inter, recursiveTab + "\t"); // z zrodlowej na pomocnicza…
			System.out.println(recursiveTab + "Dysk ost " + n + " z " + src + " na " + dest);
			// przesuwamy ostatni
			Towers(n - 1, inter, src, dest, recursiveTab + "\t"); // z pomocniczej na docelowa
		}
		// System.out.println("Koniec (" + n + " dyski)");
	}
	//-------------------------------------------------------------
} // koniec klasy TowersApp