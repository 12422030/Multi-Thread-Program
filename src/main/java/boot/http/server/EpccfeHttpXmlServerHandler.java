package boot.http.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Date;

import tf.epccfe.flow.ERequestSign;
import tf.epccfe.flow.EResponseSign;
import boot.forward.target.epccb.TcpXmlClientForEpccb;
import boot.http.message.HttpXmlRequestMessage;
import boot.http.message.HttpXmlResponseMessage;
import boot.http.method.HeaderContent;
import boot.pkg.EpccfePkg;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class EpccfeHttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequestMessage> {
	
	private TxThreadLogger handlerLogger = TxThreadLoggerFactory.getInstance(EpccfeHttpXmlServerHandler.class);

    public void messageReceived(final ChannelHandlerContext ctx,
                                HttpXmlRequestMessage xmlRequest) throws Exception {
    	Date threadStart = new Date();
    	HttpRequest request = xmlRequest.getRequest();
        EpccfePkg epccfePkg = (EpccfePkg) xmlRequest.getPkg();
        handlerLogger.setTxThreadSerial(epccfePkg.getTxThreadSerial());
        TxThreadLoggerFactory.registerThreadId(epccfePkg.getTxThreadSerial());
        handlerLogger.debug("【交易开始】");
		handlerLogger.debug("接收到【网联】请求报文成功！");
		handlerLogger.debug("接收到【网联】请求Headers : " + HeaderContent.headerContent(request.headers()));
        handlerLogger.debug("接收到【网联】请求内容 :" + epccfePkg);
        
        boolean checkSign = ERequestSign.excSign(epccfePkg);
        
        if (checkSign) {
	        TcpXmlClientForEpccb tClient = new TcpXmlClientForEpccb(epccfePkg);
	        epccfePkg = tClient.execute();
	        
	        if (epccfePkg == null) {
	        	return;
	        } else {
	        	// 使用请求端线程序号覆盖响应后线程序号
	        	epccfePkg.setTxThreadSerial(handlerLogger.getTxThreadSerial());
	        }

	        EResponseSign.excSign(epccfePkg);
        }
        
        handlerLogger.debug("返回给【网联】的响应Headers : " + HeaderContent.headerContent(epccfePkg));
        handlerLogger.debug("返回给【网联】的响应内容 : " + epccfePkg);
    	ChannelFuture future = ctx.writeAndFlush(new HttpXmlResponseMessage(null,
                epccfePkg));
        if (!HttpUtil.isKeepAlive(request)) {
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(@SuppressWarnings("rawtypes") Future future) throws Exception {
                    ctx.close();
                }
            });
        }
        
        long threadMillis = System.currentTimeMillis() - threadStart.getTime();
        int threadSecond = ((int)threadMillis) / 1000;
        String timeWarn = threadSecond == 0 ? "" : threadSecond + "秒"; 
        timeWarn += threadSecond < 3 ? "" : threadSecond < 10 ? "【3秒警告】" : "【10秒警告】";
        handlerLogger.exeTime("[线程全程] 耗时".concat(timeWarn) ,threadStart);
        handlerLogger.debug("【交易结束】");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	handlerLogger.error(cause.getMessage(), cause);
    	ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpXmlRequestMessage msg) throws Exception {
        messageReceived(ctx, msg);
    }
}
