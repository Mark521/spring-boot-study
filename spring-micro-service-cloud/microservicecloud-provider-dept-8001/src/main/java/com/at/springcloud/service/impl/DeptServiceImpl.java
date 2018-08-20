package com.at.springcloud.service.impl;/**
 * Created by thinkpad on 2018/8/16.
 */

import com.at.springcloud.dao.DeptDao;
import com.at.springcloud.entities.Dept;
import com.at.springcloud.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jianyu.liang
 * @create 2018-08-16 11:37
 **/
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptDao;

    @Override
    public boolean add(Dept dept) {
        return deptDao.addDept(dept);
    }

    @Override
    public Dept get(Long id) {
        return deptDao.findById(id);
    }

    @Override
    public List<Dept> list() {
        return deptDao.findAll();
    }
}
