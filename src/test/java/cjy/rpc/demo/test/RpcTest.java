package cjy.rpc.demo.test;

import cjy.rpc.demo.netty.client.ClientSocket;
import cjy.rpc.demo.netty.codec.RpcCodec;
import cjy.rpc.demo.netty.msg.Request;
import cjy.rpc.demo.netty.server.ServerSocket;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class RpcTest {

    private static Logger logger = LoggerFactory.getLogger(RpcTest.class);

    @Test
    public void clientStart() throws Exception{
        Request request = new Request();
        request.setRequestId(UUID.randomUUID().toString());
        request.setProtocol("baga");
        ClientSocket clientSocket = new ClientSocket("127.0.0.1", 8888);
        clientSocket.start();
        Channel channel = clientSocket.getChannelFuture().channel();
        channel.writeAndFlush(request);
        System.in.read();
    }

    @Test
    public void serverStart() throws Exception{
        new ServerSocket(null, "baga", 8888).start();
        System.in.read();
    }

}
