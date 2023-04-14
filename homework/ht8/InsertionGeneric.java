
public class InsertionGeneric<T extends Comparable<? super T>> {
	public void sort(T[] array) {
		// start at the first index and iterate through to the end
		for (int i = 1; i < array.length; i++) {
			int currentIndex = i;
			/*
			 * Check:
			 * 1. that currentIndex is at least 1
			 * 2. that the item directly before the currentIndex is greater than the item at currentIndex
			 *
			 * If both conditions are met, swap the indexes
			 */
			while (currentIndex > 0 && array[currentIndex - 1].compareTo(array[currentIndex]) > 0) {
				T temp = array[currentIndex];
				array[currentIndex] = array[currentIndex - 1];
				array[currentIndex - 1] = temp;
				currentIndex--;
			}
		}
	}

}