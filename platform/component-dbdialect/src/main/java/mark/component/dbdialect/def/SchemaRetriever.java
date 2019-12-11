package mark.component.dbdialect.def;

import mark.component.dbmodel.model.Schema;
import mark.component.dbmodel.util.MetadataResultSet;
import mark.component.core.exception.BaseException;
import mark.component.dbdialect.DBServices;
import mark.component.dbdialect.MetaDialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

final class SchemaRetriever extends AbstractRetriever {

    private static final Logger LOGGER = Logger.getLogger(SchemaRetriever.class.getName());
    private final boolean supportsCatalog;
    private final boolean supportsSchema;

    public SchemaRetriever(final Connection connection) throws BaseException {
        super(connection);
        MetaDialect metaDialect = DBServices.instance().lookup(connection, MetaDialect.class);
        supportsCatalog = metaDialect.supportCatalog();
        supportsSchema = metaDialect.supportSchema();
    }

    private Set<String> retrieveAllCatalogs() throws BaseException {
        final Set<String> catalogNames = new HashSet<String>();

        if (supportsCatalog) {
            try {
                final List<String> metaDataCatalogNames = readResultsVector(getMetaData().getCatalogs());
                for (final String catalogName : metaDataCatalogNames) {
                    catalogNames.add(quoted(catalogName));
                }
            } catch (final SQLException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
            LOGGER.log(Level.FINER, "Retrieved catalogs: {0}", catalogNames);
        }

        return catalogNames;
    }

    public Collection<Schema> retrieveAllSchemas(final long retrieveRule) throws BaseException {
        final Set<Schema> schemaRefs = new HashSet<Schema>();
        final Set<String> allCatalogNames = retrieveAllCatalogs();
        if (supportsSchema) {
            MetadataResultSet results = null;
            try {
                results = new MetadataResultSet(getMetaData().getSchemas());
                while (results.next()) {
                    final String catalogName;
                    if (supportsCatalog) {
                        catalogName = quoted(results.getString("TABLE_CATALOG"));
                    } else {
                        catalogName = null;
                    }
                    final String schemaName = quoted(results.getString("TABLE_SCHEM"));
                    LOGGER.log(Level.FINER, String.format("Retrieving schema: %s --> %s",
                            catalogName,
                            schemaName));
                    if (catalogName == null) {
                        if (allCatalogNames.isEmpty()) {
                            schemaRefs.add(new Schema(null, schemaName));
                        } else {
                            for (final String expectedCatalogName : allCatalogNames) {
                                schemaRefs.add(new Schema(expectedCatalogName, schemaName));
                            }
                        }
                    } else {
                        schemaRefs.add(new Schema(catalogName, schemaName));
                    }
                }
            } catch (final SQLException ex) {
                throw new BaseException("",ex);
            } finally {
                try {
                    results.close();
                } catch (SQLException ex) {
                }
            }
        } else {
            for (final String catalogName : allCatalogNames) {
                LOGGER.log(Level.FINER, String.format("Retrieving schema: %s --> %s", catalogName, null));
                schemaRefs.add(new Schema(catalogName, null));
            }
        }

        return schemaRefs;
    }
}
