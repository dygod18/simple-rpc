package com.wumu.rpc.simplerpc.boot;


import com.wumu.rpc.simplerpc.config.ReferenceConfig;

/**
 * Created by dydy on 2019/4/28.
 */
public class ClientBoot {

    public static void main(String[] args){
        ReferenceConfig<HelloService> helloServiceReferenceConfig = new ReferenceConfig<>(HelloService.class);
        HelloService helloService = helloServiceReferenceConfig.getRef();
        String result = helloService.sayHello();
        System.out.println("调用结果：" + result);
    }

}
