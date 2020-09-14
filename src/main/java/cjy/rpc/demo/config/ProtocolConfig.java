package cjy.rpc.demo.config;

import lombok.Data;

/**
 * <rpc:protocol />标签配置信息
 */
@Data
public class ProtocolConfig<T> {

    /**
     * 协议名称
     */
    private String protocol;

    /**
     * 端口号
     */
    private int port;

}
