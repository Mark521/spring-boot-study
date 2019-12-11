package mark.component.dbdialect;

import mark.component.dbmodel.model.*;
import mark.component.core.exception.BaseException;
import org.apache.commons.dbutils.ResultSetHandler;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

public interface MetaDialect extends DBInitialize {

    /**
     * 获取数据库信息
     * @return
     */
    DatabaseInfo getDatabaseInfo();

    boolean supportSequence();

    boolean supportSynonym();

    boolean supportTriger();

    boolean supportCatalog();

    boolean supportSchema();
    char openQuote();

    char closeQuote();

    /**
     * 1. 识别quote
     * 2. 负责大小写转换
     * @param name
     * @return
     */
    String quote(final String name);

    /**
     * 1. 负责识别去除quote
     * 2. 负责大小写转换
     * @param name
     * @return
     */
    public String unquote(final String name);

    public boolean equal(String aName, String bName) throws BaseException;

    public String getDefaultCatalog(final Connection conn) throws BaseException;

    public void setDefaultCatalog(final Connection conn, final String catalogName) throws BaseException;

    public Schema getDefaultSchema(final Connection conn) throws BaseException;

    public String getDefaultSchemaName(final Connection conn) throws BaseException;

    public void setDefaultSchema(final Connection conn, final String schemaName) throws BaseException;

    /**
     * 将字符集追加到url后面
     */
    public String urlWithCharset(final String url, final String charSet) throws BaseException;

    public String getUserName(final Connection connection) throws BaseException;

    public Collection<ColumnDataType> getColumnDataTypes();

    public Collection<ColumnDataType> getColumnDataTypes(final int jdbcType);

    public Collection<ColumnDataType> getColumnDataTypes(final int jdbcType, final int precision);

    public Collection<ColumnDataType> getColumnDataTypes(final String jdbcTypeName);

    public Collection<ColumnDataType> getColumnDataTypes(final String jdbcTypeName, final int precision);

    public Collection<String> getKeyWords();

    public boolean isKeyWord(final String identifier);

    public Collection<SystemFunction> getSystemFunctions() throws BaseException;

    public SystemFunction getSystemFunction(final String systemFunctionName) throws BaseException;
    /*
     * 方言sql取元数据
     */
    public static final String KEY_INFORMATION_SCHEMA_VIEWS = "select.INFORMATION_SCHEMA.VIEWS";
    public static final String KEY_INFORMATION_SCHEMA_VIEW = "select.INFORMATION_SCHEMA.VIEW";
    public static final String KEY_INFORMATION_SCHEMA_TRIGGERS = "select.INFORMATION_SCHEMA.TRIGGERS";
    public static final String KEY_INFORMATION_SCHEMA_TABLE_CONSTRAINTS = "select.INFORMATION_SCHEMA.TABLE_CONSTRAINTS";
    public static final String KEY_INFORMATION_SCHEMA_ROUTINE = "select.INFORMATION_SCHEMA.ROUTINE";
    public static final String KEY_INFORMATION_SCHEMA_ROUTINES = "select.INFORMATION_SCHEMA.ROUTINES";
    public static final String KEY_INFORMATION_SCHEMA_CHECK_CONSTRAINTS = "select.INFORMATION_SCHEMA.CHECK_CONSTRAINTS";
    public static final String KEY_INFORMATION_SCHEMA_SCHEMATA = "select.INFORMATION_SCHEMA.SCHEMATA";
    public static final String KEY_INFORMATION_SCHEMA_SYNONYM = "select.INFORMATION_SCHEMA.SYNONYM";
    public static final String KEY_INFORMATION_SCHEMA_SYNONYMS = "select.INFORMATION_SCHEMA.SYNONYMS";
    public static final String KEY_ADDITIONAL_TABLE_ATTRIBUTES = "select.ADDITIONAL_TABLE_ATTRIBUTES";
    public static final String KEY_ADDITIONAL_COLUMN_ATTRIBUTES = "select.ADDITIONAL_COLUMN_ATTRIBUTES";

    public String getInformationSchemaSQL(String key);

    /**
     * 获取数据库基本信息
     * @param conn
     * @return
     * @throws BaseException
     */
    public DatabaseInfo getDatabaseInfo(final Connection conn) throws BaseException;

