package cjy.rpc.demo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * <rpc:registry />标签的配置信息
 */
public class RegistryConfig<T> {

    /**
     * 主机地址
     */
    private String address;

    /**
     * 端口号
     */
    private int port;

    /**
     * 注册中心协议
     */
    private String protocol;

}
