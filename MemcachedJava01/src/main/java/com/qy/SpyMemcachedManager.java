package com.qy;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SpyMemcachedManager {
    private List<String> servers;   // 192.168.245.132:11211
    private MemcachedClient memClient;
    public static int DEFAULT_TIMEOUT = 5;
    public static TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;

    public SpyMemcachedManager(List<String> servers) {
        this.servers = servers;
    }

    public void connect() throws IOException {
        if (memClient != null) {
            return;
        }
        // 连接Memcached集群
        memClient = new MemcachedClient(AddrUtil.getAddresses(servers));
    }

    public void disConnect() {
        if (memClient == null) {
            return;
        }
        memClient.shutdown();
    }

    public boolean set(String key, Object value, int expire) throws Exception {
        Future<Boolean> f = memClient.set(key, expire, value);
        return getBooleanValue(f);
    }

    public Object get(String key) {
        return memClient.get(key);
    }

    // 异步获取
    public Object asyncGet(String key) {
        Object obj = null;
        Future<Object> f = memClient.asyncGet(key);
        try {
            obj = f.get(SpyMemcachedManager.DEFAULT_TIMEOUT,
                    SpyMemcachedManager.DEFAULT_TIMEUNIT);
        } catch (Exception e) {
            f.cancel(false);
        }
        return obj;
    }

    public boolean add(String key, Object value, int expire) {
        Future<Boolean> f = memClient.add(key, expire, value);
        return getBooleanValue(f);
    }

    public boolean replace(String key, Object value, int expire) {
        Future<Boolean> f = memClient.replace(key, expire, value);
        return getBooleanValue(f);
    }

    public boolean delete(String key) {
        Future<Boolean> f = memClient.delete(key);
        return getBooleanValue(f);
    }

    public boolean flush() {
        Future<Boolean> f = memClient.flush();
        return getBooleanValue(f);
    }

    public Map<String, Object> getMulti(Collection<String> keys) {
        return memClient.getBulk(keys);
    }

    public Map<String, Object> getMulti(String[] keys) {
        return memClient.getBulk(keys);
    }

    public Map<String, Object> asyncGetMulti(Collection<String> keys) {
        Map map = null;
        Future<Map<String, Object>> f = memClient.asyncGetBulk(keys);
        try {
            map = f.get(SpyMemcachedManager.DEFAULT_TIMEOUT,
                    SpyMemcachedManager.DEFAULT_TIMEUNIT);
        } catch (Exception e) {
            f.cancel(false);
        }
        return map;
    }

    public Map<String, Object> asyncGetMulti(String keys[]) {
        Map map = null;
        Future<Map<String, Object>> f = memClient.asyncGetBulk(keys);
        try {
            map = f.get(SpyMemcachedManager.DEFAULT_TIMEOUT,
                    SpyMemcachedManager.DEFAULT_TIMEUNIT);
        } catch (Exception e) {
            f.cancel(false);
        }
        return map;
    }

    // 当key不存在时，为defaultValue，key存在时，value+by
    public long increment(String key, int by, long defaultValue, int expire) {
        return memClient.incr(key, by, defaultValue, expire);
    }

    public long increment(String key, int by) {
        return memClient.incr(key, by);
    }

    public long decrement(String key, int by, long defaultValue, int expire) {
        return memClient.decr(key, by, defaultValue, expire);
    }

    public long decrement(String key, int by) {
        return memClient.decr(key, by);
    }

    public long asyncIncrement(String key, int by) {
        Future<Long> f = memClient.asyncIncr(key, by);
        return getLongValue(f);
    }

    public long asyncDecrement(String key, int by) {
        Future<Long> f = memClient.asyncDecr(key, by);
        return getLongValue(f);
    }

    private boolean getBooleanValue(Future<Boolean> f) {
        try {
            Boolean bool = f.get(SpyMemcachedManager.DEFAULT_TIMEOUT,
                    SpyMemcachedManager.DEFAULT_TIMEUNIT);
            return bool.booleanValue();
        } catch (Exception e) {
            f.cancel(false);
            return false;
        }
    }

    private long getLongValue(Future<Long> f) {
        try {
            Long l = f.get(SpyMemcachedManager.DEFAULT_TIMEOUT,
                    SpyMemcachedManager.DEFAULT_TIMEUNIT);
            return l.longValue();
        } catch (Exception e) {
            f.cancel(false);
        }
        return -1;
    }

    public void printStats() throws IOException {
        printStats(null);
    }

    public void printStats(OutputStream stream) throws IOException {
        Map<SocketAddress,Map<String,String>> statMap = memClient.getStats();
        if(stream == null){
            stream = System.out;
        }
        Set<SocketAddress> socketAddresses = statMap.keySet();
        StringBuffer stringBuffer = new StringBuffer();
        for (SocketAddress address: socketAddresses) {
            stringBuffer.append(address.toString()+"\n");
            Map<String,String> stat = statMap.get(address);
            for (Map.Entry<String,String> entry:stat.entrySet()) {
                stringBuffer.append("key="+entry.getKey()+",value="+entry.getValue()+"\n");
            }
            stringBuffer.append("\n");
        }
        stream.write(stringBuffer.toString().getBytes());
        stream.flush();
    }
}
