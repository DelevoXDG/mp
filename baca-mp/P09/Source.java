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

class ParamsExt {
	private int context;
	public Node node;
	private int value;
	public ParamsExt(int adrs, Node node) {
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

class paramsStackExt {
	private Params[] arr;
	private int top;
	paramsStackExt(final int initSize) {
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
	public Node L;
	public Node R;
	public Node parent;
	Node() {
		this.info = null;
		this.L = null;
		this.R = null;
		this.parent = null;
	}
	Node(Person value) {
		this.info = value;
		this.L = null;
		this.R = null;
		this.parent = null;
	}
	public Node(Person value, Node L, Node R, Node parent) {
		this.info = value;
		this.L = L;
		this.R = R;
		this.parent = parent;
	}
	public Person getValue() {
		return this.info;
	}
	public void setValue(Person value) {
		this.info = value;
	}
	public Node getLeft() {
		return this.L;
	}
	public void setLeft(Node L) {
		this.L = L;
		if (L == null) {
			return;
		}
		L.setParent(this);
	}
	public Node getRight() {
		return this.R;
	}
	public void setRight(Node R) {
		this.R = R;
		if (R == null) {
			return;
		}
		R.setParent(this);
	}
	public Node getParent() {
		return this.parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	@Override public String toString() {
		return "{" +
				"this='" + this.getValue() + "'" +
				", L='" + (L != null ? this.L.getValue() : "null") + "'" +
				", R='" + (R != null ? this.R.getValue() : "null") + "'" +
				", parent='" + (parent != null ? this.parent.getValue() : "null") + "'" +
				"}";
	}
}

class BST {
	private Node root;
	private StringBuilder result;
	private Person[] arr;
	private Order order;
	class Index {
		private int index;
		Index(int i) {
			index = i;
		}
		public int getIndex() {
			return this.index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public void next() {
			this.index++;
		}
		public void prev() {
			this.index--;
		}
	}
	public Node getRoot() {
		return this.root;
	}
	public void setRoot(Node root) {
		this.root = root;
	}
	BST() {
		this.root = null;
	}
	BST(Person[] arr, String orderName) {
		this.arr = arr;
		this.order = parseOrder(orderName);
		Index idx = order.getIndex();
		this.root = this.order.Create(idx, arr[idx.getIndex()], Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	public Order parseOrder(String orderName) {
		switch (orderName) {
			case "PREORDER":
				return new preOrder(this);
			case "INORDER":
				return new inOrder(this);
			case "POSTORDER":
				return new postOrder(this);
		}
		throw new IllegalArgumentException("Undefined order.");
	}
	public static String NodeToString(Person p) {
		StringBuilder sb = new StringBuilder("");
		sb.append(p.priority).append(" - ").append(p.name).append(" ").append(p.surname);
		return sb.toString();
	}
	private static interface Action {
		void action(Node node);
	}
	private static class Blank implements Action {
		public void action(Node node) {
		}
	}
	private static class Print implements Action {
		public void action(Node node) {
			System.out.println(node.getValue());
		}
	}
	private static class AddToList implements Action {
		public void action(Node node) {
		}
	}
	public interface Order {
		void traverse(Node cur);

		Index getIndex();

		Node Create(Index preIndex, Person data, int MIN, int MAX);

		public String getString(Node start);

		void setAction(Action action);
	}
	public class preOrder implements Order {
		Action act;
		Person[] arr;
		preOrder(BST tree) {
			this.arr = tree.arr;
			this.act = new Blank();
		}
		preOrder(Action action) {
			this.act = action;
		}
		public Index getIndex() {
			return new Index(0);
		}
		public Node Create(Index preIndex, Person data, int MIN, int MAX) {
			if (preIndex.getIndex() >= arr.length) {
				return null;
			}
			if (data.priority <= MIN) {
				return null;
			}
			if (data.priority >= MAX) {
				return null;
			}
			Node cur = new Node(data);
			preIndex.next();
			if (preIndex.getIndex() < arr.length) {
				cur.L = Create(preIndex, arr[preIndex.getIndex()], MIN, data.priority);
			}
			if (preIndex.getIndex() < arr.length) {
				cur.R = Create(preIndex, arr[preIndex.getIndex()], data.priority, MAX);
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
						Person p = cur.info;
						sb.append(p.priority).append(" - ").append(p.name).append(" ").append(p.surname).append(", ");
						stack.push(new Params(1, cur));
						stack.push(new Params(0, cur.L));
						break;
					}
					case 1: {
						stack.push(new Params(0, cur.R));
						break;
					}
				}
			}
			sb.setLength(Math.max(0, sb.length() - 2));
			return sb.toString();
		}
		public void setAction(Action action) {
			this.act = action;
		}
		public void traverse(Node cur) {
			if (cur != null) {
				act.action(cur);
				traverse(cur.getLeft());
				traverse(cur.getRight());
			}
		}
	}
	public class inOrder implements Order {
		Action act;
		Person[] arr;
		inOrder(BST tree) {
			this.arr = tree.arr;
			this.act = new Blank();
		}
		inOrder(Action action) {
			this.act = action;
		}
		public Index getIndex() {
			return new Index(0);
		}
		public Node Create(Index preIndex, Person data, int MIN, int MAX) {
			throw new IllegalStateException("Undefined");
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
						stack.push(new Params(0, cur.L));
						break;
					}
					case 1: {
						Person p = cur.info;
						sb.append(p.priority).append(" - ").append(p.name).append(" ").append(p.surname).append(", ");
						stack.push(new Params(0, cur.R));
						break;
					}
				}
			}
			sb.setLength(Math.max(0, sb.length() - 2));
			return sb.toString();
		}
		public void setAction(Action action) {
			this.act = action;
		}
		public void traverse(Node cur) {
			if (cur != null) {
				traverse(cur.getLeft());
				act.action(cur);
				traverse(cur.getRight());
			}
		}
	}
	public class postOrder implements Order {
		Action act;
		Person[] arr;
		postOrder(BST tree) {
			this.arr = tree.arr;
			this.act = new Blank();
		}
		postOrder(Action action) {
			this.act = action;
		}
		public Index getIndex() {
			return new Index(arr.length - 1);
		}
		public Node Create(Index postIndex, Person data, int MIN, int MAX) {
			if (postIndex.getIndex() >= arr.length) {
				return null;
			}
			if (data.priority <= MIN) {
				return null;
			}
			if (data.priority >= MAX) {
				return null;
			}
			Node cur = new Node(data);
			postIndex.prev();
			if (postIndex.getIndex() >= 0) {
				cur.R = Create(postIndex, arr[postIndex.getIndex()], data.priority, MAX);
			}
			if (postIndex.getIndex() >= 0) {
				cur.L = Create(postIndex, arr[postIndex.getIndex()], MIN, data.priority);
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
						stack.push(new Params(0, cur.L));
						break;
					}
					case 1: {
						stack.push(new Params(2, cur));
						stack.push(new Params(0, cur.R));
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
		public void setAction(Action action) {
			this.act = action;
		}
		public void traverse(Node cur) {
			if (cur != null) {
				traverse(cur.getLeft());
				traverse(cur.getRight());
				act.action(cur);
			}
		}
	}
	public Node findParent(int priority, Node start) {
		if (this.root == null) {
			return null;
		}
		if (this.root.info.priority == priority) {
			return null;
		}
		Node cur = root;
		while (cur != null) {
			if ((cur.L != null && cur.L.info.priority == priority)
					|| (cur.R != null && cur.R.info.priority == priority)) {
				return cur;
			}
			if (priority < cur.info.priority) {
				cur = cur.L;
			} else {
				cur = cur.R;
			}
		}
		return null;
	}
	public Node findPrev(int priority, Node start) {
		Node	cur			= start;
		Node	maxPriority	= null;
		while (cur != null) {
			if (priority > cur.info.priority) {
				maxPriority = cur;
				cur = cur.R;
			} else {
				cur = cur.L;
			}
		}
		return maxPriority;
	}
	public Node findNext(int priority, Node start) {
		Node	cur			= start;
		Node	minPriority	= null;
		while (cur != null) {
			if (priority < cur.info.priority) {
				minPriority = cur;
				cur = cur.L;
			} else {
				cur = cur.R;
			}
		}
		return minPriority;
	}
	public Node findMin(Node cur) {
		Node prev = null;
		while (cur != null) {
			prev = cur;
			cur = cur.getLeft();
		}
		return prev;
	}
	public Node findMax(Node cur) {
		Node prev = null;
		while (cur != null) {
			prev = cur;
			cur = cur.getRight();
		}
		return prev;
	}
	public Node insert(Person toAdd, Node start) {
		if (start == null) {
			start = new Node(toAdd);
		} else {
			Node prev = null;
			for (Node cur = start; cur != null;) {
				prev = cur;
				if (toAdd.priority < cur.L.info.priority) {
					cur = cur.L;
				} else {
					cur = cur.R;
				}
			}
			if (toAdd.priority < prev.info.priority) {
				prev.L = new Node(toAdd);
			} else {
				prev.R = new Node(toAdd);
			}
		}
		return start;
	}
	public void insert(Person toAdd) {
		root = insert(toAdd, this.root);
	}
	public Node findMin() {
		return findMin(this.root);
	}
	public Node findMax() {
		return findMax(this.root);
	}
	// private Node delete(Node cur, int priority) {
	// 	if (cur == null) {
	// 		return null;
	// 	}
	// 	if (priority < cur.info.priority) {
	// 		Node replacement = delete(cur.getLeft(), priority);
	// 		cur.L = replacement;
	// 		return cur;
	// 	}
	// 	if (priority > cur.info.priority) {
	// 		Node replacement = delete(cur.getRight(), priority);
	// 		cur.R = replacement;
	// 		return cur;
	// 	}
	// 	if (cur.getLeft() == null) {
	// 		return cur;
	// 	}
	// 	if (cur.getRight() == null) {
	// 		return cur.getLeft();
	// 	}
	// 	Node replacement = findMin(cur.getRight());
	// 	cur.info = replacement.info;
	// 	Node newChild = delete(cur.R, cur.info.priority);
	// 	cur.setRight(newChild);
	// 	return cur;
	// 	// return "";
	// }
	public Node getNode(int priority) {
		Node cur = root;
		while (cur != null && cur.info.priority != priority) {
			if (priority < cur.info.priority) {
				cur = cur.L;
			} else {
				cur = cur.R;
			}
		}
		return cur;
	}
	public Node delete(int priority, Node start) {
		paramsStack stack = new paramsStack(8);
		stack.push(new Params(0, start));
		Node result = null;
		while (stack.isEmpty() == false) {
			Params	curPars	= stack.pop();
			Node	cur		= curPars.getNode();
			switch (curPars.getContext()) {
				case 0: {
					if (cur == null) {
						result = null;
						break;
					}
					// Person p = cur.info;
					if (priority < cur.info.priority) {
						stack.push(new Params(1, cur));
						stack.push(new Params(0, cur.L));
						break;
					}
					if (priority > cur.info.priority) {
						stack.push(new Params(2, cur));
						stack.push(new Params(0, cur.R));
						break;
					}
					if (cur.L == null) {
						result = cur.R;
						break;
					}
					if (cur.R == null) {
						result = cur.L;
						break;
					}
					Node replacement = findMin(cur.R);
					cur.info = replacement.info;
					stack.push(new Params(3, cur));
					priority = cur.info.priority;
					stack.push(new Params(0, cur.R));
					break;
				}
				case 1: {
					cur.L = result;
					result = cur;
					break;
				}
				case 2: {
					cur.R = result;
					result = cur;
					break;
				}
				case 3: {
					cur.R = result;
					result = cur;
					break;
				}
			}
		}
		return result;
	}
	public void delete(int priority) {
		this.root = delete(priority, this.root);
	}
	public static void traverseNodes(StringBuilder sb, String padding, String pointer, Node node, boolean hasRightSib) {
		if (node != null) {
			sb.append("\n");
			sb.append(padding);
			sb.append(pointer);
			sb.append(node.getValue());
			StringBuilder paddingBuilder = new StringBuilder(padding);
			if (hasRightSib) {
				paddingBuilder.append("│  ");
			} else {
				paddingBuilder.append("   ");
			}
			String	paddingForBoth	= paddingBuilder.toString();
			String	pointerRight	= "└──";
			String	pointerLeft		= (node.getRight() != null) ? "├──" : "└──";
			traverseNodes(sb, paddingForBoth, pointerLeft, node.getLeft(), node.getRight() != null);
			traverseNodes(sb, paddingForBoth, pointerRight, node.getRight(), false);
		}
	}
	public static String traversePreOrder(Node root) {
		if (root == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(root.getValue());
		String	pointerRight	= "└──";
		String	pointerLeft		= (root.getRight() != null) ? "├──" : "└──";
		traverseNodes(sb, "", pointerLeft, root.getLeft(), root.getRight() != null);
		traverseNodes(sb, "", pointerRight, root.getRight(), false);
		return sb.toString();
	}
	public int getHeight(Node cur) {
		if (cur == null) {
			return 0;
		}
		int	L	= getHeight(cur.L);
		int	R	= getHeight(cur.R);
		if (L > R) {
			return L + 1;
		}
		return R + 1;
	}
	public String toString(Order order, Node start) {
		return order.getString(start);
	}
	public String toString(Order order) {
		return order.getString(root);
	}
}

public class Source {
	public static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		int test_count = sc.nextInt();
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
						if (tree.getNode(priority) == null) {
							result.append(request).append(" ").append(priority).append(": BRAK");
						}
						tree.setRoot(tree.delete(priority, tree.getRoot()));
						break;
					}
					case "ENQUE": {
						int personToAdd = sc.nextInt();
						break;
					}
					case "DEQUEMAX": {
						break;
					}
					case "DEQUEMIN": {
						break;
					}
					case "NEXT": {
						int priority = sc.nextInt();
						result.append(request).append(" ").append(priority).append(": ");
						if (tree.getNode(priority) == null) {
							result.append("BRAK");
							break;
						}
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
						if (tree.getNode(priority) == null) {
							result.append("BRAK");
							break;
						}
						Node next = tree.findPrev(priority, tree.getRoot());
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
					case "PREORDER": {
						result.append(request).append(": ");
						result.append(tree.toString(tree.new preOrder(tree)));
						break;
					}
					case "INORDER": {
						result.append(request).append(": ");
						result.append(tree.toString(tree.new inOrder(tree)));
						break;
					}
					case "POSTORDER": {
						result.append(request).append(": ");
						result.append(tree.toString(tree.new postOrder(tree)));
						break;
					}
					case "HEIGHT": {
						result.append(request).append(": ");
						result.append(Integer.toString(tree.getHeight(tree.getRoot())));
						break;
					}
				}
				if (result.length() != 0) {
					curOut.append(result.toString()).append("\n");
				}
			}
			System.out.print("ZESTAW " + testNo + "\n");
			System.out.print(curOut);
		}
	}
}
