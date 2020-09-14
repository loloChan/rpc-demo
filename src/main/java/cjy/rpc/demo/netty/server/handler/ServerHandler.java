package cjy.rpc.demo.netty.server.handler;

import cjy.rpc.demo.netty.msg.Request;
import cjy.rpc.demo.netty.msg.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

/**
 * 服务端处理器
 * @author loloChan
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private ApplicationContext applicationContext;

    public ServerHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!(msg instanceof Request)) {
            super.channelRead(ctx, msg);
            return;
        }

        Request request = (Request) msg;

        Response response = new Response(200,request.getRequestId());

        String ref = request.getRef();
        Object bean = applicationContext.getBean(ref);
        Class<?> clazz = Class.forName(request.get_interface());
        //不存在bean，或bean与接口不相符
        if (bean == null || !(clazz.isInstance(bean))) {
            response.setRequestId(request.getRequestId());
            response.setStatus(400);
            response.setCause(new Exception("ref与interface不相符"));
            ctx.channel().writeAndFlush(response);
            return;
        }
        Method method = clazz.getMethod(request.getMethodName(), request.getParamTypes());
        Object result = method.invoke(bean, request.getArgs());
        response.setResult(result);
        ctx.channel().writeAndFlush(response);

        super.channelRead(ctx, msg);

        //释放
        ReferenceCountUtil.release(msg);
    }
}
