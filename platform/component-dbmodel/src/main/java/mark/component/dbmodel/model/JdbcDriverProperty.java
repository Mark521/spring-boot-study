package mark.component.dbmodel.model;

import java.io.Serializable;
import java.sql.DriverPropertyInfo;
import java.util.*;

public final class JdbcDriverProperty extends Property
        implements Serializable, Comparable<JdbcDriverProperty> {

    private static final long serialVersionUID = 8030156654422512161L;
    private final String description;
    private final boolean required;
    private final List<String> choices;

    public JdbcDriverProperty(final DriverPropertyInfo driverPropertyInfo) {
        super(driverPropertyInfo.name, driverPropertyInfo.value);
        description = driverPropertyInfo.description;
        required = driverPropertyInfo.required;

        if (driverPropertyInfo.choices == null) {
            choices = Collections.emptyList();
        } else {
            choices = Arrays.asList(driverPropertyInfo.choices);
            Collections.sort(choices);
        }
    }

    @Override
    public int compareTo(final JdbcDriverProperty otherProperty) {
        if (otherProperty == null) {
            return -1;
        } else {
            return getName().toLowerCase().compareTo(otherProperty.getName().toLowerCase());
        }
    }

    public Collection<String> getChoices() {
        return new ArrayList<String>(choices);
    }

    public String getDescription() {
        if (description != null) {
            return description;
        } else {
            return "";
        }
    }

    @Override
    public String getValue() {
        return (String) super.getValue();
    }

    public boolean isRequired() {
        return required;
    }
}
