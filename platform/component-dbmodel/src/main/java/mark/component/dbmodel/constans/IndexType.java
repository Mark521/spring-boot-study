package mark.component.dbmodel.constans;

import java.io.Serializable;
import java.sql.DatabaseMetaData;

/**
 * An enumeration wrapper around index types.
 */
public enum IndexType implements Serializable {

    /**
     * Unknown
     */
    unknown(-1),
    /**
     * Statistic.
     */
    statistic(DatabaseMetaData.tableIndexStatistic),
    /**
     * Clustered.
     */
    clustered(DatabaseMetaData.tableIndexClustered),
    /**
     * Hashed.
     */
    hashed(DatabaseMetaData.tableIndexHashed),
    /**
     * Other.
     */
    other(DatabaseMetaData.tableIndexOther);

    /**
     * Gets the value from the id.
     * 
     * @param id
     *        Id of the enumeration.
     * @return IndexType
     */
    public static IndexType valueOf(final int id) {
        for (final IndexType type : IndexType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        return unknown;
    }
    private final int id;

    private IndexType(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
