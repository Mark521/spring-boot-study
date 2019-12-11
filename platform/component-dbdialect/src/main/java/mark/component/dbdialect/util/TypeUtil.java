package mark.component.dbdialect.util;

import mark.component.dbmodel.model.Column;

import java.sql.Types;

import static mark.component.dbmodel.model.JavaType.*;


/**
 *
 * @author liang
 * 将类型分为六类：数值、日期、字符串、二进制串、字符流、字节流
 */
public abstract class TypeUtil {

    public static boolean isNumeric(int javaType) {
        return BOOLEAN <= javaType && javaType <= BIGDECIMAL;
    }

    public static boolean isDate(int javaType) {
        return DATE <= javaType && javaType <= TIMESTAMP;
    }

    public static boolean isString(int javaType) {
        return javaType == STRING;
    }

    public static boolean isBinary(int javaType) {
        return javaType == BYTES;
    }

    public static boolean isCharacterStream(int javaType) {
        return javaType == CHARACTER_STREAM;
    }

    public static boolean isBinaryStream(int javaType) {
        return javaType == BINARY_STREAM;
    }

    public static int is(int javaType) {
        switch (javaType) {
            case BOOLEAN:
            case BYTE:
            case SHORT:
            case INTEGER:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case BIGDECIMAL:
                return NUMERIC;
            case TIME:
            case DATE:
            case TIMESTAMP:
                return DATETIME;
            case STRING:
                return STRING;
            case BYTES:
                return BINARY;
            case CHARACTER_STREAM:
                return CLOB;
            case BINARY_STREAM:
                return BLOB;
        }
        return OBJECT;
    }

    public static int getJdbcType(int javaType) {
        switch (javaType) {
            case BOOLEAN:
                return Types.BIT;
            case BYTE:
                return Types.TINYINT;
            case SHORT:
                return Types.SMALLINT;
            case INTEGER:
                return Types.INTEGER;
            case LONG:
                return Types.BIGINT;
            case FLOAT:
                return Types.REAL;
            case DOUBLE:
                return Types.FLOAT;
            case BIGDECIMAL:
                return Types.DECIMAL;
            case TIME:
                return Types.TIME;
            case DATE:
                return Types.DATE;
            case TIMESTAMP:
                return Types.TIMESTAMP;
            case STRING:
                return Types.VARCHAR;
            case BYTES:
                return Types.VARBINARY;
            case CHARACTER_STREAM:
                return Types.CLOB;
            case BINARY_STREAM:
                return Types.BLOB;
        }
        return Types.OTHER;
    }
    /*
     * 备选类型映射
     */
    private final static int[] _booleans = new int[]{Types.BIT, Types.INTEGER, Types.CHAR, Types.VARCHAR};
    private final static int[] _bytes = new int[]{Types.TINYINT, Types.INTEGER, Types.CHAR, Types.VARCHAR};
    private final static int[] _shorts = new int[]{Types.SMALLINT, Types.INTEGER, Types.CHAR, Types.VARCHAR};
    private final static int[] _integers = new int[]{Types.INTEGER, Types.BIGINT, Types.CHAR, Types.VARCHAR};
    private final static int[] _longs = new int[]{Types.BIGINT, Types.CHAR, Types.VARCHAR};
    private final static int[] _floats = new int[]{Types.REAL, Types.DOUBLE, Types.NUMERIC, Types.DECIMAL, Types.CHAR, Types.VARCHAR};
    private final static int[] _doubles = new int[]{Types.FLOAT, Types.DOUBLE, Types.NUMERIC, Types.DECIMAL, Types.CHAR, Types.VARCHAR};
    private final static int[] _bigdecimals = new int[]{Types.NUMERIC, Types.DECIMAL, Types.CHAR, Types.VARCHAR};
    private final static int[] _times = new int[]{Types.TIME, Types.CHAR, Types.VARCHAR};
    private final static int[] _dates = new int[]{Types.DATE, Types.CHAR, Types.VARCHAR};
    private final static int[] _timestamps = new int[]{Types.TIMESTAMP, Types.CHAR, Types.VARCHAR};
    private final static int[] _strings = new int[]{Types.CHAR, Types.VARCHAR};
    private final static int[] _bytess = new int[]{Types.VARBINARY, Types.BINARY};
    private final static int[] _cstreams = new int[]{Types.CLOB, Types.NCLOB, Types.VARCHAR, Types.CHAR};
    private final static int[] _bstreams = new int[]{Types.BLOB, Types.VARBINARY, Types.BINARY};
    private final static int[] _others = new int[]{Types.OTHER};

    public static int[] getCandidateJdbcTypes(int javaType) {
        switch (javaType) {
            case BOOLEAN:
                return _booleans;
            case BYTE:
                return _bytes;
            case SHORT:
                return _shorts;
            case INTEGER:
                return _integers;
            case LONG:
                return _longs;
            case FLOAT:
                return _floats;
            case DOUBLE:
                return _doubles;
            case BIGDECIMAL:
                return _bigdecimals;
            case TIME:
                return _times;
            case DATE:
                return _dates;
            case TIMESTAMP:
                return _timestamps;
            case STRING:
                return _strings;
            case BYTES:
                return _bytess;
            case CHARACTER_STREAM:
                return _cstreams;
            case BINARY_STREAM:
                return _bstreams;
        }
        return _others;
    }

