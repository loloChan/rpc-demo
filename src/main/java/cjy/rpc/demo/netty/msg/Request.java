package cjy.rpc.demo.netty.msg;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 客户端请求信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    /**
     * 通信管道
     */
    private Channel channel;

    /**
     * 请求唯一标识符
     */
    private String requestId;

    /**
     * 调用的方法名
     */
    private String methodName;

    /**
     * 入参类型
     */
    private Class[] paramTypes;

    /**
     * 入参参数
     */
    private Object[] args;

    /**
     * 接口
     */
    private String _interface;

    /**
     * 服务端的实现类
     */
    private String ref;

    /**
     * 别名
     */
    private String alias;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 服务ip地址
     */
    private String address;

    /**
     * 端口号
     */
    private int port;

}
