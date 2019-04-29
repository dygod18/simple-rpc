package com.wumu.rpc.simplerpc.config;

import com.wumu.rpc.simplerpc.invoke.Invoker;
import com.wumu.rpc.simplerpc.invoke.RpcConsumerInvoker;
import com.wumu.rpc.simplerpc.proxy.JdkProxyFactory;

/**
 * Created by dydy on 2019/4/17.
 */
public class ReferenceConfig<T> {

    private JdkProxyFactory proxyFactory = new JdkProxyFactory();

    private String interfaceName;
    private Class<?>[] interfaceClass;

    private T ref;

    public ReferenceConfig(Class interfaceClass){
        this.interfaceClass = new Class[]{interfaceClass};
        ref = createProxy();
    }

    /**
     * 创建对应service的代理类
     * @return
     */
    private T createProxy(){
        // 需要创建一个invoker，invoker持有nettyClient，用于跟远程服务通信
        Invoker invoker = new RpcConsumerInvoker<>();
        return (T)proxyFactory.getProxy(invoker, interfaceClass);
    }

    public T getRef(){
        return ref;
    }

}
