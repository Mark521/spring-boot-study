package mark.component.dbdialect.def;

import mark.component.dbmodel.util.ObjectToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

final class InformationSchemaViews implements Serializable {

    private static final long serialVersionUID = 3587581365346059044L;
    public static final String KEY_INFORMATION_SCHEMA_VIEWS = "select.INFORMATION_SCHEMA.VIEWS";
    public static final String KEY_INFORMATION_SCHEMA_VIEW = "select.INFORMATION_SCHEMA.VIEW";
    public static final String KEY_INFORMATION_SCHEMA_TRIGGERS = "select.INFORMATION_SCHEMA.TRIGGERS";
    public static final String KEY_INFORMATION_SCHEMA_TABLE_CONSTRAINTS = "select.INFORMATION_SCHEMA.TABLE_CONSTRAINTS";
    public static final String KEY_INFORMATION_SCHEMA_ROUTINE = "select.INFORMATION_SCHEMA.ROUTINE";
    public static final String KEY_INFORMATION_SCHEMA_ROUTINES = "select.INFORMATION_SCHEMA.ROUTINES";
    public static final String KEY_INFORMATION_SCHEMA_CHECK_CONSTRAINTS = "select.INFORMATION_SCHEMA.CHECK_CONSTRAINTS";
    public static final String KEY_INFORMATION_SCHEMA_SCHEMATA = "select.INFORMATION_SCHEMA.SCHEMATA";
    /*查询单个同义词*/
    public static final String KEY_INFORMATION_SCHEMA_SYNONYM = "select.INFORMATION_SCHEMA.SYNONYM";
    /*查询所有同义词*/
    public static final String KEY_INFORMATION_SCHEMA_SYNONYMS = "select.INFORMATION_SCHEMA.SYNONYMS";
    public static final String KEY_ADDITIONAL_TABLE_ATTRIBUTES = "select.ADDITIONAL_TABLE_ATTRIBUTES";
    public static final String KEY_ADDITIONAL_COLUMN_ATTRIBUTES = "select.ADDITIONAL_COLUMN_ATTRIBUTES";
    private final Map<String, String> informationSchemaQueries = new HashMap<String, String>();

    /**
     * Creates empty information schema views.
     */
    public InformationSchemaViews() {
        init(null);
    }

    /**
     * Information schema views from a map.
     * 
     * @param informationSchemaViewsSql
     *        Map of information schema view definitions.
     */
    public InformationSchemaViews(final Map<String, String> informationSchemaViewsSql) {
        init(informationSchemaViewsSql);
    }

    public InformationSchemaViews(final Properties informationSchemaViewsSql) {
        Map<String, String> informations = new HashMap<String, String>();
        for (Map.Entry<Object, Object> entry : informationSchemaViewsSql.entrySet()) {
            informations.put(entry.getKey().toString(), entry.getValue().toString());
        }
        init(informations);
    }

    private void init(final Map<String, String> informationSchemaViewsSql) {
        if (informationSchemaViewsSql != null) {
            final String[] keys = new String[]{
                KEY_INFORMATION_SCHEMA_VIEWS,
                KEY_INFORMATION_SCHEMA_VIEW,
                KEY_INFORMATION_SCHEMA_TRIGGERS,
                KEY_INFORMATION_SCHEMA_TABLE_CONSTRAINTS,
                KEY_INFORMATION_SCHEMA_ROUTINE,
                KEY_INFORMATION_SCHEMA_ROUTINES,
                KEY_INFORMATION_SCHEMA_CHECK_CONSTRAINTS,
                KEY_INFORMATION_SCHEMA_SCHEMATA,
                KEY_INFORMATION_SCHEMA_SYNONYM,
                KEY_INFORMATION_SCHEMA_SYNONYMS,
                KEY_ADDITIONAL_TABLE_ATTRIBUTES,
                KEY_ADDITIONAL_COLUMN_ATTRIBUTES
            };
            for (final String key : keys) {
                if (informationSchemaViewsSql.containsKey(key)) {
                    try {
                        informationSchemaQueries.put(key, informationSchemaViewsSql.get(key));
                    } catch (final IllegalArgumentException e) {
                        // Ignore
                    }
                }
            }
        }
    }

