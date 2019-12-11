package mark.component.dbmodel.constans;

import mark.component.dbmodel.model.Schema;

/**
 * Represents a database object.
 */
public interface DatabaseObject extends NamedObject {

    Schema getSchema();
}
