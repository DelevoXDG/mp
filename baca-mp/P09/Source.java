// Maksim Zdobnikau - 2

// Ogolna idea rozwiazania
// Rozwiazanie opiera sie na wlasnosciach bst
// CREATE i HEIGHT zaimplementowane rekurencyjnie, reszta - iteracyjnie
// Funckje wypisywania preorder, inorder, postorder uzywaja wlasna implementacje stosu stanow wywolanej funkcji, czyli parametry funkcji i miejsce, od ktorego
// jest wykonywana
// Zlozonosc czasowa wszystkich operacyj co najwyzej liniowa

import java.util.Scanner;

class Params {				// Klasa parametrow aktualnego wywolania "rekurencyjnego" funkcji rekurencyjnej
	private int context;	// Od ktorego miejsca kontynujemy wykonywanie funkcji 
	public Node node;		// Argument funkcji rekurencyjnej
	public Params(int adrs, Node node) {	// Konstruktor
		this.context = adrs;
		this.node = node;
	}
	// Gettery
	public int getContext() {
		return context;
	}
	public Node getNode() {
		return node;
	}
}

class paramsStack {			// Stos przechowywujacy kolejne parametry dla funkcji symulacji funkcji rekurencyjnej
	private Params[] arr;	// Tablica dynamiczna dla trzymania parametrow
	private int top;		// Indeks ostatniego elementu na stosie
	paramsStack(final int initSize) {		// Konstruktor
		arr = new Params[initSize];
		top = 0;
	}
	public void push(final Params p) {		// Dodawanie
		if (this.isFull()) {				// Ewentualne zwiekszenie rozmiaru tablicy, jezeli wypelnilismy stos
			Params[] tmp = new Params[this.arr.length * 2];	// Tworzenie nowej podtablicy o dwa razy wiekszej pojemnosci i kopiowanie wartosci
			for (int i = 0; i < this.arr.length; i++) {
				tmp[i] = this.arr[i];
			}
			this.arr = tmp;		// Referencja wskazuje na nowa tablice
		}
		arr[top++] = p;		// Dodajemy na stos, zwiekszamy indeks ostatniego elementu 
	}
	public Params pop() {
		if (top - 1 < 0) {
			throw new ArrayIndexOutOfBoundsException("Stack underflow");
		}
		top--;
		return arr[top];	// Usuwamy i zwracamy ostatnio dodany element
	}
	public Params peek() {	// Przegladamy ostatnio dodany element stosu
		if (top <= 0) {
			throw new ArrayIndexOutOfBoundsException("Stack is empty");
		}
		return arr[top - 1];
	}
	public boolean isEmpty() {
		// Funkcja sprawdza, czy stos jest pusty
		return top == 0;
	}
	public boolean nonEmpty() {
		// Funkcja sprawdza, czy stos jest nie pusty
		return top != 0;
	}
	public boolean isFull() {
		// Funkcja sprawdza, czy stos jest wypelniony
		return top == arr.length;
	}
	public int size() {
		// Zwraca aktualny rozmiar stosu
		return top;
	}
}

class Person {
	public int priority;			// priorytet osoby
	public String name;				// imie osoby
	public String surname;			// Nazwisko osoby
	Person(String name, String surname, int priority) {
		this.name = name;
		this.surname = surname;
		this.priority = priority;
	}	// konstruktor
}	// koniec klasy Person

class Node {
	public Person info;		// element danych (klucz)
	public Node left;			// lewy potomek węzła
	public Node right;			// prawy lewy potomek węzła 

	Node(Person value) {
		this.info = value;
		this.left = null;
		this.right = null;
	}	// konstruktor
}
// koniec klasy Person

class BST {
	private Node root;		// Korzen drzewa

	BST() {
		this.root = null;
	}
	BST(Person[] arr, String orderName) {
		// Konstruktor, ktory rekurencyjnie tworzy drzewo BST na podstawie tablicy osob w zadanym porzadku  
		Order order = parseOrder(orderName, arr);	// Wyznaczamy interfejs porzadku

		// Uzuwamy funkcji rekurencyjnej tworzacej BST dla danego parzadku Order
		// Zaczunami o pierwdzego dla preorder i od ostatniego elementu dla postorder 
		this.root = order.Create(arr[order.getStartingIndex()], Integer.MIN_VALUE, Integer.MAX_VALUE);
		// Referencje do pierwszego elementu zbudowanego drzewa ustawiamy jako korzen
	}
	public Node getRoot() {			// Zwraca referencje do korzenia
		return this.root;
	}
	public void setRoot(Node root) {
		this.root = root;		// Ustawia referencje do przekazanego argumenta korzenia 
	}
	public Order parseOrder(String orderName, Person[] arr) {
		// Wyznaczamy interfejs porzadku w zaleznosci od prekazanego napisu
		switch (orderName) {
			case "PREORDER":
				return new preOrder(arr);
			case "INORDER":
				return new inOrder(arr);
			case "POSTORDER":
				return new postOrder(arr);
		}
		throw new IllegalArgumentException("Undefined order.");
	}
	public static String NodeToString(Person p) {
		// Zwracamy napis, ktory zawiera priorytet, imie i nazwisko osoby
		StringBuilder sb = new StringBuilder("");
		sb.append(p.priority).append(" - ").append(p.name).append(" ").append(p.surname);
		return sb.toString();
	}

