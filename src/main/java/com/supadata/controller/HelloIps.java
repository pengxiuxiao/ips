package com.supadata.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: HelloIps
 * @Description:
 * @Author: pxx
 * @Date: 2019/3/26 11:13
 * @Description:
 */
@RestController
@RequestMapping("/hi")
public class HelloIps {

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
}
