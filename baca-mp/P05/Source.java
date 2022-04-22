
// Maksim Zdobnikau
import java.util.Scanner;

class Stack {

}

class Backpack {
	int[] _weights;
	int _capacity;
	public static int topi;

	boolean found = false;
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
			found = true;
			return true;
		}
		if (requiredCapacity < 0) {
			return false;
			// sb.append(" ").append(_weights[i]);
			// rec_pakuj_old(requiredCapacity, i + 1, sb);
		}
		if (i >= _weights.length) {
			return false;			// Brak elementow, liczba 
		}
		requiredCapacity -= _weights[i];
		sb.append(" ").append(_weights[i]);
		boolean check = rec_pakuj(requiredCapacity, i + 1, sb);
		if (check == true) {
			return true;
		}
		requiredCapacity += _weights[i];
		int newlength = sb.length();
		sb.setLength(newlength - 1 - numLength(_weights[i]));	// Usuwanie spacji i dodanej wczesniej liczby
		check = rec_pakuj(requiredCapacity, i + 1, sb);
		if (check == true) {
			return true;
		}
		return false;
	}

	public String PackWeight() {
		StringBuilder result = new StringBuilder("");
		rec_pakuj(this._capacity, 0, result);

		return result.toString();
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
			System.out.println(bag.PackWeight());
		}
	}

}
