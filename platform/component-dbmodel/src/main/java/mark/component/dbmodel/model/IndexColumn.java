package mark.component.dbmodel.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import mark.component.dbmodel.constans.IndexColumnSortSequence;
import mark.component.dbmodel.constans.NamedObject;

import java.util.Collection;

public final class IndexColumn extends AbstractDependantObject<Table> {

    private static final long serialVersionUID = -6923211341742623556L;
    @JsonBackReference
    private  Column column;
    @JsonBackReference
    private  Index index;
    private int indexOrdinalPosition;
    private IndexColumnSortSequence sortSequence;

    public IndexColumn(final Index index, final Column column) {
        super(column.getParent(), column.getName());
        this.index = index;
        this.column = column;
    }
    public IndexColumn(){

    }

    public Column getColumn() {
        return this.column;
    }

    @Override
    public int compareTo(final NamedObject obj) {
        if (obj == null) {
            return -1;
        }

        final IndexColumn other = (IndexColumn) obj;
        int comparison = 0;

        if (comparison == 0) {
            comparison = indexOrdinalPosition - other.indexOrdinalPosition;
        }
        if (comparison == 0) {
            comparison = super.compareTo(other);
        }

        return comparison;
    }

    public int getDecimalDigits() {
        return column.getScale();
    }

    public String getDefaultValue() {
        return column.getDefaultValue();
    }

    public Index getIndex() {
        return index;
    }

    public int getIndexOrdinalPosition() {
        return indexOrdinalPosition;
    }

    public int getOrdinalPosition() {
        return column.getOrdinalPosition();
    }

    public Privilege<Column> getPrivilege(final String name) {
        return column.getPrivilege(name);
    }

    public Collection<Privilege<Column>> getPrivileges() {
        return column.getPrivileges();
    }

    public Column getReferencedColumn() {
        return column.getReferencedColumn();
    }

    public int getSize() {
        return column.getPrecision();
    }

    public IndexColumnSortSequence getSortSequence() {
        return sortSequence;
    }

    public ColumnDataType getType() {
        return column.getType();
    }

    public String getWidth() {
        return column.getWidth();
    }

    public boolean isNullable() {
        return column.isNullable();
    }

    public boolean isPartOfForeignKey() {
        return column.isPartOfForeignKey();
    }

    public boolean isPartOfPrimaryKey() {
        return column.isPartOfPrimaryKey();
    }

    public boolean isPartOfUniqueIndex() {
        return column.isPartOfUniqueIndex();
    }

    public void setIndexOrdinalPosition(final int indexOrdinalPosition) {
        this.indexOrdinalPosition = indexOrdinalPosition;
    }

    public void setSortSequence(final IndexColumnSortSequence sortSequence) {
        this.sortSequence = sortSequence;
    }
}
