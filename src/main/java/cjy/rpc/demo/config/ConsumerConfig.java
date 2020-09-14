package cjy.rpc.demo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <rpc:consumer />标签配置信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerConfig<T> {

    /**
     * 协议
     */
    private String protocol;

    /**
     * 接口名称
     */
    private String _interface;

    /**
     * 别名
     */
    private String alias;


}
