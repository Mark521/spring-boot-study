package mark.component.dbmodel.model;

import mark.component.dbmodel.util.Multimap;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 数据库模型,描述数据库对象(模式,表，视图,存储过程，函数,同义词等)
 * @author liang>
 */
public final class Database extends AbstractNamedObject {

    private static final long serialVersionUID = 4051323422934251828L;
    private DatabaseInfo databaseInfo;
    private JdbcDriverInfo jdbcDriverInfo;
    private Multimap<ColumnDataType, Integer> jdbc2java = new Multimap<ColumnDataType, Integer>();
    private Multimap<Integer, ColumnDataType> java2jdbc = new Multimap<Integer, ColumnDataType>();
    private ColumnDataTypes columnDataTypes = new ColumnDataTypes();
    private Collection<Schema> schemas = new ArrayList<Schema>();
    private Collection<Table> tables = new ArrayList<Table>();
    private Collection<View> views = new ArrayList<View>();
    private Collection<Procedure> procedures = new ArrayList<Procedure>();
    private Collection<Function> functions = new ArrayList<Function>();
    private Collection<Sequence> sequences = new ArrayList<Sequence>();

    public Database(String name) {
        super(name);
    }
    public Database(){

    }
    public ColumnDataTypes getColumnDataTypes() {
        return columnDataTypes;
    }

    public void setColumnDataTypes(ColumnDataTypes columnDataTypes) {
        this.columnDataTypes = columnDataTypes;
    }

    public DatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }

    public void setDatabaseInfo(DatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }

    public Collection<Function> getFunctions() {
        return functions;
    }
    
    public void addFunctions(Collection<Function> functions) {
        this.functions.addAll(functions);
    }

    public Multimap<Integer, ColumnDataType> getJava2jdbc() {
        return java2jdbc;
    }

    public void setJava2jdbc(Multimap<Integer, ColumnDataType> java2jdbc) {
        this.java2jdbc = java2jdbc;
    }

    public Multimap<ColumnDataType, Integer> getJdbc2java() {
        return jdbc2java;
    }

    public void setJdbc2java(Multimap<ColumnDataType, Integer> jdbc2java) {
        this.jdbc2java = jdbc2java;
    }

    public JdbcDriverInfo getJdbcDriverInfo() {
        return jdbcDriverInfo;
    }

    public void setJdbcDriverInfo(JdbcDriverInfo jdbcDriverInfo) {
        this.jdbcDriverInfo = jdbcDriverInfo;
    }

    public Collection<Procedure> getProcedures() {
        return procedures;
    }

    public void addProcedures(Collection<Procedure> procedures) {
        this.procedures.addAll(procedures);
    }

    public Collection<Schema> getSchemas() {
        return schemas;
    }

    public void addSchemas(Collection<Schema> schemas) {
        this.schemas.addAll(schemas);
    }

    public Collection<Sequence> getSequences() {
        return sequences;
    }

    public void addSequences(Collection<Sequence> sequences) {
        this.sequences.addAll(sequences);
    }

    public Collection<Table> getTables() {
        return tables;
    }

    public void addTables(Collection<Table> tables) {
        this.tables.addAll(tables);
    }

    public Collection<View> getViews() {
        return views;
    }

    public void addViews(Collection<View> views) {
        this.views.addAll(views);
    }
}
