package cjy.rpc.demo.domain;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务提供者服务信息
 */
public final class ProviderServerInfo {

    /**
     * 服务地址
     */
    private static String serverHost;

    /**
     * 协议与端口号映射集
     */
    private static Map<String, Integer> protocolMap = new ConcurrentHashMap<String, Integer>();

    static {
        try {
            serverHost = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {

        }
    }

    /**
     * 获取服务地址
     * @return
     */
    public static String getServerHost() {
        return serverHost;
    }

    /**
     * 根据协议获取服务端口号
     * @param protocol 协议名称
     * @return
     */
    public static Integer getServerPort(String protocol) {
        if (protocolMap.containsKey(protocol)) {
            return protocolMap.get(protocol);
        }
        return null;
    }

    /**
     * 设置服务协议对应的端口号
     * @param protocol 协议名称
     * @param port 端口号
     */
    public static void setServerPort(String protocol, Integer port) {
        if (StringUtils.isNotBlank(protocol) && null != port) {
            protocolMap.put(protocol, port);
        }
    }

}
