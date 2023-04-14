import java.util.Arrays;

public class heapSort {
	private static void swap(int A[], int a, int b) {
		int temp = A[a];
		A[a] = A[b];
		A[b] = temp;
	}
	private static void buildHeap(int A[], int root, int curSize) {
		// ! using minHeap instead of maxHeap to sort in reverse order

		// Searching for the right position for value at root of current subtree
		int	min	= root; // Initialize smallest as root
		int	L	= 2 * root + 1; // left child at array representation of heap
		int	R	= 2 * root + 2; // right child at array representation of heap

		// If left child is smaller than root
		if (L < curSize && A[L] < A[min]) {
			min = L;					// Set it as current min
		}
		// If right child is smaller than smallest so far
		if (R < curSize && A[R] < A[min]) {
			min = R;					// Set it as current min
		}

		if (min == root) { // Heap is "built" for current node 
			return;
		}
		// Building tree for child
		swap(A, root, min);
		// Keep searching right position at child subtree
		buildHeap(A, min, curSize);
	}

	static void sort(int[] A) {
		int N = A.length;
		for (int i = N / 2 - 1; i >= 0; i--) {
			buildHeap(A, i, N);
		}
		// System.out.println(getHeap(A));
		// Build heap first 

		for (int i = N - 1; i >= 0; i--) { // Starting at the end of the array
			// Put minimal value element in correct place 
			swap(A, 0, i);
			// System.out.println(getHeap(A));
			// need to fix heap, so heapify again for first element
			// new size is i
			buildHeap(A, 0, i);
			// System.out.println(getHeap(A));

		}
	}

	private static String getHeap(int[] heap) {
		int[] newHeap = new int[heap.length + 1];
		newHeap[0] = 0;
		for (int i = 0; i < heap.length; i++) {
			newHeap[i + 1] = heap[i];
		}
		heap = newHeap;
		int				size		= heap.length - 1;
		int				maxHeight	= (int) (Math.log(size) / Math.log(2));  // log base 2 of n

		StringBuilder	hs			= new StringBuilder();  // heap string builder
		for (int curHeight = maxHeight; curHeight >= 0; curHeight--) {  // number of layers, we build this backwards
			int				layerLength	= (int) Math.pow(2, curHeight);  // numbers per layer

			StringBuilder	line		= new StringBuilder();  // line string builder
			for (int i = layerLength; i < (int) Math.pow(2, curHeight + 1); i++) {
				// before spaces only on not-last layer
				if (curHeight != maxHeight) {
					line.append(" ".repeat((int) Math.pow(2, maxHeight - curHeight)));
				}
				// extra spaces for long lines
				int loops = maxHeight - curHeight;
				if (loops >= 2) {
					loops -= 2;
					while (loops >= 0) {
						line.append(" ".repeat((int) Math.pow(2, loops)));
						loops--;
					}
				}

				if (i <= size) {
					// add in the number
					line.append(String.format("%-2s", heap[i]));  // add leading zeros
				} else {
					// or empty child
					line.append("**");
				}

				line.append(" ".repeat((int) Math.pow(2, maxHeight - curHeight)));  // after spaces
				// extra spaces for long lines
				loops = maxHeight - curHeight;
				if (loops >= 2) {
					loops -= 2;
					while (loops >= 0) {
						line.append(" ".repeat((int) Math.pow(2, loops)));
						loops--;
					}
				}
			}
			hs.insert(0, line.toString() + "\n");  // prepend line
		}
		return hs.toString();
	}
	public static void main(String[] args) {
		int arr[] = { 9, 6, 8, 1, 2, 7, 3, 4, 5, 0 };

		System.out.println(Arrays.toString(arr));
		sort(arr);
		System.out.println(Arrays.toString(arr));
		System.out.println(getHeap(arr));

	}
}
