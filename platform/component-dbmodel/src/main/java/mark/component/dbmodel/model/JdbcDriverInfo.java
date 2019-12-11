package mark.component.dbmodel.model;

import java.io.Serializable;
import java.util.*;

public final class JdbcDriverInfo implements Serializable {

    private static final long serialVersionUID = 8030156654422512161L;
    private static final String NEWLINE = System.getProperty("line.separator");
    private String driverName;
    private String driverClassName;
    private String driverVersion;
    private String connectionUrl;
    private boolean jdbcCompliant;
    private final Set<JdbcDriverProperty> jdbcDriverProperties = new HashSet<JdbcDriverProperty>();

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getDriverName() {
        return driverName;
    }

    public Collection< JdbcDriverProperty> getDriverProperties() {
        final List< JdbcDriverProperty> properties = new ArrayList< JdbcDriverProperty>(jdbcDriverProperties);
        Collections.sort(properties);
        return properties;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public boolean isJdbcCompliant() {
        return jdbcCompliant;
    }

    public void addJdbcDriverProperty(final JdbcDriverProperty jdbcDriverProperty) {
        jdbcDriverProperties.add(jdbcDriverProperty);
    }

    public void setConnectionUrl(final String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public void setDriverName(final String driverName) {
        this.driverName = driverName;
    }

    public void setDriverVersion(final String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public void setJdbcCompliant(final boolean jdbcCompliant) {
        this.jdbcCompliant = jdbcCompliant;
    }

    public void setJdbcDriverClassName(final String jdbcDriverClassName) {
        driverClassName = jdbcDriverClassName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(JdbcDriverProperty property : jdbcDriverProperties){
            sb.append("\n\t\t");
            sb.append(property.getName()).append(" = ").append(property.getValue());
        }
        
        return "JdbcDriverInfo{" + 
                " \n\tdriverName=" + driverName + 
                " \n\tdriverClassName=" + driverClassName + 
                " \n\tdriverVersion=" + driverVersion + 
                " \n\tconnectionUrl=" + connectionUrl + 
                " \n\tjdbcCompliant=" + jdbcCompliant + 
                " \n\tjdbcDriverProperties=" + sb.toString() + 
                "\n}";
    }
}
