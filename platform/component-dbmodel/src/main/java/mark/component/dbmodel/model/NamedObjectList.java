package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.NamedObject;
import mark.component.dbmodel.util.ObjectToString;
import mark.component.dbmodel.util.Utility;

import java.io.Serializable;
import java.util.*;

public class NamedObjectList<N extends NamedObject> implements Serializable, Iterable<N> {

    private static final long serialVersionUID = 3257847666804142128L;

    private static String makeLookupKey(final NamedObject namedObject) {
        final String key;
        if (namedObject == null) {
            key = null;
        } else {
            key = namedObject.getLookupKey();
        }
        return key;
    }

    private static String makeLookupKey(final NamedObject namedObject, final String name) {
        final StringBuilder buffer = new StringBuilder(256);

        final String key;
        final String namedObjectLookupKey = makeLookupKey(namedObject);

        if (namedObjectLookupKey != null) {
            buffer.append(namedObjectLookupKey);
        }
        if (buffer.length() > 0) {
            buffer.append('.');
        }
        buffer.append(name);

        key = buffer.toString();
        return key;
    }

    private static String makeLookupKey(final String fullName) {
        final String key;
        if (Utility.isBlank(fullName)) {
            key = null;
        } else {
            key = fullName;
        }
        
        return key;
    }
    private final Map<String, N> objects = new HashMap<String, N>();

    @Override
    public Iterator<N> iterator() {
        return values().iterator();
    }

    @Override
    public String toString() {
        return ObjectToString.toString(values());
    }

    public void add(final N namedObject) {
        if (namedObject == null) {
            throw new IllegalArgumentException("Cannot add a null object to the list");
        }
        final String key = makeLookupKey(namedObject);
        objects.put(key, namedObject);
    }

    public N lookup(final NamedObject namedObject, final String name) {
        final String key = makeLookupKey(namedObject, name);
        return objects.get(key);
    }

    public N lookup(final String fullName) {
        final String key = makeLookupKey(fullName);
        return objects.get(key);
    }

    public void remove(final NamedObject namedObject) {
        if (namedObject != null) {
            objects.remove(makeLookupKey(namedObject));
        }
    }

    public int size() {
        return objects.size();
    }
    
    public List<N> values() {
        final List<N> all = new ArrayList<N>(objects.values());
        Collections.sort(all);
        return all;
    }
}
