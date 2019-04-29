package com.wumu.rpc.simplerpc.config;

import com.wumu.rpc.simplerpc.invoke.Invoker;
import com.wumu.rpc.simplerpc.protocol.Protocol;
import com.wumu.rpc.simplerpc.protocol.RpcProtocol;
import com.wumu.rpc.simplerpc.proxy.JdkProxyFactory;

/**
 * Created by dydy on 2018/6/14.
 */
public class ServiceConfig<T> {

    private JdkProxyFactory proxyFactory = new JdkProxyFactory();
    private Protocol protocol = new RpcProtocol();
    /**
     * 接口实现类的引用，在类初始化的时候设置进去
     */
    private T ref;

    /**
     * 实际暴露出去的接口
     */
    private Class<?> interfaceClass;

    public ServiceConfig (Object bean, Object interfaceClass){
        this.ref = (T) bean;
        this.interfaceClass = (Class<?>) interfaceClass;
    }

    /**
     * 将对应的service 暴露出去。即注册到注册中心，同时让框架只有其代理类
     */
    public void export() {
        doExport();
    }

    private void doExport() {
        // 获取对应接口实现类的invoker， ref 对应的接口是实现类, interfaceClass 声明的接口
        Invoker<?> invoker = proxyFactory.getInvoker(ref, (Class) interfaceClass);

        //TODO 记录暴露的invoker 方便做销毁
        protocol.export(invoker);
    }

}
