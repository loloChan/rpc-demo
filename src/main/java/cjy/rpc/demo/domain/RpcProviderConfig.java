package cjy.rpc.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务提供者提供服务的信息
 * 该信息需要从注册中心上拉取
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcProviderConfig {

    /**
     * 服务接口
     */
    private String _interface;

    /**
     * 别名
     */
    private String alias;

    /**
     * 服务端引用名称
     */
    private String ref;

    /**
     * 服务ip地址
     */
    private String address;

    /**
     * 协议名称
     */
    private String protocol;

    /**
     * 端口号
     */
    private int port;

}
