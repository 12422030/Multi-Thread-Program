package boot.http.message;

import boot.enumeration.PkgHeaderNames;
import boot.pkg.EpccfeHeader;
import boot.pkg.EpccfePkg;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;

/**
 * @author RockChan
 * @date 2017-08-23
 * @version 1.0
 */
public class HttpXmlResponseMessage {
    private FullHttpResponse httpResponse;
	private EpccfePkg pkg;

    public HttpXmlResponseMessage(FullHttpResponse httpResponse, Object result) {
        this.httpResponse = httpResponse;
        this.pkg = (EpccfePkg)result;
    	if (this.httpResponse != null) {
    		HttpHeaders headers = this.httpResponse.headers();
    		EpccfeHeader pkgHeader = new EpccfeHeader();
    		pkgHeader.setPkgLength(headers.get(HttpHeaderNames.CONTENT_LENGTH));
    		pkgHeader.setMsgTp(headers.get(PkgHeaderNames.MSG_TP.getName()));
    		pkgHeader.setOriIssrId(headers.get(PkgHeaderNames.ORI_ISSR_ID.getName()));
    		pkgHeader.setPyerAcctTp(headers.get(PkgHeaderNames.PYER_ACCT_TP.getName()));
    		pkgHeader.setPyeeAcctTp(headers.get(PkgHeaderNames.PYEE_ACCT_TP.getName()));
    		pkgHeader.setReservedField(headers.get(PkgHeaderNames.RESERVED_FIELD.getName()));
    		pkg.setPkgHeader(pkgHeader);
    	}
    }

    /**
     * @return the httpResponse
     */
    public final FullHttpResponse getHttpResponse() {
        return httpResponse;
    }

    /**
     * @param httpResponse the httpResponse to set
     */
    public final void setHttpResponse(FullHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    /**
     * @return the pkg
     */
    public final EpccfePkg getResult() {
        return pkg;
    }

    /**
     * @param pkg the pkg to set
     */
    public final void setResult(EpccfePkg pkg) {
        this.pkg = pkg;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "HttpXmlResponse [httpResponse=" + httpResponse + ", pkg="
                + pkg + "]";
    }

}
