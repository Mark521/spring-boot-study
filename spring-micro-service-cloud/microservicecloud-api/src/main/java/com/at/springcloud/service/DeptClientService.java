package com.at.springcloud.service;/**
 * Created by thinkpad on 2018/8/17.
 */

import com.at.springcloud.entities.Dept;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author jianyu.liang
 * @create 2018-08-17 12:32
 **/
@FeignClient(value = "MICROSERVICECLOUD-PROVIDER-DEPT-8001")
public interface DeptClientService {
    @RequestMapping(value="/dept/add", method = RequestMethod.POST)
    public boolean add(Dept dept);

    @RequestMapping(value="/dept/get/{id}", method = RequestMethod.GET)
    public Dept get(@PathVariable("id") Long id);

    @RequestMapping(value="/dept/list", method = RequestMethod.GET)
    public List<Dept> list();
}
