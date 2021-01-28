package boot.http.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import tf.epccfe.sys.SysInfo;
import boot.pkg.EpccfePkg;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

/**
 * @author RockChan
 * @date 2017.08.23
 * @version 1.0
 */
public abstract class AbstractHttpXmlEncoder<T> extends
		MessageToMessageEncoder<T> {
	private TxThreadLogger xmlEncoderLogger = TxThreadLoggerFactory.getInstance(AbstractHttpXmlEncoder.class);

	protected ByteBuf encode0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		EpccfePkg pkg = (EpccfePkg) msg;
		String xmlStr = EpccfePkg.XML_HEAD + pkg.getPkgBody().getPkgXml()
				+ "\r\n" + pkg.getPkgBody().getPkgS() + "\r\n";

		if (SysInfo.MonitorFlag) {
			xmlEncoderLogger.setTxThreadSerial(pkg.getTxThreadSerial());
			xmlEncoderLogger.debug("【网联】HTTP请求（或响应）报文内容：" + xmlStr); 
    	}
		
		ByteBuf encodeBuf = Unpooled.copiedBuffer(xmlStr, CharsetUtil.UTF_8);
		return encodeBuf;
	}
}
