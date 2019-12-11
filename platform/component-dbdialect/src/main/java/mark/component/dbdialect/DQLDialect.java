package mark.component.dbdialect;

import mark.component.dbmodel.model.Table;

/**
 * 类名称： DQLDialect
 * 描述：
 * 
 * 创建：   liang, 2013-3-21 17:37:49
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
public interface DQLDialect {

    public String buildCountSQL(Table table, String colName);

    public String buildCountSQL(Table table, String colName, String asName, String condition);

    public String buildCountSQLBySQL(String sql, String colName, String asName);

    public String buildMinSQL(Table table, String colName);

    public String buildMinSQL(Table table, String colName, String asName, String condition);

    public String buildMaxSQL(Table table, String colName);

    public String buildMaxSQL(Table table, String colName, String asName, String condition);

    public String buildSumSQL(Table table, String colName);

    public String buildSumSQL(Table table, String colName, String asName, String condition);

    public String buildAvgSQL(Table table, String colName);

    public String buildAvgSQL(Table table, String colName, String asName, String condition);

    public String buildMedianSQL(Table table, String colName, String asName, String condition);

    public String buildStdDevSQL(Table table, String colName, String asName, String condition);

    public String buildVarianceSQL(Table table, String colName, String asName, String condition);

    public String buildModeSQL(Table table, String colName, String asName, String condition);

    public String buildTopNSQL(Table table, String colName, long N, String condition);

    public String buildBottomNSQL(Table table, String colName, long N, String condition);

    public String getDomainSizeSQL(Table table, String colName, String conditions);

    /*
     * 适用于mysql、sqlite、bdb
     * offset 元组偏移，索引从1开始
     * limit  元组限制条数，从1开始
     * 语句样式select * from tablename order by colname limit M,N
     * 其偏移位从0开始
     */
    public String buildPageSQL(String sql, long offset, long limit);

    public String buildNextSequenceValueSQL(String sequenceName);

    public String buildQuerySequenceInfoSQL(String sequenceName);
}