    public JdbcDriverInfo getJdbcDriverInfo(final Connection conn) throws BaseException;

    /**
     * 获取数据库信息
     * @param conn
     * @return
     * @throws BaseException
     */
    public Database getDatabase(final Connection conn) throws BaseException;

    public Database getDatabase(final Connection conn, final long retrieveRule) throws BaseException;

    /**
     * 获取所有catalog
     * @param conn 数据库连接
     * @param catalogPatternName 目录名
     * @param retrieveRule 获取catalog对象的粒度
     * @return 满足条件的所有目录
     * @throws BaseException
     */
    public Collection<String> getCatalogs(final Connection conn, final String catalogPatternName, final long retrieveRule) throws BaseException;

    public Collection<String> getCatalogs(final Connection conn, final String catalogName) throws BaseException;

    public Collection<String> getCatalogs(final Connection conn) throws BaseException;

    /**
     * 获取catalogName下所有Schema
     * @param conn 数据库连接
     * @param catalogName 目录名
     * @param schemaPatternName  模式名
     * @param retrieveRule 获取Table对象的粒度
     * @return 满足条件的所有表(视图)
     * @throws BaseException
     */
    public Collection<Schema> getSchemas(final Connection conn, final String catalogName, final String schemaPatternName, final long retrieveRule) throws BaseException;

    public Collection<Schema> getSchemas(final Connection conn, final String catalogName, final long retrieveRule) throws BaseException;

    public Collection<Schema> getSchemas(final Connection conn, final String catalogName) throws BaseException;

    public Collection<Schema> getSchemas(final Connection conn, final long retrieveRule) throws BaseException;

    public Collection<Schema> getSchemas(final Connection conn) throws BaseException;

    /**
     * 获取schema下所有表
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param tablePatternName   表过滤名
     * @param retrieveRule 获取Table对象的粒度
     * @return 满足条件的所有表、视图
     * @throws BaseException
     */
    public Collection<Table> getTables(final Connection conn, final String catalogName, final String schemaName, final String tablePatternName, final long retrieveRule) throws BaseException;

    public Collection<Table> getTables(final Connection conn, final String catalogName, final String schemaName, final long retrieveRule) throws BaseException;

    public Collection<Table> getTables(final Connection conn,final String schemaName, final long retrieveRule) throws BaseException;

    public Collection<Table> getTables(final Connection conn, final long retrieveRule) throws BaseException;

    public Collection<Table> getTables(final Connection conn, final String catalogName, final String schemaName) throws BaseException;

    public Collection<Table> getTables(final Connection conn, final String schemaName) throws BaseException;

    public Collection<Table> getTables(final Connection conn) throws BaseException;

    public Collection<Table> getTables(final Connection conn, final Schema schema, final String tablePatternName, final long retrieveRule) throws BaseException;

    public Collection<Table> getTables(final Connection conn, final Schema schema, final String tableName) throws BaseException;

    public Collection<Table> getTables(final Connection conn, final Schema schema, final long retrieveRule) throws BaseException;

    public Collection<Table> getTables(final Connection conn, final Schema schema) throws BaseException;

    /**
     * 查询表数据量SQL
     */
    public String getTableCountSQL();

    /**
     * 获取分页查询SQL
     */
    public String getTablesByPageSQL();

    /**
     * 查询表数据量
     * @param conn
     * @param schemaName
     * @return
     * @throws BaseException
     */
    public Integer getTableCount(final Connection conn,final String schemaName, final String tablePatternName) throws BaseException;

    /**
     * 获取分页查询SQL
     * @return
     * @throws BaseException
     */
    public Collection<Table> getTablesByPage(final Connection conn,final String schemaName, final String tablePatternName, Integer limit, Integer offset,final long retrieveRule) throws BaseException;

    /**
     * 获取schema下所有视图
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param viewPatternName    视图
     * @param retrieveRule 获取View对象的粒度
     * @return 满足条件的所有视图
     * @throws BaseException
     */
    public Collection<View> getViews(final Connection conn, final String catalogName, final String schemaName, final String viewPatternName, final long retrieveRule) throws BaseException;

    public Collection<View> getViews(final Connection conn, final String catalogName, final String schemaName, final long retrieveRule) throws BaseException;

