package mark.component.dbdialect.mysql;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbmodel.model.DatabaseInfo;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "MySQL", majorVersion = 5, minorVersion = 0)
public class MySQL50MetaDialect extends MySQLMetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.mysql_50.properties";
    }

    @Override
    protected String getJdbcTypeFileName() {
        return "mysql_50_types.properties";
    }

    @Override
    protected String getJava2JdbcFileName() {
        return "mysql_50_java2jdbc_types.properties";
    }

    @Override
    protected String getJdbc2JavaFileName() {
        return "mysql_50_jdbc2java_types.properties";
    }

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setProductName("MySQL");
        databaseInfo.setMajorVersion(5);
        databaseInfo.setMinorVersion(0);
        return databaseInfo;
    }
}
