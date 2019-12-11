package mark.moduledatasource.constans;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public enum DatabaseType {
    Unknown("Unknown"),
    Oracle("Oracle"),
    SQLServer("Microsoft SQL Server"),
    SQLServer2000("Microsoft SQL Server 2000"),
    MySQL("MySQL"),
    DB2("DB2"),
    PostgreSQL("PostgreSQL"),
    SQLite("SQLite"),
    BDB("Berkeley DB"),
    Sybase("Sybase"),
    Hive("Hive"),
    Teradata("Teradata"),
    GreenPlum("GreenPlum"),
    HWMPPDB("HWMPPDB"),
    HBase("HBase"),
    Spark("Spark"),
    Phoenix("Phoenix"),
    HWHive("HWHive"),
    XHHive("XHHive"),
    GBase("GBase"),
    GaussDB100("GaussDB100");
    private String name;

    DatabaseType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static DatabaseType getDatabaseType(String dbName){
        if(StringUtils.isBlank(dbName)){
            return Unknown;
        }
        for (DatabaseType databaseType:DatabaseType.values()){
            if(dbName.equalsIgnoreCase(databaseType.getName())){
                return databaseType;
            }
        }
        return Unknown;
    }

    public static DatabaseType getDatabaseTypeByConnection(Connection conn){
        try {
            DatabaseMetaData meta = conn.getMetaData();
            String sDbms = conn.getMetaData().getDatabaseProductName().trim().toLowerCase();
            if (sDbms.indexOf("oracle") != -1) {
                return Oracle;
            } else if (sDbms.indexOf("db2") != -1) {
                return DB2;
            } else if (sDbms.indexOf("sql server") != -1) {
                return SQLServer;
            } else if (sDbms.indexOf("postgres") != -1) {
                return PostgreSQL;
            } else if (sDbms.indexOf("mysql") != -1) {
                return MySQL;
            } else if (sDbms.indexOf("sqlite") != -1) {
                if (meta.getDriverName().toLowerCase().equals("sqlitejdbc")) {
                    return SQLite;
                } else if (meta.getDriverName().toLowerCase().equals("sqlite/jdbc")) {
                    return BDB;
                }
            } else if (sDbms.indexOf("hive") != -1) {
                return Hive;
            } else if (sDbms.indexOf("adaptive server enterprise") != -1 || sDbms.indexOf("ase") != -1) {
                return Sybase;
            }
        }catch (SQLException e){
        }
        return Unknown;
    }
}
