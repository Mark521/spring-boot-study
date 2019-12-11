package mark.component.dbmodel.service;

import mark.component.dbmodel.qo.DataSourceQo;
import mark.component.dbmodel.model.Table;

import java.util.Collection;

public interface DataSourceService {

    /**
     * 查询库表数据量信息
     * 某张表
     * @param qo
     * @return
     */
    Integer queryTableCount(DataSourceQo qo);

    /**
     * 查询表信息
     *
     * @param qo
     * @return
     */
    Collection<Table> queryTable(DataSourceQo qo);

    /**
     * 自动创建表
     * @param qo
     * @param table
     * @return
     */
    boolean createTable(DataSourceQo qo, Table table);


}
