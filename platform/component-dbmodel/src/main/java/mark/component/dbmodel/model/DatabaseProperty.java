package mark.component.dbmodel.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DatabaseProperty extends Property
        implements Serializable, Comparable<DatabaseProperty> {

    private static final long serialVersionUID = -7150431683440256142L;
    private static final Set<Entry<String, String>> acronyms;

    static {
        final Map<String, String> acronymsMap = new HashMap<String, String>();
        acronymsMap.put("JDBC", "Jdbc");
        acronymsMap.put("ANSI", "Ansi");
        acronymsMap.put("SQL", "Sql");
        acronymsMap.put("URL", "Url");
        acronymsMap.put("TYPE_FORWARD_ONLY", "Type_forward_only");
        acronymsMap.put("TYPE_SCROLL_INSENSITIVE", "Type_scroll_insensitive");
        acronymsMap.put("TYPE_SCROLL_SENSITIVE", "Type_scroll_sensitive");
        acronyms = Collections.unmodifiableSet(acronymsMap.entrySet());
    }
    private transient String description;

    public DatabaseProperty(final String name, final Object value) {
        super(name, (Serializable) value);
        buildDescription();
    }

    @Override
    public int compareTo(final DatabaseProperty otherDbProperty) {
        if (otherDbProperty == null) {
            return -1;
        } else {
            return getDescription().toLowerCase().compareTo(otherDbProperty.getDescription().toLowerCase());
        }
    }

    public String getDescription() {
        buildDescription();
        return description;
    }

    @Override
    public String toString() {
        return getDescription() + " = " + getValue();
    }

    private void buildDescription() {
        if (description == null) {
            final String get = "get";
            description = getName();
            if (description.startsWith(get)) {
                description = description.substring(get.length());
            }

            for (final Entry<String, String> acronym : acronyms) {
                description = description.replaceAll(acronym.getKey(),
                        acronym.getValue());
            }

            final int strLen = description.length();
            final StringBuilder buffer = new StringBuilder(strLen);
            for (int i = 0; i < strLen; i++) {
                final char ch = description.charAt(i);
                if (Character.isUpperCase(ch) || Character.isTitleCase(ch)) {
                    buffer.append(' ').append(Character.toLowerCase(ch));
                } else {
                    buffer.append(ch);
                }
            }
            description = buffer.toString();

            for (final Entry<String, String> acronym : acronyms) {
                description = description.replaceAll(acronym.getValue().toLowerCase(),
                        acronym.getKey());
                description = description.replaceAll(acronym.getValue(),
                        acronym.getKey());
            }

            description = description.trim();
        }
    }
}
