package com.wumu.rpc.simplerpc.boot;

/**
 * Created by dydy on 2019/4/28.
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello() {
        return "hello";
    }

}
