package cjy.rpc.demo.netty.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface WriteFuture<T> extends Future<T> {

    /**
     * 获取异常
     * @return
     */
    Throwable getCause();

    /**
     * 设置异常
     * @param cause
     */
    void setCause(Throwable cause);

    /**
     * 是否写出成功
     * @return
     */
    boolean isWriteSuccess();

    /**
     * 设置是否写成功
     * @param flag
     */
    void setWriteSuccess(boolean flag);

    /**
     * 获取请求Id
     * @return
     */
    String getRequestId();

    /**
     * 获取响应对象
     * @return
     */
    T getResponse() throws ExecutionException, InterruptedException;

    /**
     * 设置响应对象
     * @param response
     */
    void setResponse(T response);

    /**
     * 是否超时
     * @return
     */
    boolean isTimeout();


}
