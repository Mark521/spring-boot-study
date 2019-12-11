package mark.component.dbdialect.db2;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbmodel.model.DatabaseInfo;
import mark.component.core.exception.BaseException;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author liang
 */
@ProductAnnotation(productName = "DB2", majorVersion = 2)
public class DB2400MetaDialect extends DB2MetaDialect {

    @Override
    protected String getSchemaInfomationFileName() {
        return "/information/schema/INFORMATION_SCHEMA.db2_400.properties";
    }

    @Override
    public List<String> getDBCharSet(Connection conn) throws BaseException {
        return Collections.EMPTY_LIST;
    }

    @Override
    protected String getJdbcTypeFileName() {
        return "db2_400_types.properties";
    }

    @Override
    protected String getJava2JdbcFileName() {
        return "db2_400_java2jdbc_types.properties";
    }

    @Override
    protected String getJdbc2JavaFileName() {
        return "db2_400_jdbc2java_types.properties";
    }

    @Override
    public DatabaseInfo resolveDatabaseInfo() {
        DatabaseInfo dbInfo = new DatabaseInfo();
        dbInfo.setProductName("DB2");
        dbInfo.setMajorVersion(400);
        dbInfo.setMinorVersion(0);
        return dbInfo;
    }
}
