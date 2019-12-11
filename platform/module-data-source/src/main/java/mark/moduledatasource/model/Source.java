package mark.moduledatasource.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.PropertyPlaceholderHelper;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

public class Source extends ApiSource {

    private static final long serialVersionUID = 1L;

    private boolean inset;// 内置数据源，在XML中配置
    private boolean valid;// 是否有效

    private boolean publicSource;// 是否公开的数据源（所有应用都可使用）
    private String appIds;// 非公开时，允许使用此数据源的应用ID，逗号开关，逗号结尾，逗号分隔

    private Integer createUser;// 创建人
    private Long createTime;// 创建时间
    private Integer updateUser;// 修改人
    private Long updateTime;// 修改时间

    private List<String> appNames;// 冗余属性

    public Source() {

    }

    public Source(ApiSource api) {
        setId(api.getId());
        setName(api.getName());
        setRemark(api.getRemark());
        setEnabled(api.isEnabled());

        setType(api.getType());
        setKey(api.getKey());

        setUrl(api.getUrl());
        setHost(api.getHost());
        setPort(api.getPort());
        setDatabase(api.getDatabase());
        setCatalog(api.getCatalog());
        setSchema(api.getSchema());
        setUsername(api.getUsername());
        setPassword(api.getPassword());

        setCredential(api.getCredential());
        setCredentialUrl(api.getCredentialUrl());
        setDriverFile(api.getDriverFile());
        setDriverFileUrl(api.getDriverFileUrl());
        setDriverClass(api.getDriverClass());

        setInitialSize(api.getInitialSize());
        setMaxActive(api.getMaxActive());
        setMinIdle(api.getMinIdle());
        setMaxWait(api.getMaxWait());

        setCustoms(api.getCustoms());
        setCustomMap(api.getCustomMap());
        setDefinition(api.getDefinition());
    }

    public Source(Map<String, String> properties, SourceDefinition definition) throws Exception {
        Map<String, String> pro = new HashMap<>(properties);
        if (properties.containsKey("id")) {
            setId(Integer.valueOf(properties.get("id")));
        }
        setName(properties.get("name"));
        setRemark(properties.get("remark"));
        setEnabled(Boolean.parseBoolean(properties.get("enabled")));

        setType(definition.getType());
        setKey(definition.getKey());

        setPublicSource(Boolean.parseBoolean(pro.get("publicSource")));
        setAppIds(pro.get("appIds"));

        Properties pp = new Properties();
        Map<String, String> customMap = new HashMap<>();
        // 未在SourceDefinition中定义的属性直接忽略掉
        for (SourcePro sp : definition.getProperties()) {
            if (sp.isRequired() && !pro.containsKey(sp.getKey())) {
                if (getId() != null && sp.isFile()) {
                    // 修改操作，并且是文件，则可以不填
                } else if (sp.getParentSelect() != null && sp.getDisplayValue() != null) {
                    // 动态组件，只在部分场景下必填
                    String parentValue = properties.get(sp.getParentSelect());
                    List<String> displayValue = Arrays.asList(sp.getDisplayValue().split(","));
                    if (parentValue != null && displayValue.contains(parentValue)) {
                        throw new Exception("必填属性" + sp.getKey() + "为空");
                    }
                } else {
                    throw new Exception("必填属性" + sp.getKey() + "为空");
                }
            }
            String value = pro.get(sp.getKey());
            if (StringUtils.isBlank(value)) {
                if (StringUtils.isNotBlank(sp.getDefaultValue())) {
                    value = sp.getDefaultValue();
                } else {
                    continue;
                }
            }
            pp.put(sp.getKey(), value);
            try {
                Field field = ApiSource.class.getDeclaredField(sp.getKey());
                field.setAccessible(true);
                if (field.getType() == Integer.class) {
                    field.set(this, Integer.valueOf(value));
                } else if (field.getType() == Long.class) {
                    field.set(this, Long.valueOf(value));
                } else {
                    field.set(this, value);
                }
            } catch (NoSuchFieldException e) {
                // 自定义的属性
                customMap.put(sp.getKey(), value);
            } catch (Exception e) {
                throw e;
            }
        }
        if (definition.getExpression() != null) {
            for (Entry<String, String> entry : definition.getExpression().entrySet()) {
                PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("{", "}");
                String value = helper.replacePlaceholders(entry.getValue(), pp);
                pp.put(entry.getKey(), value);
                try {
                    Field field = ApiSource.class.getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    if (field.getType() == Integer.class) {
                        field.set(this, Integer.valueOf(value));
                    } else if (field.getType() == Long.class) {
                        field.set(this, Long.valueOf(value));
                    } else {
                        field.set(this, value);
                    }
                } catch (NoSuchFieldException e) {
                    // 自定义的属性
                    customMap.put(entry.getKey(), value);
                } catch (Exception e) {
                    throw e;
                }
            }
        }

        if (!customMap.isEmpty()) {
            setCustomMap(customMap);
        }
    }

