package tf.epccfe.entity;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * MsgHeader information.
 */
public class MsgHeader  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5954958276869963964L;
	
	private String SndDt = "";
	
	private String MsgTp = "";
	
	private String IssrId = "";
	
	private String Drctn = "";
	
	private String SignSN = "";
	
	private String NcrptnSN = "";
	
	private String DgtlEnvlp = "";
	
	public void rtnDrctn() {
		if (StringUtils.isNotEmpty(Drctn)) {
			char ch0 = Drctn.charAt(0);
			char nch0 = '0';
			char ch1 = Drctn.charAt(1);
			switch (ch0) {
			case '1':
				nch0 = '2';
				break;
			case '2':
				nch0 = '1';
				break;
			default:
				break;
			}
			Drctn = new String(new char[] {nch0, ch1});
		}
	}

	public String getSndDt() {
		return SndDt;
	}

	public void setSndDt(String sndDt) {
		SndDt = sndDt;
	}

	public String getMsgTp() {
		return MsgTp;
	}

	public void setMsgTp(String msgTp) {
		MsgTp = msgTp;
	}

	public String getIssrId() {
		return IssrId;
	}

	public void setIssrId(String issrId) {
		IssrId = issrId;
	}

	public String getDrctn() {
		return Drctn;
	}

	public void setDrctn(String drctn) {
		Drctn = drctn;
	}

	public String getSignSN() {
		return SignSN;
	}

	public void setSignSN(String signSN) {
		SignSN = signSN;
	}

	public String getNcrptnSN() {
		return NcrptnSN;
	}

	public void setNcrptnSN(String ncrptnSN) {
		NcrptnSN = ncrptnSN;
	}

	public String getDgtlEnvlp() {
		return DgtlEnvlp;
	}

	public void setDgtlEnvlp(String dgtlEnvlp) {
		DgtlEnvlp = dgtlEnvlp;
	}
}