package mark.moduledatasource.model;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class SourcePro implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;// 属性唯一标记
    private String name;// 属性名称
    private int type = 1;// 界面组件类型，1：文本框组件，2：密码组件，3：文件组件，4：单选组件，5：多选组件，6：多行文件组件
    private boolean required;// 是否必填
    private boolean allowModify = true;// 是否允许修改
    private String format;// 文本框组件：正则表达式，文件组件：允许的后缀（逗号分隔）
    private String sampleValue;// 样例说明
    private String defaultValue;// 默认值
    // 下拉选项
    // 如果是二级选项，就会有上级选项
    private String parentSelect;// 上级选项
    private String displayValue;// 当上级选项的值等于该值，当前组件显示，如果未配置表示一直显示，多个值用逗号分隔
    // 如果是二级选项，配置的Key=父Key_自己的Key
    private LinkedHashMap<String, String> selectValue;

    private boolean custom;// 是否自定义属性，不允许在XML在配置

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isAllowModify() {
        return allowModify;
    }

    public void setAllowModify(boolean allowModify) {
        this.allowModify = allowModify;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSampleValue() {
        return sampleValue;
    }

    public void setSampleValue(String sampleValue) {
        this.sampleValue = sampleValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getParentSelect() {
        return parentSelect;
    }

    public void setParentSelect(String parentSelect) {
        this.parentSelect = parentSelect;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public LinkedHashMap<String, String> getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(LinkedHashMap<String, String> selectValue) {
        this.selectValue = selectValue;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public boolean isFile() {
        return type == 3;
    }
}
