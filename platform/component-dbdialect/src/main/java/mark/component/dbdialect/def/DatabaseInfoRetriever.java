package mark.component.dbdialect.def;

import mark.component.dbmodel.constans.SearchableType;
import mark.component.dbmodel.model.*;
import mark.component.dbmodel.util.MetadataResultSet;
import mark.component.core.exception.BaseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

final class DatabaseInfoRetriever extends AbstractRetriever {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInfoRetriever.class.getName());
    private static final List<String> ignoreMethods = Arrays.asList("getDatabaseProductName",
            "getDatabaseProductVersion",
            "getURL",
            "getUserName",
            "getDriverName",
            "getDriverVersion");

    private static boolean isDatabasePropertiesResultSetMethod(final Method method) {
        final Class<?> returnType = method.getReturnType();
        final boolean isPropertiesResultSetMethod = returnType.equals(ResultSet.class) && method.getParameterTypes().length == 0;

        return isPropertiesResultSetMethod;
    }

    private static boolean isDatabasePropertyMethod(final Method method) {
        final Class<?> returnType = method.getReturnType();
        final boolean notPropertyMethod = returnType.equals(ResultSet.class)
                || returnType.equals(Connection.class)
                || method.getParameterTypes().length > 0;
        return !notPropertyMethod;
    }

    private static boolean isDatabasePropertyResultSetType(final Method method) {
        final String[] databasePropertyResultSetTypes = new String[]{
            "deletesAreDetected",
            "insertsAreDetected",
            "updatesAreDetected",
            "othersDeletesAreVisible",
            "othersInsertsAreVisible",
            "othersUpdatesAreVisible",
            "ownDeletesAreVisible",
            "ownInsertsAreVisible",
            "ownUpdatesAreVisible",
            "supportsResultSetType"
        };
        final boolean isDatabasePropertyResultSetType = Arrays.binarySearch(databasePropertyResultSetTypes, method.getName()) >= 0;
        return isDatabasePropertyResultSetType;
    }

    private static DatabaseProperty retrieveResultSetTypeProperty(final DatabaseMetaData dbMetaData,
                                                                  final Method method,
                                                                  final int resultSetType,
                                                                  final String resultSetTypeName)
            throws IllegalAccessException, InvocationTargetException {
        final String name = method.getName() + "For" + resultSetTypeName
                + "ResultSets";
        Boolean propertyValue = null;
        propertyValue = (Boolean) method.invoke(dbMetaData,
                Integer.valueOf(resultSetType));

        return new DatabaseProperty(name, propertyValue);
    }

    public DatabaseInfoRetriever(final Connection connection) {
        super(connection);
    }

    public DatabaseInfo retrieveDatabaseInfo() throws BaseException {
        try {
            final DatabaseMetaData dbMetaData = getMetaData();
            final DatabaseInfo dbInfo = new DatabaseInfo();

            dbInfo.setUserName(dbMetaData.getUserName());
            dbInfo.setProductName(dbMetaData.getDatabaseProductName());
            dbInfo.setProductVersion(dbMetaData.getDatabaseProductVersion());
            dbInfo.setMajorVersion(dbMetaData.getDatabaseMajorVersion());
            dbInfo.setMinorVersion(dbMetaData.getDatabaseMinorVersion());
            retrieveAdditionalDatabaseInfo(dbInfo);
            return dbInfo;
        } catch (SQLException ex) {
            throw new BaseException(ex);
        }
    }

    public JdbcDriverInfo retrieveJdbcDriverInfo() throws BaseException {
        final JdbcDriverInfo driverInfo = new JdbcDriverInfo();

        try {
            final DatabaseMetaData dbMetaData = getMetaData();
            final String url = dbMetaData.getURL();


            if (driverInfo != null) {
                driverInfo.setDriverName(dbMetaData.getDriverName());
                driverInfo.setDriverVersion(dbMetaData.getDriverVersion());
                driverInfo.setConnectionUrl(url);
                final Driver jdbcDriver = DriverManager.getDriver(url);
                driverInfo.setJdbcDriverClassName(jdbcDriver.getClass().getName());
                driverInfo.setJdbcCompliant(jdbcDriver.jdbcCompliant());
            }
            final Driver jdbcDriver = DriverManager.getDriver(url);
            final DriverPropertyInfo[] propertyInfo = jdbcDriver.getPropertyInfo(url, new Properties());
            for (final DriverPropertyInfo driverPropertyInfo : propertyInfo) {
                driverInfo.addJdbcDriverProperty(new JdbcDriverProperty(driverPropertyInfo));
            }
        } catch (SQLException ex) {
            throw new BaseException(ex);
        }
        return driverInfo;
    }

    protected void retrieveAdditionalDatabaseInfo(DatabaseInfo dbInfo) throws SQLException {
        final DatabaseMetaData dbMetaData = getMetaData();

        final Collection<DatabaseProperty> dbProperties = new ArrayList<DatabaseProperty>();

        final Method[] methods = DatabaseMetaData.class.getMethods();
        for (final Method method : methods) {
            try {
                if (ignoreMethods.contains(method.getName())) {
                    continue;
                }
                if (isDatabasePropertyMethod(method)) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINER, "Retrieving database property using method: {0}", method);
                    }
                    Object value = method.invoke(dbMetaData);
                    if (value != null && method.getName().endsWith("s")
                            && value instanceof String) {
                        // Probably a comma-separated list
                        value = ((String) value).split(",");
                    }
                    // Add to the properties map
                    dbProperties.add(new DatabaseProperty(method.getName(), value));
                } else if (isDatabasePropertiesResultSetMethod(method)) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINER, "Retrieving database property using method: {0}", method);
                    }
                    final ResultSet results = (ResultSet) method.invoke(dbMetaData);
                    final List<String> resultsList = readResultsVector(results);
                    dbProperties.add(new DatabaseProperty(method.getName(), resultsList.toArray(new String[resultsList.size()])));
                } else if (isDatabasePropertyResultSetType(method)) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINER, "Retrieving database property using method: {0}", method);
                    }
                    dbProperties.add(retrieveResultSetTypeProperty(dbMetaData,
                            method,
                            ResultSet.TYPE_FORWARD_ONLY,
                            "TYPE_FORWARD_ONLY"));
                    dbProperties.add(retrieveResultSetTypeProperty(dbMetaData,
                            method,
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            "TYPE_SCROLL_INSENSITIVE"));
                    dbProperties.add(retrieveResultSetTypeProperty(dbMetaData,
                            method,
                            ResultSet.TYPE_SCROLL_SENSITIVE,
                            "TYPE_SCROLL_SENSITIVE"));
                }
            } catch (final IllegalAccessException e) {
                LOGGER.log(Level.FINE, "Could not execute method, " + method, e);
            } catch (final InvocationTargetException e) {
                LOGGER.log(Level.FINE,
                        "Could not execute method, " + method,
                        e.getCause());
            } catch (final AbstractMethodError e) {
                LOGGER.log(Level.FINE, "JDBC driver does not support " + method, e);
            } catch (final SQLFeatureNotSupportedException e) {
                LOGGER.log(Level.FINE, "JDBC driver does not support " + method, e);
            }
        }

        dbInfo.addAll(dbProperties);
    }

    public List<ColumnDataType> retrieveSystemColumnDataTypes() throws SQLException {
        final Schema systemSchema = new Schema();

        final MetadataResultSet results = new MetadataResultSet(getMetaData().getTypeInfo());
        List<ColumnDataType> columnDataTypes = new ArrayList<ColumnDataType>();
        try {
            while (results.next()) {
                final String typeName = results.getString("TYPE_NAME");
                final int jdbcType = results.getInt("DATA_TYPE", 0);
                LOGGER.log(Level.FINER, String.format("Retrieving data type: %s (with type id %d)",
                        typeName,
                        jdbcType));
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
                // Set the Java SQL type code, but no mapped Java class is
                // available, so use the defaults
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

                columnDataTypes.add(columnDataType);
            }
        } finally {
            results.close();
        }
        return columnDataTypes;
    }

    public List<ColumnDataType> retrieveUserDefinedColumnDataTypes(final String catalogName, final String schemaName) throws BaseException {
        List<ColumnDataType> columnDataTypes = new ArrayList<ColumnDataType>();
        MetadataResultSet results = null;
        try {
            results = new MetadataResultSet(getMetaData().getUDTs(catalogName, schemaName, "%", null));
            while (results.next()) {
                // "TYPE_CAT", "TYPE_SCHEM"
                final String typeName = results.getString("TYPE_NAME");
                LOGGER.log(Level.FINER, "Retrieving data type: {0}", typeName);
                final short jdbcType = results.getShort("DATA_TYPE", (short) 0);
                final String className = results.getString("CLASS_NAME");
                final String remarks = results.getString("REMARKS");
                final short baseTypeValue = results.getShort("BASE_TYPE", (short) 0);

                final Schema schema = new Schema(catalogName, schemaName);
                final ColumnDataType baseType = lookupOrCreateColumnDataType((int) baseTypeValue);
                final ColumnDataType columnDataType = new ColumnDataType(schema,
                        typeName);
                columnDataType.setUserDefined(true);
                columnDataType.setJdbcType(jdbcType);
                columnDataType.setClassName(className);
                columnDataType.setBaseType(baseType);
                columnDataType.setRemarks(remarks);
                columnDataType.addAttributes(results.getAttributes());
                columnDataTypes.add(columnDataType);
            }

        } catch (SQLException ex) {
            throw new BaseException(ex.getMessage());
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException ex) {
            }
        }
        return columnDataTypes;
    }
}
