import java.util.TreeMap;

/**
 * Interface para diferentes implementações de TreeMap
 * Permite comparar PV, AVL e Red-Black trees de forma uniforme
 */
public interface TreeMapImplementation<K, V> {
    void put(K key, V value);
    V get(K key);
    V remove(K key);
    String getName();
    void clear();
}

/**
 * Wrapper para TreeMap padrão do Java (Red-Black Tree)
 */
class StandardTreeMap<K, V> implements TreeMapImplementation<K, V> {
    private final TreeMap<K, V> map = new TreeMap<>();
    
    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }
    
    @Override
    public V get(K key) {
        return map.get(key);
    }
    
    @Override
    public V remove(K key) {
        return map.remove(key);
    }
    
    @Override
    public String getName() {
        return "Red-Black Tree (Java TreeMap)";
    }
    
    @Override
    public void clear() {
        map.clear();
    }
}

/**
 * Wrapper para implementações personalizadas
 */
class PVTreeMapWrapper<K extends Comparable<K>, V> implements TreeMapImplementation<K, V> {
    // private final TreeMap<K, V> map = new TreeMap<>();
    
    @Override
    public void put(K key, V value) {
        // TODO
        // map.put(key, value);
        throw new UnsupportedOperationException("Implemente usando nossa classe TreeMap");
    }
    
    @Override
    public V get(K key) {
        // TODO
        // return map.get(key);
        throw new UnsupportedOperationException("Implemente usando nossa classe TreeMap");
    }
    
    @Override
    public V remove(K key) {
        // TODO
        // return map.remove(key);
        throw new UnsupportedOperationException("Implemente usando nossa classe TreeMap");
    }
    
    @Override
    public String getName() {
        // TODO
        return "PV Tree (Red-Black)";
    }
    
    @Override
    public void clear() {
        // TODO
        // map.clear();
        throw new UnsupportedOperationException("Implemente usando nossa classe TreeMap");
    }
}

class AVLTreeMapWrapper<K extends Comparable<K>, V> implements TreeMapImplementation<K, V> {
    // private final TreeMapAVL<K, V> map = new TreeMapAVL<>();
    
    @Override
    public void put(K key, V value) {
        // TODO
        // map.put(key, value);
        throw new UnsupportedOperationException("Implemente usando nossa classe TreeMapAVL");
    }
    
    @Override
    public V get(K key) {
        // TODO
        // return map.get(key);
        throw new UnsupportedOperationException("Implemente usando nossa classe TreeMapAVL");
    }
    
    @Override
    public V remove(K key) {
        // TODO
        // return map.remove(key);
        throw new UnsupportedOperationException("Implemente usando nossa classe TreeMapAVL");
    }
    
    @Override
    public String getName() {
        // TODO
        return "AVL Tree";
    }
    
    @Override
    public void clear() {
        // TODO
        // map.clear();
        throw new UnsupportedOperationException("Implemente usando nossa classe TreeMapAVL");
    }
}
