package boot.forward.target.epccb;

import tf.epccfe.sys.EpccbInfo;
import boot.forward.target.TgServInfo;
import boot.pkg.EpccfePkg;
import boot.tcp.client.TcpXmlClient;
import boot.tcp.client.TcpXmlClientHandle;
import boot.tcp.message.TcpXmlMessage;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class TcpXmlClientForEpccb {
	private TxThreadLogger clientLogger = TxThreadLoggerFactory.getInstance(TcpXmlClientForEpccb.class);

	private EpccfePkg epccfePkg;
	
	@SuppressWarnings("unused")
	private TcpXmlClientForEpccb() {
		
	}
	
	public TcpXmlClientForEpccb(EpccfePkg epccfePkg) {
		this.epccfePkg = epccfePkg;
	}

	public EpccfePkg execute() throws Exception {
		TgServInfo serv = EpccbInfo.epccbCluster.getTgServ();
		
        TcpXmlMessage request = new TcpXmlMessage(epccfePkg);
        TcpXmlClientHandle clientHandle = new TcpXmlClientHandle(request);
        
        clientLogger.debug("发往【行内】请求内容 : " + epccfePkg);
        new TcpXmlClient().connect(serv.getServIP(), Integer.parseInt(serv.getServPort()), Integer.parseInt(/*serv.getTimeout()*/ "3000"), clientHandle);
        
        if (clientHandle.getMsg() == null) {
        	return null;
        }
        
		return clientHandle.getMsg().getPkg();
	}
}
