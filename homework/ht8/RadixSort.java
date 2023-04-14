
import java.util.*;

public class RadixSort {
	public static int getMax(int[] A) {
		final int	N	= A.length;
		int			max	= A[0];
		for (int i = 1; i < N; i++) {
			if (A[i] > max) {
				max = A[i];
			}
		}
		return max;
	}

	public static void countSortAt(int A[], int pos) {
		final int	N		= A.length;
		final int	max		= getMax(A);
		int[]		output	= new int[N + 1];
		int[]		count	= new int[max + 1];

		for (int i = 0; i < max; ++i)
			count[i] = 0;

		for (int i = 0; i < N; i++)
			count[(A[i] / pos) % 10]++;

		for (int i = 1; i < 10; i++) {
			count[i] += count[i - 1];
		}

		for (int i = N - 1; i >= 0; i--) {
			output[count[(A[i] / pos) % 10] - 1] = A[i];
			count[(A[i] / pos) % 10]--;
		}

		for (int i = 0; i < N; i++) {
			A[i] = output[i];
		}
	}

	public static void sort(int arr[]) {
		int	size	= arr.length;
		int	max		= getMax(arr);

		// Using count sort but digit by digit starting from the first one
		for (int place = 1; max / place > 0; place *= 10) {
			countSortAt(arr, place);
		}
	}
}
