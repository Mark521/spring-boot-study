package mark.component.dbdialect.oracle;

import mark.component.core.exception.BaseException;
import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbdialect.def.DefaultMetaDialect;
import mark.component.dbmodel.constans.RetrieveRule;
import mark.component.dbmodel.model.DatabaseInfo;
import mark.component.dbmodel.model.Schema;
import mark.component.dbmodel.model.Sequence;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "Oracle")
public class OracleMetaDialect extends DefaultMetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.oracle_12c.properties";
    }

    public String quote(String name) {
        if (name == null) {
            return null;
        }
        if (name.charAt(0) != openQuote() || name.charAt(name.length() - 1) != closeQuote()) {
            name = openQuote() + name + closeQuote();
        }
        return name;
    }

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
    public List<String> getDBCharSet(Connection conn) throws BaseException {

        String vSQL = "select value from nls_database_parameters where parameter = 'NLS_CHARACTERSET'";
        QueryRunner runner = new QueryRunner();
        String charset = null;

        try {
            charset = runner.query(conn, vSQL, new ScalarHandler<String>());
        } catch (SQLException ex) {
            throw new BaseException("Error : MetaDialect.getDBCharSet",ex);
        }
        if (charset != null) {
            return Collections.singletonList(charset);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    protected String getJdbcTypeFileName() {
        return "oracle_12c_types.properties";
    }

    @Override
    protected String getJava2JdbcFileName() {
        return "oracle_12c_java2jdbc_types.properties";
    }

    @Override
    protected String getJdbc2JavaFileName() {
        return "oracle_12c_jdbc2java_types.properties";
    }

    protected String getKeywordsFileName() {
        return "/keywords/oracle_12c_keywords.txt";
    }

    protected String getSystemFunctionsFileName() {
        return "/functions/oracle_12c_system_functions.xml";
    }

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo dbInfo = new DatabaseInfo();
        dbInfo.setProductName("Oracle");
        dbInfo.setMajorVersion(12);
        dbInfo.setMinorVersion(0);
        return dbInfo;
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
    public Schema getDefaultSchema(Connection conn) throws BaseException {
        try {
            String schemaSQL = "SELECT SYS_CONTEXT('USERENV','CURRENT_SCHEMA') CURRENT_SCHEMA FROM DUAL";
            String schema = new QueryRunner().query(conn, schemaSQL, new ScalarHandler<String>());
            return getSchema(conn, schema);
        } catch (SQLException ex) {
            throw new BaseException("MetaDialect.getDefaultSchema Failed.",ex);
        }
    }

    @Override
    public void setDefaultSchema(Connection conn, String name) throws BaseException {
        try {
            String schemaSQL = "ALTER SESSION SET CURRENT_SCHEMA=" + unquote(name);
            new QueryRunner().update(schemaSQL);
        } catch (SQLException ex) {
            throw new BaseException("MetaDialect.setDefaultSchema Failed.", ex);
        }
    }

    public boolean validateSQL(final Connection conn, final String sqlString) throws BaseException {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE");
        sb.append("\tc     number; ");
        sb.append("\tl_sql long := '").append(sqlString).append("'; ");
        sb.append("BEGIN ");
        sb.append("\tc := dbms_sql.open_cursor; ");
        sb.append("\tdbms_sql.parse(c, l_sql, dbms_sql.native); ");
        sb.append("\tdbms_sql.close_cursor(c);  ");
        sb.append("END;");
        try {
            executeDDL(conn, sb.toString());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean validateDDL(final Connection conn, final String sqlString) throws BaseException {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE ");
        sb.append("\tc     number; ");
        sb.append("\tl_sql long := '").append(sqlString).append("'; ");
        sb.append("BEGIN ");
        sb.append("\tc := dbms_sql.open_cursor; ");
        sb.append("\tdbms_sql.parse(c, l_sql, dbms_sql.native); ");
        sb.append("\tdbms_sql.close_cursor(c);  ");
        sb.append("END;");
        try {
            executeDDL(conn, sb.toString());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean validateDQL(final Connection conn, final String sqlString) throws BaseException {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE ");
        sb.append("\tc     number; ");
        sb.append("\tl_sql long := '").append(sqlString).append("'; ");
        sb.append("BEGIN ");
        sb.append("\tc := dbms_sql.open_cursor; ");
        sb.append("\tdbms_sql.parse(c, l_sql, dbms_sql.native); ");
        sb.append("\tdbms_sql.close_cursor(c);  ");
        sb.append("END;");
        try {
            executeDDL(conn, sb.toString());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean validateDML(final Connection conn, final String sqlString) throws BaseException {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE ");
        sb.append("\tc     number; ");
        sb.append("\tl_sql long := '").append(sqlString).append("'; ");
        sb.append("BEGIN ");
        sb.append("\tc := dbms_sql.open_cursor; ");
        sb.append("\tdbms_sql.parse(c, l_sql, dbms_sql.native); ");
        sb.append("\tdbms_sql.close_cursor(c);  ");
        sb.append("END;");
        try {
            executeDDL(conn, sb.toString());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Collection<Sequence> getSequences(final Connection conn,
            final String catalogName, final String schemaName, String sequencePatternName, final long retrieveRule)
            throws BaseException {
        List<Sequence> sequences = new ArrayList<Sequence>();
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        boolean regex = RetrieveRule.REGEX == (RetrieveRule.REGEX & retrieveRule);
        Statement statement = null;
        ResultSet rs = null;
        Pattern pattern = null;
        if (sequencePatternName == null || sequencePatternName.isEmpty()) {
            sequencePatternName = null;
            regex = false;
        } else if (regex) {
            pattern = Pattern.compile(sequencePatternName);
            sequencePatternName = null;
        } else {
            sequencePatternName = unquote(sequencePatternName);
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT SEQUENCE_NAME, MIN_VALUE, MAX_VALUE, MIN_VALUE START_VALUE, INCREMENT_BY, LAST_NUMBER LAST_VALUE FROM USER_SEQUENCES ");
            if (sequencePatternName != null) {
                sb.append("WHERE SEQUENCE_NAME LIKE ").append(sequencePatternName);
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
                }
                final Sequence sequence = new Sequence(schema, quote(sn, quoted));
                sequence.setMinValue(rs.getBigDecimal("MIN_VALUE").toBigInteger());
                sequence.setMaxValue(rs.getBigDecimal("MAX_VALUE").toBigInteger());
                sequence.setStartValue(rs.getBigDecimal("START_VALUE").toBigInteger());
                sequence.setIncrementBy(rs.getBigDecimal("INCREMENT_BY").toBigInteger());
                sequence.setLastValue(rs.getBigDecimal("LAST_VALUE").toBigInteger());

                sequences.add(sequence);
            }
        } catch (SQLException ex) {
            throw new BaseException("", ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(statement);
        }
        return sequences;
    }

    public BigInteger nextValue(final Connection conn, final String catalogName, String schemaName, String sequenceName)
            throws BaseException {
        BigInteger nextValue = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT ");
            if (schemaName != null && !schemaName.isEmpty()) {
                sb.append(schemaName).append(".");
            }
            sb.append(sequenceName);
            sb.append(".NEXTVAL FROM DUAL");

            statement = conn.createStatement();
            rs = statement.executeQuery(sb.toString());
            if (rs.next()) {
                nextValue = rs.getBigDecimal(1).toBigInteger();
            }
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e) {
            }
            throw new BaseException("", ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(statement);
        }
        return nextValue;
    }
    
    public BigInteger currValue(final Connection conn, final String catalogName, String schemaName, String sequenceName)
            throws BaseException {
        BigInteger currValue = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT ");
            if (schemaName != null && !schemaName.isEmpty()) {
                sb.append(schemaName).append(".");
            }
            sb.append(sequenceName);
            sb.append(".CURRVAL FROM DUAL");

            statement = conn.createStatement();
            rs = statement.executeQuery(sb.toString());
            if (rs.next()) {
                currValue = rs.getBigDecimal(1).toBigInteger();
            }
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e) {
            }
            throw new BaseException("", ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(statement);
        }
        return currValue;
    }

    @Override
    public String getTableCountSQL(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) " +
                " FROM all_tables o, all_tab_comments c " +
                " WHERE  " +
                " o.owner LIKE ? ESCAPE '/' " +
                " AND o.table_name LIKE ? ESCAPE '/' " +
                " AND o.owner = c.owner (+) " +
                " AND o.table_name = c.table_name(+)");
        return sql.toString();
    }

    @Override
    public String getTablesByPageSQL() {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * from ( " +
                "SELECT  " +
                " NULL AS table_cat, " +
                " o.owner AS table_schem, " +
                " o.table_name AS table_name, " +
                " 'TABLE' AS table_type,  " +
                " nvl(c.comments, '') AS remarks, " +
                " ROWNUM AS RN " +
                " FROM all_tables o, all_tab_comments c " +
                " WHERE  " +
                " o.owner LIKE ? ESCAPE '/' " +
                " AND o.table_name LIKE ? ESCAPE '/' " +
                " AND o.owner = c.owner (+) " +
                " AND o.table_name = c.table_name(+) " +
                " ORDER BY o.owner, o.table_name,o.LAST_ANALYZED) A" +
                " WHERE RN <= ? AND RN > ?");
        return sql.toString();
    }
}
