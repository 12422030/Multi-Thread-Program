package boot.server.main;

import org.apache.commons.lang.StringUtils;

import tf.epccfe.sys.SysInfo;

import boot.http.server.EpccfeHttpServer;
import boot.tcp.server.EpccfeTcpServer;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class EpccfeServer1 implements Runnable {
	private static TxThreadLogger serverLogger = TxThreadLoggerFactory.getInstance(EpccfeTcpServer.class);

	private String serverType;
	
	private String oprType;
	
	@SuppressWarnings("unused")
	private EpccfeServer1() {
		
	}
	
	public EpccfeServer1(String serverType, String oprType) {
		this.serverType = serverType;
		this.oprType = oprType;
	}

	public static void main(String[] args) {

		if (args == null || args.length == 0 || StringUtils.isEmpty(args[0]) || "start".equals(args[0])) {

        	serverLogger.info("正在初始化【epccfeServer】前置服务器（版本号：" + SysInfo.SysVersion
        			+ "）...");
        	
    		Thread syncHttpThread = new Thread(new EpccfeServer1("http", "start"));
        	syncHttpThread.start();

//    		Thread syncTcpThread = new Thread(new EpccfeServer1("tcp", "start"));
//        	syncTcpThread.start();
		} else if (args.length == 1 && "stop".equals(args[0])) {
    		Thread syncHttpThread = new Thread(new EpccfeServer1("http", "stop"));
        	syncHttpThread.start();

    		Thread syncTcpThread = new Thread(new EpccfeServer1("tcp", "stop"));
        	syncTcpThread.start();
		}
	}

	@Override
	public void run() {
		if ("http".equals(this.serverType)) {
			if ("start".equals(oprType)) {
	        	serverLogger.info("开始启动前置HTTP服务器...");
	        	try {
					EpccfeHttpServer.run();
				} catch (Exception e) {
		            serverLogger.error("前置HTTP服务器启动异常!", e);
				}
			} else if ("stop".equals(oprType)) {
	        	serverLogger.info("开始停止前置HTTP服务器...");
	        	try {
		        	EpccfeHttpServer.shutdown();
				} catch (Exception e) {
		            serverLogger.error("前置HTTP服务器关闭异常!", e);
				}
			}
		} else if ("tcp".equals(this.serverType)) {
			if ("start".equals(oprType)) {
	        	serverLogger.info("开始启动前置TCP服务器...");
	        	try {
		        	EpccfeTcpServer.run();
				} catch (Exception e) {
		            serverLogger.error("前置TCP服务器启动异常!", e);
				}
			} else if ("stop".equals(oprType)) {
	        	serverLogger.info("开始停止前置TCP服务器...");
	        	try {
		        	EpccfeTcpServer.shutdown();
				} catch (Exception e) {
		            serverLogger.error("前置TCP服务器关闭异常!", e);
				}
			}
		}
	}
}
