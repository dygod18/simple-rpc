package com.wumu.rpc.simplerpc.codec;

import com.wumu.rpc.simplerpc.invoke.Invocation;
import com.wumu.rpc.simplerpc.invoke.RpcInvocation;
import com.wumu.rpc.simplerpc.result.Result;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dydy on 2018/7/17.
 */
public class NettyCodecAdapter {

    private static final int HEAD_LENGTH = 4;

    private final ChannelHandler simpleRpcEncoder = new SimpleRpcEncoder();
    private final ChannelHandler serverDecoder = new SimpleRpcDecoder() {
        @Override
        protected Invocation buildResult(String requestMsg) {
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
    };

    private final ChannelHandler clientDecoder = new SimpleRpcDecoder() {
        @Override
        protected Invocation buildResult(String responseMsg) {
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
    };

    public ChannelHandler getSimpleRpcEncoder() {
        return simpleRpcEncoder;
    }

    public ChannelHandler getServerDecoder() {
        return serverDecoder;
    }

    public ChannelHandler getClientDecoder() {
        return clientDecoder;
    }

    private class SimpleRpcEncoder extends MessageToByteEncoder {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            byte[] payload = msg.toString().getBytes();
            byte[] header = new byte[4];
            header[3] = (byte) (payload.length & 0xFF);
            header[2] = (byte) (payload.length >> 8 & 0xFF);
            header[1] = (byte) (payload.length >> 16 & 0xFF);
            header[0] = (byte) (payload.length >> 24 & 0xFF);
            byte[] request = new byte[header.length + payload.length];
            System.arraycopy(header, 0, request, 0, header.length);
            System.arraycopy(payload, 0, request, header.length, payload.length);
            out.writeBytes(request);
        }
    }

    private abstract class SimpleRpcDecoder extends ByteToMessageDecoder {

        /**
         * 解码器，将msg解码为result
         */
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            // 首先做一次 decode 避免tcp粘包拆包问题，保证消息是完整的

            // 判断可读长度是否大于header 的长度
            if (in.readableBytes() < HEAD_LENGTH) {
                return;
            }

            // 判断buffer中是否有足够的读取长度
            in.markReaderIndex();

            // 从buf中读取4个字节，正好为记录的当前payload的长度
            int length = in.readInt();

            // 如果没有足够长度，则重置当前索引回刚才标记的位置
            if(in.readableBytes() < length) {
                in.resetReaderIndex();
                return;
            }
            // 将payload读取出来交给handler去处理
            byte[] bytes = new byte[length];
            in.readBytes(bytes);
            String requestMsg = new String(bytes);

            // 然后交给接下来的Handler（RpcServerHandler）去处理
            out.add(buildResult(requestMsg));
        }

        protected abstract Invocation buildResult(String requestMsg);
    }

}
