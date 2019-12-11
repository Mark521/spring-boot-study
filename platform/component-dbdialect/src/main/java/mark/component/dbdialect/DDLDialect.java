package mark.component.dbdialect;

import mark.component.dbmodel.model.*;

/**
 * 类名称： DDLDialect
 * 描述：
 * 
 * 创建：   liang, 2013-3-22 13:52:08
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
public interface DDLDialect {

    /**
     * 创建方言表，不抛异常则创建成功
     * 此方法只能在方言的代码的被调用，公共的代码就使用createGeneralTable
     * Column对象中不设置javatype,设置jdbctype
     * @return 数组的第一个位置是创建表的SQL，后面是添加注释的SQL，有些数据库的注释就是创建表的SQL中，那时数组长度为1
     */
    public String createTableSQL(Table table);

    /**
     * 克隆表，不抛异常则创建成功
     */
    public String createLikeTableSQL(Table sourceTable, Table targetTable);

    /**
     * 删除表，不抛异常则删除成功
     */
    public String dropTableSQL(Table table);

    /**
     * 创建视图，不抛异常则创建成功
     * asSelect带数据
     */
    public String createViewSQL(Table view, String asSelect);

    /**
     * 删除视图，不抛异常则删除成功
     */
    public String dropViewSQL(Table view);

    /**
     * 创建索引，不抛异常则创建成功
     * @param index 确定具体某个索引列的升序或降序排序方向。默认设置为 ASC，true表示DESC，false表示ASC
     */
    public String createIndexSQL(Index index);

    /**
     * 删除索引，不抛异常则删除成功
     */
    public String dropIndexSQL(Index index);

    /**
     * 创建存储过程，不抛异常则创建成功
     */
    public String createProcedureSQL(Procedure procedure);

    /**
     * 删除存储过程，不抛异常则删除成功
     */
    public String dropProcedureSQL(Procedure procedure);

    /**
     * 创建序列，不抛异常则创建成功
     */
    public String createSequenceSQL(Sequence sequence);

    /**
     * 删除序列，不抛异常则删除成功
     */
    public String dropSequenceSQL(Sequence sequence);

    /**
     * 创建触发器，不抛异常则创建成功
     */
    public String createTriggerSQL(Trigger trigger);

    /**
     * 删除触发器，不抛异常则删除成功
     */
    public String dropTriggerSQL(Trigger trigger);

    /**
     * 清空表里的数据
     */
    public String truncateTableSQL(Table table);
}
