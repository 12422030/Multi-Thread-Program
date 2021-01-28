package boot.tcp.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import boot.tcp.message.TcpXmlMessage;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class TcpXmlClientHandle extends
        SimpleChannelInboundHandler<TcpXmlMessage> {
	private TxThreadLogger clientLogger = TxThreadLoggerFactory.getInstance(TcpXmlClientHandle.class);

	private TcpXmlMessage msg;
	
	private String reqSerial;
	
	@SuppressWarnings("unused")
	private TcpXmlClientHandle() {
		
	}
	
	public TcpXmlClientHandle(TcpXmlMessage msg) {
		super();
		this.msg = msg;
		this.reqSerial = msg.getPkg().getTxThreadSerial();
	}

    private void messageReceived(ChannelHandlerContext ctx,
    		TcpXmlMessage msg) throws Exception {
    	
    	clientLogger.setTxThreadSerial(this.reqSerial);
    	clientLogger.debug("【行内】响应内容 : " + msg.getPkg());
        
        msg.getPkg().setTxThreadSerial(this.reqSerial);
        this.setMsg(msg);

        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	this.msg.setPkg(null);
        cause.printStackTrace();
        ctx.channel().close();
    }


	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			TcpXmlMessage msg) throws Exception {
		messageReceived(ctx, msg);
	}

	public TcpXmlMessage getMsg() {
		return msg;
	}

	public void setMsg(TcpXmlMessage msg) {
		this.msg = msg;
	}
}