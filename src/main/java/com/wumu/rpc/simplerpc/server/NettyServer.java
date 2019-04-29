package com.wumu.rpc.simplerpc.server;

import com.wumu.rpc.simplerpc.codec.NettyCodecAdapter;
import com.wumu.rpc.simplerpc.handler.ExchangeHandler;
import com.wumu.rpc.simplerpc.handler.RpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author  wumu
 */
public class NettyServer {

    private static final AtomicInteger INDEX = new AtomicInteger(1);
    private static final Integer DEFAULT_PORT = 8080;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private InetSocketAddress bindAddress;
    protected ExchangeHandler exchangeHandler;

    public NettyServer(ExchangeHandler exchangeHandler) {
        // 先默认设置为本地地址
        bindAddress = new InetSocketAddress(DEFAULT_PORT);
        this.exchangeHandler = exchangeHandler;
        doOpen();
    }

    protected void doOpen() {
        ServerBootstrap sb = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        sb.channel(NioServerSocketChannel.class)
                .group(bossGroup, workerGroup);

        sb.option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, 100);

        final RpcServerHandler nettyHandler = new RpcServerHandler(exchangeHandler);

        sb.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                NettyCodecAdapter adapter = new NettyCodecAdapter();
                ChannelPipeline p = ch.pipeline();
                p.addLast("decoder", adapter.getServerDecoder());
                p.addLast("encoder", adapter.getServerEncoder());
                p.addLast("handler", nettyHandler);
            }
        });

        sb.bind(bindAddress);
        System.out.println("server 启动完成");
    }

}
