package com.mark.demo.model;/**
 * Created by thinkpad on 2018/7/12.
 */

/**
 * @author jianyu.liang
 * @create 2018-07-12 11:52
 **/
public class User {

    private Integer id ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String name = "mark";
    private Integer age = 1;


    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.age = 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
