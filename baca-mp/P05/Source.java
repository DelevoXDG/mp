
// Maksim Zdobnikau
import java.util.Scanner;

class Params {
	private String _adrs;
	private int _n;
	private int _requiredCapacity;

	public Params(String adrs, int requiredCapacity, int n) {
		this._adrs = adrs;
		this._requiredCapacity = requiredCapacity;
		this._n = n;
	}
	public String getAdrs() {
		return _adrs;
	}
	public int getCurIndex() {
		return _n;
	}
	public int getRequiredCapacity() {
		return _requiredCapacity;
	}
}

class paramStack {			// Implementacja stosu typu Int			
	private Params[] arr;		// Tablica, przechowywujacy elementy stosu
	private int top;		// Aktualny rozmiar stosu

	public paramStack(final int maxSize) {		// Konstruktor stosu
		arr = new Params[maxSize];					// Przyjmuje maksymalny rozmiar stosu
		top = 0;								// Ustawiamy aktualny rozmiar stosu
	}
	public void push(final Params data) {			// Dodawanie elementow do stosu
		if (top + 1 >= arr.length) {
			throw new ArrayIndexOutOfBoundsException("Stack Overflow");
		}
		arr[top++] = data;
	}
	public Params pop() {							// Usuwanie elementow ze stosu 
		if (top - 1 < 0) {
			throw new ArrayIndexOutOfBoundsException("Stack Underflow");
		}
		top--;
		return arr[top];
	}
	public Params peek() {							// Zwraca gorny element stosu
		if (top <= 0) {
			throw new ArrayIndexOutOfBoundsException("Stack is empty");
		}
		return arr[top - 1];
	}
	public Boolean isEmpty() {					// Funkcja sprawdza, czy stos jest pusty
		return top == 0;
	}
	public Boolean nonEmpty() {					// Funkcja sprawdza, czy stos jest nie pusty
		return top != 0;
	}
	public Boolean isFull() {					// Funkcja sprawdza, czy stos jest wypelniony
		return top == arr.length;
	}
	public int size() {							// Zwraca aktualny rozmiar stosu
		return top;
	}
	@Override public String toString() {		// Zwraca string z elementow stosu
		StringBuilder result = new StringBuilder("");
		for (int i = 0; i < top; i++) {
			result.append(" ").append(arr[i]);
		}
		return result.toString();
	}
}

class Backpack {
	int[] _weights;
	int _capacity;
	public static int topi;

	public Backpack(int capacity, int weightCount) {
		this._weights = new int[weightCount];
		this._capacity = capacity;
	}

	public void fill(Scanner sc) {
		for (int i = 0; i < _weights.length; i++) {
			_weights[i] = sc.nextInt();
		}
	}
	public void print() {
		for (int i = 0; i < _weights.length; i++) {
			System.out.println(_weights[i]);
		}
	}
	public static int numLength(int n) {
		if (n < 10) return 1;
		if (n < 100) return 2;
		if (n < 1000) return 3;
		if (n < 10000) return 4;
		if (n < 100000) return 5;
		return 6;
		// throw new IllegalArgumentException("num should be 10^6");
	}
	public boolean rec_pakuj(int requiredCapacity, int i, StringBuilder sb) {
		String	temp		= sb.toString();
		int		oldlength	= sb.length();
		// if (found == true) {
		// 	return;
		// }
		if (requiredCapacity == 0) {
			return true;
		}
		if (requiredCapacity < 0) {
			return false;
		}
		if (i >= this._weights.length) {	// Sprawdzamy wszystkie elementy dla aktualnej funkcji iteracyjnyj
			return false;			// Brak elementow
		}
		requiredCapacity -= _weights[i];
		sb.append(" ").append(_weights[i]);
		if (rec_pakuj(requiredCapacity, i + 1, sb) == true) {
			return true;
		}
		requiredCapacity += _weights[i];
		sb.setLength(sb.length() - 1 - numLength(_weights[i]));	// Usuwanie spacji i dodanej wczesniej liczby

		return rec_pakuj(requiredCapacity, i + 1, sb);
	}
	public boolean iter_pakuj(int requiredCapacity, StringBuilder sb) {
		paramStack	stack	= new paramStack(2 * this._weights.length);
		boolean		found	= false;
		int			i		= 0;
		stack.push(new Params("CALL", requiredCapacity, 0));
		while (stack.nonEmpty() == true) {
			Params curPars = stack.pop();
			i = curPars.getCurIndex();
			requiredCapacity = curPars.getRequiredCapacity();
			String temp = sb.toString();
			switch (curPars.getAdrs()) {
				case "CALL": {
					if (requiredCapacity == 0) {
						found = true;
						// continue;
						return true;
					}
					if (requiredCapacity < 0) {
						i++;
						continue;
					}
					if (i >= this._weights.length) {
						continue;
					}
					requiredCapacity -= _weights[i];
					sb.append(" ").append(_weights[i]);
					stack.push(new Params("RESUME 1", requiredCapacity, i));
					stack.push(new Params("CALL", requiredCapacity, i + 1));
					break;
				}
				case "RESUME 1": {
					if (found == true) {
						return true;
					}
					requiredCapacity += _weights[i];
					sb.setLength(sb.length() - 1 - numLength(_weights[i]));
					stack.push(new Params("RESUME 2", requiredCapacity, i));
					stack.push(new Params("CALL", requiredCapacity, i + 1));
					break;
				}
				case "RESUME 2": {
					if (found == true) return true;
					break;
				}
			}

		}
		return found;
	}
	public String PackWeight() {
		StringBuilder	resultStr	= new StringBuilder("");
		StringBuilder	tempSB1		= new StringBuilder("");
		StringBuilder	tempSB2		= new StringBuilder("");
		boolean			result		= false;
		result = rec_pakuj(this._capacity, 0, tempSB1);
		if (result == false) {
			return "BRAK";
		}
		resultStr.append("REC:  ").append(this._capacity).append(" =").append(tempSB1.toString()).append("\n");
		result = iter_pakuj(this._capacity, tempSB2);
		if (result == false) {
			throw new IllegalStateException("Recursive and iterative give different result");
		}
		resultStr.append("ITER: ").append(this._capacity).append(" =").append(tempSB2.toString()).append("\n");
		return resultStr.toString();
	}
}

public class Source {

	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		StringBuilder	output		= new StringBuilder();

		int				test_count	= sc.nextInt();

		while (test_count-- > 0) {
			String		OP_resultStr	= "";
			int			capacity		= sc.nextInt();
			int			weightCount		= sc.nextInt();
			Backpack	bag				= new Backpack(capacity, weightCount);
			bag.fill(sc);
			System.out.print(bag.PackWeight());
		}
	}

}