	public interface Order {			// Intefejs porzadku
		int getStartingIndex();			// Zwraca indeks z ktoregi zaczynamy budowanie drzewa 

		Node Create(Person data, int MIN, int MAX);	// Rekurencyjna funkcja dla tworzenia drzewa 

		public String getString(Node start);		// Zwraca napis w odpowiednim porzadku przejscia po elementach
	}
	public class preOrder implements Order {
		Person[] arr;			// Tablica elementow w zadanym porzadku prioritetow
		int index;				// Indeks, ktore uzuwany w kolejnych wywolaniach funkcji rekurencyjnej, zeby sliedzic, ktore elementy juz dodalismy to BST 

		// konstruktor przyjmujacy tablice z ktorej nalezy zbudowac
		preOrder(Person[] arr) {
			this.arr = arr;
			this.index = 0;
		}
		// konstruktor domyslny
		preOrder() {
		}
		public int getStartingIndex() {
			// Zaczynamy od pierwszego elementa dla podanej tablicy postOrder

			this.index = 0;
			return this.index;
		}
		public Node Create(Person info, int MIN, int MAX) {
			// Funkcja rekurencyjna tworzaca drzewo na podstawie listy preOrder
			if (index >= arr.length) {	// Jezeli aktualny indeks wiekszy od dlugosci tablicy 
				return null;			// Mamy juz zbudowane poddrzewo
			}
			// Sprawdzamy, czy aktualny element nalezy do poddrzewa (miesci sie w przedziale (MIN, MAX))
			if (info.priority <= MIN) {
				return null;
			}
			if (info.priority >= MAX) {
				return null;
			}
			Node cur = new Node(info); 	// Tworzymy node z aktualnej osoby, to jest korzen aktualnego poddrzewa 
			index++;					// Przechodzimy do nastepnego elementu tablicy
			if (index < arr.length) {
				// Rekurencyjnie tworzymy lewe poddrzewo, w ktorym musza byc elementy w przedziale (MIN, cur)
				cur.left = Create(arr[index], MIN, cur.info.priority);
			}
			if (index < arr.length) {
				// Rekurencyjnie tworzymy prawe poddrzewo, w ktorym musza byc elementy w przedziale (cur, MAX)
				cur.right = Create(arr[index], cur.info.priority, MAX);
			}
			return cur;	// Zwracamy korzen aktualnego poddrzewa
		}
		public String getString(Node start) {
			// Zwracamy napis z elementami poddrzewa wypisanymi w porzadku preorder
			StringBuilder	sb		= new StringBuilder("");	// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
			paramsStack		stack	= new paramsStack(8);		// Stos parametrow dla symulacji funkcji rekurencyjnej
			stack.push(new Params(0, start));			// Odkladamy na stos gorne wywolanie
			while (stack.isEmpty() == false) {			// Poki stos nie jest pusty (mamy kolejne wywolania rekurencyjne)
				Params	curPars	= stack.pop();			// Pobieramy aktualne parametry
				Node	cur		= curPars.getNode();	// Aktualny node, dla ktorego stosujemy "rekurencyjna" funkcje
				switch (curPars.getContext()) {			// Przechodzimy w odpowiednie miejsce
					// Iteracyjnie robimy przejscie preorder
					// wypisz(cur)
					// preorder(cur.L)
					// preorder(cur.R)
					case 0: {
						if (cur == null) {
							break;
						}
						Person p = cur.info;
						sb.append(p.priority).append(" - ").append(p.name).append(" ").append(p.surname).append(", "); // dodawanie aktualnej osoby do napisu wynikowego
						stack.push(new Params(1, cur));
						stack.push(new Params(0, cur.left));
						break;
					}
					case 1: {
						stack.push(new Params(0, cur.right));
						break;
					}
				}
			}
			sb.setLength(Math.max(0, sb.length() - 2)); // Usuwamy zbedne znaki
			return sb.toString();	// Zwracamy napis wynikoy 
		}

	}
	public class inOrder implements Order {
		Person[] arr;			// Tablica elementow w zadanym porzadku prioritetow

