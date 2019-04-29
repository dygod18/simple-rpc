package com.wumu.rpc.simplerpc.proxy;

import com.wumu.rpc.simplerpc.invoke.Invoker;
import com.wumu.rpc.simplerpc.invoke.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by dydy on 2018/8/15.
 */
public class InvokerInvocationHandler implements InvocationHandler {

    private final Invoker<?> invoker;

    public InvokerInvocationHandler(Invoker<?> invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if(method.getDeclaringClass() == Object.class) {
            return method.invoke(invoker, args);
        }
        if ("toString".equals(methodName) && parameterTypes.length == 0) {
            return invoker.toString();
        }
        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
            return invoker.hashCode();
        }
        if ("equals".equals(methodName) && parameterTypes.length == 1) {
            return invoker.equals(args[0]);
        }

        return invoker.invoke(new RpcInvocation(method.getDeclaringClass().getName(), methodName, args));
    }

}
