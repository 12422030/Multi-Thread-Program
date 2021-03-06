package tf.epccfe.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import boot.forward.target.TgServCluster;
import boot.forward.target.TgServInfo;

import common.Env;
import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;


public class EpccsInfo {
    private static TxThreadLogger logger = TxThreadLoggerFactory.getInstance(EpccsInfo.class);

    private static File file = null; 
	
    private static Properties prop = new Properties();

    /** 
     * 配置参数容器
     */
    private static final Map<String, String> PROPS = new HashMap<String, String>();
    
    /**
     * 行内渠道服务器列表
     */
    public static TgServCluster epccsCluster = new TgServCluster();
    
	
	public static void epccsInit() {
	    boolean isInitFailed = false;
        
        // 加载配置文件
        InputStream fis = null;
        try {
    	    file = new File(Env.SYS_ETC_DIR + Env.FILE + "host_epccs.cfg");
            fis = new FileInputStream(file);
            InputStreamReader is = new InputStreamReader(fis,"UTF-8"); 
            prop.load(is);
            is.close();
            fis.close();
        } catch (Exception e) {
            logger.error("获取配置文件[host_epccs.cfg]异常错误！", e);
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

		// 参数配置信息载入
		Iterator<String> keys = prop.stringPropertyNames().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			PROPS.put(key, CfgParmValue.getValue(prop, key));
		}
		Iterator<String> it = PROPS.keySet().iterator();
		String key;
		String proField;
		String hostId;
		String activeId = "0";
		Map<String, TgServInfo> tempMap = new HashMap<String, TgServInfo>();
		while (it.hasNext()) {
			key = it.next();
			if (key.startsWith("host")) {
				proField = key.substring(key.lastIndexOf(".") + 1);
				hostId = key.substring(0, key.indexOf("."));
				TgServInfo tsi = tempMap.get(hostId);
				if (tsi == null) {
					tsi = new TgServInfo();
					tsi.setServActive(true);
					tempMap.put(hostId, tsi);
				}
				if ("name".equals(proField)) {
					tsi.setServName(PROPS.get(key));
				} else if ("desc".equals(proField)) {
					tsi.setServDesc(PROPS.get(key));
				} else if ("ip".equals(proField)) {
					tsi.setServIP(PROPS.get(key));
				} else if ("port".equals(proField)) {
					tsi.setServPort(PROPS.get(key));
				} else if ("url".equals(proField)) {
					tsi.setServURL(PROPS.get(key));
				} else if ("timeout".equals(proField)) {
					tsi.setTimeout(PROPS.get(key));
				}
			} else if ("active_host_epccs_id".equals(key)) {
				activeId = PROPS.get(key);
			}
		}

		// 【行内渠道】配置参数服务器信息初始化
		if ("0".equals(activeId)) {
			epccsCluster.setActiveStandbyMode(false);
		} else {
			epccsCluster.setActiveStandbyMode(true);
		}
		it = tempMap.keySet().iterator();
		while (it.hasNext()) {
			key = it.next();
			TgServInfo tsi = tempMap.get(key);
			if (activeId.equals(key.substring(10))) {
				epccsCluster.setActiveServ(tsi);
			} else {
				epccsCluster.addStandbyServ(tsi);
			}
		}
		
		if (epccsCluster.getActiveServ() == null && epccsCluster.getStandbyServSize() == 0) {
			logger.error("获取【网联】配置参数服务器信息异常错误！");
			isInitFailed = true;
		}
		
		if (epccsCluster.getActiveServ() == null && epccsCluster.isActiveStandbyMode()) {
			logger.error("获取【网联】配置参数服务器信息异常错误！[主服务器编号不正确]");
			isInitFailed = true;
		}
        
        if (isInitFailed) {
            logger.info("-----------------系统初始化失败！------------------");
            System.exit(-1);
        }
    }
}
