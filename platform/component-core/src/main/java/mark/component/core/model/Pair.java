package mark.component.core.model;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class Pair<K, V> implements Comparable<Pair<K, V>>, Serializable {

    private static final long serialVersionUID = 1L;

    private K key;
    private V value;

    public Pair() {

    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Pair<?, ?> newPair = (Pair<?, ?>) obj;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(key, newPair.key);
        builder.append(value, newPair.value);
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(key);
        builder.append(value);
        return builder.toHashCode();
    }

    @Override
    public int compareTo(Pair<K, V> o) {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(key, o.key);
        builder.append(value, o.value);
        return builder.toComparison();
    }

    @Override
    public String toString() {
        return key + ":" + value;
    }

}
