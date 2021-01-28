package tf.epccfe.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import tf.epccfe.schd.SchdMain;

import common.Env;
import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;
import common.util.NumU;
import common.util.TxtU;


public class SysInfo {
    private static TxThreadLogger logger = TxThreadLoggerFactory.getInstance(SysInfo.class);

	/**
	 * 系统配置最后修改时间
	 */
	private static long lastModifiedTime = 0;
	/**
	 * 系统配置文件对象变量
	 */
    private static File file = null;
	
    private static Properties prop = new Properties();
    
    /**
     * 系统版本号
     */
    public static String SysVersion = "";
    
	/**
	 * 网联服务端口号
	 */
	public static int AppEpccsListenPort = 7070;
	/**
	 * 行内服务端口号
	 */
	public static int AppEpccbListenPort = 7080;
	/**
	 * 默认的超时时限（毫秒）
	 */
	public static int DefaultTimeout = 60 * 1000;

	/**
	 * 银行金融机构编码
	 */
	public static String IssrId = "";
	/**
	 * 银行公钥证书序列号
	 */
	public static String SignSN = "";
	/**
	 * 临时文件路径（相对路径）
	 */
	public static String AppTmpPath = "";
	/**
	 * SFTP本地根目标（绝对路径）
	 */
	public static String SftpLocalBasePath = "";
	/**
	 * 是否启用系统性能监控
	 */
	public static boolean MonitorFlag = false;
	/**
	 * 是否启用前置验签标志
	 */
	public static boolean HsmUseFlag = false;
	/**
	 * 是否为定时任务服务器
	 */
	public static boolean SchdServFlag = false;
	
    /**
     * 线程池最大线程数
     */
    public static int ThreadPoolMaxSize = 12;
    
	/**
	 * SFTP文件服务器HOST
	 */
	public static String SftpReqHost = "";
	/**
	 * SFTP文件服务器用户名
	 */
	public static String SftpReqUsername = "";
	/**
	 * SFTP文件服务器用户密码
	 */
	public static String SftpReqPassword = "";
	/**
	 * SFTP文件服务器端口号
	 */
	public static String SftpReqPort = "22";
	/**
	 * SFTP下载文件限速
	 */
	public static int SftpDownloadLimit = 128;
	/**
	 * SFTP文件根路径
	 */
	public static String SftpRootPath = "";
	/**
	 * SFTP是否使用代理
	 */
	public static boolean SftpProxyFlag = false;
	/**
	 * SFTP代理服务器HOST
	 */
	public static String SftpProxyHost = "";
	/**
	 * SFTP代理服务器端口号
	 */
	public static String SftpProxyPort = "";
	/**
	 * SFTP代理服务器代理方式
	 */
	public static String SftpProxyMode = "HTTP";
	

	// 系统初始化
	static {
		try {
			SysInfo.sysInit();
			EpccbInfo.epccbInit();
			EpccsInfo.epccsInit();
			
			// 定时任务
			if (SysInfo.SchdServFlag) {
				SchdMain.schdMain();
			}
		} catch (Exception e) {
			logger.error("系统初始化异常。", e);
            System.exit(-1);
		}
	}
	
