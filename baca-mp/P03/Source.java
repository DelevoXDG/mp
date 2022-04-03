// Maksim Zdobnikau - 2

import java.util.Scanner;

// Idea rozwiazania:
// pending

// Section code itself
class String_Stack {		// Implementacja stosu typu String
	private String[] arr;	// Tablica, przechowywujacy elementy stosu
	private int size;		// Aktualny rozmiar stosu

	public String_Stack(final int maxSize) {	// Konstruktor stosu
		arr = new String[maxSize];				// Przyjmuje maksymalny rozmiar stosu
		size = 0;
	}
	public void push(final String data) {		// Dodawanie elementow do stosu
		if (size + 1 >= arr.length) {
			throw new ArrayIndexOutOfBoundsException("Stack Overflow Exception");
		}
		arr[size++] = data;
	}
	public String pop() {						// Usuwanie elementow ze stosu 
		if (size - 1 < 0) {
			throw new ArrayIndexOutOfBoundsException("Stack Underflow Exception");
		}
		size--;
		return arr[size];
	}
	public String peek() {						// Zwraca gorny element stosu
		return size <= 0 ? "" : arr[size - 1];
	}
	public Boolean isEmpty() {					// Funkcja sprawdza, czy stos jest pusty
		return size == 0;
	}
	public Boolean isNonEmpty() {				// Funkcja sprawdza, czy stos jest nie pusty
		return size != 0;
	}
	public Boolean isFull() {					// Funkcja sprawdza, czy stos jest wypelniony
		return size == arr.length;
	}
	public int size() {							// Zwraca aktualny rozmiar stosu
		return size;
	}
	@Override public String toString() {		// Zwraca string z elementow stosu
		String result = "";
		for (int i = 0; i < size - 1; i++) {
			result += arr[i] + " ";
		}
		if (size > 0)
			result += arr[size - 1];
		return result;
	}
}

class Char_Stack {			// Implementacja stosu typu String			
	private char[] arr;		// Tablica, przechowywujacy elementy stosu
	private int size;		// Aktualny rozmiar stosu

	public Char_Stack(final int maxSize) {		// Konstruktor stosu
		arr = new char[maxSize];				// Przyjmuje maksymalny rozmiar stosu
		size = 0;
	}
	public void push(final char data) {			// Dodawanie elementow do stosu
		if (size + 1 >= arr.length) {
			throw new ArrayIndexOutOfBoundsException("Stack Overflow");
		}
		arr[size++] = data;
	}
	public char pop() {							// Usuwanie elementow ze stosu 
		if (size - 1 < 0) {
			throw new ArrayIndexOutOfBoundsException("Stack Underflow");
		}
		size--;
		return arr[size];
	}
	public char peek() {						// Zwraca gorny element stosu
		if (size <= 0) {
			throw new ArrayIndexOutOfBoundsException("Stack is empty");
		}
		return arr[size - 1];
	}
	public Boolean isEmpty() {					// Funkcja sprawdza, czy stos jest pusty
		return size == 0;
	}
	public Boolean NonEmpty() {					// Funkcja sprawdza, czy stos jest nie pusty
		return size != 0;
	}
	public Boolean isFull() {					// Funkcja sprawdza, czy stos jest wypelniony
		return size == arr.length;
	}
	public int size() {							// Zwraca aktualny rozmiar stosu
		return size;
	}
	@Override public String toString() {		// Zwraca string z elementow stosu
		String result = "";
		for (int i = 0; i < size - 1; i++) {
			result += arr[i] + " ";
		}
		if (size > 0)
			result += arr[size - 1];
		return result;
	}
}

public class Source {
	public static Scanner sc = new Scanner(System.in); 	 // Scanner do wczytywania danych wejsciowych 

	public static class rpn {		// Klasa ONP

		static class NotDFA {		// Skonczony automat deterministyczny [SAD]
			public enum State {		// Klas stanu SAD
				// Skonczony automat deterministyczny zmienia stany w zaleznosci od:
				// 1) Aktalnego stanu
				// 2) Wczytanej wartosci

				q0(false),
				q1(true),	// Akceptujacy stan
				q2(false),
				err(false);	// Stan bledu, odrazu mozemy zatrzymac SAD 

