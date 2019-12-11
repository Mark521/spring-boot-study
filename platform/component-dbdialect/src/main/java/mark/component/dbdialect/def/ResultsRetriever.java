package mark.component.dbdialect.def;

import mark.component.dbmodel.model.*;
import mark.component.dbmodel.util.Utility;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

final class ResultsRetriever {

    private final ResultSetMetaData resultsMetaData;

    public ResultsRetriever(final ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            throw new SQLException("Cannot retrieve metadata for null results");
        }
        resultsMetaData = resultSet.getMetaData();
    }

    public ResultsColumns retrieveResults() throws SQLException {
        final ResultsColumns resultColumns = new ResultsColumns("");
        final int columnCount = resultsMetaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            final String catalogName = resultsMetaData.getCatalogName(i);
            final String schemaName = resultsMetaData.getSchemaName(i);
            String tableName = resultsMetaData.getTableName(i);
            if (Utility.isBlank(tableName)) {
                tableName = "";
            }

            final Schema schema = new Schema(catalogName, schemaName);
            final Table table = new Table(schema, tableName);
            
            final String databaseSpecificTypeName = resultsMetaData.getColumnTypeName(i);
            final ColumnDataType columnDataType = new ColumnDataType(schema, databaseSpecificTypeName);
            columnDataType.setJdbcType(resultsMetaData.getColumnType(i));
            columnDataType.setClassName(resultsMetaData.getColumnClassName(i));
            columnDataType.setPrecision(resultsMetaData.getPrecision(i));
            final int scale = resultsMetaData.getScale(i);
            columnDataType.setMaximumScale(scale);
            columnDataType.setMinimumScale(scale);

            final String columnName = resultsMetaData.getColumnName(i);
            final ResultsColumn column = new ResultsColumn(table, columnName);
            column.setOrdinalPosition(i);
            column.setType(columnDataType);

            column.setLabel(resultsMetaData.getColumnLabel(i));
            column.setDisplaySize(resultsMetaData.getColumnDisplaySize(i));

            final boolean isNullable = resultsMetaData.isNullable(i) == ResultSetMetaData.columnNullable;
            column.setAutoIncrement(resultsMetaData.isAutoIncrement(i));
            column.setCaseSensitive(resultsMetaData.isCaseSensitive(i));
            column.setCurrency(resultsMetaData.isCurrency(i));
            column.setDefinitelyWritable(resultsMetaData.isDefinitelyWritable(i));
            column.setNullable(isNullable);
            column.setReadOnly(resultsMetaData.isReadOnly(i));
            column.setSearchable(resultsMetaData.isSearchable(i));
            column.setSigned(resultsMetaData.isSigned(i));
            column.setWritable(resultsMetaData.isWritable(i));

            resultColumns.addColumn(column);
        }

        return resultColumns;
    }
}