    /**
     * Gets the additional attributes SQL for columns, from the additional
     * configuration.
     * 
     * @return Additional attributes SQL for columns.
     */
    public String getAdditionalColumnAttributesSql() {
        return informationSchemaQueries.get(KEY_ADDITIONAL_COLUMN_ATTRIBUTES);
    }

    /**
     * Gets the additional attributes SQL for tables, from the additional
     * configuration.
     * 
     * @return Additional attributes SQL for tables.
     */
    public String getAdditionalTableAttributesSql() {
        return informationSchemaQueries.get(KEY_ADDITIONAL_TABLE_ATTRIBUTES);
    }

    /**
     * Gets the table check constraints SQL from the additional
     * configuration.
     * 
     * @return Table check constraints SQL.
     */
    public String getCheckConstraintsSql() {
        return informationSchemaQueries.get(KEY_INFORMATION_SCHEMA_CHECK_CONSTRAINTS);
    }

    /**
     * Gets the routine definitions SQL from the additional configuration.
     * 
     * @return Routine defnitions SQL.
     */
    public String getRoutineSql() {
        return informationSchemaQueries.get(KEY_INFORMATION_SCHEMA_ROUTINE);
    }
    
    /**
     * Gets the routine definitions SQL from the additional configuration.
     * 
     * @return Routine defnitions SQL.
     */
    public String getRoutinesSql() {
        return informationSchemaQueries.get(KEY_INFORMATION_SCHEMA_ROUTINES);
    }

    /**
     * Gets the schemata SQL from the additional configuration.
     * 
     * @return Schemata SQL.
     */
    public String getSchemataSql() {
        return informationSchemaQueries.get(KEY_INFORMATION_SCHEMA_SCHEMATA);
    }

    public String getSynonymSql() {
        return informationSchemaQueries.get(KEY_INFORMATION_SCHEMA_SYNONYM);
    }
    
    /**
     * Gets the synonyms SQL from the additional configuration.
     * 
     * @return Synonyms SQL.
     */
    public String getSynonymsSql() {
        return informationSchemaQueries.get(KEY_INFORMATION_SCHEMA_SYNONYMS);
    }

    /**
     * Gets the table constraints SQL from the additional configuration.
     * 
     * @return Table constraints SQL.
     */
    public String getTableConstraintsSql() {
        return informationSchemaQueries.get(KEY_INFORMATION_SCHEMA_TABLE_CONSTRAINTS);
    }

    /**
     * Gets the trigger definitions SQL from the additional configuration.
     * 
     * @return Trigger defnitions SQL.
     */
    public String getTriggersSql() {
        return informationSchemaQueries.get(KEY_INFORMATION_SCHEMA_TRIGGERS);
    }

    /**
     * Gets the view definitions SQL from the additional configuration.
     * 
     * @return View defnitions SQL.
     */
    public String getViewsSql() {
        return informationSchemaQueries.get(KEY_INFORMATION_SCHEMA_VIEWS);
    }
    
    /**
     * Gets the view definitions SQL from the additional configuration.
     * 
     * @return View defnitions SQL.
     */
    public String getViewSql() {
        return informationSchemaQueries.get(KEY_INFORMATION_SCHEMA_VIEW);
    }

    public boolean hasAdditionalColumnAttributesSql() {
        return informationSchemaQueries.containsKey(KEY_ADDITIONAL_COLUMN_ATTRIBUTES);
    }

    public boolean hasAdditionalTableAttributesSql() {
        return informationSchemaQueries.containsKey(KEY_ADDITIONAL_TABLE_ATTRIBUTES);
    }

    public boolean hasCheckConstraintsSql() {
        return informationSchemaQueries.containsKey(KEY_INFORMATION_SCHEMA_CHECK_CONSTRAINTS);
    }

    public boolean hasRoutineSql() {
        return informationSchemaQueries.containsKey(KEY_INFORMATION_SCHEMA_ROUTINE);
    }
    
