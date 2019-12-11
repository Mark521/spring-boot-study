/*
 * 
 */
package mark.component.dbdialect.constans;

import mark.component.dbmodel.constans.DatabaseType;

import java.io.Serializable;

/**
 * 类名称： DatabaseProduction
 * 描述：
 * 
 * 创建：   liang, 2013-4-1 16:06:05
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
public enum DatabaseProduction implements Serializable {

    Unknown(DatabaseType.Unknown, 0, 0),
    Oracle8i(DatabaseType.Oracle, 8, 0),
    Oracle9i(DatabaseType.Oracle, 9, 0),
    Oracle10g(DatabaseType.Oracle, 10, 2),
    Oracle11g(DatabaseType.Oracle, 11, 2),
    SQLServer2000(DatabaseType.SQLServer, 8, 0),
    SQLServer2005(DatabaseType.SQLServer, 9, 0),
    SQLServer2008(DatabaseType.SQLServer, 10, 0),
    MySQL51(DatabaseType.MySQL, 5, 1),
    MySQL55(DatabaseType.MySQL, 5, 5),
    MySQL56(DatabaseType.MySQL, 5, 6),
    MySQL6(DatabaseType.MySQL, 6, 0),
    DB2(DatabaseType.DB2, 9, 0),
    PostgreSQL8(DatabaseType.PostgreSQL, 8, 0),
    PostgreSQL9(DatabaseType.PostgreSQL, 9, 0),
    HWMPPDB(DatabaseType.HWMPPDB, 9, 0),
    SQLite(DatabaseType.SQLite, 0, 0),
    BDB11g(DatabaseType.BDB, 11, 2),
    Sybase(DatabaseType.Sybase, 0, 0),
    Hive(DatabaseType.Hive, 0, 0);
    private DatabaseType database;
    private int majorVersion;
    private int minorVersion;

    DatabaseProduction(DatabaseType database, int majorVersion, int minorVersion) {
        this.database = database;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    public DatabaseType getDatabaseType() {
        return database;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }
}
