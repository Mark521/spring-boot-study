/*
 * 
//*******************************************************     

//**任何人或组织未经授权，不能修改代码     
//*******************************************************       
 */
package mark.component.dbdialect.db2;

import mark.component.dbdialect.def.DefaultDDLDialect;
import mark.component.dbmodel.model.Sequence;

import java.math.BigInteger;

/**
 * 类名: DB2DDLDialect.java
 * 创建者 >
 * 时间: 2013-4-18, 19:29:23
 */
public class DB2DDLDialect extends DefaultDDLDialect {

    @Override
    public String createSequenceSQL(Sequence sequence) {
        if (sequence == null) {
            throw new NullPointerException("sequence object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE SEQUENCE ");
        sb.append(sequence.getName());
        sb.append(" AS BIGINT ");
        
        BigInteger startValue = sequence.getStartValue();
        if (startValue != null) {
            sb.append(" START WITH ");
            sb.append(startValue);
        }else{
            sb.append(" START WITH 0");
        }
        
        BigInteger incrementBy = sequence.getIncrementBy();
        if (incrementBy != null) {
            sb.append(" INCREMENT BY ");
            sb.append(incrementBy);
        }else{
            sb.append(" INCREMENT BY 1");
        }

        BigInteger minValue = sequence.getMinValue();
        if (minValue != null) {
            sb.append(" MINVALUE ");
            sb.append(minValue);
        }
        BigInteger maxValue = sequence.getMaxValue();
        if (maxValue != null) {
            sb.append(" MAXVALUE ");
            sb.append(maxValue);
        }

        return sb.toString();
    }
}
