package com.wumu.rpc.simplerpc.codec;

import com.wumu.rpc.simplerpc.invoke.Invocation;
import com.wumu.rpc.simplerpc.invoke.RpcInvocation;
import com.wumu.rpc.simplerpc.result.Result;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dydy on 2018/7/17.
 */
public class NettyCodecAdapter {

    private final ChannelHandler serverEncoder = new ServerEncoder();
    private final ChannelHandler serverDecoder = new ServerDecoder();

    private final ChannelHandler clientDecoder = new ClientDecoder();

    public ChannelHandler getServerEncoder() {
        return serverEncoder;
    }

    public ChannelHandler getServerDecoder() {
        return serverDecoder;
    }

    public ChannelHandler getClientDecoder() {
        return clientDecoder;
    }

    private class ServerEncoder extends MessageToByteEncoder {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            out.writeBytes(msg.toString().getBytes());
        }
    }

    private class ServerDecoder extends ChannelInboundHandlerAdapter {

        /**
         * 解码器，将msg解码为invocation
         *
         * @param ctx
         * @param msg
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws NoSuchMethodException {
            // 首先做一次 decode 避免tcp粘包拆包问题，保证消息是完整的
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            String requestMsg = new String(bytes, 0, buf.readableBytes());

            ctx.fireChannelRead(buildInvocation(requestMsg));
            // 然后交给接下来的Handler（RpcServerHandler）去处理
        }

        public Invocation buildInvocation(String requestMsg){
            String[] requests = requestMsg.split("\\s+");
            String serviceName = "";
            String methodName = "";
            String invokeId = "";
            List<String> argsList = new ArrayList<>();
            for (int i = 0; i < requests.length; i++) {
                if (i == 0) {
                    invokeId = requests[i];
                } else if (i == 1){
                    serviceName = requests[i];
                } else if (i == 2) {
                    methodName = requests[i];
                }else{
                    argsList.add(requests[i]);
                }
            }
            return new RpcInvocation(serviceName, methodName, argsList.toArray(), invokeId);
        }

    }

    private class ClientDecoder extends ChannelInboundHandlerAdapter{
        /**
         * 解码器，将msg解码为result
         *
         * @param ctx
         * @param msg
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws NoSuchMethodException {
            // decode 避免tcp粘包拆包问题，保证消息是完整的；
            // 当然，这里的实现比较简单，实际情况下，会跟服务端约定一个结束符，用以标记消息已经读取完成
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            String responseMsg = new String(bytes, 0, buf.readableBytes());

            ctx.fireChannelRead(buildResult(responseMsg));
        }

        private Result buildResult(String responseMsg){
            String[] responses = responseMsg.split("\\s+");
            String result = "";
            String invokeId = "";
            for (int i = 0; i < responses.length; i++) {
                if (i == 0) {
                    invokeId = responses[i];
                } else {
                    result = responses[i];
                }
            }
            return new Result(invokeId, result);
        }

    }

}
