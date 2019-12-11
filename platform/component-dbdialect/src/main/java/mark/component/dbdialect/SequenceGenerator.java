package mark.component.dbdialect;

import mark.component.dbmodel.model.*;
import mark.component.core.exception.BaseException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 类名: SequenceGenerator.java
 * 创建者 >
 * 时间: 2013-4-18, 19:59:23
 */
public class SequenceGenerator implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "ADQM_SEQUENCE_REGISTER";//表名
    public static final String SEQUENCE_PARAM = "SEQUENCE_NAME";    //名称
    public static final String MIN_VALUE_PARAM = "MIN_VALUE";       //最小值
    public static final String MAX_VALUE_PARAM = "MAX_VALUE";       //最大值
    public static final String START_VALUE_PARAM = "START_VALUE";   //初始值
    public static final String INCREMENT_PARAM = "INCREMENT_BY";  //增长值
    public static final String LAST_VALUE_PARAM = "LAST_VALUE";     //最后一个值

    /**
     * 生成ADQM_SEQUNCE_REGISTER表的建表语句
     * @param sequence
     * @param conn
     * @return
     * @throws SQLException 
     */
    public static String createTable(Sequence sequence, Connection conn) throws SQLException, BaseException {
        MetaDialect dialect = DialectRepository.lookupDialect(conn, MetaDialect.class);

        Schema schema = sequence.getSchema();
        Table table = new Table(schema, TABLE_NAME);

        Column seqName = new Column(table, SEQUENCE_PARAM);
        ColumnDataType columnDataType = new ColumnDataType(schema, dialect.getJdbcTypeName(Types.VARCHAR));
        seqName.setType(columnDataType);
        seqName.setPrecision(64);
        table.addColumn(seqName);

        Column startValue = new Column(table, START_VALUE_PARAM);
        columnDataType = new ColumnDataType(schema, dialect.getJdbcTypeName(Types.INTEGER));
        startValue.setType(columnDataType);
        startValue.setDefaultValue(sequence.getStartValue() + "");
        table.addColumn(startValue);

        Column incValue = new Column(table, INCREMENT_PARAM);
        columnDataType = new ColumnDataType(schema, dialect.getJdbcTypeName(Types.INTEGER));
        incValue.setType(columnDataType);
        incValue.setDefaultValue(sequence.getIncrementBy() + "");
        table.addColumn(incValue);

        Column maxValue = new Column(table, MAX_VALUE_PARAM);
        columnDataType = new ColumnDataType(schema, dialect.getJdbcTypeName(Types.BIGINT));
        maxValue.setType(columnDataType);
        maxValue.setDefaultValue(sequence.getMaxValue() + "");
        table.addColumn(maxValue);

        Column minValue = new Column(table, MIN_VALUE_PARAM);
        columnDataType = new ColumnDataType(schema, dialect.getJdbcTypeName(Types.BIGINT));
        minValue.setType(columnDataType);
        minValue.setDefaultValue(sequence.getMinValue() + "");
        table.addColumn(minValue);

        Column lastValue = new Column(table, LAST_VALUE_PARAM);
        columnDataType = new ColumnDataType(schema, dialect.getJdbcTypeName(Types.BIGINT));
        lastValue.setType(columnDataType);
        lastValue.setDefaultValue((sequence.getStartValue()) + "");
        table.addColumn(lastValue);

        DDLDialect ddlDialect = DialectRepository.lookupDialect(conn, DDLDialect.class);
        return ddlDialect.createTableSQL(table);
    }

    /**
     * 生成插入sequence数据语句
     * @param sequence
     * @return 
     */
    public static String buildSequenceSQL(Sequence sequence) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(TABLE_NAME);
        sb.append("(");
        sb.append(SEQUENCE_PARAM);
        sb.append(", ");
        sb.append(START_VALUE_PARAM);
        sb.append(", ");
        sb.append(INCREMENT_PARAM);
        sb.append(", ");
        sb.append(MIN_VALUE_PARAM);
        sb.append(", ");
        sb.append(MAX_VALUE_PARAM);
        sb.append(", ");
        sb.append(LAST_VALUE_PARAM);
        sb.append(") VALUES('");
        sb.append(sequence.getName());
        sb.append("', ");
        sb.append(sequence.getStartValue());
        sb.append(", ");
        sb.append(sequence.getIncrementBy());
        sb.append(", ");
        sb.append(sequence.getMinValue());
        sb.append(", ");
        sb.append(sequence.getMaxValue());
        sb.append(", ");
        sb.append(sequence.getLastValue());
        sb.append(")");

        return sb.toString();
    }

    /**
     * 生成删除sequence语句
     * @param sequence
     * @return 
     */
    public static String deleteSequenceSQL(Sequence sequence) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(TABLE_NAME);
        sb.append(" WHERE ");
        sb.append(SEQUENCE_PARAM);
        sb.append("='");
        sb.append(sequence.getName());
        sb.append("'");

        return sb.toString();
    }

    /**
     * 获取最后一个值
     * @param sequenceName
     * @return 
     */
    public static String lastValueSQL(String sequenceName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(LAST_VALUE_PARAM);
        sb.append(" FROM ");
        sb.append(TABLE_NAME);
        sb.append(" WHERE ");
        sb.append(SEQUENCE_PARAM);
        sb.append("='");
        sb.append(sequenceName);
        sb.append("'");

        return sb.toString();
    }

    /**
     * 生成更新sequence最后一个值语句
     * @param sequenceName
     * @return 
     */
    public static String updateLastValueSQL(String sequenceName) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(TABLE_NAME);
        sb.append(" SET ");
        sb.append(LAST_VALUE_PARAM);
        sb.append("=");
        sb.append(LAST_VALUE_PARAM);
        sb.append("+");
        sb.append(INCREMENT_PARAM);
        sb.append(" WHERE ");
        sb.append(SEQUENCE_PARAM);
        sb.append("='");
        sb.append(sequenceName);
        sb.append("'");

        return sb.toString();
    }

    /**
     * 生成查询sequence语句
     * @param sequenceName
     * @return 
     */
    public static String querySQL(String sequenceName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(SEQUENCE_PARAM);
        sb.append(", ");
        sb.append(START_VALUE_PARAM);
        sb.append(", ");
        sb.append(INCREMENT_PARAM);
        sb.append(", ");
        sb.append(MIN_VALUE_PARAM);
        sb.append(", ");
        sb.append(MAX_VALUE_PARAM);
        sb.append(", ");
        sb.append(LAST_VALUE_PARAM);
        sb.append(" FROM ");
        sb.append(TABLE_NAME);
        sb.append(" WHERE ");
        sb.append(SEQUENCE_PARAM);
        sb.append("='");
        sb.append(sequenceName);
        sb.append("'");

        return sb.toString();
    }
}
