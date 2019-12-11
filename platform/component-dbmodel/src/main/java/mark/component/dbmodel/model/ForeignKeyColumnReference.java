package mark.component.dbmodel.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(
        value = {"foreignKeyColumn","primaryKeyColumn"}
)
public final class ForeignKeyColumnReference 
    implements Serializable, Comparable<ForeignKeyColumnReference> {

    private static final long serialVersionUID = 3689073962672273464L;
    private Column foreignKeyColumn;
    private Column primaryKeyColumn;
    private int keySequence;

    @Override
    public int compareTo(final ForeignKeyColumnReference other) {
        if (other == null) {
            return -1;
        }

        int comparison = 0;

        if (comparison == 0) {
            comparison = getKeySequence() - other.getKeySequence();
        }
        // Note: For the primary key and foreign key columns, compare by
        // name.
        if (comparison == 0) {
            comparison = getPrimaryKeyColumn().getFullName().compareTo(other.getPrimaryKeyColumn().getFullName());
        }
        if (comparison == 0) {
            comparison = getForeignKeyColumn().getFullName().compareTo(other.getForeignKeyColumn().getFullName());
        }

        return comparison;
    }

    @JSONField(serialize = false)
    public Column getForeignKeyColumn() {
        return foreignKeyColumn;
    }

    public int getKeySequence() {
        return keySequence;
    }
    @JSONField(serialize = false)
    public Column getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    public void setForeignKeyColumn(final Column foreignKeyColumn) {
        this.foreignKeyColumn = foreignKeyColumn;
    }

    public void setKeySequence(final int keySequence) {
        this.keySequence = keySequence;
    }

    public void setPrimaryKeyColumn(final Column primaryKeyColumn) {
        this.primaryKeyColumn = primaryKeyColumn;
    }
}
