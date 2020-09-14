package cjy.rpc.demo.netty.server;

import cjy.rpc.demo.domain.ProviderServerInfo;
import cjy.rpc.demo.netty.codec.RpcCodec;
import cjy.rpc.demo.netty.msg.Request;
import cjy.rpc.demo.netty.msg.Response;
import cjy.rpc.demo.netty.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.Socket;

/**
 * 服务端socket
 * @author chenjianyuan
 */
public class ServerSocket implements Runnable {

    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(ServerSocket.class);

    private ApplicationContext applicationContext;

    /**
     * 端口号
     */
    private int port;

    /**
     * 协议
     */
    private String protocol;

    public ServerSocket(ApplicationContext applicationContext, String protocol, int port) {
        this.port = port;
        this.protocol = protocol;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {

        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new RpcCodec(Request.class, Response.class));
                            channel.pipeline().addLast(new ServerHandler(applicationContext));
                        }
                    });
            if (isPortUsing(port)) {
                logger.error("port={} is using",port);
                return;
            }
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
            ProviderServerInfo.setServerPort(protocol,port);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

    }

    /**
     * 判断端口是否正在使用
     * @param port
     * @return
     */
    private boolean isPortUsing(int port) {
        try {
            Socket socket = new Socket("localhost", port);
            socket.close();
            return false;
        } catch (IOException e) {

        }
        return true;
    }

    /**
     * 启动端口监听
     */
    public void start() {
        new Thread(this).start();
    }
}
