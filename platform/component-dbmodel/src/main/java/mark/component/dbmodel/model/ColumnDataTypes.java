package mark.component.dbmodel.model;

public class ColumnDataTypes extends NamedObjectList<ColumnDataType> {

    private static final long serialVersionUID = 6793135093651666453L;

    public ColumnDataType lookupColumnDataTypeByType(final int type) {
        final Schema systemSchema = new Schema();
        ColumnDataType columnDataType = null;
        for (final ColumnDataType currentColumnDataType : this) {
            if (type == currentColumnDataType.getJdbcType()) {
                columnDataType = currentColumnDataType;
                if (columnDataType.getSchema().equals(systemSchema)) {
                    break;
                }
            }
        }

        return columnDataType;
    }
}