    public boolean hasRoutinesSql() {
        return informationSchemaQueries.containsKey(KEY_INFORMATION_SCHEMA_ROUTINES);
    }

    public boolean hasSchemataSql() {
        return informationSchemaQueries.containsKey(KEY_INFORMATION_SCHEMA_SCHEMATA);
    }

    public boolean hasSynonymsSql() {
        return informationSchemaQueries.containsKey(KEY_INFORMATION_SCHEMA_SYNONYMS);
    }

    public boolean hasTableConstraintsSql() {
        return informationSchemaQueries.containsKey(KEY_INFORMATION_SCHEMA_TABLE_CONSTRAINTS);
    }

    public boolean hasTriggerSql() {
        return informationSchemaQueries.containsKey(KEY_INFORMATION_SCHEMA_TRIGGERS);
    }

    public boolean hasViewsSql() {
        return informationSchemaQueries.containsKey(KEY_INFORMATION_SCHEMA_VIEWS);
    }
    
    public boolean hasViewSql() {
        return informationSchemaQueries.containsKey(KEY_INFORMATION_SCHEMA_VIEW);
    }

    /**
     * Sets the additional attributes SQL for columns.
     * 
     * @param sql
     *        Additional attributes SQL for columns.
     */
    public void setAdditionalColumnAttributesSql(final String sql) {
        informationSchemaQueries.put(KEY_ADDITIONAL_COLUMN_ATTRIBUTES, sql);
    }

    /**
     * Sets the additional attributes SQL for tables.
     * 
     * @param sql
     *        Additional attributes SQL for tables.
     */
    public void setAdditionalTableAttributesSql(final String sql) {
        informationSchemaQueries.put(KEY_ADDITIONAL_TABLE_ATTRIBUTES, sql);
    }

    /**
     * Sets the table check constraints SQL.
     * 
     * @param sql
     *        Table check constraints SQL.
     */
    public void setCheckConstraintsSql(final String sql) {
        informationSchemaQueries.put(KEY_INFORMATION_SCHEMA_CHECK_CONSTRAINTS, sql);
    }

    /**
     * Sets the procedure definitions SQL.
     * 
     * @param sql
     *        Procedure definitions SQL.
     */
    public void setRoutinesSql(final String sql) {
        informationSchemaQueries.put(KEY_INFORMATION_SCHEMA_ROUTINES, sql);
    }

    /**
     * Sets the schemata SQL.
     * 
     * @param sql
     *        Schemata SQL.
     */
    public void setSchemataSql(final String sql) {
        informationSchemaQueries.put(KEY_INFORMATION_SCHEMA_SCHEMATA, sql);
    }

    /**
     * Sets the synonym SQL.
     * 
     * @param sql
     *        Synonym SQL.
     */
    public void setSynonymSql(final String sql) {
        informationSchemaQueries.put(KEY_INFORMATION_SCHEMA_SYNONYMS, sql);
    }

    /**
     * Sets the table constraints SQL.
     * 
     * @param sql
     *        Table constraints SQL.
     */
    public void setTableConstraintsSql(final String sql) {
        informationSchemaQueries.put(KEY_INFORMATION_SCHEMA_TABLE_CONSTRAINTS, sql);
    }

    /**
     * Sets the trigger definitions SQL.
     * 
     * @param sql
     *        Trigger definitions SQL.
     */
    public void setTriggersSql(final String sql) {
        informationSchemaQueries.put(KEY_INFORMATION_SCHEMA_TRIGGERS, sql);
    }

    /**
     * Sets the view definitions SQL.
     * 
     * @param sql
     *        View defnitions SQL.
     */
    public void setViewsSql(final String sql) {
        informationSchemaQueries.put(KEY_INFORMATION_SCHEMA_VIEWS, sql);
    }
    
    /**
     * Sets the view definitions SQL.
     * 
     * @param sql
     *        View defnitions SQL.
     */
    public void setViewSql(final String sql) {
        informationSchemaQueries.put(KEY_INFORMATION_SCHEMA_VIEW, sql);
    }

    @Override
    public String toString() {
        return ObjectToString.toString(informationSchemaQueries);
    }
}
