package cjy.rpc.demo.config.spring.bean;

import cjy.rpc.demo.config.ProviderConfig;
import cjy.rpc.demo.domain.ProviderServerInfo;
import cjy.rpc.demo.domain.RpcProviderConfig;
import cjy.rpc.demo.netty.server.ServerSocket;
import cjy.rpc.demo.registry.RedisRegistryCenter;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author chenjianyuan
 */
public class ProviderBean<T> extends ProviderConfig<T> implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(ProviderConfig.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RpcProviderConfig rpcProviderConfig = new RpcProviderConfig();
        rpcProviderConfig.set_interface(get_interface());
        rpcProviderConfig.setAlias(getAlias());
        rpcProviderConfig.setProtocol(getProtocol());
        rpcProviderConfig.setRef(getRef());
        //生产者提供服务的端口号应该根据协议而定，不同的协议在不同的端口中提供服务
        rpcProviderConfig.setAddress(ProviderServerInfo.getServerHost());
        Integer port = ProviderServerInfo.getServerPort(getProtocol());
        if (null == port) {
            //抛出异常
        }
        rpcProviderConfig.setPort(port);
        //启动服务监听端口
        new ServerSocket(applicationContext, getProtocol(), port).start();
        //将rpc信息注册到注册中心
        String key = RedisRegistryCenter.genericKey(get_interface(), getAlias(), getProtocol());
        long count = RedisRegistryCenter.registryProvider(key, JSON.toJSONString(rpcProviderConfig));

        logger.info("注册生产者：interface={},alias={},protocol={},count={}",get_interface(),getAlias(),getProtocol(),count);

    }
}
