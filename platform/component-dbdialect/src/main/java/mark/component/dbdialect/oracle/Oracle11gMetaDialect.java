package mark.component.dbdialect.oracle;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbmodel.model.DatabaseInfo;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "Oracle", majorVersion = 11)
public class Oracle11gMetaDialect extends OracleMetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.oracle_11g.properties";
    }

    @Override
    protected String getJdbcTypeFileName() {
        return "oracle_11g_types.properties";
    }

    @Override
    protected String getJava2JdbcFileName() {
        return "oracle_11g_java2jdbc_types.properties";
    }

    @Override
    protected String getJdbc2JavaFileName() {
        return "oracle_11g_jdbc2java_types.properties";
    }

    protected String getKeywordsFileName() {
        return "/keywords/oracle_11g_keywords.txt";
    }

    protected String getSystemFunctionsFileName() {
        return "/functions/oracle_11g_system_functions.xml";
    }

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setProductName("Oracle");
        databaseInfo.setMajorVersion(11);
        databaseInfo.setMinorVersion(0);
        return databaseInfo;
    }
}
