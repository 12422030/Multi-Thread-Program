package boot.tcp.client;

import java.io.File;
import java.io.FileReader;
import java.net.URL;

import tf.epccfe.sys.SysInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;
import boot.pkg.EpccfeBody;
import boot.pkg.EpccfeHeader;
import boot.pkg.EpccfePkg;
import boot.tcp.codec.LengthHeaderBasedFrameDecoder;
import boot.tcp.codec.LengthHeaderPrepender;
import boot.tcp.codec.TcpXmlDecoder;
import boot.tcp.codec.TcpXmlEncoder;
import boot.tcp.message.TcpXmlMessage;


public class TcpXmlClient {

    public void connect(String host, int port, final int timeout, final TcpXmlClientHandle clientHandle) throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                        	ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(timeout));
                            ch.pipeline().addLast("frameDecoder", new LengthHeaderBasedFrameDecoder(Integer.MAX_VALUE,0,8,0,8));
                            ch.pipeline().addLast("frameEncoder", new LengthHeaderPrepender(8));
                            ch.pipeline().addLast("String-decoder", new StringDecoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast("String-encoder", new StringEncoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast("tcpxml-decoder", new TcpXmlDecoder());
                            ch.pipeline().addLast("tcpxml-encoder", new TcpXmlEncoder());
                            ch.pipeline().addLast("xmlClientHandler", clientHandle);
                        }
                    });

            // 发起异步连接操作
            ChannelFuture f = b.connect(host,port).sync();

            TcpXmlMessage msg = clientHandle.getMsg();
            clientHandle.setMsg(null);
            f.channel().writeAndFlush(msg);

            // 当代客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (Exception e){
        	 System.out.println(e);
        	 throw e;
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int port = 7080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }

		EpccfePkg epccfePkg = new EpccfePkg();
		EpccfeHeader pkgHeader = new EpccfeHeader();
        EpccfeBody pkgBody = new EpccfeBody();
        
		ClassLoader classLoader = SysInfo.class.getClassLoader();
		URL resource = classLoader.getResource("epcc.201.001.01");
		String filePath = resource.getPath();
		File file = new File(filePath);
		char[] chars = null;
		try {
			FileReader reader = new FileReader(file);
			int fileLen = (int)file.length();
			chars = new char[fileLen];
			reader.read(chars);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String content = String.valueOf(chars);
		int rootB = content.indexOf("<root");
		int rootE = content.lastIndexOf("</root>");
		String pkgXml = content.substring(rootB, rootE + 7);
        content = content.substring(rootE + 7);
        int sB = content.lastIndexOf("{S:");
        int sE = content.lastIndexOf("}");
        String pkgS = content.substring(sB, sE + 1);
        String sValue = pkgS.substring(3, pkgS.lastIndexOf("}"));
        pkgBody.setPkgXml(pkgXml);
        pkgBody.setPkgS(pkgS);
        pkgBody.setsValue(sValue);
        epccfePkg.setPkgBody(pkgBody);

        pkgHeader.setMsgTp("epcc.299.999.01");
        pkgHeader.setOriIssrId("G4000311000018");
        pkgHeader.setPyerAcctTp("  ");
        pkgHeader.setPyeeAcctTp("  ");
        pkgHeader.setReservedField("1000F0617T8800      ");
        epccfePkg.setPkgHeader(pkgHeader);
		
        TcpXmlMessage request = new TcpXmlMessage(epccfePkg);
        
        new TcpXmlClient().connect("192.168.20.43", port, 60, new TcpXmlClientHandle(request));

        System.exit(0);
    }
}