		// konstruktor przyjmujacy tablice z ktorej nalezy zbudowac
		inOrder(Person[] arr) {
			this.arr = arr;
		}
		// konstruktor domyslny
		inOrder() {
		}

		public int getStartingIndex() {
			// Nie mamy funkcji budowania drzewa inorder
			return 0;
		}
		public Node Create(Person info, int MIN, int MAX) {
			// Nie mamy funkcji budowania drzewa inorder
			throw new IllegalStateException("Undefined");
		}
		public String getString(Node start) {
			// Zwracamy napis z elementami poddrzewa wypisanymi w porzadku inorder 
			StringBuilder	sb		= new StringBuilder("");// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
			paramsStack		stack	= new paramsStack(8);// Stos parametrow dla symulacji funkcji rekurencyjnej
			stack.push(new Params(0, start));// Odkladamy na stos gorne wywolanie
			while (stack.isEmpty() == false) {// Poki stos nie jest pusty (mamy kolejne wywolania rekurencyjne)
				Params	curPars	= stack.pop();// Pobieramy aktualne parametry
				Node	cur		= curPars.getNode();// Aktualny node, dla ktorego stosujemy "rekurencyjna" funkcje
				switch (curPars.getContext()) {// Przechodzimy w odpowiednie miejsce
					// Iteracyjnie robimy przejscie inorder
					// inorder(cur.L)
					// wypisz(cur)
					// inorder(cur.R)
					case 0: {
						if (cur == null) {
							break;
						}
						stack.push(new Params(1, cur));
						stack.push(new Params(0, cur.left));
						break;
					}
					case 1: {
						Person p = cur.info;
						sb.append(p.priority).append(" - ").append(p.name).append(" ").append(p.surname).append(", ");
						stack.push(new Params(0, cur.right));
						break;
					}
				}
			}
			sb.setLength(Math.max(0, sb.length() - 2));// Usuwamy zbedne znaki
			return sb.toString();// Zwracamy napis wynikoy 
		}

	}
	public class postOrder implements Order {
		Person[] arr;			// Tablica elementow w zadanym porzadku prioritetow
		int index;				// Indeks, ktore uzuwany w kolejnych wywolaniach funkcji rekurencyjnej, zeby sliedzic, ktore elementy juz dodalismy to BST 

		// konstruktor przyjmujacy tablice z ktorej nalezy zbudowac
		postOrder(Person[] arr) {
			this.arr = arr;
			index = arr.length - 1;
		}
		// konstruktor domyslny