	public static void sysInit() {
	    boolean isInitFailed = false;
        
        // 加载配置文件
        InputStream fis = null;
        try {
    	    file = new File(Env.SYS_ETC_DIR + Env.FILE + "epccfe.cfg");
    	    lastModifiedTime = file.lastModified();
            fis = new FileInputStream(file);
            InputStreamReader is = new InputStreamReader(fis,"UTF-8"); 
            prop.load(is);
            is.close();
            fis.close();

    		ClassLoader classLoader = SysInfo.class.getClassLoader();
    		URL resource = classLoader.getResource("application.properties");
    		String filePath = resource.getPath();
            fis = new FileInputStream(filePath);
            is = new InputStreamReader(fis,"UTF-8"); 
            prop.load(is);
            is.close();
            fis.close();
        } catch (Exception e) {
            logger.error("获取配置文件[epccfe.cfg]异常错误！", e);
            logger.info("-----------------系统初始化失败！------------------");
            System.exit(-1);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
        
        SysInfo.SysVersion = CfgParmValue.getValue(prop, "version");
        if (StringUtils.isEmpty(SysInfo.SysVersion)) {
        	SysInfo.SysVersion = "V0.0.1";
        }
        
        SysInfo.AppEpccsListenPort = NumU.getIntValue(CfgParmValue.getValue(prop, "EPCCS_LISTEN_PORT"));
        if (SysInfo.AppEpccsListenPort <= 0) {
            logger.error("获取配置参数项[EPCCS_LISTEN_PORT]异常错误！");
            isInitFailed = true;
        }

        SysInfo.AppEpccbListenPort = NumU.getIntValue(CfgParmValue.getValue(prop, "EPCCB_LISTEN_PORT"));
        if (SysInfo.AppEpccbListenPort <= 0) {
            logger.error("获取配置参数项[EPCCB_LISTEN_PORT]异常错误！");
            isInitFailed = true;
        }
        
        SysInfo.DefaultTimeout = NumU.getIntValue(CfgParmValue.getValue(prop, "DEFAULT_TIMEOUT"));
        if (SysInfo.DefaultTimeout < 0) {
            logger.error("获取配置参数项[DEFAULT_TIMEOUT]异常错误！");
            isInitFailed = true;
        }
        
        SysInfo.IssrId = CfgParmValue.getValue(prop, "ISSRID");
        if (StringUtils.isEmpty(SysInfo.IssrId)) {
            logger.error("获取配置参数项[ISSRID]异常错误！");
            isInitFailed = true;
        }
        
        SysInfo.SignSN = CfgParmValue.getValue(prop, "SIGN_SN");
        if (StringUtils.isEmpty(SysInfo.SignSN)) {
            logger.error("获取配置参数项[SIGN_SN]异常错误！");
            isInitFailed = true;
        }
        
        SysInfo.AppTmpPath = Env.SYS_DIR + CfgParmValue.getValue(prop, "APP_TMP_PATH") + Env.FILE;
        File tmpFolder = new File(SysInfo.AppTmpPath);
        tmpFolder.mkdirs();
        if (SysInfo.AppTmpPath == Env.SYS_DIR || tmpFolder.isFile()) {
            logger.error("获取配置参数项[APP_TMP_PATH]异常错误！");
            isInitFailed = true;
        }

        SysInfo.SftpLocalBasePath = CfgParmValue.getValue(prop, "SFTP_LOCAL_BASE_PATH") + Env.FILE;
        File sftpFolder = new File(SysInfo.SftpLocalBasePath);
        if (SysInfo.SftpLocalBasePath == Env.FILE || sftpFolder.isFile()) {
            logger.error("获取配置参数项[SFTP_LOCAL_BASE_PATH]异常错误！");
            isInitFailed = true;
        }
        sftpFolder.mkdirs();

        SysInfo.MonitorFlag = "1".equals(CfgParmValue.getValue(prop, "MONITOR_FLAG"));
        SysInfo.HsmUseFlag = "1".equals(CfgParmValue.getValue(prop, "HSM_USE_FLAG"));
        
        SysInfo.SchdServFlag = "1".equals(CfgParmValue.getValue(prop, "SCHD_SERV_FLAG"));
        
        SysInfo.ThreadPoolMaxSize = NumU.getIntValue(CfgParmValue.getValue(prop, "THREAD_POOL_MAX_SIZE"));
        if (SysInfo.ThreadPoolMaxSize <= 2) {
            logger.error("获取配置参数项[THREAD_POOL_MAX_SIZE]异常错误！");
            isInitFailed = true;
        }

        
        SysInfo.SftpReqHost = CfgParmValue.getValue(prop, "SFTP_REQ_HOST");
        if (StringUtils.isEmpty(SysInfo.SftpReqHost)) {
            logger.error("获取配置参数项[SFTP_REQ_HOST]异常错误！");
            isInitFailed = true;
        }
        SysInfo.SftpReqUsername = CfgParmValue.getValue(prop, "SFTP_REQ_USERNAME");
        if (StringUtils.isEmpty(SysInfo.SftpReqUsername)) {
            logger.error("获取配置参数项[SFTP_REQ_USERNAME]异常错误！");
            isInitFailed = true;
        }
        SysInfo.SftpReqPassword = CfgParmValue.getValue(prop, "SFTP_REQ_PASSWORD");
        if (StringUtils.isEmpty(SysInfo.SftpReqPassword)) {
            logger.error("获取配置参数项[SFTP_REQ_PASSWORD]异常错误！");
            isInitFailed = true;
        }

        SysInfo.SftpReqPort = CfgParmValue.getValue(prop, "SFTP_REQ_PORT");
        if (NumU.getIntValue(SysInfo.SftpReqPort) <= 0) {
            logger.error("获取配置参数项[SFTP_REQ_PORT]异常错误！");
            isInitFailed = true;
        }

        SysInfo.SftpDownloadLimit = NumU.getIntValue(CfgParmValue.getValue(prop, "SFTP_DOWNLOAD_LIMIT"));
        if (SysInfo.SftpDownloadLimit < 10
        		|| SysInfo.SftpDownloadLimit > 4096) {
            logger.error("获取配置参数项[SFTP_DOWNLOAD_LIMIT]异常错误！");
            isInitFailed = true;
        }

        SysInfo.SftpRootPath = CfgParmValue.getValue(prop, "SFTP_ROOT_PATH");
        if (StringUtils.isEmpty(SysInfo.SftpRootPath)) {
            logger.error("获取配置参数项[SFTP_ROOT_PATH]异常错误！");
            isInitFailed = true;
        }
        
        SysInfo.SftpProxyFlag = "1".equals(CfgParmValue.getValue(prop, "SFTP_PROXY_FLAG"));
        if (SysInfo.SftpProxyFlag) {
	        SysInfo.SftpProxyHost = CfgParmValue.getValue(prop, "SFTP_PROXY_HOST");
	        if (StringUtils.isEmpty(SysInfo.SftpProxyHost)) {
	            logger.error("获取配置参数项[SFTP_PROXY_HOST]异常错误！");
	            isInitFailed = true;
	        }

	        SysInfo.SftpProxyPort = CfgParmValue.getValue(prop, "SFTP_PROXY_PORT");
	        if (NumU.getIntValue(SysInfo.SftpProxyPort) <= 0) {
	            logger.error("获取配置参数项[SFTP_PROXY_PORT]异常错误！");
	            isInitFailed = true;
	        }

	        SysInfo.SftpProxyMode = CfgParmValue.getValue(prop, "SFTP_PROXY_MODE");
	        if (!"HTTP".equals(SysInfo.SftpProxyMode)
	        		&& !"SOCKS4".equals(SysInfo.SftpProxyMode)
	        		&& !"SOCKS5".equals(SysInfo.SftpProxyMode)) {
	            logger.error("获取配置参数项[SFTP_PROXY_MODE]异常错误！");
	            isInitFailed = true;
	        }
        }
        
        if (isInitFailed) {
            logger.info("-----------------系统初始化失败！------------------");
            System.exit(-1);
        }
    }
	
	// 特定系统参数定时刷新
	public static void sysRefresh() {

	    long newTime = file.lastModified();
	    if (newTime == 0) {
            logger.warn(TxtU.msgFormat("获取配置文件{1}异常错误！", file.getName()));
	    } else if (newTime > lastModifiedTime) {
	        InputStream fis = null;
	        try {
	    	    lastModifiedTime = file.lastModified();
	            fis = new FileInputStream(file);
	            prop.load(fis);
	            fis.close();
	        } catch (Exception e) {
	            logger.error("获取配置文件[epccfe.cfg]异常错误！", e);
	        } finally {
	            if (fis != null) {
	                try {
	                    fis.close();
	                } catch (IOException e) {
	                }
	            }
	        }

	        SysInfo.MonitorFlag = "1".equals(CfgParmValue.getValue(prop, "MONITOR_FLAG"));
	        SysInfo.HsmUseFlag = "1".equals(CfgParmValue.getValue(prop, "HSM_USE_FLAG"));
	    }
	}
}
