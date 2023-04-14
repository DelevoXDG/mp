import java.util.Scanner;
class String_Stack {		
	private String[] arr;	
	private int size;		
	public String_Stack(final int maxSize) {	
		arr = new String[maxSize];				
		size = 0;								
	}
	public void push(final String data) {		
		if (size + 1 >= arr.length) {
			throw new ArrayIndexOutOfBoundsException("Stack Overflow Exception");
		}
		arr[size++] = data;
	}
	public String pop() {						
		if (size - 1 < 0) {
			throw new ArrayIndexOutOfBoundsException("Stack Underflow Exception");
		}
		size--;
		return arr[size];
	}
	public String peek() {						
		return size <= 0 ? "" : arr[size - 1];
	}
	public Boolean isEmpty() {					
		return size == 0;
	}
	public Boolean isNonEmpty() {				
		return size != 0;
	}
	public Boolean isFull() {					
		return size == arr.length;
	}
	public int size() {							
		return size;
	}
	@Override public String toString() {		
		String result = "";
		for (int i = 0; i < size - 1; i++) {
			result += arr[i] + " ";
		}
		if (size > 0)
			result += arr[size - 1];
		return result;
	}
}
class Char_Stack {			
	private char[] arr;		
	private int size;		
	public Char_Stack(final int maxSize) {		
		arr = new char[maxSize];				
		size = 0;								
	}
	public void push(final char data) {			
		if (size + 1 >= arr.length) {
			throw new ArrayIndexOutOfBoundsException("Stack Overflow");
		}
		arr[size++] = data;
	}
	public char pop() {							
		if (size - 1 < 0) {
			throw new ArrayIndexOutOfBoundsException("Stack Underflow");
		}
		size--;
		return arr[size];
	}
	public char peek() {						
		if (size <= 0) {
			throw new ArrayIndexOutOfBoundsException("Stack is empty");
		}
		return arr[size - 1];
	}
	public Boolean isEmpty() {					
		return size == 0;
	}
	public Boolean NonEmpty() {					
		return size != 0;
	}
	public Boolean isFull() {					
		return size == arr.length;
	}
	public int size() {							
		return size;
	}
	@Override public String toString() {		
		String result = "";
		for (int i = 0; i < size - 1; i++) {
			result += arr[i] + " ";
		}
		if (size > 0)
			result += arr[size - 1];
		return result;
	}
}
public class Source {
	public static Scanner sc = new Scanner(System.in); 	 
	public static class rpn {		
		static class NotDFA {		
			public enum State {		
				q0(false),
				q1(true),	
				q2(false),
				err(false);	
				final boolean isAcceptable;	
				State(boolean expr) {		
					isAcceptable = expr;
				}
				State z;	
				State o1;	
				State o2;	
				State lp;	
				State rp;	
				static {
					q0.z = q1;
					q0.lp = q0;
					q0.o1 = q2;
					q1.o2 = q0;
					q1.rp = q1;
					q2.z = q1;
					q2.lp = q0;
					q2.o1 = q2;
				}
				public State readNext(String symb) {	
					if (symb.equals("z")) {
						if (z == null) return err;		
						return z;						
					} else if (symb.equals("o1")) {
						if (o1 == null) return err;		
						return o1;						
					} else if (symb.equals("o2")) {
						if (o2 == null) return err;		
						return o2;						
					} else if (symb.equals("(")) {
						if (lp == null) return err;		
						return lp;						
					} else if (symb.equals(")")) {
						if (rp == null) return err;		
						return rp;						
					}
					return err;							
				}
				Boolean isAcceptable() {	
					return this.isAcceptable;
				}
				Boolean hasError() {		
					return this == err;
				}
				State getState() {			
					return this;
				}
			}
		}
		private static boolean isOperand(char symbol) {	
			return (symbol >= 'a' && symbol <= 'z');
		}
		private static boolean isOperator(char symbol) {
			return ("()~!^*/%+-<>?&|=".contains(String.valueOf(symbol)));
		}
		private static boolean isRightSide(char symbol) {
			return ("~!^=".contains(String.valueOf(symbol)));
		}
		private static boolean isUnary(char symbol) {	
			return ("~!".contains(String.valueOf(symbol)));
		}
		private static boolean isCorrectINFSymbol(char symbol) {	
			return isOperator(symbol) || isOperand(symbol);
		}
		private static boolean isCorrectRPNSymbol(char symbol) {	
			return ("~!^*/%+-<>?&|=".contains(String.valueOf(symbol))) || isOperand(symbol);
		}
		private static int getPriority(char OR) {	
			if (OR == 'Z') return 20;				
			if (OR == '(') return 10;
			if (OR == ')') return 10;
			if (OR == '~') return 9;
			if (OR == '!') return 9;
			if (OR == '^') return 8;
			if (OR == '*') return 7;
			if (OR == '/') return 7;
			if (OR == '%') return 7;
			if (OR == '+') return 6;
			if (OR == '-') return 6;
			if (OR == '>') return 5;
			if (OR == '<') return 5;
			if (OR == '?') return 4;
			if (OR == '&') return 3;
			if (OR == '|') return 2;
			if (OR == '=') return 1;
			throw new IllegalArgumentException("Unknown symbol"); 
		}
		private static String encloseInBrackets(String str) {	
			return "( " + str + " )";
		}
		public static String rpnParse(String infStr) {	
			Char_Stack		stack			= new Char_Stack(infStr.length()); 
			Char_Stack		result			= new Char_Stack(infStr.length()); 
			int				leftPar_count	= 0;	
			int				rightPar_count	= 0;	
			NotDFA.State	automatState	= NotDFA.State.q0;	
			for (int i = 0; i < infStr.length(); i++) {	
				char sym = infStr.charAt(i);		
				if (isCorrectINFSymbol(sym)) {		
					if (isOperand(sym)) {			
						automatState = automatState.readNext("z");  
						result.push(sym);					  
					} else if (sym == '(') {
						automatState = automatState.readNext("(");  
						leftPar_count++;					  
						stack.push(sym);					  
					} else if (sym == ')') {
						automatState = automatState.readNext(")");  
						rightPar_count++;					  
						while (stack.NonEmpty() && stack.peek() != '(') {	
							result.push(stack.pop());						
						}
						if (stack.NonEmpty() && stack.peek() == '(') {		
							stack.pop();
						} else {											
							return "error";									
						}
					} else if (isOperator(sym)) {
						if (isRightSide(sym)) {	
							if (isUnary(sym)) {	
								automatState = automatState.readNext("o1");  
							} else {
								automatState = automatState.readNext("o2");  
							}
							while (stack.NonEmpty() && stack.peek() != '('   
									&& getPriority(stack.peek()) > getPriority(sym)) {	
								result.push(stack.pop());								
							}
						} else {
							automatState = automatState.readNext("o2");	 		
							while (stack.NonEmpty() && stack.peek() != '(' &&		
									getPriority(stack.peek()) >= getPriority(sym)) {  	
								result.push(stack.pop());								
							}
						}
						stack.push(sym);										
					} else {
						throw new IllegalArgumentException("Unknown symbol detected");	
					}
					if (automatState.hasError()) { 
						return "error";			
					}
				}
			}
			if (automatState.isAcceptable == false || leftPar_count != rightPar_count) {	
				return "error"; 
			}
			while (stack.NonEmpty()) {	
				result.push(stack.pop());
			}
			return result.toString();	
		}
		public static String infParse(String rpnStr) {		
			String_Stack	result			= new String_Stack(rpnStr.length());
			Char_Stack		outline_stack	= new Char_Stack(rpnStr.length());		
			for (int i = 0; i < rpnStr.length(); i++) {				
				char sym = rpnStr.charAt(i);
				if (isCorrectRPNSymbol(sym)) {	 	 	 	 
					if (isOperand(sym)) {					
						outline_stack.push('Z');			
						result.push(String.valueOf(sym));	
					} else if (isOperator(sym)) { 
						if (isUnary(sym)) {	
							if (outline_stack.isEmpty()) {	
								return "error";				
							}
							char	prevOutline	= outline_stack.pop();
							String	prevExpr	= result.pop();
							if (getPriority(prevOutline) < getPriority(sym)) { 
								prevExpr = encloseInBrackets(prevExpr);			
							}
							outline_stack.push(sym);
							result.push(sym + " " + prevExpr); 
						} else { 
							if (outline_stack.size() < 2) {
								return "error";
							}
							char	prevOB	= outline_stack.pop();
							char	prevOA	= outline_stack.pop();
							String	prevB	= result.pop();
							String	prevA	= result.pop();
							if (getPriority(prevOA) < getPriority(sym)) { 
								prevA = encloseInBrackets(prevA);
							}
							if (getPriority(prevOB) < getPriority(sym)) {  
								prevB = encloseInBrackets(prevB);  
							} else if (isRightSide(sym) == false && getPriority(prevOB) == getPriority(sym)) { 
								prevB = encloseInBrackets(prevB);  
							}
							outline_stack.push(sym);
							result.push(prevA + " " + sym + " " + prevB);	
						}
					} else {
						throw new IllegalArgumentException("Unknown symbol");	
					}
				}
			}
			if (result.size() != 1) { 
				return "error"; 
			}
			return result.toString();	
		}
	}
	public static void main(String[] args) {
		int test_count = sc.nextInt();	
		while (test_count-- > 0) {	
			String	inputType	= sc.next();	
			String	inputStr	= sc.nextLine();
			if (inputType.charAt(0) == 'I') { 
				System.out.println("ONP: " + rpn.rpnParse(inputStr));	
			} else {						 
				System.out.println("INF: " + rpn.infParse(inputStr));	
			}
		}
	}
}
