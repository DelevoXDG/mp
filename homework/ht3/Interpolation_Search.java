// Maksim Zdobnikau - 2

import java.util.Scanner;

class Algorithm {

	public static int Interpolation_Search(int arr[], int size, int searched_Value) { // Interpolation Search
		int	low_bound		= 0; 				// Lower bound
		int	up_bound		= size - 1; 		// Upper bound
		int	cur_probe_id	= -1;				// Probe element idx
		int	valuePos_id		= -1; 				// Searched_Value idx

		while ((arr[low_bound] <= searched_Value) && (searched_Value <= arr[up_bound])) {
			if (arr[up_bound] - arr[low_bound] == 0) {			//Avoiding diviosn by zero
				return up_bound;
			}
			cur_probe_id = low_bound
					+ ((searched_Value - arr[low_bound]) * (up_bound - low_bound) / (arr[up_bound] - arr[low_bound]));

			if (arr[cur_probe_id] == searched_Value) { 			// Checks proble for equality
				while (cur_probe_id < size - 1 && arr[cur_probe_id + 1] == searched_Value) { // Checks for same elements
					cur_probe_id++;
				}
				return cur_probe_id;
			} else if (searched_Value > arr[cur_probe_id]) { 	// Checks probe in relation to both bounds  
				low_bound = cur_probe_id + 1;					// And adjusts it
			} else {
				up_bound = cur_probe_id - 1;
			}
		}

		return valuePos_id; 	//returns -1 if not found
	}
}

public class Interpolation_Search {
	public static Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		int test_num = in.nextInt(); // Number of Tests
		while (test_num-- > 0) {
			int		n			= in.nextInt();
			int[]	test_arr	= new int[n];
			for (int i = 0; i < n; i++) {
				test_arr[i] = in.nextInt();
			}
			int searched_Value = in.nextInt();

			for (int i = 0; i < n; i++) {
				System.out.print(test_arr[i] + " ");
			}
			System.out.print("\n");
			System.out.println(
					"Searched value index : a[" + Algorithm.Interpolation_Search(test_arr, n, searched_Value) + "]");
		}
	}
}

//4
// 10
// 1 3 6 10 30 30 30 30 39 45
// 30
// 5
// 20
// 160
// 15000
// 15000
// 32000
// 15000
// 3
// 2
// 10
// 11
// 7
// 8
// 5
// 5
// 5
// 5
// 5
// 5
// 5
// 5
// 5
