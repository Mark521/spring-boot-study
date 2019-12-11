package mark.component.dbdialect.def;

import mark.component.core.exception.BaseException;
import mark.component.dbmodel.constans.*;
import mark.component.dbmodel.model.*;
import mark.component.dbmodel.util.MetadataResultSet;
import mark.component.dbmodel.util.Utility;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class TableRetriever extends AbstractRetriever {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableRetriever.class);
    //private static final Logger LOGGER = Logger.getLogger(TableRetriever.class.getName());

    private static String[] toStrings(final Collection<TableType> tableTypes) {
        if (tableTypes == null || tableTypes.isEmpty()) {
            return new String[0];
        }

        final List<String> tableTypeStrings = new ArrayList<String>(tableTypes.size());
        for (final TableType tableType : tableTypes) {
            if (tableType != null) {
                tableTypeStrings.add(tableType.toString().toUpperCase(Locale.ENGLISH));
            }
        }
        return tableTypeStrings.toArray(new String[tableTypeStrings.size()]);
    }

    public TableRetriever(final Connection connection) {
        super(connection);
    }

    public Collection<Column> retrieveColumns(final Table table, final long retrieveRule) throws BaseException {
        return retrieveColumns(table, null, retrieveRule);
    }

    public Collection<Column> retrieveColumns(final Table table, final String namePattern, final long retrieveRule) throws BaseException {
        MetadataResultSet results = null;
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        Collection<Column> columns = new ArrayList<Column>();
        try {
            results = new MetadataResultSet(getMetaData().getColumns(
                    unquoted(table.getSchema().getCatalogName()),
                    unquoted(table.getSchema().getName()),
                    unquoted(table.getName()),
                    unquoted(namePattern)));

            while (results.next()) {
                // Get the "COLUMN_DEF" value first as it the Oracle drivers
                // don't handle it properly otherwise.
                // http://issues.apache.org/jira/browse/DDLUTILS-29?page=all
                final String defaultValue = results.getString("COLUMN_DEF");

                final String columnCatalogName = quoted(results.getString("TABLE_CAT"), quoted);
                final String schemaName = quoted(results.getString("TABLE_SCHEM"), quoted);
                final String tableName = quoted(results.getString("TABLE_NAME"), quoted);
                final String columnName = quoted(results.getString("COLUMN_NAME"), quoted);

                if (equal(table.getName(), tableName) && (StringUtils.isBlank(table.getSchema().getName())||belongsToSchema(table, columnCatalogName, schemaName))) {

                    Column column = lookupOrCreateColumn(table, columnName);
                    final int ordinalPosition = results.getInt("ORDINAL_POSITION", 0);
                    final int dataType = results.getInt("DATA_TYPE", 0);
                    final String typeName = results.getString("TYPE_NAME");
                    final int size = results.getInt("COLUMN_SIZE", 0);
                    final int decimalDigits = results.getInt("DECIMAL_DIGITS", 0);
                    final boolean isNullable = results.getInt("NULLABLE", DatabaseMetaData.columnNullableUnknown) == DatabaseMetaData.columnNullable;
                    final String remarks = results.getString("REMARKS");

                    column.setOrdinalPosition(ordinalPosition);
                    column.setType(lookupOrCreateColumnDataType(table.getSchema(), dataType, typeName));
                    column.setPrecision(size);
                    column.setScale(decimalDigits);
                    column.setRemarks(remarks);
                    column.setNullable(isNullable);
                    if (defaultValue != null) {
                        column.setDefaultValue(defaultValue);
                    }

                    column.addAttributes(results.getAttributes());
                    columns.add(column);
                    table.addColumn(column);
                }
            }

        } catch (final SQLException ex) {
            throw new BaseException("Could not retrieve columns for table " + table, ex);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }
        return columns;
    }

    public NamedObjectList<ForeignKey> retrieveImportedKeys(final Table table, final long retrieveRule) throws BaseException {
        final NamedObjectList<ForeignKey> foreignKeys = new NamedObjectList<ForeignKey>();
        MetadataResultSet results;

        try {
            final DatabaseMetaData metaData = getMetaData();
            results = new MetadataResultSet(metaData.getImportedKeys(unquoted(table.getSchema().getCatalogName()),
                    unquoted(table.getSchema().getName()),
                    unquoted(table.getName())));
            createForeignKeys(table,results, foreignKeys, retrieveRule);
        } catch (final SQLException ex) {
            throw new BaseException("Could not retrieve forign keys for table " + table, ex);
        }
        return foreignKeys;
    }

    public NamedObjectList<ForeignKey> retrieveExportedKeys(final Table table, final long retrieveRule) throws BaseException {
        final NamedObjectList<ForeignKey> foreignKeys = new NamedObjectList<ForeignKey>();
        MetadataResultSet results;

        try {
            final DatabaseMetaData metaData = getMetaData();

            results = new MetadataResultSet(metaData.getExportedKeys(unquoted(table.getSchema().getCatalogName()),
                    unquoted(table.getSchema().getName()),
                    unquoted(table.getName())));
            createForeignKeys(table,results, foreignKeys, retrieveRule);
        } catch (final SQLException ex) {
            throw new BaseException("Could not retrieve forign keys for table " + table, ex);
        }
        return foreignKeys;
    }

    public NamedObjectList<ForeignKey> retrieveForeignKeys(final Table table, final long retrieveRule) throws BaseException {
        final NamedObjectList<ForeignKey> foreignKeys = new NamedObjectList<ForeignKey>();
        MetadataResultSet results;

        try {
            final DatabaseMetaData metaData = getMetaData();
            results = new MetadataResultSet(metaData.getImportedKeys(unquoted(table.getSchema().getCatalogName()),
                    unquoted(table.getSchema().getName()),
                    unquoted(table.getName())));
            createForeignKeys(table,results, foreignKeys, retrieveRule);

            ////注释下面代码：外键不属于被引用的表 修复某个表的外键对应的引用表（参考表）也被强行加了这个外键记录 hxf 20180629
            /*results = new MetadataResultSet(metaData.getExportedKeys(unquoted(table.getSchema().getCatalogName()),
                    unquoted(table.getSchema().getName()),
                    unquoted(table.getName())));
            createForeignKeys(results, foreignKeys, retrieveRule);*/
            if(foreignKeys.size()>0){
                for(ForeignKey fk:foreignKeys){
                    table.addForeignKey(fk);
                }
            }

        } catch (final SQLException ex) {
            throw new BaseException("Could not retrieve forign keys for table " + table, ex);
        }
        return foreignKeys;
    }





    public Collection<Index> retrieveIndices(final Table table, final String indexPatternName, final boolean unique, final long retrieveRule) throws BaseException {
        try {
            retrieveIndices1(table, indexPatternName, unique, retrieveRule);
        } catch (final BaseException e) {
            retrieveIndices2(table, indexPatternName, unique, retrieveRule);
        }

        return table.getIndices();
    }

    public PrimaryKey retrievePrimaryKey(final Table table, final long retrieveRule) throws BaseException {
        MetadataResultSet results = null;
        try {
            results = new MetadataResultSet(getMetaData().getPrimaryKeys(unquoted(table.getSchema().getCatalogName()),
                    unquoted(table.getSchema().getName()),
                    unquoted(table.getName())));

            boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
            PrimaryKey primaryKey = null;
            while (results.next()) {
                // "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME"
                final String columnName = quoted(results.getString("COLUMN_NAME"), quoted);
                final String primaryKeyName = quoted(results.getString("PK_NAME"), quoted);
                final int keySequence = Integer.parseInt(results.getString("KEY_SEQ"));

                primaryKey = table.getPrimaryKey();
                if (primaryKey == null) {
                    primaryKey = new PrimaryKey(table, primaryKeyName);
                }

                Column column = table.getColumn(columnName);
                //retrieve column
                if (column == null) {
                    retrieveColumns(table, columnName, retrieveRule);
                    column = table.getColumn(columnName);
                }
                // Register primary key information
                if (column != null) {
                    column.setPartOfPrimaryKey(true);
                    final IndexColumn indexColumn = new IndexColumn(primaryKey, column);
                    indexColumn.setSortSequence(IndexColumnSortSequence.ascending);
                    indexColumn.setIndexOrdinalPosition(keySequence);
                    primaryKey.addColumn(indexColumn);
                }

                table.setPrimaryKey(primaryKey);
            }

            return primaryKey;
        } catch (final SQLException ex) {
            throw new BaseException("Could not retrieve primary keys for table " + table, ex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }
        return null;
    }

    public Set<Table> retrieveTablesByPage(String schemaName,String tableNamePattern,String querySQL, Integer limit, Integer offset, final long retrieveRule) throws BaseException {
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        Set<Table> tables = new HashSet<Table>();

        PreparedStatement preparedStatement = null;
        MetadataResultSet results = null;
        if(StringUtils.isBlank(querySQL)){
            return tables;
        }
        if(StringUtils.isBlank(tableNamePattern)){
            tableNamePattern = "%";
        }
        try{
            preparedStatement = getConnection().prepareStatement(querySQL);
            preparedStatement.setString(1, unquoted(schemaName));
            preparedStatement.setString(2, unquoted(tableNamePattern));
            preparedStatement.setInt(3,limit);
            preparedStatement.setInt(4,offset);
            LOGGER.info("querySQL:{}, schema:{}, tableName:{},limit:{}, offset:{}"
                    ,querySQL, schemaName, tableNamePattern, limit, offset);
            results = new MetadataResultSet(preparedStatement.executeQuery());
            while (results.next()) {
                // "TABLE_CAT", "TABLE_SCHEM"
                String tableName = results.getString("TABLE_NAME");

                tableName = quoted(tableName, quoted);
                final TableType tableType = results.getEnum("TABLE_TYPE", TableType.unknown);
                String tableCatalogName = quoted(results.getString("TABLE_CAT"), quoted);
                String tableSchema = quoted(results.getString("TABLE_SCHEM"), quoted);
                final String remarks = results.getString("REMARKS");


                //final Schema schema = new Schema(catalogName, schemaName);
                final Schema schema = new Schema(tableCatalogName, tableSchema);

                final Table table;
                if (tableType == TableType.view) {
                    View view = new View(schema, tableName);
                    table=view;
                } else {
                    table = new Table(schema, tableName);
                }
                table.setType(tableType);
                table.setRemarks(remarks);
                tables.add(table);
            }
        } catch (SQLException ex){
            throw new BaseException("", ex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
            DbUtils.closeQuietly(preparedStatement);
        }
        return tables;

    }

    public Integer getTablesCount(final String schemaName,String tableNamePattern,String querySQL)throws BaseException{
        Integer currValue = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        if(StringUtils.isBlank(querySQL)){
            return 0;
        }
        if(StringUtils.isBlank(tableNamePattern)){
            tableNamePattern = "%";
        }
        try{
            LOGGER.info("querySQL:{}, schema:{}, tableName:{}",querySQL, unquoted(schemaName), unquoted(tableNamePattern));
            preparedStatement = getConnection().prepareStatement(querySQL);
            preparedStatement.setString(1, unquoted(schemaName));
            preparedStatement.setString(2, unquoted(tableNamePattern));

            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                currValue = rs.getInt(1);
            }
        } catch (SQLException ex){
            throw new BaseException("", ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(preparedStatement);
        }
        return currValue;
    }

    public Set<Table> retrieveTables(final String catalogName,
                                     String schemaName,
                                     String tableNamePattern,
                                     final Collection<TableType> tableTypes,
                                     final long retrieveRule)
            throws BaseException {

        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        boolean regex = RetrieveRule.REGEX == (RetrieveRule.REGEX & retrieveRule);
        Pattern pattern = null;
        Set<Table> tables = new HashSet<Table>();
        MetadataResultSet results = null;
        if (tableNamePattern == null || tableNamePattern.isEmpty()) {
            tableNamePattern = null;
            regex = false;
        } else if (regex) {
            pattern = Pattern.compile(tableNamePattern.toUpperCase());
            tableNamePattern = null;
        } else {
            tableNamePattern = unquoted(tableNamePattern);
        }

        try {
            results = new MetadataResultSet(getMetaData().getTables(unquoted(catalogName),
                    unquoted(schemaName),
                    tableNamePattern,
                    toStrings(tableTypes)));

            while (results.next()) {
                // "TABLE_CAT", "TABLE_SCHEM"
                String tableName = results.getString("TABLE_NAME");
                if (regex) {
                    Matcher matcher = pattern.matcher(tableName.toUpperCase());
                    if (!matcher.matches()) {
                        continue;
                    }
                }

                tableName = quoted(tableName, quoted);
                final TableType tableType = results.getEnum("TABLE_TYPE", TableType.unknown);
                final String remarks = results.getString("REMARKS");

                String tableCatalogName = quoted(results.getString("TABLE_CAT"), quoted);
                String tableSchema = quoted(results.getString("TABLE_SCHEM"), quoted);

                //final Schema schema = new Schema(catalogName, schemaName);
                final Schema schema = new Schema(tableCatalogName, tableSchema);

                final Table table;
                if (tableType == TableType.view) {
                    View view = new View(schema, tableName);
                    table=view;
                } else {
                    table = new Table(schema, tableName);
                }
                table.setType(tableType);
                table.setRemarks(remarks);
                tables.add(table);
            }
        } catch (SQLException ex) {
            throw new BaseException("", ex);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }

        return tables;
    }

    public Set<View> retrieveViews(final String catalogName,
                                   String schemaName,
                                   final String tableNamePattern,
                                   final long retrieveRule)
            throws BaseException {

        final Collection<TableType> tableTypes = Collections.singleton(TableType.view);
        Set<Table> tables = retrieveTables(catalogName, schemaName, tableNamePattern, tableTypes, retrieveRule);
        Set<View> views = new HashSet<View>();
        for (Table table : tables) {
            views.add((View) table);
        }
        return views;
    }

    private void createForeignKeys(final Table table,final MetadataResultSet results, final NamedObjectList<ForeignKey> foreignKeys, final long retrieveRule)
            throws BaseException {
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        try {
            while (results.next()) {
                String foreignKeyName = quoted(results.getString("FK_NAME"), quoted);
                if (Utility.isBlank(foreignKeyName)) {
                    foreignKeyName = UNKNOWN;
                }

                final String pkTableCatalogName = quoted(results.getString("PKTABLE_CAT"), quoted);
                final String pkTableSchemaName = quoted(results.getString("PKTABLE_SCHEM"), quoted);
                final String pkTableName = quoted(results.getString("PKTABLE_NAME"), quoted);
                final String pkColumnName = quoted(results.getString("PKCOLUMN_NAME"), quoted);

                final String fkTableCatalogName = quoted(results.getString("FKTABLE_CAT"), quoted);
                final String fkTableSchemaName = quoted(results.getString("FKTABLE_SCHEM"), quoted);
                final String fkTableName = quoted(results.getString("FKTABLE_NAME"), quoted);
                final String fkColumnName = quoted(results.getString("FKCOLUMN_NAME"), quoted);

                ForeignKey foreignKey = foreignKeys.lookup(foreignKeyName);
                if (foreignKey == null) {
                    foreignKey = new ForeignKey(table,foreignKeyName);

                }

                final int keySequence = results.getInt("KEY_SEQ", 0);
                final int updateRule = results.getInt("UPDATE_RULE", ForeignKeyUpdateRule.unknown.getId());
                final int deleteRule = results.getInt("DELETE_RULE", ForeignKeyUpdateRule.unknown.getId());
                final int deferrability = results.getInt("DEFERRABILITY", ForeignKeyDeferrability.unknown.getId());

                final Column pkColumn = lookupOrCreateColumn(pkTableCatalogName,
                        pkTableSchemaName,
                        pkTableName,
                        pkColumnName);
                final Column fkColumn = lookupOrCreateColumn(fkTableCatalogName,
                        fkTableSchemaName,
                        fkTableName,
                        fkColumnName);
                // Make a direct connection between the two columns
                if (pkColumn != null && fkColumn != null) {
                    foreignKey.addColumnReference(keySequence, pkColumn, fkColumn);
                    foreignKey.setUpdateRule(ForeignKeyUpdateRule.valueOf(updateRule));
                    foreignKey.setDeleteRule(ForeignKeyUpdateRule.valueOf(deleteRule));
                    foreignKey.setDeferrability(ForeignKeyDeferrability.valueOf(deferrability));
                    foreignKey.addAttributes(results.getAttributes());
                    foreignKey.setReferenceSchema(pkColumn.getSchema().getFullName());
                    foreignKey.setReferenceTable(fkColumn.getSchema().getFullName());
                    fkColumn.setReferencedColumn(pkColumn);
                    pkColumn.getParent().addForeignKey(foreignKey);
                    fkColumn.getParent().addForeignKey(foreignKey);
                }
                foreignKeys.add(foreignKey);
            }
        } catch (SQLException ex) {
            throw new BaseException("", ex);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    private void createIndices(final Table table, String indexPatternName, final MetadataResultSet results, final long retrieveRule) throws BaseException {
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        Pattern pattern = null;
        boolean regex = RetrieveRule.REGEX == (RetrieveRule.REGEX & retrieveRule);
        if (indexPatternName == null || indexPatternName.isEmpty()) {
            indexPatternName = null;
            regex = false;
        } else if (regex) {
            pattern = Pattern.compile(indexPatternName);
            indexPatternName = null;
        } else {
            indexPatternName = unquoted(indexPatternName);
        }
        try {
            while (results.next()) {
                // "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME"
                String indexName = results.getString("INDEX_NAME");
                if (regex) {
                    Matcher matcher = pattern.matcher(indexName);
                    if (!matcher.matches()) {
                        continue;
                    }
                }
                indexName = quoted(indexName, quoted);
                if (Utility.isBlank(indexName)) {
                    indexName = UNKNOWN;
                }
                final String columnName = quoted(results.getString("COLUMN_NAME"), quoted);
                if (Utility.isBlank(columnName)) {
                    continue;
                }

                Index index = table.getIndex(indexName);
                if (index == null) {
                    index = new Index(table, indexName);
                }

                final boolean uniqueIndex = !results.getBoolean("NON_UNIQUE");
                final int type = results.getInt("TYPE", IndexType.unknown.getId());
                final int ordinalPosition = results.getInt("ORDINAL_POSITION", 0);
                final IndexColumnSortSequence sortSequence = IndexColumnSortSequence.valueOfFromCode(results.getString("ASC_OR_DESC"));
                final int cardinality = results.getInt("CARDINALITY", 0);
                final int pages = results.getInt("PAGES", 0);

                final Column column = table.getColumn(columnName);
                if (column != null) {
                    column.setPartOfUniqueIndex(uniqueIndex);
                    final IndexColumn indexColumn = new IndexColumn(index, column);
                    indexColumn.setIndexOrdinalPosition(ordinalPosition);
                    indexColumn.setSortSequence(sortSequence);
                    //
                    index.addColumn(indexColumn);
                    index.setUnique(uniqueIndex);
                    index.setType(IndexType.valueOf(type));
                    index.setCardinality(cardinality);
                    index.setPages(pages);
                    index.addAttributes(results.getAttributes());
                }
                table.addIndex(index);
            }
        } catch (SQLException ex) {
            throw new BaseException("", ex);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    private Column lookupOrCreateColumn(final Table table, final String columnName) {
        Column column = null;
        if (table != null) {
            column = table.getColumn(columnName);
        }
        if (column == null) {
            column = new Column(table, columnName);
        }
        return column;
    }

    private Column lookupOrCreateColumn(final String catalogName,
                                        final String schemaName,
                                        final String tableName,
                                        final String columnName) throws BaseException {
        final boolean supportsCatalogs = supportCatalog();
        Column column = null;
        final Schema schema = new Schema(supportsCatalogs ? catalogName : null, schemaName);
        Table table = new Table(schema, tableName);
        column = new Column(table, columnName);
        table.addColumn(column);

        return column;
    }

    private void retrieveIndices1(final Table table, String indexPatternName, final boolean unique, final long retrieveRule) throws BaseException {
        MetadataResultSet results = null;

        try {
            results = new MetadataResultSet(getMetaData().getIndexInfo(unquoted(table.getSchema().getCatalogName()),
                    unquoted(table.getSchema().getName()),
                    unquoted(table.getName()),
                    unique,
                    true/* approximate */));
            createIndices(table, indexPatternName, results, retrieveRule);
        } catch (final SQLException ex) {
            throw new BaseException("Could not retrieve indices for table " + table, ex);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    private void retrieveIndices2(final Table table, final String indexPatternName, final boolean unique, final long retrieveRule) throws BaseException {
        MetadataResultSet results = null;
        try {
            if (Character.isDigit(table.getName().charAt(0))
                    || table.getName().indexOf('-') >= 0
                    || table.getName().length() != table.getName().getBytes().length) {
                //System.out.println(table);
                results = new MetadataResultSet(getMetaData().getIndexInfo(null, null, quoted(table.getName()), unique, true/* approximate */));
                createIndices(table, indexPatternName, results, retrieveRule);
                return;
            }

            results = new MetadataResultSet(getMetaData().getIndexInfo(null, null, table.getName(), unique, true/* approximate */));
            createIndices(table, indexPatternName, results, retrieveRule);
        } catch (final SQLException ex) {
            throw new BaseException("Could not retrieve indices for table " + table, ex);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    public void retrieveTableColumnPrivileges(final Column column, final long retrieveRule) throws BaseException {
        MetadataResultSet results = null;
        final Table table = column.getParent();
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        try {
            results = new MetadataResultSet(getMetaData().getColumnPrivileges(
                    unquoted(table.getSchema().getCatalogName()),
                    unquoted(table.getSchema().getName()),
                    unquoted(table.getName()),
                    unquoted(column.getName())));

            while (results.next()) {
                final String catalogName = quoted(results.getString("TABLE_CAT"), quoted);
                final String schemaName = quoted(results.getString("TABLE_SCHEM"), quoted);
                final String tableName = quoted(results.getString("TABLE_NAME"), quoted);
                final String columnName = quoted(results.getString("COLUMN_NAME"), quoted);
                final String privilegeName = results.getString("PRIVILEGE");
                final String grantor = results.getString("GRANTOR");
                final String grantee = results.getString("GRANTEE");
                final boolean isGrantable = results.getBoolean("IS_GRANTABLE");

                Privilege<Column> columnPrivilege = column.getPrivilege(privilegeName);
                if (columnPrivilege == null) {
                    columnPrivilege = new Privilege<Column>(column, quoted(privilegeName, quoted));
                }
                columnPrivilege.addGrant(grantor, grantee, isGrantable);
                columnPrivilege.addAttributes(results.getAttributes());
                column.addPrivilege(columnPrivilege);
            }
        } catch (final SQLException ex) {
            throw new BaseException("", ex);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    public void retrieveTablePrivileges(final Table table, final long retrieveRule) throws BaseException {
        MetadataResultSet results = null;
        try {
            results = new MetadataResultSet(getMetaData().getTablePrivileges(
                    unquoted(table.getSchema().getCatalogName()),
                    unquoted(table.getSchema().getName()),
                    unquoted(table.getName())));
            while (results.next()) {

                final String catalogName = quoted(results.getString("TABLE_CAT"));
                final String schemaName = quoted(results.getString("TABLE_SCHEM"));
                final String tableName = quoted(results.getString("TABLE_NAME"));

                final String privilegeName = results.getString("PRIVILEGE");
                final String grantor = results.getString("GRANTOR");
                final String grantee = results.getString("GRANTEE");
                final boolean isGrantable = results.getBoolean("IS_GRANTABLE");

                Privilege<Table> tablePrivilege = table.getPrivilege(privilegeName);
                if (tablePrivilege == null) {
                    tablePrivilege = new Privilege<Table>(table, privilegeName);
                }
                tablePrivilege.addGrant(grantor, grantee, isGrantable);
                tablePrivilege.addAttributes(results.getAttributes());

                table.addPrivilege(tablePrivilege);

            }
        } catch (final Exception e) {
            LOGGER.warn("Could not retrieve table privileges", e);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
}