		postOrder() {
		}
		public int getStartingIndex() {
			// Zaczynamy od ostatniego elementa dla podanej tablicy postOrder
			this.index = arr.length - 1;
			return this.index;
		}
		public Node Create(Person info, int MIN, int MAX) {
			// Funkcja rekurencyjna tworzaca drzewo na podstawie listy postOrder
			if (index < 0) {			// Jezeli aktualny indeks nie miesci w zakresie dlugosci tablicy 
				return null;			// Mamy juz zbudowane poddrzewo
			}
			// Sprawdzamy, czy aktualny element nalezy do poddrzewa (miesci sie w przedziale (MIN, MAX))
			if (info.priority <= MIN) {
				return null;
			}
			if (info.priority >= MAX) {
				return null;
			}

			Node cur = new Node(info);// Tworzymy node z aktualnej osoby, to jest korzen aktualnego poddrzewa 
			index--;// Przechodzimy do poprzedniego elementu tablicy
			if (index >= 0) {
				// Rekurencyjnie tworzymy prawe poddrzewo, w ktorym musza byc elementy w przedziale (cur, MAX)
				cur.right = Create(arr[index], cur.info.priority, MAX);
			}
			if (index >= 0) {
				// Rekurencyjnie tworzymy lewe poddrzewo, w ktorym musza byc elementy w przedziale (MIN, cur)
				cur.left = Create(arr[index], MIN, cur.info.priority);
			}
			return cur;
		}
		public String getString(Node start) {
			// Zwracamy napis z elementami poddrzewa wypisanymi w porzadku preorder
			paramsStack stack = new paramsStack(8);// Stos parametrow dla symulacji funkcji rekurencyjnej
			stack.push(new Params(0, start));// Odkladamy na stos gorne wywolanie
			StringBuilder sb = new StringBuilder("");// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
			while (stack.isEmpty() == false) {// Poki stos nie jest pusty (mamy kolejne wywolania rekurencyjne)
				Params	curPars	= stack.pop();// Pobieramy aktualne parametry
				Node	cur		= curPars.getNode();// Aktualny node, dla ktorego stosujemy "rekurencyjna" funkcje
				switch (curPars.getContext()) {// Przechodzimy w odpowiednie miejsce
					// Iteracyjnie robimy przejscie postorder
					// postorder(cur.L)
					// postorder(cur.R)
					// wypisz(cur)
					case 0: {
						if (cur == null) {
							break;
						}
						stack.push(new Params(1, cur));
						stack.push(new Params(0, cur.left));
						break;
					}
					case 1: {
						stack.push(new Params(2, cur));
						stack.push(new Params(0, cur.right));
						break;
					}
					case 2: {
						Person p = cur.info;
						sb.append(p.priority).append(" - ").append(p.name).append(" ").append(p.surname).append(", ");
						break;
					}
				}
			}
			sb.setLength(Math.max(0, sb.length() - 2));// Usuwamy zbedne znaki
			return sb.toString();// Zwracamy napis wynikoy 
		}

	}
	public Node findPrev(final int priority, Node start) {
		// Funkcja iteracyjna szuka inorder poprzednika dla wartosci zadanej argumentem (najwiekszy element mniejszy od przekazanego argumentem)
		if (this.findNodeAndParent(priority)[0] == null) {
			return null;
		}
		Node prevNode = null;	// Node poprzednika
		for (Node cur = start; cur != null;) {	// Przechozimy po kolejnych poddrzewach
			if (cur.info.priority < priority) {
				// Jezeli priorytet aktualnego elementa jest mniejszy od prioryteta przekazanego argumentem 
				prevNode = cur;	// Ten element moze byc poprzednikiem
				cur = cur.right;	// Ale sprawdzamy prawe poddrzewo, w ktorym beda elementy o wiekszych badz rownych priorytetach niz cur,
				// ale mozliwie mniejszych od prioryteta przekazanego argumentem 
			} else {
				// WPP szukamy poprzednika w lewym poddrzewie, bo w prawym napewno sa elementy >= od prekazanej argumentem wartosci
				cur = cur.left;
			}
		}
		return prevNode;	// Zwracamy poprzednika
	}
	public Node findNext(final int priority, Node start) {
		// Funkcja iteracyjna szuka inorder nastepnika dla wartosci zadanej argumentem (najmniejszy element wiekszy od przekazanego argumentem)
		if (this.findNodeAndParent(priority)[0] == null) {
			return null;
		}
		Node nextNode = null;// Node nastepnika
		for (Node cur = start; cur != null;) {		// Przechozimy po kolejnych poddrzewach
			if (cur.info.priority > priority) {
				// Jezeli priorytet aktualnego elementa jest wiekszy od prioryteta przekazanego argumentem 
				nextNode = cur;	// Ten element moze byc nastepnikiem
				cur = cur.left;	// Ale sprawdzamy lewe poddrzewo, w ktorym beda elementy o mniejszych prioritach niz cur, 
				// ale mozliwie wiekszych od priorita przekazanego argumentem
			} else {
				// WPP szukamy poprzednika w prawym poddrzewie, bo w lewym napewno sa elementy < od prekazanej argumentem wartosci 
				cur = cur.right;
			}
		}
		return nextNode;
	}
	public Node findPrev(final int priority) {
		// Zwracamy poprzednika zaczynajac przeszukiwanie od korzenia
		return findPrev(priority, this.root);
	}
	public Node findNext(final int priority) {
		// Zwracamy nastepnika zaczynajac przeszukiwanie od korzenia
		return this.findNext(priority, this.root);
	}
	public Node[] findMin(final Node start) {
		// Funkcja zwraca element minimalny i jego poprzednika dla poddrzewa o korzenie przekazanym argumentem
		Node	minNode		= null;	// Node elementa minimalnego		
		Node	prevprev	= null;	// Poprzednik elementa minimalnego
		for (Node cur = start; cur != null;) {		// Przechodzimy po wszystkich lewych poddrzewach
			prevprev = minNode;						// Bo w kazdym lewym poddrzewie sa elementy o priorytetach mnieszych od prioriteta jego rodzica
			minNode = cur;
			cur = cur.left;
		}
		return new Node[] { minNode, prevprev };		// Zwracamy wynik jako tablice
	}
	public Node[] findMax(final Node start) {
		// Funkcja zwraca element maksymalny i jego poprzednika dla poddrzewa o korzenie przekazanym argumentem
		Node	maxNode		= null;	// Node elementa maksymalnego
		Node	prevprev	= null;	// Poprzednik elementa maksymalnego
		for (Node cur = start; cur != null;) {		// Przechodzimy po wszystkich prawych poddrzewach
			prevprev = maxNode;						// Bo w kazdym prawym poddrzewie sa elementy o priorytetach wiekszych badz rownych od prioriteta jego rodzica
			maxNode = cur;
			cur = cur.right;
		}
		return new Node[] { maxNode, prevprev };		// Zwracamy wynik jako tablice
	}
	public Node enque(final Person toAdd, Node start) {
		// Funkcja dodaje osobe przekazana argumentem do poddrzewa o korzeniu przekazanym argumentem
		if (start == null) {		// Jezeli poddrzewo jest puste, ustawiamy nowy element na miejsce korzenia
			start = new Node(toAdd);
		} else {	// Wpp
			Node parent = null;
			for (Node cur = start; cur != null;) {
				// Szukamy odpowiednie wolne miejsce dla dodania elementu
				parent = cur;	// Tez zaznaczmy rodzica dodawanego elementu
				if (toAdd.priority < cur.info.priority) {
					cur = cur.left;
				} else {
					cur = cur.right;
				}
			}
			// Ustawiamy lewe/prawo dziecko rodzica w zaleznosci od tego, gdzie pasuje dodawawny element
			if (toAdd.priority < parent.info.priority) {
				parent.left = new Node(toAdd);
			} else {
				parent.right = new Node(toAdd);
			}
		}
		return start;			// Zwracamy korzen poddrzewa po dodawaniu elementa
	}
	public void enque(Person toAdd) {
		this.root = enque(toAdd, this.root);		// Dodajemy element i aktualizujemy korzen zeby zabiezpieczyc tez mozliwosc dodawanie elementa jako korzenia
	}
	public Node[] findNodeAndParent(final int priority) {
		// Funkcja przeszukiwajaca element o priorytecie przekazanym argumentem i jego rodzica 
		Node	parent	= null;		// Node rodzica
		Node	cur		= null;		// Node elementa
		boolean	found	= false;	// Czy udalo sie znalezc element o odpowiedniej wartosci
		for (cur = this.root; cur != null && found == false;) {
			if (priority < cur.info.priority) {
				parent = cur;
				cur = cur.left;
			} else if (priority > cur.info.priority) {
				parent = cur;
				cur = cur.right;
			} else {
				found = true;	// Udalo sie znalezc element taki ze priority == cur.info.priority
			}
		}
		if (found == false) {	// Nie udalo sie znalezc element o podanym priorytecie 
			return new Node[] { null, null };	// Zwracamy null
		}
		return new Node[] { cur, parent };	// Zwracamy element i jego rodzica
	}
	public Node deleteNode(Node toDel, Node parent) {
		// Funkcja usuwania elementa przekazanego elementem
		// Przyjmuje tez jako argument rodzica usuwanego elementu
		if (toDel == null) {	// Nic do usuniecia
			return toDel;		// Zwracamy null
		}
		// Note przypadek 1 : Usuwany elemnent ma co najwyzej jedno dziecko
		if (toDel.left == null || toDel.right == null) {
			Node replacement = null;	// Element na ktory bedzie wskazywac rodzic usuwanego elementa zamiast wskazywac na usuniety element
			if (toDel.left == null && toDel.right == null) {	// Usuwany element nie ma dzieci
				replacement = null;						// Zastepnik jest nullem
			} else if (toDel.left != null) {				// Usuwany element ma lewe dziecko
				replacement = toDel.left;					// Zastepnik bedzie lewym dzickiem usuwanego elementa
			} else {									// Usuwany element ma prawe dziecko
				replacement = toDel.right;					// Zastepnik bedzie prawym dzickiem usuwanego elementa
			}
			if (parent == null) {						// Usuwany element nie ma rodzica, tzn jest korzeniem
				this.root = replacement;				// Ustawiamy zastepnik na miejsce korzenia
			} else if (parent.left == toDel) {				// Usuwany element jest lewym dzickiem 
				parent.left = replacement;					// Ustawiamy odpowiednio zastepnik
			} else {									// Usuwany element jest praawym dzickiem 
				parent.right = replacement;					// Ustawiamy odpowiednio zastepnik
			}
			return toDel;								// Zwracamy referencje do usunietego elementa
		}

		// Note przypadek 2 : Usuwany elemnent ma dwoch dzieci
		Node	next		= null;				// Inorder nastepnik usuwanego elementa
		Node	nextParent	= null;				// Rodzci inorder nastepnika usuwanego elementa
		if (toDel.right.left == null) {
			// Jezeli prawe dziecko usuwanego elementa jest jego nastepnikiem (nie ma lewego poddrzewa), 
			// mozemy zastosowac prostszy podstawowy przypadek usuwania wezla z jednym dzieckiem / brakiem dzieci
			// Zatem poprostu ustawiamy prawe dziecko (tez moze byc rowne null) nastepnika jako prawe poddrzewo usuwanego elementa
			// Tym samym usuwajac nastepnik z calego drzewa
			next = toDel.right;
			toDel.right = next.right;
		} else {
			Node[] nodeInfo = findMin(toDel.right);
			next = nodeInfo[0];
			nextParent = nodeInfo[1];
			// Usuwanie najmniejszego elementu, ten element moze miec jedynie prawo dziecko lub nie miec dzieci w ogole
			// Zatem zamiast wszazywac na usuniety nastepnik, lewe poddrzewo rodzica nastepnika teraz wskazuje na jedno dziecko nastepnika/null  
			//	Tym samym usuwajac nastepnik z calego drzewa
			nextParent.left = next.right;
		}
		// Naprawilismy referencje, a poniewaz znalezlismy i usunelismy nastepnik danego elementa z drzewa
		// Nadpisujemy wartosc usuwanego drzewa wartoscia nastepnika 
		toDel.info = next.info;

		return toDel;			// Zwracamy referencje do usunietego elementa
	}
	public Node deleteValue(final int priority) {
		// Funkcja usuwa pierwszy znaleziony element o podanej wartosci
		Node[]	nodeInfo	= findNodeAndParent(priority);	// Szukamy element o podanym priorytecie
		Node	toDel		= nodeInfo[0];		// Usuwany element 
		Node	parent		= nodeInfo[1];		// Rodzic usuwanego elementa
		return deleteNode(toDel, parent);		// Wywolanie funkcji usuwanacej node
	}
	public Node dequeMin() {
		// Funkcja usuwa element minimalny i zwraca referencje do tego elementu
		Node[]	nodeInfo	= findMin(this.root);	// Szukamy minimalny element w calym drzewie
		Node	minNode		= nodeInfo[0];			// Usuwany element
		Node	parent		= nodeInfo[1];			// Rodzic usuwanego elementa

		return deleteNode(minNode, parent);			// Wywolanie funkcji usuwanacej node
	}
	public Node dequeMax() {
		// Funkcja usuwa element maksymalny i zwraca referencje do tego elementu
		Node[]	nodeInfo	= findMax(this.root);	// Szukamy maksymalny element w calym drzewie
		Node	maxNode		= nodeInfo[0];			// Usuwany element
		Node	parent		= nodeInfo[1];			// Rodzic usuwanego elementa

		return deleteNode(maxNode, parent);			// Wywolanie funkcji usuwanacej node
	}
	public Node findMin() {
		// Funkcja zwraca elememt minimalny w calym drzewie
		return findMin(this.root)[0];
	}
	public Node findMax() {
		// Funkcja zwraca elememt maksymalny w calym drzewie
		return findMax(this.root)[0];
	}
	public int getHeight(Node cur) {
		// Funckja rekurencyjna, zwraca wysokosc danego poddrzewa
		if (cur == null) {	// Jezeli poddrzewo jest puste, zwraca 0
			return 0;
		}
		int	L	= getHeight(cur.left);		// Znalezenie wysokosci lewego poddrzewa
		int	R	= getHeight(cur.right);		// Znalezenie wysokosci prawego poddrzewa
		return Math.max(L, R) + 1;		// Wyznaczenie najwiekszej dlugosci sposrod znalezionych i dodawanie dlugosci aktualnego Node'a 
	}
	public String toString(Order order, Node start) {
		// Funkcja zwraca napis poddrzewa przekazanego argumentem w porzadku przekazanym argumentem
		return order.getString(start);
	}
	public String toString(Order order) {
		// Funkcja zwraca napis calego drzewa w porzadku przekazanym argumentem
		return order.getString(this.root);
	}
}

