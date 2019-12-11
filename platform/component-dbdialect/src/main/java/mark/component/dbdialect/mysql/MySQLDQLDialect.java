package mark.component.dbdialect.mysql;

import mark.component.dbdialect.def.DefaultDQLDialect;
import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbdialect.SequenceGenerator;
import mark.component.dbmodel.model.Table;

/**
 * 类名称： MySQLDQLDialect
 * 描述：
 * 
 * 创建：   liang, 2013-3-29 13:38:44
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
@ProductAnnotation(productName = "MySQL")
public class MySQLDQLDialect extends DefaultDQLDialect {

    @Override
    public String buildTopNSQL(Table table, String colName, long N, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT ");
        sb.append(colName);
        sb.append(" FROM ");
        sb.append(table.getName());

        if (condition != null && condition.length() > 0) {
            sb.append(" WHERE ");
            sb.append(condition);
        }
        sb.append(" ORDER BY ");
        sb.append(colName);
        sb.append(" desc LIMIT 0,");
        sb.append(N);

        return sb.toString();
    }

    @Override
    public String buildBottomNSQL(Table table, String colName, long N, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT ");
        sb.append(colName);
        sb.append(" FROM ");
        sb.append(table.getName());

        if (condition != null && condition.length() > 0) {
            sb.append(" WHERE ");
            sb.append(condition);
        }
        sb.append(" ORDER BY ");
        sb.append(colName);
        sb.append(" LIMIT 0,");
        sb.append(N);

        return sb.toString();
    }

    @Override
    public String buildPageSQL(String sql, long offset, long limit) {
        if (limit > 0) {
            sql += " LIMIT " + limit;
        } else {
            sql += " LIMIT 0";
        }
        if (offset > 0) {
            sql += " OFFSET " + (offset-1);
        } else {
            sql += " OFFSET 10";
        }

        return sql;
    }
    
    @Override
    public String buildNextSequenceValueSQL(String sequenceName) {
        return SequenceGenerator.lastValueSQL(sequenceName);
    }
    
    @Override
    public String buildQuerySequenceInfoSQL(String sequenceName) {
        return SequenceGenerator.querySQL(sequenceName);
    }
}
