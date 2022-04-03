class BIS {
	public static int BS_findPos(int[] arr, int left, int right, final int search_Value) {
		int mid = 0;
		while (left <= right) {
			mid = (left + right) / 2;

			if (search_Value < arr[mid]) {
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		return left;
	}

	static void binaryInsertionSort(int[] arr) {
		int size = arr.length;

		for (int i = 1; i < size; i++) {
			int insertion_Pos = BS_findPos(arr, //
					0, i - 1, // [0,i-1] - sorted subarray
					arr[i]);  // Searching positiong for current element in sorted subarray of elements before

			for (int j = i - 1; j >= insertion_Pos; j--) { // Shifting other array elements to allow insertion
				arr[j + 1] = arr[j];
			}

			arr[insertion_Pos] = arr[i]; 			// Inserting current element at required position
		}
	}

	static void printArray(int arr[]) {
		int n = arr.length;
		for (int i = 0; i < n; ++i)
			System.out.print(arr[i] + " ");
		System.out.println();
	}

	public static void main(String args[]) {
		int arr1[] = { 3, 1, 1, 5, 8, 2, 4, 7, 3, 2, 1, 2 };
		printArray(arr1);
		binaryInsertionSort(arr1);
		printArray(arr1);

	}
}