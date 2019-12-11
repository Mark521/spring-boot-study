package mark.component.dbdialect;

import mark.component.core.exception.BaseException;

import java.sql.Connection;

public interface DBInitialize {

    void initialize(Connection connection) throws BaseException;
}