				final boolean isAcceptable;	// Czy jest poprawnym stanem wyjsciowym
				State(boolean expr) {		// Konstruktor Stanu
					isAcceptable = expr;
				}
				// "Alfabet" SAD
				State z;	// operandy
				State o1;	// operatory unarne
				State o2;	// operatory binarne
				State lp;	// lewy nawias
				State rp;	// prawy nawias

				static {
					// Tabelka stanow automatu skonczonego
					// Jezeli nie wskazanego przejcia, automat zmienia stan na err
					// err - stan bledu, ocznacza, ze automat juz napewny nie zaakceptuje slowo

					// Stan q0
					q0.z = q1;
					q0.lp = q0;
					q0.o1 = q2;
					// Stan q1
					q1.o2 = q0;
					q1.rp = q1;
					q2.z = q1;
					// Stan q2
					q2.lp = q0;
					q2.o1 = q2;
				}

				public State readNext(String symb) {	// Funkcja, wczytujaca nastepny symbol
					if (symb.equals("z")) {
						if (z == null) return err;		// Jezeli nie wskazanego przejcia, automat zmienia stan na err
						return z;						// Zwraca stan po wczytaniu operanda
					} else if (symb.equals("o1")) {
						if (o1 == null) return err;		// Jezeli nie wskazanego przejcia, automat zmienia stan na err
						return o1;						// Zwraca stan po wczytaniu operatora jednoargumentowego
					} else if (symb.equals("o2")) {
						if (o2 == null) return err;		// Jezeli nie wskazanego przejcia, automat zmienia stan na err
						return o2;						// Zwraca stan po wczytaniu operatora dwuargumentowego
					} else if (symb.equals("(")) {
						if (lp == null) return err;		// Jezeli nie wskazanego przejcia, automat zmienia stan na err
						return lp;						// Zwraca stan po wczytaniu lewego nawiasa
					} else if (symb.equals(")")) {
						if (rp == null) return err;		// Jezeli nie wskazanego przejcia, automat zmienia stan na err
						return rp;						// Zwraca stan po wczytaniu prawego nawiasa
					}
					return err;							// Nieznany symbol, zwracamy stan bledu
				}

				Boolean isAcceptable() {	// Sprawdzamy, czy skonczony automat deterministyczny jest w stanie akceptujacym
					return this.isAcceptable;
				}
				Boolean hasError() {		// Sprawdzamy, czy skonczony automat deterministyczny jest w stanie bledu
					return this == err;
				}
				State getState() {			// Zwraca stan SAD
					return this;
				}
			}
		}

		private static boolean isOperand(char symbol) {	// Sprawdza, czy symbol jest operandem
			return (symbol >= 'a' && symbol <= 'z');
		}

		private static boolean isOperator(char symbol) {//  Sprawdza, czy symbol jest operatorem
			return ("()~!^*/%+-<>?&|=".contains(String.valueOf(symbol)));
		}
		// private static boolean isParenthesis(char symbol) {
		// 	return ("()".contains(String.valueOf(symbol)));
		// }
		private static boolean isRightSide(char symbol) {// Sprawdza, czy symbol jest operatorem prawostronnym
			return ("~!^=".contains(String.valueOf(symbol)));
		}
		private static boolean isUnary(char symbol) {	// Sprawdza, czy symbol jest operatorem jednoargumentowym
			return ("~!".contains(String.valueOf(symbol)));
		}
		private static boolean isCorrectINFSymbol(char symbol) {	// Sprawdza, czy symbol jest poprawnym symbolem dla postaci infiksowej
			return isOperator(symbol) || isOperand(symbol);
		}
		private static boolean isCorrectRPNSymbol(char symbol) {	// Sprawdza, czy symbol jest poprawnym symbolem dla postaci ONP
			return ("~!^*/%+-<>?&|=".contains(String.valueOf(symbol))) || isOperand(symbol);
		}
		private static int getPriority(char OR) {	// Zwraca priorytet wskazanego symbola 
			if (OR == 'Z') return 20;				// Z ocznacza Operand, uzywany jest dla konwersju ONP->INF 
			if (OR == '(') return 10;
			if (OR == ')') return 10;
			if (OR == '~') return 9;
			if (OR == '!') return 9;
			if (OR == '^') return 8;
			if (OR == '*') return 7;
			if (OR == '/') return 7;
			if (OR == '%') return 7;
			if (OR == '+') return 6;
			if (OR == '-') return 6;
			if (OR == '>') return 5;
			if (OR == '<') return 5;
			if (OR == '?') return 4;
			if (OR == '&') return 3;
			if (OR == '|') return 2;
			if (OR == '=') return 1;

			throw new IllegalArgumentException("Unknown symbol"); // Nieznany symmbol
		}
		private static String encloseInBrackets(String str) {	// Zwraca sstring wyrazenia w nawiasch
			return "( " + str + " )";
		}

