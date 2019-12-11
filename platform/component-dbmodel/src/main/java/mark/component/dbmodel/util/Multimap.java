package mark.component.dbmodel.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Multimap<K, V> extends HashMap<K, List<V>> {

    private static final long serialVersionUID = 1470713639458689002L;

    public V add(final K key, final V value) {
        List<V> values = null;
        if (containsKey(key)) {
            values = get(key);
        }
        if (values == null) {
            values = new ArrayList<V>();
        }
        put(key, values);

        values.add(value);
        return value;
    }
}
