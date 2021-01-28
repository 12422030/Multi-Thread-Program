package tf.epccfe.method;

import org.apache.commons.lang.StringUtils;

public class GetSysRtnCd {
	
	public static String SECCESS_CD = "00000000";

	public static String getSysRtnCd(String drctnCd, String seq) {
		
		String sysRtnCd = "PS000098";
		if (StringUtils.isNotEmpty(drctnCd) && drctnCd.length() == 2
				&& StringUtils.isNotEmpty(seq) && seq.length() == 3) {
			char c = drctnCd.charAt(1);
			switch (c) {
			case '2':
				sysRtnCd = "PS500".concat(seq);
				break;
			case '3':
				sysRtnCd = "RS600".concat(seq);
				break;

			default:
				break;
			}
		}
		
		return sysRtnCd;
		
	}
}
