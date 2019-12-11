package mark.component.dbdialect.def;

import mark.component.dbmodel.model.Table;
import mark.component.dbdialect.DQLDialect;
import mark.component.dbdialect.ProductAnnotation;

/**
 * 类名称： DefaultDQLDialect
 * 描述：
 * 
 * 创建：   liang, 2013-3-22 9:48:42
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
@ProductAnnotation
public abstract class DefaultDQLDialect implements DQLDialect {

    public String buildCountSQL(Table table, String colName) {
        return buildCountSQL(table, colName, null, null);
    }

    public String buildCountSQL(Table table, String colName, String asName, String condition) {
        if (table == null) {
            throw new NullPointerException(" table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(");
        sb.append(colName);
        sb.append(")");

        if (asName != null && asName.length() > 0) {
            sb.append(" AS ");
            sb.append(asName);
        }
        sb.append(" FROM ");
        sb.append(table.getFullName());
        if (condition != null && condition.length() > 0) {
            sb.append(" WHERE ");
            sb.append(condition);
        }

        return sb.toString();
    }

    public String buildCountSQLBySQL(String sql, String colName, String asName) {
        if (sql == null) {
            throw new NullPointerException(" sql is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(");
        sb.append(colName);
        sb.append(")");

        if (asName != null && asName.length() > 0) {
            sb.append(" AS ");
            sb.append(asName);
        }
        sb.append(" FROM (");
        sb.append(sql);
        sb.append(")");

        return sb.toString();
    }

    public String buildMinSQL(Table table, String colName) {
        return buildMinSQL(table, colName, null, null);
    }

    public String buildMinSQL(Table table, String colName, String asName, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT MIN(");
        sb.append(colName);
        sb.append(")");
        if (asName != null && asName.length() > 0) {
            sb.append(" AS ");
            sb.append(asName);
        }
        sb.append(" FROM ");
        sb.append(table.getFullName());
        if (condition != null && condition.length() > 0) {
            sb.append(" WHERE ");
            sb.append(condition);
        }

        return sb.toString();
    }

    public String buildMaxSQL(Table table, String colName) {
        return buildMaxSQL(table, colName, null, null);
    }

    public String buildMaxSQL(Table table, String colName, String asName, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT MAX(");
        sb.append(colName);
        sb.append(")");

        if (asName != null && asName.length() > 0) {
            sb.append(" AS ");
            sb.append(asName);
        }
        sb.append(" FROM ");
        sb.append(table.getFullName());
        if (condition != null && condition.length() > 0) {
            sb.append(" WHERE ");
            sb.append(condition);
        }

        return sb.toString();
    }

    public String buildSumSQL(Table table, String colName) {
        return buildSumSQL(table, colName, null, null);
    }

    public String buildSumSQL(Table table, String colName, String asName, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM(");
        sb.append(colName);
        sb.append(")");

        if (asName != null && asName.length() > 0) {
            sb.append(" AS ");
            sb.append(asName);
        }
        sb.append(" FROM ");
        sb.append(table.getFullName());

        if (condition != null && condition.length() > 0) {
            sb.append(" WHERE ");
            sb.append(condition);
        }

        return sb.toString();
    }

    public String buildAvgSQL(Table table, String colName) {
        return buildAvgSQL(table, colName, null, null);
    }

    public String buildAvgSQL(Table table, String colName, String asName, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT AVG(");
        sb.append(colName);
        sb.append(")");
        if (asName != null && asName.length() > 0) {
            sb.append(" AS ");
            sb.append(asName);
        }
        sb.append(" FROM ");
        sb.append(table.getFullName());

        if (condition != null && condition.length() > 0) {
            sb.append(" WHERE ");
            sb.append(condition);
        }

        return sb.toString();
    }

    public String buildMedianSQL(Table table, String colName, String asName, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT MEDIAN(");
        sb.append(colName);
        sb.append(")");

        if (asName != null && asName.length() > 0) {
            sb.append(" AS ");
            sb.append(asName);
        }
        sb.append(" FROM ");
        sb.append(table.getFullName());

        if (condition != null && !condition.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(condition);
        }

        return sb.toString();
    }

    public String buildStdDevSQL(Table table, String colName, String asName, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT STDDEV(");
        sb.append(colName);
        sb.append(")");
        if (asName != null && asName.length() > 0) {
            sb.append(" AS ");
            sb.append(asName);
        }
        sb.append(" FROM ");
        sb.append(table.getFullName());

        if (condition != null && !condition.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(condition);
        }

        return sb.toString();
    }

    public String buildVarianceSQL(Table table, String colName, String asName, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT VARIANCE(");
        sb.append(colName);
        sb.append(")");

        if (asName != null && asName.length() > 0) {
            sb.append(" AS ");
            sb.append(asName);
        }
        sb.append(" FROM ");
        sb.append(table.getFullName());

        if (condition != null && !condition.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(condition);
        }

        return sb.toString();
    }

    public String buildModeSQL(Table table, String colName, String asName, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT STATS_MODE(");
        sb.append(colName);
        sb.append(")");

        if (asName != null && asName.length() > 0) {
            sb.append(" AS ");
            sb.append(asName);
        }
        sb.append(" FROM ");
        sb.append(table.getFullName());

        if (condition != null && !condition.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(condition);
        }

        return sb.toString();
    }

    public String buildTopNSQL(Table table, String colName, long N, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT TOP ");
        sb.append(N);
        sb.append(" ");
        sb.append(colName);
        sb.append(" FROM ");
        sb.append(table.getFullName());

        if (condition != null && condition.length() > 0) {
            sb.append(" WHERE ");
            sb.append(condition);
        }
        sb.append(" ORDER BY ");
        sb.append(colName);
        sb.append(" DESC ");

        return sb.toString();
    }

    public String buildBottomNSQL(Table table, String colName, long N, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT TOP ");
        sb.append(N);
        sb.append(" ");
        sb.append(colName);
        sb.append(" FROM ");
        sb.append(table.getFullName());
        if (condition != null && condition.length() > 0) {
            sb.append(" WHERE ");
            sb.append(condition);
        }
        sb.append(" ORDER BY ");
        sb.append(colName);

        return sb.toString();
    }

    public String getDomainSizeSQL(Table table, String colName, String conditions) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(DISTINCT ");
        sb.append(colName);
        sb.append(") FROM ");
        sb.append(table.getFullName());

        if (conditions != null && !conditions.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(conditions);
        }

        return sb.toString();
    }

    public String buildPageSQL(String sql, long offset, long limit) {
        if (limit > 0) {
            sql += " LIMIT " + (limit - 1);
        } else {
            sql += " LIMIT 0";
        }
        if (offset > 0) {
            sql += "," + offset;
        } else {
            sql += ",10";
        }

        return sql;
    }

    public String buildNextSequenceValueSQL(String sequenceName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(sequenceName);
        sb.append(".nextval");
        sb.append(" AS LAST_VALUE ");
        sb.append(" FROM ");
        sb.append(" DUAL ");

        return sb.toString();
    }

    public String buildQuerySequenceInfoSQL(String sequenceName) {
        String sql = "SELECT SEQUENCE_NAME, MIN_VALUE, MAX_VALUE, INCREMENT_BY, MIN_VALUE AS START_VALUE, LAST_NUMBER AS LAST_VALUE"
                + " FROM USER_SEQUENCES";
        if (sequenceName != null && !sequenceName.isEmpty()) {
            sql += " WHERE SEQUENCE_NAME='" + sequenceName + "'";
        }

        return sql;
    }
}
