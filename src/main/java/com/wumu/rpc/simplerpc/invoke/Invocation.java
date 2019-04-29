package com.wumu.rpc.simplerpc.invoke;

/**
 * Created by dydy on 2018/7/19.
 */
public interface Invocation {

    String getServiceName();

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArguments();

    String getInvokeId();

}