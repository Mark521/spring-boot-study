package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.DatabaseObject;
import mark.component.dbmodel.constans.NamedObject;
import mark.component.dbmodel.util.Utility;

/**
 * 存放数据库信息
 */
public abstract class AbstractDatabaseObject extends AbstractNamedObject implements DatabaseObject {

    private int id;
    private Schema schema;
    private String fullName;
    private transient int hashCode;

    public AbstractDatabaseObject(final Schema schema, final String name){
        super(name);
        this.schema = schema;
    }
    public AbstractDatabaseObject(){}

    @Override
    public int compareTo(final NamedObject obj) {
        if (obj == null) {
            return -1;
        }

        if (obj instanceof DatabaseObject) {
            if(getSchema()!=null) {
                final int compareTo = getSchema().compareTo(((DatabaseObject) obj).getSchema());
                if (compareTo != 0) {
                    return compareTo;
                }
            }
        }

        return super.compareTo(obj);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        final AbstractDatabaseObject other = (AbstractDatabaseObject) obj;
        if (schema == null) {
            if (other.schema != null) {
                return false;
            }
        } else if (!schema.equals(other.schema)) {
            return false;
        }

        return true;
    }

    @Override
    public String getFullName() {
        buildFullName();
        return fullName;
    }

    @Override
    public final Schema getSchema() {
        return schema;
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
            if(schema!=null) {
                final StringBuilder buffer = new StringBuilder();
                final String schemaFullName = schema.getFullName();
                if (schema != null && !Utility.isBlank(schemaFullName)) {
                    buffer.append(schemaFullName).append('.');
                }
                final String quotedName = getName();
                if (!Utility.isBlank(quotedName)) {
                    buffer.append(quotedName);
                }
                fullName = buffer.toString();
            }else{
                fullName=getName();
            }
        }
    }

    private void buildHashCode() {
        if (hashCode == 0) {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + (schema == null ? 0 : schema.hashCode());
            result = prime * result + super.hashCode();
            hashCode = result;
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

}
