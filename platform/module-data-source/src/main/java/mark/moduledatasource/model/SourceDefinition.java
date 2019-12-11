package mark.moduledatasource.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SourceDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    private int type;// 数据源分类，1：关系型数据库，2：大数据，3：文件服务器
    private String key;// 数据源唯一标示
    private String name;// 名称
    private String remark;// 备注
    private List<SourcePro> properties;
    // 有些属性是不需要输入的，它是固定值，比如驱动类名
    // 有些属性是不需要输入的，它是通过其他属性组合而来，比如url
    // 使用{}包装上面的变量，用于生成属性值，Key为属性名，Value为表达式
    private Map<String, String> expression;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SourcePro> getProperties() {
        return properties;
    }

    public void setProperties(List<SourcePro> properties) {
        this.properties = properties;
    }

    public Map<String, String> getExpression() {
        return expression;
    }

    public void setExpression(Map<String, String> expression) {
        this.expression = expression;
    }

}