    public Collection<View> getViews(final Connection conn, final String schemaName, final long retrieveRule) throws BaseException;

    public Collection<View> getViews(final Connection conn, final long retrieveRule) throws BaseException;

    public Collection<View> getViews(final Connection conn, final String catalogName, final String schemaName) throws BaseException;

    public Collection<View> getViews(final Connection conn, final String schemaName) throws BaseException;

    public Collection<View> getViews(final Connection conn) throws BaseException;

    public Collection<View> getViews(final Connection conn, final Schema schema, final String viewPatternName, final long retrieveRule) throws BaseException;

    public Collection<View> getViews(final Connection conn, final Schema schema, final String viewName) throws BaseException;

    public Collection<View> getViews(final Connection conn, final Schema schema, final long retrieveRule) throws BaseException;

    public Collection<View> getViews(final Connection conn, final Schema schema) throws BaseException;

    /**
     * 获取catalog.schema.table下所有索引
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param tableName   表名
     * @param indexName   索引名
     * @param retrieveRule 获取Index对象的粒度
     * @return 满足条件的所有索引
     * @throws BaseException
     */
    public Collection<Index> getTableIndices(final Connection conn, final String catalogName, final String schemaName, final String tableName, final String indexName, final long retrieveRule) throws BaseException;

    public Collection<Index> getTableIndices(final Connection conn, final String catalogName, final String schemaName, final String tableName, final long retrieveRule) throws BaseException;

    public Collection<Index> getTableIndices(final Connection conn, final String schemaName, final String tableName, final long retrieveRule) throws BaseException;

    public Collection<Index> getTableIndices(final Connection conn, final String tableName, final long retrieveRule) throws BaseException;

    public Collection<Index> getTableIndices(final Connection conn, final String catalogName, final String schemaName, final String tableName) throws BaseException;

    public Collection<Index> getTableIndices(final Connection conn, final String schemaName, final String tableName) throws BaseException;

    public Collection<Index> getTableIndices(final Connection conn, final String tableName) throws BaseException;

    public Collection<Index> getTableIndices(final Connection conn, final Schema schema, final String tableName, final String indexPatternName, final long retrieveRule) throws BaseException;

    public Collection<Index> getTableIndices(final Connection conn, final Schema schema, final String tableName, final String indexName) throws BaseException;

    public Collection<Index> getTableIndices(final Connection conn, final Schema schema, final String tableName) throws BaseException;

    public Collection<Index> getTableIndices(final Connection conn, final Table table, final long retrieveRule) throws BaseException;

    public Collection<Index> getTableIndices(final Connection conn, final Table table, final String indexPatternName, final long retrieveRule) throws BaseException;

    public Collection<Index> getTableIndices(final Connection conn, final Table table) throws BaseException;

    /**
     * 获取catalog.schema下所有索引
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param indexPatternName   索引名
     * @param retrieveRule 获取Index对象的粒度
     * @return 满足条件的所有索引
     * @throws BaseException
     */
    public Collection<Index> getIndices(final Connection conn, final String catalogName, final String schemaName, final String indexPatternName, final long retrieveRule) throws BaseException;

    public Collection<Index> getIndices(final Connection conn, final String catalogName, final String schemaName, final long retrieveRule) throws BaseException;

    public Collection<Index> getIndices(final Connection conn, final String schemaName, final long retrieveRule) throws BaseException;

    public Collection<Index> getIndices(final Connection conn, final long retrieveRule) throws BaseException;

    public Collection<Index> getIndices(final Connection conn, final String catalogName, final String schemaName) throws BaseException;

    public Collection<Index> getIndices(final Connection conn, final String schemaName) throws BaseException;

    public Collection<Index> getIndices(final Connection conn) throws BaseException;

    public Collection<Index> getIndices(final Connection conn, final Schema schema, final String indexPatternName, final long retrieveRule) throws BaseException;

    public Collection<Index> getIndices(final Connection conn, final Schema schema, final String indexName) throws BaseException;

    public Collection<Index> getIndices(final Connection conn, final Schema schema) throws BaseException;