public class Source {
	public static Scanner sc = new Scanner(System.in);	// Scanner do wczytywania danych wejsciowych
	public static void main(String[] args) {
		int				test_count	= sc.nextInt();			// Liczba zestawow testowych
		StringBuilder	output		= new StringBuilder("");	// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
		for (int testNo = 1; testNo < test_count + 1; testNo++) {	// Przechodzimy po wszystkich zestawach testowych
			int				opCount	= sc.nextInt();				// Liczba operacji
			BST				tree	= new BST();				// Drzewo na ktorym beda wykonane operacje
			StringBuilder	curOut	= new StringBuilder("");	// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow aktualnego zestawu testowego
			while (opCount-- > 0) {								// Przechodzimy po wszystkich roskazach
				StringBuilder	result	= new StringBuilder("");		// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow aktualnego poleceia
				String			request	= sc.next();					// Nazwa poleceia
				switch (request) {	// Wykonujemy odpowiednie polecenie
					// SECTION MODUL EDYCJI
					case "CREATE": {
						String		orderName	= sc.next();	// Nazwa porzadku
						int			peopleCount	= sc.nextInt();	// Liczba osob
						Person[]	people		= new Person[peopleCount];	// Tablica osob
						for (int i = 0; i < peopleCount; i++) {	// Wczytujemy kolejne osoby i dodajemy to tablicy
							int		priority	= sc.nextInt();
							String	name		= sc.next();
							String	surname		= sc.next();
							people[i] = new Person(name, surname, priority);
						}
						tree = new BST(people, orderName);	// Tworzenie drzewa na postawie listy osob w zadanym porzadku priorytetow
						break;
					}
					case "DELETE": {
						int priority = sc.nextInt();		// Priorytet usuwanej osoby 
						if (tree.deleteValue(priority) == null) {
							// Jezeli nie udalo sie usunac element o podanym priorytecie
							// Ten element nie istnieje
							result.append(request).append(" ").append(priority).append(": BRAK");
						}
						break;
					}
					// SECTION MODUL KOLEJKOWANIA
					case "ENQUE": {
						// Dane dodawanej osoby
						int		priority	= sc.nextInt();
						String	name		= sc.next();
						String	surname		= sc.next();
						tree.enque(new Person(name, surname, priority)); // Wywolanie funkcji dodawania osoby
						break;
					}
					case "DEQUEMAX": {
						result.append(request).append(": ");

						Node max = tree.dequeMax(); 	// Referencja do usunietej osoby o najwyzszym prioritecie
						if (max == null) {
							// Jezeli nie udalo sie usunac element
							// Ten element nie istnieje
							result.append("BRAK");
							break;
						}
						result.append(BST.NodeToString(max.info));	// Wypisywanie danych usunetej osoby
						break;
					}
					case "DEQUEMIN": {
						result.append(request).append(": ");
						Node min = tree.dequeMin();		// Referencja do usunietej osoby o najwyzszym prioritecie
						if (min == null) {
							// Jezeli nie udalo sie usunac element
							// Ten element nie istnieje
							result.append("BRAK");
							break;
						}
						result.append(BST.NodeToString(min.info));	// Wypisywanie danych usunetej osoby
						break;
					}
					case "NEXT": {
						// Zwraca najblisza osobe o priorytecie wiekszym niz podany, o ile istnieje
						int priority = sc.nextInt();
						result.append(request).append(" ").append(priority).append(": ");
						Node next = tree.findNext(priority, tree.getRoot());	// Referencja do nastepnika
						if (next == null) {
							// Element nie istnieje
							result.append("BRAK");
							break;
						}
						if (next.info.priority == priority) {
							// Element nie jest mniejszy od podanego
							result.append("BRAK");
							break;
						}
						result.append(BST.NodeToString(next.info));// Wypisywanie danych znalezionej osoby
						break;
					}
					case "PREV": {
						// Zwraca najblisza osobe o priorytecie mniejszym niz podany, o ile istnieje
						int priority = sc.nextInt();
						result.append(request).append(" ").append(priority).append(": ");
						Node prev = tree.findPrev(priority, tree.getRoot());// Referencja do poprzednika
						if (prev == null) {
							// Element nie istnieje
							result.append("BRAK");
							break;
						}
						if (prev.info.priority == priority) {
							// Element nie jest mniejszy od podanego
							result.append("BRAK");
							break;
						}
						result.append(BST.NodeToString(prev.info));// Wypisywanie danych znalezionej osoby
						break;
					}
					// SECTION MODUL RAPORTOWANIA
					case "PREORDER": {
						// Wywolanie funkcji iteracyjnej wypisywania w odpowiednim porzadku prioritetow
						result.append(request).append(": ");
						result.append(tree.toString(tree.new preOrder()));// Dodawanie napisu do wyniku
						break;
					}
					case "INORDER": {
						// Wywolanie funkcji iteracyjnej wypisywania w odpowiednim porzadku prioritetow
						result.append(request).append(": ");
						result.append(tree.toString(tree.new inOrder()));// Dodawanie napisu do wyniku
						break;
					}
					case "POSTORDER": {
						// Wywolanie funkcji iteracyjnej wypisywania w odpowiednim porzadku prioritetow
						result.append(request).append(": ");
						result.append(tree.toString(tree.new postOrder()));// Dodawanie napisu do wyniku
						break;
					}
					case "HEIGHT": {
						// Wywolanie funkcji rekurencyjnej dla znalezenia wysokosci drzewa
						result.append(request).append(": ");
						result.append(Integer.toString(Math.max(tree.getHeight(tree.getRoot()) - 1, 0)));
						break;
					}
				}
				if (result.length() != 0) {
					// Jezeli polecenie ma wynik, dodajemy na koniec znak nowej linii
					curOut.append(result.toString()).append("\n");	// Dodajemy wynik aktualnego polecenia do wyniku calego zestawu 
				}
			}
			output.append("ZESTAW ").append(testNo).append("\n").append(curOut);	// Dodajemy wynik aktualnego zestawu do wyjscia
		}
		System.out.print(output);	// Wypisywanie wyjscia
	}
}

