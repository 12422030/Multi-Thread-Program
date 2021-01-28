package tf.epccfe.schd.sftp;

import java.util.Calendar;

import org.quartz.Job;
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
 * 定时下载当时所有文件
 * @author RockChan
 *
 */
public class SFTPAllFileJob implements Job {
	
    private static TxThreadLogger _log = TxThreadLoggerFactory.getInstance(SFTPAllFileJob.class);
    

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
    	_log.info("定时下载当时所有文件开始！");
    	
    	SFTPChannel channel = null;
    	ChannelSftp chSftp = null;
    	try {
            channel = new SFTPChannel();
            chSftp = channel.getChannel(60000);
            
            String datePath = SDU.sysDate();

            String parentPath = SysInfo.SftpRootPath + Env.FILE + SysInfo.IssrId + Env.FILE + datePath;
            String dstFolder = SysInfo.SftpLocalBasePath + Env.FILE + datePath;

            try{
            	SFTPAllFile.downloadAllNewFile(channel, chSftp, parentPath, dstFolder);
	            
	            Calendar now = Calendar.getInstance();	
//	            int hour = now.get(Calendar.HOUR_OF_DAY);
//	            if (hour < 2) { // 凌晨2点(不含）前继续同步前一日文件
//	            	now.add(Calendar.DATE, -1);
//	            	datePath = FmU.formatDate(now.getTime(), "yyyyMMdd");
//	            	parentPath = SysInfo.SftpRootPath + Env.FILE + SysInfo.IssrId + Env.FILE + datePath;
//	            	dstFolder = SysInfo.SftpLocalBasePath + Env.FILE + datePath;
//	
//	    			downloadAllNewFile(channel, chSftp, parentPath, dstFolder);
//	            }
	            
	            // 前一天
            	now.add(Calendar.DATE, -1);
            	datePath = FmU.formatDate(now.getTime(), "yyyyMMdd");
            	parentPath = SysInfo.SftpRootPath + Env.FILE + SysInfo.IssrId + Env.FILE + datePath;
            	dstFolder = SysInfo.SftpLocalBasePath + Env.FILE + datePath;
            	SFTPAllFile.downloadAllNewFile(channel, chSftp, parentPath, dstFolder);
    			
    			// 后一天
            	now.add(Calendar.DATE, 2);
            	datePath = FmU.formatDate(now.getTime(), "yyyyMMdd");
            	parentPath = SysInfo.SftpRootPath + Env.FILE + SysInfo.IssrId + Env.FILE + datePath;
            	dstFolder = SysInfo.SftpLocalBasePath + Env.FILE + datePath;
            	SFTPAllFile.downloadAllNewFile(channel, chSftp, parentPath, dstFolder);
    			
            } finally {
                chSftp.quit();
                channel.closeChannel();
            }
            
            _log.info("定时下载当时所有文件成功！");
        } catch (Exception e) {
            _log.error("定时下载当时所有文件失败", e);
        }
    }
}
