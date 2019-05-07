package com.wumu.rpc.simplerpc.result;


import com.wumu.rpc.simplerpc.invoke.Invocation;
import io.netty.util.internal.StringUtil;

/**
 * Created by dydy on 2018/8/14.
 */
public class Result implements Invocation{

    private String invokeId; // 从返回值中解析出来的调用编号，用于跟消费端的调用线程建立关联

    private Object invokeResult;

    public Result(String invokeId, Object msg) {
        this.invokeId = invokeId;
        this.invokeResult = msg;
    }

    @Override
    public String getServiceName() {
        return null;
    }

    @Override
    public String getMethodName() {
        return null;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return new Class<?>[0];
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public String getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(String invokeId) {
        this.invokeId = invokeId;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder().append(invokeId)
                .append(StringUtil.SPACE).append(invokeResult);
        return sb.toString();
    }

    public Object getInvokeResult(){
        return invokeResult;
    }

}
