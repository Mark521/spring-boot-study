package com.mark.demo.controller;/**
 * Created by thinkpad on 2018/7/5.
 */

import com.mark.demo.model.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jianyu.liang
 * @create 2018-07-05 15:13
 **/

@RestController
public class HelloController
{

    @ApiOperation(value="获取用户详细信息",notes = "根据URL", response = User.class)
    @ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "用户名称", required = true, dataType = "String", paramType = "path"), @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path")})
    @RequestMapping(value = "hello/{id}/{name}", method = RequestMethod.GET)
    public User hello(@PathVariable("id") Integer id,@PathVariable("name") String name ){
        return new User(id, name);
    }

    @RequestMapping(value = "gotoHello")
    public ModelAndView gotoHello(){
        return new ModelAndView("test");
    }
}
