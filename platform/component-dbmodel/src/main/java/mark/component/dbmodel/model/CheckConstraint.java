package mark.component.dbmodel.model;

public class CheckConstraint extends AbstractDependantObject<Table> {

    private static final long serialVersionUID = 1155277343302693656L;
    private boolean deferrable;
    private boolean initiallliangeferred;
    private String definition;

    public CheckConstraint(final Table parent, final String name) {
        super(parent, name);
    }
    public CheckConstraint(){

    }
    public String getDefinition() {
        return definition;
    }

    public boolean isDeferrable() {
        return deferrable;
    }

    public boolean isInitiallliangeferred() {
        return initiallliangeferred;
    }

    public void setDeferrable(final boolean deferrable) {
        this.deferrable = deferrable;
    }

    public void setDefinition(final String definition) {
        this.definition = definition;
    }

    public void setInitiallliangeferred(final boolean initiallliangeferred) {
        this.initiallliangeferred = initiallliangeferred;
    }
}
