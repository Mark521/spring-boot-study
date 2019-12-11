/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mark.component.dbdialect.db2;

import mark.component.dbdialect.def.DefaultDQLDialect;

/**
 * 类名称： DB2DQLDialect
 * 描述：
 * 
 * 创建：   liang, 2013-3-29 13:51:09
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
public class DB2DQLDialect extends DefaultDQLDialect {
    
    @Override
    public String buildNextSequenceValueSQL(String sequenceName) {
        return "values nextval as LAST_VALUE for " + sequenceName;
    }
    
    @Override
    public String buildQuerySequenceInfoSQL(String sequenceName) {
        String sql = "SELECT SEQNAME AS SEQUENCE_NAME, "
                + "MINVALUE AS MIN_VALUE, "
                + "MAXVALUE AS MAX_VALUE, "
                + "INCREMENT AS INCREMENT_BY, "
                + "LASTASSIGNEDVAL AS LAST_VALUE, "
                + "START AS START_VALUE"
                + " FROM SYSIBM.SYSSEQUENCES";
        if (sequenceName != null && !sequenceName.isEmpty()) {
            sql += " WHERE SEQNAME='" + sequenceName + "'";
        }
        
        return sql;
    }
}
