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
    public void put(int key, String value) {
    }

    //TODO:
    public String remove(int key) {
    }

    //TODO:
    public String get(int key) {
    }

    //TODO:
    public boolean contains(String value) {
    }

    //TODO:
    public boolean containsKey(int key) {
    }

    //TODO:
    private int height() {
    }
}

class Node {
    Pair pair;
    Node parent;
    Node left;
    Node right;
    boolean red;

    Node(int key, String value) {
        this(key, value, true);
    }

    Node(int key, String value, boolean color) {
        this.pair = new Pair(key, value);
        this.red = color;
    }

    String getValue() {
        return this.pair.value;
    }

    boolean isLeaf() {
        return this.left == null && this.right == null;
    }
}

class Pair {
    int key;
    String value;

    Pair(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
