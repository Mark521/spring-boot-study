package mark.component.dbmodel.model;

/**
 *
 * @author liang
 * 将类型分为六类：数值、日期、字符串、二进制串、字符流、字节流
 */
public interface JavaType {

    public static final int BOOLEAN = 1;
    public static final int BYTE = 2;
    public static final int SHORT = 3;
    public static final int INTEGER = 4;
    public static final int LONG = 5;
    public static final int FLOAT = 6;  //MySQL的REAL和FLOAT和这里的概念相反
    public static final int DOUBLE = 7;
    public static final int BIGDECIMAL = 8;
    public static final int TIME = 9;
    public static final int DATE = 10;
    public static final int TIMESTAMP = 11;
    public static final int STRING = 12;
    public static final int BYTES = 13;
    public static final int CHARACTER_STREAM = 14;//Reader
    public static final int BINARY_STREAM = 15;//InputStream
    public static final int OBJECT = 16;
    /*
     * 类型分组
     */
    public static final int NUMERIC = 100;
    public static final int DATETIME = 101;
    //shared with STRING = 12
    //public static final int STRING = 12;
    public static final int BINARY = 102;
    public static final int CLOB = 103;
    public static final int BLOB = 104;
    //shared with OBJECT = 16
    //public static final int OBJECT = 16;
    public static final int ANY = 105;

    public int getJdbcType(int javaType);

    public int[] getCandidateJdbcTypes(int javaType);

    public int getJavaType(int jdbcType);

    public int[] getCandidateJavaTypes(int jdbcType);

    public String getJdbcTypeName(int jdbcType);

    public String getJavaTypeName(int javaType);

    public int getJdbcType(String jdbcTypeName);

    public int getJavaType(String javaTypeName);
}
