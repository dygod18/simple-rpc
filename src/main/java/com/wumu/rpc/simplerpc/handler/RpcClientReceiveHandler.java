package com.wumu.rpc.simplerpc.handler;

import com.wumu.rpc.simplerpc.result.Result;
import com.wumu.rpc.simplerpc.utils.LocalCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by dydy on 2019/4/17.
 */
public class RpcClientReceiveHandler extends SimpleChannelInboundHandler{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收返回的消息，并放入到localcache 中
        Result result = (Result) msg;
        LocalCache localCache = LocalCache.getInstance();
        localCache.putResult(result.getInvokeId(), result.getInvokeResult());
    }
}