    public boolean isInset() {
        return inset;
    }

    public void setInset(boolean inset) {
        this.inset = inset;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isPublicSource() {
        return publicSource;
    }

    public void setPublicSource(boolean publicSource) {
        this.publicSource = publicSource;
    }

    public String getAppIds() {
        return this.appIds;
    }

    public void setAppIds(String appIds) {
        if (appIds == null) {
            return;
        }
        appIds = appIds.replace(" ", "");
        if (!appIds.startsWith(",")) {
            appIds = "," + appIds;
        }
        if (!appIds.endsWith(",")) {
            appIds = appIds + ",";
        }
        this.appIds = appIds;
    }

    public List<String> getAppIdList() {
        if (this.appIds == null) {
            return null;
        }
        List<String> list = new ArrayList<>();
        String[] array = appIds.split(",");
        for (String id : array) {
            if (StringUtils.isBlank(id)) {
                continue;
            }
            list.add(id);
        }
        return list;
    }

    public void setAppIdList(List<String> appIdList) {
        if (appIdList == null || appIdList.isEmpty()) {
            this.appIds = null;
        }
        setAppIds(StringUtils.join(appIdList, ","));
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        if (createTime == null) {
            return null;
        }
        return new Date(createTime);
    }

    public void setCreateTime(Date createTime) {
        if (createTime == null) {
            this.createTime = null;
            return;
        }
        this.createTime = createTime.getTime();
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        if (updateTime == null) {
            return null;
        }
        return new Date(updateTime);
    }

    public void setUpdateTime(Date updateTime) {
        if (updateTime == null) {
            this.updateTime = null;
            return;
        }
        this.updateTime = updateTime.getTime();
    }

    public List<String> getAppNames() {
        return appNames;
    }

    public void setAppNames(List<String> appNames) {
        this.appNames = appNames;
    }

    public ApiSource toApiSource() {
        ApiSource api = new ApiSource();
        api.setId(getId());
        api.setName(getName());
        api.setRemark(getRemark());
        api.setEnabled(isEnabled());

        api.setType(getType());
        api.setKey(getKey());

        api.setUrl(getUrl());
        api.setHost(getHost());
        api.setPort(getPort());
        api.setDatabase(getDatabase());
        api.setCatalog(getCatalog());
        api.setSchema(getSchema());
        api.setUsername(getUsername());
        api.setPassword(getPassword());

        api.setCredential(getCredential());
        api.setCredentialUrl(getCredentialUrl());
        api.setDriverFile(getDriverFile());
        api.setDriverFileUrl(getDriverFileUrl());
        api.setDriverClass(getDriverClass());

        api.setInitialSize(getInitialSize());
        api.setMaxActive(getMaxActive());
        api.setMinIdle(getMinIdle());
        api.setMaxWait(getMaxWait());

        api.setCustomMap(getCustomMap());
        api.setCustomUrlMap(getCustomUrlMap());
        api.setDefinition(getDefinition());
        return api;
    }
}
