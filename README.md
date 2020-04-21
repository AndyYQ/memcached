# memcached
Java 连接memcached，客户端Memcached-Java-Client和spymemcached客户端的简单使用。
memcached client for java：较早推出的memcached JAVA客户端API，应用广泛，运行比较稳定。 
spymemcached：支持异步，单线程的memcached客户端，用到了java1.5版本的concurrent和nio，存取速度会高于前者，但是稳定性不好，测试中常报timeOut等相关异常。