    public static int getJavaType(int jdbcType) {
        switch (jdbcType) {
            case Types.BIT:
            case Types.BOOLEAN:
                return BOOLEAN;
            case Types.TINYINT:
                return BYTE;
            case Types.SMALLINT:
                return SHORT;
            case Types.INTEGER:
                return INTEGER;
            case Types.BIGINT:
                return LONG;
            case Types.REAL:
                return FLOAT;
            case Types.FLOAT:
            case Types.DOUBLE:
                return DOUBLE;
            case Types.NUMERIC:
            case Types.DECIMAL:
                return BIGDECIMAL;
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                return STRING;
            case Types.TIME:
                return TIME;
            case Types.DATE:
                return DATE;
            case Types.TIMESTAMP:
                return TIMESTAMP;
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                return BYTES;
            case Types.CLOB:
            case Types.NCLOB:
                return CHARACTER_STREAM;
            case Types.BLOB:
                return BINARY_STREAM;
        }

        return OBJECT;
    }
    /*
     * 备选类型映射
     */
    private final static int[] booleans_ = new int[]{BOOLEAN, INTEGER, STRING};
    private final static int[] bytes_ = new int[]{BYTE, INTEGER, STRING};
    private final static int[] shorts_ = new int[]{SHORT, INTEGER, STRING};
    private final static int[] integers_ = new int[]{INTEGER, LONG, STRING};
    private final static int[] longs_ = new int[]{LONG, BIGDECIMAL, STRING};
    private final static int[] floats_ = new int[]{FLOAT, DOUBLE, BIGDECIMAL, STRING};
    private final static int[] doubles_ = new int[]{DOUBLE, BIGDECIMAL, STRING};
    private final static int[] bigdecimals_ = new int[]{BIGDECIMAL, STRING};
    private final static int[] times_ = new int[]{TIME, STRING};
    private final static int[] dates_ = new int[]{DATE, STRING};
    private final static int[] timestamps_ = new int[]{TIMESTAMP, STRING};
    private final static int[] strings_ = new int[]{STRING};
    private final static int[] bytess_ = new int[]{BYTES};
    private final static int[] cstreams_ = new int[]{CHARACTER_STREAM, STRING};
    private final static int[] bstreams_ = new int[]{BINARY_STREAM};
    private final static int[] others_ = new int[]{OBJECT};

    public static int[] getCandidateJavaTypes(int jdbcType) {

        switch (jdbcType) {
            case Types.BIT:
            case Types.BOOLEAN:
                return booleans_;
            case Types.TINYINT:
                return bytes_;
            case Types.SMALLINT:
                return shorts_;
            case Types.INTEGER:
                return integers_;
            case Types.BIGINT:
                return longs_;
            case Types.REAL:
                return floats_;
            case Types.FLOAT:
            case Types.DOUBLE:
                return doubles_;
            case Types.NUMERIC:
            case Types.DECIMAL:
                return bigdecimals_;
            case Types.TIME:
                return times_;
            case Types.DATE:
                return dates_;
            case Types.TIMESTAMP:
                return timestamps_;
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                return strings_;
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                return bytess_;
            case Types.CLOB:
            case Types.NCLOB:
                return cstreams_;
            case Types.BLOB:
                return bstreams_;
        }
        return others_;
    }

    public static String getJdbcTypeName(int jdbcType) {
        switch (jdbcType) {
            case Types.BIT:
                return "BIT";
            case Types.BOOLEAN:
                return "BOOLEAN";
            case Types.TINYINT:
                return "TINYINT";
            case Types.SMALLINT:
                return "SMALLINT";
            case Types.INTEGER:
                return "INTEGER";
            case Types.BIGINT:
                return "BIGINT";
            case Types.REAL:
                return "REAL";
            case Types.FLOAT:
                return "FLOAT";
            case Types.DOUBLE:
                return "DOUBLE";
            case Types.NUMERIC:
                return "NUMERIC";
            case Types.DECIMAL:
                return "DECIMAL";
            case Types.CHAR:
                return "CHAR";
            case Types.VARCHAR:
                return "VARCHAR";
            case Types.LONGVARCHAR:
                return "LONGVARCHAR";
            case Types.NCHAR:
                return "NCHAR";
            case Types.NVARCHAR:
                return "NVARCHAR";
            case Types.LONGNVARCHAR:
                return "LONGNVARCHAR";
            case Types.TIME:
                return "TIME";
            case Types.DATE:
                return "DATE";
            case Types.TIMESTAMP:
                return "TIMESTAMP";
            case Types.BINARY:
                return "BINARY";
            case Types.VARBINARY:
                return "VARBINARY";
            case Types.LONGVARBINARY:
                return "LONGVARBINARY";
            case Types.CLOB:
                return "CLOB";
            case Types.NCLOB:
                return "NCLOB";
            case Types.BLOB:
                return "BLOB";
        }
        return "OTHER";
    }

