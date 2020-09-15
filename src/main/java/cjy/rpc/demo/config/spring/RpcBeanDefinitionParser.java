package cjy.rpc.demo.config.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 自定义标签解析器，用于解析自定义的xml标签，例如<rpc:registry />
 * 将标签解析为beandefinition对象
 */
public class RpcBeanDefinitionParser implements BeanDefinitionParser {

    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(RpcBeanDefinitionParser.class);
    /**
     * 标签所对应的解析对象
     */
    private final Class<?> beanClass;

    public RpcBeanDefinitionParser(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);

        //解析标签属性进行封装
        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            if (!isProperty(method, beanClass)) {
                continue;
            }
            //设置属性
            String methodName = method.getName();
            String propertyName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
            //xml标签属性与java关键字重复时，通过添加 _ 区分，所以要进行处理
            String tagName = propertyName;
            if (propertyName.contains("_")) {
                tagName = propertyName.substring(1);
            }
            String value = element.getAttribute(tagName);
            beanDefinition.getPropertyValues().addPropertyValue(propertyName, value);
        }
        //将bean注册到Spring容器
        String beanName = element.getAttribute("id");
        parserContext.getRegistry().registerBeanDefinition(beanName,beanDefinition);
        return beanDefinition;
    }

    /**
     * 判断设置属性的方法
     * @param method
     * @param beanClass
     * @return
     */
    private boolean isProperty(Method method, Class<?> beanClass) {

        if (null == method || beanClass == null) {
            return false;
        }

        String methodName = method.getName();
        //判断一个方法是否用于设置成员变量的基础判断
        boolean flag = methodName.length() > 3 &&
                methodName.startsWith("set") &&
                Modifier.isPublic(method.getModifiers()) &&
                method.getParameterTypes().length == 1;

        if (!flag) {
            return false;
        }

        //属性类型
        Class<?> type = method.getParameterTypes()[0];

        Method getter = null;

        try{
            getter = beanClass.getMethod("get" + methodName.substring(3));
        } catch (NoSuchMethodException e) {
            logger.info("no such method : class = {}, methodName = {}",beanClass.getName(), "get" + methodName.substring(3));
        }

        if (null == getter) {
            try {
                getter = beanClass.getMethod("is" + methodName.substring(3));
            } catch (NoSuchMethodException e) {
                logger.info("no such method : class = {} methodName = {}",beanClass.getName(),"is" + methodName.substring(3));
            }
        }

        flag = null != getter &&
                Modifier.isPublic(getter.getModifiers()) &&
                type.equals(getter.getReturnType());
        return flag;

    }
}
