package mark.component.dbmodel.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import mark.component.dbmodel.util.Utility;
import mark.component.dbmodel.constans.NamedObject;
import mark.component.dbmodel.util.NamedObjectSort;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public abstract class AbstractNamedObject implements NamedObject {

    private int id;
    private String name;
    private String remarks;
    private String remarkExt;
    private final Map<String, Object> attributeMap = new HashMap<>();

    public AbstractNamedObject(final String name){
        this.name = name;
    }
    public AbstractNamedObject(){}

    @Override
    public int compareTo(final NamedObject obj){
        if(obj == null){
            return -1;
        }
        return NamedObjectSort.alphabetical.compare(this,obj);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AbstractNamedObject)) {
            return false;
        }
        final AbstractNamedObject other = (AbstractNamedObject) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public final Object getAttribute(final String name) {
        return attributeMap.get(name);
    }

    @Override
    public final <T> T getAttribute(final String name, final T defaultValue) {
        final Object attributeValue = getAttribute(name);
        if (attributeValue == null) {
            return defaultValue;
        } else {
            try {
                return (T) attributeValue;
            } catch (final ClassCastException e) {
                return defaultValue;
            }
        }
    }


    @Override
    public final Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributeMap);
    }

    public void setAttributesJson(String attributes) {
        if(StringUtils.isNotBlank(attributes)&&!"{}".equals(attributes)){
            this.addAttributes(JSONObject.parseObject(attributes,Map.class));
        }
    }
    public String getAttributesJson(){
        return JSONObject.toJSONString(getAttributes(), SerializerFeature.DisableCircularReferenceDetect);
    }

    public String getRemarkExt() {
        return remarkExt;
    }

    public void setRemarkExt(String remarkExt) {
        this.remarkExt = remarkExt;
    }

    @Override
    public String getFullName() {
        return name;
    }


    @Override
    public String getLookupKey() {
        return getFullName();
    }

    @Override
    public final String getName() {
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    @Override
    public final String getRemarks() {
        return remarks;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.hashCode());

        return result;
    }

    @Override
    public final void setAttribute(final String name, final Object value) {
        if (!Utility.isBlank(name)) {
            if (value == null) {
                attributeMap.remove(name);
            } else {
                attributeMap.put(name, value);
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public final void addAttributes(final Map<String, Object> values) {
        if (values != null) {
            attributeMap.putAll(values);
        }
    }

    public final void setRemarks(final String remarks) {
        if (remarks == null) {
            this.remarks = "";
        } else {
            this.remarks = remarks;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
