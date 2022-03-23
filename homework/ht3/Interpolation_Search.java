// Maksim Zdobnikau - 2

import java.util.Scanner;

class Algorithm {
	public static int Interpolation_Search(int arr[], int size, int searched_Value) { // Interpolation Search
		int	low_bound		= 0; 				// Lower bound
		int	up_bound		= size - 1; 		// Upper bound
		int	cur_probe_id	= -1;				// Probe element idx
		int	valuePos_id		= -1; 				// Searched_Value idx

		while ((arr[low_bound] <= searched_Value) && (searched_Value <= arr[up_bound])) {
			cur_probe_id = low_bound
					+ (searched_Value - arr[low_bound]) * (up_bound - low_bound) / (arr[up_bound] - arr[low_bound]);

			if (arr[cur_probe_id] == searched_Value) { 			// Checks proble for equality
				valuePos_id = cur_probe_id;
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
				System.out.println(test_arr[i] + " ");
			}
			System.out
					.println("Searched value : a[" + Algorithm.Interpolation_Search(test_arr, n, searched_Value) + "]");
		}
	}
}