    /**
     * 获取schema下所有存储过程
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param procedurePatternName   存储过程名
     * @param retrieveRule 获取Procedure对象的粒度
     * @return 满足条件的所有存储过程
     * @throws BaseException
     */
    public Collection<Procedure> getProcedures(final Connection conn, final String catalogName, final String schemaName, final String procedurePatternName, final long retrieveRule) throws BaseException;

    public Collection<Procedure> getProcedures(final Connection conn, final String catalogName, final String schemaName, final long retrieveRule) throws BaseException;

    public Collection<Procedure> getProcedures(final Connection conn, final String schemaName, final long retrieveRule) throws BaseException;

    public Collection<Procedure> getProcedures(final Connection conn, final long retrieveRule) throws BaseException;

    public Collection<Procedure> getProcedures(final Connection conn, final String catalogName, final String schemaName) throws BaseException;

    public Collection<Procedure> getProcedures(final Connection conn, final String schemaName) throws BaseException;

    public Collection<Procedure> getProcedures(final Connection conn) throws BaseException;

    public Collection<Procedure> getProcedures(final Connection conn, final Schema schema, final String procedurePatternName, final long retrieveRule) throws BaseException;

    public Collection<Procedure> getProcedures(final Connection conn, final Schema schema, final String procedureName) throws BaseException;

    public Collection<Procedure> getProcedures(final Connection conn, final Schema schema) throws BaseException;

    /**
     * 获取schema下所有函数
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param functionPatternName   存储过程名
     * @param retrieveRule 获取Function对象的粒度
     * @return 满足条件的所有函数
     * @throws BaseException
     */
    public Collection<Function> getFunctions(final Connection conn, final String catalogName, final String schemaName, final String functionPatternName, final long retrieveRule) throws BaseException;

    public Collection<Function> getFunctions(final Connection conn, final String catalogName, final String schemaName, final long retrieveRule) throws BaseException;

    public Collection<Function> getFunctions(final Connection conn, final String schemaName, final long retrieveRule) throws BaseException;

    public Collection<Function> getFunctions(final Connection conn, final long retrieveRule) throws BaseException;

    public Collection<Function> getFunctions(final Connection conn, final String catalogName, final String schemaName) throws BaseException;

    public Collection<Function> getFunctions(final Connection conn, final String schemaName) throws BaseException;

    public Collection<Function> getFunctions(final Connection conn) throws BaseException;

    public Collection<Function> getFunctions(final Connection conn, final Schema schema, final String functionPatternName, final long retrieveRule) throws BaseException;

    public Collection<Function> getFunctions(final Connection conn, final Schema schema, final String functionName) throws BaseException;

    public Collection<Function> getFunctions(final Connection conn, final Schema schema) throws BaseException;

    /**
     * 获取catalog.schema.table下所有列信息
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param tableName   存储过程/函数名
     * @param columnPatternName  存储过程/函数名
     * @param retrieveRule 获取Column对象的粒度
     * @return 满足条件的所有存储过程、函数
     * @throws BaseException
     */
    public Collection<Column> getColumns(final Connection conn, final String catalogName, final String schemaName, final String tableName, final String columnPatternName, final long retrieveRule) throws BaseException;

    public Collection<Column> getColumns(final Connection conn, final String catalogName, final String schemaName, final String tableName, final long retrieveRule) throws BaseException;

    public Collection<Column> getColumns(final Connection conn, final String schemaName, final String tableName, final long retrieveRule) throws BaseException;

    public Collection<Column> getColumns(final Connection conn, final String tableName, final long retrieveRule) throws BaseException;

    public Collection<Column> getColumns(final Connection conn, final String catalogName, final String schemaName, final String tableName) throws BaseException;

    public Collection<Column> getColumns(final Connection conn, final String schemaName, final String tableName) throws BaseException;

    public Collection<Column> getColumns(final Connection conn, final String tableName) throws BaseException;

    public Collection<Column> getColumns(final Connection conn, final Schema schema, final String tableName, final String columnPatternName, final long retrieveRule) throws BaseException;

    public Collection<Column> getColumns(final Connection conn, final Schema schema, final String tableName, final String columnName) throws BaseException;

    public Collection<Column> getColumns(final Connection conn, final Table table, final String columnName, final long retrieveRule) throws BaseException;

