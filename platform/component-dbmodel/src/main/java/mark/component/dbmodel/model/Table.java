package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.TableType;
import mark.component.dbmodel.constans.NamedObject;
import mark.component.dbmodel.constans.TableRelationshipType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;

public class Table extends AbstractDatabaseObject {

    private enum TableAssociationType {
        all,
        exported,
        imported;
    }
    private static final long serialVersionUID = 3257290248802284852L;
    private TableType type = TableType.unknown; // Default value
    private PrimaryKey primaryKey;
    private final NamedObjectList<Column> columns = new NamedObjectList<Column>();
    private final NamedObjectList<ForeignKey> foreignKeys = new NamedObjectList<ForeignKey>();
    private final NamedObjectList<Index> indices = new NamedObjectList<Index>();
    private final NamedObjectList<CheckConstraint> checkConstraints = new NamedObjectList<CheckConstraint>();
    private final NamedObjectList<Trigger> triggers = new NamedObjectList<Trigger>();
    private final NamedObjectList<Privilege<Table>> privileges = new NamedObjectList<Privilege<Table>>();
    private int sortIndex;

    public Table(final Schema schema, final String name) {
        super(schema, name);
    }
    public Table(){

    }
    @Override
    public int compareTo(final NamedObject obj) {
        if (obj == null) {
            return -1;
        }

        final Table other = (Table) obj;
        int comparison = 0;

        if (comparison == 0) {
            comparison = sortIndex - other.sortIndex;
        }
        if (comparison == 0) {
            comparison = super.compareTo(other);
        }

        return comparison;
    }

    public Collection<CheckConstraint> getCheckConstraints() {
        return new ArrayList<CheckConstraint>(checkConstraints.values());
    }

    public Column getColumn(final String name) {
        return columns.lookup(this, name);
    }

    public List<Column> getColumns() {
        return new ArrayList<Column>(columns.values());
    }

    public Collection<Column> getPrimaryColumns() {
        if (primaryKey == null) {
            return CollectionUtils.EMPTY_COLLECTION;
        }
        Set<Column> columns = new HashSet<Column>();
        for (IndexColumn indexColumn : primaryKey.getColumns()) {
            columns.add(indexColumn.getColumn());
        }
        return columns;
    }

