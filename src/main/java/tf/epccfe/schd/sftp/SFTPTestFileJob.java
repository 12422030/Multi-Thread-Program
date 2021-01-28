package tf.epccfe.schd.sftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import tf.epccfe.sftp.util.SFTPChannel;
import tf.epccfe.sys.SysInfo;

import com.jcraft.jsch.ChannelSftp;
import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class SFTPTestFileJob implements Job {
	
    private static TxThreadLogger _log = TxThreadLoggerFactory.getInstance(SFTPTestFileJob.class);
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
    	_log.info("定时下载文件开始！");
        
    	try {
            
            SFTPChannel channel = new SFTPChannel();
            ChannelSftp chSftp = channel.getChannel(60000);
            
            String rootPath = "/home/";
            String filePath = "/yqets/";
            String filename = "eTellerServer.tar";
            

            String dstFolder = SysInfo.SftpLocalBasePath + filePath;
            File folder = new File(dstFolder);
            if (!folder.exists()) {
            	folder.mkdirs();
            }
            String dstFile = dstFolder + filename;
            OutputStream out = new FileOutputStream(dstFile);
        	InputStream is = null;
            try {
            	int blockSize = (SysInfo.SftpDownloadLimit + 31) / 32;
                byte[] buff = new byte[1024 * blockSize];
                int read;
                is = chSftp.get(rootPath+filePath+filename);
                if (is != null) {
                	_log.info("Start to read input stream");
                    do {
                    	Thread.sleep(1012 * blockSize / SysInfo.SftpDownloadLimit);
                        read = is.read(buff, 0, buff.length);
                        if (read > 0) {
                            out.write(buff, 0, read);
                        }
                        out.flush();
                    } while (read >= 0);
                    _log.info("input stream read done.");
                }
            } finally {
            	out.close();
            	if (is != null) {
            		is.close();
            	}
                chSftp.quit();
                channel.closeChannel();
            }
            
            _log.info("定时下载文件成功！");
        } catch (Exception e) {
            _log.error("定时下载文件失败", e);
        }
    }
}
