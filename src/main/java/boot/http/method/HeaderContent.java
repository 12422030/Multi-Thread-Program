package boot.http.method;

import io.netty.handler.codec.http.HttpHeaders;
import boot.enumeration.PkgHeaderNames;
import boot.pkg.EpccfeHeader;
import boot.pkg.EpccfePkg;

public class HeaderContent {

	public static String headerContent(EpccfePkg epccfePkg) {

        EpccfeHeader pkgHeader = epccfePkg.getPkgHeader();
		StringBuffer sb = new StringBuffer("[");
		sb.append(PkgHeaderNames.MSG_TP.getName()).append(":")
				.append(pkgHeader.getMsgTp())
				.append(";");
		sb.append(PkgHeaderNames.ORI_ISSR_ID.getName()).append(":")
				.append(pkgHeader.getOriIssrId())
				.append(";");
		sb.append(PkgHeaderNames.PYER_ACCT_TP.getName()).append(":")
				.append(pkgHeader.getPyerAcctTp())
				.append(";");
		sb.append(PkgHeaderNames.PYEE_ACCT_TP.getName()).append(":")
				.append(pkgHeader.getPyeeAcctTp())
				.append(";");
		sb.append(PkgHeaderNames.RESERVED_FIELD.getName()).append(":")
				.append(pkgHeader.getReservedField())
				.append(";");

		return sb.append("]").toString();
    }
    
	public static String headerContent(HttpHeaders headers) {
		StringBuffer sb = new StringBuffer("[");
		sb.append(PkgHeaderNames.MSG_TP.getName()).append(":")
				.append(headers.get(PkgHeaderNames.MSG_TP.getName()))
				.append(";");
		sb.append(PkgHeaderNames.ORI_ISSR_ID.getName()).append(":")
				.append(headers.get(PkgHeaderNames.ORI_ISSR_ID.getName()))
				.append(";");
		sb.append(PkgHeaderNames.PYER_ACCT_TP.getName()).append(":")
				.append(headers.get(PkgHeaderNames.PYER_ACCT_TP.getName()))
				.append(";");
		sb.append(PkgHeaderNames.PYEE_ACCT_TP.getName()).append(":")
				.append(headers.get(PkgHeaderNames.PYEE_ACCT_TP.getName()))
				.append(";");
		sb.append(PkgHeaderNames.RESERVED_FIELD.getName()).append(":")
				.append(headers.get(PkgHeaderNames.RESERVED_FIELD.getName()))
				.append(";");

		return sb.append("]").toString();
	}
}
