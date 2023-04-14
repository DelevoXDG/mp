public class CountSort {
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

	public static int getMin(int[] A) {
		final int	N	= A.length;
		int			min	= A[0];
		for (int i = 1; i < N; i++) {
			if (A[i] < min) {
				min = A[i];
			}
		}
		return min;
	}

	public static void sort(int[] A) {
		int			N			= A.length;

		int			output[]	= new int[N];

		// Find range of array values
		final int	min			= getMin(A);
		final int	max			= getMax(A);
		final int	valRange	= max - min + 1;

		int			count[]		= new int[valRange];
		// Set array counting occurences to zero
		for (int i = 0; i < valRange; ++i) {
			count[i] = 0;
		}
		// Count occurences of each element
		for (int i = 0; i < N; i++) {
			int valRep = A[i] - min;
			count[valRep]++;
		}

		// Change count[i] so it contains last position of element of value i
		for (int i = 1; i < valRange; i++) {
			count[i] += count[i - 1];
		}

		// Reverse order to insure stability
		for (int i = N - 1; i >= 0; i--) {
			int	VAL		= A[i];
			int	ValREP	= (VAL - min);					// Value representation of current element in input array
			int	last	= (count[ValREP] - 1);  		// Last index for arr[i]-1 to occur
			output[last] = VAL;
			count[ValREP]--;
		}

		for (int i = 0; i < N; ++i) {
			A[i] = output[i];
		}
	}

}
