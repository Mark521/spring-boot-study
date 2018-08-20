package com.at.springcloud.dao;/**
 * Created by thinkpad on 2018/8/16.
 */

import com.at.springcloud.entities.Dept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author jianyu.liang
 * @create 2018-08-16 11:28
 **/
@Mapper
//实现使用mybatis的xml
public interface DeptDao {

    public boolean addDept(Dept dept);
    public Dept findById(Long id);
    public List<Dept> findAll();
}
