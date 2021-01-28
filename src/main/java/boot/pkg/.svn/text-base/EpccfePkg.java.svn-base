package boot.pkg;

import java.io.Serializable;

import common.Env;

/**
 * @author RockChan
 * @date 2017.08.29
 * @version 1.0
 */
public class EpccfePkg implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1578014953071253003L;
	
	public final static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";

	private EpccfeHeader pkgHeader;
	
	private EpccfeBody pkgBody;
	
	private String txThreadSerial;
	
	public EpccfeHeader getPkgHeader() {
		return pkgHeader;
	}

	public void setPkgHeader(EpccfeHeader pkgHeader) {
		this.pkgHeader = pkgHeader;
	}

	public EpccfeBody getPkgBody() {
		return pkgBody;
	}

	public void setPkgBody(EpccfeBody pkgBody) {
		this.pkgBody = pkgBody;
	}

	public String getTxThreadSerial() {
		return txThreadSerial;
	}

	public void setTxThreadSerial(String txThreadSerial) {
		this.txThreadSerial = txThreadSerial;
	}

	/*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		return pkgHeader.toString() + Env.LINE + pkgBody.toString();
	}
}