// test.00.in
// 2 17
// CREATE PREORDER 10 15
// A A 10
// B B 13
// C C 11
// D D 14
// E E 20
// F F 15
// G G 25
// H H 25
// K K 30
// L L
// INORDER PREORDER
// POSTORDER HEIGHT NEXT 14 PREV 22 PREV 15 PREV 11 NEXT 30 DELETE 20
// DEQUEMIN DEQUEMAX
// HEIGHT
// INORDER
// PREORDER POSTORDER 26
// CREATE POSTORDER 7 21
// A A 28
// B B 36
// C C 42
// D D 37
// E E 30
// F F 22
// H H
// HEIGHT PREORDER
// INORDER POSTORDER
// DEQUEMAX
// DELETE 10 DELETE 42 NEXT 0 PREV 36 DELETE 21 PREV 22 PREV 37
// HEIGHT DELETE 22
// DEQUEMIN DEQUEMAX
// DEQUEMIN PREORDER
// INORDER POSTORDER
// DEQUEMAX PREORDER
// INORDER POSTORDER
// DEQUEMIN

// test.00.out
// ZESTAW 1
// INORDER: 10 - B B, 11 - D D, 13 - C C, 14 - E E, 15 - A A, 20 - F F
// PREORDER: 15 - A A, 10 - B B, 13 - C C, 11 - D D, 14 - E E, 20 - F F
// POSTORDER: 11 - D D, 14 - E E, 13 - C C, 10 - B B, 20 - F F, 15 - A A
// HEIGHT: 3
// NEXT 14: 15 - A A
// PREV 22: BRAK
// PREV 15: 14 - E E
// PREV 11: 10 - B B
// NEXT 30: BRAK
// DEQUEMIN: 10 - B B
// DEQUEMAX: 15 - A A
// HEIGHT: 1
// INORDER: 11 - D D, 13 - C C, 14 - E E
// PREORDER: 13 - C C, 11 - D D, 14 - E E
// POSTORDER: 11 - D D, 14 - E E, 13 - C C
// ZESTAW 2
// HEIGHT: 3
// PREORDER: 22 - H H, 21 - A A, 30 - F F, 28 - B B, 37 - E E, 36 - C C, 42 - D D
// INORDER: 21 - A A, 22 - H H, 28 - B B, 30 - F F, 36 - C C, 37 - E E, 42 - D D
// POSTORDER: 21 - A A, 28 - B B, 36 - C C, 42 - D D, 37 - E E, 30 - F F, 22 - H H
// DEQUEMAX: 42 - D D
// DELETE 10: BRAK
// DELETE 42: BRAK
// NEXT 0: BRAK
// PREV 36: 30 - F F
// PREV 22: BRAK
// PREV 37: 36 - C C
// HEIGHT: 3
// DEQUEMIN: 28 - B B
// DEQUEMAX: 37 - E E
// DEQUEMIN: 30 - F F
// PREORDER: 36 - C C
// INORDER: 36 - C C
// POSTORDER: 36 - C C
// DEQUEMAX: 36 - C C
// PREORDER: 
// INORDER: 
// POSTORDER: 
// DEQUEMIN: BRAK