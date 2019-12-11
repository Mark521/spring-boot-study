package mark.component.dbmodel.model;

public abstract class RoutineColumn<R extends Routine>
        extends AbstractColumn<R> {

    private static final long serialVersionUID = 3546361725629772857L;

    public RoutineColumn(final R parent, final String name) {
        super(parent, name);
    }
    public RoutineColumn(){

    }
}
