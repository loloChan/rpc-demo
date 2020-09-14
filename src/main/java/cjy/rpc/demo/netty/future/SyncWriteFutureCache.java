package cjy.rpc.demo.netty.future;

import cjy.rpc.demo.netty.msg.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 异步结果集缓存
 * @author chenjianyuan
 */
public final class SyncWriteFutureCache {

    public static final Map<String, WriteFuture<Response>> syncKey = new ConcurrentHashMap<>();

}
