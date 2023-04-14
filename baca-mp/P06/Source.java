import java.util.Scanner;
class musicCollection {					
	private String[] songs;				
	private String longestPrefix;		
	musicCollection(String[] array) {	
		songs = array;					
		if (array.length != 0) {
			longestPrefix = songs[0];	
		}
		this.arrangeAndFindPrefix(0, songs.length - 1);	
	}
	private static boolean isOdd(final int num) {	
		return num % 2 != 0;
	}
	private static boolean isEven(final int num) {	
		return num % 2 == 0;
	}
	private static void shiftLeftArray(String[] arr, final int start, final int end, final int offset) {
		for (int i = start; i < end - offset; i++) {
			arr[i] = arr[i + offset];
		}
	}
	private static int getFloor(final double num) {	
		return (int) Math.round(Math.floor(num));
	}
	private void swap(String[] arr, int a, int b) {	
		String tmp = arr[a];
		arr[a] = arr[b];
		arr[b] = tmp;
	}
	public void arrangeAndFindPrefix(int begin, int end) {	
		int curLen = end - begin + 1;		
		if (curLen <= 0) {	
			return;
		}
		if (curLen == 1) {	
			String	song		= songs[begin];	
			int		maxLength	= Math.min(song.length(), longestPrefix.length()); 
			for (int i = 0; i < maxLength; i++) {	
				if (song.charAt(i) != longestPrefix.charAt(i)) { 	
					longestPrefix = longestPrefix.substring(0, i);	
					return;											
				}
			}
			longestPrefix = longestPrefix.substring(0, maxLength);
			return;		
		}
		if (curLen == 2) {	
			arrangeAndFindPrefix(begin, begin); 
			arrangeAndFindPrefix(end, end);		
			return;
		}
		int last_tabA = getFloor((begin + end) / 2d);	
		if (isEven(last_tabA)) {						
			String tmp = songs[last_tabA];	
			if (isOdd(curLen)) {
				shiftLeftArray(songs, last_tabA, end + 1, 1);
				songs[end] = tmp;			
				arrangeAndFindPrefix(end, end);
				end = end - 1;
			} else {
				shiftLeftArray(songs, last_tabA, end, 1);
				songs[end - 1] = tmp;		
				arrangeAndFindPrefix(end - 1, end - 1);
				arrangeAndFindPrefix(end, end);
				end = end - 2;
			}
			last_tabA--;				
		}
		int	first_tabB1	= last_tabA + 1;				
		int	first_tabA2	= (begin + first_tabB1) / 2; 	
		int	len_tabA2	= last_tabA - first_tabA2 + 1;	
		for (int i = 0; i < len_tabA2; i++) {				
			swap(songs, first_tabA2 + i, first_tabB1 + i);	
		}
		arrangeAndFindPrefix(begin, last_tabA);  	
		arrangeAndFindPrefix(last_tabA + 1, end);	
	}
	@Override public String toString() {				
		StringBuilder	result	= new StringBuilder(""); 
		int				len		= songs.length;	
		for (int i = 0; i < len; i++) {
			result.append(songs[i] + " ");	
		}
		result.append("\n").append(longestPrefix).append("\n");	
		return result.toString();	
	}
}
public class Source {
	public static Scanner sc = new Scanner(System.in);			
	public static void main(String[] args) {
		StringBuilder	output		= new StringBuilder(""); 	 	
		int				test_count	= sc.nextInt();				
		while (test_count-- > 0) {	 							
			int			songCount	= sc.nextInt();				
			String[]	songs		= new String[songCount];	
			for (int i = 0; i < songs.length; i++) {
				songs[i] = sc.next();							
			}
			musicCollection music = new musicCollection(songs);	
			output.append(music);								
		}
		System.out.print(output);				
	}
}
