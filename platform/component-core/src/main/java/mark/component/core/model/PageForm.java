package mark.component.core.model;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PageForm {
    private int page = -1;// 页码，从1开始
    private int rows = -1;// 页大小
    private int offset = -1;// 下标，偏移量，从0开始
    private int curr = -1;// 页码，从1开始
    private int limit = -1;// 页大小
    // 排序，Key：查询对象的属性名（如果排序的列名没有被查询出来，则直接使用数据库的列名），Value：升级或降序（ASC、DESC）
    private Map<String, String> order = new LinkedHashMap<String, String>();
    private PageOrder[] order2;

    public PageForm() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page < 1) {
            this.page = 1;
        } else {
            this.page = page;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows < 1) {
            this.rows = 1;
        } else if (rows > 200) {
            this.rows = 200;
        } else {
            this.rows = rows;
        }
    }

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

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        if (curr < 1) {
            this.curr = 1;
        } else {
            this.curr = curr;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit < 1) {
            this.limit = 1;
        } else if (limit > 200) {
            this.limit = 200;
        } else {
            this.limit = limit;
        }
    }

    public Map<String, String> getOrder() {
        return order;
    }

    public void setOrder(Map<String, String> order) {
        this.order = order;
    }

    public PageOrder[] getOrder2() {
        if (order2 == null) {
            return null;
        }
        return Arrays.copyOf(order2, order2.length);
    }

    public void setOrder2(PageOrder[] order2) {
        if (order2 == null) {
            this.order2 = null;
            return;
        }
        this.order2 = Arrays.copyOf(order2, order2.length);
    }

    public <T> Page<T> toPage() {
        Page<T> p = new Page<T>();

        if (order != null) {
            for (Entry<String, String> entry : order.entrySet()) {
                p.addOrder(new Pair<String, String>(entry.getKey(), entry.getValue()));
            }
        } else if (order2 != null) {
            for (PageOrder po : order2) {
                String asc = "DESCENDING".equalsIgnoreCase(po.getOrder()) ? "DESC" : "ASC";
                p.addOrder(new Pair<String, String>(po.getProp(), asc));
            }
        }

        // offset和limit的组合
        if (offset != -1 && limit != -1) {
            p.setOffset(offset);
            p.setLimit(limit);
            return p;
        }

        // page和rows的组合
        if (page != -1 && rows != -1) {
            p.setOffset((page - 1) * rows);
            p.setLimit(rows);
            return p;
        }

        // crrr和limit的组合
        if (curr != -1 && limit != -1) {
            p.setOffset((curr - 1) * limit);
            p.setLimit(limit);
            return p;
        }

        p.setOffset(0);
        p.setLimit(10);
        return p;
    }
}
