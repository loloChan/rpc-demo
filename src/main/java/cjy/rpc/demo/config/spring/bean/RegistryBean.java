package cjy.rpc.demo.config.spring.bean;

import cjy.rpc.demo.config.RegistryConfig;
import cjy.rpc.demo.registry.RedisRegistryCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RegistryBean<T> extends RegistryConfig<T> implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(RegistryBean.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        logger.info("connect to registry ...");
        RedisRegistryCenter.init(getAddress(), getPort());
        logger.info("connect to registry complete --> address={},port={}", getAddress(), getPort());

        //TODO 初始化netty服务端
    }
}
