package com.wumu.rpc.simplerpc.protocol;

import com.wumu.rpc.simplerpc.handler.ExchangeHandler;
import com.wumu.rpc.simplerpc.invoke.Invocation;
import com.wumu.rpc.simplerpc.invoke.Invoker;
import com.wumu.rpc.simplerpc.server.NettyServer;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dydy on 2018/7/16.
 * 协议持有类
 */
public class RpcProtocol implements Protocol {

    protected final Map<String, Invoker> invokerMap = new ConcurrentHashMap<>();

    private ExchangeHandler channelHandler = new ExchangeHandler() {
        @Override
        public Object reply(ChannelHandlerContext ctx, Object msg) {
            if(msg instanceof Invocation) {
                Invocation inv = (Invocation) msg;
                Invoker invoker = getInvoker(getUrl(inv));
                try {
                    return invoker.invoke(inv);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            throw new RuntimeException();
        }
    };

    /**
     * 从请求的参数中获取到对应的接口
     * @param invocation
     * @return
     */
    private String getUrl(Invocation invocation){
        return invocation.getServiceName();
    }

    private Invoker getInvoker(String url){
        return invokerMap.get(url);
    }

    @Override
    public <T> Invoker<T> export(Invoker<T> invoker) {
        // 服务导出
        doExport(invoker);
        // 服务注册
        doRegister();
        return null;
    }

    private void doExport(Invoker invoker){
        //
        String key = invoker.getUrl();
        invokerMap.put(key, invoker);
        // 启动Netty服务器
        openServer();
    }

    private void doRegister(){
        // 暂时不实现服务注册
    }

    private void openServer(){
        // TODO 暂时不考虑已经启动，但是需要reset的场景
        createServer();
    }

    private void createServer(){
        // 创建NettyServer
        new NettyServer(channelHandler);
    }

}
