package com.qy;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

public class TestMemcached {
    public static void main(String[] args) {
        // 注意集群顺序，set，get操作时集群顺序保持一致
        String[] servers = new String[]{"192.168.245.132:11211", "192.168.245.132:11212", "192.168.245.132:11213"};
        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(servers);
        pool.setFailover(true);
        pool.setInitConn(10);
        pool.setMinConn(5);
        pool.setMaxConn(250);
        pool.setMaintSleep(30);
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setAliveCheck(true);
        pool.initialize();
        MemCachedClient memClient = new MemCachedClient();
//        boolean status = memClient.set("key", "hello");
//        System.out.println(status);
        Object value = memClient.get("key");
        System.out.println(value);
    }
}
