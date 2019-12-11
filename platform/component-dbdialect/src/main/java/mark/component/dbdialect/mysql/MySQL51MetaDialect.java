package mark.component.dbdialect.mysql;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbmodel.model.DatabaseInfo;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "MySQL", majorVersion = 5, minorVersion = 1)
public class MySQL51MetaDialect extends MySQLMetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.mysql_51.properties";
    }

    @Override
    protected String getJdbcTypeFileName() {
        return "mysql_51_types.properties";
    }

    @Override
    protected String getJava2JdbcFileName() {
        return "mysql_51_java2jdbc_types.properties";
    }

    @Override
    protected String getJdbc2JavaFileName() {
        return "mysql_51_jdbc2java_types.properties";
    }

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setProductName("MySQL");
        databaseInfo.setMajorVersion(5);
        databaseInfo.setMinorVersion(1);
        return databaseInfo;
    }
}
