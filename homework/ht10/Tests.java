
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
// import sorts.*;

public class Tests {
	private static interface Algo {
		void sort(int[] arr);
	}

	private static class HeapSort_Test implements Algo {
		public void sort(int[] arr) {
			heapSort.sort(arr);
		}
	}

	private void testSort_int_stable(int[] test, Algo algo) {
		Integer[]					correct		= Arrays.stream(test).boxed().toArray(Integer[]::new);
		InsertionGeneric<Integer>	correctObj	= new InsertionGeneric<Integer>();
		correctObj.sort(correct);

		algo.sort(test);
		assertEquals(Arrays.toString(correct), Arrays.toString(test));

	}

	@Test public void heapSort() {
		Algo	algo	= new HeapSort_Test();

		int[]	test0	= { 9, 6, 8, 1, 2, 7, 3, 4, 5, 0 };
		testSort_int_stable(test0, algo);

		int[] test1 = { 9, 4, 9, 1, 2, 7, 3, 4, 5, 0 };
		testSort_int_stable(test1, algo);

		int[] test2 = { 1, 4, 1, 2, 7, 5, 2 };
		testSort_int_stable(test2, algo);

		int[] test3 = { -5, -10, 0, -3, 8, 5, -1, 10 };
		testSort_int_stable(test3, algo);
	}
}
