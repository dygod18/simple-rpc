package com.wumu.rpc.simplerpc.client;

import com.wumu.rpc.simplerpc.codec.NettyCodecAdapter;
import com.wumu.rpc.simplerpc.handler.RpcClientReceiveHandler;
import com.wumu.rpc.simplerpc.invoke.Invocation;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

import java.net.InetSocketAddress;

/**
 * Created by dydy on 2019/4/17.
 */
public class NettyClient {

    private static final Integer DEFAULT_PORT = 8080;

    private volatile Channel channel;

    public NettyClient(){
        init();
    }

    /**
     * 初始化一个netty client，并建立与远程服务端的连接
     */
    public void init(){
        EventLoopGroup group = new NioEventLoopGroup();

        RpcClientReceiveHandler rpcClientHandler = new RpcClientReceiveHandler();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(DEFAULT_PORT))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            NettyCodecAdapter nettyCodecAdapter = new NettyCodecAdapter();
                            ch.pipeline().addLast(nettyCodecAdapter.getClientDecoder());
                            ch.pipeline().addLast(nettyCodecAdapter.getSimpleRpcEncoder());
                            ch.pipeline().addLast(rpcClientHandler);
                        }
                    });
            // 建立服务端到远程的连接
            ChannelFuture future = b.connect().sync();
            //获取channel
            channel  = future.channel();
        } catch (InterruptedException e) {
            group.shutdownGracefully();
        }
    }

    /**
     * 获取channel 并向服务端发起请求
     */
    public ChannelFuture send(Invocation invocation) {
        String serviceName = invocation.getServiceName();
        String methodName = invocation.getMethodName();
        Object[] args = invocation.getArguments();
        String invokeId = invocation.getInvokeId();
        StringBuilder sb = new StringBuilder().append(invokeId)
                .append(StringUtil.SPACE)
                .append(serviceName)
                .append(StringUtil.SPACE)
                .append(methodName)
                .append(StringUtil.SPACE);
        if (args != null && args.length > 0 ){
            sb.append(args.toString());
        }
        return channel.writeAndFlush(sb.toString());
    }

}
