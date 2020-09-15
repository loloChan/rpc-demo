package cjy.rpc.demo.netty.manage;

import cjy.rpc.demo.netty.client.ClientSocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * client socket管理
 * @author chenjianyuan
 */
public final class NetManager {

    private NetManager() { }

    /**
     * 缓存已建立连接的socket
     */
    private static Map<String, ClientSocket> clientSocketCache = new ConcurrentHashMap<>();

    /**
     * 获取连接
     * @param host
     * @param port
     * @return
     */
    public static ClientSocket getClientSocket(String host, int port) {

        ClientSocket clientSocket;
        String key = genericKey(host, port);
        if (clientSocketCache.containsKey(key)) {
            clientSocket = clientSocketCache.get(key);
            if (clientSocket.isActive()) {
                return clientSocket;
            }else {
                clientSocketCache.remove(key);
            }
        }

        clientSocket = new ClientSocket(host, port);
        clientSocket.start();
        clientSocketCache.put(key, clientSocket);
        return clientSocket;
    }

    private static String genericKey(String host, int port) {
        return host + "_" + port;
    }

}
