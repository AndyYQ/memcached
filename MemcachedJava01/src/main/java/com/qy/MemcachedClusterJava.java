package com.qy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemcachedClusterJava {
    public static void main(String[] args) throws Exception{
        List<String> servers = new ArrayList<>();
        servers.add("192.168.245.132:11211");
        servers.add("192.168.245.132:11212");
        servers.add("192.168.245.132:11213");
        SpyMemcachedManager spyMemcachedManager = new SpyMemcachedManager(servers);
        // 连接Memecached集群
        spyMemcachedManager.connect();
        // 打印集群状态信息
        spyMemcachedManager.printStats();
        spyMemcachedManager.set("language", "Java", 0);
        Object value = spyMemcachedManager.asyncGet("language");
        System.out.println(value);  // Java
        value = spyMemcachedManager.increment("num", 2, 4, 0);
        System.out.println(value);  // 4
        value = spyMemcachedManager.increment("num", 2, 4, 0);
        System.out.println(value);  // 6
        Map<String, Object> multiValues = spyMemcachedManager.getMulti(new String[]{"language", "num"});
        for(Map.Entry<String,Object> entry : multiValues.entrySet()){
            System.out.println("key ="+entry.getKey()+",value="+entry.getValue());
        }
        spyMemcachedManager.disConnect();
    }
}
