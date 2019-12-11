package mark.component.dbdialect.util;

import mark.component.dbmodel.model.JavaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author liang
 * 将类型分为六类：数值、日期、字符串、二进制串、字符流、字节流
 */
public class TypeResolver implements JavaType {

    private final Map<String, Integer> jdbcTypes;
    private final Map<Integer, int[]> jdbc2javaTypes;
    private final Map<Integer, int[]> java2jdbcTypes;

    public TypeResolver(final String datatypeFilename, final String java2jdbcFilename, final String jdbc2javaFilename) throws IOException {
        this.jdbcTypes = new HashMap<String, Integer>();
        this.jdbc2javaTypes = new HashMap<Integer, int[]>();
        this.java2jdbcTypes = new HashMap<Integer, int[]>();
        initTypes(datatypeFilename, java2jdbcFilename, jdbc2javaFilename);
    }

    public int getJdbcType(int javaType) {
        return java2jdbcTypes.get(javaType)[0];
    }

    public int[] getCandidateJdbcTypes(int javaType) {
        return java2jdbcTypes.get(javaType);
    }

    public int getJavaType(int jdbcType) {
        return jdbc2javaTypes.get(jdbcType)[0];
    }

    public int[] getCandidateJavaTypes(int jdbcType) {
        return jdbc2javaTypes.get(jdbcType);
    }

    @Override
    public String getJdbcTypeName(int jdbcType) {
        return TypeUtil.getJdbcTypeName(jdbcType);
    }

    @Override
    public int getJdbcType(String jdbcTypeName) {
        return TypeUtil.getJdbcType(jdbcTypeName);
    }

    @Override
    public String getJavaTypeName(int javaType) {
        return TypeUtil.getJavaTypeName(javaType);
    }

    @Override
    public int getJavaType(String javaTypeName) {
        return TypeUtil.getJavaType(javaTypeName);
    }

    private void initTypes(String datatypeFilename, String java2jdbcFilename, String jdbc2javaFilename) throws IOException {

        Properties properties = new Properties();
        /*
         * jdbc类型
         */
        properties.load(TypeResolver.class.getResourceAsStream("/types/" + datatypeFilename));
        for (Map.Entry entry : properties.entrySet()) {
            String jdbcTypeName = entry.getKey().toString();
            Integer jdbcType = Integer.valueOf(entry.getValue().toString());
            jdbcTypes.put(jdbcTypeName, jdbcType);
        }

        properties.clear();
        /*
         * jdbc到java类型映射
         */
        properties.load(TypeResolver.class.getResourceAsStream("/types/" + jdbc2javaFilename));
        for (Map.Entry entry : properties.entrySet()) {
            String jdbcTypeName = entry.getKey().toString();
            String javaTypeName = entry.getValue().toString();
            //System.out.println(jdbcTypeName + "=" + javaTypeName);
            Integer jdbcType = Integer.valueOf(jdbcTypes.get(jdbcTypeName));
            String[] javaTypenames = javaTypeName.split(",");

            int[] javaTypes = new int[javaTypenames.length];
            for (int i = 0; i < javaTypenames.length; i++) {
                javaTypes[i] = Integer.valueOf(getJavaType(javaTypenames[i]));
            }
            jdbc2javaTypes.put(jdbcType, javaTypes);
        }

        properties.clear();
        /*
         * java到jdbc类型映射
         */
        //System.out.println("java2jdbcFilename = " + java2jdbcFilename);
        properties.load(TypeResolver.class.getResourceAsStream("/types/" + java2jdbcFilename));
        for (Map.Entry entry : properties.entrySet()) {
            String javaTypeName = entry.getKey().toString();
            String jdbcTypeName = entry.getValue().toString();
            Integer javaType = Integer.valueOf(getJavaType(javaTypeName));
            String[] jdbcTypenames = jdbcTypeName.split(",");
            //System.out.println(javaTypeName + "=" + jdbcTypeName);
            int[] jdbcTypes = new int[jdbcTypenames.length];
            for (int i = 0; i < jdbcTypenames.length; i++) {
                jdbcTypes[i] = Integer.valueOf(this.jdbcTypes.get(jdbcTypenames[i]));
            }
            java2jdbcTypes.put(javaType, jdbcTypes);
        }
        properties.clear();

    }
}
