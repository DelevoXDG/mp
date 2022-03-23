//Maksim Zdobnikau
package homework.ht1;

class Vector {

    private int[] a; // referencja do wektora
    private int maxSize; // maksymalna dlugosc wektora
    private int n; // aktualna dlugosc wektora

    public Vector(int m) { // konstruktor
        maxSize = m;
        n = 0;
        a = new int[maxSize];
    }

    // Dopisz:

    // konstruktor przyjmujacy maksymalny rozmiar "m" i wypelniajacy wektor
    // "l" losowymi wartosciami z przedzialu 0-100 (prosze znalezc jak losowac w
    // Internecie)
    public Vector(int m, int l) {
        maxSize = m;
        a = new int[maxSize];
        if (l <= maxSize) { // Setting numbers of random values to add up to maxSize
            n = l;
        } else {
            n = maxSize;
            System.out.println("Max vector size reached.");
        }
        int max = 100;
        int min = 0;
        for (int i = 0; i < n; i++) { // Filling vector with random values
            a[i] = (int) (Math.floor(Math.random() * (max - min + 1) + min));
        }
    }

    // wyswietl wektor na ekran
    public void display() {
        for (int i = 0; i < n; i++) {
            System.out.println(a[i]);
        }
    }

    // zwroc Stringa z kolejnymi wartosciami z wektora rozdzielonymi spacja
    @Override
    public String toString() {
        String vector_string = "";
        int i = 0;
        for (i = 0; i < n - 1; i++) {
            vector_string = vector_string + a[i] + " ";
        }
        if (n > 0) {// no space after last value
            vector_string = vector_string + a[i];
        }
        return vector_string;
    }

    // wstaw "x" na koniec wektora (uwaga na mksymalny rozmiar)
    public void insert(int x) {
        if (n + 1 <= maxSize) {// Bounds checking
            a[n] = x;
            n++; // Updating vector size
        } else {
            System.out.println("Max vector size reached.");
        }
    }

    // usun z wektora wszystkie wystapienia liczby "x"
    public void remove(int x) {
        int shift = 0;
        for (int i = 0; i < n; i++) {
            a[i - shift] = a[i]; // Shifting all values in vector based on number of removed elements
            if (a[i] == x) {
                shift++;
            }
        }
        n -= shift; // Updating vector size
    }

    // zwraca element o podanym indeksie (uwaga na nieprawidlowe indeksy)
    public int at(int i) {
        if (i >= 0 && i < n) { // Bounds checking
            return a[i];
        } else {
            System.out.print("Incorrect index. ");
        }
        return 0;
    }

    // Stworz w funkcji main prezentacje wszystkich funkcji
    public static void main(String[] args) {
        Vector v0 = new Vector(5);
        Vector v1 = new Vector(10, 5);
        System.out.println("Vector0 (display)");
        v0.display();
        System.out.println("Vector1 (random elements) (display)"); // Constructor with random values
        v1.display();

        System.out.println("Adding elements to vector0");
        v0.insert(33);
        v0.insert(22);
        v0.insert(11);
        v0.insert(33);
        v0.insert(33);
        v0.insert(55); // Exceeds limit
        System.out.println("Vector0 (display)");
        v0.display();

        System.out.println("Vector0 (toString)");
        System.out.println(v0.toString());
        System.out.println("Vector1 (toString)");
        System.out.println(v1.toString());

        System.out.println("Removing elements equal to 33 from vector0");
        v0.remove(33);
        System.out.println("Vector0 (display)");
        v0.display();
        System.out.println("Vector0 (at)");
        for (int i = -1; i < 3; i++) {
            // System.out.printf("[%3d]", i);
            // System.out.printf("%3d%n", v0.at(i));
            System.out.printf("[%2d] ", i);
            System.out.printf("%-20s %n", v0.at(i));
        }
        System.out.println("Vector0 (toString)");
        System.out.println(v0); // ==System.out.println(v0.toString());
        System.out.println("Vector1 (toString)");
        System.out.println(v1);
        System.out.println("Removing elements equal to 22 from vector0");
        v0.remove(22);
        System.out.println("Vector0 (toString): " + v0);
        System.out.println("Vector 2 (maxSize = 0)");
        Vector v2 = new Vector(0, 5); // Empty vector
        System.out.println("Adding 0 to vector2. ");
        v2.insert(0);
        System.out.println("Vector2 (toString): " + v2);
        System.out.println("Removing elements equal to 0 from vector2. ");
        v2.remove(0);
        System.out.println("Vector2 (toString): " + v2);
        System.out.println("Vector0 (at)");
        System.out.printf("[%2d] ", 0);
        System.out.printf("%-20s %n", v2.at(0));
        System.out.println("Vector2 (display)");
        v2.display();
        System.out.println("Vector2 (toString)");
        System.out.println(v2);
    }
}