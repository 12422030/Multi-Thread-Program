package tf.epccfe.schd;

import java.util.Calendar;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import tf.epccfe.sys.EpccbInfo;
import boot.forward.target.TgServChecker;
import boot.forward.target.TgServInfo;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;
import common.util.SDU;
import common.util.TxtU;

/**
 * @author RockChan
 * @date 2017.08.25
 * @version 1.0
 * @TASKNO 
 */
public class TgServCheckupJob implements Job {
	
    private static TxThreadLogger _log = TxThreadLoggerFactory.getInstance(TgServCheckupJob.class);
    

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
        	_log.debug("【行内】服务器健康检查...");
        	
            JobDataMap jdm = context.getJobDetail().getJobDataMap();

            int delaySecond = 0;
            try {
            	delaySecond = Integer.parseInt(TxtU.text(jdm.getString("delaySecond")));
            } catch (Exception e) {
            }
            delaySecond = Math.max(10, delaySecond); // 最少延迟10秒
            delaySecond = Math.min(60, delaySecond); // 最多延迟60秒
            
        	List<TgServInfo> tgServList = EpccbInfo.epccbCluster.getAllServ();
        	if (tgServList != null && tgServList.size() > 0) {
        		for (TgServInfo serv : tgServList) {
        			// 异步线程连接服务器，若正常返回结果，则刷新最后激活状态时间，若当着状态异常，则直接变成正常
        			Thread servCheckerThread = new Thread(new TgServChecker(serv));
        			servCheckerThread.start();
        			
        			// 检查服务器最后激活状态时间（服务器响应时间可能过长，不能及时返回异常结果，通过最后激活状态时间判断异常状态）
    				String time = TgServChecker.servTimeHashMap.get(serv.getServName());
    				if (time == null) {
    					continue;
    				}
    				int delay = SDU.between(time, SDU.sysDTime(), Calendar.SECOND);
    				if (serv.isServActive() && delay > delaySecond) {
    					serv.setServActive(false);
    					TgServChecker.refreshServTime(serv.getServName());
    					_log.error("服务器[" + serv.getServName() + "]健康检查状态异常，已停止服务！");
    				} else if (delay >= 300) {
    					TgServChecker.refreshServTime(serv.getServName());
    					_log.error("服务器[" + serv.getServName() + "]健康检查状态异常，已停止服务！");
    				}
        		}
        	}
        } catch (Exception e) {
            _log.error("【行内】服务器健康检查失败！", e);
        }
        
        try {
        	// TODO 网联服务器健康检查
        } catch (Exception e) {
            _log.error("【网联】服务器健康检查失败！", e);
        }
    }
}
