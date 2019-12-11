package mark.component.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台使用的分页对象，不允许出现在Action的对外接口的参数上
 * 
 * @author thinkpad
 *
 * @param <T>
 */
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private int offset;// 开始下标，从0开始
    private int limit;// 取多少行
    private boolean calculateCount = true;// 是否需要统计总行数
    private List<Pair<String, String>> order = new ArrayList<Pair<String, String>>();// 排序，Key：查询对象的属性名（如果排序的列名没有被查询出来，则直接使用数据库的列名），Value：升级或降序（ASC、DESC）
    private List<Pair<String, String>> unsafeOrder = new ArrayList<Pair<String, String>>();// 排序的扩展，比如需要在排序的字段上加函数时则不能使用上面的排序，该属性只允许内置代码访问，不允许直接将外部接口传过来的参数赋值给它

    private int total;// 总行数，当不计算总行数时，-1表示还有更多数据，正数表示没有了
    private List<T> records;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        if (offset < 0) {
            this.offset = 0;
        } else {
            this.offset = offset;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit < 1) {
            this.limit = 1;
        } else {
            this.limit = limit;
        }
    }

    public boolean isCalculateCount() {
        return calculateCount;
    }

    public void setCalculateCount(boolean calculateCount) {
        this.calculateCount = calculateCount;
    }

    public List<Pair<String, String>> getOrder() {
        return order;
    }

    public void setOrder(List<Pair<String, String>> order) {
        this.order = order;
    }

    public void addOrder(Pair<String, String> order) {
        this.order.add(order);
    }

    public boolean hasOrder() {
        return (order != null && !order.isEmpty()) || (unsafeOrder != null && !unsafeOrder.isEmpty());
    }

    public List<Pair<String, String>> getUnsafeOrder() {
        return unsafeOrder;
    }

    public void setUnsafeOrder(List<Pair<String, String>> unsafeOrder) {
        this.unsafeOrder = unsafeOrder;
    }

    public void addUnsafeOrder(Pair<String, String> unsafeOrder) {
        this.unsafeOrder.add(unsafeOrder);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public static <T> Page<T> maxPage() {
        Page<T> p = new Page<T>();
        p.setOffset(0);
        p.setLimit(Integer.MAX_VALUE);
        return p;
    }

    public static <T> Page<T> maxPage(String column, String order) {
        Page<T> p = new Page<T>();
        p.setOffset(0);
        p.setLimit(Integer.MAX_VALUE);
        p.addOrder(new Pair<String, String>(column, order));
        return p;
    }

}
