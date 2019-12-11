/*
 */
package mark.component.dbdialect.mysql;

import mark.component.dbdialect.def.DefaultDMLDialect;
import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbdialect.SequenceGenerator;

/**
 * 类名: MySQLDMLDialect.java
 * 创建者 >
 * 时间: 2013-4-7, 17:30:43
 */
@ProductAnnotation(productName = "MySQL")
public class MySQLDMLDialect extends DefaultDMLDialect {

    @Override
    public String buildSequenceUpdateSQL(String sequenceName) {
        return SequenceGenerator.updateLastValueSQL(sequenceName);
    }
}
