package mark.component.dbmodel.constans;

import java.sql.DatabaseMetaData;

/**
 * An enumeration wrapper around JDBC procedure types.
 */
public enum ProcedureReturnType implements RoutineReturnType {

    /**
     * Result unknown.
     */
    unknown(DatabaseMetaData.procedureResultUnknown, "result unknown"),
    /**
     * No result.
     */
    noResult(DatabaseMetaData.procedureNoResult, "no result"),
    /**
     * Returns result.
     */
    returnsResult(DatabaseMetaData.procedureReturnsResult, "returns result");

    /**
     * Gets the enum value from the integer.
     * 
     * @param id
     *        Id of the integer
     * @return ForeignKeliangeferrability
     */
    public static ProcedureReturnType valueOf(final int id) {
        for (final ProcedureReturnType type : ProcedureReturnType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        return unknown;
    }
    private final int id;
    private final String text;

    private ProcedureReturnType(final int id, final String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
