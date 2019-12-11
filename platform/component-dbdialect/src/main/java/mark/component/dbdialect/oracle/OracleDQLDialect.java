package mark.component.dbdialect.oracle;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbdialect.def.DefaultDQLDialect;
import mark.component.dbmodel.model.Table;

/**
 * 类名称： OracleDQLDialect
 * 描述：
 * 
 * 创建：   liang, 2013-3-22 9:49:35
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
@ProductAnnotation(productName = "Oracle")
public class OracleDQLDialect extends DefaultDQLDialect {

    @Override
    public String buildTopNSQL(Table table, String colName, long N, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(colName);
        sb.append(" FROM ");
        sb.append("(SELECT DISTINCT ");
        sb.append(colName);
        sb.append(" FROM ");
        sb.append(table.getFullName());

        if (condition != null && condition.length() > 0) {
            sb.append(" WHERE ");
            sb.append(condition);
        }
        sb.append(" ORDER BY ");
        sb.append(colName);
        sb.append(" DESC ) ");
        sb.append("WHERE ROWNUM <= ");
        sb.append(N);

        return sb.toString();
    }

    @Override
    public String buildBottomNSQL(Table table, String colName, long N, String condition) {
        if (table == null) {
            throw new NullPointerException("table object is null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(colName);
        sb.append(" FROM ");
        sb.append("(SELECT DISTINCT ");
        sb.append(colName);
        sb.append(" FROM ");
        sb.append(table.getFullName());

        if (condition != null && condition.length() > 0) {
            sb.append(" WHERE ");
            sb.append(condition);
        }

        sb.append(" ORDER BY ");
        sb.append(colName);
        sb.append(" ) WHERE ROWNUM <= ");
        sb.append(N);

        return sb.toString();
    }

    @Override
    public String buildPageSQL(String sql, long offset, long limit) {
        StringBuilder sb = new StringBuilder();
        if (limit <= 0) {
            if (offset <= 1) {
                sb.append(sql);
            } else {
                sb.append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM (");
                sb.append(sql);
                sb.append(") A ");
                sb.append(") DVIEW WHERE RN >= ");
                sb.append(offset);
            }
        } else {
            if (offset <= 1) {
                sb.append("SELECT * FROM ( ");
                sb.append(sql);
                sb.append(") A WHERE ROWNUM <= ");
                sb.append(limit);
            } else {
                sb.append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM (");
                sb.append(sql);
                sb.append(") A WHERE ROWNUM < ");
                sb.append(limit + offset);
                sb.append(") DVIEW WHERE RN >= ");
                sb.append(offset);
            }
        }

        return sb.toString();
    }
}
