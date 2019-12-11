package mark.component.dbmodel.constans;

import java.io.Serializable;
import java.sql.DatabaseMetaData;

/**
 * The deferrability value for foreign keys.
 */
public enum ForeignKeyDeferrability implements Serializable{

    /**
     * Unknown
     */
    unknown(-1, "unknown"),
    /**
     * Initially deferred.
     */
    initiallyDeferred(DatabaseMetaData.importedKeyInitiallyDeferred,
    "initially deferred"),
    /**
     * Initially immediate.
     */
    initiallyImmediate(DatabaseMetaData.importedKeyInitiallyImmediate,
    "initially immediate"),
    /**
     * Not deferrable.
     */
    keyNotDeferrable(DatabaseMetaData.importedKeyNotDeferrable, "not deferrable");

    /**
     * Gets the enum value from the integer.
     * 
     * @param id
     *        Id of the integer
     * @return ForeignKeyDeferrability
     */
    public static ForeignKeyDeferrability valueOf(final int id) {
        for (final ForeignKeyDeferrability fkDeferrability : ForeignKeyDeferrability.values()) {
            if (fkDeferrability.getId() == id) {
                return fkDeferrability;
            }
        }
        
        return unknown;
    }
    private final int id;
    private final String text;

    private ForeignKeyDeferrability(final int id, final String text) {
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
