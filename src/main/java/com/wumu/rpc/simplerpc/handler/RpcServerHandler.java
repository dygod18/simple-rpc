package com.wumu.rpc.simplerpc.handler;

import com.wumu.rpc.simplerpc.result.Result;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by dydy on 2018/7/17.
 */
@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler {

    private ExchangeHandler handler;

    public RpcServerHandler(ExchangeHandler handler) {
        this.handler = handler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("----------------------------------RpcServerHandler:" + msg);

        // 新起线程池来处理worker线程
        ExecutorService executor = getExecutor();
        // 交给ExchangeHandler 来执行
        executor.execute(() -> {
            // 在handler类中调用对应接口的代理方法
            Result result = (Result) handler.reply(ctx, msg);
            ctx.writeAndFlush(result);
        });
    }

    private ExecutorService getExecutor(){
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60 * 1000, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
    }

    /**
     * 异常捕获
     * @param ctx 上下文
     * @param cause 异常对象
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("server caught exception...");
        ctx.close();
    }
}
