package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.FunctionColumnType;

public final class FunctionColumn extends RoutineColumn<Function> {

    private static final long serialVersionUID = 3546361725629772857L;
    private FunctionColumnType functionColumnType;

    public FunctionColumn(final Function parent, final String name) {
        super(parent, name);
    }
    public FunctionColumn(){

    }
    public FunctionColumnType getColumnType() {
        return functionColumnType;
    }

    public void setFunctionColumnType(final FunctionColumnType functionColumnType) {
        this.functionColumnType = functionColumnType;
    }
}
