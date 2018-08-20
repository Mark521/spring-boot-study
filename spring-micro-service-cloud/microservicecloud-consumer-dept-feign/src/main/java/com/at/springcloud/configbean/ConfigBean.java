package com.at.springcloud.configbean;/**
 * Created by thinkpad on 2018/8/16.
 */

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RetryRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author jianyu.liang
 * @create 2018-08-16 11:55
 **/
@Configuration//注解版配置application.xml
public class ConfigBean {
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public IRule myRule(){
        return new RetryRule();
    }
}
