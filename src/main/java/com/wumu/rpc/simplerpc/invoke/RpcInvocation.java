package com.wumu.rpc.simplerpc.invoke;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by dydy on 2018/7/19.
 */
public class RpcInvocation implements Invocation, Serializable {

    String serviceName;
    String methodName;
    Object[] arguments;
    String sign;

    public RpcInvocation(String serviceName, String methodName, Object[] arguments) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.arguments = arguments;
        this.sign = UUID.randomUUID().toString();
    }

    public RpcInvocation(String serviceName, String methodName, Object[] arguments, String sign) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.arguments = arguments;
        this.sign = sign;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return new Class<?>[0];
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public String getInvokeId(){
        return sign;
    }
}
