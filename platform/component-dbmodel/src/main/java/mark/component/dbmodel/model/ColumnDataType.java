package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.SearchableType;

import java.util.*;

/**
 * 数据库列类型模型, 描述列类型信息
 * @author liang>
 */
public final class ColumnDataType extends AbstractDatabaseObject {

    private static final long serialVersionUID = 3688503281676530744L;
    // Fields related to the java.sql.Types type
    private int jdbcType; //int jdbcType
    // Other fields
    private boolean userDefined;
    private int precision;
    private String literalPrefix;
    private String literalSuffix;
    private String createParameters;
    private boolean nullable;
    private boolean caseSensitive;
    private SearchableType searchable;
    private boolean unsigned;
    private boolean fixedPrecisionScale;
    private boolean autoIncrementable;
    private String localizedTypeName;
    private int minimumScale;
    private int maximumScale;
    private int numPrecisionRadix; // usually 2 or 10
    private String className;
    private ColumnDataType baseType;
    //扩展，用于生成列的SQL信息
    private String[] expression;//createParameters属性无法表达类型的参数，因为各数据库实现标准不一致
    public static final ColumnDataType unknownType = new ColumnDataType(null, null);

    public ColumnDataType(final Schema schema, final String name) {
        super(schema, name);
        searchable = SearchableType.unknown;
        createParameters = "";
    }
    public ColumnDataType(){

    }
    public ColumnDataType getBaseType() {
        return baseType;
    }

    public String getCreateParameters() {
        return createParameters;
    }

    public String getDatabaseSpecificTypeName() {
        return getName();
    }

    public String getLiteralPrefix() {
        return literalPrefix;
    }

    public String getLiteralSuffix() {
        return literalSuffix;
    }

    public String getLocalTypeName() {
        return localizedTypeName;
    }

    public int getMaximumScale() {
        return maximumScale;
    }

    public int getMinimumScale() {
        return minimumScale;
    }

    public int getNumPrecisionRadix() {
        return numPrecisionRadix;
    }

    public int getPrecision() {
        return precision;
    }

    public SearchableType getSearchable() {
        return searchable;
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public boolean isAutoIncrementable() {
        return autoIncrementable;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public boolean isFixedPrecisionScale() {
        return fixedPrecisionScale;
    }

    public boolean isNullable() {
        return nullable;
    }

    public boolean isUnsigned() {
        return unsigned;
    }

    public boolean isUserDefined() {
        return userDefined;
    }

    public void setAutoIncrementable(final boolean autoIncrementable) {
        this.autoIncrementable = autoIncrementable;
    }

    public void setBaseType(final ColumnDataType baseType) {
        this.baseType = baseType;
    }

    public void setCaseSensitive(final boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public void setCreateParameters(final String createParams) {
        createParameters = createParams;
    }

    public void setFixedPrecisionScale(final boolean fixedPrecisionScale) {
        this.fixedPrecisionScale = fixedPrecisionScale;
    }

    public void setLiteralPrefix(final String literalPrefix) {
        this.literalPrefix = literalPrefix;
    }

    public void setLiteralSuffix(final String literalSuffix) {
        this.literalSuffix = literalSuffix;
    }

    public void setLocalTypeName(final String localTypeName) {
        localizedTypeName = localTypeName;
    }

    public void setMaximumScale(final int maximumScale) {
        this.maximumScale = maximumScale;
    }

    public void setMinimumScale(final int minimumScale) {
        this.minimumScale = minimumScale;
    }

    public void setNullable(final boolean nullable) {
        this.nullable = nullable;
    }

    public void setNumPrecisionRadix(final int numPrecisionRadix) {
        this.numPrecisionRadix = numPrecisionRadix;
    }

    public void setPrecision(final int precision) {
        this.precision = precision;
    }

    public void setSearchable(final SearchableType searchable) {
        this.searchable = searchable;
    }

    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public void setUnsigned(final boolean unsignedAttribute) {
        unsigned = unsignedAttribute;
    }

    public void setUserDefined(final boolean userDefined) {
        this.userDefined = userDefined;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the expression
     */
    public String[] getExpression() {
        return expression;
    }

    /**
     * @param expression the expression to set
     */
    public void setExpression(String[] expression) {
        this.expression = expression;
    }
    private static final String lengthStr = "${l}";
    private static final String precisionStr = "${p}";
    private static final String scaleStr = "${s}";
    private static final String starStr = "${*}";
    private static final String[] allParams = new String[]{lengthStr, precisionStr, scaleStr, starStr};

    public String getTypeSql(Long length, Integer precision, Integer scale, String... params) {
        //收集所有参数
        Map<String, Object> maps = new HashMap<String, Object>();
        if (length != null) {
            maps.put(lengthStr, length);
        }
        if (precision != null) {
            maps.put(precisionStr, precision);
        }
        if (scale != null) {
            maps.put(scaleStr, scale);
        }

        if (params != null && params.length != 0) {
            maps.put(starStr, "");
            int i = 1;
            for (String s : params) {
                if (s != null) {
                    maps.put("${" + i++ + "}", s);
                }
            }
        }
        //收集所有表达式
        List<String> expressionList = new LinkedList<String>();
        if (getExpression() != null) {
            expressionList.addAll(Arrays.asList(getExpression()));
        }
        expressionList.add("");


        //查找最合适的表达式
        String sql = null;
        for (String exp : expressionList) {
            boolean flag = true;
            for (String p : allParams) {
                if (exp.contains(p) && !maps.containsKey(p)) {
                    flag = false;
                    break;
                }
            }
            if (!flag) {
                continue;
            }
            int i = 1;
            while (true) {
                String p = "${" + i++ + "}";
                if (exp.contains(p)) {
                    if (!maps.containsKey(p)) {
                        flag = false;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (flag) {
                sql = exp;
                break;
            }
        }
        if (sql == null) {
            return null;
        }

        maps.remove(starStr);
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            sql = sql.replace(entry.getKey(), entry.getValue().toString());
        }
        if (sql.contains(starStr)) {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                str.append(getLiteralPrefix()).append(params[i]).append(getLiteralSuffix());
                if (i != params.length - 1) {
                    str.append(",");
                }
            }
            sql = sql.replace(starStr, str.toString());
        }

        return getName() + sql;
    }
}
