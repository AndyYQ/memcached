package com.qy;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import java.net.InetSocketAddress;

public class MemcachedJava {
    public static void main(String[] args) {
        try {
            // 连接Memcached服务的客户端对象
            MemcachedClient memcachedClient = new MemcachedClient(new InetSocketAddress("192.168.245.132", 11211));
            System.out.println("Connection to server sucessful.");
            // 用于将 value(数据值) 存储在指定的 key(键) 中，如果set的key已经存在，该命令可以更新该key所对应的原来的数据
            OperationFuture<Boolean> future = memcachedClient.set("name", 0, "mike");
            System.out.println("set status:" + future.get());// 输出执行set方法后的状态
            System.out.println("get - " + memcachedClient.get("name"));// 使用get方法获取数据

            // add 对应的key不存在，则添加
            future = memcachedClient.add("memcached", 0, "add");// 添加
            System.out.println("add status:" + future.get());// 输出执行add方法后的状态
            System.out.println("get - " + memcachedClient.get("memcached"));// 获取键对应的值

            // 如果 add 的 key 已经存在，则不会更新数据(过期的 key 会更新)，之前的值将仍然保持相同,执行状态返回false
            future = memcachedClient.add("name", 0, "lisi");
            System.out.println("add status:" + future.get());// 输出执行add方法后的状态
            System.out.println("get - " + memcachedClient.get("name"));// 获取键对应的值

            // replace 命令用于替换已存在的 key(键) 的 value(数据值)。如果 key 不存在，执行状态返回false，替换失败。
            future = memcachedClient.replace("memcached", 0, "memcached replace");
            System.out.println("replace status:" + future.get());// 输出执行replace方法后的状态
            System.out.println("replace - " + memcachedClient.get("memcached"));// 获取键对应的值

            // append 命令用于向已存在 key(键) 的 value(数据值) 后面追加数据 。
            future = memcachedClient.append("memcached", " append");// 对存在的key进行数据添加操作
            System.out.println("append status:" + future.get());// 输出执行 append方法后的状态
            System.out.println("append - " + memcachedClient.get("memcached"));// 获取键对应的值

            // prepend 命令用于向已存在 key(键) 的 value(数据值) 前面追加数据 。
            future = memcachedClient.prepend("memcached", "prepend ");// 对存在的key进行数据添加操作
            System.out.println("prepend status:" + future.get());// 输出执行prepend方法后的状态
            System.out.println("prepend - " + memcachedClient.get("memcached"));// 获取键对应的值

            // CAS（Check-And-Set 或 Compare-And-Swap） 命令用于执行一个"检查并设置"的操作它仅在当前客户端最后一次取值后，该key 对应的值没有被其他客户端修改的情况下，才能够将值写入。
            CASValue casValue = memcachedClient.gets("memcached");// 通过 gets 方法获取 CAS token（令牌）
            System.out.println("CAS token - " + casValue);// 输出 CAS token（令牌） 值
            CASResponse casresp = memcachedClient.cas("memcached", casValue.getCas(), " CAS");// 尝试使用cas方法来更新数据
            System.out.println("CAS Response - " + casresp);// 输出 CAS 响应信息
            System.out.println("CAS - " + memcachedClient.get("memcached"));// 输出值

            // delete 命令用于删除已存在的 key(键)。
            future = memcachedClient.delete("memcached");// 对存在的key进行数据添加操作
            System.out.println("delete status:" + future.get());// 输出执行 delete方法后的状态
            System.out.println("delete - " + memcachedClient.get("memcached"));// 获取键对应的值

            // incr 与 decr 命令用于对已存在的 key(键) 的数字值进行自增或自减操作。incr 与 decr 命令操作的数据必须是十进制的32位无符号整数。
            // 如果 key 不存在返回 NOT_FOUND，如果键的值不为数字，则返回 CLIENT_ERROR，其他错误返回 ERROR。
            future = memcachedClient.set("number", 10, "1000");// 添加数字值
            System.out.println("set status:" + future.get());// 输出执行 set 方法后的状态
            System.out.println("incr - " + memcachedClient.incr("number", 100));// 自增并输出
            System.out.println("decr - " + memcachedClient.decr("number", 101));// 自减并输出

            // 关闭连接
            memcachedClient.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