    public Collection<Column> getColumns(final Connection conn, final Table table, final String columnName) throws BaseException;

    public Collection<Column> getColumns(final Connection conn, final Table table, final long retrieveRule) throws BaseException;

    public Collection<Column> getColumns(final Connection conn, final Table table) throws BaseException;

    /**
     * 获取schema下所有序列
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param sequencePatternName   序列名
     * @param retrieveRule 获取Sequence对象的粒度
     * @return 满足条件的所有序列
     * @throws BaseException
     */
    public Collection<Sequence> getSequences(final Connection conn, final String catalogName, final String schemaName, final String sequencePatternName, final long retrieveRule) throws BaseException;

    public Collection<Sequence> getSequences(final Connection conn, final String catalogName, final String schemaName, final long retrieveRule) throws BaseException;

    public Collection<Sequence> getSequences(final Connection conn, final String schemaName, final long retrieveRule) throws BaseException;

    public Collection<Sequence> getSequences(final Connection conn, final long retrieveRule) throws BaseException;

    public Collection<Sequence> getSequences(final Connection conn, final String catalogName, final String schemaName) throws BaseException;

    public Collection<Sequence> getSequences(final Connection conn, final String schemaName) throws BaseException;

    public Collection<Sequence> getSequences(final Connection conn) throws BaseException;

    public Collection<Sequence> getSequences(final Connection conn, final Schema schema, final String sequenceName, final long retrieveRule) throws BaseException;

    public Collection<Sequence> getSequences(final Connection conn, final Schema schema, final String sequenceName) throws BaseException;

    public Collection<Sequence> getSequences(final Connection conn, final Schema schema) throws BaseException;

    /**
     * 按名字获取Schema对象
     * @param conn 数据库连接
     * @param catalogName 目录名
     * @param schemaPatternName  模式名
     * @param retrieveRule 获取Schema对象的粒度
     * @return 满足条件的首个Schema
     * @throws BaseException
     */
    public Schema getSchema(final Connection conn, final String catalogName, final String schemaPatternName, final long retrieveRule) throws BaseException;

    public Schema getSchema(final Connection conn, final String catalogName, final String schemaName) throws BaseException;

    public Schema getSchema(final Connection conn, final String schemaPatternName, final long retrieveRule) throws BaseException;

    public Schema getSchema(final Connection conn, final String schemaName) throws BaseException;

    public Schema getSchema(final Connection conn) throws BaseException;

    /**
     * 获取表
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param tablePatternName   表名
     * @param retrieveRule 获取Table对象的粒度
     * @return 满足条件第一个表或者视图
     * @throws BaseException
     */
    public Table getTable(final Connection conn, final String catalogName, final String schemaName, final String tablePatternName, final long retrieveRule) throws BaseException;

    public Table getTable(final Connection conn, final String schemaName, final String tablePatternName, final long retrieveRule) throws BaseException;

    public Table getTable(final Connection conn, final String tablePatternName, final long retrieveRule) throws BaseException;

    public Table getTable(final Connection conn, final String catalogName, final String schemaName, final String tableName) throws BaseException;

    public Table getTable(final Connection conn, final String schemaName, final String tableName) throws BaseException;

    public Table getTable(final Connection conn, final String tableName) throws BaseException;

    public Table getTable(final Connection conn, final Schema schema, final String tablePatternName, final long retrieveRule) throws BaseException;

    public Table getTable(final Connection conn, final Schema schema, final String tableName) throws BaseException;

    /**
     * 获取指定名称的view对象
     * @param schemaName
     * @param viewPatternName
     * @return
     * @throws BaseException
     */
    public View getView(final Connection conn, final String catalogName, final String schemaName, final String viewPatternName, final long retrieveRule) throws BaseException;

    public View getView(final Connection conn, final String schemaName, final String viewPatternName, final long retrieveRule) throws BaseException;

    public View getView(final Connection conn, final String viewPatternName, final long retrieveRule) throws BaseException;

    public View getView(final Connection conn, final String catalogName, final String schemaName, final String viewName) throws BaseException;

    public View getView(final Connection conn, final String schemaName, final String viewName) throws BaseException;

    public View getView(final Connection conn, final String viewName) throws BaseException;

