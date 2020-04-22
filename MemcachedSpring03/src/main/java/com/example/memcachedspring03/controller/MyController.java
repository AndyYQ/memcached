package com.example.memcachedspring03.controller;

import com.whalin.MemCached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {

    @Autowired
    private MemCachedClient memCachedClient;

    @ResponseBody
    @RequestMapping("/getKey")
    public String getKey() {
        memCachedClient.set("memcachedKey", "hello Memcached !!!!");
        String value = (String) memCachedClient.get("memcachedKey");
        return value;
    }
}
