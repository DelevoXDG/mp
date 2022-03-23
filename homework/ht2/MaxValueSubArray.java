package homework.ht2;

import java.io.*;
import java.util.Scanner;

public class MaxValueSubArray {

	public static void menu() {
		System.out.println("\n_____________________________");
		System.out.println("Operations:  ");
		System.out.println("_______________________________");
		System.out.println(" 1. n^3 ");
		System.out.println(" 2. n^2 ");
		System.out.println(" 3. n");
		System.out.println(" 4. Change input");
		System.out.println(" 5. Exit");
		System.out.println("===============================");
		System.out.print("Select an option: ");
	}

	public static enum OP {
		n3(1), n2(2), n(3), change_algorithm(4), exit(5);

		public final int fId;

		private OP(int id) {
			this.fId = id;
		}

		public static OP forInt(int id) {
			for (OP op : values()) {
				if (op.fId == id) {
					return op;
				}
			}
			throw new IllegalArgumentException();
		}
	}

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		// Przykład 1: Maksymalna podtablica

		// Dane: tablica liczb całkowitych: a[0], ... , a[n-1] .
		// Problem. Wyznaczyć sumę maksymalnej podtablicy.

		// Jako maksymalną podtablicę definiujemy fragment ciągły tablicy a[i .. j], o
		// maksymalnej
		// nieujemnej sumie elementów, np. dla ciągu: -2, 7, -4, 8, -5, 4 to podciąg:
		// 7,-4, 8,
		// tworzy maksymaclną podtablicę o sumie równej 11.
		// Jeśli wszystkie liczby są ujemne to maksymalna podtablica jest pusta i suma
		// równa się 0.
		// Gdy wszystkie liczby są równe 0 to maksymalna podtablica zawiera pierwszy
		// element a[0] i
		// suma też równa się 0.
		OP op = OP.exit;
		do {
			System.out.println("\n_____________________________");
			System.out.print("Enter array size: ");
			int N = 0;

			N = sc.nextInt();
			// N = IOC.getInt2();
			int[] a = new int[N];
			for (int i = 0; i < N; i++) {
				System.out.print("[" + i + "]: ");
				a[i] = sc.nextInt();
			}
			System.out.println("===============================");
			for (int i = 0; i < N; i++) {
				System.out.print("[" + a[i] + "] ");
			}
			do {
				menu();
				int num = sc.nextInt(); // Parse operation
				try {
					op = OP.forInt(num);
				} catch (IllegalArgumentException e) {
					System.out.println("===============================");
					System.out.println("Illegal argument.");
					op = OP.change_algorithm;
				}
				int maxSum = 0;
				int maxSum_start_index = 0;
				int maxSum_end_index = 0;
				long startTime = System.nanoTime();

				switch (op) {
					case n3: {
						if (N > 0) {
							maxSum = a[0];
						}
						int curSum = 0;
						for (int i = 0; i < N; i++) { // subArray starting from i, i in(0,n)
							for (int j = i; j < N; j++) { // subArray ending at j, j in (i, n)
								curSum = 0;
								for (int k = i; k <= j; k++) { // calculating sum of a[i...j]
									curSum += a[k];
								}
								if (curSum >= maxSum) {
									maxSum = curSum;
									maxSum_start_index = i;
									maxSum_end_index = j;
								}
							}
						}
						break;
					}
					case n2: {
						int curSum = 0;
						if (N > 0) {
							maxSum = a[0];
						}
						for (int i = 0; i < N; i++) { // subArray starting from i, i in(0,n)
							curSum = 0;
							for (int j = i; j < N; j++) { // subArray ending at j, j in (i, n)
								curSum = curSum + a[j];

								if (curSum >= maxSum) {
									maxSum = curSum;
									maxSum_start_index = i;
									maxSum_end_index = j;
								}
							}
						}
						break;
					}
					case n: {
						int curSum = 0;
						int curSum_start_index = 0;

						if (N > 0) {
							maxSum = a[0];
						}
						for (int i = 0; i < N; i++) {
							curSum = curSum + a[i];
							if (curSum < 0) {
								curSum = 0;
								curSum_start_index = i + 1;
							} else if (curSum > maxSum || (curSum == maxSum && (curSum != 0 || maxSum < 0))) {
								maxSum = curSum;
								maxSum_start_index = curSum_start_index;
								maxSum_end_index = i;
							}
						}
						break;
					}
					case change_algorithm: {
						break;
					}
					case exit: //
					{
						break;
					}
					default: {
						System.out.println("Unexpected option.");
						System.out.println("===============================");
						break;
					}
				}

				if (op != OP.change_algorithm && op != OP.exit) {
					System.out.println("Max Sub Array: ");
					if (maxSum < 0) {
						System.out.println("empty");
					} else {
						for (int i = maxSum_start_index; i <= maxSum_end_index; i++) {
							System.out.print("[" + i + "] ");
						}
						System.out.println("");
						for (int i = maxSum_start_index; i <= maxSum_end_index; i++) {
							System.out.print(" " + a[i] + "  ");
						}
					}
					System.out.println("\nMax Sum: " + (maxSum >= 0 ? maxSum : "0"));

					long stopTime = System.nanoTime();
					System.out.println(((stopTime - startTime) / 1000000) + " ms");
				}
			} while (op != OP.change_algorithm && op != OP.exit);

		} while (op != OP.exit);
		sc.close();
	}

}

// in

// 5
// 5
// -6
// 7
// -4
// 8
// 1
// 2
// 3
// 4
// 3
// 0
// 0
// 0
// 1
// 2
// 3
// 4
// 3
// 0
// -4
// 0
// 1
// 2
// 3
// 4
// 3
// -5
// -2
// 0
// 1
// 2
// 3
// 4
// 2
// -8
// -5
// 1
// 2
// 3
// 5