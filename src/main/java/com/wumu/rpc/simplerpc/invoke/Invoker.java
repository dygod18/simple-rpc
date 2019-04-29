package com.wumu.rpc.simplerpc.invoke;

/**
 * Created by dydy on 2018/6/25.
 */
public interface Invoker<T> {

    String getUrl();

    Object invoke(Invocation invocation) throws Throwable;
}
