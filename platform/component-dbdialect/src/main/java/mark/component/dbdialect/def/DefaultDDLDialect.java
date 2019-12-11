package mark.component.dbdialect.def;

import mark.component.dbmodel.model.*;
import mark.component.dbdialect.DDLDialect;
import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbdialect.util.TypeUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.List;

/**
 * 类名称： DefaultDDLDialect
 * 描述：
 * 
 * 创建：   liang, 2013-3-22 17:34:31
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
@ProductAnnotation
public abstract class DefaultDDLDialect implements DDLDialect {

    public String createTableSQL(Table table) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }

        List<Column> columns = table.getColumns();
        if (columns == null || columns.size() <= 0) {
            throw new NullPointerException("columns of table is null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(table.getName());
        sb.append("(");

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            sb.append(TypeUtil.getColumnTypeDefinition(column));
            if (column.isPartOfPrimaryKey()) {
                sb.append(" PRIMARY KEY ");
            } else {
                if (column.getDefaultValue() != null && !column.getDefaultValue().isEmpty()) {
                    sb.append(" DEFAULT ");
                    sb.append(column.getDefaultValue());
                }
                if (!column.isNullable()) {
                    sb.append(" not null ");
                } else {
                    sb.append(" null ");
                }
            }
//            if (col.getComments() != null && !"".equals(col.getComments())) {
//                //有的注释是加在字段后面，有的注释是在创建完表之后再添加
//                addComment(conn, sb, comments, catalog, schema, tableName, col);
//            }
            if (i != columns.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");

        return sb.toString();
    }

    public String createLikeTableSQL(Table sourceTable, Table targetTable) {
        if (sourceTable == null) {
            throw new NullPointerException("source tale object is null");
        }
        if (targetTable == null) {
            throw new NullPointerException("target  tale object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(targetTable.getName());
        sb.append(" LIKE ");
        sb.append(sourceTable.getName());

        return sb.toString();
    }

    public String dropTableSQL(Table table) {
        if (table == null) {
            throw new NullPointerException("tale object is null");
        }

        return "DROP TABLE " + table.getName();
    }

    public String createViewSQL(Table view, String asSelect) {
        if (view == null) {
            throw new NullPointerException("view object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE VIEW ");
        sb.append(view.getName());
        sb.append(" AS ");
        sb.append(asSelect);

        return sb.toString();
    }

    public String dropViewSQL(Table view) {
        if (view == null) {
            throw new NullPointerException("view object is null");
        }

        return "DROP VIEW " + view.getName();
    }

    public String createIndexSQL(Index index) {
        if (index == null) {
            throw new NullPointerException("index object is null");
        }
        List<IndexColumn> columns = index.getColumns();
        if (columns == null || columns.isEmpty()) {
            throw new NullPointerException("columns of index object is null");
        }

        StringBuilder sb = new StringBuilder();
        if (index.isUnique()) {
            sb.append("CREATE UNIQUE INDEX ");
        } else {
            sb.append("CREATE INDEX ");
        }
        sb.append(index.getName());
        sb.append(" ON ");
        sb.append(index.getParent().getName());
        sb.append("(");

        final String trimComma = ", ";
        for (IndexColumn column : columns) {
            sb.append(column.getName());
            if (column.getSortSequence() != null) {
                sb.append(" ");
                sb.append(column.getSortSequence().getCode());
                sb.append(" ");
            }
            sb.append(trimComma);
        }
        String trimSQL = StringUtils.stripEnd(sb.toString(), trimComma);
        trimSQL += ")";

        return trimSQL;
    }

    public String dropIndexSQL(Index index) {
        if (index == null) {
            throw new NullPointerException("index object is null");
        }

        return "DROP INDEX " + index.getName();
    }

    public String createProcedureSQL(Procedure procedure) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String dropProcedureSQL(Procedure procedure) {
        if (procedure == null) {
            throw new NullPointerException("procedure object is null");
        }

        return "DROP PROCEDURE " + procedure.getName();
    }

    public String createSequenceSQL(Sequence sequence) {
        if (sequence == null) {
            throw new NullPointerException("sequence object is null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE SEQUENCE ");
        sb.append(sequence.getName());
        
        BigInteger minValue = sequence.getMinValue();
        if (minValue != null) {
            sb.append(" MINVALUE ");
            sb.append(minValue);
        }
        BigInteger maxValue = sequence.getMaxValue();
        if (maxValue != null) {
            sb.append(" MAXVALUE ");
            sb.append(maxValue);
        }
        
        BigInteger startValue = sequence.getStartValue();
        if (startValue != null) {
            sb.append(" START WITH ");
            sb.append(startValue);
        }else{
            sb.append(" START WITH 0");
        }
        
        BigInteger incrementBy = sequence.getIncrementBy();
        if (incrementBy != null) {
            sb.append(" INCREMENT BY ");
            sb.append(incrementBy);
        }else{
            sb.append(" INCREMENT BY 1");
        }

        return sb.toString();
    }

    public String dropSequenceSQL(Sequence sequence) {
        if (sequence == null) {
            throw new NullPointerException("sequence object is null");
        }

        return "DROP SEQUENCE " + sequence.getName();
    }

    public String createTriggerSQL(Trigger trigger) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String dropTriggerSQL(Trigger trigger) {
        if (trigger == null) {
            throw new NullPointerException("trigger object is null");
        }

        return "DROP TRIGGER " + trigger.getName();
    }

    public String truncateTableSQL(Table table) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }

        return "TRUNCATE TABLE " + table.getName();
    }
}
