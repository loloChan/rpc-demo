package cjy.rpc.demo.netty.codec;

import cjy.rpc.demo.serialize.JsonSerializer;
import cjy.rpc.demo.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * rpc协议编/解码器
 * @author loloChan
 */
public class RpcCodec extends ByteToMessageCodec<Object> {

    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(RpcCodec.class);

    /**
     * 编码对象
     */
    private Class<?> decoderClass;

    /**
     * 解码对象
     */
    private Class<?> encoderClass;

    /**
     * 序列化器
     */
    private Serializer serializer;

    public RpcCodec(Class<?> decoderClass, Class<?> encoderClass) {
        this.decoderClass = decoderClass;
        this.encoderClass = encoderClass;
        //默认序列化器
        this.serializer = new JsonSerializer();
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          Object o, ByteBuf byteBuf) throws Exception {
        if (encoderClass.isInstance(o)) {
            byte[] bytes = serializer.serialize(o);
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.markReaderIndex();
        int length = byteBuf.readInt();
        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[length];
        byteBuf.readBytes(data);
        //反序列化
        Object obj = serializer.deserialize(decoderClass, data);
        list.add(obj);
    }

    /**
     * 设置自定义的序列化工具
     * @param serializer
     */
    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }
}
