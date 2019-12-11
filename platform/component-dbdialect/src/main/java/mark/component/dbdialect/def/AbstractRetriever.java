package mark.component.dbdialect.def;

import mark.component.dbmodel.constans.DatabaseObject;
import mark.component.dbmodel.model.ColumnDataType;
import mark.component.dbmodel.model.Schema;
import mark.component.core.exception.BaseException;
import mark.component.dbdialect.DBServices;
import mark.component.dbdialect.MetaDialect;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract class AbstractRetriever implements Serializable {

    private final Connection connection;
    static final String UNKNOWN = "<unknown>";

    public static List<String> readResultsVector(final ResultSet results)
            throws SQLException {
        final List<String> values = new ArrayList<String>();
        try {
            while (results.next()) {
                final String value = results.getString(1);
                values.add(value);
            }
        } finally {
            results.close();
        }

        return values;
    }

    public AbstractRetriever(final Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    public boolean belongsToSchema(final DatabaseObject dbObject,
            final String catalogName,
            final String schemaName) throws BaseException {
        if (dbObject == null) {
            return false;
        }
        final String dbObjectCatalogName = dbObject.getSchema().getCatalogName();
        final String dbObjectSchemaName = dbObject.getSchema().getName();
        return equal(dbObjectCatalogName, catalogName) && equal(dbObjectSchemaName, schemaName);
    }

    public ColumnDataType lookupOrCreateColumnDataType(final Schema schema, final int jdbcType, final String jdbcTypeNmae) throws BaseException {
        MetaDialect metaDialect = DBServices.instance().lookup(connection, MetaDialect.class);
        Collection<ColumnDataType> columnDataTypes = metaDialect.getColumnDataTypes(jdbcTypeNmae);
        ColumnDataType columnDataType;
        if (columnDataTypes == null || columnDataTypes.isEmpty()) {
            columnDataType = new ColumnDataType(schema, jdbcTypeNmae);
            columnDataType.setJdbcType(jdbcType);
        } else {
            columnDataType = columnDataTypes.iterator().next();
        }
        return columnDataType;

    }

    public ColumnDataType lookupOrCreateColumnDataType(final int jdbcType) throws BaseException {
        MetaDialect metaDialect = DBServices.instance().lookup(connection, MetaDialect.class);
        Collection<ColumnDataType> columnDataTypes = metaDialect.getColumnDataTypes(jdbcType);
        if (columnDataTypes != null && !columnDataTypes.isEmpty()) {
            return columnDataTypes.iterator().next();
        }
        return null;
    }

    public String getInformationSchemaSQL(String key) throws BaseException {
        MetaDialect metaDialect = DBServices.instance().lookup(connection, MetaDialect.class);
        return metaDialect.getInformationSchemaSQL(key);
    }

    public boolean supportCatalog() throws BaseException {
        MetaDialect metaDialect = DBServices.instance().lookup(connection, MetaDialect.class);
        return metaDialect.supportCatalog();
    }

    public boolean supportSchema() throws BaseException {
        MetaDialect metaDialect = DBServices.instance().lookup(connection, MetaDialect.class);
        return metaDialect.supportSchema();
    }

    public String quoted(final String name) throws BaseException {
        if (name == null || name.isEmpty()) {
            return name;
        }
        MetaDialect metaDialect = DBServices.instance().lookup(connection, MetaDialect.class);
        return metaDialect.quote(name);
    }

    public String quoted(final String name, final boolean quoted) throws BaseException {
        if (!quoted) {
            return name;
        }
        return quoted(name);
    }

    public String unquoted(final String name) throws BaseException {
        if (name == null || name.length() <= 2) {
            return name;
        }
        MetaDialect metaDialect = DBServices.instance().lookup(connection, MetaDialect.class);
        return metaDialect.unquote(name);
    }

    public String unquoted1(final String name) throws BaseException {
        if (name == null || name.length() <= 2) {
            return name;
        }
        MetaDialect metaDialect = DBServices.instance().lookup(connection, MetaDialect.class);
        char q = metaDialect.openQuote();
        if (name.charAt(0) == q) {
            return metaDialect.unquote(name);
        } else {
            return name;
        }
    }

    public boolean equal(String aName, String bName) throws BaseException {
        if ("".equals(aName)) {
            aName = null;
        }
        if ("".equals(bName)) {
            bName = null;
        }
        if (aName == null && bName == null) {
            return true;
        } else if (aName == null || bName == null) {
            return false;
        }
        aName = unquoted1(aName);
        bName = unquoted1(bName);
        return aName.equals(bName);
    }
}