    public View getView(final Connection conn, final Schema schema, final String viewPatternName, final long retrieveRule) throws BaseException;

    public View getView(final Connection conn, final Schema schema, final String viewName) throws BaseException;

    /**
     * 获取索引
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param tableName   表名
     * @param indexPatternName   索引名
     * @param retrieveRule 获取Index对象的粒度
     * @return 满足条件第一个索引
     * @throws BaseException
     */
    public Index getIndex(final Connection conn, final String catalogName, final String schemaName, final String tableName, final String indexPatternName, final long retrieveRule) throws BaseException;

    public Index getIndex(final Connection conn, final String schemaName, final String tableName, final String indexPatternName, final long retrieveRule) throws BaseException;

    public Index getIndex(final Connection conn, final String tableName, final String indexPatternName, final long retrieveRule) throws BaseException;

    public Index getIndex(final Connection conn, final String catalogName, final String schemaName, final String tableName, final String indexName) throws BaseException;

    public Index getIndex(final Connection conn, final String schemaName, final String tableName, final String indexName) throws BaseException;

    public Index getIndex(final Connection conn, final String tableName, final String indexName) throws BaseException;

    public Index getIndex(final Connection conn, final Schema schema, final String tableName, final String indexPatternName, final long retrieveRule) throws BaseException;

    public Index getIndex(final Connection conn, final Schema schema, final String tableName, final String indexName) throws BaseException;

    public Index getIndex(final Connection conn, final Table table, final String indexPatternName, final long retrieveRule) throws BaseException;

    public Index getIndex(final Connection conn, final Table table, final String indexName) throws BaseException;

    /**
     * 获取存储过程
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param procedurePatternName   存储过程名
     * @param retrieveRule 获取Procedure对象的粒度
     * @return 满足条件第一个存储过程
     * @throws BaseException
     */
    public Procedure getProcedure(final Connection conn, final String catalogName, final String schemaName, final String procedurePatternName, final long retrieveRule) throws BaseException;

    public Procedure getProcedure(final Connection conn, final String schemaName, final String procedurePatternName, final long retrieveRule) throws BaseException;

    public Procedure getProcedure(final Connection conn, final String procedurePatternName, final long retrieveRule) throws BaseException;

    public Procedure getProcedure(final Connection conn, final String catalogName, final String schemaName, final String procedureName) throws BaseException;

    public Procedure getProcedure(final Connection conn, final String schemaName, final String procedureName) throws BaseException;

    public Procedure getProcedure(final Connection conn, final String procedureName) throws BaseException;

    public Procedure getProcedure(final Connection conn, final Schema schema, final String procedurePatternName, final long retrieveRule) throws BaseException;

    public Procedure getProcedure(final Connection conn, final Schema schema, final String procedureName) throws BaseException;

    /**
     * 获取函数
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param functionPatternName   过程名
     * @param retrieveRule 获取Function对象的粒度
     * @return 满足条件第一个函数
     * @throws BaseException
     */
    public Function getFunction(final Connection conn, final String catalogName, final String schemaName, final String functionPatternName, final long retrieveRule) throws BaseException;

    public Function getFunction(final Connection conn, final String schemaName, final String functionPatternName, final long retrieveRule) throws BaseException;

    public Function getFunction(final Connection conn, final String functionPatternName, final long retrieveRule) throws BaseException;

    public Function getFunction(final Connection conn, final String catalogName, final String schemaName, final String functionName) throws BaseException;

    public Function getFunction(final Connection conn, final String schemaName, final String functionName) throws BaseException;

    public Function getFunction(final Connection conn, final String functionName) throws BaseException;

    public Function getFunction(final Connection conn, final Schema schema, final String functionPatternName, final long retrieveRule) throws BaseException;

    public Function getFunction(final Connection conn, final Schema schema, final String functionName) throws BaseException;

    /**
     * 获取过程
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param tableName   过程名
     * @param retrieveRule 获取Function对象的粒度
     * @return 满足条件第一个函数
     * @throws BaseException
     */
    public Column getColumn(final Connection conn, final String catalogName, final String schemaName, final String tableName, final String columnPatternName, final long retrieveRule) throws BaseException;

    public Column getColumn(final Connection conn, final String schemaName, final String tableName, final String columnPatternName, final long retrieveRule) throws BaseException;

