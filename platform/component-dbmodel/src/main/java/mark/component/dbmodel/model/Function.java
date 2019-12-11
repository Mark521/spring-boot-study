package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.FunctionReturnType;
import mark.component.dbmodel.constans.RoutineBodyType;
import mark.component.dbmodel.constans.RoutineType;

import java.util.ArrayList;
import java.util.List;

public final class Function extends Routine {

    private static final long serialVersionUID = 3906925686089134130L;
    private FunctionReturnType functionType;
    private final NamedObjectList<FunctionColumn> columns = new NamedObjectList<FunctionColumn>();
    private RoutineBodyType routineBodyType;
    private String returnsText;
    public Function(final Schema schema, final String name) {
        super(schema, name);
        // Default values
        functionType = FunctionReturnType.unknown;
        routineBodyType = RoutineBodyType.unknown;
    }
    public Function(){
    // Default values
        functionType = FunctionReturnType.unknown;
        routineBodyType = RoutineBodyType.unknown;
    }

    public FunctionColumn getColumn(final String name) {
        return columns.lookup(this, name);
    }

    @Override
    public List<FunctionColumn> getColumns() {
        return new ArrayList<FunctionColumn>(columns.values());
    }

    public FunctionReturnType getReturnType() {
        return functionType;
    }

    @Override
    public RoutineBodyType getRoutineBodyType() {
        return routineBodyType;
    }

    public RoutineType getType() {
        return RoutineType.function;
    }

    public void addColumn(final FunctionColumn column) {
        columns.add(column);
    }

    @Override
    public void setRoutineBodyType(final RoutineBodyType routineBodyType) {
        this.routineBodyType = routineBodyType;
    }

    public void setType(final FunctionReturnType type) {
        if (type == null) {
            throw new IllegalArgumentException("Null function type");
        }
        functionType = type;
    }

    public String getReturnsText() {
        return returnsText;
    }

    public void setReturnsText(String returnsText) {
        this.returnsText = returnsText;
    }
}
