import java.util.Scanner;

class Votes {
	private int[] votes;
	private StringBuilder result;
	private static final int smallSize = 50;
	Votes(int[] arr, int[] queries) {
		this.votes = arr;
		this.result = new StringBuilder("");
		int N = queries.length;
		for (int i = 0; i < N; i++) {
			this.result.append(queries[i]).append(" ");
			int M = arr.length;
			if (queries[i] < 1 || queries[i] > M) {
				this.result.append("brak\n");
				continue;
			}
			int voteCount = getVotesAt(0, votes.length - 1, queries[i]);
			this.result.append(voteCount);
			this.result.append("\n");
		}
	}
	public void swap(final int A, final int B) {
		int temp = votes[A];
		votes[A] = votes[B];
		votes[B] = temp;
	}
	private int firstIndex(int L, int R, final int value) {
		while (L < R) {
			if (votes[L] == value) {
				return L;
			}
			L++;
		}
		return R;
	}
	private void bubblePlus(final int L, final int R) {
		boolean	check	= false;
		int		nextR	= L;
		for (int i = R; i > L; i--) {
			if (check == true) {
				check = false;
				i = nextR;
			}
			for (int j = L; j < i; j++) {
				if (votes[j] > votes[j + 1]) {
					swap(j, j + 1);
					check = true;
					nextR = j;
				}
			}
		}
	}
	private int partition(final int L, final int R, final int pivotPos) {
		int	i	= L - 1;
		int	j	= R;
		swap(pivotPos, R);
		int pivot = votes[R];
		do {
			while (pivot > votes[++i]) {}
			while (j > L && votes[--j] > pivot) {}
			if (i < j) {
				swap(i, j);
			}
		} while (i < j);
		swap(i, R);
		return i;
	}
	private int getVotesAt(final int L, final int R, int k) {
		int size = R - L + 1;
		if (size < smallSize) {
			bubblePlus(L, R);
			return votes[L + k - 1];
		}
		int fivesCount = (int) Math.ceil(size / 5d);
		for (int i = 0; i < fivesCount; i++) {
			int	curL	= L + 5 * i;
			int	curR	= curL + 4;
			if (curR > R) {
				curR = R;
			}
			bubblePlus(curL, curR);
			int medPos = ((curL + curR + 1) / 2);
			swap(L + i, medPos);
		}
		int	medMedPos		= 0;
		int	medMedVal		= 0;

		int	nextMedianPos	= ((fivesCount) / 2);
		medMedVal = getVotesAt(L, L + fivesCount - 1, nextMedianPos);
		medMedPos = firstIndex(L, L + fivesCount - 1, medMedVal);

		int	pivotPos	= partition(L, R, medMedPos);
		int	pivotFirst	= pivotPos;
		for (int i = L; i < pivotFirst; i++) {
			if (votes[i] == medMedVal) {
				swap(i, --pivotFirst);
			}
		}
		int pivotLast = pivotPos;
		for (int i = pivotPos + 1; i <= R; i++) {
			if (votes[i] == medMedVal) {
				swap(i, ++pivotLast);
			}
		}
		int	cardA	= pivotFirst - L;
		int	cardB	= pivotLast - pivotFirst + 1;
		if (k <= cardA) {
			return getVotesAt(L, pivotFirst - 1, k);
		}
		if (k > cardA + cardB) {
			return getVotesAt(pivotLast + 1, R, k - cardA - cardB);
		}
		return medMedVal;
	}
	@Override
	public String toString() {
		return result.toString();
	}
}

public class Source {
	public static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		StringBuilder	output		= new StringBuilder("");
		int				test_count	= sc.nextInt();
		while (test_count-- > 0) {
			int		N		= sc.nextInt();
			int[]	votes	= new int[N];
			for (int i = 0; i < N; i++) {
				votes[i] = sc.nextInt();
			}
			int		M		= sc.nextInt();
			int[]	queries	= new int[M];
			for (int i = 0; i < M; i++) {
				queries[i] = sc.nextInt();
			}
			Votes result = new Votes(votes, queries);
			output.append(result);
		}
		System.out.print(output);
	}
}
