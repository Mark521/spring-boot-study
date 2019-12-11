package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.DatabaseType;

import java.io.Serializable;
import java.util.*;

public final class DatabaseInfo implements Serializable {

    private static final long serialVersionUID = 4051323422934251828L;
    private static final String NEWLINE = System.getProperty("line.separator");
    private String userName;
    private String productName;
    private String productVersion;
    private int majorVersion;
    private int minorVersion;
    private final Set<DatabaseProperty> databaseProperties = new HashSet<DatabaseProperty>();
    public DatabaseInfo(){

    }
    public String getProductName() {
        return productName;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public Collection<DatabaseProperty> getProperties() {
        final List<DatabaseProperty> properties = new ArrayList<DatabaseProperty>(databaseProperties);
        Collections.sort(properties);

        return properties;
    }

    public String getUserName() {
        return userName;
    }

    public void addAll(final Collection<DatabaseProperty> dbProperties) {
        if (dbProperties != null) {
            databaseProperties.addAll(dbProperties);
        }
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public void setProductVersion(final String productVersion) {
        this.productVersion = productVersion;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    /*
     * BDB 和 SQLite如何确定??
     */
    public DatabaseType getDatabaseType() {
        try {
            if (productName.indexOf("oracle") != -1) {
                return DatabaseType.Oracle;
            } else if (productName.indexOf("db2") != -1) {
                return DatabaseType.DB2;
            } else if (productName.indexOf("sql server") != -1) {
                return DatabaseType.SQLServer;
            } else if (productName.indexOf("postgres") != -1) {
                return DatabaseType.PostgreSQL;
            } else if (productName.indexOf("mysql") != -1) {
                return DatabaseType.MySQL;
            } else if (productName.indexOf("sqlite") != -1) {
                return DatabaseType.SQLite;
            } else if (productName.indexOf("hive") != -1) {
                return DatabaseType.Hive;
            } else if (productName.indexOf("adaptive server enterprise") != -1 || productName.indexOf("ase") != -1) {
                return DatabaseType.Sybase;
            }
        } catch (Exception ex) {
        }
        return DatabaseType.Unknown;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(DatabaseProperty property : databaseProperties){
            sb.append("\n\t\t");
            sb.append(property.getName()).append(" = ").append(property.getValue());
        }
        return "DatabaseInfo{"
                + " \n\tproductName=" + productName
                + " \n\tproductVersion=" + productVersion
                + " \n\tmajorVersion=" + majorVersion
                + " \n\tminorVersion=" + minorVersion
                + " \n\tuserName=" + userName
                + " \n\tdatabaseProperties=" + sb.toString()
                + "\n}";
    }
}
