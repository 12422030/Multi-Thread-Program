package boot.tcp.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;

import tf.epccfe.flow.BRequestSign;
import boot.pkg.EpccfePkg;
import boot.tcp.message.TcpXmlMessage;

import common.Env;
import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class EpccfeTcpXmlServerHandler extends SimpleChannelInboundHandler<TcpXmlMessage> {

	private TxThreadLogger handlerLogger = TxThreadLoggerFactory.getInstance(EpccfeTcpXmlServerHandler.class);

    public void messageReceived(final ChannelHandlerContext ctx,
                                TcpXmlMessage xmlRequest) throws Exception {
    	Date threadStart = new Date();
    	EpccfePkg epccfePkg = xmlRequest.getPkg();
        handlerLogger.setTxThreadSerial(epccfePkg.getTxThreadSerial());
        TxThreadLoggerFactory.registerThreadId(epccfePkg.getTxThreadSerial());
        handlerLogger.debug("【交易开始】");
        handlerLogger.debug("接收到【行内】请求报文成功！");
        handlerLogger.debug("接收到【行内】的报文内容 : " + epccfePkg);

        boolean checkSign = BRequestSign.excSign(epccfePkg);
        
        // test
        String longstr = "";
        FileReader fr=new FileReader("D:\\info.log");
        //可以换成工程目录下的其他文本文件
        BufferedReader br=new BufferedReader(fr);
        while(br.readLine()!=null){
        	longstr += br.readLine() + Env.LINE;
        }
        br.close();
        String[] sp1 = longstr.split(Env.LINE);
        System.out.println("sp1.length = " + sp1.length);
        String[][] sp2 = new String[sp1.length][];
        int i = 0;
        for (String sp : sp1) {
        	sp2[i] = sp.split("-");
            System.out.println("sp2["+i+"].length = " + sp2[i].length);
            i++;
        }
        	
        
//        if (checkSign) {
//        	
//        	String msgTp = epccfePkg.getPkgHeader().getMsgTp();
//        	if ("epcc.999.999.01".equals(msgTp)) { // 对账明细文件确认报文
//        		Epcc999FLow epcc999Flow = new Epcc999FLow(epccfePkg);
//        		epccfePkg = epcc999Flow.execute();
//		        
//		        if (epccfePkg == null) {
//		        	return;
//		        }
//        	} else {
//		        HttpXmlClientForEpccs tClient = new HttpXmlClientForEpccs(epccfePkg);
//		        epccfePkg = tClient.execute();
//		        
//		        if (epccfePkg == null) {
//		        	return;
//		        }
//		        
//		        BResponseSign.excSign(epccfePkg);
//        	}
//        }

        handlerLogger.debug("返回给【行内】的响应内容 : " + epccfePkg);
        ctx.writeAndFlush(new TcpXmlMessage(epccfePkg));
        
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
    protected void channelRead0(ChannelHandlerContext ctx, TcpXmlMessage msg) throws Exception {

		if (msg.getPkg() == null) {
	        ctx.writeAndFlush(msg);
			return;
		}
        messageReceived(ctx, msg);
    }

}
