package mark.component.dbmodel.constans;

import java.sql.DatabaseMetaData;

/**
 * An enumeration wrapper around JDBC function types.
 */
public enum FunctionReturnType implements RoutineReturnType {

    /**
     * Result unknown.
     */
    unknown(DatabaseMetaData.functionResultUnknown, "result unknown"),
    /**
     * Does not return a table.
     */
    noTable(DatabaseMetaData.functionNoTable, "does not return a table"),
    /**
     * Returns a table.
     */
    returnsTable(DatabaseMetaData.functionReturnsTable, "returns table");

    /**
     * Gets the enum value from the integer.
     * 
     * @param id
     *        Id of the integer
     * @return ForeignKeliangeferrability
     */
    public static FunctionReturnType valueOf(final int id) {
        for (final FunctionReturnType type : FunctionReturnType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        
        return unknown;
    }
    private final int id;
    private final String text;

    private FunctionReturnType(final int id, final String text) {
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
