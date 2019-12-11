package mark.component.dbmodel.constans;

import java.io.Serializable;
import java.sql.DatabaseMetaData;

/**
 * An enumeration wrapper around JDBC procedure types.
 */
public enum SearchableType implements Serializable {

    /**
     * Unknown
     */
    unknown(-1, "unknown"),
    /**
     * Not searchable.
     */
    predNone(DatabaseMetaData.typePredNone, "not searchable"),
    /**
     * Only searchable with where .. like.
     */
    predChar(DatabaseMetaData.typePredChar, "only searchable with where .. like"),
    /**
     * Searchable except with where .. like.
     */
    predBasic(DatabaseMetaData.typePredBasic,
    "searchable except with where .. like"),
    /**
     * Searchable.
     */
    searchable(DatabaseMetaData.typeSearchable, "searchable");

    /**
     * Gets the enum value from the integer.
     * 
     * @param id
     *        Id of the integer
     * @return SearchableType
     */
    public static SearchableType valueOf(final int id) {
        for (final SearchableType type : SearchableType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        
        return unknown;
    }
    private final int id;
    private final String text;

    private SearchableType(final int id, final String text) {
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
