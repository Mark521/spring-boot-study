package mark.component.dbmodel.model;

public class PrimaryKey extends Index {

    private static final long serialVersionUID = -7169206178562782087L;

    /**
     * Copies information from an index.
     * 
     * @param index
     *        Index
     */
    public PrimaryKey(final Index index) {
        super(index.getParent(), index.getName());
        setCardinality(index.getCardinality());
        setPages(index.getPages());
        setRemarks(index.getRemarks());
        setType(index.getType());
        setUnique(index.isUnique());
        // Copy columns
        for (final IndexColumn column : index.getColumns()) {
            addColumn((IndexColumn) column);
        }
    }

    public PrimaryKey(final Table parent, final String name) {
        super(parent, name);
    }

    @Override
    public final boolean isUnique() {
        return true;
    }
}