    public static String getJavaTypeName(int javaType) {
        switch (javaType) {
            case BOOLEAN:
                return "BOOLEAN";
            case BYTE:
                return "BYTE";
            case SHORT:
                return "SHORT";
            case INTEGER:
                return "INTEGER";
            case LONG:
                return "LONG";
            case FLOAT:
                return "FLOAT";
            case DOUBLE:
                return "DOUBLE";
            case BIGDECIMAL:
                return "BIGDECIMAL";
            case TIME:
                return "TIME";
            case DATE:
                return "DATE";
            case TIMESTAMP:
                return "TIMESTAMP";
            case STRING:
                return "STRING";
            case BYTES:
                return "BYTES";
            case CHARACTER_STREAM:
                return "CHARACTER_STREAM";
            case BINARY_STREAM:
                return "BINARY_STREAM";
        }
        return "OBJECT";
    }

    public static int getJdbcType(String jdbcTypeName) {
        if (jdbcTypeName.equalsIgnoreCase("BOOLEAN")) {
            return BOOLEAN;
        } else if (jdbcTypeName.equalsIgnoreCase("BYTE")) {
            return BYTE;
        } else if (jdbcTypeName.equalsIgnoreCase("SHORT")) {
            return SHORT;
        } else if (jdbcTypeName.equalsIgnoreCase("INTEGER")) {
            return INTEGER;
        } else if (jdbcTypeName.equalsIgnoreCase("LONG")) {
            return LONG;
        } else if (jdbcTypeName.equalsIgnoreCase("FLOAT")) {
            return FLOAT;
        } else if (jdbcTypeName.equalsIgnoreCase("DOUBLE")) {
            return DOUBLE;
        } else if (jdbcTypeName.equalsIgnoreCase("BIG DECIMAL")) {
            return BIGDECIMAL;
        } else if (jdbcTypeName.equalsIgnoreCase("TIME")) {
            return TIME;
        } else if (jdbcTypeName.equalsIgnoreCase("DATE")) {
            return DATE;
        } else if (jdbcTypeName.equalsIgnoreCase("TIMESTAMP")) {
            return TIMESTAMP;
        } else if (jdbcTypeName.equalsIgnoreCase("STRING")) {
            return STRING;
        } else if (jdbcTypeName.equalsIgnoreCase("CHARACTER STREAM")) {
            return CHARACTER_STREAM;
        } else if (jdbcTypeName.equalsIgnoreCase("BINARY STREAM")) {
            return BINARY_STREAM;
        } else {
            return OBJECT;
        }
    }

    public static int getJavaType(String javaTypeName) {
        if (javaTypeName.equalsIgnoreCase("BOOLEAN")) {
            return BOOLEAN;
        } else if (javaTypeName.equalsIgnoreCase("BYTE")) {
            return BYTE;
        } else if (javaTypeName.equalsIgnoreCase("SHORT")) {
            return SHORT;
        } else if (javaTypeName.equalsIgnoreCase("INTEGER")) {
            return INTEGER;
        } else if (javaTypeName.equalsIgnoreCase("LONG")) {
            return LONG;
        } else if (javaTypeName.equalsIgnoreCase("FLOAT")) {
            return FLOAT;
        } else if (javaTypeName.equalsIgnoreCase("DOUBLE")) {
            return DOUBLE;
        } else if (javaTypeName.equalsIgnoreCase("BIGDECIMAL")) {
            return BIGDECIMAL;
        } else if (javaTypeName.equalsIgnoreCase("TIME")) {
            return TIME;
        } else if (javaTypeName.equalsIgnoreCase("DATE")) {
            return DATE;
        } else if (javaTypeName.equalsIgnoreCase("TIMESTAMP")) {
            return TIMESTAMP;
        } else if (javaTypeName.equalsIgnoreCase("STRING")) {
            return STRING;
        } else if (javaTypeName.equalsIgnoreCase("BYTES")) {
            return BYTES;
        } else if (javaTypeName.equalsIgnoreCase("CHARACTER_STREAM")) {
            return CHARACTER_STREAM;
        } else if (javaTypeName.equalsIgnoreCase("BINARY_STREAM")) {
            return BINARY_STREAM;
        } else {
            return OBJECT;
        }
    }

    public static String getColumnTypeDefinition(Column column) {
        StringBuilder sb = new StringBuilder();
        sb.append(column.getName());
        if (column.getPrecision() == 0) {
            return sb.toString();
        }
        sb.append(" ").append(column.getType());
        sb.append("(").append(column.getPrecision());
        if (column.getScale() != 0) {
            sb.append(", ").append(column.getScale());
        }
        sb.append(")");

        return sb.toString();
    }
}
