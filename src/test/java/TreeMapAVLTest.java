import static org.junit.Assert.*;
import org.junit.Test;

public class TreeMapAVLTest {

    @Test
    public void testPutAndGet() {
        TreeMapAVL map = new TreeMapAVL();
        assertNull(map.put(1, "one"));
        assertEquals("one", map.get(1));
        assertEquals("one", map.put(1, "new"));
        assertEquals("new", map.get(1));
    }

    @Test
    public void testSize() {
        TreeMapAVL map = new TreeMapAVL();
        assertEquals(0, map.size());
        map.put(1, "one");
        assertEquals(1, map.size());
        map.put(2, "two");
        assertEquals(2, map.size());
    }

    @Test
    public void testIsEmpty() {
        TreeMapAVL map = new TreeMapAVL();
        assertTrue(map.isEmpty());
        map.put(1, "one");
        assertFalse(map.isEmpty());
    }

    @Test
    public void testClear() {
        TreeMapAVL map = new TreeMapAVL();
        map.put(1, "one");
        map.put(2, "two");
        map.clear();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void testRemove() {
        TreeMapAVL map = new TreeMapAVL();
        map.put(1, "one");
        map.put(2, "two");
        assertEquals("one", map.remove(1));
        assertNull(map.get(1));
        assertEquals(1, map.size());
    }

    @Test
    public void testContainsKey() {
        TreeMapAVL map = new TreeMapAVL();
        map.put(1, "one");
        assertTrue(map.containsKey(1));
        assertFalse(map.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        TreeMapAVL map = new TreeMapAVL();
        map.put(1, "one");
        assertTrue(map.containsValue("one"));
        assertFalse(map.containsValue("two"));
    }

    @Test
    public void testHeight() {
        TreeMapAVL map = new TreeMapAVL();
        assertEquals(-1, map.height());
        map.put(1, "one");
        assertEquals(0, map.height());
        map.put(2, "two");
        assertEquals(1, map.height());
    }
}
