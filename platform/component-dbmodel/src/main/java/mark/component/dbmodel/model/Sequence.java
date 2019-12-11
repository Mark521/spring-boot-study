package mark.component.dbmodel.model;

import java.math.BigInteger;

/**
 * 类名称： Sequence
 * 描述：
 * 
 * 创建：   liang, 2013-3-21 19:01:32
 *
 * 修订历史：（降序）
 * BugID/BacklogID  -  开发人员  日期
 * #  
 */
public class Sequence extends AbstractDatabaseObject {

    private BigInteger incrementBy = BigInteger.ONE;             //增量值
    private BigInteger minValue = BigInteger.ZERO;                  //最小值
    private BigInteger startValue = BigInteger.ZERO;                //开始值
    private BigInteger maxValue;      //最大值
    private BigInteger lastValue;                  //最后一个值

    public Sequence(Schema schema, String name) {
        super(schema, name);
    }
    public Sequence(){

    }
    public BigInteger getIncrementBy() {
        return incrementBy;
    }

    public void setIncrementBy(BigInteger incrementBy) {
        this.incrementBy = incrementBy;
    }

    public BigInteger getLastValue() {
        return lastValue;
    }

    public void setLastValue(BigInteger lastValue) {
        this.lastValue = lastValue;
    }

    public BigInteger getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigInteger maxValue) {
        this.maxValue = maxValue;
    }

    public BigInteger getMinValue() {
        return minValue;
    }

    public void setMinValue(BigInteger minValue) {
        this.minValue = minValue;
    }

    public BigInteger getStartValue() {
        return startValue;
    }

    public void setStartValue(BigInteger startValue) {
        this.startValue = startValue;
    }
}
