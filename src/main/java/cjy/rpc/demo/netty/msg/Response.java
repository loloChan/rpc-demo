package cjy.rpc.demo.netty.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务端响应信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {


    /**
     * 响应码
     */
    private int status;

    /**
     * 响应消息所属请求的id
     */
    private String requestId;

    /**
     * 服务调用返回值
     */
    private Object result;

    /**
     * 异常信息
     */
    private Throwable cause;

    public Response(int status, String requestId) {
        this.status = status;
        this.requestId = requestId;
    }

}
