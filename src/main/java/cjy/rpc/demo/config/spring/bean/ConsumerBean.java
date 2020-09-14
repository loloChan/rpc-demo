package cjy.rpc.demo.config.spring.bean;

import cjy.rpc.demo.config.ConsumerConfig;
import cjy.rpc.demo.domain.RpcProviderConfig;
import cjy.rpc.demo.netty.msg.Request;
import cjy.rpc.demo.proxy.JDKProxy;
import cjy.rpc.demo.registry.RedisRegistryCenter;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * 实现FactoryBean接口，那么Spring在创建bean的时，暴露的不是配置的bean，而是getObject所
 * 获取到的individual objects（个性化对象）。也就是根据配置文件+自定义所创建的对象
 * @param <T>
 */
public class ConsumerBean<T> extends ConsumerConfig<T> implements FactoryBean {

    /**
     * 日志对象
     */
    private Logger logger = LoggerFactory.getLogger(ConsumerBean.class);

    /**
     * netty通信客户端
     */
    private ChannelFuture channelFuture;

    /**
     * 服务提供者信息
     */
    private RpcProviderConfig rpcProviderConfig;

    @Override
    public T getObject() throws Exception {

        //根据interface信息以及alias信息获取生产者信息
        if (null == rpcProviderConfig) {
            String key = RedisRegistryCenter.genericKey(get_interface(), getAlias(), getProtocol());
            String providerInfo = RedisRegistryCenter.obtainProvider(key);
            rpcProviderConfig = JSON.parseObject(providerInfo, RpcProviderConfig.class);
        }

        //封装request
        Request request = new Request();
        request.setChannel(channelFuture.channel());
        request.setAlias(rpcProviderConfig.getAlias());
        request.set_interface(rpcProviderConfig.get_interface());
        request.setRef(rpcProviderConfig.getRef());
        request.setProtocol(rpcProviderConfig.getProtocol());
        request.setAddress(rpcProviderConfig.getAddress());
        request.setPort(rpcProviderConfig.getPort());
        //返回代理类对象
        return (T) JDKProxy.getProxy(getObjectType(),request);
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return Class.forName(get_interface());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
