package mark.component.dbdialect.util;

import mark.component.dbmodel.constans.DatabaseType;

/**
 *
 * @author liang
 */
public abstract class DBUtil {

    public static String buildDBUrl(DatabaseType type, String host, int port, String dbNameAndParameters) {

        switch (type) {
            case Oracle:
                return "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbNameAndParameters;
            case SQLServer:
                return "jdbc:sqlserver://" + host + ":" + port + ";DatabaseName=" + dbNameAndParameters;
            case MySQL:
                return "jdbc:mysql://" + host + ":" + port + "/" + dbNameAndParameters;
            case DB2:
                return "jdbc:db2://" + host + ":" + port + "/" + dbNameAndParameters;
            case PostgreSQL:
            case HWMPPDB:
                return "jdbc:postgresql://" + host + ":" + port + "/" + dbNameAndParameters;
            case BDB:
            case SQLite:
                return "jdbc:sqlite:" + dbNameAndParameters;
            case Sybase:
                return "jdbc:jtds:sybase://" + host + ":" + port + ";DatabaseName=" + dbNameAndParameters;
            case Hive:
                return "jdbc:hive://" + host + ":" + port + "/" + dbNameAndParameters;
        }
        return null;
    }
}
