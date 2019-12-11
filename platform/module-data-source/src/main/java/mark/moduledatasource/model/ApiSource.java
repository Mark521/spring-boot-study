package mark.moduledatasource.model;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 数据源
 * 
 * @author liang
 *
 */
public class ApiSource implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;// 数据库主键
    private String name;// 数据源名称
    private String remark;// 备注
    private boolean enabled;// 是否启用

    // 以下属性由每种数据源自定义配置
    private int type;// 数据源分类，1：关系型数据库，2：大数据，3：文件服务器
    private String key;// 数据源分类唯一标示，XML中定义

    private String url;
    private String host;
    private Integer port;
    private String database;
    private String catalog;
    private String schema;
    private String username;
    private String password;
    // 无论XML中是否配置成file，这个属性都做为文件处理
    // 凭证文件在【默认】文件服务器中的【Key】，多文件使用逗号分隔
    private String credential;
    // 凭证文件在【数据源】文件服务器中的【URL】，多文件使用逗号分隔
    private String credentialUrl;
    // 无论XML中是否配置成file，这个属性都做为文件处理
    // Jdbc驱动Jar在【默认】文件服务器中的【Key】，多文件使用逗号分隔
    private String driverFile;
    // Jdbc驱动Jar在【数据源】文件服务器中的【URL】，多文件使用逗号分隔
    private String driverFileUrl;
    private String driverClass;
    private Integer initialSize;
    private Integer maxActive;
    private Integer minIdle;
    private Long maxWait;// 毫秒
    private String validationQuery;

    private Map<String, String> customMap; // 自定义属性，JSON格式，XML中定义属性如果上面不存在，则保存在这个字段中
    private Map<String, String> customUrlMap; // JSON格式，自定义文件属性在数据源文件服务器中的【URL】保存在这里

    // private boolean thirdAuth;// 是否第三方认证，目前不支持
    // private String authProvider;// 第三方认证提供者，目前不支持

    private SourceDefinition definition;// 冗余属性
    private String keyName;// 冗余属性

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public void setCredentialUrl(String credentialUrl) {
        this.credentialUrl = credentialUrl;
    }

    public String getDriverFile() {
        return driverFile;
    }

    public void setDriverFile(String driverFile) {
        this.driverFile = driverFile;
    }

    public String getDriverFileUrl() {
        return driverFileUrl;
    }

    public void setDriverFileUrl(String driverFileUrl) {
        this.driverFileUrl = driverFileUrl;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Long maxWait) {
        this.maxWait = maxWait;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public String getCustoms() {
        if (customMap == null) {
            return null;
        }
        return new JSONObject(new HashMap<String, Object>(customMap)).toJSONString();
    }

    public void setCustoms(String customs) {
        this.customMap = new HashMap<>();
        if (StringUtils.isBlank(customs)) {
            return;
        }
        JSONObject json = JSONObject.parseObject(customs);
        for (Entry<String, Object> entry : json.entrySet()) {
            if (entry.getValue() != null) {
                this.customMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
    }

    public Map<String, String> getCustomMap() {
        if (customMap == null) {
            customMap = new HashMap<>();
        }
        return customMap;
    }

    public void setCustomMap(Map<String, String> customMap) {
        this.customMap = customMap;
    }

    public String getCustomUrls() {
        if (customUrlMap == null) {
            return null;
        }
        return new JSONObject(new HashMap<String, Object>(customUrlMap)).toJSONString();
    }

    public void setCustomUrls(String customUrls) {
        this.customUrlMap = new HashMap<>();
        if (StringUtils.isBlank(customUrls)) {
            return;
        }
        JSONObject json = JSONObject.parseObject(customUrls);
        for (Entry<String, Object> entry : json.entrySet()) {
            if (entry.getValue() != null) {
                this.customUrlMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
    }

    public Map<String, String> getCustomUrlMap() {
        if (customUrlMap == null) {
            customUrlMap = new HashMap<>();
        }
        return customUrlMap;
    }

    public void setCustomUrlMap(Map<String, String> customUrlMap) {
        this.customUrlMap = customUrlMap;
    }

    public SourceDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(SourceDefinition definition) {
        this.definition = definition;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

}
