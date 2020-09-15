package cjy.rpc.demo.netty.client;

import cjy.rpc.demo.netty.client.handler.ClientHandler;
import cjy.rpc.demo.netty.codec.RpcCodec;
import cjy.rpc.demo.netty.msg.Request;
import cjy.rpc.demo.netty.msg.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author chenjianyuan
 */
public class ClientSocket implements Runnable{

    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(ClientSocket.class);

    /**
     * 用于channelFurure未初始化完毕时，阻塞获取channelFurute的线程
     */
    private CountDownLatch latch = new CountDownLatch(1);

    /**
     * ip地址
     */
    private String host;

    /**
     * 端口号
     */
    private int port;

    /**
     * client channel
     */
    private ChannelFuture channelFuture;

    public ClientSocket(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new RpcCodec(Response.class, Request.class));
                            channel.pipeline().addLast(new ClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            this.channelFuture = future;
            future.addListener(future1 -> {
                if (future1.isSuccess()) {
                    latch.countDown();
                    logger.info("client connect to ip={} port={} success!",host,port);
                }else {
                    logger.error(future1.cause().getMessage(),future1.cause());
                }
            });
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        } finally {
            latch.countDown();
        }
    }

    /**
     * 获取channelFuture
     * @return
     */
    public ChannelFuture getChannelFuture() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(),e);
        }
        return this.channelFuture;
    }

    /**
     * 启动连接
     */
    public void start() {
        new Thread(this).start();
    }

    /**
     * 判断连接是否有效
     * @return
     */
    public boolean isActive() {
        Channel channel = this.channelFuture.channel();
        return channel.isActive();
    }
}
