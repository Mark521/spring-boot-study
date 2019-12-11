package mark.component.dbmodel.constans;

import java.sql.DatabaseMetaData;

/**
 * An enumeration wrapper around procedure column types.
 */
public enum ProcedureColumnType implements RoutineColumnType {

    /**
     * Unknown.
     */
    unknown(DatabaseMetaData.procedureColumnUnknown, "unknown"),
    /**
     * In.
     */
    in(DatabaseMetaData.procedureColumnIn, "in"),
    /**
     * In/ out.
     */
    inOut(DatabaseMetaData.procedureColumnInOut, "in/ out"),
    /**
     * Out.
     */
    out(DatabaseMetaData.procedureColumnOut, "out"),
    /**
     * Return.
     */
    returnValue(DatabaseMetaData.procedureColumnReturn, "return"),
    /**
     * Return.
     */
    result(DatabaseMetaData.procedureColumnResult, "result");

    /**
     * Gets the enum value from the integer.
     * 
     * @param id
     *        Id of the integer
     * @return ForeignKeliangeferrability
     */
    public static ProcedureColumnType valueOf(final int id) {
        for (final ProcedureColumnType type : ProcedureColumnType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        return unknown;
    }
    private final int id;
    private final String text;

    private ProcedureColumnType(final int id, final String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return text;
    }
}
