package com.at.springcloud;/**
 * Created by thinkpad on 2018/8/16.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author jianyu.liang
 * @create 2018-08-16 12:08
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.at.springcloud"})
@ComponentScan("com.at.springcloud")
public class DeptConsumer80Feign_App {
    public static void main(String[] args) {
        SpringApplication.run(DeptConsumer80Feign_App.class, args);
    }
}
