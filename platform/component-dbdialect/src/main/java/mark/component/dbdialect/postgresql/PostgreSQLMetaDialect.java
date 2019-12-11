package mark.component.dbdialect.postgresql;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbdialect.def.DefaultMetaDialect;
import mark.component.dbmodel.model.DatabaseInfo;
import mark.component.dbmodel.model.Schema;
import mark.component.dbmodel.model.Sequence;
import mark.component.core.exception.BaseException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "PostgreSQL")
public class PostgreSQLMetaDialect extends DefaultMetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.postgresql.properties";
    }

    @Override
    public List<String> getDBCharSet(Connection conn) throws BaseException {

        String vSQL = "select pg_encoding_to_char(encoding) from pg_database where datname = '";
        QueryRunner runner = new QueryRunner();
        String charset = null;

        try {
            charset = runner.query(conn, vSQL, new ScalarHandler<String>());
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
        return "postgresql_9_types.properties";
    }

    @Override
    protected String getJava2JdbcFileName() {
        return "postgresql_9_java2jdbc_types.properties";
    }

    @Override
    protected String getJdbc2JavaFileName() {
        return "postgresql_9_jdbc2java_types.properties";
    }

    protected String getKeywordsFileName() {
        return "/keywords/postgresql_9_keywords.txt";
    }

    protected String getSystemFunctionsFileName() {
        return "/functions/postgresql_9_system_functions.xml";
    }

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setProductName("PostgreSQL");
        databaseInfo.setMajorVersion(9);
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

    public Sequence getSequence(Connection conn, final String sequenceName) throws BaseException {
        return null;
    }

    public Collection<Sequence> getSequences(Connection conn) throws BaseException {
        return null;
    }

    @Override
    public String getInformationSchemaSQL(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    @Override
    public String getTableCountSQL(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1)\n" +
                "FROM\n" +
                "\tpg_catalog.pg_namespace n,\n" +
                "\tpg_catalog.pg_class C \n" +
                "\tLEFT JOIN pg_catalog.pg_description d ON ( C.oid = d.objoid AND d.objsubid = 0 )\n" +
                "WHERE\n" +
                "\tC.relnamespace = n.oid \n" +
                "\tAND n.nspname LIKE ? \n" +
                "\tAND C.relname LIKE ? \n" +
                "\tAND ( FALSE OR ( C.relkind IN ( 'r', 'p' ) AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema' ) )");
        return sql.toString();
    }

    @Override
    public String getTablesByPageSQL(){

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \n" +
                "\tNULL AS\tTABLE_CAT,\n" +
                "\tn.nspname AS TABLE_SCHEM,\n" +
                "\tC.relname AS TABLE_NAME,\n" +
                "\tCASE n.nspname ~ '^pg_' OR n.nspname = 'information_schema' \n" +
                "\tWHEN TRUE THEN\n" +
                "\t\tCASE\n" +
                "\t\t\tWHEN n.nspname = 'pg_catalog'  OR n.nspname = 'information_schema' THEN\n" +
                "\t\t\t\t\tCASE C.relkind  WHEN 'r' THEN 'SYSTEM TABLE' ELSE NULL END\n" +
                "\t\t\tWHEN n.nspname = 'pg_toast' THEN\n" +
                "\t\t\t\t\tCASE C.relkind WHEN 'r' THEN 'SYSTEM TOAST TABLE' WHEN 'i' THEN 'SYSTEM TOAST INDEX' ELSE NULL END \n" +
                "\t\t\tELSE\n" +
                "\t\t\t\t\tCASE C.relkind WHEN 'r' THEN 'TEMPORARY TABLE' WHEN 'p' THEN 'TEMPORARY TABLE' ELSE NULL END \n" +
                "\t\t\tEND \n" +
                "\tWHEN FALSE THEN\n" +
                "\t\tCASE C.relkind WHEN 'r' THEN 'TABLE' WHEN 'p' THEN 'TABLE' ELSE NULL END\n" +
                "\tELSE NULL END AS TABLE_TYPE,\n" +
                "\td.description AS REMARKS \n" +
                "FROM\n" +
                "pg_catalog.pg_namespace n,\n" +
                "pg_catalog.pg_class C\n" +
                "LEFT JOIN pg_catalog.pg_description d ON ( C.oid = d.objoid AND d.objsubid = 0 )\n" +
                "LEFT JOIN pg_catalog.pg_class dc ON ( d.classoid = dc.oid AND dc.relname = 'pg_class' )\n" +
                "LEFT JOIN pg_catalog.pg_namespace dn ON ( dn.oid = dc.relnamespace AND dn.nspname = 'pg_catalog' )\n" +
                "WHERE\n" +
                "C.relnamespace = n.oid " +
                "AND n.nspname LIKE ? \n" +
                "\tAND C.relname LIKE ? \n" +
                "AND ( FALSE OR ( C.relkind IN ( 'r', 'p' ) AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema' ) ) \n" +
                "ORDER BY TABLE_TYPE, TABLE_SCHEM, TABLE_NAME \n" +
                "OFFSET ? LIMIT ?");
        return sql.toString();
    }
}
