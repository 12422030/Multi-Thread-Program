package boot.forward.target;


public class TgServInfo {
	
	/**
	 * 服务器名称
	 */
    private String servName;
    
    /**
     * 服务器描述
     */
    private String servDesc;
    
    /**
     * 服务器IP
     */
    private String servIP;
    
    /**
     * 服务器服务端口
     */
    private String servPort;
    
    /**
     * 服务器URL
     */
    private String servURL;
    
    /**
     * 服务器连接超时时间
     */
    private String timeout;
    
    /**
     * 服务器是否可用
     */
    private boolean isServActive = true;
    
    public TgServInfo() {
        
    }
    
    public TgServInfo(String servName, String servIP, String servURL, String servPort, String timeout) {
        this.servName = servName;
        this.servIP = servIP;
        this.servURL = servURL;
        this.servPort = servPort;
        this.timeout = timeout;
    }

	public String getServName() {
		return servName;
	}

	public void setServName(String servName) {
		this.servName = servName;
	}

	public String getServDesc() {
		return servDesc;
	}

	public void setServDesc(String servDesc) {
		this.servDesc = servDesc;
	}

	public String getServIP() {
		return servIP;
	}

	public void setServIP(String servIP) {
		this.servIP = servIP;
	}

	public String getServPort() {
		return servPort;
	}

	public void setServPort(String servPort) {
		this.servPort = servPort;
	}

	public String getServURL() {
		return servURL;
	}

	public void setServURL(String servURL) {
		this.servURL = servURL;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public boolean isServActive() {
		return isServActive;
	}

	public void setServActive(boolean isServActive) {
		this.isServActive = isServActive;
	}
}
