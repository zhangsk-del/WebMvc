package com.web.controller;

import com.web.annotation.Param;
import com.web.annotation.RequestMapping;

public class TestController {

    @RequestMapping("/test.do")
    public String test(@Param("name") String name, @Param("pass") String pass) {

        System.out.println("接收到参数啦" + name + "---" + pass);

        return "success";
    }


}
