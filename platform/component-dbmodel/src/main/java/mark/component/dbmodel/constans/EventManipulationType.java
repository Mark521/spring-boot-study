package mark.component.dbmodel.constans;

import java.io.Serializable;

/**
 * Constraint type.
 */
public enum EventManipulationType implements Serializable {

    /**
     * Unknown
     */
    unknown,
    /**
     * Insert
     */
    insert,
    /**
     * Delete
     */
    delete,
    /**
     * Update
     */
    update;
}
