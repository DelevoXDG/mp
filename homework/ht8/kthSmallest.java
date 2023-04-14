public class kthSmallest {
	public static <T> void swap(int[] arr, final int A, final int B) {
		int temp = arr[A];
		arr[A] = arr[B];
		arr[B] = temp;
	}

	public static int partition(int[] arr, final int L, final int R, final int pivotIndex) {
		int	i		= L - 1;
		int	j		= R;
		int	pivot	= arr[pivotIndex];

		do {
			while (pivot > arr[++i]) {
			}
			while (j > L && pivot < arr[--j]) {
			}
			if (i < j) {
				swap(arr, i, j);
			}
		} while (i < j);

		swap(arr, i, pivotIndex);
		return i;
	}

	public static int getKthSmallest(int[] arr, int L, int R, int k) {
		if (L == R) {
			return arr[L];
		}

		// int	pivotIndex	= L + (int) Math.floor(Math.random() * (R - L + 1));
		int	pivotIndex	= R;
		int	part		= partition(arr, L, R, pivotIndex);

		if (part == k - 1) {
			// If in the index of pivot is k-1 after partition(), than that pivot is the k-th smallest element 
			return arr[part];
		}
		// After pivot index after partition rests after smaller values and before larger values
		// If pivot index after partition is >= than (k-1), than k-th smallest will be found in the left subarray  
		if (part >= k - 1) {
			return getKthSmallest(arr, L, part - 1, k);
		} else {
			// Else it will be found in right subarray
			return getKthSmallest(arr, part + 1, R, k);
		}
	}

}
