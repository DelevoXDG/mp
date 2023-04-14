import java.util.Scanner;
class Helpers { 
	public static <T> void swap(T[] arr, int A, int B) {
		T temp = arr[A];
		arr[A] = arr[B];
		arr[B] = temp;
	}
}
class iterableArray {
	private int[] arr; 			
	private int iterator;		
	public iterableArray(int[] arr, int index) {
		this.arr = arr;
		this.iterator = index;
	}
	public int[] getArray() {
		return this.arr;
	}
	public void setArray(int[] arr) {
		this.arr = arr;
	}
	public int getIndex() {
		return this.iterator;
	}
	public void setIndex(int index) {
		this.iterator = index;
	}
	public int getValue() {
		if (this.iterator >= this.arr.length) {
			throw new ArrayIndexOutOfBoundsException("Index " + (this.iterator + 1) + " for size " + this.arr.length);
		}
		return this.arr[iterator];
	}
	public void moveRight(int offset) {
		this.iterator += offset;
	}
	public boolean isEmpty() {
		return iterator >= arr.length;
	}
	public int compareTo(iterableArray arr) {
		if (this.getValue() == arr.getValue()) {
			return 0;
		}
		if (this.getValue() < arr.getValue()) {
			return -1;
		}
		return 1;
	}
}
class minHeap {			
	private iterableArray[] matrix;	
	private int N;					
	minHeap(int N) {				
		this.matrix = new iterableArray[N];
		this.N = N;
	}
	void build() {					
		for (int i = (N - 1) / 2; i >= 0; i--) {
			downHeap(i);
		}
	}
	public iterableArray getMin() {
		return this.matrix[0];
	}
	private int getMinChildPos(final int pos) {
		int	L	= 2 * pos + 1;
		int	R	= 2 * pos + 2;
		if (R >= N || matrix[R].compareTo(matrix[L]) > 0) {
			return L;
		}
		return R;
	}
	private int getParentPos(final int pos) {
		return (pos - 1) / 2;
	}
	public void upHeap(int pos) {
		iterableArray goingUp = matrix[pos];
		for (int i = getParentPos(pos); 
				pos > 0 && matrix[i].compareTo(goingUp) > 0; i = getParentPos(i)) {  
			matrix[pos] = matrix[i];
			pos = i; 
		} 
		matrix[pos] = goingUp;
	}
	public void downHeap(int k)
	{
		iterableArray	goingDown	= matrix[k];
		boolean			notBuilt	= (k < N / 2);
		while (notBuilt) {
			int L = getMinChildPos(k);
			if (goingDown.compareTo(matrix[L]) > 0) {
				matrix[k] = matrix[L];
				k = L;
				notBuilt = (k < N / 2);
			} else {
				notBuilt = false; 
			}
		} 
		matrix[k] = goingDown;
	}
	public void setArr(int i, iterableArray arr) {
		this.matrix[i] = arr;
	}
	public int getLength() {
		return N;
	}
	public void setLength(int N) {
		this.N = N;
	}
	public iterableArray[] getArray() {
		return this.matrix;
	}
	public void setArray(iterableArray[] matrix) {
		this.matrix = matrix;
	}
}
class Concatenated {
	private int[] result;
	public Concatenated(int[][] arrs) {
		this.result = mergeSorted(arrs);
	}
	private static int[] mergeSorted(int arrs[][]) {
		int		N			= arrs.length;
		minHeap	heap		= new minHeap(N);
		int		totalCount	= 0;
		for (int i = 0; i < N; i++) {
			iterableArray arr = new iterableArray(arrs[i], 0);
			heap.setArr(i, arr);
			totalCount += arrs[i].length;
		}
		heap.build(); 
		int[] result = new int[totalCount];
		for (int i = 0; i < totalCount; i++) {
			iterableArray cur = heap.getMin();
			result[i] = cur.getValue();
			cur.moveRight(1);
			if (cur.isEmpty()) {
				Helpers.swap(heap.getArray(), 0, heap.getLength() - 1);
				heap.setLength(heap.getLength() - 1);
			}
			heap.downHeap(0);
		}
		return result;
	}
	@Override public String toString() {
		StringBuilder	sb	= new StringBuilder("");
		int				N	= result.length;
		for (int i = 0; i < N - 1; i++) {
			sb.append(result[i]).append(" ");
		}
		if (N > 0) {
			sb.append(result[N - 1]);
		}
		return sb.toString();
	}
} 
public class Source {
	public static Scanner sc = new Scanner(System.in); 	
	public static void main(String[] args) {
		StringBuilder	output		= new StringBuilder(""); 
		int				test_count	= sc.nextInt(); 
		while (test_count-- > 0) { 
			String	result		= "";
			int		arrCount	= sc.nextInt();
			int[]	lengths		= new int[arrCount];
			for (int i = 0; i < arrCount; i++) {
				lengths[i] = sc.nextInt();
			}
			int[][] arrs = new int[arrCount][];
			for (int i = 0; i < arrCount; i++) {
				arrs[i] = new int[lengths[i]];
				for (int j = 0; j < lengths[i]; j++) {
					arrs[i][j] = sc.nextInt();
				}
			}
			output.append(new Concatenated(arrs)).append("\n");
		}
		System.out.print(output); 
	}
}
