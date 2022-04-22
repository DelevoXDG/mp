
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

	public void rec_pakuj(int requiredCapacity, int i, StringBuilder sb) {

		if (found == true) {
			return;
		}
		if (i >= _weights.length) {
			return;			// Brak elementow, liczba 
		}
		requiredCapacity -= _weights[i];
		if (requiredCapacity == 0) {
			sb.append(_weights[i]).append(" ");
			found = true;
			return;
		}
		if (requiredCapacity < 0) {
			requiredCapacity += _weights[i];

		} else {
			sb.append(" ").append(_weights[i]);
		}
		// if (requiredCapacity > 0)
		rec_pakuj(requiredCapacity, i + 1, sb);

		if (found == false) {
			// if (requiredCapacity > 0) {
			// 	sb.setLength(sb.length() - 2);
			// }
			rec_pakuj(this._capacity, i + 1, sb);
		}
		return;
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
