package tf.epccfe.schd;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import tf.epccfe.sys.SysInfo;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class SysInfoRefreshJob implements Job {
	
    private static TxThreadLogger _log = TxThreadLoggerFactory.getInstance(SysInfoRefreshJob.class);
    

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
        	SysInfo.sysRefresh();
        } catch (Exception e) {
            _log.error("系统参数定时刷新失败", e);
        }
    }
}
