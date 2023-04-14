class qs {
	public void swap(int i, int j) {// zamiana położenia dwóch elementów
		int tmp = A[i];
		A[i] = A[j];
		A[j] = tmp;
	}

	public int Partition(int L, int R) { //wesja Hoare’a
		int	i		= L - 1, j = R;
		int	pivot	= A[R]; // ostatni element jest dzielący
		while (true) {
			while (A[++i] < pivot) {
			}
			; // i=L po ++i
				// po wyjściu z pętli element: A[i] ≥ pivot
			while (j > L && A[--j] > pivot) {
			}
			; // j=R-1 po --j
				// po wyściu z pętli element: A[j] ≤ pivot lub j == L

			if (i >= j)
				break;
			else
				swap(i, j);
		} // koniec pętli while(true)
		swap(i, R); // zamieniamy elementy A[i] i A[R]
		return i;
	}

	public void QuickSort(int L, int R) {
		if (L >= R)
			return; // jeśli rozmiar podtablicy jest <= 1
		// elementy są już posortowane
		else {
			int q = Partition(L, R);
			QuickSort(L, q - 1);
			QuickSort(q + 1, R);
		}
	}
}