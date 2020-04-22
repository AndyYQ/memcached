package com.example.memcachedspring04.controller;

import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {

    @Autowired
    private MemcachedClient memcachedClient;

    @ResponseBody
    @RequestMapping("/getKey")
    public String test(){
        memcachedClient.set("name", 0, "qin");
        String value = (String) memcachedClient.get("name");
        return value;
    }
}
