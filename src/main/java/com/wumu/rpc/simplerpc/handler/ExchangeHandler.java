package com.wumu.rpc.simplerpc.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by dydy on 2018/7/17.
 * 实际的rpc请求处理类，入参为invocation，基于invocation调用对应服务的invoker 方法
 */
public abstract class ExchangeHandler {

    public abstract Object reply(ChannelHandlerContext ctx, Object msg);
}
