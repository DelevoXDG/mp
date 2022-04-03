class BubbleSort {

	static void swap(int[] arr, final int i, final int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	static void Bubble_Sort_Wyk(int arr[]) {
		int n = arr.length;
		for (int i = n - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (arr[j] > arr[j + 1]) {
					swap(arr, j, j + 1);
				}
			}
		}
	}

	static void Bubble_Sort_Mod1(int arr[]) {
		int		n			= arr.length;
		boolean	check		= false;
		int		left_of_i	= 0; // [1] Index of position determining second loop exit
		for (int i = n - 1; i > 0; i--) {

			if (check == true) {  	// [1] Reducing amount of comparisons
				check = false;  	// [1]
				i = left_of_i;		// [1]
			}
			for (int j = 0; j < i; j++) {
				if (arr[j] > arr[j + 1]) {
					swap(arr, j, j + 1);
					left_of_i = j; // [1]
					check = true;  // [1] Remembering index of element where swap already occured 
				}
			}
		}
	}

	static void Bubble_Sort_Mod2(int arr[]) {
		int		n		= arr.length;
		boolean	sorted	= false;  // [2] Check whether swap was required at all	in inner loop		
		for (int i = n - 1; i > 0 && sorted == false; i--) {  // [2] If no swapping occured, array is sorted, hence no more comparisons required
			sorted = true;  // [2]  
			for (int j = 0; j < i; j++) {
				if (arr[j] > arr[j + 1]) {
					swap(arr, j, j + 1);
					sorted = false;  // [2]
				}
			}
		}

	}

	static void Bubble_Sort_Mod3(int arr[]) {
		int n = arr.length;
		for (int i = n - 1; i > 0; i--) {
			if (i % 2 == 0) {  // [3] Sorting starting comparisons from beginnings of the array
				for (int j = 0; j < i; j++) {
					if (arr[j] > arr[j + 1]) {
						swap(arr, j, j + 1);
					}
				}
			} else {		// [3] Sorting starting comparisons from end of the array
				for (int j = n - 1; j > 0; j--) {
					if (arr[j] < arr[j - 1]) {
						swap(arr, j, j - 1);
					}
				}
			}
		}
	}

	static void printArray(int arr[]) {
		int n = arr.length;
		for (int i = 0; i < n; ++i)
			System.out.print(arr[i] + " ");
		System.out.println();
	}

	static String Get_Array(int arr[]) {
		String	strarr	= "";
		int		n		= arr.length;
		for (int i = 0; i < n; i++) {
			strarr += arr[i] + " ";
		}
		return strarr;
	}
	//Hello world

	public static void main(String args[]) {

		int arr1[] = { 3, 1, 1, 5, 8, 2, 4, 7, 3, 2, 1, 2 };
		printArray(arr1);
		Bubble_Sort_Mod1(arr1);
		printArray(arr1);

		int arr2[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		printArray(arr2);
		Bubble_Sort_Mod2(arr2);
		printArray(arr2);

		int arr3[] = { 3, 1, 1, 5, 8, 2, 4, 7, 3, 2, 1, 2 };
		printArray(arr3);
		Bubble_Sort_Mod3(arr3);
		printArray(arr3);
	}

}