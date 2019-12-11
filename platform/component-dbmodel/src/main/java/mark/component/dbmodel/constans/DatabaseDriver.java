package mark.component.dbmodel.constans;

public enum DatabaseDriver {

    Unknown(DatabaseType.Unknown, "Unknown", "Unknown", 0),
    OracleThinServiceName(DatabaseType.Oracle, "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@//${host}:${port}/${name}", 1521),
    OracleThinSID(DatabaseType.Oracle, "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@${host}:${port}:${name}", 1521),
    OracleThinTNSName(DatabaseType.Oracle, "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@${name}", 1521),
    OracleOCI(DatabaseType.Oracle, "oracle.jdbc.OracleDriver", "jdbc:oracle:oci:@${host}:${port}:${name}", 1521),
    OracleOCI8(DatabaseType.Oracle, "oracle.jdbc.OracleDriver", "jdbc:oracle:oci8:@${host}:${port}:${name}", 1521),
    SQLServer(DatabaseType.SQLServer, "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://${host}:${port};DatabaseName=${name}", 1433),
    SQLServer2000(DatabaseType.SQLServer2000, "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:microsoft:sqlserver://${host}:${port};DatabaseName=${name}", 1433),
    MySQL(DatabaseType.MySQL, "com.mysql.jdbc.Driver", "jdbc:mysql://${host}:${port}/${name}", 3306),
    DB2(DatabaseType.DB2, "com.ibm.db2.jcc.DB2Driver", "jdbc:db2://${host}:${port}/${name}", 50000),
    PostgreSQL(DatabaseType.PostgreSQL, "org.postgresql.Driver", "jdbc:postgresql://${host}:${port}/${name}", 5432),
    HWMPPDB(DatabaseType.HWMPPDB, "org.hwmppdb.Driver", "jdbc:postgresql://${host}:${port}/${name}", 5432),
    SQLiteFile(DatabaseType.SQLite, "org.sqlite.JDBC", "jdbc:sqlite:${name}", 0),
    SQLiteMemory(DatabaseType.SQLite, "org.sqlite.JDBC", "jdbc:sqlite:", 0),
    BDBFile(DatabaseType.BDB, "SQLite.JDBCDriver", "jdbc:sqlite:/${name}", 0),
    BDBMemory(DatabaseType.BDB, "SQLite.JDBCDriver", "jdbc:sqlite:/", 0),
    SybaseASE(DatabaseType.Sybase, "com.sybase.jdbc4.jdbc.SybDriver", "jdbc:sybase:Tds:${host}:${port}/${name}", 5000),
    SybaseAnywhere(DatabaseType.Sybase, "com.sybase.jdbc4.jdbc.SybDriver", "jdbc:sybase:Tds:${host}:${port}?ServiceName=${name}", 2638),
    Hive(DatabaseType.Hive, "org.apache.hadoop.hive.jdbc.HiveDriver", "jdbc:hive://${host}:${port}/${name}", 10000),
    Teradata(DatabaseType.Teradata, "com.teradata.jdbc.TeraDriver", "jdbc:teradata://${host}/${name}", 0),
    GreenPlum(DatabaseType.GreenPlum, "org.postgresql.Driver", "jdbc:postgresql://${host}:${port}/${name}", 5432),
    GBase(DatabaseType.GBase, "com.gbase.jdbc.Driver", "jdbc:gbase://${host}:${port}/${name}", 5258),
    GaussDB100(DatabaseType.GaussDB100,"com.huawei.gauss.jdbc.ZenithDriver","jdbc:zenith:@${host}:${port}?useSSL=false",40000);
    private static final String HOST = "${host}";
    private static final String PORT = "${port}";
    private static final String NAME = "${name}";
    private DatabaseType database;
    private String driverClass;
    private String url;
    private int defaultPort;

    DatabaseDriver(DatabaseType database, String driverClass, String url, int defaultPort) {
        this.database = database;
        this.driverClass = driverClass;
        this.url = url;
        this.defaultPort = defaultPort;
    }


    public DatabaseType getDatabaseType() {
        return this.database;
    }

    public String getDriverClass() {
        return this.driverClass;
    }

    public int getDefaultPort() {
        return this.defaultPort;
    }

    public String buildUrl(String host, String name) {
        return buildUrl(host, defaultPort, name);
    }

    public String buildUrl(String host, int port, String name) {
        return this.url.replace(HOST, host).replace(PORT, Integer.toString(port)).replace(NAME, name);
    }

}
