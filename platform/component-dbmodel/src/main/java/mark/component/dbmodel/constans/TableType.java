package mark.component.dbmodel.constans;

import java.io.Serializable;

/**
 * An enumeration wrapper around JDBC table types.
 */
public enum TableType implements Serializable {

    unknown,
    system_table,
    global_temporary,
    local_temporary,
    table,
    view,
    alias,
    synonym,
    manual_operation//人工新建或导入表（非数据源自动获取）
}
