package cjy.rpc.demo.netty.future;

import cjy.rpc.demo.netty.msg.Response;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *  异步获取rpc调用结果
 * @author chenjianyuan
 */
public class SyncWriteFuture implements WriteFuture<Response> {

    /**
     * 用于获取响应对象的阻塞等待
     */
    private CountDownLatch latch = new CountDownLatch(1);

    /**
     * 判断等待时间是否超时
     */
    private final long begin = System.currentTimeMillis();

    /**
     * 过期时间
     */
    private long timeout;

    /**
     * 服务端响应对象
     */
    private Response response;

    /**
     * 请求id
     */
    private final String requestId;

    /**
     * 判断通过channel写是否成功
     */
    private boolean writeSuccess = false;

    /**
     * write时发生的异常
     */
    private Throwable cause;

    /**
     * 是否已过期
     */
    private boolean isTimeout = false;

    public SyncWriteFuture(String requestId) {
        this.requestId = requestId;
    }

    /**
     * 获取异常
     *
     * @return
     */
    @Override
    public Throwable getCause() {
        return this.cause;
    }

    /**
     * 设置异常
     *
     * @param cause
     */
    @Override
    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    /**
     * 是否写出成功
     *
     * @return
     */
    @Override
    public boolean isWriteSuccess() {
        return this.writeSuccess;
    }

    /**
     * 设置是否写成功
     *
     * @param flag
     */
    @Override
    public void setWriteSuccess(boolean flag) {
        this.writeSuccess = flag;
    }

    /**
     * 获取请求Id
     *
     * @return
     */
    @Override
    public String getRequestId() {
        return this.requestId;
    }

    /**
     * 获取响应对象
     *
     * @return
     */
    @Override
    public Response getResponse() throws ExecutionException, InterruptedException {
        return get();
    }

    /**
     * 设置响应对象
     *
     * @param response
     */
    @Override
    public void setResponse(Response response) {
        this.response = response;
        latch.countDown();
    }

    /**
     * 是否超时
     *
     * @return
     */
    @Override
    public boolean isTimeout() {
        if (this.isTimeout) {
            return this.isTimeout;
        }
        return System.currentTimeMillis() - this.begin > this.timeout;
    }

    @Override
    public Response get() throws InterruptedException, ExecutionException {
        latch.await();
        return response;
    }

    @Override
    public Response get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return this.response;
        }
        return null;
    }

    /**
     * 不支持该方法
     * @param mayInterruptIfRunning
     * @return
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    /**
     * 不支持该方法
     * @return
     */
    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    /**
     * 不支持该方法
     * @return
     */
    @Override
    public boolean isDone() {
        throw new UnsupportedOperationException();
    }
}
