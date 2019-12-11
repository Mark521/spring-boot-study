package mark.component.dbmodel.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResultsColumns extends AbstractNamedObject {

    private static final long serialVersionUID = 5204766782914559188L;
    private final NamedObjectList<ResultsColumn> columns = new NamedObjectList<ResultsColumn>();

    public ResultsColumns(final String name) {
        super(name);
    }

    public ResultsColumn getColumn(final String name) {
        return columns.lookup(name);
    }

    public List<ResultsColumn> getColumns() {
        return new ArrayList<ResultsColumn>(columns.values());
    }

    public String getColumnsListAsString() {
        String columnsList = "";
        final List<ResultsColumn> columns = getColumns();
        if (columns != null && columns.size() > 0) {
            final StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < columns.size(); i++) {
                if (i > 0) {
                    buffer.append(", ");
                }
                final ResultsColumn column = columns.get(i);
                buffer.append(column.getFullName());
            }
            columnsList = buffer.toString();
        }
        return columnsList;
    }

    public Iterator<ResultsColumn> iterator() {
        return getColumns().iterator();
    }

    public void addColumn(final ResultsColumn column) {
        columns.add(column);
    }
}
