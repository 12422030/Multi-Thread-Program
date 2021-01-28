package boot.http.codec;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpUtil;

import java.util.List;

import tf.epccfe.sys.SysInfo;
import boot.enumeration.PkgHeaderNames;
import boot.http.message.HttpXmlResponseMessage;
import boot.pkg.EpccfeHeader;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

/**
 * @author RockChan
 * @date 2017.08.23
 * @version 1.0
 */
public class HttpXmlResponseEncoder extends
        AbstractHttpXmlEncoder<HttpXmlResponseMessage> {
	private TxThreadLogger xmlEncoderLogger = TxThreadLoggerFactory.getInstance(HttpXmlResponseEncoder.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.netty.handler.codec.MessageToMessageEncoder#encode(io.netty.channel
     * .ChannelHandlerContext, java.lang.Object, java.util.List)
     */
    protected void encode(ChannelHandlerContext ctx, HttpXmlResponseMessage msg,
                          List<Object> out) throws Exception {
        ByteBuf body = encode0(ctx, msg.getResult());
        FullHttpResponse response = msg.getHttpResponse();
        if (response == null) {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, body);
        } else {
            response = new DefaultFullHttpResponse(msg.getHttpResponse()
                    .protocolVersion(), msg.getHttpResponse().status(),
                    body);
        }
        HttpHeaders headers = response.headers();
        headers.set(HttpHeaderNames.CONTENT_TYPE, "application/xml;charset=utf-8");
        EpccfeHeader pkgHeader = msg.getResult().getPkgHeader();
        headers.set(PkgHeaderNames.MSG_TP.getName(), pkgHeader.getMsgTp());
        headers.set(PkgHeaderNames.ORI_ISSR_ID.getName(), pkgHeader.getOriIssrId());
        headers.set(PkgHeaderNames.PYER_ACCT_TP.getName(), pkgHeader.getPyerAcctTp());
        headers.set(PkgHeaderNames.PYEE_ACCT_TP.getName(), pkgHeader.getPyeeAcctTp());
        headers.set(PkgHeaderNames.RESERVED_FIELD.getName(), pkgHeader.getReservedField());
        HttpUtil.setContentLength(response, body.readableBytes());

    	if (SysInfo.MonitorFlag) {
    		xmlEncoderLogger.setTxThreadSerial(msg.getResult().getTxThreadSerial());
    		xmlEncoderLogger.debug("返回【网联】HTTP响应报文！"+ctx.toString());
    	}
    	
        out.add(response);
    }
}
