package com.wumu.rpc.simplerpc.boot;


import com.wumu.rpc.simplerpc.config.ReferenceConfig;

import java.util.concurrent.ExecutorService;

import static com.sun.javafx.runtime.async.BackgroundExecutor.getExecutor;

/**
 * Created by dydy on 2019/4/28.
 */
public class ClientBoot {

    public static void main(String[] args){
        try {
            ReferenceConfig<HelloService> helloServiceReferenceConfig = new ReferenceConfig<>(HelloService.class);
            HelloService helloService = helloServiceReferenceConfig.getRef();
            ExecutorService executor = getExecutor();
            for(int i = 0; i < 1000; i++){
                int finalI = i;
                executor.execute(() -> {
                    String result = helloService.sayHello();
                    System.out.println("调用结果：" + result + ", count:" + finalI);
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
