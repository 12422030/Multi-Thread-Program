package tf.epccfe.entity;

import java.io.Serializable;

/**
 * SysRtnInf information.
 */
public class SysRtnInf  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1531600232686681104L;

	private String SysRtnCd = "";
	
	private String SysRtnDesc = "";
	
	private String SysRtnTm = "";

	public String getSysRtnCd() {
		return SysRtnCd;
	}

	public void setSysRtnCd(String sysRtnCd) {
		SysRtnCd = sysRtnCd;
	}

	public String getSysRtnDesc() {
		return SysRtnDesc;
	}

	public void setSysRtnDesc(String sysRtnDesc) {
		SysRtnDesc = sysRtnDesc;
	}

	public String getSysRtnTm() {
		return SysRtnTm;
	}

	public void setSysRtnTm(String sysRtnTm) {
		SysRtnTm = sysRtnTm;
	}
	
}