
// Java implementation QuickSort
// using Lomuto's partition Scheme

class Hoare {
	static int partition(int[] arr, int low, int high) {
		int	pivot	= arr[low];
		int	i		= low - 1, j = high + 1;

		while (true) {
			// Find leftmost element greater
			// than or equal to pivot
			qslomutohoare.printArray(arr, pivot, i, j, "\tpartition start", low, high);
			// do {
			// 	i++;
			// 	qslomutohoare.printArray(arr, pivot, i, j, "\ti++ until " + arr[i] + " >= " + pivot, low, high);
			// } while (arr[i] < pivot);
			while (arr[++i] < pivot) {
			}
			;
			// Find rightmost element smaller
			// than or equal to pivot
			// QuickSort.printArray(arr, pivot, i, j);
			do {
				j--;
				qslomutohoare.printArray(arr, pivot, i, j, "\tj-- until " + arr[j] + " <= " + pivot, low, high);
			} while (arr[j] > pivot);

			// If two pointers met.
			if (i >= j) {
				qslomutohoare.printArray(arr, pivot, i, j, "\tno swap, pointers met", low, high);
				// return j;
				break;
			} else {
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
				qslomutohoare.printArray(arr, pivot, i, j, "\tswapped", low, high);
			}
			// swap(arr[i], arr[j]);
		}
		swap(i, R);
		return i;
	}

	/*
	 * The main function that
	 * implements QuickSort
	 * arr[] --> Array to be sorted,
	 * low --> Starting index,
	 * high --> Ending index
	 */
	static void quickSort(int[] arr, int low, int high) {
		System.out.println("Quicksort(" + low + " " + high + ") ");
		if (low < high) {
			/*
			 * pi is partitioning index,
			 * arr[p] is now at right place
			 */
			int pi = partition(arr, low, high);

			// Separately sort elements before
			// partition and after partition
			quickSort(arr, low, pi);
			quickSort(arr, pi + 1, high);
		}
	}

}

class Lomato {
	static void Swap(int[] arr, int a, int b) {
		int temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;

	}

	/*
	 * This function takes last element as
	 * pivot, places the pivot element at its
	 * correct position in sorted array, and
	 * places all smaller (smaller than pivot)
	 * to left of pivot and all greater elements
	 * to right of pivot
	 */
	static int partition(int[] arr, int low, int high) {
		int	pivot	= arr[high];

		// Index of smaller element
		int	i		= (low - 1);

		for (int j = low; j <= high - 1; j++) {
			// If current element is smaller
			// than or equal to pivot
			if (arr[j] <= pivot) {
				qslomutohoare.printArray(arr, pivot, i + 1, j, "swapped (" + arr[j] + " <= " + pivot + ")", low, high);
				i++; // increment index of
					// smaller element
				Swap(arr, i, j);

			} else {
				qslomutohoare.printArray(arr, pivot, i, j, "no swap (" + arr[j] + " > " + pivot + ")", low, high);
			}

		}
		Swap(arr, i + 1, high);
		qslomutohoare.printArray(arr, pivot, i + 1, high, "swapped i+1 with high", low, high);
		return (i + 1);
	}

	/*
	 * The main function that
	 * implements QuickSort
	 * arr[] --> Array to be sorted,
	 * low --> Starting index,
	 * high --> Ending index
	 */
	static void quickSort(int[] arr, int low, int high) {
		System.out.println("Quicksort(" + low + " " + high + ") ");
		if (low < high) {
			/*
			 * pi is partitioning index,
			 * arr[p] is now at right place
			 */
			int pi = partition(arr, low, high);

			// Separately sort elements before
			// partition and after partition
			quickSort(arr, low, pi - 1);
			quickSort(arr, pi + 1, high);
		}
	}

}

public class qslomutohoare {
	/* Function to print an array */
	static void printArray(int[] arr, int pivot) {
		int i;
		for (i = 0; i < arr.length; i++)
			System.out.print(arr[i] + " ");
		System.out.println(" pivot = " + pivot);
	}

	static void printArray(int[] arr, int pivot, int i, int j) {

		for (int a = 0; a < arr.length; a++)
			System.out.print(arr[a] + " ");
		System.out.println(" pivot = " + pivot + "\t i = " + i + " j = " + j);
	}

	static void printArray(int[] arr, int pivot, int i, int j, String action, int lo, int hi) {
		if (lo == -1) {
			lo++;
			System.out.print("- ");
		}

		for (int a = lo; a < hi + 1; a++)
			System.out.print(arr[a] + " ");
		System.out.println(" pivot = " + pivot + "\t i = " + i + " j = " + j + " " + action);
	}

	static void printArray(int[] arr) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < arr.length; i++)
			sb.append(arr[i] + " ");
		System.out.println(sb.toString());
	}

	static void printTwo(int[] arr1, int[] arr2) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < arr1.length; i++) {
			String diff = arr1[i] == arr2[i] ? "" : " not equal";
			sb.append(arr1[i]).append(" \t | ").append(arr2[i]).append(diff).append("\n");

		}
		System.out.print(sb.toString());
	}

	static public void main(String[] args) {
		int[]	arr		= { 9, 4, 8, 1, 2, 7, 3, 4, 5, 0 };
		int[]	arr2	= { 9, 4, 8, 1, 2, 7, 3, 4, 5, 0 };
		int		n		= arr.length;
		// System.out.println("Lomato");
		// printArray(arr);
		// Lomato.quickSort(arr, 0, n - 1);
		// System.out.println("Sorted array: ");
		// printArray(arr);
		System.out.println("\n\n\nHoare");
		printArray(arr2);
		Hoare.quickSort(arr2, 0, n - 1);
		System.out.println("Sorted array: ");
		printArray(arr2);
	}
}
// This code is contributed by vt_m.
