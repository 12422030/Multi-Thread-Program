package boot.tcp.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import tf.epccfe.sys.SysInfo;
import boot.pkg.EpccfeBody;
import boot.pkg.EpccfeHeader;
import boot.pkg.EpccfePkg;
import boot.tcp.message.TcpXmlMessage;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

/**
 * @author RockChan
 * @date 2017.08.23
 * @version 1.0
 */
public class TcpXmlDecoder extends MessageToMessageDecoder<String> {
	
	private TxThreadLogger txLogger = TxThreadLoggerFactory.getInstance(TcpXmlDecoder.class, true);

    @Override
    public void decode(ChannelHandlerContext ctx, String reqt,
                          List<Object> out) throws Exception {

		if (SysInfo.MonitorFlag) {
			txLogger.debug("TCP服务端接收到请求（或响应）报文：" + reqt);
    	}
        
    	EpccfePkg epccfePkg = new EpccfePkg();
		EpccfeHeader pkgHeader = new EpccfeHeader();
        EpccfeBody pkgBody = new EpccfeBody();

        String header = reqt.substring(0, 53);
        pkgHeader.setMsgTp(header.substring(0, 15));
        pkgHeader.setOriIssrId(header.substring(15, 29));
        pkgHeader.setPyerAcctTp(header.substring(29, 31));
        pkgHeader.setPyeeAcctTp(header.substring(31, 33));
        pkgHeader.setReservedField(header.substring(33));
        epccfePkg.setPkgHeader(pkgHeader);
        
		String content = reqt.substring(53);
		int rootB = content.indexOf("<root");
		int rootE = content.lastIndexOf("</root>");
		if (rootB < 0 || rootE < 0) {
            sendError(ctx, out);
            return;
		}
		String pkgXml = content.substring(rootB, rootE + 7);
        pkgBody.setPkgXml(pkgXml);
        content = content.substring(rootE + 7);
        int idx = content.lastIndexOf("{S:");
        if (idx >= 0) {
            int sB = content.lastIndexOf("{S:");
            int sE = content.lastIndexOf("}");
            String pkgS = content.substring(sB, sE + 1);
	        String sValue = pkgS.substring(3, pkgS.lastIndexOf("}"));
	        pkgBody.setPkgS(pkgS);
	        pkgBody.setsValue(sValue);
        } else {
        	pkgBody.setPkgS("");
        	pkgBody.setsValue("");
        }

		int msgHeaderB = pkgXml.indexOf("<MsgHeader>");
		int msgHeaderE = pkgXml.lastIndexOf("</MsgHeader>");
		String msgHeaderXml = pkgXml.substring(msgHeaderB, msgHeaderE + 12);
		pkgBody.setMsgHeaderXml(msgHeaderXml);
		int msgBodyB = pkgXml.indexOf("<MsgBody>");
		int msgBodyE = pkgXml.lastIndexOf("</MsgBody>");
		String msgBodyXml = pkgXml.substring(msgBodyB, msgBodyE + 10);
		pkgBody.setMsgBodyXml(msgBodyXml);
		
        epccfePkg.setPkgBody(pkgBody);
        epccfePkg.setTxThreadSerial(txLogger.getTxThreadSerial());
        TcpXmlMessage request = new TcpXmlMessage(epccfePkg);
        

		if (SysInfo.MonitorFlag) {
	        txLogger.debug("TCP服务端接收请求（或响应）报文解析正常！");
    	}
        out.add(request);
    }

    private static void sendError(ChannelHandlerContext ctx, List<Object> out) {
    	EpccfePkg pkg = null;
//    	EpccfeBody pkgBody = new EpccfeBody();
//    	pkgBody.setMsgBodyXml("test");
//		pkg.setPkgBody(pkgBody);
    	TcpXmlMessage msg = new TcpXmlMessage(pkg);
//        ctx.writeAndFlush(msg);
        out.add(msg);
    }
}