		public static String rpnParse(String infStr) {	// Konwersja z INF do ONP, zwraca string w postaci ONP
			Char_Stack		stack			= new Char_Stack(infStr.length()); // Stos, przechowywajacy operatory 
			Char_Stack		result			= new Char_Stack(infStr.length()); // Stos "wyjsciowy"

			int				leftPar_count	= 0;	// Licznik lewych nawiasow
			int				rightPar_count	= 0;	// Licznik prawych nawiasow 

			NotDFA.State	autoState		= NotDFA.State.q0;	// Inicjalizacja poczatkowego stanu skonczonego automatu deterministycznego 

			for (int i = 0; i < infStr.length(); i++) {
				char sym = infStr.charAt(i);		// Aktualnie sprawdzany symbol z wyrazenia INF
				if (isCorrectINFSymbol(sym)) {		// Sprawdzamy, czy symbol jest poprawnym symbolem dla alfabetu INF
					if (isOperand(sym)) {			// Sprawdzamy, czy symobol jest operandem
						autoState = autoState.readNext("z");  // Wczytalismy operand, odpowednia zmiana stanu skonczonego automatu deterministycznego 
						result.push(sym);					  // Dodajemy operator na wyjsciowy stos
					} else if (sym == '(') {
						autoState = autoState.readNext("(");  // Wczytalismy lewy nawias, odpowednia zmiana stanu skonczonego automatu deterministycznego 
						leftPar_count++;					  // Inkrementacja liczniku lewych nawiasow

						stack.push(sym);					  // Dodajemy lewy nawias na stos
					} else if (sym == ')') {
						autoState = autoState.readNext(")");  // Wczytalismy prawy nawias, odpowednia zmiana stanu skonczonego automatu deterministycznego 	
						rightPar_count++;					  // Inkrementacja liczniku prawych nawiasow

						while (stack.NonEmpty() && stack.peek() != '(') {	// Zdejmujemy operatory do napotkania lewego nawiasu lub skonczenia elementow na stosie
							result.push(stack.pop());						// I wstawiamy na wyjscie
						}
						if (stack.NonEmpty() && stack.peek() == '(') {		// Usuwamy se stosu lewy nawias
							stack.pop();
						} else {											// Nie ma lewego nawiasu dla prawego
							return "error";									// Zatem zwracamy blad
						}
					} else if (isOperator(sym)) {// Sprawdzamy, czy symobol jest operatorem
						if (isRightSide(sym)) {	// Czy operator jest prawostronnie laczny
							if (isUnary(sym)) {	// Sprawdzamy, czy symbol jest operatorem jednostronnym
								autoState = autoState.readNext("o1");  // Wczytalismy operator jednoargumentowy, odpowednia zmiana stanu skonczonego automatu deterministycznego
							} else {
								autoState = autoState.readNext("o2");  // Wczytalismy operator dwuargumentowy, odpowednia zmiana stanu skonczonego automatu deterministycznego
							}
							while (stack.NonEmpty() && stack.peek() != '('   // Zdejmujemy ze stosu do napotkania operatora o mniejszym badz rownym od aktualnego priorytecie lub skonczenia elementow na stosie
									&& getPriority(stack.peek()) > getPriority(sym)) {	// operatory o priorytecie wiekszym niz u aktualnego symbolu
								result.push(stack.pop());																// I podajemy te operatory na wyjscie
							}
						} else {// Napotkany lewostronnie laczny operator
							// binary_OR_count++;
							autoState = autoState.readNext("o2");	 		// Wczytalismy operator dwuargumentowy, odpowednia zmiana stanu skonczonego automatu deterministycznego

							while (stack.NonEmpty() && stack.peek() != '(' &&		// Zdejmujemy ze stosu do napotkania operatora o mniejszym od aktualnego priorytecie lub skonczenia elementow na stosie
									getPriority(stack.peek()) >= getPriority(sym)) {  	// operatory o priorytecie nie mniejszym niz u aktualnego symbolu
								result.push(stack.pop());								// I podajemy te operatory na wyjscie
							}
						}
						stack.push(sym);									// Dodajemy na stos aktualny operator
					} else {
						throw new IllegalArgumentException("Unknown symbol detected");	// Nie znany symbol (nie powinno zdarzyc)
					}

					if (autoState.hasError() || leftPar_count != rightPar_count) {
						return "error";
					}
				}

			}
			if (autoState.isAcceptable == false || leftPar_count != rightPar_count) {
				return "error";
			}
			while (stack.NonEmpty()) {
				result.push(stack.pop());
			}

			return result.toString();
		}

