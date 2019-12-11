package mark.component.dbdialect.def;

import mark.component.dbmodel.constans.RetrieveRule;
import mark.component.dbmodel.constans.SearchableType;
import mark.component.dbmodel.constans.TableType;
import mark.component.dbmodel.model.*;
import mark.component.dbmodel.util.MetadataResultSet;
import mark.component.core.exception.BaseException;
import mark.component.dbdialect.MetaDialect;
import mark.component.dbdialect.util.TypeResolver;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author liang
 * @milestone 2013年10月19日
 */
public abstract class DefaultMetaDialect implements MetaDialect {

    protected TypeResolver typeResolver;
    protected final DatabaseInfo databaseInfo;
    protected final Map<String, List<ColumnDataType>> columnDataTypeMap = new HashMap<String, List<ColumnDataType>>();
    protected final Map<Integer, List<ColumnDataType>> jdbcTypeMap = new HashMap<Integer, List<ColumnDataType>>();
    protected final List<ColumnDataType> jdbcTypes = new ArrayList<ColumnDataType>();
    protected final List<String> keywords = new ArrayList<String>();
    protected Map<String, SystemFunction> systemFunctions;
    protected final Map<String, String> schemaInfomations = new HashMap<String, String>();
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMetaDialect.class);

    protected DefaultMetaDialect() {
        databaseInfo = resolveDatabaseInfo();
        try {
            typeResolver = new TypeResolver(getJdbcTypeFileName(), getJava2JdbcFileName(), getJdbc2JavaFileName());
        } catch (IOException ex) {
        }
    }

    protected abstract String getSchemaInfomationFileName();

    protected abstract String getJdbcTypeFileName();

    protected abstract String getJava2JdbcFileName();

    protected abstract String getJdbc2JavaFileName();

    protected abstract String getKeywordsFileName();

    protected abstract String getSystemFunctionsFileName();

    protected abstract DatabaseInfo resolveDatabaseInfo();

    public char openQuote() {
        return '"';
    }

    public char closeQuote() {
        return '"';
    }

    public String quote(String name) {
        if (name == null) {
            return null;
        }
        if (name.charAt(0) != openQuote()
                || name.charAt(name.length() - 1) != closeQuote()) {
            name = openQuote() + name + closeQuote();
        }
        return name;
    }

    public String quote(final String name, boolean quote) {
        if (quote) {
            return quote(name);
        }
        return name;
    }

    public String unquote(String name) {
        if (name == null || name.length() < 2) {
            return name;
        }

        if (name.charAt(0) == openQuote()
                && name.charAt(name.length() - 1) == closeQuote()) {
            name = name.substring(1, name.length() - 1);
        }
        return name;
    }

    public boolean equal(String aName, String bName) throws BaseException {
        if (aName == null && bName == null) {
            return true;
        } else if (aName == null || bName == null) {
            return false;
        }
        aName = unquote(aName);
        bName = unquote(bName);
        return aName.equals(bName);
    }

    public String getInformationSchemaSQL(String key) {
        return schemaInfomations.get(key);
    }

    public String getDefaultCatalog(final Connection conn)
            throws BaseException {
        return null;
    }

    public void setDefaultCatalog(final Connection conn,
            final String catalogName) throws BaseException {
    }

    @Override
    public Schema getDefaultSchema(Connection conn) throws BaseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDefaultSchemaName(final Connection conn)
            throws BaseException {
        Schema schema = getDefaultSchema(conn);
        if (schema != null) {
            return schema.getName();
        }
        return null;
    }

    @Override
    public void setDefaultSchema(Connection conn, String name)
            throws BaseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }

    protected Properties getConfigProperties() throws BaseException {
        Properties properties = new Properties();
        try(InputStream inputStream = DefaultMetaDialect.class.getResourceAsStream(getSchemaInfomationFileName())) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException ex) {
            throw new BaseException("",ex);
        }

        return properties;
    }

    public DatabaseInfo getDatabaseInfo(final Connection conn)
            throws BaseException {
        DatabaseInfoRetriever infoRetriever = new DatabaseInfoRetriever(conn);
        return infoRetriever.retrieveDatabaseInfo();
    }

    public JdbcDriverInfo getJdbcDriverInfo(final Connection conn)
            throws BaseException {
        DatabaseInfoRetriever infoRetriever = new DatabaseInfoRetriever(conn);
        return infoRetriever.retrieveJdbcDriverInfo();
    }

    @Override
    public Database getDatabase(final Connection conn) throws BaseException {
        final Database database = new Database(getUserName(conn));
        database.setDatabaseInfo(getDatabaseInfo(conn));
        database.setJdbcDriverInfo(getJdbcDriverInfo(conn));
        return database;
    }

    public Database getDatabase(final Connection conn, long retrieveRule)
            throws BaseException {
        final Database database = new Database(getUserName(conn));
        database.setDatabaseInfo(getDatabaseInfo(conn));
        database.setJdbcDriverInfo(getJdbcDriverInfo(conn));
        if (retrieveRule > RetrieveRule.DEFAULT) {
            retrieveRule |= RetrieveRule.SCHEMA;
        } else {
            return database;
        }
        Collection<Schema> schemas = getSchemas(conn, retrieveRule);
        database.addSchemas(schemas);
        for (Schema schema : schemas) {

            if (RetrieveRule.TABLE == (retrieveRule & RetrieveRule.TABLE)) {
                Collection<Table> tables = getTables(conn, schema, retrieveRule);
                database.addTables(tables);
            }

            if (RetrieveRule.VIEW == (retrieveRule & RetrieveRule.VIEW)) {
                Collection<View> views = getViews(conn, schema, retrieveRule);
                database.addViews(views);
            }

            if (RetrieveRule.PROCEDURE == (retrieveRule & RetrieveRule.PROCEDURE)) {
                RoutineRetriever routineRetriever = new RoutineRetriever(conn);
                Collection<Procedure> procedures = routineRetriever.retrieveProcedures(schema.getCatalogName(),
                        schema.getName(), "*", retrieveRule);
                database.addProcedures(procedures);
            }

            if (RetrieveRule.FUNCTION == (retrieveRule & RetrieveRule.FUNCTION)) {
                RoutineRetriever routineRetriever = new RoutineRetriever(conn);
                Collection<Function> functions = routineRetriever.retrieveFunctions(schema.getCatalogName(),
                        schema.getName(), "*", retrieveRule);
                database.addFunctions(functions);
            }

            if (RetrieveRule.SEQUENCE == (retrieveRule & RetrieveRule.SEQUENCE)) {
                Collection<Sequence> sequences = getSequences(conn, schema);
                database.addSequences(sequences);
            }
        }

        return database;
    }

    public Collection<String> getCatalogs(final Connection conn, String catalogPatternName, final long retrieveRule)
            throws BaseException {
        final List<String> catalogs = new ArrayList<String>();
        boolean supportCatalog = supportCatalog();
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        boolean regex = RetrieveRule.REGEX == (RetrieveRule.REGEX & retrieveRule);
        boolean all = false;
        catalogPatternName = unquote(catalogPatternName);
        Pattern pattern = null;
        if (catalogPatternName == null || catalogPatternName.isEmpty()) {
            all = true;
        } else if (regex) {
            pattern = Pattern.compile(catalogPatternName);
            catalogPatternName = null;
        } else {
            catalogPatternName = unquote(catalogPatternName);
        }
        if (supportCatalog) {
            try {
                ResultSet rs = conn.getMetaData().getCatalogs();
                while (rs.next()) {
                    final String catalog = rs.getString(1);
                    if (all) {
                    } else if (regex) {
                        Matcher matcher = pattern.matcher(catalog);
                        if (!matcher.matches()) {
                            continue;
                        }
                    } else {
                        if (!catalogPatternName.equals(catalog)) {
                            continue;
                        }
                    }
                    catalogs.add(quote(catalog, quoted));
                }
            } catch (SQLException ex) {
                throw new BaseException(ex);
            }
        }
        return catalogs;
    }

    public Collection<String> getCatalogs(final Connection conn, final String catalogName) throws BaseException {
        return getCatalogs(conn, catalogName, RetrieveRule.DEFAULT);
    }

    public Collection<String> getCatalogs(final Connection conn) throws BaseException {
        return getCatalogs(conn, null, RetrieveRule.DEFAULT);
    }

    public Collection<Schema> getSchemas(final Connection conn,
            String catalogName, String schemaPatternName, final long retrieveRule)
            throws BaseException {

        List<Schema> schemaRefs = new ArrayList<Schema>();

        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        boolean regex = RetrieveRule.REGEX == (RetrieveRule.REGEX & retrieveRule);
        boolean all = false;
        Pattern pattern = null;
        boolean supportsSchema = supportSchema();
        boolean supportsCatalog = supportCatalog();

        if (!supportsCatalog) {
            catalogName = null;
        }
        if (schemaPatternName == null || schemaPatternName.isEmpty()) {
            all = true;
        } else {
            schemaPatternName = unquote(schemaPatternName);
            if (regex) {
                pattern = Pattern.compile(schemaPatternName);
            }
        }

        if (supportsSchema) {
            MetadataResultSet results = null;
            try {
                results = new MetadataResultSet(conn.getMetaData().getSchemas());
                while (results.next()) {
                    final String schemaName = results.getString("TABLE_SCHEM");
                    if (all) {
                    } else if (regex) {
                        Matcher matcher = pattern.matcher(schemaName);
                        if (!matcher.matches()) {
                            continue;
                        }
                    } else {
                        if (!schemaPatternName.equals(schemaName)) {
                            continue;
                        }
                    }
                    if (supportsCatalog) {
                        // final String cataName =
                        // quote(results.getString("TABLE_CATALOG"), quoted);
                        catalogName = unquote(catalogName);
                        final String cataName = results.getString("TABLE_CATALOG");
                        if (catalogName == null || cataName.equals(catalogName)) {
                            schemaRefs.add(new Schema(quote(cataName, quoted), quote(schemaName, quoted)));
                        }
                    } else {
                        schemaRefs.add(new Schema(null, quote(schemaName, quoted)));
                    }
                }
            } catch (final SQLException ex) {
                throw new BaseException(ex);
            } finally {
                try {
                    if (results != null) {
                        results.close();
                    }
                } catch (SQLException ex) {
                }
            }
        }
        return schemaRefs;
    }

    public Collection<Schema> getSchemas(final Connection conn, final String catalogName, final long retrieveRule)
            throws BaseException {
        return getSchemas(conn, catalogName, null, retrieveRule);
    }

    public Collection<Schema> getSchemas(final Connection conn, final String catalogName) throws BaseException {
        return getSchemas(conn, catalogName, null, RetrieveRule.DEFAULT);
    }

    public Collection<Schema> getSchemas(final Connection conn, final long retrieveRule) throws BaseException {
        return getSchemas(conn, getDefaultCatalog(conn), null, retrieveRule);
    }

    public Collection<Schema> getSchemas(final Connection conn) throws BaseException {
        return getSchemas(conn, getDefaultCatalog(conn), null, RetrieveRule.DEFAULT);
    }

    @Override
    public Integer getTableCount(
            final Connection conn,final String schemaName,final String tablePatternName
    ) throws BaseException {
        TableRetriever tableRetriever = new TableRetriever(conn);
        Integer tablesCount = tableRetriever.getTablesCount(schemaName, tablePatternName, this.getTableCountSQL());
        return tablesCount;
    }

    @Override
    public synchronized Collection<Table> getTablesByPage(
            final Connection conn,final String schemaName,
            final String tablePatternName, Integer limit, Integer offset,final long retrieveRule
    ) throws BaseException {
        TableRetriever tableRetriever = new TableRetriever(conn);
        Collection<Table> tables = tableRetriever.retrieveTablesByPage(schemaName, tablePatternName, this.getTablesByPageSQL(), limit, offset, retrieveRule);
        if (RetrieveRule.COLUMN == (retrieveRule & RetrieveRule.COLUMN)) {
            for (Table table : tables) {
                Collection<Column> columns = tableRetriever.retrieveColumns(table, retrieveRule);
                if (RetrieveRule.PRIMARY_KEY == (retrieveRule & RetrieveRule.PRIMARY_KEY)) {
                    tableRetriever.retrievePrimaryKey(table, retrieveRule);
                }
            }
        }
        return tables;
    }

    /*
     * 经oracle jdbc驱动源代码分析可知，对应oracle：schemaName，tablePatternName都可以应用like子句指定的表达式
     *  select * from all_objects t WHERE o.owner LIKE :1 ESCAPE '/'  AND o.object_name LIKE :2 ESCAPE '/'
     */
    public synchronized Collection<Table> getTables(final Connection conn,
            final String catalogName, final String schemaName,
            final String tablePatternName, final long retrieveRule)
            throws BaseException {

        TableRetriever tableRetriever = new TableRetriever(conn);
        Collection<Table> tables = tableRetriever.retrieveTables(catalogName,
                schemaName, tablePatternName,
                Collections.singleton(TableType.table), retrieveRule);
        if (RetrieveRule.COLUMN == (retrieveRule & RetrieveRule.COLUMN)) {
            for (Table table : tables) {
                Collection<Column> columns = tableRetriever.retrieveColumns(table, retrieveRule);
                if (RetrieveRule.PRIMARY_KEY == (retrieveRule & RetrieveRule.PRIMARY_KEY)) {
                    tableRetriever.retrievePrimaryKey(table, retrieveRule);
                }
            }
        }
        /*
        if (RetrieveRule.INDEX == (retrieveRule & RetrieveRule.INDEX)) {
            for (Table table : tables) {
                Collection<Index> indices = tableRetriever.retrieveIndices(table, null, false, retrieveRule);
            }
        }
        if (RetrieveRule.FORGIGN_KEY == (retrieveRule & RetrieveRule.FORGIGN_KEY)) {
            for (Table table : tables) {
                NamedObjectList<ForeignKey> foreignKeys = tableRetriever.retrieveForeignKeys(table, retrieveRule);
            }
        }
        if (RetrieveRule.CONSTRAINT == (retrieveRule & RetrieveRule.CONSTRAINT)) {
            for (Table table : tables) {
                NamedObjectList<ForeignKey> foreignKeys = tableRetriever.retrieveForeignKeys(table, retrieveRule);
            }
        }*/

        return tables;
    }

    public Collection<Table> getTables(final Connection conn,
            final String catalogName, final String schemaName, final long retrieveRule)
            throws BaseException {
        return getTables(conn, catalogName, schemaName, null, retrieveRule);
    }

    public Collection<Table> getTables(final Connection conn, final String schemaName, final long retrieveRule)
            throws BaseException {
        return getTables(conn, getDefaultCatalog(conn), schemaName, null, retrieveRule);
    }

    public Collection<Table> getTables(final Connection conn, final long retrieveRule) throws BaseException {
        return getTables(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), null, retrieveRule);
    }

    public Collection<Table> getTables(final Connection conn, final String catalogName, final String schemaName)
            throws BaseException {
        return getTables(conn, catalogName, schemaName, null, RetrieveRule.DEFAULT);
    }

    public Collection<Table> getTables(final Connection conn, final String schemaName) throws BaseException {
        return getTables(conn, getDefaultCatalog(conn), schemaName, null, RetrieveRule.DEFAULT);
    }

    public Collection<Table> getTables(final Connection conn) throws BaseException {
        return getTables(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), null, RetrieveRule.DEFAULT);
    }

    public Collection<Table> getTables(final Connection conn, final Schema schema, final String tableName, final long retrieveRule)
            throws BaseException {
        return getTables(conn, schema.getCatalogName(), schema.getName(), tableName, retrieveRule);
    }

    public Collection<Table> getTables(final Connection conn, final Schema schema, final String tableName) throws BaseException {
        return getTables(conn, schema.getCatalogName(), schema.getName(), tableName, RetrieveRule.DEFAULT);
    }

    public Collection<Table> getTables(final Connection conn, final Schema schema, final long retrieveRule) throws BaseException {
        return getTables(conn, schema.getCatalogName(), schema.getName(), null, retrieveRule);
    }

    public Collection<Table> getTables(final Connection conn, final Schema schema) throws BaseException {
        return getTables(conn, schema.getCatalogName(), schema.getName(), null, RetrieveRule.DEFAULT);
    }

    @Override
    public String getTableCountSQL() {
        return null;
    }

    @Override
    public String getTablesByPageSQL(){
        return null;
    }

    public Collection<View> getViews(final Connection conn,
                                     final String catalogName, final String schemaName, final String viewPatternName, final long retrieveRule)
            throws BaseException {

        TableRetriever tableRetriever = new TableRetriever(conn);
        Collection<View> views = tableRetriever.retrieveViews(catalogName, schemaName, viewPatternName, retrieveRule);
        if (RetrieveRule.COLUMN == (retrieveRule & RetrieveRule.COLUMN)) {
            for (View table : views) {
                Collection<Column> columns = tableRetriever.retrieveColumns(table, retrieveRule);
            }
        }
        return views;
    }

    public Collection<View> getViews(final Connection conn,
            final String catalogName, final String schemaName, final long retrieveRule)
            throws BaseException {
        return getViews(conn, catalogName, schemaName, null, retrieveRule);
    }

    public Collection<View> getViews(final Connection conn, final String schemaName, final long retrieveRule)
            throws BaseException {
        return getViews(conn, getDefaultCatalog(conn), schemaName, null, retrieveRule);
    }

    public Collection<View> getViews(final Connection conn, final long retrieveRule) throws BaseException {
        return getViews(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), null, retrieveRule);
    }

    public Collection<View> getViews(final Connection conn, final String catalogName, final String schemaName)
            throws BaseException {
        return getViews(conn, catalogName, schemaName, null, RetrieveRule.DEFAULT);
    }

    public Collection<View> getViews(final Connection conn, final String schemaName) throws BaseException {
        return getViews(conn, getDefaultCatalog(conn), schemaName, null, RetrieveRule.DEFAULT);
    }

    public Collection<View> getViews(final Connection conn) throws BaseException {
        return getViews(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), null, RetrieveRule.DEFAULT);
    }

    public Collection<View> getViews(final Connection conn,
            final Schema schema, final String viewName, final long retrieveRule)
            throws BaseException {
        return getViews(conn, schema.getCatalogName(), schema.getName(), null, retrieveRule);
    }

    public Collection<View> getViews(final Connection conn, final Schema schema, final String viewName)
            throws BaseException {
        return getViews(conn, schema.getCatalogName(), schema.getName(), viewName, RetrieveRule.DEFAULT);
    }

    public Collection<View> getViews(final Connection conn, final Schema schema, final long retrieveRule)
            throws BaseException {
        return getViews(conn, schema.getCatalogName(), schema.getName(), null, retrieveRule);
    }

    public Collection<View> getViews(final Connection conn, final Schema schema) throws BaseException {
        return getViews(conn, schema.getCatalogName(), schema.getName(), null, RetrieveRule.DEFAULT);
    }

    public Collection<Index> getTableIndices(final Connection conn,
            String catalogName, String schemaName, String tableName, String indexPatternName, final long retrieveRule)
            throws BaseException {
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);

        catalogName = quote(unquote(catalogName), quoted);
        schemaName = quote(unquote(schemaName), quoted);
        tableName = quote(unquote(tableName), quoted);

        final Schema schema = new Schema(catalogName, schemaName);
        final Table table = new Table(schema, tableName);

        TableRetriever tableRetriever = new TableRetriever(conn);
        tableRetriever.retrieveIndices(table, indexPatternName, false, retrieveRule);

        return table.getIndices();
    }

    public Collection<Index> getTableIndices(final Connection conn,
            final String catalogName, final String schemaName, final String tableName, final long retrieveRule)
            throws BaseException {
        return getTableIndices(conn, catalogName, schemaName, tableName, null, retrieveRule);
    }

    public Collection<Index> getTableIndices(final Connection conn,
            final String schemaName, final String tableName, final long retrieveRule)
            throws BaseException {
        return getTableIndices(conn, getDefaultCatalog(conn), schemaName, tableName, null, retrieveRule);
    }

    public Collection<Index> getTableIndices(final Connection conn, final String tableName, final long retrieveRule)
            throws BaseException {
        return getTableIndices(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), tableName, null, retrieveRule);
    }

    public Collection<Index> getTableIndices(final Connection conn,
            final String catalogName, final String schemaName, final String tableName)
            throws BaseException {
        return getTableIndices(conn, catalogName, schemaName, tableName, null, RetrieveRule.DEFAULT);
    }

    public Collection<Index> getTableIndices(final Connection conn, final String schemaName, final String tableName)
            throws BaseException {
        return getTableIndices(conn, getDefaultCatalog(conn), schemaName, tableName, null, RetrieveRule.DEFAULT);
    }

    public Collection<Index> getTableIndices(final Connection conn, final String tableName) throws BaseException {
        return getTableIndices(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), tableName, null, RetrieveRule.DEFAULT);
    }

    public Collection<Index> getTableIndices(final Connection conn,
            final Schema schema, final String tableName, final String indexName, final long retrieveRule)
            throws BaseException {
        return getTableIndices(conn, schema.getCatalogName(), schema.getName(), tableName, indexName, retrieveRule);
    }

    public Collection<Index> getTableIndices(final Connection conn,
            final Schema schema, final String tableName, final String indexName)
            throws BaseException {
        return getTableIndices(conn, schema.getCatalogName(), schema.getName(), tableName, indexName, RetrieveRule.DEFAULT);
    }

    public Collection<Index> getTableIndices(final Connection conn, final Schema schema, final String tableName)
            throws BaseException {
        return getTableIndices(conn, schema.getCatalogName(), schema.getName(), tableName, null, RetrieveRule.DEFAULT);
    }

    public Collection<Index> getTableIndices(final Connection conn, final Table table, final long retrieveRule)
            throws BaseException {
        Schema schema = table.getSchema();
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }

        return getTableIndices(conn, catalogName, schemaName, table.getName(), null, retrieveRule);
    }

    public Collection<Index> getTableIndices(final Connection conn,
            final Table table, final String indexPatternName, final long retrieveRule)
            throws BaseException {
        Schema schema = table.getSchema();
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }

        return getTableIndices(conn, catalogName, schemaName, table.getName(), indexPatternName, retrieveRule);
    }

    public Collection<Index> getTableIndices(final Connection conn, final Table table) throws BaseException {
        Schema schema = table.getSchema();
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }

        return getTableIndices(conn, catalogName, schemaName, table.getName(), null, RetrieveRule.DEFAULT);
    }

    public Collection<Index> getIndices(final Connection conn,
            final String catalogName, final String schemaName, final String indexPatternName, final long retrieveRule)
            throws BaseException {
        List<Index> indices = new ArrayList<Index>();
        Collection<Table> tables = getTables(conn, catalogName, schemaName, retrieveRule);
        for (Table table : tables) {
            indices.addAll(getTableIndices(conn, table, indexPatternName, retrieveRule));
        }
        return indices;
    }

    public Collection<Index> getIndices(final Connection conn,
            final String catalogName, final String schemaName, final long retrieveRule)
            throws BaseException {
        return getIndices(conn, catalogName, schemaName, null, retrieveRule);
    }

    public Collection<Index> getIndices(final Connection conn,
            final String schemaName, final long retrieveRule)
            throws BaseException {
        return getIndices(conn, getDefaultCatalog(conn), schemaName, null, retrieveRule);
    }

    public Collection<Index> getIndices(final Connection conn, final long retrieveRule) throws BaseException {
        return getIndices(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), null, retrieveRule);
    }

    public Collection<Index> getIndices(final Connection conn,
            final String catalogName, final String schemaName)
            throws BaseException {
        return getIndices(conn, catalogName, schemaName, null, RetrieveRule.DEFAULT);
    }

    public Collection<Index> getIndices(final Connection conn, final String schemaName) throws BaseException {
        return getIndices(conn, getDefaultCatalog(conn), schemaName, null, RetrieveRule.DEFAULT);
    }

    public Collection<Index> getIndices(final Connection conn) throws BaseException {
        return getIndices(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), null, RetrieveRule.DEFAULT);
    }

    public Collection<Index> getIndices(final Connection conn,
            final Schema schema, final String indexName, final long retrieveRule)
            throws BaseException {
        return getIndices(conn, schema.getCatalogName(), schema.getName(), indexName, retrieveRule);
    }

    public Collection<Index> getIndices(final Connection conn,
            final Schema schema, final String indexName)
            throws BaseException {
        return getIndices(conn, schema.getCatalogName(), schema.getName(), indexName, RetrieveRule.DEFAULT);
    }

    public Collection<Index> getIndices(final Connection conn, final Schema schema) throws BaseException {
        return getIndices(conn, schema.getCatalogName(), schema.getName(), null, RetrieveRule.DEFAULT);
    }

    public Collection<Procedure> getProcedures(final Connection conn,
            final String catalogName, final String schemaName,
            final String procedurePatternName, final long retrieveRule)
            throws BaseException {
        RoutineRetriever routineRetriever = new RoutineRetriever(conn);

        Collection<Procedure> procedures = routineRetriever.retrieveProcedures(catalogName, schemaName, procedurePatternName, retrieveRule);

        if (RetrieveRule.COLUMN == (RetrieveRule.COLUMN & retrieveRule)) {
            for (Procedure procedure : procedures) {
                routineRetriever.retrieveProcedureColumns(procedure, retrieveRule);
            }
        }
        return procedures;
    }

    public Collection<Procedure> getProcedures(final Connection conn,
            final String catalogName, final String schemaName, final long retrieveRule)
            throws BaseException {
        return getProcedures(conn, catalogName, schemaName, null, retrieveRule);
    }

    public Collection<Procedure> getProcedures(final Connection conn, final String schemaName, final long retrieveRule)
            throws BaseException {
        return getProcedures(conn, getDefaultCatalog(conn), schemaName, null, retrieveRule);
    }

    public Collection<Procedure> getProcedures(final Connection conn, final long retrieveRule) throws BaseException {
        return getProcedures(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), null, retrieveRule);
    }

    public Collection<Procedure> getProcedures(final Connection conn, final String catalogName, final String schemaName)
            throws BaseException {
        return getProcedures(conn, catalogName, schemaName, null, RetrieveRule.DEFAULT);
    }

    public Collection<Procedure> getProcedures(final Connection conn, final String schemaName) throws BaseException {
        return getProcedures(conn, getDefaultCatalog(conn), schemaName, null, RetrieveRule.DEFAULT);
    }

    public Collection<Procedure> getProcedures(final Connection conn) throws BaseException {
        return getProcedures(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), null, RetrieveRule.DEFAULT);
    }

    public Collection<Procedure> getProcedures(final Connection conn,
            final Schema schema, final String procedurePatternName, final long retrieveRule)
            throws BaseException {
        return getProcedures(conn, schema.getCatalogName(), schema.getName(), procedurePatternName, retrieveRule);
    }

    public Collection<Procedure> getProcedures(final Connection conn, final Schema schema, final String procedureName)
            throws BaseException {
        return getProcedures(conn, schema.getCatalogName(), schema.getName(), procedureName, RetrieveRule.DEFAULT);
    }

    public Collection<Procedure> getProcedures(final Connection conn, final Schema schema) throws BaseException {
        return getProcedures(conn, schema.getCatalogName(), schema.getName(), null, RetrieveRule.DEFAULT);
    }

    public Collection<Function> getFunctions(final Connection conn,
            final String catalogName, final String schemaName,
            final String functionPatternName, final long retrieveRule)
            throws BaseException {
        RoutineRetriever routineRetriever = new RoutineRetriever(conn);

        Collection<Function> functions = routineRetriever.retrieveFunctions(catalogName, schemaName, functionPatternName, retrieveRule);
        if (RetrieveRule.COLUMN == (RetrieveRule.COLUMN & retrieveRule)) {
            for (Function function : functions) {
                routineRetriever.retrieveFunctionColumns(function, retrieveRule);
            }
        }

        return functions;
    }

    public Collection<Function> getFunctions(final Connection conn,
            final String catalogName, final String schemaName,
            final long retrieveRule) throws BaseException {
        return getFunctions(conn, catalogName, schemaName, null, retrieveRule
                | RetrieveRule.REGEX);
    }

    public Collection<Function> getFunctions(final Connection conn,
            final String schemaName, final long retrieveRule)
            throws BaseException {
        return getFunctions(conn, getDefaultCatalog(conn), schemaName, null,
                retrieveRule | RetrieveRule.REGEX);
    }

    public Collection<Function> getFunctions(final Connection conn,
            final long retrieveRule) throws BaseException {
        return getFunctions(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), null, retrieveRule
                | RetrieveRule.REGEX);
    }

    public Collection<Function> getFunctions(final Connection conn,
            final String catalogName, final String schemaName)
            throws BaseException {
        return getFunctions(conn, catalogName, schemaName, null,
                RetrieveRule.DEFAULT | RetrieveRule.REGEX);
    }

    public Collection<Function> getFunctions(final Connection conn,
            final String schemaName) throws BaseException {
        return getFunctions(conn, getDefaultCatalog(conn), schemaName, null,
                RetrieveRule.DEFAULT | RetrieveRule.REGEX);
    }

    public Collection<Function> getFunctions(final Connection conn)
            throws BaseException {
        return getFunctions(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), null, RetrieveRule.DEFAULT
                | RetrieveRule.REGEX);
    }

    public Collection<Function> getFunctions(final Connection conn,
            final Schema schema, final String functionPatternName,
            final long retrieveRule) throws BaseException {
        return getFunctions(conn, schema.getCatalogName(), schema.getName(),
                functionPatternName, retrieveRule | RetrieveRule.REGEX);
    }

    public Collection<Function> getFunctions(final Connection conn,
            final Schema schema, final String functionName)
            throws BaseException {
        return getFunctions(conn, schema.getCatalogName(), schema.getName(),
                functionName, RetrieveRule.DEFAULT);
    }

    public Collection<Function> getFunctions(final Connection conn,
            final Schema schema) throws BaseException {
        return getFunctions(conn, schema.getCatalogName(), schema.getName(),
                null, RetrieveRule.DEFAULT | RetrieveRule.REGEX);
    }

    public Collection<Column> getColumns(final Connection conn,
            final String catalogName, final String schemaName,
            final String tableName, final String columnPatternName,
            final long retrieveRule) throws BaseException {
        final Schema schema = new Schema(catalogName, schemaName);
        final Table table = new Table(schema, tableName);
        TableRetriever tableRetriever = new TableRetriever(conn);

        tableRetriever.retrieveColumns(table, columnPatternName, retrieveRule);

        if (RetrieveRule.PRIMARY_KEY == (retrieveRule & RetrieveRule.PRIMARY_KEY)) {
            tableRetriever.retrievePrimaryKey(table, retrieveRule);
        }
        return table.getColumns();
    }

    public Collection<Column> getColumns(final Connection conn,
            final String catalogName, final String schemaName,
            final String tableName, final long retrieveRule)
            throws BaseException {
        return getColumns(conn, catalogName, schemaName, tableName, null,
                retrieveRule | RetrieveRule.REGEX);
    }

    public Collection<Column> getColumns(final Connection conn,
            final String schemaName, final String tableName,
            final long retrieveRule) throws BaseException {
        return getColumns(conn, getDefaultCatalog(conn), schemaName, tableName,
                null, retrieveRule | RetrieveRule.REGEX);
    }

    public Collection<Column> getColumns(final Connection conn,
            final String tableName, final long retrieveRule)
            throws BaseException {
        return getColumns(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), tableName, null, retrieveRule
                | RetrieveRule.REGEX);
    }

    public Collection<Column> getColumns(final Connection conn,
            final String catalogName, final String schemaName,
            final String tableName) throws BaseException {
        return getColumns(conn, catalogName, schemaName, tableName, null,
                RetrieveRule.DEFAULT | RetrieveRule.REGEX);
    }

    public Collection<Column> getColumns(final Connection conn,
            final String schemaName, final String tableName)
            throws BaseException {
        return getColumns(conn, getDefaultCatalog(conn), schemaName, tableName,
                null, RetrieveRule.DEFAULT | RetrieveRule.REGEX);
    }

    public Collection<Column> getColumns(final Connection conn,
            final String tableName) throws BaseException {
        return getColumns(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), tableName, null,
                RetrieveRule.DEFAULT | RetrieveRule.REGEX);
    }

    public Collection<Column> getColumns(final Connection conn,
            final Schema schema, final String tableName,
            final String columnPatternName, final long retrieveRule)
            throws BaseException {
        return getColumns(conn, schema.getCatalogName(), schema.getName(),
                tableName, columnPatternName, retrieveRule | RetrieveRule.REGEX);
    }

    public Collection<Column> getColumns(final Connection conn,
            final Schema schema, final String tableName, final String columnName)
            throws BaseException {
        return getColumns(conn, schema.getCatalogName(), schema.getName(),
                tableName, columnName, RetrieveRule.DEFAULT);
    }

    public Collection<Column> getColumns(final Connection conn,
            final Table table, final String columnName, final long retrieveRule)
            throws BaseException {
        Schema schema = table.getSchema();
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        return getColumns(conn, catalogName, schemaName, table.getName(),
                columnName, retrieveRule);
    }

    public Collection<Column> getColumns(final Connection conn,
            final Table table, final String columnName) throws BaseException {
        return getColumns(conn, table, columnName, RetrieveRule.DEFAULT);
    }

    public Collection<Column> getColumns(final Connection conn,
            final Table table, final long retrieveRule) throws BaseException {
        return getColumns(conn, table, null, retrieveRule | RetrieveRule.REGEX);
    }

    public Collection<Column> getColumns(final Connection conn,
            final Table table) throws BaseException {
        return getColumns(conn, table, null, RetrieveRule.DEFAULT
                | RetrieveRule.REGEX);
    }

    public Collection<Sequence> getSequences(final Connection conn,
            final String catalogName, final String schemaName,
            String sequencePatternName, final long retrieveRule)
            throws BaseException {
        List<Sequence> sequences = new ArrayList<Sequence>();
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        Statement statement = null;
        ResultSet rs = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT SEQUENCE_NAME, MIN_VALUE, MAX_VALUE, START_VALUE, INCREMENT_BY, LAST_VALUE FROM ");
            if (schemaName != null && !schemaName.isEmpty()) {
                sb.append(schemaName).append(".");
            }
            sb.append("ADQM_SEQUENCE_REGISTER ");
            sequencePatternName = unquote(sequencePatternName);
            boolean regex = RetrieveRule.REGEX == (RetrieveRule.REGEX & retrieveRule);
            Pattern pattern = null;
            if (regex) {
                pattern = Pattern.compile(sequencePatternName);
            }
            Schema schema = new Schema(catalogName, schemaName);
            statement = conn.createStatement();
            rs = statement.executeQuery(sb.toString());

            while (rs.next()) {
                String sn = rs.getString("SEQUENCE_NAME");
                if (regex) {
                    Matcher matcher = pattern.matcher(sn);
                    if (!matcher.matches()) {
                        continue;
                    }
                } else {
                    if (!sequencePatternName.equals(sn)) {
                        continue;
                    }
                }

                Sequence sequence = new Sequence(schema, quote(sn, quoted));
                sequence.setMinValue(rs.getBigDecimal("MIN_VALUE").toBigInteger());
                sequence.setMaxValue(rs.getBigDecimal("MAX_VALUE").toBigInteger());
                sequence.setStartValue(rs.getBigDecimal("START_VALUE").toBigInteger());
                sequence.setIncrementBy(rs.getBigDecimal("INCREMENT_BY").toBigInteger());
                sequence.setLastValue(rs.getBigDecimal("LAST_VALUE").toBigInteger());

                sequences.add(sequence);
            }
        } catch (SQLException ex) {
            throw new BaseException("",ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(statement);
        }
        return sequences;
    }

    public Collection<Sequence> getSequences(final Connection conn,
            final String catalogName, final String schemaName,
            final long retrieveRule) throws BaseException {
        return getSequences(conn, catalogName, schemaName, null, retrieveRule
                | RetrieveRule.REGEX);
    }

    public Collection<Sequence> getSequences(final Connection conn,
            final String schemaName, final long retrieveRule)
            throws BaseException {
        return getSequences(conn, getDefaultCatalog(conn), schemaName, null,
                retrieveRule | RetrieveRule.REGEX);
    }

    public Collection<Sequence> getSequences(final Connection conn,
            final long retrieveRule) throws BaseException {
        return getSequences(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), null, retrieveRule
                | RetrieveRule.REGEX);
    }

    public Collection<Sequence> getSequences(final Connection conn,
            final String catalogName, final String schemaName)
            throws BaseException {
        return getSequences(conn, catalogName, schemaName, null,
                RetrieveRule.DEFAULT | RetrieveRule.REGEX);
    }

    public Collection<Sequence> getSequences(final Connection conn,
            final String schemaName) throws BaseException {
        return getSequences(conn, getDefaultCatalog(conn), schemaName, null,
                RetrieveRule.DEFAULT | RetrieveRule.REGEX);
    }

    public Collection<Sequence> getSequences(final Connection conn)
            throws BaseException {
        return getSequences(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), null, RetrieveRule.DEFAULT
                | RetrieveRule.REGEX);
    }

    public Collection<Sequence> getSequences(final Connection conn,
            final Schema schema, final String sequenceName,
            final long retrieveRule) throws BaseException {
        return getSequences(conn, getDefaultCatalog(conn), schema.getName(),
                sequenceName, retrieveRule);
    }

    public Collection<Sequence> getSequences(final Connection conn,
            final Schema schema, final String sequenceName)
            throws BaseException {
        return getSequences(conn, getDefaultCatalog(conn), schema.getName(),
                sequenceName, RetrieveRule.DEFAULT);
    }

    public Collection<Sequence> getSequences(final Connection conn,
            final Schema schema) throws BaseException {
        return getSequences(conn, getDefaultCatalog(conn), schema.getName(),
                null, RetrieveRule.DEFAULT | RetrieveRule.REGEX);
    }

    public Schema getSchema(final Connection conn, final String catalogName,
            final String schemaPatternName, final long retrieveRule)
            throws BaseException {
        Collection<Schema> schemas = getSchemas(conn, catalogName,
                schemaPatternName, retrieveRule);
        if (schemas.isEmpty()) {
            return null;
        }
        return schemas.iterator().next();
    }

    public Schema getSchema(final Connection conn, final String catalogName,
            final String schemaName) throws BaseException {
        return getSchema(conn, catalogName, schemaName, RetrieveRule.DEFAULT);
    }

    public Schema getSchema(final Connection conn,
            final String schemaPatternName, final long retrieveRule)
            throws BaseException {
        return getSchema(conn, getDefaultCatalog(conn), schemaPatternName,
                retrieveRule | RetrieveRule.REGEX);
    }

    public Schema getSchema(final Connection conn, final String schemaName)
            throws BaseException {
        return getSchema(conn, getDefaultCatalog(conn), schemaName,
                RetrieveRule.DEFAULT);
    }

    public Schema getSchema(final Connection conn) throws BaseException {
        return getSchema(conn, getDefaultCatalog(conn), null,
                RetrieveRule.DEFAULT | RetrieveRule.REGEX);
    }

    public Table getTable(final Connection conn, final String catalogName,
            final String schemaName, final String tablePatternName,
            final long retrieveRule) throws BaseException {
        Collection<Table> tables = getTables(conn, catalogName, schemaName,
                tablePatternName, retrieveRule);
        if (tables.isEmpty()) {
            return null;
        }
        return tables.iterator().next();
    }

    public Table getTable(final Connection conn, final String schemaName,
            final String tablePatternName, final long retrieveRule)
            throws BaseException {
        return getTable(conn, getDefaultCatalog(conn), schemaName,
                tablePatternName, retrieveRule);
    }

    public Table getTable(final Connection conn, final String tablePatternName,
            final long retrieveRule) throws BaseException {
        return getTable(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), tablePatternName, retrieveRule);
    }

    public Table getTable(final Connection conn, final String catalogName,
            final String schemaName, final String tableName)
            throws BaseException {
        return getTable(conn, catalogName, schemaName, tableName,
                RetrieveRule.DEFAULT);
    }

    public Table getTable(final Connection conn, final String schemaName,
            final String tableName) throws BaseException {
        return getTable(conn, getDefaultCatalog(conn), schemaName, tableName,
                RetrieveRule.DEFAULT);
    }

    public Table getTable(final Connection conn, final String tableName)
            throws BaseException {
        return getTable(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), tableName, RetrieveRule.DEFAULT);
    }

    public Table getTable(final Connection conn, final Schema schema,
            final String tablePatternName, final long retrieveRule)
            throws BaseException {
        return getTable(conn, schema.getCatalogName(), schema.getName(),
                tablePatternName, retrieveRule);
    }

    public Table getTable(final Connection conn, final Schema schema,
            final String tableName) throws BaseException {
        return getTable(conn, schema.getCatalogName(), schema.getName(),
                tableName, RetrieveRule.DEFAULT);
    }

    public View getView(final Connection conn, final String catalogName,
            final String schemaName, final String viewPatternName,
            final long retrieveRule) throws BaseException {
        Collection<View> views = getViews(conn, catalogName, schemaName,
                viewPatternName, retrieveRule);
        if (views.isEmpty()) {
            return null;
        }
        return views.iterator().next();
    }

    public View getView(final Connection conn, final String schemaName,
            final String viewPatternName, final long retrieveRule)
            throws BaseException {
        return getView(conn, getDefaultCatalog(conn), schemaName,
                viewPatternName, retrieveRule);
    }

    public View getView(final Connection conn, final String viewPatternName,
            final long retrieveRule) throws BaseException {
        return getView(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), viewPatternName, retrieveRule);
    }

    public View getView(final Connection conn, final String catalogName, final String schemaName, final String viewName)
            throws BaseException {
        return getView(conn, catalogName, schemaName, viewName,
                RetrieveRule.DEFAULT);
    }

    public View getView(final Connection conn, final String schemaName,
            final String viewName) throws BaseException {
        return getView(conn, getDefaultCatalog(conn), schemaName, viewName,
                RetrieveRule.DEFAULT);
    }

    public View getView(final Connection conn, final String viewName)
            throws BaseException {
        return getView(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), viewName, RetrieveRule.DEFAULT);
    }

    public View getView(final Connection conn, final Schema schema,
            final String viewPatternName, final long retrieveRule)
            throws BaseException {
        return getView(conn, schema.getCatalogName(), schema.getName(),
                viewPatternName, retrieveRule);
    }

    public View getView(final Connection conn, final Schema schema,
            final String viewName) throws BaseException {
        return getView(conn, schema.getCatalogName(), schema.getName(),
                viewName, RetrieveRule.DEFAULT);
    }

    public Index getIndex(final Connection conn, final String catalogName,
            final String schemaName, final String tableName,
            final String indexPatternName, final long retrieveRule)
            throws BaseException {
        Collection<Index> indices = getTableIndices(conn, catalogName,
                schemaName, tableName, indexPatternName, retrieveRule);
        if (indices.isEmpty()) {
            return null;
        }
        return indices.iterator().next();
    }

    public Index getIndex(final Connection conn, final String schemaName,
            final String tableName, final String indexPatternName,
            final long retrieveRule) throws BaseException {
        return getIndex(conn, getDefaultCatalog(conn), schemaName, tableName,
                indexPatternName, retrieveRule);
    }

    public Index getIndex(final Connection conn, final String tableName,
            final String indexPatternName, final long retrieveRule)
            throws BaseException {
        return getIndex(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), tableName, indexPatternName,
                retrieveRule);
    }

    public Index getIndex(final Connection conn, final String catalogName,
            final String schemaName, final String tableName,
            final String indexName) throws BaseException {
        return getIndex(conn, catalogName, schemaName, tableName, indexName,
                RetrieveRule.DEFAULT);
    }

    public Index getIndex(final Connection conn, final String schemaName,
            final String tableName, final String indexName)
            throws BaseException {
        return getIndex(conn, getDefaultCatalog(conn), schemaName, tableName,
                indexName, RetrieveRule.DEFAULT);
    }

    public Index getIndex(final Connection conn, final String tableName, final String indexName) throws BaseException {
        return getIndex(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), tableName, indexName, RetrieveRule.DEFAULT);
    }

    public Index getIndex(final Connection conn,
            final Schema schema, final String tableName, final String indexPatternName, final long retrieveRule)
            throws BaseException {
        return getIndex(conn, schema.getCatalogName(), schema.getName(), tableName, indexPatternName, retrieveRule);
    }

    public Index getIndex(final Connection conn, final Schema schema, final String tableName, final String indexName)
            throws BaseException {
        return getIndex(conn, schema.getCatalogName(), schema.getName(), tableName, indexName, RetrieveRule.DEFAULT);
    }

    public Index getIndex(final Connection conn, final Table table, final String indexPatternName, final long retrieveRule)
            throws BaseException {
        Schema schema = table.getSchema();
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        return getIndex(conn, catalogName, schemaName, table.getName(),
                indexPatternName, retrieveRule);
    }

    public Index getIndex(final Connection conn, final Table table, final String indexName) throws BaseException {
        return getIndex(conn, table, indexName, RetrieveRule.DEFAULT);
    }

    public Procedure getProcedure(final Connection conn,
            final String catalogName, final String schemaName, final String procedurePatternName, final long retrieveRule)
            throws BaseException {
        Collection<Procedure> procedures = getProcedures(conn, catalogName,
                schemaName, procedurePatternName, retrieveRule);
        if (procedures.isEmpty()) {
            return null;
        }
        return procedures.iterator().next();
    }

    public Procedure getProcedure(final Connection conn,
            final String schemaName, final String procedurePatternName, final long retrieveRule)
            throws BaseException {
        return getProcedure(conn, getDefaultCatalog(conn), schemaName, procedurePatternName, retrieveRule);
    }

    public Procedure getProcedure(final Connection conn,
            final String procedurePatternName, final long retrieveRule)
            throws BaseException {
        return getProcedure(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), procedurePatternName, retrieveRule);
    }

    public Procedure getProcedure(final Connection conn,
            final String catalogName, final String schemaName, final String procedureName)
            throws BaseException {
        return getProcedure(conn, catalogName, schemaName, procedureName,
                RetrieveRule.DEFAULT);
    }

    public Procedure getProcedure(final Connection conn, final String schemaName, final String procedureName)
            throws BaseException {
        return getProcedure(conn, getDefaultCatalog(conn), schemaName, procedureName, RetrieveRule.DEFAULT);
    }

    public Procedure getProcedure(final Connection conn, final String procedureName) throws BaseException {
        return getProcedure(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), procedureName, RetrieveRule.DEFAULT);
    }

    public Procedure getProcedure(final Connection conn,
            final Schema schema, final String procedurePatternName, final long retrieveRule)
            throws BaseException {
        return getProcedure(conn, schema.getCatalogName(), schema.getName(), procedurePatternName, retrieveRule);
    }

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn,
            final String catalogName, final String schemaName, final String procedureName, final long retrieveRule)
            throws BaseException {
        RoutineRetriever routineRetriever = new RoutineRetriever(conn);
        return routineRetriever.retrieveProcedureColumns(catalogName, schemaName, procedureName, retrieveRule);
    }

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn,
            final Schema schema, final String procedureName, final long retrieveRule)
            throws BaseException {
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        return getProcedurePatameters(conn, catalogName, schemaName, procedureName, retrieveRule);
    }

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn, final Schema schema, final String procedureName)
            throws BaseException {
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        return getProcedurePatameters(conn, catalogName, schemaName, procedureName, RetrieveRule.DEFAULT);
    }

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn, final Procedure procedure) throws BaseException {
        RoutineRetriever routineRetriever = new RoutineRetriever(conn);
        return routineRetriever.retrieveProcedureColumns(procedure, RetrieveRule.DEFAULT);
    }

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn, final Procedure procedure, final long retrieveRule)
            throws BaseException {
        RoutineRetriever routineRetriever = new RoutineRetriever(conn);
        return routineRetriever.retrieveProcedureColumns(procedure, retrieveRule);
    }

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn, final String procedureName) throws BaseException {
        return getProcedurePatameters(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), procedureName, RetrieveRule.DEFAULT);
    }

    public Collection<ProcedureColumn> getProcedurePatameters(final Connection conn, final String procedureName, final long retrieveRule)
            throws BaseException {
        return getProcedurePatameters(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), procedureName, retrieveRule);
    }

    public Procedure getProcedure(final Connection conn, final Schema schema, final String procedureName)
            throws BaseException {
        return getProcedure(conn, schema.getCatalogName(), schema.getName(), procedureName, RetrieveRule.DEFAULT);
    }

    public Function getFunction(final Connection conn,
            final String catalogName, final String schemaName, final String functionPatternName, final long retrieveRule)
            throws BaseException {
        Collection<Function> functions = getFunctions(conn, catalogName, schemaName, functionPatternName, retrieveRule);
        if (functions.isEmpty()) {
            return null;
        }
        return functions.iterator().next();
    }

    public Function getFunction(final Connection conn,
            final String schemaName, final String functionPatternName, final long retrieveRule)
            throws BaseException {
        return getFunction(conn, getDefaultCatalog(conn), schemaName, functionPatternName, retrieveRule);
    }

    public Function getFunction(final Connection conn, final String functionPatternName, final long retrieveRule)
            throws BaseException {
        return getFunction(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), functionPatternName, retrieveRule);
    }

    public Function getFunction(final Connection conn,
            final String catalogName, final String schemaName, final String functionName)
            throws BaseException {
        return getFunction(conn, catalogName, schemaName, functionName, RetrieveRule.DEFAULT);
    }

    public Function getFunction(final Connection conn, final String schemaName, final String functionName)
            throws BaseException {
        return getFunction(conn, getDefaultCatalog(conn), schemaName, functionName, RetrieveRule.DEFAULT);
    }

    public Function getFunction(final Connection conn, final String functionName)
            throws BaseException {
        return getFunction(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), functionName, RetrieveRule.DEFAULT);
    }

    public Function getFunction(final Connection conn,
            final Schema schema, final String functionPatternName, final long retrieveRule)
            throws BaseException {
        return getFunction(conn, schema.getCatalogName(), schema.getName(), functionPatternName, retrieveRule);
    }

    public Function getFunction(final Connection conn, final Schema schema, final String functionName)
            throws BaseException {
        return getFunction(conn, schema.getCatalogName(), schema.getName(), functionName, RetrieveRule.DEFAULT);
    }

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn,
            final String catalogName, final String schemaName, final String functionName, final long retrieveRule)
            throws BaseException {
        RoutineRetriever routineRetriever = new RoutineRetriever(conn);
        return routineRetriever.retrieveFunctionColumns(catalogName, schemaName, functionName, retrieveRule);
    }

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn,
            final Schema schema, final String functionName, final long retrieveRule)
            throws BaseException {
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        return getFunctionPatameters(conn, catalogName, schemaName, functionName, retrieveRule);
    }

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final Schema schema, final String functionName)
            throws BaseException {
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        return getFunctionPatameters(conn, catalogName, schemaName, functionName, RetrieveRule.DEFAULT);
    }

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final Function function) throws BaseException {
        return getFunctionPatameters(conn, function, RetrieveRule.DEFAULT);
    }

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final Function function, final long retrieveRule)
            throws BaseException {
        RoutineRetriever routineRetriever = new RoutineRetriever(conn);
        return routineRetriever.retrieveFunctionColumns(function, retrieveRule);
    }

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final String functionName) throws BaseException {
        return getFunctionPatameters(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), functionName, RetrieveRule.DEFAULT);
    }

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final String functionName, final long retrieveRule)
            throws BaseException {
        return getFunctionPatameters(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), functionName, retrieveRule);
    }

    public Column getColumn(final Connection conn,
            final String catalogName, final String schemaName,
            final String tableName, final String columnPatternName, final long retrieveRule)
            throws BaseException {
        Collection<Column> columns = getColumns(conn, catalogName, schemaName, tableName, columnPatternName, retrieveRule);
        if (columns.isEmpty()) {
            return null;
        }
        return columns.iterator().next();
    }

    public Column getColumn(final Connection conn, final String schemaName,
            final String tableName, final String columnPatternName,
            final long retrieveRule) throws BaseException {
        return getColumn(conn, getDefaultCatalog(conn), schemaName, tableName,
                columnPatternName, retrieveRule);
    }

    public Column getColumn(final Connection conn, final String tableName,
            final String columnPatternName, final long retrieveRule)
            throws BaseException {
        return getColumn(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), tableName, columnPatternName,
                retrieveRule);
    }

    public Column getColumn(final Connection conn, final String catalogName,
            final String schemaName, final String tableName,
            final String columnName) throws BaseException {
        return getColumn(conn, catalogName, schemaName, tableName, columnName,
                RetrieveRule.DEFAULT);
    }

    public Column getColumn(final Connection conn, final String schemaName,
            final String tableName, final String columnName)
            throws BaseException {
        return getColumn(conn, getDefaultCatalog(conn), schemaName, tableName,
                columnName, RetrieveRule.DEFAULT);
    }

    public Column getColumn(final Connection conn, final Schema schema,
            final String tableName, final String columnPatternName,
            final long retrieveRule) throws BaseException {
        return getColumn(conn, schema.getCatalogName(), schema.getName(),
                tableName, columnPatternName, retrieveRule);
    }

    public Column getColumn(final Connection conn, final Schema schema,
            final String tableName, final String columnName)
            throws BaseException {
        return getColumn(conn, schema.getCatalogName(), schema.getName(),
                tableName, columnName, RetrieveRule.DEFAULT);
    }

    public Column getColumn(final Connection conn, final Table table,
            final String columnPatternName, final long retrieveRule)
            throws BaseException {
        Schema schema = table.getSchema();
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        return getColumn(conn, catalogName, schemaName, table.getName(),
                columnPatternName, retrieveRule);
    }

    public Column getColumn(Connection conn, Table table, String columnName)
            throws BaseException {
        return getColumn(conn, table, columnName, RetrieveRule.DEFAULT);
    }

    public Sequence getSequence(final Connection conn,
            final String catalogName, final String schemaName,
            String sequenceName, final long retrieveRule) throws BaseException {
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT SEQUENCE_NAME, MIN_VALUE, MAX_VALUE, START_VALUE, INCREMENT_BY, LAST_VALUE FROM ");
            if (schemaName != null && !schemaName.isEmpty()) {
                sb.append(schemaName).append(".");
            }
            sb.append("ADQM_SEQUENCE_REGISTER WHERE SEQUENCE_NAME = ?");
            sequenceName = unquote(sequenceName);
            Schema schema = new Schema(catalogName, schemaName);

            ps = conn.prepareStatement(sb.toString());
            ps.setString(1, sequenceName);
            rs = ps.executeQuery(sb.toString());

            while (rs.next()) {
                String sn = rs.getString("SEQUENCE_NAME");
                if (!sequenceName.equals(sn)) {
                    continue;
                }

                Sequence sequence = new Sequence(schema, quote(sn, quoted));
                sequence.setMinValue(rs.getBigDecimal("MIN_VALUE").toBigInteger());
                sequence.setMaxValue(rs.getBigDecimal("MAX_VALUE").toBigInteger());
                sequence.setStartValue(rs.getBigDecimal("START_VALUE").toBigInteger());
                sequence.setIncrementBy(rs.getBigDecimal("INCREMENT_BY").toBigInteger());
                sequence.setLastValue(rs.getBigDecimal("LAST_VALUE").toBigInteger());

                return sequence;
            }
        } catch (SQLException ex) {
            throw new BaseException("",ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
        }
        return null;
    }

    public Sequence getSequence(final Connection conn, final String schemaName,
            final String sequenceName, final long retrieveRule)
            throws BaseException {
        return getSequence(conn, getDefaultCatalog(conn), schemaName,
                sequenceName, retrieveRule);
    }

    public Sequence getSequence(final Connection conn,
            final String sequenceName, final long retrieveRule)
            throws BaseException {
        return getSequence(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), sequenceName, retrieveRule);
    }

    public Sequence getSequence(final Connection conn,
            final String catalogName, final String schemaName,
            final String sequenceName) throws BaseException {
        return getSequence(conn, catalogName, schemaName, sequenceName,
                RetrieveRule.DEFAULT);
    }

    public Sequence getSequence(final Connection conn, final String schemaName,
            final String sequenceName) throws BaseException {
        return getSequence(conn, getDefaultCatalog(conn), schemaName,
                sequenceName, RetrieveRule.DEFAULT);
    }

    public Sequence getSequence(final Connection conn, final String sequenceName)
            throws BaseException {
        return getSequence(conn, getDefaultCatalog(conn),
                getDefaultSchemaName(conn), sequenceName, RetrieveRule.DEFAULT);
    }

    public Sequence getSequence(final Connection conn, final Schema schema,
            final String sequenceName, final long retrieveRule)
            throws BaseException {
        return getSequence(conn, schema.getCatalogName(), schema.getName(),
                sequenceName, retrieveRule);
    }

    public Sequence getSequence(final Connection conn, final Schema schema,
            final String sequenceName) throws BaseException {
        return getSequence(conn, schema.getCatalogName(), schema.getName(),
                sequenceName, RetrieveRule.DEFAULT);
    }

    public BigInteger nextValue(final Connection conn, final String catalogName,
            final String schemaName, String sequenceName) throws BaseException {
        Boolean autoCommit = null;
        BigInteger nextValue = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT LAST_VALUE + 1 FROM ");
            if (schemaName != null && !schemaName.isEmpty()) {
                sb.append(schemaName).append(".");
            }
            sb.append("ADQM_SEQUENCE_REGISTER WHERE SEQUENCE_NAME = ?");
            sequenceName = unquote(sequenceName);

            ps = conn.prepareStatement(sb.toString());
            ps.setString(1, sequenceName);
            rs = ps.executeQuery(sb.toString());

            if (rs.next()) {
                nextValue = rs.getBigDecimal(1).toBigInteger();
            }
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e) {
            }
            throw new BaseException("",ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            if (autoCommit != null) {
                try {
                    conn.setAutoCommit(autoCommit);
                } catch (SQLException ex) {
                }
            }
        }
        return nextValue;
    }

    public BigInteger nextValue(final Connection conn, final String schemaName, final String sequenceName) throws BaseException {
        return nextValue(conn, getDefaultCatalog(conn), schemaName, sequenceName);
    }

    public BigInteger nextValue(final Connection conn, final String sequenceName) throws BaseException {
        return nextValue(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), sequenceName);
    }

    public BigInteger nextValue(final Connection conn, final Sequence sequence) throws BaseException {
        Schema schema = sequence.getSchema();
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        return nextValue(conn, catalogName, schemaName, sequence.getName());
    }

    public BigInteger currValue(final Connection conn, final String catalogName, final String schemaName, String sequenceName)
            throws BaseException {
        BigInteger currValue = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT LAST_VALUE FROM ");
            if (schemaName != null && !schemaName.isEmpty()) {
                sb.append(schemaName).append(".");
            }
            sb.append("ADQM_SEQUENCE_REGISTER WHERE SEQUENCE_NAME = ?");
            sequenceName = unquote(sequenceName);

            ps = conn.prepareStatement(sb.toString());
            ps.setString(1, sequenceName);
            rs = ps.executeQuery(sb.toString());

            if (rs.next()) {
                currValue = rs.getBigDecimal(1).toBigInteger();
            }
        } catch (SQLException ex) {
            throw new BaseException("",ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
        }
        return currValue;
    }

    public BigInteger currValue(final Connection conn, final String schemaName, final String sequenceName)
            throws BaseException {
        return currValue(conn, getDefaultCatalog(conn), schemaName, sequenceName);
    }

    public BigInteger currValue(final Connection conn, final String sequenceName) throws BaseException {
        return currValue(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), sequenceName);
    }

    public BigInteger currValue(final Connection conn, final Sequence sequence) throws BaseException {
        Schema schema = sequence.getSchema();
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        return currValue(conn, catalogName, schemaName, sequence.getName());
    }

    public void restartValue(final Connection conn, final String catalogName,
            final String schemaName, String sequenceName, BigInteger restartValue)
            throws BaseException {
        if (restartValue == null) {
            restartValue = BigInteger.ZERO;
        }
        sequenceName = unquote(sequenceName);
        PreparedStatement ps = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE ");
            if (schemaName != null && !schemaName.isEmpty()) {
                sb.append(schemaName).append(".");
            }
            sb.append("ADQM_SEQUENCE_REGISTER WHERE SEQUENCE_NAME = ? ");
            sb.append("SET LAST_VALUE = ?, START_VALUE = ? ");
            sb.append("WHERE SEQUENCE_NAME = ?");

            ps = conn.prepareStatement(sb.toString());
            ps.setBigDecimal(1, new BigDecimal(restartValue));
            ps.setBigDecimal(2, new BigDecimal(restartValue));
            ps.setString(3, sequenceName);

            ps.execute();
        } catch (SQLException ex) {
            throw new BaseException("",ex);
        } finally {
            DbUtils.closeQuietly(ps);
        }
    }

    public void restartValue(final Connection conn, final String schemaName, final String sequenceName, BigInteger restartValue)
            throws BaseException {
        restartValue(conn, getDefaultCatalog(conn), schemaName, sequenceName, restartValue);
    }

    public void restartValue(final Connection conn, final String sequenceName, BigInteger restartValue) throws BaseException {
        restartValue(conn, getDefaultCatalog(conn), getDefaultSchemaName(conn), sequenceName, restartValue);
    }

    public void restartValue(final Connection conn, final Sequence sequence, BigInteger restartValue) throws BaseException {
        Schema schema = sequence.getSchema();
        String catalogName, schemaName;
        if (schema == null) {
            catalogName = getDefaultCatalog(conn);
            schemaName = getDefaultSchemaName(conn);
        } else {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        restartValue(conn, catalogName, schemaName, sequence.getName(), restartValue);
    }

    public PrimaryKey getPrimaryKey(final Connection conn, final Table table, final long retrieveRule) throws BaseException {
        TableRetriever tableRetriever = new TableRetriever(conn);
        return tableRetriever.retrievePrimaryKey(table, retrieveRule);
    }

    public PrimaryKey getPrimaryKey(final Connection conn, final Table table) throws BaseException {
        return getPrimaryKey(conn, table, RetrieveRule.DEFAULT);
    }

    public Collection<ForeignKey> getForeignKeys(final Connection conn, final Table table, final long retrieveRule)
            throws BaseException {
        TableRetriever tableRetriever = new TableRetriever(conn);
        return tableRetriever.retrieveForeignKeys(table, retrieveRule).values();
    }

    public Collection<ForeignKey> getForeignKeys(final Connection conn, final Table table) throws BaseException {
        return getForeignKeys(conn, table, RetrieveRule.DEFAULT);
    }

    public Collection<ForeignKey> getImportedKeys(final Connection conn, final Table table, final long retrieveRule)
            throws BaseException {
        TableRetriever tableRetriever = new TableRetriever(conn);
        return tableRetriever.retrieveImportedKeys(table, retrieveRule).values();
    }

    public Collection<ForeignKey> getImportedKeys(final Connection conn, final Table table) throws BaseException {
        return getImportedKeys(conn, table, RetrieveRule.DEFAULT);
    }

    public Collection<ForeignKey> getExportedKeys(final Connection conn, final Table table, final long retrieveRule)
            throws BaseException {
        TableRetriever tableRetriever = new TableRetriever(conn);
        return tableRetriever.retrieveExportedKeys(table, retrieveRule).values();
    }

    public Collection<ForeignKey> getExportedKeys(final Connection conn, final Table table) throws BaseException {
        return getExportedKeys(conn, table, RetrieveRule.DEFAULT);
    }

    public Collection<ColumnDataType> getUserDataTypes(Connection conn, Schema schema) throws BaseException {
        Database database = new Database("database");
        DatabaseInfoRetriever retriever = new DatabaseInfoRetriever(conn);
        retriever.retrieveUserDefinedColumnDataTypes(schema.getCatalogName(),
                schema.getName());
        return database.getColumnDataTypes().values();
    }

    public Collection<ResultsColumn> getColumnsByQuery(final Connection conn,
            final String vSQL) throws BaseException {
        String viewSQL = "select * from (" + vSQL + ") tab where 1=0";
        ResultSet rs = null;
        ResultsColumns resultColumns = null;
        try(Statement statement = conn.createStatement()) {
            rs = statement.executeQuery(viewSQL);
            final ResultsRetriever resultsRetriever = new ResultsRetriever(rs);
            resultColumns = resultsRetriever.retrieveResults();
            return resultColumns.getColumns();
        } catch (SQLException ex) {
            throw new BaseException("",ex);
        } finally {
            DbUtils.closeQuietly(rs);
        }
    }

    public Collection<Column> getColumnsByView(final Connection conn,
            final String vSQL) throws BaseException {
    	//postgresql 使用到
    	String sql =vSQL;
    	String schemaName=null;
    	String newViewName =null;
    	if(sql.indexOf("&")>0) {
    		sql =sql.substring(0,sql.indexOf("&"));
    		schemaName=vSQL.substring((vSQL.indexOf("&")+1),vSQL.length()).trim();
    	}

        String sqlStr1 = "SELECT * FROM (" + sql + ") t_temp_view WHERE 1=0";
        /*
         * 名字是不会重复的
         */
        String viewName = "V"
                + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "").substring(1, 30);
        newViewName = viewName;
        if(null != schemaName && !schemaName.equals("") && schemaName.length()>0) {
        	newViewName = schemaName+"."+"\"" +viewName+"\"";//postgresql 视图需要加上模式名称
        }
        sqlStr1 = "CREATE VIEW " + newViewName + " AS " + sqlStr1;
        try {
            executeDDL(conn, sqlStr1);
            View view = getView(conn, viewName);
            return getColumns(conn, view);
        } finally {
            executeDDL(conn, "DROP VIEW " + newViewName);
        }
    }

    public boolean validateSQL(final Connection conn, final String sqlString)
            throws BaseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean validateDDL(final Connection conn, final String sqlString)
            throws BaseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean validateDQL(final Connection conn, final String sqlString)
            throws BaseException {
        try(Statement statement = conn.createStatement()) {
            statement.executeQuery("select 1 from (" + sqlString + ") tab where 1=2");
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    public boolean validateDML(final Connection conn, final String sqlString)
            throws BaseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<ProcedureColumn, Object> callPorcedure(final Connection conn,
            final Procedure procedure, final Object[] parameters)
            throws BaseException {
        try {
            /*
             * 获取存储过程调用的SQL
             */
            StringBuilder paramSqlSb = new StringBuilder("");
            boolean hasReturnParam = false;
            List<ProcedureColumn> params = procedure.getColumns();
            int inParamConut = 0;
            if (params != null && !params.isEmpty()) {
                int i = 0;
                for (ProcedureColumn param : params) {
                    switch (param.getColumnType()) {
                        case in:
                        case inOut:
                            if (i++ > 0) {
                                paramSqlSb.append(", ");
                            }
                            paramSqlSb.append("?");
                            inParamConut++;
                            break;
                        case out:
                            if (i++ > 0) {
                                paramSqlSb.append(", ");
                            }
                            paramSqlSb.append("?");
                            break;
                        case result:
                        case returnValue:
                            hasReturnParam = true;
                            break;
                        default:
                            throw new SQLException("Unknown procedure(" + procedure.getFullName() + ") parameter.");
                    }
                }
            }

            if (inParamConut > parameters.length) {
                throw new BaseException("MetaDialect.callPorcedure Failed : lack of parameters");
            }
            String procedureFullName = procedure.getFullName();
            StringBuilder callSqlSb = new StringBuilder("{");
            if (hasReturnParam) {
                callSqlSb.append("? = ");
            }
            callSqlSb.append("call ").append(procedureFullName);
            callSqlSb.append("(").append(paramSqlSb).append(")}");
            /*
             * 配置存储过程
             */
            CallableStatement callStmt = conn.prepareCall(callSqlSb.toString());
            int i = 0;
            if (CollectionUtils.isNotEmpty(params)) {
                for (ProcedureColumn param : params) {
                    int jdbcType = param.getType().getJdbcType();
                    switch (param.getColumnType()) {
                        case in:
                            callStmt.setObject(i + 1, parameters[i]);
                            break;
                        case inOut:
                            callStmt.setObject(i + 1, parameters[i]);
                            callStmt.registerOutParameter(i + 1, jdbcType);
                            break;
                        case out:
                        case result:
                        case returnValue:
                            callStmt.registerOutParameter(i + 1, jdbcType);
                            break;
                        default:
                            throw new SQLException("Unknown procedure(" + procedure.getFullName() + ") parameter.");
                    }
                    ++i;
                }
            }
            callStmt.execute();

            Map<ProcedureColumn, Object> returnVal = new HashMap<ProcedureColumn, Object>();
            i = 0;
            if (CollectionUtils.isNotEmpty(params)) {
                for (ProcedureColumn param : params) {
                    ++i;
                    switch (param.getColumnType()) {
                        case out:
                        case result:
                        case returnValue:
                            returnVal.put(param, callStmt.getObject(i + 1));
                            break;
                    }
                }
            }
            return returnVal;
        } catch (SQLException ex) {
            throw new BaseException("MetaDialect.callPorcedure Failed : " + ex.getMessage());
        }
    }

    @Override
    public int getJdbcType(final int javaType) {
        return typeResolver.getJdbcType(javaType);
    }

    @Override
    public int[] getCandidateJdbcTypes(final int javaType) {
        return typeResolver.getCandidateJdbcTypes(javaType);
    }

    @Override
    public int getJavaType(final int jdbcType) {
        return typeResolver.getJavaType(jdbcType);
    }

    @Override
    public int[] getCandidateJavaTypes(final int jdbcType) {
        return typeResolver.getCandidateJavaTypes(jdbcType);
    }

    @Override
    public String getJdbcTypeName(final int jdbcType) {
        return typeResolver.getJdbcTypeName(jdbcType);
    }

    @Override
    public String getJavaTypeName(final int javaType) {
        return typeResolver.getJavaTypeName(javaType);
    }

    @Override
    public int getJdbcType(final String jdbcTypeName) {
        return typeResolver.getJdbcType(jdbcTypeName);
    }

    @Override
    public int getJavaType(final String javaTypeName) {
        return typeResolver.getJavaType(javaTypeName);
    }

    public Collection<ColumnDataType> getColumnDataTypes() {
        return jdbcTypes;
    }

    public Collection<ColumnDataType> getColumnDataTypes(int jdbcType) {
        Collection<ColumnDataType> columnDataTypes = new ArrayList<ColumnDataType>();

        for (ColumnDataType columnDataType : jdbcTypes) {
            if (columnDataType.getJdbcType() == jdbcType) {
                columnDataTypes.add(columnDataType);
            }
        }
        return columnDataTypes;
    }

    public Collection<ColumnDataType> getColumnDataTypes(int jdbcType, int precision) {
        Collection<ColumnDataType> columnDataTypes = new ArrayList<ColumnDataType>();

        for (ColumnDataType columnDataType : jdbcTypeMap.get(jdbcType)) {
            if (columnDataType.getJdbcType() == jdbcType) {
                if (columnDataType.getPrecision() >= precision) {
                    columnDataTypes.add(columnDataType);
                }
            }
        }
        return columnDataTypes;
    }

    public Collection<ColumnDataType> getColumnDataTypes(String jdbcTypeName) {
        return columnDataTypeMap.get(jdbcTypeName);
    }

    public Collection<ColumnDataType> getColumnDataTypes(final String jdbcTypeName, final int precision) {
        Collection<ColumnDataType> columnDataTypes = new ArrayList<ColumnDataType>();

        for (ColumnDataType columnDataType : columnDataTypeMap.get(jdbcTypeName)) {
            if (columnDataType.getName().equalsIgnoreCase(jdbcTypeName)) {
                if (columnDataType.getPrecision() >= precision) {
                    columnDataTypes.add(columnDataType);
                }
            }
        }
        return columnDataTypes;
    }

    public Collection<String> getKeyWords() {
        return keywords;
    }

    public boolean isKeyWord(String identifier) {
        return keywords.contains(identifier);
    }

    public SystemFunction getSystemFunction(final String systemFunctionName) throws BaseException {
        return systemFunctions.get(systemFunctionName);
    }

    public Collection<SystemFunction> getSystemFunctions() throws BaseException {
        return systemFunctions.values();
    }

    public String urlWithCharset(String url, String charSet) throws BaseException {
        return url;
    }

    public String getUserName(final Connection connection) throws BaseException {
        String user = "";
        try {
            DatabaseMetaData meta = connection.getMetaData();
            user = meta.getUserName();
        } catch (SQLException ex) {
            throw new BaseException("MetaDialect.getUserName Failed.",ex);
        }

        return user;
    }

    public Boolean executeDDL(final Connection conn, final String sql) throws BaseException {
        return executeDDL(conn, sql, null);
    }

    public Boolean executeDDL(final Connection conn, final String sql,
            final Object[] args) throws BaseException {
//        try {
//            return ExecuteRunner.executeDDL(conn, sql, args);
//        } catch (SQLException ex) {
//            throw new BaseException("MetaDialect.executeDDL Failed." + ex.getMessage());
//        }
        throw new BaseException("MetaDialect.executeDDL Failed.");
    }

    public <T> T executeDQL(final Connection conn, final String sql, final Object[] args, ResultSetHandler<T> handler)
            throws BaseException {
        try {
            return new QueryRunner().query(conn, sql, handler, args);
        } catch (SQLException ex) {
            throw new BaseException("MetaDialect.executeDQL Failed.",ex);
        }
    }

    public <T> T executeDQL(final Connection conn, final String sql, ResultSetHandler<T> handler) throws BaseException {
        // return executeDQL(conn, sql, handler);
        return null;
    }

    /**
     * 执行DDL SQL语句 包括创建表、索引、视图，删除表、索引、视图
     * @param conn
     * @param sql
     * @param args
     * @return
     * @throws BaseException
     */
    public Integer executeDML(final Connection conn, final String sql,
            final Object[] args) throws BaseException {
//        try {
//            return ExecuteRunner.executeDML(conn, sql, args);
//        } catch (SQLException ex) {
//            throw new BaseException("MetaDialect.executeDML Failed.",ex);
//        }
        throw new BaseException("MetaDialect.executeDML Failed.");
    }

    public Integer executeDML(final Connection conn, final String sql)
            throws BaseException {
        return executeDML(conn, sql, null);
    }

    /*
     * 针对只执行，且不知道执行的是什么类型的语句，且不需要结果返回的
     */
    public void execute(final Connection conn, final String sql)
            throws BaseException {
        execute(conn, sql, null);
    }

    public void execute(final Connection conn, final String sql, final Object[] args) throws BaseException {
//        try {
//            ExecuteRunner.execute(conn, sql, args);
//        } catch (SQLException ex) {
//            throw new BaseException("MetaDialect.execute Failed.",ex);
//        }
        throw new BaseException("MetaDialect.execute Failed.");
    }

    @Override
    public synchronized void initialize(Connection connection) throws BaseException {
        if (!columnDataTypeMap.isEmpty()) {
            return;
        }
        final Schema systemSchema = new Schema();

        MetadataResultSet results = null;
        InputStream siis = null;
        try {
            results = new MetadataResultSet(connection.getMetaData().getTypeInfo());
            while (results.next()) {
                final String typeName = results.getString("TYPE_NAME");
                final int jdbcType = results.getInt("DATA_TYPE", 0);
                final int precision = results.getInt("PRECISION", 0);
                final String literalPrefix = results.getString("LITERAL_PREFIX");
                final String literalSuffix = results.getString("LITERAL_SUFFIX");
                final String createParameters = results.getString("CREATE_PARAMS");
                final boolean isNullable = results.getInt("NULLABLE", DatabaseMetaData.typeNullableUnknown) == DatabaseMetaData.typeNullable;
                final boolean isCaseSensitive = results.getBoolean("CASE_SENSITIVE");
                final SearchableType searchable = SearchableType.valueOf(results.getInt("SEARCHABLE", SearchableType.unknown.ordinal()));
                final boolean isUnsigned = results.getBoolean("UNSIGNED_ATTRIBUTE");
                final boolean isFixedPrecisionScale = results.getBoolean("FIXED_PREC_SCALE");
                final boolean isAutoIncremented = results.getBoolean("AUTO_INCREMENT");
                final String localTypeName = results.getString("LOCAL_TYPE_NAME");
                final int minimumScale = results.getInt("MINIMUM_SCALE", 0);
                final int maximumScale = results.getInt("MAXIMUM_SCALE", 0);
                final int numPrecisionRadix = results.getInt("NUM_PREC_RADIX", 0);
                final ColumnDataType columnDataType = new ColumnDataType(systemSchema, typeName);
                columnDataType.setJdbcType(jdbcType);
                columnDataType.setPrecision(precision);
                columnDataType.setLiteralPrefix(literalPrefix);
                columnDataType.setLiteralSuffix(literalSuffix);
                columnDataType.setCreateParameters(createParameters);
                columnDataType.setNullable(isNullable);
                columnDataType.setCaseSensitive(isCaseSensitive);
                columnDataType.setSearchable(searchable);
                columnDataType.setUnsigned(isUnsigned);
                columnDataType.setFixedPrecisionScale(isFixedPrecisionScale);
                columnDataType.setAutoIncrementable(isAutoIncremented);
                columnDataType.setLocalTypeName(localTypeName);
                columnDataType.setMinimumScale(minimumScale);
                columnDataType.setMaximumScale(maximumScale);
                columnDataType.setNumPrecisionRadix(numPrecisionRadix);

                columnDataType.addAttributes(results.getAttributes());

                jdbcTypes.add(columnDataType);
                List<ColumnDataType> types = columnDataTypeMap.get(typeName);
                if (types == null) {
                    types = new ArrayList<ColumnDataType>();
                    columnDataTypeMap.put(typeName, types);
                }
                types.add(columnDataType);

                types = jdbcTypeMap.get(jdbcType);
                if (types == null) {
                    types = new ArrayList<ColumnDataType>();
                    jdbcTypeMap.put(jdbcType, types);
                }
                types.add(columnDataType);
            }

            /*
             * 关键字
             */
            final String keyWordFilename = getKeywordsFileName();
            InputStream is = TypeResolver.class.getResourceAsStream(keyWordFilename);
            BufferedReader dr = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String line;
            while ((line = dr.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                keywords.add(line);
            }

            /*
             * 系统函数
             */
            final String systemFunctionFilename = getSystemFunctionsFileName();
            InputStream fis = TypeResolver.class.getResourceAsStream(systemFunctionFilename);
            systemFunctions = ParserFunction.parserFunctions(fis);

            /*
             * 取元数据的本地sql
             */
            final String schemaInfomationsFilename = getSchemaInfomationFileName();
            siis = TypeResolver.class.getResourceAsStream(schemaInfomationsFilename);
            Properties properties = new Properties();
            properties.load(siis);
            for (Map.Entry entry : properties.entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                schemaInfomations.put(key, value);
            }
        } catch (Exception ex) {
            throw new BaseException( "MetaDialect.initialize Failed : " + ex.getMessage(),ex);
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                IOUtils.closeQuietly(siis);
            } catch (SQLException ex) {
            }
        }
    }
}
