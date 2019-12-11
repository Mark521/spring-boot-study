package mark.moduledatasource.util;

import mark.component.dbmodel.constans.DatabaseDriver;
import mark.component.core.exception.BaseException;
import mark.component.core.exception.ErrorCodeFactory;
import mark.moduledatasource.constans.DBContextHolder;
import mark.moduledatasource.dynamic.AbstractDynamicDataSource;
import mark.moduledatasource.dynamic.DruidDynamicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: ting.huang
 * @Date: 2018/7/12 17:45
 * @Description:
 */

@Component
public class DruidUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static final Log LOG = LogFactory.getLog(DruidUtil.class);
    private static final Logger logger = LoggerFactory.getLogger(DruidUtil.class);
    private static final Map<String, Integer> DB_DATA_CHAR_TYPE = new HashMap<String, Integer>(10);
    private static final Map<String, Integer> DB_DATA_NUMERIC_TYPE = new HashMap<String, Integer>(10);
    private static final Map<String, Integer> DB_DATA_DATE_TYPE = new HashMap<String, Integer>(4);
    static{
        DB_DATA_CHAR_TYPE.put("CHAR", 2000);
        DB_DATA_CHAR_TYPE.put("VARCHAR2", 4000);
        DB_DATA_CHAR_TYPE.put("NCHAR", 2000);
        DB_DATA_CHAR_TYPE.put("NVARCHAR2", 4000);

        DB_DATA_DATE_TYPE.put("DATE", -1);
        DB_DATA_DATE_TYPE.put("TIMESTAMP", -1);
        DB_DATA_DATE_TYPE.put("TIME", -1);

        DB_DATA_NUMERIC_TYPE.put("LONG", -1);
        DB_DATA_NUMERIC_TYPE.put("NUMBER", -1);
        DB_DATA_NUMERIC_TYPE.put("DECIMAL", -1);
        DB_DATA_NUMERIC_TYPE.put("INTEGER", -1);
        DB_DATA_NUMERIC_TYPE.put("DOUBLE", -1);
        DB_DATA_NUMERIC_TYPE.put("NUMERIC", -1);
        DB_DATA_NUMERIC_TYPE.put("BIGINT", -1);
        DB_DATA_NUMERIC_TYPE.put("SMALLINT", -1);
        DB_DATA_NUMERIC_TYPE.put("TINYINT", -1);
    }

    public static final int ERROR_CODE_IS_NOT_CHAR = 709010;
    public static final int ERROR_CODE_IS_NOT_NUMERIC = 709020;
    public static final int ERROR_CODE_IS_NOT_DATE = 709030;

    public static DataSource getDataSource(String databaseType, String dbConUrl, String userName, String password) {
        DataSource dataSource = null;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            String key = dbConUrl + "@" + userName;
            map.put(DBContextHolder.DATASOURCE_KEY,key);
            DatabaseDriver databaseDriver=convertDriver(databaseType);
            map.put(DBContextHolder.DATASOURCE_DRIVER,databaseDriver.getDriverClass());
            map.put(DBContextHolder.DATASOURCE_URL,
                    dbConUrl);
            map.put(DBContextHolder.DATASOURCE_USERNAME, userName);
            map.put(DBContextHolder.DATASOURCE_PASSWORD, password);
            DruidDynamicDataSource druidDataSourceBean = applicationContext.getBean(DruidDynamicDataSource.class);
            druidDataSourceBean.verifyAndInitDataSource(map);
            dataSource = druidDataSourceBean.getDataSource(key);
        } catch (Exception e) {
            LOG.error("Failed to get getDataSource: ("+dbConUrl+"@"+userName+"):"+e.getMessage());
            throw new BaseException(e.getMessage());
        }
        return dataSource;
    }

    public static Connection getConnection(String databaseType, String dbConUrl, String userName, String password) {
        Connection conn = null;
        try {
            Date start = new Date();
            if(logger.isDebugEnabled()){
                logger.debug("DruidUtil getConnection, dbConUrl:{}, userName:{} start;", dbConUrl, userName);
            }
            DataSource dataSource = getDataSource(databaseType, dbConUrl, userName, password);
            if(logger.isDebugEnabled()){
                logger.debug("DruidUtil getConnection, dbConUrl:{}, userName:{} end, startTime:{}, endTime:{};"
                        , dbConUrl, userName, MarkDateUtils.toString(start), MarkDateUtils.toString(new Date()));
            }
            conn=dataSource.getConnection();
        } catch (Exception e) {
           LOG.error("Failed to get database connection: ("+dbConUrl+"@"+userName+"):"+e.getMessage());
            throw new BaseException(e.getMessage());
        }
        return conn;
    }

    public static void closeConnection(Connection conn){
        DbUtils.closeQuietly(conn);
        DruidUtil.resetDefaultDataSource();
    }
    public static DatabaseDriver convertDriver(String dsDbType){
        //暂时解决方案：因为没有连接方式的参数，所以同一种数据库默认返回第一个驱动
        for(DatabaseDriver dd : DatabaseDriver.values()){
            if(dd.getDatabaseType().getName().equalsIgnoreCase(dsDbType)){
                return dd;
            }
        }
        return DatabaseDriver.Unknown;
    }
    public static void resetDefaultDataSource(){
        DBContextHolder.clearDBType();
    }
    public static Connection getDefaultConnection(){
        Connection conn = null;
        try {
            WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
            DruidDynamicDataSource druidDataSourceBean = wac.getBean(DruidDynamicDataSource.class);
            DataSource datasource=druidDataSourceBean.getDataSource(AbstractDynamicDataSource.DEFAULT_DATASOURCE_KEY);
            conn = datasource.getConnection();
        } catch (Exception e) {
            LOG.error("Failed to get the default database connection: "+e.getMessage());
        }
        return conn;
    }
    public static void validateDateType(String value, String dataType) throws BaseException {
        if(StringUtils.isNotEmpty(value)){
            if(DruidUtil.DB_DATA_CHAR_TYPE.containsKey(dataType.toUpperCase())){
                int length = DruidUtil.DB_DATA_CHAR_TYPE.get(dataType);
                if(value.length() > length )
                    throw ErrorCodeFactory.getErrorCode(DruidUtil.ERROR_CODE_IS_NOT_CHAR, value).newException();
            }else {
                if (DruidUtil.DB_DATA_NUMERIC_TYPE.containsKey(dataType.toUpperCase())) {
                    if (!NumberUtils.isNumber(value))
                        throw ErrorCodeFactory.getErrorCode(ERROR_CODE_IS_NOT_NUMERIC, value).newException();
                } else {
                    if (DruidUtil.DB_DATA_DATE_TYPE.containsKey(dataType.toUpperCase())) {
						/*String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1][0-9])|([2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
						Pattern p = Pattern.compile(eL);
						Matcher m = p.matcher(value);
						if(!m.matches()) {
							throw new BaseException(ERROR_CODE_IS_NOT_DATE, value);
						}        */
                        try {
							/*if(value.contains("/")){
								value = value.trim().replaceAll("/", "-");
							}*/
                            String[] dt = value.trim().split(" ");
                            if (dt.length == 1) {
                                value = dt[0].trim() + " 00:00:00.0";
                            }
                            Timestamp.valueOf(value).toString();
                        } catch (Exception ex) {
                            throw ErrorCodeFactory.getErrorCode(ERROR_CODE_IS_NOT_DATE, value).newException();
                        }
                    }
                }
            }
        }
    }
    public static String regSourceTable(String sourceTable) {
        String[] reg = { "^\"(.+?)\"\\.\"(.+?)\"(\\.\"(.+)\")?$", // oracle
                "^`(.+?)`\\.`(.+?)`(\\.`(.+)`)?$", // mysql
                "^\\[(.+?)\\]\\.\\[(.+?)\\](\\.\\[(.+)\\])?$" // sqlserver
        };

        for (int i = 0; i < reg.length; ++i) {
            Pattern pattern = Pattern.compile(reg[i]);
            Matcher matcher = pattern.matcher(sourceTable);
            if (matcher.find()) {
                String catalog = matcher.group(1);
                String schema = matcher.group(2);
                String tableName = matcher.group(4);
                if (tableName == null) {
                    sourceTable = catalog + "." + schema;
                } else {
                    sourceTable = catalog + "." + schema + "." + tableName;
                }
                return sourceTable;
            }
        }
        return sourceTable;
    }
}
