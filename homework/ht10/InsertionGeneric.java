
public class InsertionGeneric<T extends Comparable<? super T>> {
	public void sort(T[] array) {
		for (int i = 1; i < array.length; i++) {
			int cur = i;

			while (cur > 0 && array[cur - 1].compareTo(array[cur]) < 0) {
				T temp = array[cur];
				array[cur] = array[cur - 1];
				array[cur - 1] = temp;
				cur--;
			}
		}
	}

}