package cjy.rpc.demo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <rpc:provider />标签配置信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderConfig<T> {

    /**
     * 协议
     */
    private String protocol;

    /**
     * 接口名称
     */
    private String _interface;

    /**
     * 引用Spring bean 的实例名称
     */
    private String ref;

    /**
     * 别名
     */
    private String alias;

}
