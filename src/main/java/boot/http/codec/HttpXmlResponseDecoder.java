package boot.http.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.List;

import tf.epccfe.sys.Sys;
import tf.epccfe.sys.SysInfo;
import boot.http.message.HttpXmlResponseMessage;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

/**
 * @author RockChan
 * @date 2017.08.23
 * @version 1.0
 */
public class HttpXmlResponseDecoder extends
        AbstractHttpXmlDecoder<FullHttpResponse> {
	private static TxThreadLogger decoderLogger = TxThreadLoggerFactory.getInstance(HttpXmlResponseDecoder.class);

    @Override
	protected void decode(ChannelHandlerContext ctx, FullHttpResponse resp,
			List<Object> out) throws Exception {
		
		if (!resp.status().equals(HttpResponseStatus.OK)) {
    		decoderLogger.debug("HTTP服务端接收到【网联】异常响应：" + ctx.toString());
    		decoderLogger.debug("HTTP服务端接收到【网联】异常响应状态：" + resp.status().toString());
    		decoderLogger.debug("HTTP服务端接收到【网联】异常响应内容：" + resp.content().toString(Sys.UTF_8));
    		return;
		}
		
    	if (SysInfo.MonitorFlag) {
    		decoderLogger.debug("HTTP服务端接收到【网联】响应：" + ctx.toString());
    	}

		HttpXmlResponseMessage resMsg = new HttpXmlResponseMessage(resp,
				decode0(ctx, resp.content()));
		
        if (resMsg.getResult().getPkgBody() == null) {
        	decoderLogger.debug("HTTP服务端接收到【网联】异常响应内容：" + resp.content().toString(Sys.UTF_8));
    		return;
        }
        
		out.add(resMsg);
	}
}
