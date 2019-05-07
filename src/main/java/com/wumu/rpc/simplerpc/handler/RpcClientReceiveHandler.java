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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(("client caught exception..." + cause));
        ctx.close();
    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) {
//        //关闭客户端连接到服务端的channel 这里是会引发tcp四次挥手
//        ctx.close();
//        System.out.println("channel 已经关闭,id:" + ctx.channel().id());
//    }
}
