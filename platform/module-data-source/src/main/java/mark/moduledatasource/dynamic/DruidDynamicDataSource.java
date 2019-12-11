package mark.moduledatasource.dynamic;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * @Author: ting.huang
 * @Date: 2018/7/12 17:10
 * @Description:
 */

public class DruidDynamicDataSource extends AbstractDynamicDataSource<DruidDataSource> {

    private Logger logger = LoggerFactory.getLogger(DruidDynamicDataSource.class);

    private boolean testWhileIdle = true;
    private boolean testOnBorrow = false;
    private boolean testOnReturn = false;
    // <!-- 配置初始化大小、最小、最大活跃 -->
    private  int initialSize = 5;
    private  int minIdle = 10;
    private  int maxActive = 20;
    private  long maxWait = 60000;// 配置获取连接等待超时的时间
    private  long  timeBetweenEvictionRunsMillis = 2000;
    //<!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
    private  long  minEvictableIdleTimeMillis  = 600000;
    private  long  maxEvictableIdleTimeMillis  = 900000;
    private  long  phyTimeoutMillis = 1200000;//强制退出连接时间

    // 是否打开连接泄露自动检测
    private boolean removeAbandoned = true;
    // 连接长时间没有使用，被认为发生泄露时长
    private long removeAbandonedTimeoutMillis = 1800000;//30分钟
    // 发生泄露时是否需要输出 log，建议在开启连接泄露检测时开启，方便排错
    private boolean logAbandoned = true;

    // 只要maxPoolPreparedStatementPerConnectionSize>0,poolPreparedStatements就会被自动设定为true，使用oracle时可以设定此值。
    // private int maxPoolPreparedStatementPerConnectionSize = -1;

    // 配置监控统计拦截的filters
    private String filters; // 监控统计："stat" 防SQL注入："wall" 组合使用： "stat,wall"
    private List<Filter> filterList;

    /*
     * 创建数据源
     * @see com.cdelabcare.pubservice.datasource.IDynamicDataSource#createDataSource(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public DruidDataSource createDataSource(String driverClassName, String url, String username,
                                            String password) {
//        DruidDataSource parent = (DruidDataSource) super.getApplicationContext().getBean(
//                DEFAULT_DATASOURCE_KEY);
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driverClassName);
//        ds.setInitialSize(parent.getInitialSize());
//        ds.setMinIdle(parent.getMinIdle());
//        ds.setMaxActive(parent.getMaxActive());
//        ds.setMaxWait(parent.getMaxWait());
//        ds.setTimeBetweenConnectErrorMillis(parent.getTimeBetweenConnectErrorMillis());
//        ds.setTimeBetweenEvictionRunsMillis(parent.getTimeBetweenEvictionRunsMillis());
//        ds.setMinEvictableIdleTimeMillis(parent.getMinEvictableIdleTimeMillis());
        ds.setInitialSize(initialSize);
        ds.setMinIdle(minIdle);
        ds.setMaxActive(maxActive);
        ds.setMaxWait(maxWait);
        ds.setTimeBetweenConnectErrorMillis(60000);
        ds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        ds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        ds.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);
        ds.setPhyTimeoutMillis(phyTimeoutMillis);
        //关闭连接失败自动重连功能
        ds.setConnectionErrorRetryAttempts(0);
        ds.setBreakAfterAcquireFailure(true);
        //parent.getValidationQuery() 返回 select 'X'
        if (driverClassName.indexOf("oracle") > -1) {
            ds.setValidationQuery("select 'X' from dual");
            ds.addConnectionProperty("remarksReporting", "true");
        } else if (driverClassName.indexOf("db2") > -1) {
            ds.setValidationQuery("SELECT CURRENT DATE FROM SYSIBM.SYSDUMMY1");
        } else {
            ds.setValidationQuery("SELECT 'x'");
        }

        if (driverClassName.indexOf("oracle") > -1) {
        	ds.addConnectionProperty("remarksReporting", "true");
        } else if (driverClassName.indexOf("mysql") > -1) {
            ds.setUrl(url + "&remarks=true&useInformationSchema=true");
        }
        ds.setTestWhileIdle(testWhileIdle);
        ds.setTestOnBorrow(testOnBorrow);
        ds.setTestOnReturn(testOnReturn);

        ds.setRemoveAbandoned(removeAbandoned);
        ds.setRemoveAbandonedTimeoutMillis(removeAbandonedTimeoutMillis);
        ds.setLogAbandoned(logAbandoned);

        // 只要maxPoolPreparedStatementPerConnectionSize>0,poolPreparedStatements就会被自动设定为true，参照druid的源码
        ds.setMaxPoolPreparedStatementPerConnectionSize(20);

        if (StringUtils.isNotBlank(filters)) {
            try {
                ds.setFilters(filters);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        logger.info("createDataSource: driverClass:{}, url:{}, userName:{}", driverClassName, url, username);
        if(logger.isDebugEnabled()){
            logger.debug("createDataSource: password:{}", password);
        }

        addFilterList(ds);
        if (driverClassName.indexOf("mysql") > -1) {
            Properties properties = new Properties();
            properties.setProperty("remarks", "true");//读取表备注信息
            properties.setProperty("useInformationSchema", "true");
            ds.setConnectProperties(properties);
        }
        return ds;
    }

    private void addFilterList(DruidDataSource ds) {
        if (filterList != null) {
            List<Filter> targetList = ds.getProxyFilters();
            for (Filter add : filterList) {
                boolean found = false;
                for (Filter target : targetList) {
                    if (add.getClass().equals(target.getClass())) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    targetList.add(add);
            }
        }
    }
}
