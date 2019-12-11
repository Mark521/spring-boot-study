package mark.component.dbdialect.mysql;

import mark.component.dbdialect.def.DefaultMetaDialect;
import mark.component.core.exception.BaseException;
import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbmodel.model.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "MySQL")
public class MySQLMetaDialect extends DefaultMetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.mysql_50.properties";
    }

    @Override
    public List<String> getDBCharSet(Connection conn) throws BaseException {

        String vSQL = "show variables like 'character_set_database'";
        QueryRunner runner = new QueryRunner();
        String charset = null;

        try {
            charset = runner.query(conn, vSQL, new ScalarHandler<String>(2));
        } catch (SQLException ex) {
            throw new BaseException("Error : MetaDialect.getDBCharSet", ex);
        }
        if (charset != null) {
            return Collections.singletonList(charset);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    protected String getJdbcTypeFileName() {
        return "mysql_57_types.properties";
    }

    @Override
    protected String getJava2JdbcFileName() {
        return "mysql_57_java2jdbc_types.properties";
    }

    @Override
    protected String getJdbc2JavaFileName() {
        return "mysql_57_jdbc2java_types.properties";
    }

    protected String getKeywordsFileName() {
        return "/keywords/mysql_57_keywords.txt";
    }

    protected String getSystemFunctionsFileName() {
        return "/functions/mysql_57_system_functions.xml";
    }

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setProductName("MySQL");
        databaseInfo.setMajorVersion(5);
        databaseInfo.setMinorVersion(7);
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

    public char openQuote() {
        return '`';
    }

    public char closeQuote() {
        return '`';
    }

    public String urlWithCharset(String url, String charSet) throws BaseException {
        if (charSet == null || charSet.isEmpty()) {
            return url;
        }
        if (url.toLowerCase().matches(".*characterencoding=.*")) {
            int begin = url.toLowerCase().indexOf("characterencoding");
            String ssss = url.substring(begin);
            String s = "";
            if (ssss.indexOf("&") != -1) {
                s = ssss.substring(ssss.indexOf("&"));
            }
            return url.substring(0, begin) + "characterEncoding=" + charSet + s;
        } else {
            return url + "?useUnicode=true&characterEncoding=" + charSet;
        }
    }

    public Sequence getSequence(Connection conn, final String sequenceName) throws BaseException {
        return null;
    }

    public Collection<Sequence> getSequences(Connection conn) throws BaseException {
        return null;
    }

    @Override
    public String getDefaultCatalog(Connection conn) throws BaseException {
        try {
            String catalogSQL = "SELECT database()";
            String catalog = new QueryRunner().query(conn, catalogSQL, new ScalarHandler<String>());
            return catalog;
        } catch (SQLException ex) {
            throw new BaseException("MetaDialect.getDefaultCatalog Failed.", ex);
        }
    }

    @Override
    public void setDefaultCatalog(Connection conn, String name) throws BaseException {
        try {
            String catalogSQL = "USE " + unquote(name);
            new QueryRunner().update(catalogSQL);
        } catch (SQLException ex) {
            throw new BaseException("MetaDialect.setDefaultCatalog Failed.", ex);
        }
    }
    
    @Override
    public Schema getDefaultSchema(Connection conn) throws BaseException {
        return null;
    }

    public String getDefaultSchemaName(final Connection conn)
            throws BaseException {
        Schema schema = getDefaultSchema(conn);
        if (schema != null) {
            return schema.getName();
        }
        return null;
    }

    public boolean validateDQL(final Connection conn, final String sqlString) throws BaseException {
        try {
            Statement statement = conn.createStatement();
            statement.executeQuery("select 1 from (" + sqlString + ") tab where false");
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    @Override
    public String getInformationSchemaSQL(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTableCountSQL(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT " +
                "COUNT(*) " +
                "FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE " +
                "TABLE_TYPE='BASE TABLE' " +
                "AND TABLE_SCHEMA=? " +
                "AND TABLE_NAME LIKE ?");

        return sql.toString();
    }

    @Override
    public String getTablesByPageSQL(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT " +
                "TABLE_SCHEMA AS TABLE_CAT, " +
                "NULL AS TABLE_SCHEM, " +
                "TABLE_NAME, " +
                "CASE WHEN TABLE_TYPE='BASE TABLE' THEN CASE WHEN TABLE_SCHEMA = 'mysql' OR TABLE_SCHEMA = 'performance_schema' THEN 'SYSTEM TABLE' ELSE 'TABLE' END WHEN TABLE_TYPE='TEMPORARY' THEN 'LOCAL_TEMPORARY' ELSE TABLE_TYPE END AS TABLE_TYPE, " +
                "TABLE_COMMENT AS REMARKS, " +
                "NULL AS TYPE_CAT, " +
                "NULL AS TYPE_SCHEM, " +
                "NULL AS TYPE_NAME, " +
                "NULL AS SELF_REFERENCING_COL_NAME, " +
                "NULL AS REF_GENERATION " +
                "FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE " +
                "TABLE_TYPE='BASE TABLE' " +
                "AND TABLE_SCHEMA=? " +
                "AND TABLE_NAME LIKE ? " +
                "LIMIT ? OFFSET ?");
        return sql.toString();
    }
}
