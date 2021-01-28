package boot.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import org.apache.commons.lang.StringUtils;

import tf.epccfe.sys.SysInfo;
import boot.http.codec.HttpXmlRequestDecoder;
import boot.http.codec.HttpXmlResponseEncoder;
import boot.tcp.server.EpccfeTcpServer;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;


public class EpccfeHttpServer {
	private static TxThreadLogger serverLogger = TxThreadLoggerFactory.getInstance(EpccfeTcpServer.class);

	private static final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private static final EventLoopGroup workerGroup = new NioEventLoopGroup(SysInfo.ThreadPoolMaxSize);
    
	public static void run() throws Exception {

		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                                ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                                ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                                // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                                ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                                ch.pipeline().addLast("xml-decoder", new HttpXmlRequestDecoder());
                                ch.pipeline().addLast("xml-encoder", new HttpXmlResponseEncoder());
                                ch.pipeline().addLast(new EpccfeHttpXmlServerHandler());
                            }
                        }).option(ChannelOption.SO_BACKLOG, 128) 
                .childOption(ChannelOption.SO_KEEPALIVE, true);

		Channel ch = b.bind(SysInfo.AppEpccsListenPort).sync().channel();
		serverLogger.info("前置HTTP服务器启动成功! Http Server listening on " + SysInfo.AppEpccsListenPort);

		ch.closeFuture().sync();
	}
	
	public static void shutdown() {
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
		serverLogger.info("前置HTTP服务器已停止!");
	}
	
	public static void main(String[] args) {
		if (args == null || args.length == 0 || StringUtils.isEmpty(args[0]) || "start".equals(args[0])) {
	        try {
	        	serverLogger.info("开始启动前置HTTP服务器...");
	        	EpccfeHttpServer.run();
	        } catch (Exception e) {  
	            serverLogger.error("前置HTTP服务器启动异常!", e);
	        }
		} else if (args.length == 1 && "stop".equals(args[0])) {
	        try {
	        	serverLogger.info("开始停止前置HTTP服务器...");
	        	EpccfeHttpServer.shutdown();
	        } catch (Exception e) {  
	            serverLogger.error("前置HTTP服务器关闭异常!", e);
	        }
		}
	}
}
