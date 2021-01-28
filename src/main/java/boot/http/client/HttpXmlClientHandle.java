package boot.http.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import boot.http.message.HttpXmlRequestMessage;
import boot.http.message.HttpXmlResponseMessage;
import boot.http.method.HeaderContent;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class HttpXmlClientHandle extends
        SimpleChannelInboundHandler<HttpXmlResponseMessage> {
	private TxThreadLogger clientLogger = TxThreadLoggerFactory.getInstance(HttpXmlClientHandle.class);

	private HttpXmlRequestMessage request;
	
	private HttpXmlResponseMessage response;
	
	private String reqSerial;
	
	@SuppressWarnings("unused")
	private HttpXmlClientHandle() {
		
	}
	
	public HttpXmlClientHandle(HttpXmlRequestMessage request) {
		super();
		this.request = request;
		this.reqSerial = request.getPkg().getTxThreadSerial();
	}

    private void messageReceived(ChannelHandlerContext ctx,
    		HttpXmlResponseMessage msg) throws Exception {

    	clientLogger.setTxThreadSerial(this.reqSerial);
    	clientLogger.debug("【网联】响应Headers : " + HeaderContent.headerContent(msg.getHttpResponse().headers()));
    	clientLogger.debug("【网联】响应内容 : " + msg.getResult());
        
        msg.getResult().setTxThreadSerial(this.reqSerial);
        this.setResponse(msg);

        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	this.response = new HttpXmlResponseMessage(null, null);
        cause.printStackTrace();
        ctx.channel().close();
    }


	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			HttpXmlResponseMessage msg) throws Exception {
		messageReceived(ctx, msg);
	}

	public HttpXmlRequestMessage getRequest() {
		return request;
	}

	public void setRequest(HttpXmlRequestMessage request) {
		this.request = request;
	}

	public HttpXmlResponseMessage getResponse() {
		return response;
	}

	public void setResponse(HttpXmlResponseMessage response) {
		this.response = response;
	}
}