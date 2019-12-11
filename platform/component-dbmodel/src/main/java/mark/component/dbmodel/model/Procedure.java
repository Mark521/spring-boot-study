package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.ProcedureReturnType;
import mark.component.dbmodel.constans.RoutineType;

import java.util.ArrayList;
import java.util.List;

public final class Procedure extends Routine {

    private static final long serialVersionUID = 3906925686089134130L;
    private ProcedureReturnType procedureType;
    private String returnsText;
    private final NamedObjectList<ProcedureColumn> columns = new NamedObjectList<ProcedureColumn>();

    public Procedure(final Schema schema, final String name) {
        super(schema, name);
        // Default values
        procedureType = ProcedureReturnType.unknown;
    }
    public Procedure(){
        procedureType = ProcedureReturnType.unknown;
    }
    public ProcedureColumn getColumn(final String name) {
        return columns.lookup(this, name);
    }

    @Override
    public List<ProcedureColumn> getColumns() {
        return new ArrayList<ProcedureColumn>(columns.values());
    }

    public ProcedureReturnType getReturnType() {
        return procedureType;
    }

    public RoutineType getType() {
        return RoutineType.procedure;
    }

    public void addColumn(final ProcedureColumn column) {
        columns.add(column);
    }

    public void setType(final ProcedureReturnType type) {
        if (type == null) {
            throw new IllegalArgumentException("Null procedure type");
        }
        procedureType = type;
    }

    public String getReturnsText() {
        return returnsText;
    }

    public void setReturnsText(String returnsText) {
        this.returnsText = returnsText;
    }
}
