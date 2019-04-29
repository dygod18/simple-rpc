package com.wumu.rpc.simplerpc.invoke;


import com.wumu.rpc.simplerpc.result.Result;

/**
 * Created by dydy on 2018/7/16.
 */
public abstract class RpcServerInvoker<T> implements Invoker<T> {

    private final T proxy;
    private final String url;

    public RpcServerInvoker(T proxy, Class<T> type) {
        this.url = type.getName();
        this.proxy = proxy;
    }

    protected abstract Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable;

    @Override
    public Result invoke(Invocation invocation) throws Throwable {
        return new Result(invocation.getInvokeId(), (String) doInvoke(proxy, invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments()));
    }

    @Override
    public String getUrl() {
        return url;
    }
}
