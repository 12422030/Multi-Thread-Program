package tf.epccfe.sftp.util;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import tf.epccfe.sys.SysInfo;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Proxy;
import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.ProxySOCKS4;
import com.jcraft.jsch.ProxySOCKS5;
import com.jcraft.jsch.Session;
import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;

public class SFTPChannel {
    Session session = null;
    Channel channel = null;

    private static TxThreadLogger logger = TxThreadLoggerFactory.getInstance(SFTPChannel.class);

    public ChannelSftp getChannel(int timeout) throws JSchException {
    	return this.getChannel(null, null, timeout);
    }

	public ChannelSftp getChannel(String designateHost, String designatePort,
			int timeout) throws JSchException {

		String ftpHost = SysInfo.SftpReqHost;
        int ftpPort = SFTPConstants.SFTP_DEFAULT_PORT;
        if (StringUtils.isNotEmpty(SysInfo.SftpReqPort)) {
            ftpPort = Integer.valueOf(SysInfo.SftpReqPort);
        }
        
        if (StringUtils.isNotEmpty(designateHost)) {
        	ftpHost = designateHost;
        }
        if (StringUtils.isNotEmpty(designatePort)) {
        	ftpPort = Integer.valueOf(designatePort);
        }

        JSch jsch = new JSch(); // 创建JSch对象
        session = jsch.getSession(SysInfo.SftpReqUsername, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
        logger.debug("SFTP Session created.");
        if (SysInfo.SftpReqPassword != null) {
            session.setPassword(SysInfo.SftpReqPassword); // 设置密码
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        
        // 增加代理设置
        if (SysInfo.SftpProxyFlag) {
        	Proxy proxy = null;
        	if (SFTPConstants.SFTP_PROXY_MODE_HTTP.equals(SysInfo.SftpProxyMode)) {
        		proxy = new ProxyHTTP(SysInfo.SftpProxyHost, Integer.valueOf(SysInfo.SftpProxyPort));
        	} else if (SFTPConstants.SFTP_PROXY_MODE_SOCKS4.equals(SysInfo.SftpProxyMode)) {
        		proxy = new ProxySOCKS4(SysInfo.SftpProxyHost, Integer.valueOf(SysInfo.SftpProxyPort));
        	} else if (SFTPConstants.SFTP_PROXY_MODE_SOCKS5.equals(SysInfo.SftpProxyMode)) {
        		proxy = new ProxySOCKS5(SysInfo.SftpProxyHost, Integer.valueOf(SysInfo.SftpProxyPort));
        	}
        	session.setProxy(proxy);
        }
        
        session.connect(); // 通过Session建立链接
        logger.debug("SFTP Session connected.");

        logger.debug("SFTP Opening Channel.");
        channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        logger.debug("Connected successfully to ftpHost = " + SysInfo.SftpReqHost + ",as ftpUserName = " + SysInfo.SftpReqUsername
                + ", returning: " + channel);
        return (ChannelSftp) channel;
	}

    public void closeChannel() throws Exception {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }
}
