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
class PVTreeMapWrapper implements TreeMapImplementation<Integer, String> {
    private final TreeMapPV map = new TreeMapPV();
    
    @Override
    public void put(Integer key, String value) {
        map.put(key, value);
    }
    
    @Override
    public String get(Integer key) {
        return map.get(key);
    }
    
    @Override
    public String remove(Integer key) {
        return map.remove(key);
    }
    
    @Override
    public String getName() {
        return "PV Tree (Red-Black)";
    }
    
    @Override
    public void clear() {
        map.clear();
    }
}

class AVLTreeMapWrapper implements TreeMapImplementation<Integer, String> {
    private final TreeMapAVL map = new TreeMapAVL();
    
    @Override
    public void put(Integer key, String value) {
        map.put(key, value);
    }
    
    @Override
    public String get(Integer key) {
        return map.get(key);
    }
    
    @Override
    public String remove(Integer key) {
        return map.remove(key);
    }
    
    @Override
    public String getName() {
        return "AVL Tree";
    }
    
    @Override
    public void clear() {
        map.clear();
    }
}
