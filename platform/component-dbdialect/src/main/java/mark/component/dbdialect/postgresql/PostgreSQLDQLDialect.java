/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mark.component.dbdialect.postgresql;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbdialect.def.DefaultDQLDialect;

/**
 * 类名称： PostgreSQLDQLDialect
 * 描述：
 * 
 * 创建：   liang, 2013-3-29 13:42:36
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
@ProductAnnotation(productName = "PostgreSQL")
public class PostgreSQLDQLDialect extends DefaultDQLDialect {

    @Override
    public String buildNextSequenceValueSQL(String sequenceName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("nextval ('");
        sb.append(sequenceName);
        sb.append("') AS LAST_VALUE");
        
        return sb.toString();
    }
    
    @Override
    public String buildQuerySequenceInfoSQL(String sequenceName) {
        String sql = "SELECT sequence_name AS SEQUENCE_NAME, "
                + "min_value AS MIN_VALUE, "
                + "max_value AS MAX_VALUE, "
                + "increment_by AS INCREMENT_BY, "
                + "last_value AS LAST_VALUE, "
                + "start_value AS START_VALUE"
                + " FROM SYSIBM.SYSSEQUENCES";
        
        if (sequenceName != null && !sequenceName.isEmpty()) {
            sql += " WHERE SEQNAME='" + sequenceName + "'";
        }
        
        return sql;
    }
    
    public String buildPageSQL(String sql, long offset, long limit) {
        if (limit > 0) {
            sql += " LIMIT " + (limit - 1);
        } else {
            sql += " LIMIT 10";
        }
        if (offset > 0) {
            sql += " OFFSET " + (offset - 1);
        } else {
            sql += " OFFSET 0";
        }

        return sql;
    }
}
