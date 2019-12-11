package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.ActionOrientationType;
import mark.component.dbmodel.constans.ConditionTimingType;
import mark.component.dbmodel.constans.EventManipulationType;

public class Trigger extends AbstractDependantObject<Table> {

    private static final long serialVersionUID = -1619291073229701764L;
    private EventManipulationType eventManipulationType;
    private int actionOrder;
    private StringBuilder actionCondition;
    private StringBuilder actionStatement;
    private String created;

    private ActionOrientationType actionOrientation;
    private ConditionTimingType conditionTiming;

    public Trigger(final Table parent, final String name) {
        super(parent, name);
        // Default values
        eventManipulationType = EventManipulationType.unknown;
        actionOrientation = ActionOrientationType.unknown;
        conditionTiming = ConditionTimingType.unknown;
        actionCondition = new StringBuilder();
        actionStatement = new StringBuilder();
    }
    public Trigger(){
     // Default values
        eventManipulationType = EventManipulationType.unknown;
        actionOrientation = ActionOrientationType.unknown;
        conditionTiming = ConditionTimingType.unknown;
        actionCondition = new StringBuilder();
        actionStatement = new StringBuilder();
    }
    public String getActionCondition() {
        return actionCondition.toString();
    }

    public int getActionOrder() {
        return actionOrder;
    }

    public ActionOrientationType getActionOrientation() {
        return actionOrientation;
    }

    public String getActionStatement() {
        return actionStatement.toString();
    }

    public ConditionTimingType getConditionTiming() {
        return conditionTiming;
    }

    public EventManipulationType getEventManipulationType() {
        return eventManipulationType;
    }

    public void appendActionCondition(final String actionCondition) {
        if (actionCondition != null) {
            this.actionCondition.append(actionCondition);
        }
    }

    public void appendActionStatement(final String actionStatement) {
        if (actionStatement != null) {
            this.actionStatement.append(actionStatement);
        }
    }

    public void setActionOrder(final int actionOrder) {
        this.actionOrder = actionOrder;
    }

    public void setActionOrientation(final ActionOrientationType actionOrientation) {
        this.actionOrientation = actionOrientation;
    }

    public void setConditionTiming(final ConditionTimingType conditionTiming) {
        this.conditionTiming = conditionTiming;
    }

    public void setEventManipulationType(final EventManipulationType eventManipulationType) {
        this.eventManipulationType = eventManipulationType;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
