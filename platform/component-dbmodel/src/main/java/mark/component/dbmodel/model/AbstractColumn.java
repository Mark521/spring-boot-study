package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.DatabaseObject;
import mark.component.dbmodel.constans.NamedObject;

public abstract class AbstractColumn<P extends DatabaseObject> extends AbstractDependantObject<P> {

    private static final long serialVersionUID = -8492662324895309485L;
    private ColumnDataType type;
    private int ordinalPosition;
    private int precision = 0;
    private int scale = 0;
    private boolean nullable;

    public AbstractColumn(final P parent, final String name) {
        super(parent, name);
    }
    public AbstractColumn(){

    }
    @Override
    public int compareTo(final NamedObject obj) {
        if (obj == null) {
            return -1;
        }

        final AbstractColumn<P> other = (AbstractColumn<P>) obj;
        int comparison = 0;

        if (comparison == 0) {
            comparison = ordinalPosition - other.getOrdinalPosition();
        }
        if (comparison == 0) {
            comparison = super.compareTo(other);
        }

        return comparison;
    }

    public final int getScale() {
        return scale;
    }

    public final int getOrdinalPosition() {
        return ordinalPosition;
    }

    public final int getPrecision() {
        return precision;
    }

    public final ColumnDataType getType() {
        return type;
    }

    public final String getWidth() {
        final ColumnDataType columnDataType = getType();
        if (columnDataType == null) {
            return "";
        }

        if (precision == 0 || precision == Integer.MIN_VALUE || precision == Integer.MAX_VALUE) {
            return "";
        }

        final boolean needWidth = precision != 0 || getScale() != 0;

        final StringBuilder columnWidthBuffer = new StringBuilder();
        if (needWidth) {
            columnWidthBuffer.append('(');
            columnWidthBuffer.append(precision);
            if (getScale() != 0) {
                columnWidthBuffer.append(", ").append(getScale());
            }
            columnWidthBuffer.append(')');
        }

        return columnWidthBuffer.toString();
    }

    public final boolean isNullable() {
        return nullable;
    }

    public final void setScale(final int scale) {
        this.scale = scale;
    }

    public final void setNullable(final boolean nullable) {
        this.nullable = nullable;
    }

    public final void setOrdinalPosition(final int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public final void setPrecision(final int size) {
        this.precision = size;
    }

    public void setType(final ColumnDataType type) {
        this.type = type;
    }
}
