/*
 * 
 */
package mark.component.dbdialect.gaussdb;


import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbdialect.def.DefaultDDLDialect;
import mark.component.dbmodel.model.Table;

/**
 * 类名称： OracleDDLDialect
 * 描述：
 * 
 * 创建：   liang, 2013-4-1 16:06:05
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
@ProductAnnotation(productName = "Zenith")
public class GaussDB100DDLDialect extends DefaultDDLDialect {

    @Override
    public String createLikeTableSQL(Table sourceTable, Table targetTable) {
        if (sourceTable == null) {
            throw new NullPointerException("source tale object is null");
        }
        if (targetTable == null) {
            throw new NullPointerException("target  tale object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(targetTable.getName());
        sb.append(" AS SELECT * FROM ");
        sb.append(sourceTable.getName());
        sb.append(" where 1=2 ");

        return sb.toString();
    }
}
