package mark.component.dbmodel.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

public class Column extends AbstractColumn<Table> {

    private static final long serialVersionUID = 3834591019449528633L;
    private String defaultValue;
    private boolean isPartOfPrimaryKey;
    private boolean isPartOfUniqueIndex;
    @JsonBackReference
    private Column referencedColumn;
    @JsonManagedReference
    private final NamedObjectList<Privilege<Column>> privileges = new NamedObjectList<Privilege<Column>>();
    private String columnFamily;

    public Column(final Table parent, final String name) {
        super(parent, name);
    }
    public Column(){

    }
    @JSONField(serialize = false)
    public Collection<ForeignKeyColumnReference> getForeignKeyColumnReferences() {
        if(this.getParent()!=null) {
            return this.getParent().getForeignKeyReferencess(this);
        }else
        {
            return null;
        }
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public Privilege<Column> getPrivilege(final String name) {
        return privileges.lookup(this, name);
    }

    public Collection<Privilege<Column>> getPrivileges() {
        return new ArrayList<Privilege<Column>>(privileges.values());
    }
    @JSONField(serialize = false)
    public Column getReferencedColumn() {
        return referencedColumn;
    }
    public void setReferencedColumnJson(String referencedColumnJson){
        if(StringUtils.isNotBlank(referencedColumnJson)&&!"{}".equals(referencedColumnJson)){
            Column referencedColumnObj= JSONObject.parseObject(referencedColumnJson,Column.class);
            this.setReferencedColumn(referencedColumnObj);
        }
    }
    public boolean isPartOfForeignKey() {
        return referencedColumn != null;
    }

    public boolean isPartOfPrimaryKey() {
        return isPartOfPrimaryKey;
    }

    public boolean isPartOfUniqueIndex() {
        return isPartOfUniqueIndex;
    }

    public void addPrivilege(final Privilege<Column> privilege) {
        privileges.add(privilege);
    }

    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setPartOfPrimaryKey(final boolean partOfPrimaryKey) {
        isPartOfPrimaryKey = partOfPrimaryKey;
    }

    public void setPartOfUniqueIndex(final boolean partOfUniqueIndex) {
        isPartOfUniqueIndex = partOfUniqueIndex;
    }

    public void setReferencedColumn(final Column referencedColumn) {
        this.referencedColumn = referencedColumn;
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public void setColumnFamily(String columnFamily) {
        this.columnFamily = columnFamily;
    }
}
