package boot.pkg;

import java.io.Serializable;

/**
 * @author RockChan
 * @date 2017.08.29
 * @version 1.0
 */
public class EpccfeBody implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1642733139947192878L;

	
	private String pkgXml;
	
	private String msgHeaderXml;
	
	private String msgBodyXml;
	
	private String pkgS;
	
	private String sValue;
	

	public String getPkgXml() {
		return pkgXml;
	}


	public void setPkgXml(String pkgXml) {
		this.pkgXml = pkgXml;
	}


	public String getPkgS() {
		return pkgS;
	}


	public void setPkgS(String pkgS) {
		this.pkgS = pkgS;
	}


	public String getsValue() {
		return sValue;
	}


	public void setsValue(String sValue) {
		this.sValue = sValue;
	}


	public String getMsgHeaderXml() {
		return msgHeaderXml;
	}


	public void setMsgHeaderXml(String msgHeaderXml) {
		this.msgHeaderXml = msgHeaderXml;
	}


	public String getMsgBodyXml() {
		return msgBodyXml;
	}


	public void setMsgBodyXml(String msgBodyXml) {
		this.msgBodyXml = msgBodyXml;
	}


	/*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		return "EpccfeBody [pkgXml=" + pkgXml + ", pkgS=" + pkgS + "]";
	}
}