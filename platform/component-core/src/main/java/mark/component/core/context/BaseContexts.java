package mark.component.core.context;


import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 全局常量类
 *
 * @author liang
 *
 */
public class BaseContexts {

    public static final String USERID = "USERID";// 用户数据库ID
    public static final String USERACCOUNT = "USERACCOUNT";// 用户登录账号
    public static final String USERNAME = "USERNAME";// 用户姓名
    public static final String USERRESOURCE = "USERRESOURCE";// 用户资源权限
    public static final String USERTENANT = "USERTENANT";// 用户所属租户环境
    public static final String USEROBJECT = "USEROBJECT";// 用户对象
    public static final String USERACCOUNTEXPIRED = "USERACCOUNTEXPIRED";// 用户账号过期时间
    public static final String USERMODIFYPWD = "USERMODIFYPWD";// 用户修改密码策略，null或0不用修改，1提示即将过期，2已经到期强制修改

    public static final int DATA_STATE_VALID = 1;// 有效数据
    public static final int DATA_STATE_INVALID = 0;// 无效数据

    public static final String APIPKG = "com.mark.api.service";
    public static final String APIPKGIMPL = "com.mark.api.service.impl";

    // 有很多业务需要设置操作用户ID，这些业务又不是人工操作，比如自动调度触发的业务，比如用户自己注册的业务
    public static final int SPECIAL_USER_ID = 0;

    public static final String HTTP_HEADER_ACEH = "Access-Control-Expose-Headers";
    public static final String HTTP_HEADER_CODE = "AdqCode";
    public static final String HTTP_HEADER_MSG = "AdqMsg";
    public static final String HTTP_HEADER_LOCATION = "AdqLocation";
    public static final String HTTP_HEADER_DIALOG = "IsDialog";
    public static final String HTTP_HEADER_TOKEN = "AdqToken";

    public static final BaseVariate ADQ_VAR = new BaseVariate();

    public static class BaseVariate {

        // 系统根目录，以斜杠结尾
        public String webRootPath;
        public String contextPath;

        // 应用ID，默认使用web.xml中webAppRootKey对应的值
        public String appId = "DEFAULTAPPID";
        // 应用名称，默认使用web.xml中display-name的值

        // 是否接入ASB
        public boolean asb;
        // 是否集群部署
        public boolean clustered;
        // 是否支持单点登录
        public boolean singleSignon;
        // 是否支持用户自己重置密码
        public boolean userResetPwd;
        // 是否启用租户模式
        public boolean tenantMode;
        // 系统所有配置文件，用于定时加载
        public List<String> properties = new ArrayList<>();

        // 标记系统是否启动完成
        private boolean startFinish;

        public ApplicationContext appCtx;
        public WebApplicationContext webAppCtx;
        public DataSource dataSource;
    }

    private static final Properties props = new Properties();

    public static void setProperty(String key, String value) {
        props.put(key, value);
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String def) {
        return props.getProperty(key, def);
    }

    public static boolean getBoolean(String name) {
        boolean result = false;
        try {
            result = Boolean.parseBoolean(getProperty(name));
        } catch (IllegalArgumentException | NullPointerException e) {
        }
        return result;
    }

    public static Integer getInteger(String nm) {
        return getInteger(nm, null);
    }

    public static Integer getInteger(String nm, int val) {
        Integer result = getInteger(nm, null);
        return (result == null) ? Integer.valueOf(val) : result;
    }

    public static Integer getInteger(String nm, Integer val) {
        String v = null;
        try {
            v = getProperty(nm);
        } catch (IllegalArgumentException | NullPointerException e) {
        }
        if (v != null) {
            try {
                return Integer.decode(v);
            } catch (NumberFormatException e) {
            }
        }
        return val;
    }

    public static Long getLong(String nm) {
        return getLong(nm, null);
    }

    public static Long getLong(String nm, long val) {
        Long result = getLong(nm, null);
        return (result == null) ? Long.valueOf(val) : result;
    }

    public static Long getLong(String nm, Long val) {
        String v = null;
        try {
            v = getProperty(nm);
        } catch (IllegalArgumentException | NullPointerException e) {
        }
        if (v != null) {
            try {
                return Long.decode(v);
            } catch (NumberFormatException e) {
            }
        }
        return val;
    }

    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    public static boolean checkStartFinish() {
        if (ADQ_VAR.startFinish) {
            return true;
        }
        lock.lock();
        try {
            if (ADQ_VAR.startFinish) {
                return true;
            }
            boolean flag = true;
            while (flag) {
                try {
                    condition.await();
                    flag = false;
                } catch (InterruptedException e) {
                    return false;
                }
            }
        } finally {
            lock.unlock();
        }
        return true;
    }

    public static void setStartFinish() {
        if (ADQ_VAR.startFinish) {
            return;
        }
        lock.lock();
        try {
            ADQ_VAR.startFinish = true;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

