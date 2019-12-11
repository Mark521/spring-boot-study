package mark.component.dbmodel.model;

public final class ResultsColumn extends AbstractColumn<Table> {

    private static final long serialVersionUID = -6983013302549352559L;
    private String label;
    private int displaySize;
    private boolean autoIncrement;
    private boolean caseSensitive;
    private boolean currency;
    private boolean definitelyWritable;
    private boolean readOnly;
    private boolean searchable;
    private boolean signed;
    private boolean writable;

    public ResultsColumn(final Table parent, final String name) {
        super(parent, name);
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public String getLabel() {
        return label;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public boolean isCurrency() {
        return currency;
    }

    public boolean isDefinitelyWritable() {
        return definitelyWritable;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public boolean isSigned() {
        return signed;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setAutoIncrement(final boolean isAutoIncrement) {
        autoIncrement = isAutoIncrement;
    }

    public void setCaseSensitive(final boolean isCaseSensitive) {
        caseSensitive = isCaseSensitive;
    }

    public void setCurrency(final boolean isCurrency) {
        currency = isCurrency;
    }

    public void setDefinitelyWritable(final boolean isDefinitelyWritable) {
        definitelyWritable = isDefinitelyWritable;
    }

    public void setDisplaySize(final int displaySize) {
        this.displaySize = displaySize;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setReadOnly(final boolean isReadOnly) {
        readOnly = isReadOnly;
    }

    public void setSearchable(final boolean isSearchable) {
        searchable = isSearchable;
    }

    public void setSigned(final boolean isSigned) {
        signed = isSigned;
    }

    public void setWritable(final boolean isWritable) {
        writable = isWritable;
    }
}
