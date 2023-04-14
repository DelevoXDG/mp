
import java.util.ArrayList;

class BST {
	class Node { // BST nodes
		private int value;
		private Node L;
		private Node R;
		private Node parent;

		Node() {
			this.value = 0;
			this.L = null;
			this.R = null;
			this.parent = null;
		}

		Node(int value) {
			this.value = value;
			this.L = null;
			this.R = null;
			this.parent = null;
		}

		private void addToList() {
			result.add(value);
		}

		public Node(int value, Node L, Node R, Node parent) {
			this.value = value;
			this.L = L;
			this.R = R;
			this.parent = parent;
		}

		public int getValue() {
			return this.value;
		}

		public void setValue(int value) {
			this.value = value;
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

		@Override
		public String toString() {
			return "{" +
					"this='" + this.getValue() + "'" +
					", L='" + (L != null ? this.L.getValue() : "null") + "'" +
					", R='" + (R != null ? this.R.getValue() : "null") + "'" +
					", parent='" + (parent != null ? this.parent.getValue() : "null") + "'" +
					"}";
		}

	}

	private Node root;
	private ArrayList<Integer> result;

	public Node getRoot() {
		return this.root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	BST() {
		this.root = null;
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
			node.addToList();
		}
	}

	private static interface Order {
		void traverse(Node cur);

		void setAction(Action action);
	}

	private static class preOrder implements Order {
		Action act; // Action performed during traversal

		preOrder() {
			this.act = new Blank();
		}

		preOrder(Action action) {
			this.act = action;
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

	private static class inOrder implements Order {
		Action act; // Action performed during traversal

		inOrder() {
			this.act = new Blank();
		}

		inOrder(Action action) {
			this.act = action;
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

	private static class postOrder implements Order {
		Action act; // Action performed during traversal

		postOrder() {
			this.act = new Blank();
		}

		postOrder(Action action) {
			this.act = action;
		}

		public void setAction(Action action) {
			this.act = action;
		}

		public void traverse(Node cur) {
			if (cur != null) {
				traverse(cur.getLeft());
				traverse(cur.getRight());
				act.action(cur);
				// perform action on this after traversing recursively both left and right nodes
			}
		}
	}

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

	private Node delete(Node cur, int key) { // Deleting key starting from node
		if (cur == null) { // traverse tree until null
			return null;
		}
		if (key < cur.getValue()) {
			// Seaching left nodes for key
			Node replacement = delete(cur.getLeft(), key); // Calling function recursively to delete from left node
			cur.setLeft(replacement); // Possibly replacing left child
			return cur; // Current root value remains unchanged
		}

		if (key > cur.getValue()) {
			Node replacement = delete(cur.getRight(), key); // Calling function recursively to delete from right node
			cur.setRight(replacement); // Possibly replacing right child
			return cur; // Current root value remains unchanged
		}

		// Found node of value to be deleted
		// Node with only one child or no child
		if (cur.getLeft() == null) {
			return cur.getRight();
		}
		if (cur.getRight() == null) {
			return cur.getLeft();
		}
		Node replacement = findMin(cur.getRight()); // node with two children; get smallest to the right branch
		cur.setValue(replacement.getValue());

		Node newChild = delete(cur.getRight(), cur.getValue());
		cur.setRight(newChild); // delete the inorder successor

		return cur;
	}

	public Node getNode(int value) {
		Node cur = root;
		while (cur != null && cur.getValue() != value) {
			if (value < cur.getValue()) {
				cur = cur.getLeft();
			} else {
				cur.getRight();
			}
		}
		return cur;
	}

	public Node insert(int value) {
		Node newNode = new Node(value);

		if (root == null) { // Insert at the top
			root = newNode;
			return newNode;
		}
		// insert after traversing tree
		Node cur = root;
		Node prev = null;

		while (cur != null) { // find correct insertion point
			prev = cur;
			if (value < cur.getValue()) { // Correct insertion point is on the left
				cur = cur.getLeft();
			} else {
				cur = cur.getRight(); // Correct insertion point if on the right
			}
		}

		// Inserting according to BST rules
		if (value < prev.getValue()) {
			prev.setLeft(newNode);
		} else {
			prev.setRight(newNode);
		}

		return newNode; // Also return inserted node
	}

	public void delete(int value) { // Delete key starting from root
		root = delete(root, value);
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

			String paddingForBoth = paddingBuilder.toString();
			String pointerRight = "└──";
			String pointerLeft = (node.getRight() != null) ? "├──" : "└──";

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

		String pointerRight = "└──";
		String pointerLeft = (root.getRight() != null) ? "├──" : "└──";

		traverseNodes(sb, "", pointerLeft, root.getLeft(), root.getRight() != null);
		traverseNodes(sb, "", pointerRight, root.getRight(), false);

		return sb.toString();
	}

	public synchronized String toString(Order order, Node start) {
		// Build an array list of values in BST based on Order given as argumentk
		result = new ArrayList<Integer>();
		order.setAction(new AddToList()); // Sets action to print to array list
		order.traverse(start); //

		return result.toString(); // Printing array list
	}

	public synchronized String toString(Order order) {
		return toString(order, root); // Printing BST starting from root
	}

	public static void main(String[] args) {
		BST tree = new BST();
		tree.insert(2);
		tree.insert(5);
		tree.insert(8);
		tree.insert(6);
		tree.insert(1);
		tree.insert(9);
		System.out.println(tree.toString(new preOrder()));
		System.out.println(tree.toString(new inOrder()));
		System.out.println(tree.toString(new postOrder()));
		System.out.println(tree.findMax());
		System.out.println(tree.findMin());
		System.out.println(traversePreOrder(tree.getRoot()));
		tree.delete(5);
		tree.delete(3);
		System.out.println(traversePreOrder(tree.getRoot()));
		System.out.println(tree.getNode(1));

		// System.out.println();

	}
}
