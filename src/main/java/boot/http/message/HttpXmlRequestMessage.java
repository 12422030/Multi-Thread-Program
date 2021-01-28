package boot.http.message;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import boot.enumeration.PkgHeaderNames;
import boot.pkg.EpccfeHeader;
import boot.pkg.EpccfePkg;

/**
 * @author RockChan
 * @date 2017-08-23
 * @version 1.0
 */
public class HttpXmlRequestMessage {

	private FullHttpRequest request;
	private EpccfePkg pkg;
	private String reqUrl;

	public HttpXmlRequestMessage(FullHttpRequest request, Object msg) {
		this(request, msg, "/do");
	}

	public HttpXmlRequestMessage(FullHttpRequest request, Object msg, String reqUrl) {
		this.request = request;
		this.pkg = (EpccfePkg)msg;
		if (this.request != null) {
			HttpHeaders headers = this.request.headers();
			EpccfeHeader pkgHeader = new EpccfeHeader();
			pkgHeader.setPkgLength(headers.get(HttpHeaderNames.CONTENT_LENGTH));
			pkgHeader.setMsgTp(headers.get(PkgHeaderNames.MSG_TP.getName()));
			pkgHeader.setOriIssrId(headers.get(PkgHeaderNames.ORI_ISSR_ID.getName()));
			pkgHeader.setPyerAcctTp(headers.get(PkgHeaderNames.PYER_ACCT_TP.getName()));
			pkgHeader.setPyeeAcctTp(headers.get(PkgHeaderNames.PYEE_ACCT_TP.getName()));
			pkgHeader.setReservedField(headers.get(PkgHeaderNames.RESERVED_FIELD.getName()));
			pkg.setPkgHeader(pkgHeader);
		} else {
			this.reqUrl = reqUrl;
		}
	}

	/**
	 * @return the request
	 */
	public final FullHttpRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public final void setRequest(FullHttpRequest request) {
		this.request = request;
	}

	/**
	 * @return the pkg
	 */
	public final EpccfePkg getPkg() {
		return pkg;
	}

	/**
	 * @param pkg the pkg to set
	 */
	public final void setPkg(EpccfePkg pkg) {
		this.pkg = pkg;
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HttpXmlRequest [request=" + request + ", pkg =" + pkg + "]";
	}
}
