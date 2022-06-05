import java.util.Scanner;

class Params {						// Klasa parametrow dla symulacji parametrow wywolania funkcji rekurencyjnej w funkcji iteracyjnej
	private int context;				// Zapamietane miejsce w funkcji, do ktorego wchodzimy w symulacji funkcji rekurencyjnej
	public Node node;

	public Params(int adrs, Node node) {	// Konstruktor obiektu parametrow wywolania 
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
	public int priority; // priorytet osoby 
	public String name;
	public String surname;
	Person(String name, String surname, int priority) {
		this.name = name;
		this.surname = surname;
		this.priority = priority;
	}
	// konstruktor...  
}
// koniec klasy Person

class Node { // BST nodes
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

	// private void addToList() {
	// 	result.add(info);
	// }

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
			// node.addToList();
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
		Action act; // Action performed during traversal
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
						// stack.push(new Params(2, cur));
						stack.push(new Params(0, cur.R));

						break;
					}
					// case 2: {
					// 	break;
					// }
				}
			}
			sb.setLength(Math.max(0, sb.length() - 2));	// Remove trailing comma
			return sb.toString();
		}

		public void setAction(Action action) {
			this.act = action;
		}

		public void traverse(Node cur) {
			if (cur != null) {
				// do action for current node, than traverse left and right children recursively
				act.action(cur);
				traverse(cur.getLeft());
				traverse(cur.getRight());
			}
		}
	}

	public class inOrder implements Order {
		Action act; // Action performed during traversal
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
			// throw new IllegalStateException("Undefined");
		}
		public Node Create(Index preIndex, Person data, int MIN, int MAX) {
			throw new IllegalStateException("Undefined");
			// return null;
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
					// case 2: {
					// 	break;
					// }

				}
			}
			sb.setLength(Math.max(0, sb.length() - 2));	// Remove trailing comma
			return sb.toString();
		}

		public void setAction(Action action) {
			this.act = action;
		}

		public void traverse(Node cur) {
			if (cur != null) {
				// traverse left nodes rucursively, than traverse this and traverse right
				// recursively
				traverse(cur.getLeft());
				act.action(cur);
				traverse(cur.getRight());
			}
		}
	}

	public class postOrder implements Order {
		Action act; // Action performed during traversal
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
			sb.setLength(Math.max(0, sb.length() - 2));	// Remove trailing comma
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
	// public Node findPrev(int priority) {
	// 	Node cur = findParent(priority, this.root);
	// 	if (cur == null) {
	// 		if (cur.info.priority == priority) {
	// 			return cur;
	// 		}
	// 		return null;
	// 	}
	// 	if (cur.L != null && cur.L.info.priority == priority) {
	// 		cur = cur.L;
	// 	} else if (cur.R != null && cur.R.info.priority == priority) {
	// 		cur = cur.R;
	// 	}

	// 	if (cur.L == null) {
	// 		Node tmp = findParent(cur.info.priority, this.root);
	// 		while (cur != null && cur.R != null && tmp.R.info.priority != cur.info.priority) {
	// 			node = tempNode;
	// 		}
	// 	} 
	// 	else {
	// 		Node max = cur.L;
	// 		while (max.R != null) {
	// 			max = max.R;
	// 		}
	// 		if (max.info.priority <= priority) {
	// 			return max;
	// 		}
	// 		return null;
	// 	}

	// 	return null;
	// }

	public Node findMin(Node cur) { // searching min element in branch that starts at argument
		Node prev = null;
		while (cur != null) { // Searching left children meaning traversing nodes of lower values
			prev = cur;
			cur = cur.getLeft();
		}
		return prev;
	}

	public Node findMax(Node cur) { // searching max element branch that starts at argument
		Node prev = null;
		while (cur != null) { // Searching right children meaning traversing nodes of higher values
			prev = cur;
			cur = cur.getRight();
		}
		return prev;
	}

	public Node findMin() { // Calls search from root
		return findMin(this.root);
	}

	public Node findMax() { // Calls search from root
		return findMax(this.root);
	}

	// private Node delete(Node cur, int key) { // Deleting key starting from node
	// 	if (cur == null) { // traverse tree until null
	// 		return null;
	// 	}
	// 	if (key < cur.getValue()) {
	// 		// Seaching left nodes for key
	// 		Node replacement = delete(cur.getLeft(), key); // Calling function recursively to delete from left node
	// 		cur.setLeft(replacement); // Possibly replacing left child
	// 		return cur; // Current root value remains unchanged
	// 	}

	// 	if (key > cur.getValue()) {
	// 		Node replacement = delete(cur.getRight(), key); // Calling function recursively to delete from right node
	// 		cur.setRight(replacement); // Possibly replacing right child
	// 		return cur; // Current root value remains unchanged
	// 	}

	// 	// Found node of value to be deleted
	// 	// Node with only one child or no child
	// 	if (cur.getLeft() == null) {
	// 		return cur.getRight();
	// 	}
	// 	if (cur.getRight() == null) {
	// 		return cur.getLeft();
	// 	}
	// 	Node replacement = findMin(cur.getRight()); // node with two children; get smallest to the right branch
	// 	cur.setValue(replacement.getValue());

	// 	Node newChild = delete(cur.getRight(), cur.getValue());
	// 	cur.setRight(newChild); // delete the inorder successor

	// 	return cur;
	// }

	// public Node getNode(int value) {
	// 	Node cur = root;
	// 	while (cur != null && cur.getValue() != value) {
	// 		if (value < cur.getValue()) {
	// 			cur = cur.getLeft();
	// 		} else {
	// 			cur.getRight();
	// 		}
	// 	}
	// 	return cur;
	// }

	// public Node insert(int value) {
	// 	Node newNode = new Node(value);

	// 	if (root == null) { // Insert at the top
	// 		root = newNode;
	// 		return newNode;
	// 	}
	// 	// insert after traversing tree
	// 	Node	cur		= root;
	// 	Node	prev	= null;

	// 	while (cur != null) { // find correct insertion point
	// 		prev = cur;
	// 		if (value < cur.getValue()) { // Correct insertion point is on the left
	// 			cur = cur.getLeft();
	// 		} else {
	// 			cur = cur.getRight(); // Correct insertion point if on the right
	// 		}
	// 	}

	// 	// Inserting according to BST rules
	// 	if (value < prev.getValue()) {
	// 		prev.setLeft(newNode);
	// 	} else {
	// 		prev.setRight(newNode);
	// 	}

	// 	return newNode; // Also return inserted node
	// }

	public void delete(int value) { // Delete key starting from root
		// root = delete(root, value);
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
		// Formats BST and creates tree graph
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

	public String toString(Order order, Node start) {
		// result = new StringBuilder("");
		// order.setAction(new AddToList()); // Sets action to print to array list
		// order.traverse(start); //

		// return result.toString(); // Printing array list

		return order.getString(start);
	}

	public String toString(Order order) {
		return order.getString(root);
	}

}

public class Source {
	public static Scanner sc = new Scanner(System.in); // Scanner do wczytywania danych wejsciowych

	public static void main(String[] args) {
		int test_count = sc.nextInt();					// Liczba zestawow testowych
		for (int testNo = 1; testNo < test_count + 1; testNo++) {	// Przechodzimy przez kazdy zestaw
			int				opCount	= sc.nextInt();		// Liczba roskazow
			BST				tree	= new BST();
			String			result	= "";
			StringBuilder	curOut	= new StringBuilder(""); 	 	// Tworzenie obiektu klasy StringBuilder dla sprytnego dopisywania wynikow
			while (opCount-- > 0) {
				String request = sc.next();
				switch (request) {
					case "CREATE": { // order N [x0, x1, ... , xN]
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
						// [x] Create tree
						break;
					}
					case "DELETE": { //x
						int priority = sc.nextInt();

						// [ ] deleteOfPriority
					}
					case "ENQUE": { //x 
						int personToAdd = sc.nextInt();
						// [ ] addToQueue
						break;
					}
					case "DEQUEMAX": {
						// [ ] sout(popMax)
						break;
					}
					case "DEQUEMIN": {
						// [ ] sout(popMin)
						break;
					}
					case "NEXT": {
						int lesser = sc.nextInt();
						// [x] sout(popGreater(lesser))
						break;
					}
					case "PREV": {
						int greater = sc.nextInt();
						// [ ] sout(popLesser(greater))
						break;
					}
					case "PREORDER": {
						// [x] sout(BST.toString(preorder))
						// System.out.println(tree.toString(tree.new preOrder(tree)));
						result = tree.toString(tree.new preOrder(tree));
						break;
					}
					case "INORDER": {
						// [x] sout(BST.toString(inorder))
						result = tree.toString(tree.new inOrder(tree));
						break;
					}
					case "POSTORDER": {
						// [x] sout(BST.toString(postorder))
						result = tree.toString(tree.new postOrder(tree));
						break;
					}
					case "HEIGHT": {
						// [ ] sout(tree.getHeight())
						break;
					}
				}
				if (result != "") {
					curOut.append(result).append("\n");	// Dodawanie sformatowanego wyniku do wyjscia
				}
			}

			System.out.print("ZESTAW " + testNo + "\n");
			System.out.print(curOut);
			// curOut = new StringBuilder("");
		}
		// System.out.print(output);				// Wypisywanie wyjscia
	}
}
