package mark.moduledatasource.controller;

import mark.component.dbmodel.constans.DatabaseType;
import mark.component.dbmodel.model.Table;
import mark.component.dbmodel.qo.DataSourceQo;
import mark.component.dbmodel.service.DataSourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class SourceInfoController {

    @Autowired
    private DataSourceService dataSourceService;

    private DataSourceQo getMySQLObj(String schema, String tableName){
        String jdbcURL = "jdbc:mysql://172.16.1.95:3306/test_mysql?characterEncoding=UTF-8&useSSL=false";
        String userName = "dsesusr";
        String pwd = "1";
        if(StringUtils.isBlank(schema)) {
            schema = "test_mysql";
        }
        //tableName = "95_test_10";
        DatabaseType dbType = DatabaseType.MySQL;
        DataSourceQo qo = new DataSourceQo();
        qo.setDbType(dbType);
        qo.setJdbcURL(jdbcURL);
        qo.setUserName(userName);
        qo.setPasswd(pwd);
        qo.setSchema(schema);
        qo.setTableName(StringUtils.trim(tableName) + "%");
        qo.setPaging(true);
        return qo;
    }

    private DataSourceQo getORCALbj(String schema, String tableName){
        String jdbcURL = "jdbc:oracle:thin:@//172.16.1.78:1521/test";
        String userName = "ADQCZW_TEST";
        String pwd = "ADQCZW_TEST";
        if(StringUtils.isBlank(schema)) {
            schema = "ADQGAJ_TEST";
        }
        //tableName = "95_test_10";
        DatabaseType dbType = DatabaseType.Oracle;
        DataSourceQo qo = new DataSourceQo();
        qo.setDbType(dbType);
        qo.setJdbcURL(jdbcURL);
        qo.setUserName(userName);
        qo.setPasswd(pwd);
        qo.setSchema(schema);
        qo.setTableName(StringUtils.trim(tableName) + "%");
        qo.setPaging(true);
        return qo;
    }

    @RequestMapping("/queryTableCount/{schema}/{tableName}")
    public String queryTableCount(@PathVariable String schema,@PathVariable String tableName) {
        DataSourceQo qo = getMySQLObj(schema,tableName);
        Integer tableCount = dataSourceService.queryTableCount(qo);
        return tableCount.toString();
    }

    @RequestMapping("/queryTables/{schema}/{tableName}")
    public String queryTables(@PathVariable String schema,@PathVariable String tableName){
        DataSourceQo qo = getMySQLObj(schema, tableName);
        Collection<Table> tables = dataSourceService.queryTable(qo);
        StringBuilder tableNames = new StringBuilder();
        tables.forEach(e -> tableNames.append(e.getName() + ","));
        return tableNames.toString();
    }

}
