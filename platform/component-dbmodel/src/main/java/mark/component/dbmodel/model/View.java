package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.CheckOptionType;
import mark.component.dbmodel.constans.TableType;

public class View extends Table {

    private static final long serialVersionUID = 3257290248802284852L;
    private StringBuilder definition;
    private CheckOptionType checkOption;
    private boolean updatable;

    public View(final Schema schema, final String name) {
        super(schema, name);
        definition = new StringBuilder();
    }
    public View(){
        definition = new StringBuilder();
    }
    public CheckOptionType getCheckOption() {
        return checkOption;
    }

    public String getDefinition() {
        return definition.toString();
    }
    public void setDefinition(String definition){
        this.appendDefinition(definition);
    }
    @Override
    public TableType getType() {
        return TableType.view;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void appendDefinition(final String definition) {
        if (definition != null) {
            this.definition.append(definition);
        }
    }

    public void setCheckOption(final CheckOptionType checkOption) {
        this.checkOption = checkOption;
    }

    @Override
    public void setType(final TableType type) {
        if (type != TableType.view) {
            throw new UnsupportedOperationException("Cannot reset view type");
        }
    }

    public void setUpdatable(final boolean updatable) {
        this.updatable = updatable;
    }
}
