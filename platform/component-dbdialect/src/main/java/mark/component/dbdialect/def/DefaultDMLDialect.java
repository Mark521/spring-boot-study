package mark.component.dbdialect.def;

import mark.component.dbmodel.model.Column;
import mark.component.dbmodel.model.Table;
import mark.component.dbdialect.DMLDialect;
import mark.component.dbdialect.ProductAnnotation;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

/**
 * 类名称： DefaultDMLDialect
 * 描述：
 * 
 * 创建：   liang, 2013-3-22 17:34:40
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
@ProductAnnotation
public abstract class DefaultDMLDialect implements DMLDialect {

    public String buildInsertSQL(Table table) {
        return buildInsertSQL(table, table.getColumns());
    }

    public String buildInsertSQL(Table table, List<Column> columns) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        if (columns == null) {
            throw new NullPointerException("column of table is null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(table.getName());
        sb.append("(");

        final String comma = ",";
        final String query = "?";

        String columnStr = "";
        String valueStr = "";
        for (Column column : columns) {
            columnStr += column.getName();
            columnStr += comma;

            valueStr += query;
            valueStr += comma;
        }
        columnStr = StringUtils.stripEnd(columnStr, comma);
        valueStr = StringUtils.stripEnd(valueStr, comma);

        sb.append(columnStr);
        sb.append(" VALUES(");
        sb.append(valueStr);
        sb.append(")");

        return sb.toString();
    }

    public String buildBatchInsertSQL(Table table, String select) {
        return buildBatchInsertSQL(table, table.getColumns(), select);
    }

    public String buildBatchInsertSQL(Table table, List<Column> columns, String select) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        if (columns == null) {
            throw new NullPointerException("column of table is null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(table.getName());
        sb.append("(");

        final String comma = ",";
        String columnStr = "";
        for (Column column : columns) {
            columnStr += column.getName();
            columnStr += comma;
        }
        columnStr = StringUtils.stripEnd(columnStr, comma);
        sb.append(columnStr);
        sb.append(") ");
        sb.append(select);

        return sb.toString();
    }

    public void addBatchDML(ProductAnnotation dialect, PreparedStatement stat, Map<Column, Object> values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String buildDeleteSQL(Table table) {
        return buildDeleteSQL(table, "");
    }

    public String buildDeleteSQL(Table table, String where) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(table.getName());
        if (where != null && !where.isEmpty()) {
            if (where.indexOf("WHERE") != -1) {
                sb.append(where);
            } else {
                sb.append("WHERE= ");
                sb.append(where);
            }
        }

        return sb.toString();
    }

    public String buildDeleteSQL(Table table, List<Column> columns) {
        return buildDeleteSQL(table, columns, "");
    }

    public String buildDeleteSQL(Table table, List<Column> columns, String where) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(table.getName());
        sb.append(" WHERE ");

        String strip = "AND ";
        String columnStr = "";
        for (Column column : columns) {
            columnStr += column.getName();
            columnStr += " =? ";
            columnStr += strip;
        }
        columnStr = StringUtils.stripEnd(columnStr, strip);
        sb.append(columnStr);
        if (where != null && !where.isEmpty()) {
            if (where.indexOf("WHERE") != -1) {
                sb.append(where);
            } else {
                sb.append(" WHERE= ");
                sb.append(where);
            }
        }

        return sb.toString();
    }

    public String buildUpdateSQL(Table table) {
        return buildUpdateSQL(table, "");
    }

    public String buildUpdateSQL(Table table, String where) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(table.getName());
        if (where == null && !where.isEmpty()) {
            if (where.indexOf("WHERE") != -1) {
                sb.append(where);
            } else {
                sb.append("WHERE= ");
                sb.append(where);
            }
        }

        return sb.toString();
    }

    public String buildUpdateSQL(Table table, List<Column> columns) {
        return buildUpdateSQL(table, columns, "");
    }

    public String buildUpdateSQL(Table table, List<Column> columns, String where) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(table.getName());
        sb.append(" SET ");

        final String comma = ", ";
        String columnStr = "";
        for (Column column : columns) {
            columnStr += column.getName();
            columnStr += "=?";
            columnStr += comma;
        }
        columnStr = StringUtils.stripEnd(columnStr, comma);
        sb.append(columnStr);

        if (where != null && !where.isEmpty()) {
            if (where.indexOf(" WHERE ") != -1) {
                sb.append(where);
            } else {
                sb.append(" WHERE= ");
                sb.append(where);
            }
        }

        return sb.toString();
    }

    public String mergeRecordByTable(Table srcTable,
            Table tarTable,
            List<Map<String, String>> insertColMappings,
            List<Map<String, String>> relatedColMappings,
            List<Map<String, String>> assignColMappings) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String buildSequenceUpdateSQL(String sequenceName) {
        return "";
    }
}
