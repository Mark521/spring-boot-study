package mark.component.dbdialect.oracle;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbmodel.constans.FunctionColumnType;
import mark.component.dbmodel.constans.RetrieveRule;
import mark.component.dbmodel.model.*;
import mark.component.dbmodel.util.MetadataResultSet;
import mark.component.core.exception.BaseException;

import java.sql.*;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "Oracle", majorVersion = 10)
public class Oracle10gMetaDialect extends OracleMetaDialect {

    private static final Logger LOGGER = Logger.getLogger(Oracle10gMetaDialect.class.getName());

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.oracle_10g.properties";
    }

    @Override
    protected String getJdbcTypeFileName() {
        return "oracle_10g_types.properties";
    }

    @Override
    protected String getJava2JdbcFileName() {
        return "oracle_10g_java2jdbc_types.properties";
    }

    @Override
    protected String getJdbc2JavaFileName() {
        return "oracle_10g_jdbc2java_types.properties";
    }

    protected String getKeywordsFileName() {
        return "/keywords/oracle_10g_keywords.txt";
    }

    protected String getSystemFunctionsFileName() {
        return "/functions/oracle_10g_system_functions.xml";
    }

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setProductName("Oracle");
        databaseInfo.setMajorVersion(10);
        databaseInfo.setMinorVersion(0);
        return databaseInfo;
    }

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn,
                                                            final String catalogName, final String schemaName, final String functionName, final long retrieveRule)
            throws BaseException {
        return retrieveFunctionColumns(conn, catalogName, schemaName, functionName, retrieveRule);
    }

    public Collection<FunctionColumn> getFunctionPatameters(final Connection conn, final Function function, final long retrieveRule)
            throws BaseException {
        return retrieveFunctionColumns(conn, function, retrieveRule);
    }

    public Collection<FunctionColumn> retrieveFunctionColumns(final Connection connection, String catalogName, String schemaName, String functionName, final long retrieveRule) throws BaseException {
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        Schema schema = new Schema(quote(catalogName, quoted), quote(schemaName, quoted));
        Function function = new Function(schema, functionName);
        return retrieveFunctionColumns(connection, function, retrieveRule);
    }

    public Collection<FunctionColumn> retrieveFunctionColumns(final Connection connection, final Function function, final long retrieveRule)
            throws BaseException {
        MetadataResultSet results = null;
        int ordinalNumber = 0;
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        Schema schema = function.getSchema();
        String catalogName = null;
        String schemaName = null;
        if (schema != null) {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        try {
            ResultSet rs = getFunctionColumns(connection, unquote(catalogName), unquote(schemaName), unquote(function.getName()), null);
            results = new MetadataResultSet(rs);

            while (results.next()) {
                //final String columnCatalogName = quoted(results.getString("FUNCTION_CAT"),quoted);
                //final String columnSchemaName = quoted(results.getString("FUNCTION_SCHEM"),quoted);
                //final String functionName = quoted(results.getString("FUNCTION_NAME"),quoted);
                final String columnName = quote(results.getString("COLUMN_NAME"), quoted);
                // String specificName = quoted(results.getString("SPECIFIC_NAME"),quoted);

                final FunctionColumn column = new FunctionColumn(function, columnName);
                //final String columnFullName = column.getFullName();
                final short columnType = results.getShort("COLUMN_TYPE", (short) 0);
                final int dataType = results.getInt("DATA_TYPE", 0);
                final String typeName = results.getString("TYPE_NAME");
                final int length = results.getInt("LENGTH", 0);
                final int precision = results.getInt("PRECISION", 0);
                final boolean isNullable = results.getShort("NULLABLE",
                        (short) DatabaseMetaData.functionNullableUnknown) == (short) DatabaseMetaData.functionNullable;
                final String remarks = results.getString("REMARKS");
                column.setOrdinalPosition(ordinalNumber++);
                column.setFunctionColumnType(FunctionColumnType.valueOf(columnType));
                column.setType(lookupOrCreateColumnDataType(function.getSchema(), dataType, typeName));
                column.setPrecision(length);
                column.setPrecision(precision);
                column.setNullable(isNullable);
                column.setRemarks(remarks);

                column.addAttributes(results.getAttributes());

                function.addColumn(column);
            }
        } catch (final AbstractMethodError e) {
            LOGGER.log(Level.WARNING, "JDBC driver does not support retrieving functions", e);
        } catch (final SQLFeatureNotSupportedException e) {
            LOGGER.log(Level.WARNING, "JDBC driver does not support retrieving functions", e);
        } catch (final SQLException e) {
            throw new BaseException("Could not retrieve columns for function " + function, e);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }
        return function.getColumns();
    }

    private ColumnDataType lookupOrCreateColumnDataType(final Schema schema, final int jdbcType, final String jdbcTypeNmae) throws BaseException {
        Collection<ColumnDataType> columnDataTypes = getColumnDataTypes(jdbcTypeNmae);
        ColumnDataType columnDataType;
        if (columnDataTypes == null || columnDataTypes.isEmpty()) {
            columnDataType = new ColumnDataType(schema, jdbcTypeNmae);
            columnDataType.setJdbcType(jdbcType);
        } else {
            columnDataType = columnDataTypes.iterator().next();
        }
        return columnDataType;

    }

    public static ResultSet getFunctionColumns(Connection connection,
            String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern)
            throws SQLException {
        try {

            String baseQuery = new StringBuilder().append("SELECT package_name AS function_cat,\n       arg.owner AS function_schem,\n       arg.object_name AS function_name,\n       arg.argument_name AS column_name,\n       DECODE(arg.position, 0, 5,\n                        DECODE(arg.in_out, 'IN', 1,\n                                       'OUT', 4,\n                                       'IN/OUT', 2,\n                                       0)) AS column_type,\n       DECODE (arg.data_type, 'CHAR', 1,\n                          'VARCHAR2', 12,\n                          'NUMBER', 3,\n                          'LONG', -1,\n                          'DATE', ").append("93,\n").append("                          'RAW', -3,\n").append("                          'LONG RAW', -4,\n").append("                          'TIMESTAMP', 93, \n").append("                          'TIMESTAMP WITH TIME ZONE', -101, \n").append("               'TIMESTAMP WITH LOCAL TIME ZONE', -102, \n").append("               'INTERVAL YEAR TO MONTH', -103, \n").append("               'INTERVAL DAY TO SECOND', -104, \n").append("               'BINARY_FLOAT', 100, 'BINAvRY_DOUBLE', 101,").append("               1111) AS data_type,\n").append("       DECODE(arg.data_type, 'OBJECT', arg.type_owner || '.' || arg.type_name, ").append("              arg.data_type) AS type_name,\n").append("       DECODE (arg.data_precision, NULL, arg.data_length,\n").append("                               arg.data_precision) AS precision,\n").append("       arg.data_length AS length,\n").append("       arg.data_scale AS scale,\n").append("       10 AS radix,\n").append("       1 AS nullable,\n").append("       NULL AS remarks,\n").append("       arg.default_value AS column_def,\n").append("       NULL as sql_data_type,\n").append("       NULL AS sql_datetime_sub,\n").append("       DECODE(arg.data_type,\n").append("                         'CHAR', 32767,\n").append("                         'VARCHAR2', 32767,\n").append("                         'LONG', 32767,\n").append("                         'RAW', 32767,\n").append("                         'LONG RAW', 32767,\n").append("                         NULL) AS char_octet_length,\n").append("       (arg.sequence - 1) AS ordinal_position,\n").append("       'YES' AS is_nullable,\n").append("       NULL AS specific_name,\n").append("       arg.sequence,\n").append("       arg.overload,\n").append("       arg.default_value\n").append(" FROM all_arguments arg, all_procedures proc\n").append(" WHERE arg.owner LIKE :1 ESCAPE '/'\n").append("  AND arg.object_name LIKE :2 ESCAPE '/'\n").append(" AND arg.argument_name IS NOT NULL\n").toString();

            short db_version = 10100;

            String objectIDWhere = db_version >= 10200 ? "  AND proc.object_id = arg.object_id\n  AND proc.object_type = 'FUNCTION'\n" : "  AND proc.owner = arg.owner\n  AND proc.object_name = arg.object_name\n";

            String catalogSpecifiedWhere = "  AND arg.package_name LIKE :3 ESCAPE '/'\n";

            String catalogEmptyWhere = "  AND arg.package_name IS NULL\n";

            String columnSpecifiedWhere = "  AND arg.argument_name LIKE :4 ESCAPE '/'\n";

            String columnNotSpecifiedWhere = "  AND (arg.argument_name LIKE :5 ESCAPE '/'\n     OR (arg.argument_name IS NULL\n         AND arg.data_type IS NOT NULL))\n";

            String orderBy = "ORDER BY function_schem, function_name, overload, sequence\n";

            String finalQuery = null;
            PreparedStatement s = null;
            String columnWhere = null;

            String schemaBind = schemaPattern;

            if (schemaPattern == null) {
                schemaBind = "%";
            } else if (schemaPattern.equals("")) {
                schemaBind = connection.getCatalog().toUpperCase();//getUserName().toUpperCase();
            }
            String procedureNameBind = functionNamePattern;

            if (functionNamePattern == null) {
                procedureNameBind = "%";
            } else if (functionNamePattern.equals("")) {
                SQLException __ex__ = new SQLException("functionName can't be enpty.");
                __ex__.fillInStackTrace();
                throw __ex__;
            }

            String columnNameBind = columnNamePattern;

            if ((columnNamePattern == null) || (columnNamePattern.equals("%"))) {
                columnNameBind = "%";
                columnWhere = columnNotSpecifiedWhere;
            } else {
                if (columnNamePattern.equals("")) {
                    SQLException __ex__ = new SQLException("columnName can't be enpty.");
                    __ex__.fillInStackTrace();
                    throw __ex__;
                }

                columnWhere = columnSpecifiedWhere;
            }
            if (catalog == null) {
                finalQuery = new StringBuilder().append(baseQuery).append(objectIDWhere).append(columnWhere).append(orderBy).toString();

                s = connection.prepareStatement(finalQuery);

                s.setString(1, schemaBind);
                s.setString(2, procedureNameBind);
                s.setString(3, columnNameBind);
            } else if (catalog.equals("")) {
                finalQuery = new StringBuilder().append(baseQuery).append(objectIDWhere).append(catalogEmptyWhere).append(columnWhere).append(orderBy).toString();

                s = connection.prepareStatement(finalQuery);

                s.setString(1, schemaBind);
                s.setString(2, procedureNameBind);
                s.setString(3, columnNameBind);
            } else {
                finalQuery = new StringBuilder().append(baseQuery).append(objectIDWhere).append(catalogSpecifiedWhere).append(columnWhere).append(orderBy).toString();

                System.out.println(finalQuery);
                s = connection.prepareStatement(finalQuery);

                s.setString(1, schemaBind);
                s.setString(2, procedureNameBind);
                s.setString(3, catalog);
                s.setString(4, columnNameBind);
            }

            ResultSet rs = s.executeQuery();

            return rs;
        } finally {
        }
    }
}
