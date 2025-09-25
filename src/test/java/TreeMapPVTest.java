import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TreeMapPVTest {
    private TreeMapPV tree;

    @Before
    public void setUp() {
        tree = new TreeMapPV();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
    }

    @Test
    public void testPutAndSize() {
        tree.put(1, "one");
        assertFalse(tree.isEmpty());
        assertEquals(1, tree.size());
        tree.put(2, "two");
        assertEquals(2, tree.size());
    }

    @Test
    public void testPutDuplicateKey() {
        tree.put(1, "one");
        tree.put(1, "uno");
        assertEquals(1, tree.size());
        assertEquals("uno", tree.get(1));
    }

    @Test
    public void testGet() {
        tree.put(1, "one");
        tree.put(2, "two");
        assertEquals("one", tree.get(1));
        assertEquals("two", tree.get(2));
    }

    @Test
    public void testContainsKey() {
        tree.put(1, "one");
        assertTrue(tree.containsKey(1));
        assertFalse(tree.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        tree.put(1, "one");
        tree.put(2, "two");
        assertTrue(tree.containsValue("one"));
        assertTrue(tree.containsValue("two"));
        assertFalse(tree.containsValue("three"));
    }

    @Test
    public void testReplace() {
        tree.put(1, "one");
        tree.replace(1, "uno");
        assertEquals("uno", tree.get(1));
    }

    @Test
    public void testRemove() {
        tree.put(1, "one");
        tree.put(2, "two");
        String removed = tree.remove(1);
        assertEquals("one", removed);
        assertEquals(1, tree.size());
        assertFalse(tree.containsKey(1));
    }

    @Test
    public void testRemoveNonExistent() {
        String removed = tree.remove(1);
        assertNull(removed);
    }

    @Test
    public void testClear() {
        tree.put(1, "one");
        tree.put(2, "two");
        tree.clear();
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
    }

    @Test
    public void testHeight() {
        assertEquals(-1, tree.height());
        tree.put(2, "two");
        assertEquals(0, tree.height());
        tree.put(1, "one");
        tree.put(3, "three");
        assertTrue(tree.height() >= 1); // Depends on balancing
    }

    @Test
    public void testToString() {
        tree.put(2, "two");
        tree.put(1, "one");
        tree.put(3, "three");
        String str = tree.toString();
        assertTrue(str.contains("one"));
        assertTrue(str.contains("two"));
        assertTrue(str.contains("three"));
    }
}
