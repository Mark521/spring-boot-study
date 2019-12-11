package mark.component.dbdialect.mysql;

import mark.component.dbmodel.model.DatabaseInfo;
import mark.component.dbdialect.ProductAnnotation;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "MySQL", majorVersion = 4)
public class MySQL4MetaDialect extends MySQLMetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.mysql_4.properties";
    }

    @Override
    protected String getJdbcTypeFileName() {
        return "mysql_4_types.properties";
    }

    @Override
    protected String getJava2JdbcFileName() {
        return "mysql_4_java2jdbc_types.properties";
    }

    @Override
    protected String getJdbc2JavaFileName() {
        return "mysql_4_jdbc2java_types.properties";
    }

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setProductName("MySQL");
        databaseInfo.setMajorVersion(4);
        databaseInfo.setMinorVersion(0);
        return databaseInfo;
    }
}
