package mark.component.dbdialect.def;

import mark.component.dbmodel.constans.*;
import mark.component.dbmodel.model.*;
import mark.component.dbmodel.util.MetadataResultSet;
import mark.component.core.exception.BaseException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class RoutineRetriever extends AbstractRetriever {

    private static final Logger LOGGER = Logger.getLogger(RoutineRetriever.class.getName());

    public RoutineRetriever(final Connection connection) {
        super(connection);
    }

    public Collection<FunctionColumn> retrieveFunctionColumns(final Function function, final long retrieveRule)
            throws BaseException {
        MetadataResultSet results = null;
        int ordinalNumber = 0;
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        Schema schema = function.getSchema();
        String catalogName = null;
        String schemaName = null;
        if (schema != null) {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        try {
            results = new MetadataResultSet(getMetaData().getFunctionColumns(unquoted(catalogName),
                    unquoted(schemaName),
                    unquoted(function.getName()), 
                    null));

            while (results.next()) {
                //final String columnCatalogName = quoted(results.getString("FUNCTION_CAT"),quoted);
                //final String columnSchemaName = quoted(results.getString("FUNCTION_SCHEM"),quoted);
                //final String functionName = quoted(results.getString("FUNCTION_NAME"),quoted);
                final String columnName = quoted(results.getString("COLUMN_NAME"), quoted);
                // String specificName = quoted(results.getString("SPECIFIC_NAME"),quoted);

                final FunctionColumn column = new FunctionColumn(function, columnName);
                //final String columnFullName = column.getFullName();
                final short columnType = results.getShort("COLUMN_TYPE", (short) 0);
                final int dataType = results.getInt("DATA_TYPE", 0);
                final String typeName = results.getString("TYPE_NAME");
                final int length = results.getInt("LENGTH", 0);
                final int precision = results.getInt("PRECISION", 0);
                final boolean isNullable = results.getShort("NULLABLE",
                        (short) DatabaseMetaData.functionNullableUnknown) == (short) DatabaseMetaData.functionNullable;
                final String remarks = results.getString("REMARKS");
                column.setOrdinalPosition(ordinalNumber++);
                column.setFunctionColumnType(FunctionColumnType.valueOf(columnType));
                column.setType(lookupOrCreateColumnDataType(function.getSchema(), dataType, typeName));
                column.setPrecision(length);
                column.setPrecision(precision);
                column.setNullable(isNullable);
                column.setRemarks(remarks);

                column.addAttributes(results.getAttributes());

                function.addColumn(column);
            }
        } catch (final AbstractMethodError e) {
            LOGGER.log(Level.WARNING, "JDBC driver does not support retrieving functions", e);
        } catch (final SQLFeatureNotSupportedException e) {
            LOGGER.log(Level.WARNING, "JDBC driver does not support retrieving functions", e);
        } catch (final SQLException e) {
            throw new BaseException("Could not retrieve columns for function " + function, e);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }
        return function.getColumns();
    }

    public Collection<FunctionColumn> retrieveFunctionColumns(String catalogName, String schemaName, String functionName, final long retrieveRule) throws BaseException {
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        Schema schema = new Schema(quoted(catalogName, quoted), quoted(schemaName, quoted));
        Function function = new Function(schema, functionName);
        return retrieveFunctionColumns(function, retrieveRule);
    }

    /**
     * 获取函数
     * @param catalogName
     * @param schemaName
     * @param routineInclusionRule
     * @return
     * @throws SQLException 
     */
    public List<Function> retrieveFunctions(final String catalogName,
            final String schemaName,
            String functionNamePattern,
            final long retrieveRule)
            throws BaseException {

        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        boolean regex = RetrieveRule.REGEX == (RetrieveRule.REGEX & retrieveRule);
        final List<Function> routines = new ArrayList<Function>();
        MetadataResultSet results = null;
        Pattern pattern = null;
        if (functionNamePattern == null || functionNamePattern.isEmpty()) {
            functionNamePattern = null;
            regex = false;
        } else if (regex) {
            pattern = Pattern.compile(functionNamePattern);
            functionNamePattern = null;
        } else {
            functionNamePattern = unquoted(functionNamePattern);
        }
        try {
            results = new MetadataResultSet(getMetaData().getFunctions(
                    unquoted(catalogName),
                    unquoted(schemaName),
                    functionNamePattern));

            while (results.next()) {
                String functionName = results.getString("FUNCTION_NAME");
                if (regex) {
                    Matcher matcher = pattern.matcher(functionName);
                    if (!matcher.matches()) {
                        continue;
                    }
                }
                functionName = quoted(functionName, quoted);

                final String functionCatalog = quoted(results.getString("FUNCTION_CAT"), quoted);
                final String functionSchema = quoted(results.getString("FUNCTION_SCHEM"), quoted);

                final short functionType = results.getShort("FUNCTION_TYPE", (short) FunctionReturnType.unknown.getId());
                final String remarks = results.getString("REMARKS");
                final String specificName = results.getString("SPECIFIC_NAME");

                final Schema schema = new Schema(functionCatalog, functionSchema);
                final Function function = new Function(schema, functionName);
                function.setType(FunctionReturnType.valueOf(functionType));
                function.setSpecificName(specificName);
                function.setRemarks(remarks);
                function.addAttributes(results.getAttributes());
                routines.add(function);
            }
        } catch (SQLException ex) {
            throw new BaseException(ex);
        } catch (final AbstractMethodError e) {
            LOGGER.log(Level.WARNING, "JDBC driver does not support retrieving functions", e);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }

        return routines;
    }

    public Collection<ProcedureColumn> retrieveProcedureColumns(final Procedure procedure, final long retrieveRule) throws BaseException {
        MetadataResultSet results = null;
        int ordinalNumber = 0;
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        Schema schema = procedure.getSchema();
        String catalogName = null;
        String schemaName = null;
        if (schema != null) {
            catalogName = schema.getCatalogName();
            schemaName = schema.getName();
        }
        try {
            results = new MetadataResultSet(getMetaData().getProcedureColumns(unquoted(catalogName),
                    unquoted(schemaName),
                    unquoted(procedure.getName()),
                    null));

            while (results.next()) {
//                final String columnCatalogName = quoted(results.getString("PROCEDURE_CAT"),quoted);
//                final String columnSchemaName = quoted(results.getString("PROCEDURE_SCHEM"),quoted);
//                final String procedureName = quoted(results.getString("PROCEDURE_NAME"),quoted);
                final String columnName = quoted(results.getString("COLUMN_NAME"), quoted);
//               final String specificName = quoted(results.getString("SPECIFIC_NAME"),quoted);

                final ProcedureColumn column = new ProcedureColumn(procedure, columnName);
                //final String columnFullName = column.getFullName();
                final short columnType = results.getShort("COLUMN_TYPE", (short) 0);
                final int dataType = results.getInt("DATA_TYPE", 0);
                final String typeName = results.getString("TYPE_NAME");
                final int length = results.getInt("LENGTH", 0);
                final int precision = results.getInt("PRECISION", 0);
                final boolean isNullable = results.getShort("NULLABLE",
                        (short) DatabaseMetaData.procedureNullableUnknown) == (short) DatabaseMetaData.procedureNullable;
                final String remarks = results.getString("REMARKS");
                column.setOrdinalPosition(ordinalNumber++);
                column.setProcedureColumnType(ProcedureColumnType.valueOf(columnType));
                column.setType(lookupOrCreateColumnDataType(procedure.getSchema(), dataType, typeName));
                column.setPrecision(length);
                column.setPrecision(precision);
                column.setNullable(isNullable);
                column.setRemarks(remarks);

                column.addAttributes(results.getAttributes());

                procedure.addColumn(column);
            }
        } catch (final SQLException e) {
            throw new BaseException("Could not retrieve columns for procedure " + procedure, e);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }
        return procedure.getColumns();
    }

    public Collection<ProcedureColumn> retrieveProcedureColumns(String catalogName, String schemaName, String procedureName, final long retrieveRule) throws BaseException {
        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        Schema schema = new Schema(quoted(catalogName, quoted), quoted(schemaName, quoted));
        Procedure procedure = new Procedure(schema, procedureName);
        return retrieveProcedureColumns(procedure, retrieveRule);
    }

    public List<Procedure> retrieveProcedures(final String catalogName,
            final String schemaName,
            String procedureNamePattern,
            final long retrieveRule)
            throws BaseException {

        boolean quoted = RetrieveRule.QUOTED == (RetrieveRule.QUOTED & retrieveRule);
        boolean regex = RetrieveRule.REGEX == (RetrieveRule.REGEX & retrieveRule);
        final List<Procedure> procedures = new ArrayList<Procedure>();
        MetadataResultSet results = null;
        Pattern pattern = null;
        if (procedureNamePattern == null || procedureNamePattern.isEmpty()) {
            procedureNamePattern = null;
            regex = false;
        } else if (regex) {
            pattern = Pattern.compile(procedureNamePattern);
            procedureNamePattern = null;
        } else {
            procedureNamePattern = unquoted(procedureNamePattern);
        }
        try {
            results = new MetadataResultSet(getMetaData().getProcedures(
                    unquoted(catalogName),
                    unquoted(schemaName),
                    procedureNamePattern));

            while (results.next()) {

                String procedureName = results.getString("PROCEDURE_NAME");
                if (regex) {
                    Matcher matcher = pattern.matcher(procedureName);
                    if (!matcher.matches()) {
                        continue;
                    }
                }
                procedureName = quoted(procedureName, quoted);

                final String procedureCatalog = quoted(results.getString("PROCEDURE_CAT"), quoted);
                final String procedureSchema = quoted(results.getString("PROCEDURE_SCHEM"), quoted);

                final short procedureType = results.getShort("PROCEDURE_TYPE", (short) ProcedureReturnType.unknown.getId());
                final String remarks = results.getString("REMARKS");
                final String specificName = results.getString("SPECIFIC_NAME");

                final Schema schema = new Schema(catalogName, schemaName);
                final Procedure procedure = new Procedure(schema, procedureName);
                procedure.setType(ProcedureReturnType.valueOf(procedureType));
                procedure.setSpecificName(specificName);
                procedure.setRemarks(remarks);
                procedure.addAttributes(results.getAttributes());

                procedures.add(procedure);
            }
        } catch (SQLException ex) {
            throw new BaseException(ex);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        }

        return procedures;
    }
}
