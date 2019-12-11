package mark.component.dbdialect;

import mark.component.dbmodel.model.Column;
import mark.component.dbmodel.model.Table;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

/**
 * 类名称： DMLDialect
 * 描述：
 * 
 * 创建：   liang, 2013-3-22 14:41:19
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
public interface DMLDialect {

    /**
     * 生成插入记录SQL, 默认表对象中的全部字段
     * @param table
     * @return 拼装的sql
     */
    public String buildInsertSQL(Table table);

    /**
     * 生成批量插入记录SQL, 默认表对象中的全部字段
     * select必须是标准SQL且保证所有数据库支持，如果不是标准SQL请使用其重构方法
     * @return 拼装的sql
     */
    public String buildBatchInsertSQL(Table table, String select);

    /**
     * 生成插入记录SQL, 根据传入的列
     * @param table
     * @return 拼装的sql
     */
    public String buildInsertSQL(Table table, List<Column> columns);

    /**
     * 生成批量插入记录SQL, 指定的字段
     * select必须是标准SQL且保证所有数据库支持，如果不是标准SQL请使用其重构方法
     * @return 拼装的sql
     */
    public String buildBatchInsertSQL(Table table, List<Column> columns, String select);

    /**
     * 对数据库表进行批量DML操作，此过程调用PreparedStatement的addBatch方法
     * @param dialect
     * @param values
     * @return
     */
    public void addBatchDML(ProductAnnotation dialect, PreparedStatement stat, Map<Column, Object> values);

    /**
     * 生成删除表全部记录SQL， 默认全部列
     * 不带where条件
     */
    public String buildDeleteSQL(Table table);

    /**
     * 生成删除表记录SQL,默认全部列
     * where条件必须是标准SQL且保证所有数据库支持，如果不是标准SQL请使用其重构方法
     */
    public String buildDeleteSQL(Table table, String where);

    /**
     * 生成删除表记录SQL,指定列
     */
    public String buildDeleteSQL(Table table, List<Column> columns);

    /**
     * 生成删除表记录SQL,指定列
     * where条件必须是标准SQL且保证所有数据库支持，如果不是标准SQL请使用其重构方法
     */
    public String buildDeleteSQL(Table table, List<Column> columns, String where);

    /**
     * 生成更新记录SQL,默认表全部字段
     * @param table
     * @return 拼装SQL
     */
    public String buildUpdateSQL(Table table);

    /**
     * 生成更新记录SQL，指定列
     * 不带where条件
     */
    public String buildUpdateSQL(Table table, String where);

    /**
     * 更新记录
     * where条件必须是标准SQL且保证所有数据库支持，如果不是标准SQL请使用其重构方法
     */
    public String buildUpdateSQL(Table table, List<Column> columns);

    /**
     * 更新记录SQL
     * where条件必须是标准SQL且保证所有数据库支持，如果不是标准SQL请使用其重构方法
     */
    public String buildUpdateSQL(Table table, List<Column> columns, String where);

    /**
     * 更新sequence记录
     * @return 
     */
    public String buildSequenceUpdateSQL(String sequenceName);
    /**
     * 通过一个表的某些列完成对另一个表的merge操作。
     * 所谓merge，就是指当记录在指定关联条件下能关联上时，就使用update更新目标表的指定列，
     * 当匹配不上的时候，则选择insert
     * @param
     */
    public String mergeRecordByTable(Table srcTable,
                                     Table tarTable,
                                     List<Map<String, String>> insertColMappings,
                                     List<Map<String, String>> relatedColMappings,
                                     List<Map<String, String>> assignColMappings);
}
