package cjy.rpc.demo.serialize;

import com.alibaba.fastjson.JSON;

/**
 * 使用fastjson实现序列化器
 * @author loloChan
 */
public class JsonSerializer implements Serializer {

    /**
     * 序列化
     *
     * @param obj
     * @return
     */
    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    /**
     * 反序列化
     *
     * @param clazz
     * @param bytes
     * @return
     */
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
