package mark.component.dbdialect;

import mark.component.core.exception.BaseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

/**
 * 类名称： DialectRepository
 * 描述：
 * 查找实现版本方案举例：向下查找最新实现
 * Oracle实现版本有：11.0、10.3、10.1、10.0、8.0、Oracle默认实现(0.0)、方言默认实现
 * 真实版本11.2-->11.0
 * 真实版本10.2-->10.1
 * 真实版本9.2-->8.0
 * 真实版本8.2-->8.0
 * 
 * 创建：   liang, 2013-3-22 9:18:37
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
@Deprecated
public class DialectRepository {

    private static Map<ProductDialect, Map<ProductVersion, Object>> dialectInstances = new HashMap<ProductDialect, Map<ProductVersion, Object>>();
    private static Map<Class, Object> defaultInstances = new HashMap<Class, Object>();

    public static synchronized <T extends Object> T lookupDialect(Connection conn, Class<T> clazz) throws BaseException {
        if (!loadFlag.contains(clazz.getName())) {
            lazyLoad(conn, clazz);
        }
        ProductDialect dialect = new ProductDialect(conn, clazz);
        ProductVersion product = new ProductVersion(conn, clazz);

        //查找数据库的具体实现
        Map<ProductVersion, Object> dialectInstance = dialectInstances.get(dialect);
        if (dialectInstance != null) {
            for (Entry<ProductVersion, Object> instance : dialectInstance.entrySet()) {
                if (product.majorVersion > instance.getKey().majorVersion
                        || (product.majorVersion == instance.getKey().majorVersion && product.minorVersion >= instance.getKey().minorVersion)) {
                    return clazz.cast(instance.getValue());
                }
            }
        }
        //查找默认实现
        Object obj = defaultInstances.get(clazz);
        if (obj == null) {
            assert false : "not found dialect class!";
            //断言没打开
            return null;
        }
        return clazz.cast(obj);
    }
    private static Set<String> loadFlag = new HashSet<String>();

    private static void lazyLoad(Connection conn, Class clazz) throws BaseException {
        loadFlag.add(clazz.getName());
        ServiceLoader<?> sl = ServiceLoader.load(clazz);
        Iterator itr = sl.iterator();
        while (itr.hasNext()) {
            Object obj = itr.next();
            ProductAnnotation product = obj.getClass().getAnnotation(ProductAnnotation.class);
            if (product == null) {
                //忽略这个配置
                continue;
            }
            ProductDialect dialect = new ProductDialect(product.productName(), clazz);
            if ("".equals(product.productName())) {
                //默认实现的版本
                defaultInstances.put(dialect.clazz, obj);
                continue;
            } else {
                ProductVersion version = new ProductVersion(product.majorVersion(), product.minorVersion());

                Map<ProductVersion, Object> versions = dialectInstances.get(dialect);
                if (versions == null) {
                    versions = new TreeMap<ProductVersion, Object>();
                    dialectInstances.put(dialect, versions);
                }

                versions.put(version, obj);
            }

            if (obj instanceof DBInitialize) {
                ((DBInitialize) obj).initialize(conn);
            }
        }
    }

    private static class ProductDialect {

        public String productName;
        public Class<?> clazz;

        public ProductDialect(String productName, Class<?> clazz) {
            this.productName = productName;
            this.clazz = clazz;
        }

        public ProductDialect(Connection conn, Class<?> clazz) {
            try {
                this.productName = conn.getMetaData().getDatabaseProductName();
                if (this.productName != null) {
                    //"DB2/LINUXX8664" => "DB2"
                    this.productName = this.productName.split("/")[0];
                }

            } catch (SQLException ex) {
            }
            this.clazz = clazz;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof ProductDialect)) {
                return false;
            }
            ProductDialect product = (ProductDialect) obj;

            return (product.productName.equalsIgnoreCase(productName)
                    && product.clazz.equals(clazz));
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 29 * hash + (this.productName != null ? this.productName.hashCode() : 0);
            hash = 29 * hash + (this.clazz != null ? this.clazz.hashCode() : 0);
            return hash;
        }
    }

    private static class ProductVersion implements Comparable<ProductVersion> {

        public int majorVersion;
        public int minorVersion;

        public ProductVersion(int majorVersion, int minorVersion) {
            this.majorVersion = majorVersion;
            this.minorVersion = minorVersion;
        }

        public ProductVersion(Connection conn, Class<?> clazz) {
            try {
                this.majorVersion = conn.getMetaData().getDatabaseMajorVersion();
                this.minorVersion = conn.getMetaData().getDatabaseMinorVersion();
            } catch (SQLException ex) {
                this.majorVersion = 0;
                this.minorVersion = 0;
            }

        }

        public int compareTo(ProductVersion o) {
            //最新版本排序排在最前
            if (majorVersion > o.majorVersion) {
                return -1;
            }
            if (majorVersion < o.majorVersion) {
                return 1;
            }
            if (minorVersion > o.minorVersion) {
                return -1;
            }
            if (minorVersion < o.minorVersion) {
                return 1;
            }
            return 0;
        }
    }
}
