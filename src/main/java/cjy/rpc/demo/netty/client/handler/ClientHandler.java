package cjy.rpc.demo.netty.client.handler;

import cjy.rpc.demo.netty.client.ClientSocket;
import cjy.rpc.demo.netty.future.SyncWriteFutureCache;
import cjy.rpc.demo.netty.future.WriteFuture;
import cjy.rpc.demo.netty.msg.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * client端响应数据处理器
 * @author chenjianyuan
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!(msg instanceof Response)) {
            super.channelRead(ctx, msg);
            return;
        }

        Response response = (Response) msg;

        String requestId = response.getRequestId();
        //从cache中取出writeFuture
        if (!SyncWriteFutureCache.syncKey.containsKey(requestId)){
            super.channelRead(ctx, msg);
            return;
        }
        WriteFuture<Response> writeFuture = SyncWriteFutureCache.syncKey.get(requestId);
        writeFuture.setResponse(response);
        super.channelRead(ctx, msg);
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(),cause);
        ctx.channel().close();
    }
}
