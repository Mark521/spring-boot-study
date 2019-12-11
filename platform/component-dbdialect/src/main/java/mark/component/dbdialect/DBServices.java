package mark.component.dbdialect;

import mark.component.dbmodel.model.DatabaseInfo;
import mark.component.core.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * liang
 */
public class DBServices {

    private Logger logger = LoggerFactory.getLogger(DBServices.class);

    private class DBKey {

        final public Class<?> clazz;
        final public DatabaseInfo databaseInfo;

        public DBKey(Class<?> clazz, DatabaseInfo databaseInfo) {
            this.clazz = clazz;
            this.databaseInfo = databaseInfo;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof DBKey)) {
                return false;
            }
            DBKey key = (DBKey) obj;
            return clazz.equals(key.clazz) && equal(databaseInfo, key.databaseInfo);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 53 * hash + (this.clazz != null ? this.clazz.hashCode() : 0);
            hash = 53 * hash + (this.databaseInfo != null
                    ? this.databaseInfo.getProductName().hashCode()
                    + this.databaseInfo.getMajorVersion()
                    + this.databaseInfo.getMinorVersion()
                    : 0);
            return hash;
        }
    }
    private final static DBServices SERVICE = new DBServices();
    private final Map<DBKey, Object> services = new LinkedHashMap<DBKey, Object>();

    private DBServices() {
    }

    public static DBServices instance() {
        return SERVICE;
    }

    public synchronized <T extends Object> T lookup(Connection connection, Class<T> clazz) throws BaseException {
        if (connection == null) {
            return null;
        }
        DatabaseInfo databaseInfo = getDatabaseInfo(connection);
        DBKey key = new DBKey(clazz, databaseInfo);
        Object obj = services.get(key);
        if (obj == null) {
            logger.error("service is not load,now load service:{}", key);
            obj = loadServices(connection, key);
            services.put(key, obj);
        }
        if (obj == null) {
            throw new Error("No Implemented Service For " + clazz.getName());
        }
        return clazz.cast(obj);
    }

    public int match(DatabaseInfo src, DatabaseInfo desc) {
        int matchRate = 0;
        if (src.getProductName().equalsIgnoreCase(desc.getProductName())) {
            matchRate += 10000000;
        }
        if (src.getMajorVersion() == desc.getMajorVersion()) {
            matchRate += 1000000;
        } else if (src.getMajorVersion() == 0) {
            matchRate += 100000;
        } else if (desc.getMajorVersion() == 0) {
            matchRate += 10000;
        }

        if (src.getMinorVersion() == desc.getMinorVersion()) {
            matchRate += 1000;
        } else if (src.getMinorVersion() == 0) {
            matchRate += 100;
        } else if (desc.getMinorVersion() == 0) {
            matchRate += 1;
        }
        return matchRate;
    }

    public static DatabaseInfo getDatabaseInfo(Connection conn) {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        String productName = "";
        int majorVersion = 0;
        int minorVersion = 0;
        try {
            productName = conn.getMetaData().getDatabaseProductName();
            if (productName != null) {
                //"DB2/LINUXX8664" => "DB2"
                productName = productName.split("/")[0];
            }
        } catch (SQLException ex) {
        }

        try {
            majorVersion = conn.getMetaData().getDatabaseMajorVersion();
        } catch (SQLException ex) {
        }
        try {
            minorVersion = conn.getMetaData().getDatabaseMinorVersion();
        } catch (SQLException ex) {
        }

        databaseInfo.setProductName(productName);
        databaseInfo.setMajorVersion(majorVersion);
        databaseInfo.setMinorVersion(minorVersion);

        return databaseInfo;
    }

    public boolean equal(DatabaseInfo o1, DatabaseInfo o2) {
        return o1.getProductName().equalsIgnoreCase(o2.getProductName())
                && o1.getMajorVersion() == o2.getMajorVersion()
                && o1.getMinorVersion() == o2.getMinorVersion();
    }

    private Object loadServices(Connection conn, DBKey key) throws BaseException {
        //Map<DatabaseInfo, Object> thisServices = new HashMap<DatabaseInfo, Object>();
        Class<?> clazz = key.clazz;
        DatabaseInfo databaseInfo = key.databaseInfo;

        ServiceLoader<?> sl = ServiceLoader.load(clazz);
        Iterator itr = sl.iterator();
        int match = 0;
        Object result = null;
        /*
         * 寻找最佳匹配
         */
        while (itr.hasNext()) {
            Object obj = itr.next();
            ProductAnnotation product = obj.getClass().getAnnotation(ProductAnnotation.class);
            if (product == null) {
                continue;
            }
            DatabaseInfo serviceDBInfo = new DatabaseInfo();
            serviceDBInfo.setProductName(product.productName());
            serviceDBInfo.setMajorVersion(product.majorVersion());
            serviceDBInfo.setMinorVersion(product.minorVersion());

            int matchRate = match(databaseInfo, serviceDBInfo);
            if (match < matchRate) {
                match = matchRate;
                result = obj;
            }
        }
        if (result != null) {
            if (result instanceof DBInitialize) {
                ((DBInitialize) result).initialize(conn);
            }
        }
        return result;
    }

    private Map<DatabaseInfo, Object> loadServices(Connection conn, Class clazz) throws BaseException {
        Map<DatabaseInfo, Object> thisServices = new HashMap<DatabaseInfo, Object>();
        ServiceLoader<?> sl = ServiceLoader.load(clazz);
        Iterator itr = sl.iterator();
        while (itr.hasNext()) {
            Object obj = itr.next();
            ProductAnnotation product = obj.getClass().getAnnotation(ProductAnnotation.class);
            if (product == null) {
                continue;
            }
            DatabaseInfo databaseInfo = new DatabaseInfo();
            databaseInfo.setProductName(product.productName());
            databaseInfo.setMajorVersion(product.majorVersion());
            databaseInfo.setMinorVersion(product.minorVersion());
            /*
             * 查找有冲突的实现
             */
            for (Map.Entry<DatabaseInfo, Object> entry : thisServices.entrySet()) {
                DatabaseInfo di = entry.getKey();
                Object object = entry.getValue();
                if (equal(databaseInfo, di)) {
                    throw new Error("two services has the same conf : "
                            + obj.getClass().getName() + ", "
                            + object.getClass().getName());
                }
            }
            if (obj instanceof DBInitialize) {
                ((DBInitialize) obj).initialize(conn);
            }
            thisServices.put(databaseInfo, obj);
        }
        return thisServices;
    }
}
