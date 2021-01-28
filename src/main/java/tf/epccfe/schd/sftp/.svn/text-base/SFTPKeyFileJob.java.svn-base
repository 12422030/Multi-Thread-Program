package tf.epccfe.schd.sftp;

import java.io.File;
import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import tf.epccfe.sftp.util.SFTPChannel;
import tf.epccfe.sftp.util.SFTPConstants;
import tf.epccfe.sys.SysInfo;

import com.jcraft.jsch.ChannelSftp;
import common.Env;
import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;
import common.util.FmU;

/**
 * 密钥文件定时下载
 * @author RockChan
 *
 * 前一日12点生成密钥文件
 */
public class SFTPKeyFileJob implements Job {
	
    private static TxThreadLogger _log = TxThreadLoggerFactory.getInstance(SFTPKeyFileJob.class);
    

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
    	_log.info("定时下载密钥文件开始！");
    	
    	SFTPChannel channel = null;
    	ChannelSftp chSftp = null;
    	try {
    		
            
            channel = new SFTPChannel();
            chSftp = channel.getChannel(60000);
            
            Calendar now = Calendar.getInstance();
        	now.add(Calendar.DATE, 1);
        	String datePath = FmU.formatDate(now.getTime(), "yyyyMMdd");
            String filename = SysInfo.IssrId.concat("-").concat(datePath).concat(".key");

            String srcFile = SysInfo.SftpRootPath + Env.FILE + SysInfo.IssrId + Env.FILE + datePath + Env.FILE + "trans" + Env.FILE + filename;
            String dstFolder = SysInfo.SftpLocalBasePath + Env.FILE + datePath + Env.FILE + "trans";
            File folder = new File(dstFolder);
            if (!folder.exists()) {
            	folder.mkdirs();
            }
            String dstFile = dstFolder + Env.FILE + filename;
            try {
                File file = new File(dstFile);
                if (file.exists()) {
                	return;
                }
                chSftp.get(srcFile, dstFile.concat(SFTPConstants.SFTP_FILE_DOWNLOADING_SUFFIX));
                File downFile = new File(dstFile.concat(SFTPConstants.SFTP_FILE_DOWNLOADING_SUFFIX));
    			downFile.renameTo(file);
            } finally {
                chSftp.quit();
                channel.closeChannel();
            }
            
            _log.info("定时下载密钥文件成功！");
        } catch (Exception e) {
            _log.error("定时下载密钥文件失败", e);
        }
    }
}