		public static String infParse(String rpnStr) {
			String_Stack	result			= new String_Stack(rpnStr.length());
			Char_Stack		outline_stack	= new Char_Stack(rpnStr.length());	// Stos, zawierajacy operandy lub ostatnie najwieksze orerator dla wykonanych obliczen 
			// int				OP_count		= 0;
			// int				OR_count		= 0;

			for (int i = 0; i < rpnStr.length(); i++) {
				char sym = rpnStr.charAt(i);
				if (isCorrectRPNSymbol(sym)) {
					if (isOperand(sym)) {
						// OP_count++;
						outline_stack.push('Z');		// symbol oznaczajacy operand
						result.push(String.valueOf(sym));
					} else if (isOperator(sym)) {
						// OR_count++;
						if (isUnary(sym)) {
							if (outline_stack.isEmpty()) {
								return "error";
							}
							char	prevOutline	= outline_stack.pop();
							String	prevExpr	= result.pop();

							if (getPriority(prevOutline) < getPriority(sym)) {
								prevExpr = encloseInBrackets(prevExpr);
							}

							outline_stack.push(sym);
							result.push(sym + " " + prevExpr);
						} else { // is binary 
							if (outline_stack.size() < 2) {
								return "error";
							}
							char	prevOB	= outline_stack.pop();
							char	prevOA	= outline_stack.pop();
							String	prevB	= result.pop();
							String	prevA	= result.pop();

							if (getPriority(sym) > getPriority(prevOA)) {
								prevA = encloseInBrackets(prevA);
							}
							if (getPriority(sym) > getPriority(prevOB)) {
								prevB = encloseInBrackets(prevB);
							} else if (isRightSide(sym) == false && getPriority(sym) == getPriority(prevOB)) {
								prevB = encloseInBrackets(prevB);
							}

							outline_stack.push(sym);
							result.push(prevA + " " + sym + " " + prevB);
						}
					} else {
						throw new IllegalArgumentException("Unknown symbol");
					}
				}

			}

			if (result.size() != 1) {
				return "error";
			}

			return result.toString();
		}

	}
	public static void main(String[] args) {
		int test_count = sc.nextInt();	// Liczba sprawdzanych zestawow

		while (test_count-- > 0) {
			String	inputType	= sc.next();
			String	inputStr	= sc.nextLine();
			if (inputType.charAt(0) == 'I') { // INF do ONP
				System.out.println("ONP: " + rpn.rpnParse(inputStr));
			} else {						 // ONP do INF
				System.out.println("INF: " + rpn.infParse(inputStr));
			}
		}
	}
}

// Pytania:
// [x] replaceAll()
// [x] Stan automatu

// Testy
// test.00.in
// 10
// ONP: xa_b+cd/e-?=
// ONP: bot-? me< k##< n_ul0l*+^==
// INF: a*(+b^k)
// ONP: b~bk^na*c*-!-ta*/
// ONP: (ji)_ k+ = 
// INF: pHA ^(rA-oH)? f%i=t
// ONP: pAroM - #^ fi%?t =
// INF: a*(~b)
// INF: x=$(`a+b)?(c/`d -e)
// ONP: 44c a<x >b ~ =
// test.00.out
// INF: x = a + b ? c / d - e
// INF: b ? o - t = m < e < k = n ^ ( u + l * l )
// INF: ( ~ b - ! ( b ^ k - n * a * c ) ) / ( t * a )
// ONP: Error
// INF: j = i + k
// ONP: p r o - ^ f i % ? t =
// INF: p ^ ( r - o ) ? f % i = t
// ONP: a b ~ *
// ONP: x a b + c d / e - ? =
// INF: c < a > x = ~ b