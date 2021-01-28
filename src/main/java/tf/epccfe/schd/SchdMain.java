package tf.epccfe.schd;

import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import tf.epccfe.sys.QzInfo;

import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

/**
 * 系统平台定时功能调度程序
 * @author RockChan
 * @date 2017.08.24
 * @version 1.0
 * @TASKNO S.003
 */
public class SchdMain {
    private static TxThreadLogger schdLogger = TxThreadLoggerFactory.getInstance(SchdMain.class);

    /**
     * @param args
     * @throws Exception
     */
    public static void schdMain() throws Exception {
        
        QzInfo.sysInit();
        
        SchedulerFactory sfEteller = new StdSchedulerFactory(QzInfo.propEpccfe);
        Scheduler schedEteller = sfEteller.getScheduler();
        schedEteller.start();
        schdLogger.info("【网联接入前置】定时任务启动成功！");
    }

}
