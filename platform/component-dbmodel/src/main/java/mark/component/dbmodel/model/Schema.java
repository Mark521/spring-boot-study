package mark.component.dbmodel.model;

import mark.component.dbmodel.util.Utility;
import mark.component.dbmodel.constans.NamedObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Schema implements NamedObject {

    private static final long serialVersionUID = -5309848447599233878L;
    private final String catalogName;
    private final String schemaName;
    private transient String fullName;
    private final Map<String, Object> attributeMap = new HashMap<String, Object>();

    public Schema() {
        this(null, null);
    }

    public Schema(final String catalogName, final String schemaName) {
        this.catalogName = catalogName;
        this.schemaName = schemaName;
    }

    public int compareTo(final NamedObject otherSchemaRef) {
        if (otherSchemaRef == null) {
            return -1;
        } else {
            return getFullName().replaceAll("\"", "").compareTo(otherSchemaRef.getFullName().replaceAll("\"", ""));
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Schema other = (Schema) obj;
        if (attributeMap == null) {
            if (other.attributeMap != null) {
                return false;
            }
        } else if (!attributeMap.equals(other.attributeMap)) {
            return false;
        }
        if (catalogName == null) {
            if (other.catalogName != null) {
                return false;
            }
        } else if (!catalogName.equals(other.catalogName)) {
            return false;
        }
        if (schemaName == null) {
            if (other.schemaName != null) {
                return false;
            }
        } else if (!schemaName.equals(other.schemaName)) {
            return false;
        }
        return true;
    }

    public final Object getAttribute(final String name) {
        return attributeMap.get(name);
    }

    public final <T> T getAttribute(final String name, final T defaultValue) {
        final Object attributeValue = getAttribute(name);
        if (attributeValue == null) {
            return defaultValue;
        } else {
            try {
                return (T) attributeValue;
            } catch (final ClassCastException e) {
                return defaultValue;
            }
        }
    }

    public final Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributeMap);
    }

    public String getCatalogName() {
        return catalogName;
    }

    public String getFullName() {
        buildFullName();
        return fullName;
    }

    public String getLookupKey() {
        return getFullName();
    }

    public String getName() {
        return schemaName;
    }

    public String getRemarks() {
        return "";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + (attributeMap == null ? 0 : attributeMap.hashCode());
        result = prime * result + (catalogName == null ? 0 : catalogName.hashCode());
        result = prime * result + (schemaName == null ? 0 : schemaName.hashCode());
        return result;
    }

    public final void setAttribute(final String name, final Object value) {
        if (!Utility.isBlank(name)) {
            if (value == null) {
                attributeMap.remove(name);
            } else {
                attributeMap.put(name, value);
            }
        }
    }

    @Override
    public String toString() {
        return getFullName();
    }

    private void buildFullName() {
        if (fullName == null) {
            final boolean hasCatalogName = !Utility.isBlank(catalogName);
            final boolean hasSchemaName = !Utility.isBlank(getName());
            fullName = (hasCatalogName ? catalogName : "")
                    + (hasCatalogName && hasSchemaName ? "." : "")
                    + (hasSchemaName ? getName() : "");
        }
    }
}
