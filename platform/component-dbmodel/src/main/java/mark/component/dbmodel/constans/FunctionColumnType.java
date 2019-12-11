package mark.component.dbmodel.constans;

import java.sql.DatabaseMetaData;

/**
 * An enumeration wrapper around procedure column types.
 */
public enum FunctionColumnType implements RoutineColumnType {

    /**
     * Unknown.
     */
    unknown(DatabaseMetaData.functionColumnUnknown, "unknown"),
    /**
     * In.
     */
    in(DatabaseMetaData.functionColumnIn, "in"),
    /**
     * In/ out.
     */
    inOut(DatabaseMetaData.functionColumnInOut, "in/ out"),
    /**
     * Out.
     */
    out(DatabaseMetaData.functionColumnOut, "out"),
    /**
     * Return.
     */
    returnValue(DatabaseMetaData.functionColumnResult, "return"),
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
    public static FunctionColumnType valueOf(final int id) {
        for (final FunctionColumnType type : FunctionColumnType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        
        return unknown;
    }
    private final int id;
    private final String text;

    private FunctionColumnType(final int id, final String text) {
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
