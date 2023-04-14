import java.util.concurrent.TimeUnit;
import java.util.Arrays;

class qs {
	static void swap(int[] arr, int a, int b) {
		int temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}

	public static interface Partition {
		public int partitionFunction(int[] arr, int L, int R);
	}

	public static class Left implements Partition {
		public int partitionFunction(int[] arr, int L, int R) {
			int	pivot	= arr[L];
			int	i		= R;

			for (int j = i; j >= L + 1; j--) {
				if (arr[j] >= pivot) {
					swap(arr, i, j);
					i--;
				}
			}
			swap(arr, i, L);

			return i;
		}
	}

	public static class Median implements Partition {
		public int partitionFunction(int[] arr, int L, int R) {
			int m = (L + R) / 2;

			if (arr[m] > arr[L]) {
				swap(arr, L, m);
			}
			if (arr[L] > arr[R]) {
				swap(arr, L, R);
			}
			if (arr[m] > arr[R]) {
				swap(arr, m, R);
			}
			int	pivot	= arr[L];
			int	i		= R;

			for (int j = i; j > L; j--) {
				if (arr[j] >= pivot) {
					swap(arr, i, j);
					i--;
				}
			}
			swap(arr, i, L);

			return i;
		}
	}

	public static void quickSort(int[] arr, int L, int R, Partition partition) {
		int size = R - L + 1;
		if (size <= 3) {
			smallSort(arr, L, R);
			return;
		}
		int q = partition.partitionFunction(arr, L, R);
		quickSort(arr, L, q, partition);
		quickSort(arr, q + 1, R, partition);
	}

	public static void smallSort(int[] A, int L, int R) {
		int size = R - L + 1;
		if (size <= 1) {
			return;
		}
		if (size == 2) {
			if (A[L] > A[R]) {
				swap(A, L, R);
			}
			return;
		}
		if (A[L] > A[R - 1]) {
			swap(A, L, R - 1);
		}
		if (A[L] > A[R]) {
			swap(A, L, R);
		}
		if (A[R - 1] > A[R]) {
			swap(A, R - 1, R);
		}
	}
}

public class QuickSort {
	static void whichFaster(long time1, long time2, String name1, String name2) {
		System.out.println((time2 < time1 ? name2 : name1) + " was faster by "
				+ String.format("%.2f", ((Math.abs(time1 - time2) / (float) Math.max(time2, time1)) * (float) 100))
				+ "%");
	}

	static void printTwo(int[] arr1, int[] arr2) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < arr1.length; i++) {
			String diff = arr1[i] == arr2[i] ? "" : " not equal";
			sb.append(arr1[i]).append(" \t | ").append(arr2[i]).append(diff).append("\n");
		}
		System.out.print(sb.toString());
	}

	public static void main(String[] args) {
		int		test_count			= 50;
		long	totalElapsedLeft	= 0;
		long	totalElapsedMedian	= 0;
		int		n					= 1000000;
		for (int test_num = 0; test_num < test_count; test_num++) {

			int[]	arr1	= new int[n];
			int[]	arr2	= new int[n];
			for (int i = 0; i < n; i++) {
				int value = (int) (Math.random() * 10000000 + 1);
				arr1[i] = value;
				arr2[i] = value;
			}

			long	start			= 0;
			long	finish			= 0;
			long	leftElapsed		= 0;
			long	medianElapsed	= 0;

			System.out.println("Test " + (test_num + 1));

			start = System.nanoTime();
			qs.quickSort(arr1, 0, n - 1, new qs.Left());
			finish = System.nanoTime();

			leftElapsed = TimeUnit.NANOSECONDS.toMillis(finish - start);

			System.out.println("Left:   " + leftElapsed + "ms");

			start = System.nanoTime();
			qs.quickSort(arr2, 0, n - 1, new qs.Median());
			finish = System.nanoTime();

			medianElapsed = TimeUnit.NANOSECONDS.toMillis(finish - start);

			System.out.println("Median: " + medianElapsed + "ms");

			totalElapsedLeft += leftElapsed;
			totalElapsedMedian += medianElapsed;
			whichFaster(leftElapsed, medianElapsed, "Left", "Median");

			if (Arrays.equals(arr1, arr2) == false) {
				printTwo(arr1, arr2);
				throw new IllegalStateException("Different sort results");
			}

		}
		System.out.println("----------------");
		System.out.println("Tests ran:   " + test_count);
		System.out.println("Array size:  " + n);
		System.out.println("Total left:   " + totalElapsedLeft + " ms");
		System.out.println("Total median: " + totalElapsedMedian + " ms");
		whichFaster(totalElapsedLeft, totalElapsedMedian, "Left", "Median");
	}
}