    public Column getColumn(final Connection conn, final String tableName, final String columnPatternName, final long retrieveRule) throws BaseException;

    public Column getColumn(final Connection conn, final String catalogName, final String schemaName, final String tableName, final String columnName) throws BaseException;

    public Column getColumn(final Connection conn, final String schemaName, final String tableName, final String columnName) throws BaseException;

    public Column getColumn(final Connection conn, final Schema schema, final String tableName, final String columnPatternName, final long retrieveRule) throws BaseException;

    public Column getColumn(final Connection conn, final Schema schema, final String tableName, final String columnName) throws BaseException;

    public Column getColumn(final Connection conn, final Table table, final String columnPatternName, final long retrieveRule) throws BaseException;

    public Column getColumn(Connection conn, Table table, String columnName) throws BaseException;

    /**
     * 获取过程
     * @param conn 数据库连接信息
     * @param catalogName 目录名
     * @param schemaName  模式名
     * @param sequenceName   序列名
     * @param retrieveRule 获取Sequence对象的粒度
     * @return 满足条件第一个序列
     * @throws BaseException
     */
    public Sequence getSequence(final Connection conn, final String catalogName, final String schemaName, final String sequenceName, final long retrieveRule) throws BaseException;

    public Sequence getSequence(final Connection conn, final String schemaName, final String sequenceName, final long retrieveRule) throws BaseException;

    public Sequence getSequence(final Connection conn, final String sequenceName, final long retrieveRule) throws BaseException;

    public Sequence getSequence(final Connection conn, final String catalogName, final String schemaName, final String sequenceName) throws BaseException;

    public Sequence getSequence(final Connection conn, final String schemaName, final String sequenceName) throws BaseException;

    public Sequence getSequence(final Connection conn, final String sequenceName) throws BaseException;

    public Sequence getSequence(final Connection conn, final Schema schema, final String sequenceName, final long retrieveRule) throws BaseException;

    public Sequence getSequence(final Connection conn, final Schema schema, final String sequenceName) throws BaseException;

    public BigInteger nextValue(final Connection conn, final String catalogName, final String schemaName, final String sequenceName) throws BaseException;

    public BigInteger nextValue(final Connection conn, final String schemaName, final String sequenceName) throws BaseException;

    public BigInteger nextValue(final Connection conn, final String sequenceName) throws BaseException;

    public BigInteger nextValue(final Connection conn, final Sequence sequence) throws BaseException;

    public BigInteger currValue(final Connection conn, final String catalogName, final String schemaName, final String sequenceName) throws BaseException;

    public BigInteger currValue(final Connection conn, final String schemaName, final String sequenceName) throws BaseException;

    public BigInteger currValue(final Connection conn, final String sequenceName) throws BaseException;

    public BigInteger currValue(final Connection conn, final Sequence sequence) throws BaseException;

    public void restartValue(final Connection conn, final String catalogName, final String schemaName, final String sequenceName, BigInteger restartValue) throws BaseException;

    public void restartValue(final Connection conn, final String schemaName, final String sequenceName, BigInteger restartValue) throws BaseException;

    public void restartValue(final Connection conn, final String sequenceName, BigInteger restartValue) throws BaseException;

    public void restartValue(final Connection conn, final Sequence sequence, BigInteger restartValue) throws BaseException;

