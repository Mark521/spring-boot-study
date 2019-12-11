/*
 * 
//*******************************************************     

//**任何人或组织未经授权，不能修改代码     
//*******************************************************       
 */
package mark.component.dbdialect.mysql;

import mark.component.dbdialect.def.DefaultDDLDialect;
import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbdialect.SequenceGenerator;
import mark.component.dbmodel.model.Sequence;

/**
 * 类名: MySQLDDLDialect.java
 * 创建者 >
 * 时间: 2013-4-19, 9:58:50
 */
@ProductAnnotation(productName = "MySQL")
public class MySQLDDLDialect extends DefaultDDLDialect {
    
    @Override
    public String createSequenceSQL(Sequence sequence) {
        return SequenceGenerator.buildSequenceSQL(sequence);
    }

    @Override
    public String dropSequenceSQL(Sequence sequence) {
        return SequenceGenerator.deleteSequenceSQL(sequence);
    }
}
