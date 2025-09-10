import java.util.Objects;

public class TreeMapAVL {
    private NodeAVL root;
    private int size;

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public String put(int key, String value) {
        NodeAVL newNode = new NodeAVL(key, value);

        if (this.isEmpty()) {
            this.root = newNode;
            this.size++;
            return null;
        }

        return this.put(this.root, newNode);
    }

    private String put(NodeAVL current, NodeAVL newNode) {
        if (current.equals(newNode)) {
            String aux = current.getValue();
            current.setValue(newNode.getValue());
            return aux;
        } else if (newNode.getKey() < current.getKey()) {
            if (current.left == null) {
                current.left = newNode;
                this.size++;
                return null;
            }
            return this.put(current.left, newNode);
        } else {
            if (current.right == null) {
                current.right = newNode;
                this.size++;
                return null;
            }
            return this.put(current.right, newNode);
        }
    }

    //TODO:
    public String remove(int key) {
        return null;
    }

    public String get(int key) {
        if (this.isEmpty())
            return null;

        NodeAVL found = this.search(this.root, key);

        if (found != null)
            return found.getValue();

        return null;
    }

    private NodeAVL search(NodeAVL current, int key) {
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

    public boolean containsValue(String value) {
        return this.containsValue(this.root, value);
    }

    private boolean containsValue(NodeAVL node, String value) {
        if (node == null)
            return false;

        if (node.getValue().equals(value))
            return true;

        return this.containsValue(node.left, value) || this.containsValue(node.right, value);
    }

    public boolean containsKey(int key) {
        return this.get(key) != null;
    }

    public int height() {
    	return height(this.root);
    }

    private int height(NodeAVL node) {
        if (node == null)
			return -1;
		else
			return 1 + Math.max(height(node.left), height(node.right));
    }

    private int balance(NodeAVL node) {
        return this.height(node.left) - this.height(node.right);
    }
 
	/**
	 * Método que rotaciona determinado node para a esquerda
	 * 
	 * @param node o node que será rotacionado
	 */
	private void leftRotation(NodeAVL node) {
        if (node == this.root)
            this.root = node.right;

        node.right.parent = node.parent;
        node.parent = node.right;
        node.right = node.right.left;
        node.parent.left = node;
        node.right.parent = node;
    }

	/**
	 * Método que rotaciona determinado node para a direita
	 * 
	 * @param node o node que sera rotacionado
	 */
	private void rightRotation(NodeAVL node) {
        if (node == this.root)
            this.root = node.left;

        node.left.parent = node.parent;
        node.parent = node.left;
        node.left = node.left.right;
        node.parent.right = node;
        node.left.parent = node;
    }
}

class NodeAVL {
    PairAVL pair;
    NodeAVL parent;
    NodeAVL left;
    NodeAVL right;

    NodeAVL(int key, String value) {
        this.pair = new PairAVL(key, value);
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
		NodeAVL other = (NodeAVL) obj;
		return Objects.equals(pair, other.pair);
	}
}

class PairAVL {
    int key;
    String value;

    PairAVL(int key, String value) {
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
		PairAVL other = (PairAVL) obj;
		return Objects.equals(key, other.key);
	}
}
