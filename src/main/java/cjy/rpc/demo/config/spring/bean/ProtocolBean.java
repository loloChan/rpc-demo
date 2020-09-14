package cjy.rpc.demo.config.spring.bean;

import cjy.rpc.demo.config.ProtocolConfig;
import cjy.rpc.demo.domain.ProviderServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 注册协议与端口号的映射关系
 * @param <T>
 */
public class ProtocolBean<T> extends ProtocolConfig<T> implements ApplicationContextAware {

    /**
     * 日志打印
     */
    private static Logger logger = LoggerFactory.getLogger(ProtocolBean.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ProviderServerInfo.setServerPort(getProtocol(),getPort());
        logger.info("注册服务协议：protocol={} port={}",getProtocol(),getPort());
    }
}
