import java.util.Scanner;
public class Source {
	public static Scanner sc = new Scanner(System.in); 	 
	private static void QS_Swap(int[] arr, final int a_i, final int b_i) {
		int tmp = arr[a_i];
		arr[a_i] = arr[b_i];
		arr[b_i] = tmp;
	}
	private static int QS_Partition(int[] arr, final int left, final int right) {
		final int	pivot	= arr[right]; 
		int			i		= left - 1; 
		for (int j = left; j < right; j++) { 
			if (arr[j] <= pivot) {	
				i++; 
				QS_Swap(arr, i, j);	
			}
		}
		QS_Swap(arr, i + 1, right);  	
		return i + 1; 					
	}
	private static void QS_quicksort(int[] arr, final int left, final int right) {
		if (left < right) {
			final int pivot_pos = QS_Partition(arr, left, right); 
			QS_quicksort(arr, left, pivot_pos - 1); 	
			QS_quicksort(arr, pivot_pos + 1, right);	
		}
	}
	public static int[] Quicksort(int[] arr) { 	
		QS_quicksort(arr, 0, arr.length - 1);	
		return arr;								
	}
	public static int min(final int a, final int b) { 
		if (a < b)
			return a;
		else
			return b;
	}
	public static int Latest_Idx_LesserThan(int[] arr, int left_i, int right_i, final int searched_Value) {
		int	result_i	= -1;	
		int	mid_i		= 0;	
		while (left_i <= right_i) { 
			mid_i = left_i + (right_i - left_i + 1) / 2;
			if (arr[mid_i] >= searched_Value) {
				right_i = mid_i - 1;  
			} else { 
				result_i = mid_i; 	
				left_i = mid_i + 1; 
			}
		}
		return result_i; 
	}
	public static void main(String[] args) {
		int test_count = sc.nextInt();  
		for (int test_num = 0; test_num < test_count; test_num++) {  
			int		arr_size		= sc.nextInt();			
			int[]	arr				= new int[arr_size];	
			int		triangles_count	= 0;					
			for (int i = 0; i < arr.length; i++) {
				arr[i] = sc.nextInt();						
			}
			arr = Quicksort(arr);							
			System.out.println((test_num + 1) + ": n= " + arr_size); 
			int itr = 0; 
			while (itr < arr.length) { 
				int top = min(arr.length - itr, 25); 		
				for (int j = itr; j < itr + top; j++) { 	
					System.out.print(arr[j] + " "); 		
				}
				itr += top; 								
				if (arr.length - itr > 1) {					
					System.out.println();
				}
			}
			System.out.println(); 
			for (int i = 0; i < arr.length; i++) {
				for (int j = i + 1; j < arr.length; j++) {
					int	last_checked_i		= j;	
					int	sum					= arr[i] + arr[j]; 
					int	third_side_last_i	= Latest_Idx_LesserThan(arr, last_checked_i, arr.length - 1, sum);
					int	cur_triangles_count	= third_side_last_i - last_checked_i;    
					if (cur_triangles_count > 0) { 
						for (int printed = 0; printed < min(cur_triangles_count, 10 - triangles_count); printed++) { 
							System.out.print("(" + i + "," + j + "," + (j + 1 + printed) + ") "); 
						}
					}
					triangles_count += cur_triangles_count; 
				}
			}
			if (triangles_count > 0) {  
				System.out.println("\nNumber of triangles: " + triangles_count); 
			} else {					
				System.out.println("Triangles cannot be built "); 				 
			}
		}
	}
}
