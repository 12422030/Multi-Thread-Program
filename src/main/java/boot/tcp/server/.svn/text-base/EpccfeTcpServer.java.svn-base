package boot.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import org.apache.commons.lang.StringUtils;

import tf.epccfe.sys.SysInfo;
import boot.tcp.codec.LengthHeaderBasedFrameDecoder;
import boot.tcp.codec.LengthHeaderPrepender;
import boot.tcp.codec.TcpXmlDecoder;
import boot.tcp.codec.TcpXmlEncoder;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;


public class EpccfeTcpServer {
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
                                ch.pipeline().addLast("frameDecoder", new LengthHeaderBasedFrameDecoder(Integer.MAX_VALUE,0,8,0,8));
                                ch.pipeline().addLast("frameEncoder", new LengthHeaderPrepender(8));
                                ch.pipeline().addLast("String-decoder", new StringDecoder(CharsetUtil.UTF_8));
                                ch.pipeline().addLast("String-encoder", new StringEncoder(CharsetUtil.UTF_8));
                                ch.pipeline().addLast("tcpxml-decoder", new TcpXmlDecoder());
                                ch.pipeline().addLast("tcpxml-encoder", new TcpXmlEncoder());
                                ch.pipeline().addLast(new EpccfeTcpXmlServerHandler());
                            }
                        });

		Channel ch = b.bind(SysInfo.AppEpccbListenPort).sync().channel();
		serverLogger.info("前置TCP服务器启动成功! TCP Server listening on " + SysInfo.AppEpccbListenPort);

		ch.closeFuture().sync();
	}
	
	public static void shutdown() {
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
		serverLogger.info("前置TCP服务器已停止!");
	}
	
	public static void main(String[] args) {
		if (args == null || args.length == 0 || StringUtils.isEmpty(args[0]) || "start".equals(args[0])) {
	        try {
	        	serverLogger.info("开始启动前置服务器...");
	        	EpccfeTcpServer.run();
	        } catch (Exception e) {  
	            serverLogger.error("前置服务器启动异常!", e);
	        }
		} else if (args.length == 1 && "stop".equals(args[0])) {
	        try {
	        	serverLogger.info("开始停止前置服务器...");
	        	EpccfeTcpServer.shutdown();
	        } catch (Exception e) {  
	            serverLogger.error("前置服务器关闭异常!", e);
	        }
		}
	}
}
