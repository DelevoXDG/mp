import java.util.Scanner;
class Params {				
	private int context;	
	public Node node;		
	public Params(int adrs, Node node) {	
		this.context = adrs;
		this.node = node;
	}
	public int getContext() {
		return context;
	}
	public Node getNode() {
		return node;
	}
}
class paramsStack {			
	private Params[] arr;	
	private int top;		
	paramsStack(final int initSize) {		
		arr = new Params[initSize];
		top = 0;
	}
	public void push(final Params p) {		
		if (this.isFull()) {				
			Params[] tmp = new Params[this.arr.length * 2];	
			for (int i = 0; i < this.arr.length; i++) {
				tmp[i] = this.arr[i];
			}
			this.arr = tmp;		
		}
		arr[top++] = p;		
	}
	public Params pop() {
		if (top - 1 < 0) {
			throw new ArrayIndexOutOfBoundsException("Stack underflow");
		}
		top--;
		return arr[top];	
	}
	public Params peek() {	
		if (top <= 0) {
			throw new ArrayIndexOutOfBoundsException("Stack is empty");
		}
		return arr[top - 1];
	}
	public boolean isEmpty() {
		return top == 0;
	}
	public boolean nonEmpty() {
		return top != 0;
	}
	public boolean isFull() {
		return top == arr.length;
	}
	public int size() {
		return top;
	}
}
class Person {
	public int priority;			
	public String name;				
	public String surname;			
	Person(String name, String surname, int priority) {
		this.name = name;
		this.surname = surname;
		this.priority = priority;
	}	
}	
class Node {
	public Person info;		
	public Node left;			
	public Node right;			
	Node(Person value) {
		this.info = value;
		this.left = null;
		this.right = null;
	}	
}
class BST {
	private Node root;		
	BST() {
		this.root = null;
	}
	BST(Person[] arr, String orderName) {
		Order order = parseOrder(orderName, arr);	
		this.root = order.Create(arr[order.getStartingIndex()], Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	public Node getRoot() {			
		return this.root;
	}
	public void setRoot(Node root) {
		this.root = root;		
	}
	public Order parseOrder(String orderName, Person[] arr) {
		switch (orderName) {
			case "PREORDER":
				return new preOrder(arr);
			case "INORDER":
				return new inOrder(arr);
			case "POSTORDER":
				return new postOrder(arr);
		}
		throw new IllegalArgumentException("Undefined order.");
	}
	public static String NodeToString(Person p) {
		StringBuilder sb = new StringBuilder("");
		sb.append(p.priority).append(" - ").append(p.name).append(" ").append(p.surname);
		return sb.toString();
	}
	public interface Order {			
		int getStartingIndex();			
		Node Create(Person data, int MIN, int MAX);	
		public String getString(Node start);		
	}
	public class preOrder implements Order {
		Person[] arr;			
		int index;				
		preOrder(Person[] arr) {
			this.arr = arr;
			this.index = 0;
		}
		preOrder() {
		}
		public int getStartingIndex() {
			this.index = 0;
			return this.index;
		}
		public Node Create(Person info, int MIN, int MAX) {
			if (index >= arr.length) {	
				return null;			
			}
			if (info.priority <= MIN) {
				return null;
			}
			if (info.priority >= MAX) {
				return null;
			}
			Node cur = new Node(info); 	
			index++;					
			if (index < arr.length) {
				cur.left = Create(arr[index], MIN, cur.info.priority);
			}
			if (index < arr.length) {
				cur.right = Create(arr[index], cur.info.priority, MAX);
			}
			return cur;	
		}
		public String getString(Node start) {
			StringBuilder	sb		= new StringBuilder("");	
			paramsStack		stack	= new paramsStack(8);		
			stack.push(new Params(0, start));			
			while (stack.isEmpty() == false) {			
				Params	curPars	= stack.pop();			
				Node	cur		= curPars.getNode();	
				switch (curPars.getContext()) {			
					case 0: {
						if (cur == null) {
							break;
						}
						Person p = cur.info;
						sb.append(p.priority).append(" - ").append(p.name).append(" ").append(p.surname).append(", "); 
						stack.push(new Params(1, cur));
						stack.push(new Params(0, cur.left));
						break;
					}
					case 1: {
						stack.push(new Params(0, cur.right));
						break;
					}
				}
			}
			sb.setLength(Math.max(0, sb.length() - 2)); 
			return sb.toString();	
		}
	}
	public class inOrder implements Order {
		Person[] arr;			
		inOrder(Person[] arr) {
			this.arr = arr;
		}
		inOrder() {
		}
		public int getStartingIndex() {
			return 0;
		}
		public Node Create(Person info, int MIN, int MAX) {
			throw new IllegalStateException("Undefined");
		}
		public String getString(Node start) {
			StringBuilder	sb		= new StringBuilder("");
			paramsStack		stack	= new paramsStack(8);
			stack.push(new Params(0, start));
			while (stack.isEmpty() == false) {
				Params	curPars	= stack.pop();
				Node	cur		= curPars.getNode();
				switch (curPars.getContext()) {
					case 0: {
						if (cur == null) {
							break;
						}
						stack.push(new Params(1, cur));
						stack.push(new Params(0, cur.left));
						break;
					}
					case 1: {
						Person p = cur.info;
						sb.append(p.priority).append(" - ").append(p.name).append(" ").append(p.surname).append(", ");
						stack.push(new Params(0, cur.right));
						break;
					}
				}
			}
			sb.setLength(Math.max(0, sb.length() - 2));
			return sb.toString();
		}
	}
	public class postOrder implements Order {
		Person[] arr;			
		int index;				
		postOrder(Person[] arr) {
			this.arr = arr;
			index = arr.length - 1;
		}
		postOrder() {
		}
		public int getStartingIndex() {
			this.index = arr.length - 1;
			return this.index;
		}
		public Node Create(Person info, int MIN, int MAX) {
			if (index < 0) {			
				return null;			
			}
			if (info.priority <= MIN) {
				return null;
			}
			if (info.priority >= MAX) {
				return null;
			}
			Node cur = new Node(info);
			index--;
			if (index >= 0) {
				cur.right = Create(arr[index], cur.info.priority, MAX);
			}
			if (index >= 0) {
				cur.left = Create(arr[index], MIN, cur.info.priority);
			}
			return cur;
		}
		public String getString(Node start) {
			paramsStack stack = new paramsStack(8);
			stack.push(new Params(0, start));
			StringBuilder sb = new StringBuilder("");
			while (stack.isEmpty() == false) {
				Params	curPars	= stack.pop();
				Node	cur		= curPars.getNode();
				switch (curPars.getContext()) {
					case 0: {
						if (cur == null) {
							break;
						}
						stack.push(new Params(1, cur));
						stack.push(new Params(0, cur.left));
						break;
					}
					case 1: {
						stack.push(new Params(2, cur));
						stack.push(new Params(0, cur.right));
						break;
					}
					case 2: {
						Person p = cur.info;
						sb.append(p.priority).append(" - ").append(p.name).append(" ").append(p.surname).append(", ");
						break;
					}
				}
			}
			sb.setLength(Math.max(0, sb.length() - 2));
			return sb.toString();
		}
	}
	public Node findPrev(final int priority, Node start) {
		if (this.findNodeAndParent(priority)[0] == null) {
			return null;
		}
		Node prevNode = null;	
		for (Node cur = start; cur != null;) {	
			if (cur.info.priority < priority) {
				prevNode = cur;	
				cur = cur.right;	
			} else {
				cur = cur.left;
			}
		}
		return prevNode;	
	}
	public Node findNext(final int priority, Node start) {
		if (this.findNodeAndParent(priority)[0] == null) {
			return null;
		}
		Node nextNode = null;
		for (Node cur = start; cur != null;) {		
			if (cur.info.priority > priority) {
				nextNode = cur;	
				cur = cur.left;	
			} else {
				cur = cur.right;
			}
		}
		return nextNode;
	}
	public Node findPrev(final int priority) {
		return findPrev(priority, this.root);
	}
	public Node findNext(final int priority) {
		return this.findNext(priority, this.root);
	}
	public Node[] findMin(final Node start) {
		Node	minNode		= null;	
		Node	prevprev	= null;	
		for (Node cur = start; cur != null;) {		
			prevprev = minNode;						
			minNode = cur;
			cur = cur.left;
		}
		return new Node[] { minNode, prevprev };		
	}
	public Node[] findMax(final Node start) {
		Node	maxNode		= null;	
		Node	prevprev	= null;	
		for (Node cur = start; cur != null;) {		
			prevprev = maxNode;						
			maxNode = cur;
			cur = cur.right;
		}
		return new Node[] { maxNode, prevprev };		
	}
	public Node enque(final Person toAdd, Node start) {
		if (start == null) {		
			start = new Node(toAdd);
		} else {	
			Node parent = null;
			for (Node cur = start; cur != null;) {
				parent = cur;	
				if (toAdd.priority < cur.info.priority) {
					cur = cur.left;
				} else {
					cur = cur.right;
				}
			}
			if (toAdd.priority < parent.info.priority) {
				parent.left = new Node(toAdd);
			} else {
				parent.right = new Node(toAdd);
			}
		}
		return start;			
	}
	public void enque(Person toAdd) {
		this.root = enque(toAdd, this.root);		
	}
	public Node[] findNodeAndParent(final int priority) {
		Node	parent	= null;		
		Node	cur		= null;		
		boolean	found	= false;	
		for (cur = this.root; cur != null && found == false;) {
			if (priority < cur.info.priority) {
				parent = cur;
				cur = cur.left;
			} else if (priority > cur.info.priority) {
				parent = cur;
				cur = cur.right;
			} else {
				found = true;	
			}
		}
		if (found == false) {	
			return new Node[] { null, null };	
		}
		return new Node[] { cur, parent };	
	}
	public Node deleteNode(Node toDel, Node parent) {
		if (toDel == null) {	
			return toDel;		
		}
		if (toDel.left == null || toDel.right == null) {
			Node replacement = null;	
			if (toDel.left == null && toDel.right == null) {	
				replacement = null;						
			} else if (toDel.left != null) {				
				replacement = toDel.left;					
			} else {									
				replacement = toDel.right;					
			}
			if (parent == null) {						
				this.root = replacement;				
			} else if (parent.left == toDel) {				
				parent.left = replacement;					
			} else {									
				parent.right = replacement;					
			}
			return toDel;								
		}
		Node	next		= null;				
		Node	nextParent	= null;				
		if (toDel.right.left == null) {
			next = toDel.right;
			toDel.right = next.right;
		} else {
			Node[] nodeInfo = findMin(toDel.right);
			next = nodeInfo[0];
			nextParent = nodeInfo[1];
			nextParent.left = next.right;
		}
		toDel.info = next.info;
		return toDel;			
	}
	public Node deleteValue(final int priority) {
		Node[]	nodeInfo	= findNodeAndParent(priority);	
		Node	toDel		= nodeInfo[0];		
		Node	parent		= nodeInfo[1];		
		return deleteNode(toDel, parent);		
	}
	public Node dequeMin() {
		Node[]	nodeInfo	= findMin(this.root);	
		Node	minNode		= nodeInfo[0];			
		Node	parent		= nodeInfo[1];			
		return deleteNode(minNode, parent);			
	}
	public Node dequeMax() {
		Node[]	nodeInfo	= findMax(this.root);	
		Node	maxNode		= nodeInfo[0];			
		Node	parent		= nodeInfo[1];			
		return deleteNode(maxNode, parent);			
	}
	public Node findMin() {
		return findMin(this.root)[0];
	}
	public Node findMax() {
		return findMax(this.root)[0];
	}
	public int getHeight(Node cur) {
		if (cur == null) {	
			return 0;
		}
		int	L	= getHeight(cur.left);		
		int	R	= getHeight(cur.right);		
		return Math.max(L, R) + 1;		
	}
	public String toString(Order order, Node start) {
		return order.getString(start);
	}
	public String toString(Order order) {
		return order.getString(this.root);
	}
}
public class Source {
	public static Scanner sc = new Scanner(System.in);	
	public static void main(String[] args) {
		int				test_count	= sc.nextInt();			
		StringBuilder	output		= new StringBuilder("");	
		for (int testNo = 1; testNo < test_count + 1; testNo++) {	
			int				opCount	= sc.nextInt();				
			BST				tree	= new BST();				
			StringBuilder	curOut	= new StringBuilder("");	
			while (opCount-- > 0) {								
				StringBuilder	result	= new StringBuilder("");		
				String			request	= sc.next();					
				switch (request) {	
					case "CREATE": {
						String		orderName	= sc.next();	
						int			peopleCount	= sc.nextInt();	
						Person[]	people		= new Person[peopleCount];	
						for (int i = 0; i < peopleCount; i++) {	
							int		priority	= sc.nextInt();
							String	name		= sc.next();
							String	surname		= sc.next();
							people[i] = new Person(name, surname, priority);
						}
						tree = new BST(people, orderName);	
						break;
					}
					case "DELETE": {
						int priority = sc.nextInt();		
						if (tree.deleteValue(priority) == null) {
							result.append(request).append(" ").append(priority).append(": BRAK");
						}
						break;
					}
					case "ENQUE": {
						int		priority	= sc.nextInt();
						String	name		= sc.next();
						String	surname		= sc.next();
						tree.enque(new Person(name, surname, priority)); 
						break;
					}
					case "DEQUEMAX": {
						result.append(request).append(": ");
						Node max = tree.dequeMax(); 	
						if (max == null) {
							result.append("BRAK");
							break;
						}
						result.append(BST.NodeToString(max.info));	
						break;
					}
					case "DEQUEMIN": {
						result.append(request).append(": ");
						Node min = tree.dequeMin();		
						if (min == null) {
							result.append("BRAK");
							break;
						}
						result.append(BST.NodeToString(min.info));	
						break;
					}
					case "NEXT": {
						int priority = sc.nextInt();
						result.append(request).append(" ").append(priority).append(": ");
						Node next = tree.findNext(priority, tree.getRoot());	
						if (next == null) {
							result.append("BRAK");
							break;
						}
						if (next.info.priority == priority) {
							result.append("BRAK");
							break;
						}
						result.append(BST.NodeToString(next.info));
						break;
					}
					case "PREV": {
						int priority = sc.nextInt();
						result.append(request).append(" ").append(priority).append(": ");
						Node prev = tree.findPrev(priority, tree.getRoot());
						if (prev == null) {
							result.append("BRAK");
							break;
						}
						if (prev.info.priority == priority) {
							result.append("BRAK");
							break;
						}
						result.append(BST.NodeToString(prev.info));
						break;
					}
					case "PREORDER": {
						result.append(request).append(": ");
						result.append(tree.toString(tree.new preOrder()));
						break;
					}
					case "INORDER": {
						result.append(request).append(": ");
						result.append(tree.toString(tree.new inOrder()));
						break;
					}
					case "POSTORDER": {
						result.append(request).append(": ");
						result.append(tree.toString(tree.new postOrder()));
						break;
					}
					case "HEIGHT": {
						result.append(request).append(": ");
						result.append(Integer.toString(Math.max(tree.getHeight(tree.getRoot()) - 1, 0)));
						break;
					}
				}
				if (result.length() != 0) {
					curOut.append(result.toString()).append("\n");	
				}
			}
			output.append("ZESTAW ").append(testNo).append("\n").append(curOut);	
		}
		System.out.print(output);	
	}
}
