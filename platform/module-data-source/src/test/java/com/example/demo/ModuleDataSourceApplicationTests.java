package com.example.demo;

import mark.component.dbdialect.DBServices;
import mark.component.dbdialect.MetaDialect;
import mark.moduledatasource.constans.DatabaseType;
import mark.moduledatasource.util.DruidUtil;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ModuleDataSourceApplicationTests {

    @Test
    void contextLoads() {
        String jdbcURL = "jdbc:mysql://172.16.1.95:3306/test_mysql?characterEncoding=UTF-8&useSSL=false";
        String userName = "dsesusr";
        String pwd = "1";
        String schema = "test_mysql";
        DatabaseType dbType = DatabaseType.MySQL;

        Connection connection = DruidUtil.getConnection(dbType.getName(), jdbcURL, userName, pwd);

        MetaDialect metaDialect = DBServices.instance().lookup(connection, MetaDialect.class);

        Integer tableCount = metaDialect.getTableCount(connection, schema, "95_test_10");

        System.out.println(tableCount);

    }



}
