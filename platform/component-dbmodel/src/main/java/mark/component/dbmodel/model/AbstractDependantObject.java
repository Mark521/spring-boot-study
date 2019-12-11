package mark.component.dbmodel.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mark.component.dbmodel.constans.DatabaseObject;
import mark.component.dbmodel.util.Utility;

@JsonIgnoreProperties(
        value = {"parent"}
)
public abstract class AbstractDependantObject<P extends DatabaseObject>
        extends AbstractDatabaseObject {

    private static final long serialVersionUID = -4327208866052082457L;
    private P parent;
    private transient String fullName;
    private transient String shortName;
    private transient int hashCode;

    public AbstractDependantObject(final P parent, final String name) {
        super((parent==null?null:parent.getSchema()), name);
        this.parent = parent;
    }
    public AbstractDependantObject(){

    }
    @Override
    public boolean equals(final Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        final AbstractDependantObject<P> other = (AbstractDependantObject<P>) obj;
        if (parent == null) {
            if (other.getParent() != null) {
                return false;
            }
        } else if (!parent.equals(other.getParent())) {
            return false;
        }

        return true;
    }

    @Override
    public String getFullName() {
        buildFullName();
        return fullName;
    }
    @JSONField(serialize = false)
    public final P getParent() {
        return parent;
    }
    public void setParent(P p) {
        this.parent=p;
    }

    public final String getShortName() {
        buildShortName();
        return shortName;
    }

    @Override
    public int hashCode() {
        buildHashCode();
        return hashCode;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    private void buildFullName() {
        if (fullName == null) {
            final StringBuilder buffer = new StringBuilder();
            if(parent != null) {
                final String parentFullName = parent.getFullName();
                if (!Utility.isBlank(parentFullName)) {
                    buffer.append(parentFullName).append('.');
                }
            }
            final String quotedName = getName();
            if (!Utility.isBlank(quotedName)) {
                buffer.append(quotedName);
            }
            fullName = buffer.toString();
        }
    }

    private void buildHashCode() {
        if (hashCode == 0) {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + (parent == null ? 0 : parent.hashCode());
            result = prime * result + super.hashCode();
            hashCode = result;
        }
    }

    private void buildShortName() {
        if (shortName == null) {
            final StringBuilder buffer = new StringBuilder();
            if(parent!=null) {
                final String parentName = parent.getName();
                if ( !Utility.isBlank(parentName)) {
                    buffer.append(parentName).append('.');
                }
            }
            final String quotedName = getName();
            if (!Utility.isBlank(quotedName)) {
                buffer.append(quotedName);
            }
            shortName = buffer.toString();
        }
    }
}
