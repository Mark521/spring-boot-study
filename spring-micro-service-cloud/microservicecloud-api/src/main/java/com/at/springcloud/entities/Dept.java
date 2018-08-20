package com.at.springcloud.entities;/**
 * Created by thinkpad on 2018/8/16.
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author jianyu.liang
 * @create 2018-08-16 10:52
 **/
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
//链式写法
@Accessors(chain = true)
public class Dept implements Serializable {
    private Long deptno;
    private String dname;
    private String db_source;

    public Dept(String dname){
        super();
        this.dname = dname;
    }

    public static void main(String[] args) {
        Dept dept = new Dept();
        dept.setDeptno(11L);

    }

}
