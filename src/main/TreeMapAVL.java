import java.util.Objects;

public class TreeMap {
    private Node root;
    private int size;

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public String put(int key, String value) {
        Node newNode = new Node(key value);

        if (this.isEmpty()) {
            this.root = newNode;
            this.size++;
            return null
        }

        return this.put(this.root, newNode);
    }

    private String put(Node current, Node newNode) {
        if (current.equals(newNode)) {
            String aux = current.getValue();
            current.setValue(newNode.value);
            return aux;
        } else if (newNode.getKey() < current.getKey()) {
            if (current.left == null) {
                current.left = newNode;
                this.size++;
                return null;
            }
            return this.put(current.left, newNode)
        } else {
            if (current.right == null) {
                current.right = newNode;
                this.size++;
                return null;
            }
            return this.put(current.right, newNode)
        }
    }

    //TODO:
    public String remove(int key) {
    }

    public String get(int key) {
        if (this.isEmpty())
            return null;

        Node found = this.search(this.root, key);

        if (found != null)
            return found.getValue();

        return null;
    }

    private Node search(Node current, int key) {
        if (current.getKey() == key)
            return current;
        
        if (key < current.getKey()) {
            if (current.left == null)
                return null;
            return this.search(current.left, key);
        } else {
            if (current.right == null)
                return null;
            return this.search(current.right, key);
        }
    }

    //TODO:
    public boolean contains(String value) {
    }

    //TODO:
    public boolean containsKey(int key) {
    }

    public int height() {
    	return height(this.root);
    }

    private int height(Node node) {
        if (node == null)
			return -1;
		else
			return 1 + Math.max(height(node.left), height(node.right));
    }

    //TODO:
    private int balance(Node node) {
    }
 
	/**
	 * Método que rotaciona determinado node para a esquerda
	 * 
	 * @param node o node que será rotacionado
	 */
	private void leftRotation(Node node) {
		if (node.equals(root))
			this.root = node.right;
		
		node.right.parent = node.parent;
		
		if (node.parent != null) {
			if (node.isRightChild())
				node.parent.right = node.right;
			else
				node.parent.left = node.right;
		}

		node.parent = node.right;
		
		node.right = node.parent.left;
		
		if (node.parent.left != null)
			node.parent.left.parent = node;
		
		node.parent.left = node;
	}
	
	/**
	 * Método que rotaciona determinado node para a direita
	 * 
	 * @param node o node que sera rotacionado
	 */
	private void rightRotation(Node node) {
		if (node.equals(root))
			this.root = node.left;
		
		node.left.parent = node.parent;
		
		if (node.parent != null) {
			if (node.isRightChild())
				node.parent.right = node.left;
			else
				node.parent.left = node.left;
		}
		
		node.parent = node.left;
		
		node.left = node.parent.right;
		
		if (node.parent.right != null)
			node.parent.right.parent = node;
		
		node.parent.right = node;
   }
}

class Node {
    Pair pair;
    Node parent;
    Node left;
    Node right;

    Node(int key, String value) {
        this.pair = new Pair(key, value);
        this.red = color;
    }

    int getKey() {
        return this.pair.key;
    }

    String getValue() {
        return this.pair.value;
    }

    void setValue(String value) {
        this.pair.value = value;
    }

    boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    @Override
	public int hashCode() {
		return Objects.hash(pair);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return Objects.equals(pair, other.pair);
	}
}

class Pair {
    int key;
    String value;

    Pair(int key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
	public int hashCode() {
		return Objects.hash(key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		return Objects.equals(key, other.key);
	}
}
