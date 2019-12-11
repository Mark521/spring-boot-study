package mark.component.dbmodel.constans;

import java.io.Serializable;

/**
 * An enumeration wrapper around index sort sequences.
 */
public enum IndexColumnSortSequence implements Serializable {

    /**
     * Unknown
     */
    unknown("unknown"),
    /**
     * Ascending.
     */
    ascending("ASC"),
    /**
     * Descending.
     */
    descending("DESC");

    /**
     * Find the enumeration value corresponding to the string.
     * 
     * @param code
     *        Sort sequence code.
     * @return Enumeration value
     */
    public static IndexColumnSortSequence valueOfFromCode(final String code) {
        for (final IndexColumnSortSequence type : IndexColumnSortSequence.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }

        return unknown;
    }
    private final String code;

    private IndexColumnSortSequence(final String code) {
        this.code = code;
    }

    /**
     * Index sort sequence code.
     * 
     * @return Index sort sequence code
     */
    public String getCode() {
        return code;
    }
}
