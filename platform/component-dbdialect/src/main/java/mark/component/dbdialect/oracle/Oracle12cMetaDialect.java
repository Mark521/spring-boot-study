package mark.component.dbdialect.oracle;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbmodel.model.DatabaseInfo;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "Oracle", majorVersion = 12)
public class Oracle12cMetaDialect extends OracleMetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.oracle_12c.properties";
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
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setProductName("Oracle");
        databaseInfo.setMajorVersion(12);
        databaseInfo.setMinorVersion(0);
        return databaseInfo;
    }
}