    public String getColumnsListAsString() {
        final List<Column> columnsArray = getColumns();
        final StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < columnsArray.size(); i++) {
            final Column column = columnsArray.get(i);
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(column.getName());
        }
        return buffer.toString();
    }

    public Collection<ForeignKey> getExportedForeignKeys() {
        return getForeignKeys(TableAssociationType.exported);
    }

    public ForeignKey getForeignKey(final String name) {
        return foreignKeys.lookup(this, name);
    }

    public Collection<ForeignKey> getForeignKeys() {
        return getForeignKeys(TableAssociationType.all);
    }

    public Collection<ForeignKey> getImportedForeignKeys() {
        return getForeignKeys(TableAssociationType.imported);
    }

    public Index getIndex(final String name) {
        return indices.lookup(this, name);
    }

    public Collection<Index> getIndices() {
        final List<Index> values = new ArrayList<Index>(indices.values());
        return values;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public Privilege<Table> getPrivilege(final String name) {
        return privileges.lookup(this, name);
    }

    public Collection<Privilege<Table>> getPrivileges() {
        return new ArrayList<Privilege<Table>>(privileges.values());
    }

    public Collection<Column> getNullableColumns(boolean nullable) {
        List<Column> results = new ArrayList<Column>();
        for(Column column : columns) {
            if(nullable && column.isNullable()) {
                results.add(column);
            } else if(!nullable && !column.isNullable()) {
                results.add(column);
            }

        }
        return results;
    }

    public Collection<Column> getNotKeyColumns() {
        List<Column> results = new ArrayList<Column>();
        for(Column column : columns) {
            if(!column.isPartOfForeignKey() && !column.isPartOfPrimaryKey()) {
                results.add(column);
            }
        }
        return results;
    }

    public Collection<Column> getNotPrimaryKeyColumns() {
        List<Column> results = new ArrayList<Column>();
        for(Column column : columns) {
            if(!column.isPartOfPrimaryKey()) {
                results.add(column);
            }
        }
        return results;
    }

    public Collection<Column> getImportedColumns() {
        List<Column> results = new ArrayList<Column>();
        for(ForeignKey fk : getImportedForeignKeys()) {
            for(ForeignKeyColumnReference reference : fk.getColumnReferences()) {
                results.add(reference.getForeignKeyColumn());
            }
        }
        return results;
    }

    public Collection<Column> getExportedColumns() {
        List<Column> results = new ArrayList<Column>();
        for(ForeignKey fk : getImportedForeignKeys()) {
            for(ForeignKeyColumnReference reference : fk.getColumnReferences()) {
                results.add(reference.getPrimaryKeyColumn());
            }
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    public Collection<ForeignKeyColumnReference> getForeignKeyReferencess(Column column) {
        if (column == null) {
            return CollectionUtils.EMPTY_COLLECTION;
        }
        Set<ForeignKeyColumnReference> references = new HashSet<ForeignKeyColumnReference>();
        for (ForeignKey foreignKey : getForeignKeys()) {
            for (ForeignKeyColumnReference columnReference : foreignKey.getColumnReferences()) {
                if (ObjectUtils.equals(column, columnReference.getForeignKeyColumn())
                        || ObjectUtils.equals(column, columnReference.getPrimaryKeyColumn())) {
                    references.add(columnReference);
                }
            }
        }
        return references;
    }


    public Collection<Table> getRelatedTables(final TableRelationshipType tableRelationshipType) {
        final Set<Table> relatedTables = new HashSet<Table>();
        if (tableRelationshipType != null
                && tableRelationshipType != TableRelationshipType.none) {
            final List<ForeignKey> foreignKeysList = new ArrayList<ForeignKey>(foreignKeys.values());
            for (final ForeignKey mutableForeignKey : foreignKeysList) {
                for (final ForeignKeyColumnReference columnReference : mutableForeignKey.getColumnReferences()) {
                    final Table parentTable = (Table) columnReference.getPrimaryKeyColumn().getParent();
                    final Table childTable = (Table) columnReference.getForeignKeyColumn().getParent();
                    switch (tableRelationshipType) {
                        case parent:
                            if (equals(childTable)) {
                                relatedTables.add(parentTable);
                            }
                            break;
                        case child:
                            if (equals(parentTable)) {
                                relatedTables.add(childTable);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return relatedTables;
    }

    public Trigger getTrigger(final String name) {
        return lookupTrigger(name);
    }

    public Collection<Trigger> getTriggers() {
        return new ArrayList<Trigger>(triggers.values());
    }

    public TableType getType() {
        return type;
    }

    public void addCheckConstraint(final CheckConstraint checkConstraint) {
        checkConstraints.add(checkConstraint);
    }

    public void addColumn(final Column column) {
        columns.add(column);
    }

    public void addForeignKey(final ForeignKey foreignKey) {
        foreignKeys.add(foreignKey);
    }

    public void addIndex(final Index index) {
        indices.add(index);
    }

    public void addPrivilege(final Privilege<Table> privilege) {
        privileges.add(privilege);
    }

    public void addTrigger(final Trigger trigger) {
        triggers.add(trigger);
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public Trigger lookupTrigger(final String triggerName) {
        return triggers.lookup(this, triggerName);
    }

    public void replacePrimaryKey() {
        if (primaryKey == null) {
            return;
        }

        final String primaryKeyName = primaryKey.getName();
        final Index index = indices.lookup(this, primaryKeyName);
        if (index != null) {
            boolean indexHasPkColumns = false;
            final List<IndexColumn> pkColumns = primaryKey.getColumns();
            final List<IndexColumn> indexColumns = index.getColumns();
            if (pkColumns.size() == indexColumns.size()) {
                for (int i = 0; i < indexColumns.size(); i++) {
                    if (!pkColumns.get(i).equals(indexColumns.get(i))) {
                        break;
                    }
                }
                indexHasPkColumns = true;
            }
            if (indexHasPkColumns) {
                indices.remove(index);
                setPrimaryKey(new PrimaryKey(index));
            }
        }
    }

    public void setPrimaryKey(final PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setSortIndex(final int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public void setType(final TableType type) {
        if (type == null) {
            throw new IllegalArgumentException("Null table type");
        }
        this.type = type;
    }

    private Collection<ForeignKey> getForeignKeys(final TableAssociationType tableAssociationType) {
        final List<ForeignKey> foreignKeysList = new ArrayList<ForeignKey>(foreignKeys.values());
        if (tableAssociationType != null
                && tableAssociationType != TableAssociationType.all) {
            for (final Iterator<ForeignKey> iterator = foreignKeysList.iterator(); iterator.hasNext();) {
                final ForeignKey mutableForeignKey = iterator.next();
                boolean isExportedKey = false;
                boolean isImportedKey = false;
                for (final ForeignKeyColumnReference columnReference : mutableForeignKey.getColumnReferences()) {
                    if (columnReference.getPrimaryKeyColumn().getParent().equals(this)) {
                        isExportedKey = true;
                    }
                    if (columnReference.getForeignKeyColumn().getParent().equals(this)) {
                        isImportedKey = true;
                    }
                }
                switch (tableAssociationType) {
                    case exported:
                        if (!isExportedKey) {
                            iterator.remove();
                        }
                        break;
                    case imported:
                        if (!isImportedKey) {
                            iterator.remove();
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        return foreignKeysList;
    }



}
