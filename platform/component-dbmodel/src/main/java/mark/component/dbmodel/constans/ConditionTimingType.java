package mark.component.dbmodel.constans;

import java.io.Serializable;

/**
 * Constraint type.
 */
public enum ConditionTimingType implements Serializable {

    /**
     * Unknown
     */
    unknown("unknown"),
    /**
     * Before
     */
    before("BEFORE"),
    /**
     * Instead of
     */
    instead_of("INSTEAD OF"),
    /**
     * After
     */
    after("AFTER");

    /**
     * Find the enumeration value corresponding to the string.
     * 
     * @param value
     *        Sort sequence code.
     * @return Enumeration value
     */
    public static ConditionTimingType valueOfFromValue(final String value) {
        for (final ConditionTimingType type : ConditionTimingType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }

        return unknown;
    }
    private final String value;

    private ConditionTimingType(final String value) {
        this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return Value
     */
    public final String getValue() {
        return value;
    }
}
