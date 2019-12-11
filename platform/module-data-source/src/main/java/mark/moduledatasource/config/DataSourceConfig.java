package mark.moduledatasource.config;

import mark.moduledatasource.dynamic.DruidDynamicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceConfig {

    @Bean
    public DruidDynamicDataSource druidDynamicDataSource(){
        DruidDynamicDataSource druidDynamicDataSource = new DruidDynamicDataSource();
        Map<Object, Object> map = new HashMap<>();
        druidDynamicDataSource.setTargetDataSources(map);
        return druidDynamicDataSource;
    }
}
