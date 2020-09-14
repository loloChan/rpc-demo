package cjy.rpc.demo.proxy;

import cjy.rpc.demo.netty.msg.Request;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JDKProxy {

    /**
     * 根据接口类获取代理
     * @param interfaceClass 接口类
     * @param request 远程请求参数
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getProxy(Class<?> interfaceClass, Request request) throws Exception{
        //TODO 参数校验，抛出业务异常

        InvocationHandler invocationHandler = new JDKInvocationHandler(request);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (null == classLoader) {
            classLoader = JDKProxy.class.getClassLoader();
        }
        return (T)Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, invocationHandler);
    }

}
