package mark.moduledatasource.service.impl;

import mark.component.dbdialect.DBServices;
import mark.component.dbdialect.DDLDialect;
import mark.component.dbdialect.MetaDialect;
import mark.component.dbmodel.constans.RetrieveRule;
import mark.component.dbmodel.model.Table;
import mark.component.dbmodel.qo.DataSourceQo;
import mark.component.dbmodel.service.DataSourceService;
import mark.moduledatasource.util.DruidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.Collection;

@Service
public class DataSourceServiceImpl implements DataSourceService {

    private Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);

    @Override
    public Integer queryTableCount(DataSourceQo qo) {
        Connection connection = DruidUtil.getConnection(qo.getDbType().getName(), qo.getJdbcURL(), qo.getUserName(), qo.getPasswd());
        MetaDialect metaDialect = DBServices.instance().lookup(connection, MetaDialect.class);
        Integer tableCount = metaDialect.getTableCount(connection, qo.getSchema(), qo.getTableName());
        return tableCount;
    }

    @Override
    public Collection<Table> queryTable(DataSourceQo qo) {
        Collection<Table> tables;
        Connection connection = DruidUtil.getConnection(qo.getDbType().getName(), qo.getJdbcURL(), qo.getUserName(), qo.getPasswd());
        MetaDialect metaDialect = DBServices.instance().lookup(connection, MetaDialect.class);
        DDLDialect dDLDialect = DBServices.instance().lookup(connection, DDLDialect.class);

        if(qo.isPaging()){
            tables = metaDialect.getTablesByPage(connection,qo.getSchema(), qo.getTableName(),qo.getLimit(), qo.getOffset(),RetrieveRule.ALL);
        }else {
            tables = metaDialect.getTables(connection, qo.getSchema(), RetrieveRule.ALL);
        }
        tables.forEach(e->{
            logger.info(dDLDialect.createTableSQL(e));
        });
        return tables;
    }

    @Override
    public boolean createTable(DataSourceQo qo, Table table) {
        return false;
    }
}
