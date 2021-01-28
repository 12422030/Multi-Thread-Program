package tf.epccfe.sftp.util;

import java.io.File;

import tf.epccfe.sys.SysInfo;

import com.jcraft.jsch.ChannelSftp;
import common.Env;
import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class SFTPDesignateFile implements Runnable {
	
	private static TxThreadLogger logger = TxThreadLoggerFactory.getInstance(SFTPDesignateFile.class);
    
	private DesignateFile designateFile;
	
	public SFTPDesignateFile(DesignateFile designateFile) {
		this.designateFile = designateFile;
	}

	@Override
	public void run() {
		logger.debug("异步获取指定的文件!");
		
    	SFTPChannel channel = null;
    	ChannelSftp chSftp = null;
    	try {
            channel = new SFTPChannel();
            chSftp = channel.getChannel(designateFile.getDesignateHost(), 
            		designateFile.getDesignatePort(), 60000);
            
            String srcFile = SysInfo.SftpRootPath + Env.FILE + designateFile.getScrFileFullPath() + Env.FILE + designateFile.getFileName();
            String dstFolder = SysInfo.SftpLocalBasePath + Env.FILE + designateFile.getDstFilePath();
            File folder = new File(dstFolder);
            if (!folder.exists()) {
            	folder.mkdirs();
            }
            String dstFile = dstFolder + Env.FILE + designateFile.getFileName();
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
            
            logger.debug("异步获取指定的文件成功！");
        } catch (Exception e) {
        	logger.error("异步获取指定的文件失败", e);
        }
	}

	public DesignateFile getDesignateFile() {
		return designateFile;
	}

	public void setDesignateFile(DesignateFile designateFile) {
		this.designateFile = designateFile;
	}

}
