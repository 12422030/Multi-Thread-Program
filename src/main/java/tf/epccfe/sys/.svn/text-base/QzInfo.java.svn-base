package tf.epccfe.sys;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class QzInfo {
    private static TxThreadLogger logger = TxThreadLoggerFactory.getInstance(QzInfo.class);
    
    
    public static Properties propEpccfe = new Properties();
	
	public static String InstName = null;
	
	public static void sysInit() {
	    boolean isInitFailed = false;
        
        // 加载配置文件
        InputStream fis = null;
        try {
    		ClassLoader classLoader = SysInfo.class.getClassLoader();
    		URL resource = classLoader.getResource("ttt_epccfe.properties");
    		String filePath = resource.getPath();
            fis = new FileInputStream(filePath);
            InputStreamReader is = new InputStreamReader(fis,"UTF-8"); 
            propEpccfe.load(is);
            is.close();
            fis.close();
        } catch (Exception e) {
            logger.error("获取定时任务配置文件[ttt_epccfe.properties]异常错误！", e);
            logger.info("-----------------系统初始化失败！------------------");
            isInitFailed = true;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
        
        if (isInitFailed) {
            logger.info("-----------------【定时任务】配置文件[ttt_epccfe.properties]加载失败！------------------");
            System.exit(-1);
        }
    }
}
