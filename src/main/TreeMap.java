public class TreeMap {
    private Node root;
    private int size;

    public int size() {
    	return this.size;
    }

    public boolean isEmpty() {
    	return this.root == null;
    }

    //TODO:
    public void put(String key, int value) {
    	this.size++;
		
		if (isEmpty()) {
			Node newNode = new Node(key, value);
			this.root = newNode;
			
			balance(root);
		}
		
		put(this.root, key, value);
	}
	
	private void put(Node current, String key, int value) {
		
		if (current.getKey().compareTo(key) > 0) {
			if (current.left == null) {
				Node newNode = new Node(key, value);
				
				current.left = newNode;
				newNode.parent = current;
				
				balance(newNode);
				
				return;
			}
			
			put(current.left, key, value);
		} else {
			if (current.right == null) {
				Node newNode = new Node(key, value);
				
				current.right = newNode;
				newNode.parent = current;
				
				balance(newNode);
				
				return;
			}
			
			put(current.right, key, value);
		}
	}
	
	private void balance(Node node) {
		fixupCase1(node);
	}

    private void fixupCase1(Node node) {
    	if (node.equals(root)) {
			root.turnBlack();
			return;
		} else
			fixupCase2(node);
		
	}

	private void fixupCase2(Node node) {
		if (node.parent.isBlack()) {
			return;
		} else
			fixupCase3(node);
		
	}

	private void fixupCase3(Node node) {
		if (node.getUncle().isRed()) {
			node.parent.turnBlack();
			node.getUncle().turnBlack();
			node.getGrandParent().turnRed();
			fixupCase1(node.getGrandParent());
			return;
		} else
			fixupCase4(node);
	}

	private void fixupCase4(Node node) {
		Node next = node;
		
		if (node.isRightChild() && node.parent.isLeftChild()) {
			leftRotation(node.parent);
			next = node.left;
		} else if (node.isLeftChild() && node.parent.isRightChild()) {
			rightRotation(node.parent);
			next = node.right;
		}
		
		fixupCase5(next);
	}
	
	

	private void fixupCase5(Node node) {
		node.parent.turnBlack();
		node.getGrandParent().turnRed();
		
		if (node.isLeftChild())
			rightRotation(node.getGrandParent());
		else
			leftRotation(node.getGrandParent());
		
		return;
	}
	
	//Ta como boolean so para eu lembrar que tem que fazer ainda
	private boolean leftRotation(Node node) {
		
	}
	
	//Ta como boolean so para eu lembrar que tem que fazer ainda
	private boolean rightRotation(Node node) {
		
	}

	//TODO
	public String remove(int key) {

    }

    public int get(String key) {
    	Node node = search(key);
    	
    	return node.getValue(); 
    }

    public boolean containsKey(String key) {
    	return search(key) != null;
    }
    
    private Node search(String key) {
		Node aux = this.root;
		
		while (aux != null) {
			if (aux.pair.getKey() == key)
				return aux;
			
			else if (aux.pair.getKey().compareTo(key) < 0)
				aux = aux.right;
			
			else
				aux = aux.left;
		}
		
		return aux;	
	}

    public boolean contains(int value) {
    	return inOrdem(this.root, value);
    }
    
    private boolean inOrdem(Node node, int value) {
    	if (node != null) {
    		
    		inOrdem(node.left, value);
    		
    		if (node.getValue() == value)
    			return true;
    		
    		inOrdem(node.right, value);
    	}
    	
    	return false;
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
}


class Node {
	enum Color {BLACK, RED};
	
	Pair pair;
	Node parent;
    Node left;
    Node right;
    protected Color color;
    

    public Node(String key, int value) {
        this.pair = new Pair(key, value);
        this.color = Color.RED;
    }
    
    public boolean isLeftChild() {
    	return this.getKey().compareTo(this.parent.getKey()) < 0;
    }
    
    public boolean isRightChild() {
    	return !isLeftChild();
    }
    
    public Node getUncle() {
    	if (this.parent.isRightChild())
    		return this.getGrandParent().left;
    	else
    		return this.getGrandParent().right;
    }
    
    public Node getGrandParent() {
    	return this.parent.parent;
    }

    public int getValue() {
    	return this.pair.getValue();
    }
    
    public String getKey() {
    	return this.pair.getKey();
    }

    boolean isLeaf() {
    	return this.left == null && this.right == null;
    }
    
    boolean isRed() {
    	return this.color == Color.RED;
    }
    
    boolean isBlack() {
    	return this.color == Color.BLACK;
    }
    
    void turnBlack() {
    	this.color = Color.BLACK;
    }
    
    void turnRed() {
    	this.color = Color.RED;
    }
}

class Pair {
	private String key;
    private int value;
    
    public Pair(String key, int value) {
    	this.key = key;
    	this.value = value;
    }
    
    public String getKey() {
    	return this.key;
    }
    
    public int getValue() {
    	return this.value;
    }
}
