package mark.component.dbmodel.constans;

import java.io.Serializable;
import java.sql.DatabaseMetaData;

/**
 * Foreign key update and delete rules.
 */
public enum ForeignKeyUpdateRule implements Serializable {

    /**
     * Unknown
     */
    unknown(-1, "unknown"),
    /**
     * No action.
     */
    noAction(DatabaseMetaData.importedKeyNoAction, "no action"),
    /**
     * Cascade.
     */
    cascade(DatabaseMetaData.importedKeyCascade, "cascade"),
    /**
     * Set null.
     */
    setNull(DatabaseMetaData.importedKeySetNull, "set null"),
    /**
     * Set default.
     */
    setDefault(DatabaseMetaData.importedKeySetDefault, "set default"),
    /**
     * Restrict.
     */
    restrict(DatabaseMetaData.importedKeyRestrict, "restrict");

    /**
     * Gets the enum value from the integer.
     * 
     * @param id
     *        Id of the integer
     * @return ForeignKeyUpdateRule
     */
    public static ForeignKeyUpdateRule valueOf(final int id) {
        for (final ForeignKeyUpdateRule type : ForeignKeyUpdateRule.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        
        return unknown;
    }
    private final String text;
    private final int id;

    private ForeignKeyUpdateRule(final int id, final String text) {
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
