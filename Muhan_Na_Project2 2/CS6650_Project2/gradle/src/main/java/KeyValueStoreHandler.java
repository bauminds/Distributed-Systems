import java.util.HashMap;

public class KeyValueStoreHandler implements KeyValueStore.Iface {
    private HashMap<Integer, Integer> map;
    public KeyValueStoreHandler() {
        map = new HashMap<Integer, Integer>();
    }

    public void put(int key, int value) {
        map.put(key, value);
    }

    public String get(int key) {
        if (map.containsKey(key)) return String.valueOf(map.get(key));
        return "";
    }

    public boolean deleteVal(int key) {
        if (map.containsKey(key)) {
            map.remove(key);
            return true;
        }
        return false;
    }
}
