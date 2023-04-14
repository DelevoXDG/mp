import java.util.Scanner;
import java.util.Stack;
class pParams {
	private int _adrs;
	private int _i;
	private int _requiredCapacity;
	public pParams(int adrs, int requiredCapacity, int n) {
		this._adrs = adrs;
		this._requiredCapacity = requiredCapacity;
		this._i = n;
	}
	public int getAdrs() {
		return _adrs;
	}
	public int getCurIndex() {
		return _i;
	}
	public int getRequiredCapacity() {
		return _requiredCapacity;
	}
}
class pParamstack {
	private pParams[] arr;
	private int top;
	public pParamstack(final int maxSize) {
		arr = new pParams[maxSize];
		top = 0;
	}
	public void push(final pParams data) {
		if (top + 1 >= arr.length) {
			throw new ArrayIndexOutOfBoundsException("Stack Overflow");
		}
		arr[top++] = data;
	}
	public pParams pop() {
		if (top - 1 < 0) {
			throw new ArrayIndexOutOfBoundsException("Stack Underflow");
		}
		top--;
		return arr[top];
	}
	public pParams peek() {
		if (top <= 0) {
			throw new ArrayIndexOutOfBoundsException("Stack is empty");
		}
		return arr[top - 1];
	}
	public Boolean isEmpty() {
		return top == 0;
	}
	public Boolean nonEmpty() {
		return top != 0;
	}
	public Boolean isFull() {
		return top == arr.length;
	}
	public int size() {
		return top;
	}
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("");
		for (int i = 0; i < top; i++) {
			result.append(" ").append(arr[i]);
		}
		return result.toString();
	}
}
class Backpack {
	int[] W;
	int _capacity;
	int cap;
	StringBuilder sb;
	public Backpack(int capacity, int weightCount) {
		sb = new StringBuilder("");
		this.W = new int[weightCount];
		this._capacity = capacity;
		this.cap -= capacity;
	}
	public void fill(Scanner sc) {
		for (int i = 0; i < W.length; i++) {
			W[i] = sc.nextInt();
		}
	}
	public void print() {
		for (int i = 0; i < W.length; i++) {
			System.out.println(W[i]);
		}
	}
	public static int numLength(int num) {
		if (num < 10) return 1;
		if (num < 100) return 2;
		if (num < 1000) return 3;
		if (num < 10000) return 4;
		if (num < 100000) return 5;
		return 6;
	}
	public boolean rec_pakuj(int i) {
		while (i < W.length && cap > 0) {
			cap -= W[i];
			sb.append(" ").append(W[i]);
			if (rec_pakuj(i + 1) == true) {
				return true;
			}
			cap += W[i];
			sb.setLength(sb.length() - 1 - numLength(W[i]));
			i++;
		}
		return cap == 0;
	}
	public boolean iter_pakuj(int cap, StringBuilder sb) {
		Stack<Integer> s = new Stack<Integer>();
		s.push(0);
		boolean found = false;
		while (!s.isEmpty() && !found) {
			int i = s.pop();
			if (!s.isEmpty() && s.peek() == i) {
				s.pop();
				cap += W[i];
				sb.setLength(sb.length() - 1 - numLength(W[i]));
				s.push(i + 1);
			} else {
				if (cap == 0) { return true; }
				if (cap < 0) { continue; }
				if (i >= W.length) { continue; }
				cap -= W[i];
				sb.append(" ").append(W[i]);
				s.push(i);
				s.push(i);
				s.push(i + 1);
			}
		}
		return false;
	}
	public String PackWeight() {
		StringBuilder	resultStr	= new StringBuilder("");
		StringBuilder	sbIter		= new StringBuilder("");
		boolean			resultRec	= false;
		boolean			resultIter	= false;
		cap = _capacity;
		resultRec = rec_pakuj(0);
		if (resultRec != false) {
			resultStr.append("REC:  ").append(this._capacity).append(" =").append(this.sb.toString()).append("\n");
		}
		resultIter = iter_pakuj(this._capacity, sbIter);
		if (resultIter != resultRec) {
			throw new RuntimeException("Wrong result");
		} else if (resultRec == false) { return "BRAK\n"; }
		resultStr.append("ITER: ").append(this._capacity).append(" =").append(sbIter.toString()).append("\n");
		return resultStr.toString();
	}
}
public class Source {
	public static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		StringBuilder	output		= new StringBuilder();
		int				test_count	= sc.nextInt();
		while (test_count-- > 0) {
			String		resultStr	= "";
			int			capacity	= sc.nextInt();
			int			weightCount	= sc.nextInt();
			Backpack	backpack	= new Backpack(capacity, weightCount);
			backpack.fill(sc);
			resultStr = backpack.PackWeight();
			output.append(resultStr);
		}
		System.out.print(output);
	}
}
