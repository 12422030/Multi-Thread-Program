package boot.forward.target.epccs;

import tf.epccfe.sys.EpccsInfo;
import boot.forward.target.TgServInfo;
import boot.http.client.HttpXmlClient;
import boot.http.client.HttpXmlClientHandle;
import boot.http.message.HttpXmlRequestMessage;
import boot.pkg.EpccfePkg;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class HttpXmlClientForEpccs {
	private TxThreadLogger clientLogger = TxThreadLoggerFactory.getInstance(HttpXmlClientForEpccs.class);

	private EpccfePkg epccfePkg;
	
	@SuppressWarnings("unused")
	private HttpXmlClientForEpccs() {
		
	}

	public HttpXmlClientForEpccs(EpccfePkg epccfePkg) {
		this.epccfePkg = epccfePkg;
	}
	
	public EpccfePkg execute() throws Exception {
		TgServInfo serv = EpccsInfo.epccsCluster.getTgServ();
		
		HttpXmlRequestMessage request = new HttpXmlRequestMessage(null, epccfePkg, serv.getServURL());
		HttpXmlClientHandle clientHandle = new HttpXmlClientHandle(request);

		clientLogger.debug("发往【网联】请求内容 : " + epccfePkg);
        new HttpXmlClient().connect(serv.getServIP(), Integer.parseInt(serv.getServPort()), Integer.parseInt(serv.getTimeout()), clientHandle);
		
		return clientHandle.getResponse().getResult();
	}
}
