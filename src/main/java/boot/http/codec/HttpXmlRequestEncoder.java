package boot.http.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

import java.util.List;

import tf.epccfe.sys.SysInfo;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

import boot.enumeration.PkgHeaderNames;
import boot.http.message.HttpXmlRequestMessage;
import boot.pkg.EpccfeHeader;

/**
 * @author RockChan
 * @date 2017.08.23
 * @version 1.0
 */
public class HttpXmlRequestEncoder extends
        AbstractHttpXmlEncoder<HttpXmlRequestMessage> {
	private TxThreadLogger xmlEncoderLogger = TxThreadLoggerFactory.getInstance(HttpXmlRequestEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlRequestMessage reqMsg,
                          List<Object> out) throws Exception {
        ByteBuf body = encode0(ctx, reqMsg.getPkg());
        FullHttpRequest request = reqMsg.getRequest();
        if (request == null) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.POST, reqMsg.getReqUrl(), body);
            HttpHeaders headers = request.headers();
            headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            headers.set(HttpHeaderNames.CONTENT_TYPE, "application/xml;charset=utf-8");
            headers.set(HttpHeaderNames.HOST, "hostname");
            
            EpccfeHeader pkgHeader = reqMsg.getPkg().getPkgHeader();
            headers.set(PkgHeaderNames.MSG_TP.getName(), pkgHeader.getMsgTp());
            headers.set(PkgHeaderNames.ORI_ISSR_ID.getName(), pkgHeader.getOriIssrId());
            headers.set(PkgHeaderNames.PYER_ACCT_TP.getName(), pkgHeader.getPyerAcctTp());
            headers.set(PkgHeaderNames.PYEE_ACCT_TP.getName(), pkgHeader.getPyeeAcctTp());
            headers.set(PkgHeaderNames.RESERVED_FIELD.getName(), pkgHeader.getReservedField());
        }
        HttpUtil.setContentLength(request, body.readableBytes());
        
    	if (SysInfo.MonitorFlag) {
    		xmlEncoderLogger.setTxThreadSerial(reqMsg.getPkg().getTxThreadSerial());
    		xmlEncoderLogger.debug("发往【网联】HTTP请求报文！"+ctx.toString());
    	}
        out.add(request);
    }

}
