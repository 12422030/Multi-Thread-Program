package boot.tcp.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import tf.epccfe.sys.Sys;
import tf.epccfe.sys.SysInfo;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;
import common.util.TxtU;

import boot.pkg.EpccfeHeader;
import boot.pkg.EpccfePkg;
import boot.tcp.message.TcpXmlMessage;

/**
 * @author RockChan
 * @date 2017.08.23
 * @version 1.0
 */
public class TcpXmlEncoder extends MessageToMessageEncoder<TcpXmlMessage> {
	private TxThreadLogger xmlEncoderLogger = TxThreadLoggerFactory.getInstance(TcpXmlEncoder.class);

	@Override
    protected void encode(ChannelHandlerContext ctx, TcpXmlMessage msg,
                          List<Object> out) throws Exception {
		if (msg.getPkg() == null) {
			if (SysInfo.MonitorFlag) {
				xmlEncoderLogger.debug("返回【行内】空结果!"); 
			}
			out.add(fb("", 53));
			return;
		}
		
		EpccfeHeader header = msg.getPkg().getPkgHeader();
		String pkgHeader = fb(header.getMsgTp(), 15).concat(fb(header.getOriIssrId(), 14))
				.concat(fb(header.getPyerAcctTp(), 2)).concat(fb(header.getPyeeAcctTp(), 2))
				.concat(fb(header.getReservedField(), 20));
		String pkgBody = EpccfePkg.XML_HEAD + msg.getPkg().getPkgBody().getPkgXml() 
				+ "\r\n" + msg.getPkg().getPkgBody().getPkgS() + "\r\n";

		String pkgContent = pkgHeader.concat(pkgBody);
		if (SysInfo.MonitorFlag) {
			xmlEncoderLogger.setTxThreadSerial(msg.getPkg().getTxThreadSerial());
			int bodyLength = pkgBody.getBytes(Sys.CHARSET_NAME).length;
			xmlEncoderLogger.debug("发往【行内】TCP请求（或响应）报文长度和内容：【" + bodyLength + "】" + pkgContent); 
	    	
    	}
		
		out.add(pkgContent);
    }
	
	private String fb(String str, int len) {
		return TxtU.fill_blank_after(str, len);
	}
}
