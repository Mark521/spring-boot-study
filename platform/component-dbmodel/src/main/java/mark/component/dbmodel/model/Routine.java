package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.RoutineBodyType;
import mark.component.dbmodel.util.Utility;

import java.util.List;

public abstract class Routine extends AbstractDatabaseObject {

    private static final long serialVersionUID = 3906925686089134130L;
    private String specificName;
    private RoutineBodyType routineBodyType;
    private StringBuilder definition;

    public Routine(final Schema schema, final String name) {
        super(schema, name);
        routineBodyType = RoutineBodyType.unknown;
        definition = new StringBuilder();
    }
    public Routine(){
        routineBodyType = RoutineBodyType.unknown;
        definition = new StringBuilder();
    }
    public String getDefinition() {
        return definition.toString();
    }
    public void setDefinition(String definition){
        this.appendDefinition(definition);
    }
    @Override
    public String getLookupKey() {
        final String lookupKey = super.getLookupKey();
        if (Utility.isBlank(specificName)) {
            return lookupKey;
        } else {
            return getSchema().getFullName() + "." + specificName;
        }
    }

    public RoutineBodyType getRoutineBodyType() {
        return routineBodyType;
    }

    public String getSpecificName() {
        return specificName;
    }

    public void appendDefinition(final String definition) {
        if (definition != null) {
            this.definition.append(definition);
        }
    }

    public void setRoutineBodyType(final RoutineBodyType routineBodyType) {
        this.routineBodyType = routineBodyType;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    public List<? extends RoutineColumn<? extends Routine>> getColumns() {
        return null;
    }
}
