package com.wumu.rpc.simplerpc.proxy;


import com.wumu.rpc.simplerpc.invoke.Invoker;
import com.wumu.rpc.simplerpc.invoke.RpcServerInvoker;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 基于jdkproxy 的代理工厂类
 *
 * @author dydy
 * @date 2018/6/25
 */
public class JdkProxyFactory {

    /**
     * 用于consumer 端引用创建调用代理
     * @param invoker
     * @param interfaces
     * @param <T>
     * @return
     */
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, new InvokerInvocationHandler(invoker));
    }

    /**
     * 生成对应的服务端暴露接口的invoker，代理对应的实现类方法，可以通过doInvoke方法来调用对应的实现类方法，这里也可以通过javassist的方式来实现
     * @param proxy
     * @param type
     * @param <T>
     * @return
     */
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type) {
        return new RpcServerInvoker<T>(proxy, type) {
            @Override
            protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable {
                Method method = proxy.getClass().getMethod(methodName, parameterTypes);
                return method.invoke(proxy, arguments);
            }
        };
    }
}