    /**
     * 获取指定函数的所有参数
     * @param conn
     * @param catalogName
     * @param schemaName
     * @return
     * @throws BaseException
     */
    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn, final String catalogName, final String schemaName, final String procedureName, final long retrieveRule) throws BaseException;

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn, final Schema schema, final String procedureName, final long retrieveRule) throws BaseException;

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn, final Schema schema, final String procedureName) throws BaseException;

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn, final Procedure procedure) throws BaseException;

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn, final Procedure procedure, final long retrieveRule) throws BaseException;

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn, final String procedureName) throws BaseException;

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn, final String procedureName, final long retrieveRule) throws BaseException;

    /**
     * 获取指定函数的所有参数
     * @param conn
     * @param catalogName
     * @param schemaName
     * @return
     * @throws BaseException
     */
    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final String catalogName, final String schemaName, final String functionName, final long retrieveRule) throws BaseException;

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final Schema schema, final String functionName, final long retrieveRule) throws BaseException;

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final Schema schema, final String functionName) throws BaseException;

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final Function function) throws BaseException;

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final Function function, final long retrieveRule) throws BaseException;

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final String functionName) throws BaseException;

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final String functionName, final long retrieveRule) throws BaseException;

    /**
     * 获取表所有铸件列
     * @param conn
     * @param table
     * @param retrieveRule
     * @return
     */
    public PrimaryKey getPrimaryKey(final Connection conn, final Table table, final long retrieveRule) throws BaseException;

    public PrimaryKey getPrimaryKey(final Connection conn, final Table table) throws BaseException;

    public Collection<ForeignKey> getForeignKeys(final Connection conn, final Table table, final long retrieveRule) throws BaseException;

    public Collection<ForeignKey> getForeignKeys(final Connection conn, final Table table) throws BaseException;

    public Collection<ForeignKey> getImportedKeys(final Connection conn, final Table table, final long retrieveRule) throws BaseException;

    public Collection<ForeignKey> getImportedKeys(final Connection conn, final Table table) throws BaseException;

    public Collection<ForeignKey> getExportedKeys(final Connection conn, final Table table, final long retrieveRule) throws BaseException;

    public Collection<ForeignKey> getExportedKeys(final Connection conn, final Table table) throws BaseException;

    /**
     * 获取用户定义的所有数据类型
     * @param conn
     * @param schema
     * @return
     * @throws BaseException
     */
    public Collection<ColumnDataType> getUserDataTypes(final Connection conn, final Schema schema) throws BaseException;

    public Collection<String> getDBCharSet(final Connection conn) throws BaseException;

    public Collection<ResultsColumn> getColumnsByQuery(final Connection conn, final String vSQL) throws BaseException;

    public Collection<Column> getColumnsByView(final Connection conn, final String vSQL) throws BaseException;

    public boolean validateSQL(final Connection conn, final String sqlString) throws BaseException;

    public boolean validateDDL(final Connection conn, final String sqlString) throws BaseException;

    public boolean validateDQL(final Connection conn, final String sqlString) throws BaseException;

    public boolean validateDML(final Connection conn, final String sqlString) throws BaseException;

    /**
     * 执行DDL SQL语句 包括创建表、索引、视图，删除表、索引、视图
     * @param conn
     * @param sql SQL Statement
     * @throws BaseException
     */
    public Boolean executeDDL(final Connection conn, final String sql) throws BaseException;

    public Boolean executeDDL(final Connection conn, final String sql, final Object[] args) throws BaseException;

    /**
     * 执行DDL SQL语句 包括创建表、索引、视图，删除表、索引、视图
     * @param conn
     * @param sql SQL Statement
     * @throws BaseException
     */
    public <T> T executeDQL(final Connection conn, final String sql, final Object[] args, ResultSetHandler<T> handler) throws BaseException;

    public <T> T executeDQL(final Connection conn, final String sql, ResultSetHandler<T> handler) throws BaseException;

    /**
     * 执行DDL SQL语句 包括创建表、索引、视图，删除表、索引、视图
     * @param conn
     * @param sql SQL Statement
     * @throws BaseException
     */
    public Integer executeDML(final Connection conn, final String sql, final Object[] args) throws BaseException;

    public Integer executeDML(final Connection conn, final String sql) throws BaseException;

    /*
     * 针对只执行，且不知道执行的是什么类型的语句，且不需要结果返回的
     */
    public void execute(final Connection conn, final String sql) throws BaseException;

    public void execute(final Connection conn, final String sql, final Object[] args) throws BaseException;

    public Map<ProcedureColumn, Object> callPorcedure(final Connection conn, final Procedure procedure, final Object[] parameters) throws BaseException;

    public int getJdbcType(final int javaType);

    public int[] getCandidateJdbcTypes(final int javaType);

    public int getJavaType(final int jdbcType);

    public int[] getCandidateJavaTypes(final int jdbcType);

    public String getJdbcTypeName(final int jdbcType);

    public String getJavaTypeName(final int javaType);

    public int getJdbcType(final String jdbcTypeName);

    public int getJavaType(final String javaTypeName);

}
