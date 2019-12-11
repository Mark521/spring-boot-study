package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.ProcedureColumnType;

public final class ProcedureColumn extends RoutineColumn<Procedure> {

    private static final long serialVersionUID = 3546361725629772857L;
    private ProcedureColumnType procedureColumnType;

    public ProcedureColumn(final Procedure parent, final String name) {
        super(parent, name);
    }
    public ProcedureColumn(){

    }
    public ProcedureColumnType getColumnType() {
        return procedureColumnType;
    }

    public void setProcedureColumnType(final ProcedureColumnType procedureColumnType) {
        this.procedureColumnType = procedureColumnType;
    }
}
