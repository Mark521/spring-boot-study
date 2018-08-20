package com.at.springcloud.service;/**
 * Created by thinkpad on 2018/8/16.
 */

import com.at.springcloud.entities.Dept;

import java.util.List;

/**
 * @author jianyu.liang
 * @create 2018-08-16 11:36
 **/
public interface DeptService {
    public boolean add(Dept dept);
    public Dept get(Long id);
    public List<Dept> list();
}
