package tf.epccfe.schd.sftp;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import tf.epccfe.sftp.util.SFTPAllFile;
import tf.epccfe.sftp.util.SFTPChannel;
import tf.epccfe.sys.SysInfo;

import com.jcraft.jsch.ChannelSftp;
import common.Env;
import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;
import common.util.FmU;
import common.util.SDU;

/**
 * 定时下载所有对账明细文件
 * @author RockChan
 *
 */
public class SFTPAllStatementsFileJob implements Job {
	
    private static TxThreadLogger _log = TxThreadLoggerFactory.getInstance(SFTPAllStatementsFileJob.class);
    

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
    	_log.info("定时下载所有对账明细文件开始！");
        JobDataMap jdm = context.getJobDetail().getJobDataMap();
        String servers = jdm.getString("SFTP-servers");
        if (StringUtils.isEmpty(servers)) {
        	_log.info("未配置对账明细文件服务器！");
        	return;
        }
        String[] servs = servers.split(";");
    	
    	SFTPChannel channel = null;
    	ChannelSftp chSftp = null;
    	try {
            channel = new SFTPChannel();
            
            for (String serv : servs) {
            	int idx = serv.indexOf(":");
            	if (idx <= 0) {
                	_log.warn("对账明细文件服务器配置不正确！" + serv);
            		continue;
            	}
            	String host = serv.substring(0, serv.indexOf(":"));
            	String port = serv.substring(serv.indexOf(":") + 1);

                chSftp = channel.getChannel(host, port, 60000);
                
                String datePath = SDU.sysDate();

                String parentPath = SysInfo.SftpRootPath + Env.FILE + SysInfo.IssrId + Env.FILE + datePath;
                String dstFolder = SysInfo.SftpLocalBasePath + Env.FILE + datePath;

                try{
    				SFTPAllFile.downloadAllNewFile(channel, chSftp, parentPath, dstFolder, true);
    	            
    	            Calendar now = Calendar.getInstance();	
//    	            int hour = now.get(Calendar.HOUR_OF_DAY);
//    	            if (hour < 2) { // 凌晨2点(不含）前继续同步前一日文件
//    	            	now.add(Calendar.DATE, -1);
//    	            	datePath = FmU.formatDate(now.getTime(), "yyyyMMdd");
//    	            	parentPath = SysInfo.SftpRootPath + Env.FILE + SysInfo.IssrId + Env.FILE + datePath;
//    	            	dstFolder = SysInfo.SftpLocalBasePath + Env.FILE + datePath;
    //	
//    	    			downloadAllNewFile(channel, chSftp, parentPath, dstFolder);
//    	            }
    	            
    	            // 前一天
                	now.add(Calendar.DATE, -1);
                	datePath = FmU.formatDate(now.getTime(), "yyyyMMdd");
                	parentPath = SysInfo.SftpRootPath + Env.FILE + SysInfo.IssrId + Env.FILE + datePath;
                	dstFolder = SysInfo.SftpLocalBasePath + Env.FILE + datePath;
                	SFTPAllFile.downloadAllNewFile(channel, chSftp, parentPath, dstFolder, true);
        			
        			// 后一天
                	now.add(Calendar.DATE, 2);
                	datePath = FmU.formatDate(now.getTime(), "yyyyMMdd");
                	parentPath = SysInfo.SftpRootPath + Env.FILE + SysInfo.IssrId + Env.FILE + datePath;
                	dstFolder = SysInfo.SftpLocalBasePath + Env.FILE + datePath;
                	SFTPAllFile.downloadAllNewFile(channel, chSftp, parentPath, dstFolder, true);
        			
                } finally {
                    chSftp.quit();
                    channel.closeChannel();
                }
            }
            
            _log.info("定时下载所有对账明细文件成功！");
        } catch (Exception e) {
            _log.error("定时下载所有对账明细文件失败", e);
        }
    }
}
