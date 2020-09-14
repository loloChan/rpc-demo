package cjy.rpc.demo.config.spring;

import cjy.rpc.demo.config.spring.bean.ConsumerBean;
import cjy.rpc.demo.config.spring.bean.ProtocolBean;
import cjy.rpc.demo.config.spring.bean.ProviderBean;
import cjy.rpc.demo.config.spring.bean.RegistryBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 自定义xml命名空间处理器
 * 处理spring解释过程中，遇到自定义的标签，例如<rpc:registry />等的标签
 */
public class RpcNamespceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("provider", new RpcBeanDefinitionParser(ProviderBean.class));
        registerBeanDefinitionParser("consumer", new RpcBeanDefinitionParser(ConsumerBean.class));
        registerBeanDefinitionParser("protocol", new RpcBeanDefinitionParser(ProtocolBean.class));
        registerBeanDefinitionParser("registry",new RpcBeanDefinitionParser(RegistryBean.class));
    }
}
