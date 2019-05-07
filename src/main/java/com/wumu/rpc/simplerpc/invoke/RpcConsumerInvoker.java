package com.wumu.rpc.simplerpc.invoke;

import com.wumu.rpc.simplerpc.client.NettyClient;
import com.wumu.rpc.simplerpc.utils.LocalCache;
import io.netty.channel.ChannelFuture;

/**
 * 持有netty client 发起调用
 * Created by dydy on 2019/4/17.
 */
public class RpcConsumerInvoker<T> implements Invoker<T>{

    // 持有一个netty client， 用于跟远程服务端通信
    private NettyClient client;

    public RpcConsumerInvoker() {
        client = new NettyClient();
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        return doInvoke(invocation);
    }

    protected Object doInvoke(Invocation invocation) throws Throwable {
        // 发起调用
        ChannelFuture future = client.send(invocation);
        // 将future 跟调用编号建立关联
        LocalCache localCache = LocalCache.getInstance();
        //轮询直到获取到对应的result
        do{
            Thread.sleep(100);
            System.out.println("wait for server response, invoke id : " + invocation.getInvokeId());
        }while (localCache.getResut(invocation.getInvokeId()) == null);

        return localCache.getResut(invocation.getInvokeId());
    }

}
