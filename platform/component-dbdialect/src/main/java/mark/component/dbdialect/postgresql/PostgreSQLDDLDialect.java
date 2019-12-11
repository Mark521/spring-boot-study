/*
 * 
//*******************************************************     

//**任何人或组织未经授权，不能修改代码     
//*******************************************************       
 */
package mark.component.dbdialect.postgresql;

import mark.component.dbdialect.ProductAnnotation;
import mark.component.dbdialect.def.DefaultDDLDialect;
import mark.component.dbmodel.model.Sequence;
import java.math.BigInteger;

/**
 * 类名: PostgreSQLDDLDialect.java
 * 创建者 >
 * 时间: 2013-4-18, 19:01:34
 */
@ProductAnnotation(productName = "PostgreSQL")
public class PostgreSQLDDLDialect extends DefaultDDLDialect {

    @Override
    public String createSequenceSQL(Sequence sequence) throws NullPointerException {
        if (sequence == null) {
            throw new NullPointerException("sequence object is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE SEQUENCE ");
        sb.append(sequence.getName());

        BigInteger incrementBy = sequence.getIncrementBy();
        if (incrementBy != null) {
            sb.append(" INCREMENT BY ");
            sb.append(incrementBy);
        } else {
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

        BigInteger startValue = sequence.getStartValue();
        if (startValue != null) {
            sb.append(" START ");
            sb.append(startValue);
        } else {
            sb.append(" START 0");
        }

        return sb.toString();
    }
}