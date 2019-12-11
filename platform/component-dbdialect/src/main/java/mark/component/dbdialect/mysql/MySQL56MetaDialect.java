package mark.component.dbdialect.mysql;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbmodel.model.DatabaseInfo;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "MySQL", majorVersion = 5, minorVersion = 6)
public class MySQL56MetaDialect extends MySQLMetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.mysql_56.properties";
    }

    @Override
    protected String getJdbcTypeFileName() {
        return "mysql_56_types.properties";
    }

    @Override
    protected String getJava2JdbcFileName() {
        return "mysql_56_java2jdbc_types.properties";
    }

    @Override
    protected String getJdbc2JavaFileName() {
        return "mysql_56_jdbc2java_types.properties";
    }

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setProductName("MySQL");
        databaseInfo.setMajorVersion(5);
        databaseInfo.setMinorVersion(6);
        return databaseInfo;
    }
}
