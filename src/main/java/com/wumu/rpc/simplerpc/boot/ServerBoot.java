package com.wumu.rpc.simplerpc.boot;


import com.wumu.rpc.simplerpc.config.ServiceConfig;

/**
 * Created by dydy on 2019/4/28.
 */
public class ServerBoot {

    public static void main(String[] args) {
        ServiceConfig<HelloService> helloServiceServiceConfig = new ServiceConfig(new HelloServiceImpl(), HelloService.class);
        helloServiceServiceConfig.export();
    }

}
