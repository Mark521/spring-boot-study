package mark.moduledatasource.dynamic;

import mark.moduledatasource.constans.DBContextHolder;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @Author: ting.huang
 * @Date: 2018/7/12 17:06
 * @Description:
 */
public abstract class AbstractDynamicDataSource<T extends DataSource> extends AbstractRoutingDataSource
        implements
        ApplicationContextAware {
     /** 日志 */
    protected Logger logger = LoggerFactory.getLogger(getClass());
    /** 默认的数据源KEY */
    public static final String DEFAULT_DATASOURCE_KEY = "dataSource";//defaultDataSource

    /** 数据源KEY-VALUE键值对 */
    public Map<Object, Object> targetDataSources;

    /** spring容器上下文 */
    private static ApplicationContext ctx;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    public static Object getBean(String name) {
        return ctx.getBean(name);
    }

    /**
     * @param targetDataSources the targetDataSources to set
     */
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        this.targetDataSources = targetDataSources;
        super.setTargetDataSources(targetDataSources);
        // afterPropertiesSet()方法调用时用来将targetDataSources的属性写入resolvedDataSources中的
        super.afterPropertiesSet();
    }

    /**
     * 创建数据源
     * @param driverClassName 数据库驱动名称
     * @param url 连接地址
     * @param username 用户名
     * @param password 密码
     * @return 数据源{@link T}
     */
    public abstract T createDataSource(String driverClassName, String url, String username, String password);

    /**
     * 设置系统当前使用的数据源
     * <p>数据源为空或者为0时，自动切换至默认数据源，即在配置文件中定义的默认数据源
     * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
     */
    @Override
    protected Object determineCurrentLookupKey() {
        //logger.info("【Setting up the data source currently used by the system】");
        Map<String, Object> configMap = DBContextHolder.getDBType();
        //logger.info("【Current data source configuration：{}】", configMap);
        if (MapUtils.isEmpty(configMap)) {
            // 使用默认数据源
            return DEFAULT_DATASOURCE_KEY;
        }
        // 判断数据源是否需要初始化
        this.verifyAndInitDataSource();
        //logger.info("【Switch to Data Source：{}】", configMap);
        return configMap.get(DBContextHolder.DATASOURCE_KEY);
    }

    /**
     * 判断数据源是否需要初始化
     */
    private void verifyAndInitDataSource() {
        Map<String, Object> configMap = DBContextHolder.getDBType();
        Object obj = this.targetDataSources.get(configMap.get(DBContextHolder.DATASOURCE_KEY));
        if (obj != null) {
            return;
        }
        logger.info("【Initialization of data sources】");
        T datasource = this.createDataSource(configMap.get(DBContextHolder.DATASOURCE_DRIVER)
                        .toString(), configMap.get(DBContextHolder.DATASOURCE_URL).toString(),
                configMap.get(DBContextHolder.DATASOURCE_USERNAME).toString(),
                configMap.get(DBContextHolder.DATASOURCE_PASSWORD).toString());
        this.addTargetDataSource(configMap.get(DBContextHolder.DATASOURCE_KEY).toString(),
                datasource);
    }
    /**
     * 判断数据源是否需要初始化
     */
    public void verifyAndInitDataSource(Map<String, Object> configMap) {

        Object obj = this.targetDataSources.get(configMap.get(DBContextHolder.DATASOURCE_KEY));
        if (obj != null) {
            return;
        }
        logger.info("【Initialization of data sources】");
        T datasource = this.createDataSource(configMap.get(DBContextHolder.DATASOURCE_DRIVER)
                        .toString(), configMap.get(DBContextHolder.DATASOURCE_URL).toString(),
                configMap.get(DBContextHolder.DATASOURCE_USERNAME).toString(),
                configMap.get(DBContextHolder.DATASOURCE_PASSWORD).toString());
        this.addTargetDataSource(configMap.get(DBContextHolder.DATASOURCE_KEY).toString(),
                datasource);
    }
    /**
     * 往数据源key-value键值对集合添加新的数据源
     * @param key 新的数据源键
     * @param dataSource 新的数据源
     */
    private void addTargetDataSource(String key, T dataSource) {
        this.targetDataSources.put(key, dataSource);
        super.setTargetDataSources(this.targetDataSources);
        super.afterPropertiesSet();
    }

    public T getDataSource(String key){
        if(targetDataSources.containsKey(key)){
            T ds= (T) this.targetDataSources.get(key);
            return ds;
        }else{
            return null;
        }

    }
}
