package mark.component.dbmodel.model;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import mark.component.dbmodel.constans.IndexType;
import mark.component.dbmodel.util.CompareUtility;
import mark.component.dbmodel.constans.NamedObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 索引模型,描述数据库索引模型
 * @author liang>
 */
public class Index extends AbstractDependantObject<Table> {

    private static final long serialVersionUID = 4051326747138079028L;
    @JsonManagedReference
    private final transient NamedObjectList<IndexColumn> columns = new NamedObjectList<IndexColumn>();
    private boolean isUnique;
    private IndexType type;
    private int cardinality;
    private int pages;
    private String columnsStr;

    public Index(final Table parent, final String name) {
        super(parent, name);
        // Default values
        type = IndexType.unknown;
    }
    public Index(){
        // Default values
        type = IndexType.unknown;
    }

    @Override
    public int compareTo(final NamedObject obj) {
        if (obj == null) {
            return -1;
        }

        final Index other = (Index) obj;
        final List<IndexColumn> thisColumns = getColumns();
        final List<IndexColumn> otherColumns = other.getColumns();

        return CompareUtility.compareLists(thisColumns, otherColumns);
    }

    public int getCardinality() {
        return cardinality;
    }

    public List<IndexColumn> getColumns() {
        return new ArrayList(columns.values());
    }

    public String getColumnsStr(){
        if(StringUtils.isNotBlank(this.columnsStr)){
            return this.columnsStr;
        }else {
            if (getColumns() != null && getColumns().size() > 0) {
                List<String> fields = new ArrayList<>();
                for (IndexColumn c : getColumns()) {
                    fields.add(c.getName());
                }
                return JSONArray.toJSONString(fields);
            }else {
                return "";
            }
        }
    }

    public void setColumnsStr(String columnsStr){
        this.columnsStr=columnsStr;
        if(StringUtils.isNotBlank(columnsStr)&&!"[]".equals(columnsStr)&&!"{}".equals(columnsStr)){
            JSONArray columnList=JSONArray.parseArray(columnsStr);
            if(columnList!=null&&columnList.size()>0){
                Iterator<Object> iterator=columnList.iterator();
                while(iterator.hasNext()){
                    String columnName= (String)iterator.next();
                    IndexColumn c=new IndexColumn(this,new Column(getParent(),columnName));
                    addColumn(c);
                }
            }
        }
    }

    public int getPages() {
        return pages;
    }

    public IndexType getType() {
        return type;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void addColumn(final IndexColumn column) {
        columns.add(column);
    }

    public void setCardinality(final int cardinality) {
        this.cardinality = cardinality;
    }

    public void setPages(final int pages) {
        this.pages = pages;
    }

    public void setType(final IndexType type) {
        this.type = type;
    }

    public void setUnique(final boolean unique) {
        isUnique = unique;
    }
}
