package tf.epccfe.sftp.util;

public class DesignateFile {

	/**
	 * 指定SFTP文件服务器HOST
	 */
	private String designateHost;
	
	/**
	 * 指定SFTP文件服务器端口号
	 */
	private String designatePort;
	
	/**
	 * SFTP文件服务器文件完整路径（相对路径）
	 */
	private String scrFileFullPath;
	
	/**
	 * 本地文件存放路径（相对路径）
	 */
	private String dstFilePath;
	
	/**
	 * 文件名
	 */
	private String fileName;

	public String getDesignateHost() {
		return designateHost;
	}

	public void setDesignateHost(String designateHost) {
		this.designateHost = designateHost;
	}

	public String getDesignatePort() {
		return designatePort;
	}

	public void setDesignatePort(String designatePort) {
		this.designatePort = designatePort;
	}

	public String getScrFileFullPath() {
		return scrFileFullPath;
	}

	public void setScrFileFullPath(String scrFileFullPath) {
		this.scrFileFullPath = scrFileFullPath;
	}

	public String getDstFilePath() {
		return dstFilePath;
	}

	public void setDstFilePath(String dstFilePath) {
		this.dstFilePath = dstFilePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public String toString() {
		return "DesignateFile [designateHost=" + designateHost 
				+ ", designatePort =" + designatePort
				+ ", scrFileFullPath =" + scrFileFullPath
				+ ", dstFilePath =" + dstFilePath
				+ ", fileName =" + fileName + "]";
		
	}
}
