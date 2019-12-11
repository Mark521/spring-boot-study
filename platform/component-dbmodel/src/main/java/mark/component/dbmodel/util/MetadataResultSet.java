package mark.component.dbmodel.util;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MetadataResultSet implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MetadataResultSet.class.getName());
    private static final int FETCHSIZE = 20;
    private final ResultSet results;
    private final List<String> resultSetColumns;
    private Set<String> readColumns;

    public MetadataResultSet(final ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            throw new IllegalArgumentException("Cannot use null results");
        }
        synchronized (this) {
	        results = resultSet;
	        try {
	            results.setFetchSize(FETCHSIZE);
	        } catch (final NullPointerException e) {
	            // Need this catch for the JDBC/ ODBC driver
	            LOGGER.log(Level.WARNING, "Could not set fetch size", e);
	        } catch (final SQLException e) {
	            LOGGER.log(Level.WARNING, "Could not set fetch size", e);
	        }
	        final List<String> resultSetColumns = new ArrayList<String>();
	        try {
	            final ResultSetMetaData rsMetaData = resultSet.getMetaData();
	            for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
	                String columnName;
	                columnName = rsMetaData.getColumnLabel(i + 1);
	                if (Utility.isBlank(columnName)) {
	                    columnName = rsMetaData.getColumnName(i + 1);
	                }
	                resultSetColumns.add(columnName.toUpperCase());
	            }
	        } catch (final SQLException e) {
	            LOGGER.log(Level.WARNING, "Could not get columns list");
	        }
	        this.resultSetColumns = Collections.unmodifiableList(resultSetColumns);
	        readColumns = new HashSet<String>();
        }
    }

    public void close() throws SQLException {
        results.close();
    }

    public Map<String, Object> getAttributes() {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        for (final String columnName : resultSetColumns) {
            if (!readColumns.contains(columnName)) {
                try {
                    final Object value = results.getObject(columnName);
                    attributes.put(columnName, value);
                } catch (final SQLException e) {
                    LOGGER.log(Level.WARNING, "Could not read value for column "
                            + columnName, e);
                }
            }
        }
        
        return attributes;
    }

    public boolean getBoolean(final String columnName) {
        boolean value = false;
        if (useColumn(columnName)) {
            try {
                final Object booleanValue = results.getObject(columnName);
                final String stringBooleanValue;
                if (results.wasNull() || booleanValue == null) {
                    stringBooleanValue = null;
                } else {
                    stringBooleanValue = String.valueOf(booleanValue);
                }
                if (!Utility.isBlank(stringBooleanValue)) {
                    try {
                        final int booleanInt = Integer.parseInt(stringBooleanValue);
                        value = booleanInt != 0;
                    } catch (final NumberFormatException e) {
                        value = stringBooleanValue.equalsIgnoreCase("YES")
                                || Boolean.valueOf(stringBooleanValue).booleanValue();
                    }
                }
            } catch (final SQLException e) { 
                LOGGER.log(Level.WARNING, "Could not read boolean value for column "
                        + columnName, e);
            }
        }
        
        return value;
    }

     public <E extends Enum<E>> E getEnum(final String columnName, final E defaultValue) {
        final String value = getString(columnName);
        E enumValue;
        if (value == null || defaultValue == null) {
            enumValue = defaultValue;
        } else {
            try {
                enumValue = (E) Enum.valueOf(defaultValue.getClass(),
                        value.toLowerCase(Locale.ENGLISH));
            } catch (final Exception e) {
                enumValue = defaultValue;
            }
        }
        
        return enumValue;
    }

    public int getInt(final String columnName, final int defaultValue) {
        int value = defaultValue;
        if (useColumn(columnName)) {
            try {
                value = results.getInt(columnName);
                if (results.wasNull()) {
                    value = defaultValue;
                }
            } catch (final SQLException e) {
                LOGGER.log(Level.WARNING, "Could not read integer value for column "
                        + columnName, e);
            }
        }
        
        return value;
    }

    public long getLong(final String columnName, final long defaultValue) {
        long value = defaultValue;
        if (useColumn(columnName)) {
            try {
                value = results.getLong(columnName);
                if (results.wasNull()) {
                    value = defaultValue;
                }
            } catch (final SQLException e) {
                LOGGER.log(Level.WARNING, "Could not read long value for column "
                        + columnName, e);
            }
        }
                
        return value;
    }

    public short getShort(final String columnName, final short defaultValue) {
        short value = defaultValue;
        if (useColumn(columnName)) {
            try {
                value = results.getShort(columnName);
                if (results.wasNull()) {
                    value = defaultValue;
                }
            } catch (final SQLException e) {
                LOGGER.log(Level.WARNING, "Could not read short value for column "
                        + columnName, e);
            }
        }
        
        return value;
    }

    public String getString(final String columnName) {
        String value = null;
        if (useColumn(columnName)) {
            try {
                value = results.getString(columnName);
                if (results.wasNull()) {
                    value = null;
                }
            } catch (final SQLException e) {
                LOGGER.log(Level.WARNING, "Could not read string value for column "
                        + columnName, e);
            }
        }
        
        return value;
    }

    public boolean next() throws SQLException {
        readColumns = new HashSet<String>();
        return results.next();
    }

    private boolean useColumn(final String columnName) {
        final boolean useColumn = columnName != null
                && resultSetColumns.contains(columnName);
        if (useColumn) {
            readColumns.add(columnName);
        }
        
        return useColumn;
    }
}
