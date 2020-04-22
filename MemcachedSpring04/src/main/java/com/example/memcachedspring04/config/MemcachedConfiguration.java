package com.example.memcachedspring04.config;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

@Configuration
public class MemcachedConfiguration {

    @Value("${memcached.servers}")
    private List<String> servers;

    @Bean
    public MemcachedClient memcachedClient() {
        MemcachedClient memcachedClient = null;
        try {
           memcachedClient = new MemcachedClient(AddrUtil.getAddresses(servers));
        } catch(IOException ex){
            ex.printStackTrace();
        }
        return memcachedClient;
    }

}
