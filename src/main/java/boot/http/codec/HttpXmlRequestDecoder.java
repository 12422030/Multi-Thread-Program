package boot.http.codec;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import java.util.List;

import tf.epccfe.sys.SysInfo;
import boot.http.message.HttpXmlRequestMessage;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

/**
 * @author RockChan
 * @date 2017.08.23
 * @version 1.0
 */
public class HttpXmlRequestDecoder extends
        AbstractHttpXmlDecoder<FullHttpRequest> {
	private static TxThreadLogger decoderLogger = TxThreadLoggerFactory.getInstance(HttpXmlRequestDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest reqt,
                          List<Object> out) throws Exception {
    	if (SysInfo.MonitorFlag) {
    		decoderLogger.debug("HTTP服务端接收到请求：" + ctx.toString());
    	}
        if (!reqt.decoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }
        HttpXmlRequestMessage request = new HttpXmlRequestMessage(reqt, decode0(ctx,
                reqt.content()));
        if (request.getPkg().getPkgBody() == null) {
            sendError(ctx, UNAUTHORIZED);
            return;
        }
        out.add(request);
    }

    private static void sendError(ChannelHandlerContext ctx,
                                  HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                status, Unpooled.copiedBuffer("Failure: " + status.toString()
                + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/xml;charset=utf-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
