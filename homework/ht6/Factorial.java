import java.util.Scanner;

public class Factorial {
	public static Scanner sc = new Scanner(System.in);

	public static long Fact1(long num) {
		if (num > 1)
			return num * Fact1(num - 1);
		return 1;
	}

	public static long Fact2(long n, long result) {	// Ogonowa
		if (n == 0)
			return result;
		return Fact2(n - 1, result * n);
	}

	public static long Fact2(long n) {
		return Fact2(n, 1);
	}

	public static void main(String[] args) {

		long n = sc.nextLong();

		System.out.println(Fact1(n));
		System.out.println(Fact2(n));
	}
}