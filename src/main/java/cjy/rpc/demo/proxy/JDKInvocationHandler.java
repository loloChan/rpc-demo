package cjy.rpc.demo.proxy;

import cjy.rpc.demo.netty.future.SyncWrite;
import cjy.rpc.demo.netty.msg.Request;
import cjy.rpc.demo.netty.msg.Response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 使用jdk动态代理生成服务接口的代理类
 * 由代理类实现远程链接请求并获取结果
 * 也就是说，在消费端，该类属于stub，用于网络连接调用。
 * @author chenjianyuan
 */
public class JDKInvocationHandler implements InvocationHandler {

    private Request request;

    public JDKInvocationHandler(Request request) {
        this.request = request;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        //toString、hashCode、equals方法不进行远程调用，无意义
        if ("toString".equals(methodName) && parameterTypes.length == 0) {
            return request.toString();
        } else if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
            return request.hashCode();
        } else if ("equals".equals(methodName) && parameterTypes.length == 1) {
            return request.equals(parameterTypes[0]);
        }

        //设置调用方法信息
        request.setMethodName(methodName);
        request.setParamTypes(parameterTypes);
        request.setArgs(args);

        //通过netty进行远程调用
        SyncWrite syncWrite = SyncWrite.getInstance();
        Response response = syncWrite.remoteInvoke(request, 1000);
        return response;
    }
}
