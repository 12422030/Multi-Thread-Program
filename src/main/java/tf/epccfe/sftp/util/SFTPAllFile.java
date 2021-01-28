package tf.epccfe.sftp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import tf.epccfe.sys.SysInfo;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import common.Env;
import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class SFTPAllFile {
	
	private static TxThreadLogger logger = TxThreadLoggerFactory.getInstance(SFTPAllFile.class);
	
	public static void downloadAllNewFile(SFTPChannel channel, ChannelSftp chSftp, String parentPath, String dstFolder) throws Exception {
		downloadAllNewFile(channel, chSftp, parentPath, dstFolder, false);
	}
	
	@SuppressWarnings("unchecked")
	public static void downloadAllNewFile(SFTPChannel channel, ChannelSftp chSftp, String parentPath, String dstFolder, boolean limit) throws Exception {

		File folder = new File(dstFolder);
        if (!folder.exists()) {
        	folder.mkdirs();
        }
        
    	Vector<LsEntry> ls = null;
    	try {
    		ls = chSftp.ls(parentPath);
    	} catch (Exception e) {
    		logger.warn("服务器端不存在文件夹：" + parentPath);
            return;
    	}
    	for (LsEntry o : ls) {
    		if (o.getLongname().startsWith("d")) {
    			if (!".".equals(o.getFilename()) && !"..".equals(o.getFilename())) {
    				downloadAllNewFile(channel, chSftp, parentPath + Env.FILE + o.getFilename(), dstFolder + Env.FILE + o.getFilename(), limit);
    			}
    		} else {
                String dstFile = dstFolder + Env.FILE + o.getFilename();
                File file = new File(dstFile);
                if (file.exists()) {
                	continue;
                }
                String dstFileDownloading = dstFile + SFTPConstants.SFTP_FILE_DOWNLOADING_SUFFIX;
                File downFile = new File(dstFileDownloading);
                if (downFile.exists()) {
                	continue;
                }
                
                if (limit) {
	                OutputStream out = new FileOutputStream(dstFileDownloading);
	            	InputStream is = null;
	                try {
	                	int blockSize = (SysInfo.SftpDownloadLimit + 31) / 32;
	                    byte[] buff = new byte[1024 * blockSize];
	                    int read;
	                    is = chSftp.get(parentPath + Env.FILE + o.getFilename());
	                    if (is != null) {
	                        do {
	                        	Thread.sleep(1012 * blockSize / SysInfo.SftpDownloadLimit);
	                            read = is.read(buff, 0, buff.length);
	                            if (read > 0) {
	                                out.write(buff, 0, read);
	                            }
	                            out.flush();
	                        } while (read >= 0);
	                    }
	                } finally {
	                	out.close();
	                	if (is != null) {
	                		is.close();
	                	}
	                }
                } else {
        			chSftp.get(parentPath + Env.FILE + o.getFilename(), dstFileDownloading);
                }

    			downFile.renameTo(new File(dstFolder + Env.FILE + o.getFilename()));
    		}
    	}
		
	}

}
