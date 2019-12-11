package mark.component.core.model;

public class PageOrder implements Cloneable {

    private String prop;
    private String order;

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public PageOrder clone() {
        try {
            return (PageOrder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }
    }

}
