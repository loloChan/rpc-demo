package cjy.rpc.demo.netty.future;

import cjy.rpc.demo.netty.msg.Request;
import cjy.rpc.demo.netty.msg.Response;
import io.netty.channel.Channel;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * rpc远程调用
 * @author chenjianyuan
 */
public final class SyncWrite {

    /**
     * 单例实例
     */
    private static final SyncWrite INSTANCE = new SyncWrite();

    /**
     * 单例构造函数
     */
    private SyncWrite() { }

    /**
     * 获取单例
     * @return
     */
    public static SyncWrite getInstance() {
        return INSTANCE;
    }

    /**
     * 远程调用
     * @param request 调用请求
     * @param timeout 等待结果时间
     * @return
     */
    public Response remoteInvoke(final Request request, final long timeout) throws Exception {

        if (null == request) {
            throw new NullPointerException("request");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout <= 0");
        }

        //生成request的唯一标识符，用于标识request，并识别后续服务端返回的结果
        String requestId = UUID.randomUUID().toString();
        request.setRequestId(requestId);

        WriteFuture<Response> writeFuture = new SyncWriteFuture(requestId);
        //将writeFuture添加到cache中，netty收到响应结果后返回响应结果
        SyncWriteFutureCache.syncKey.put(requestId, writeFuture);

        //通过ip+端口号获取channle连接
        Channel channel = null;

        Response response = doInvoke(channel, request, writeFuture, timeout);

        //移除key
        if (SyncWriteFutureCache.syncKey.containsKey(requestId)) {
            SyncWriteFutureCache.syncKey.remove(requestId);
        }

        return response;
    }

    /**
     * 通过netty进行网络调用
     * @param channel 与服务提供端的连接channel
     * @param request 请求参数
     * @param writeFuture 异步响应结果
     * @param timeout 等待时间
     * @return
     */
    private Response doInvoke(final Channel channel, final Request request,
                             final WriteFuture<Response> writeFuture, final long timeout) throws Exception {
        channel.writeAndFlush(request).addListener((future) -> {
            writeFuture.setWriteSuccess(future.isSuccess());
            writeFuture.setCause(future.cause());

            //失败移除
            if (!future.isSuccess()) {
                SyncWriteFutureCache.syncKey.remove(request.getRequestId());
            }
        });

        //此方法会阻塞，直至netty客户端收到响应消息，或等待时间结束返回
        Response response = writeFuture.get(timeout, TimeUnit.MILLISECONDS);

        if (null == response) {
            if (writeFuture.isTimeout()) {
                throw new  TimeoutException();
            } else {
                throw new Exception(writeFuture.getCause());
            }
        }
        return response;
    }

}
