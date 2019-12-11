package mark.component.dbdialect.mysql;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbmodel.model.DatabaseInfo;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "MySQL", majorVersion = 5, minorVersion = 7)
public class MySQL57MetaDialect extends MySQLMetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.mysql_57.properties";
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

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setProductName("MySQL");
        databaseInfo.setMajorVersion(5);
        databaseInfo.setMinorVersion(7);
        return databaseInfo;
    }
}
