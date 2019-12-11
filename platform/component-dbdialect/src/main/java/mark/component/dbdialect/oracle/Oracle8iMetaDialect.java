package mark.component.dbdialect.oracle;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbmodel.model.DatabaseInfo;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "Oracle", majorVersion = 8)
public class Oracle8iMetaDialect extends OracleMetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.oracle_8i.properties";
    }

    @Override
    protected String getJdbcTypeFileName() {
        return "oracle_8i_types.properties";
    }

    @Override
    protected String getJava2JdbcFileName() {
        return "oracle_8i_java2jdbc_types.properties";
    }

    @Override
    protected String getJdbc2JavaFileName() {
        return "oracle_8i_jdbc2java_types.properties";
    }

    protected String getKeywordsFileName() {
        return "/keywords/oracle_8i_keywords.txt";
    }

    protected String getSystemFunctionsFileName() {
        return "/functions/oracle_8i_system_functions.xml";
    }

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setProductName("Oracle");
        databaseInfo.setMajorVersion(8);
        databaseInfo.setMinorVersion(0);
        return databaseInfo;
    }
}
