package boot.forward.target;

import java.util.concurrent.ConcurrentHashMap;

import tf.epccfe.sys.SysInfo;

import common.comm.tcp.CommTcp;
import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;
import common.util.SDU;


public class TgServChecker implements Runnable {
	private static TxThreadLogger _log = TxThreadLoggerFactory.getInstance(TgServChecker.class);
    
	public static final ConcurrentHashMap<String, String> servTimeHashMap = new ConcurrentHashMap<String, String>();
	
	private TgServInfo tgServ;
	
	public TgServChecker(TgServInfo tgServ) {
		this.tgServ = tgServ;
	}

	@Override
	public void run() {
		if (SysInfo.MonitorFlag) {
			_log.debug("服务器[" + tgServ.getServName() + "]健康检查...");
		}
		
		String time = servTimeHashMap.get(tgServ.getServName());
		if (time == null) {
			refreshServTime(tgServ.getServName());
			return;
		}
		
		CommTcp _ct = null;
		
		try {
			_ct = new CommTcp(tgServ.getServIP(), Integer.parseInt(tgServ.getServPort()));
			_ct.setTimeout(Integer.parseInt(tgServ.getTimeout()) * 1000);
			
			_ct.open();
			
			_ct.write("00000000epcc.000.000.00G0000000000000000000000000000000000000");
			
			byte[] rec = new byte[8];
			_ct.read(rec, 0, 8);
			
			_ct.close();
			
			String result = new String(rec);
			if ("00000000".equals(result)) {
				if (SysInfo.MonitorFlag) {
					_log.debug("服务器[" + tgServ.getServName() + "]健康检查状态正常...");
				}
				refreshServTime(tgServ.getServName());
				// 停用状态变启用
				if (!tgServ.isServActive()) {
					tgServ.setServActive(true);
					_log.debug("服务器[" + tgServ.getServName() + "]健康检查状态恢复！");
				}
			}
			
		} catch (Exception e) {

			if (SysInfo.MonitorFlag) {
				_log.error("服务器[" + tgServ.getServName() + "]健康检查执行失败", e);
			} else {
				_log.error("服务器[" + tgServ.getServName() + "]健康检查执行失败");
			}
		}
        
	}
	
	public static synchronized void refreshServTime(String servName) {
		servTimeHashMap.put(servName, SDU.sysDTime());
	}
}
