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

    public void clear() {
        this.root = null;
        this.size = 0;
    }

    public String put(int key, String value) {
        if (this.isEmpty()) {
            this.root = new NodeAVL(key, value);
            this.size++;
            return null;
        }
        
        String[] result = new String[1];
        this.root = put(this.root, key, value, result);
        if (result[0] == null) {
            this.size++;
        }
        return result[0];
    }

    private NodeAVL put(NodeAVL node, int key, String value, String[] oldValue) {
        if (node == null) {
            return new NodeAVL(key, value);
        }

        if (key == node.getKey()) {
            oldValue[0] = node.getValue();
            node.setValue(value);
            return node;
        } else if (key < node.getKey()) {
            node.left = put(node.left, key, value, oldValue);
            if (node.left != null) node.left.parent = node;
        } else {
            node.right = put(node.right, key, value, oldValue);
            if (node.right != null) node.right.parent = node;
        }

        return rebalance(node);
    }

    public String remove(int key) {
        if (this.isEmpty()) {
            return null;
        }
        
        String[] result = new String[1];
        this.root = remove(this.root, key, result);
        if (result[0] != null) {
            this.size--;
        }
        return result[0];
    }

    private NodeAVL remove(NodeAVL node, int key, String[] removedValue) {
        if (node == null) {
            return null;
        }

        if (key < node.getKey()) {
            node.left = remove(node.left, key, removedValue);
            if (node.left != null) node.left.parent = node;
        } else if (key > node.getKey()) {
            node.right = remove(node.right, key, removedValue);
            if (node.right != null) node.right.parent = node;
        } else {
            removedValue[0] = node.getValue();
            
            if (node.left == null) {
                NodeAVL rightChild = node.right;
                if (rightChild != null) rightChild.parent = null;
                return rightChild;
            } else if (node.right == null) {
                NodeAVL leftChild = node.left;
                if (leftChild != null) leftChild.parent = null;
                return leftChild;
            } else {
                NodeAVL successor = findMin(node.right);
                node.pair.key = successor.getKey();
                node.pair.value = successor.getValue();
                node.right = remove(node.right, successor.getKey(), new String[1]);
                if (node.right != null) node.right.parent = node;
            }
        }

        return rebalance(node);
    }

    private NodeAVL rebalance(NodeAVL node) {
        if (node == null) return null;
        
        updateHeight(node);
        
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = leftRotation(node.left);
            }
            node = rightRotation(node);
        }
        else if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rightRotation(node.right);
            }
            node = leftRotation(node);
        }

        return node;
    }

    private NodeAVL leftRotation(NodeAVL x) {
        if (x == null || x.right == null) return x;
        
        NodeAVL y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.left = x;
        
        y.parent = x.parent;
        x.parent = y;
        
        updateHeight(x);
        updateHeight(y);
        
        return y;
    }

    private NodeAVL rightRotation(NodeAVL x) {
        if (x == null || x.left == null) return x;
        
        NodeAVL y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.right = x;
        
        y.parent = x.parent;
        x.parent = y;
        
        updateHeight(x);
        updateHeight(y);
        
        return y;
    }

    private void updateHeight(NodeAVL node) {
        if (node != null) {
            int leftHeight = (node.left != null) ? node.left.height : -1;
            int rightHeight = (node.right != null) ? node.right.height : -1;
            node.height = 1 + Math.max(leftHeight, rightHeight);
        }
    }

    private int getBalance(NodeAVL node) {
        if (node == null) return 0;
        int leftHeight = (node.left != null) ? node.left.height : -1;
        int rightHeight = (node.right != null) ? node.right.height : -1;
        return leftHeight - rightHeight;
    }

    private NodeAVL findMin(NodeAVL node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public String get(int key) {
        if (this.isEmpty())
            return null;

        NodeAVL found = this.search(this.root, key);
        return found != null ? found.getValue() : null;
    }

    private NodeAVL search(NodeAVL current, int key) {
        if (current == null || current.getKey() == key) {
            return current;
        }
        
        if (key < current.getKey()) {
            return this.search(current.left, key);
        } else {
            return this.search(current.right, key);
        }
    }

    public boolean containsValue(String value) {
        return this.containsValue(this.root, value);
    }

    private boolean containsValue(NodeAVL node, String value) {
        if (node == null)
            return false;

        if (Objects.equals(node.getValue(), value))
            return true;

        return this.containsValue(node.left, value) || this.containsValue(node.right, value);
    }

    public boolean containsKey(int key) {
        return this.get(key) != null;
    }

    public int height() {
        return this.root != null ? this.root.height : -1;
    }
}

class NodeAVL {
    PairAVL pair;
    NodeAVL parent;
    NodeAVL left;
    NodeAVL right;
    int height;

    NodeAVL(int key, String value) {
        this.pair = new PairAVL(key, value);
        this.height = 0;
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

    boolean hasTwoChilds() {
        return this.left != null && this.right != null;
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
