package mark.component.dbdialect.db2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbdialect.def.DefaultMetaDialect;
import mark.component.dbmodel.model.DatabaseInfo;
import mark.component.dbmodel.model.Schema;
import mark.component.core.exception.BaseException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "DB2")
public class DB2MetaDialect extends DefaultMetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.db2_400.properties";
    }

    @Override
    public List<String> getDBCharSet(Connection conn) throws BaseException {
        return Collections.EMPTY_LIST;
    }

    @Override
    protected String getJdbcTypeFileName() {
        return "db2_400_types.properties";
    }

    @Override
    protected String getJava2JdbcFileName() {
        return "db2_400_java2jdbc_types.properties";
    }

    @Override
    protected String getJdbc2JavaFileName() {
        return "db2_400_jdbc2java_types.properties";
    }

    protected String getKeywordsFileName() {
        return "/keywords/db2_400_keywords.txt";
    }

    protected String getSystemFunctionsFileName() {
        return "/functions/db2_400_system_functions.xml";
    }

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setProductName("DB2");
        databaseInfo.setMajorVersion(400);
        databaseInfo.setMinorVersion(0);
        return databaseInfo;
    }

    public boolean supportSequence() {
        return true;
    }

    public boolean supportSynonym() {
        return true;
    }

    public boolean supportTriger() {
        return true;
    }

    public boolean supportCatalog() {
        return true;
    }

    @Override
    public boolean supportSchema() {
        return true;
    }

    @Override
    public String getInformationSchemaSQL(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String quote(String name) {
        if (name == null) {
            return null;
        }
        if (name.charAt(0) != openQuote() || name.charAt(name.length() - 1) != closeQuote()) {
            name = openQuote() + name + closeQuote();
        }
        return name;
    }

    @Override
    public String unquote(String name) {
        if (name == null || name.length() < 2) {
            return name;
        }

        if (name.charAt(0) == openQuote() && name.charAt(name.length() - 1) == closeQuote()) {
            name = name.substring(1, name.length() - 1);
        } else {
            name = name.toUpperCase();
        }
        return name;
    }
    
    @Override
    public Schema getDefaultSchema(Connection conn) throws BaseException {
        try {
            String schemaSQL = "values current schema";
            String schema = new QueryRunner().query(conn, schemaSQL, new ScalarHandler<String>());
            return getSchema(conn, schema);
        } catch (SQLException ex) {
            throw new BaseException("MetaDialect.getDefaultSchema Failed.", ex);
        }
    }
    
    @Override
    public void setDefaultSchema(Connection conn, String name) throws BaseException {
        try {
            String schemaSQL = "SET SCHEMA \'" + unquote(name) + "\'";
            new QueryRunner().update(schemaSQL);
        } catch (SQLException ex) {
            throw new BaseException("MetaDialect.setDefaultSchema Failed.", ex);
        }
    }
}
