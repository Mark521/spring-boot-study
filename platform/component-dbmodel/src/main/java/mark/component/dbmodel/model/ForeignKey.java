package mark.component.dbmodel.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import mark.component.dbmodel.constans.ForeignKeyDeferrability;
import mark.component.dbmodel.constans.ForeignKeyUpdateRule;
import mark.component.dbmodel.util.CompareUtility;
import mark.component.dbmodel.constans.NamedObject;
import org.apache.commons.lang3.StringUtils;

import java.util.*;


public class ForeignKey extends AbstractDependantObject<Table> {

    private static final long serialVersionUID = 4121411795974895671L;
    @JsonManagedReference
    private final transient SortedSet<ForeignKeyColumnReference> columnReferences = new TreeSet<ForeignKeyColumnReference>();
    private ForeignKeyUpdateRule updateRule;
    private ForeignKeyUpdateRule deleteRule;
    private ForeignKeyDeferrability deferrability;
    private String primaryKeyColumn;
    private String foreignKeyColumn;
    private String referenceSchema;
    private String referenceTable;
    public ForeignKey(final Table parent,final String name) {
        super(parent, name);
        // Default values
        updateRule = ForeignKeyUpdateRule.unknown;
        deleteRule = ForeignKeyUpdateRule.unknown;
        deferrability = ForeignKeyDeferrability.unknown;
    }
    public ForeignKey(){
        // Default values
        updateRule = ForeignKeyUpdateRule.unknown;
        deleteRule = ForeignKeyUpdateRule.unknown;
        deferrability = ForeignKeyDeferrability.unknown;
    }
    @Override
    public int compareTo(final NamedObject obj) {
        if (obj == null) {
            return -1;
        }

        final ForeignKey other = (ForeignKey) obj;
        final List<ForeignKeyColumnReference> thisColumnReferences = getColumnReferences();
        final List<ForeignKeyColumnReference> otherColumnReferences = other.getColumnReferences();

        return CompareUtility.compareLists(thisColumnReferences, otherColumnReferences);
    }
    @JsonIgnore
    public List<ForeignKeyColumnReference> getColumnReferences() {
        return new ArrayList<ForeignKeyColumnReference>(columnReferences);
    }
    public  void setColumnReferences(List<ForeignKeyColumnReference> references) {

        columnReferences.addAll(references);
    }

    public final ForeignKeyDeferrability getDeferrability() {
        return deferrability;
    }

    public final ForeignKeyUpdateRule getDeleteRule() {
        return deleteRule;
    }

    public final ForeignKeyUpdateRule getUpdateRule() {
        return updateRule;
    }

    public void addColumnReference(final int keySequence,
            final Column pkColumn,
            final Column fkColumn) {
        final ForeignKeyColumnReference fkColumnReference = new ForeignKeyColumnReference();
        fkColumnReference.setKeySequence(keySequence);
        fkColumnReference.setPrimaryKeyColumn(pkColumn);
        fkColumnReference.setForeignKeyColumn(fkColumn);
        columnReferences.add(fkColumnReference);
    }

    public final void setDeferrability(final ForeignKeyDeferrability deferrability) {
        this.deferrability = deferrability;
    }

    public final void setDeleteRule(final ForeignKeyUpdateRule deleteRule) {
        this.deleteRule = deleteRule;
    }

    public final void setUpdateRule(final ForeignKeyUpdateRule updateRule) {
        this.updateRule = updateRule;
    }

    public String getForeignKeyColumn() {
        if(StringUtils.isBlank(foreignKeyColumn)) {
            List<String> foreignkeyColumns = new ArrayList<>();
            for (Iterator<ForeignKeyColumnReference> iterator = this.getColumnReferences().iterator(); iterator.hasNext(); ) {
                ForeignKeyColumnReference fkr = iterator.next();
                Column column = fkr.getForeignKeyColumn();
                foreignkeyColumns.add(column.getName());
            }
            return JSONArray.toJSONString(foreignkeyColumns,SerializerFeature.DisableCircularReferenceDetect);
        }else {
            return this.foreignKeyColumn;
        }
    }

    public void setForeignKeyColumn(String foreignKeyColumn) {
        this.foreignKeyColumn = foreignKeyColumn;
    }

    public void setPrimaryKeyColumn(String primaryKeyColumn) {
        this.primaryKeyColumn = primaryKeyColumn;
    }

    public String getPrimaryKeyColumn() {
        if(StringUtils.isBlank(primaryKeyColumn)) {
            List<String> primarykeyColumns = new ArrayList<>();
            for (Iterator<ForeignKeyColumnReference> iterator = this.getColumnReferences().iterator(); iterator.hasNext(); ) {
                ForeignKeyColumnReference fkr = iterator.next();
                Column column = fkr.getPrimaryKeyColumn();
                primarykeyColumns.add(column.getName());
            }
            return JSONArray.toJSONString(primarykeyColumns,SerializerFeature.DisableCircularReferenceDetect);
        }else{
            return this.primaryKeyColumn;
        }
    }
    public String getReferenceSchema(){
        if(this.getColumnReferences().size()>0){
        	Schema schema = this.getColumnReferences().get(0).getForeignKeyColumn().getSchema();
            return StringUtils.isEmpty(schema.getName())?schema.getCatalogName():schema.getName();
        }else{
            return this.referenceSchema;
        }
    }

    public void setReferenceSchema(String referenceSchema) {
        this.referenceSchema = referenceSchema;
    }

    public void setReferenceTable(String referenceTable) {
        this.referenceTable = referenceTable;
    }

    public String getReferenceTable(){
        if(this.getColumnReferences().size()>0){
            return this.getColumnReferences().get(0).getPrimaryKeyColumn().getParent().getName();
        }else{
            return this.referenceTable;
        }
    }
}
