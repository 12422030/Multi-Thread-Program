package boot.enumeration;

/**
 * 密钥类型表
 * @author 		RockChan
 * @since 		2017.08.28
 */
public enum PkgHeaderNames {
	MSG_TP("MsgTp"),
	ORI_ISSR_ID("OriIssrId"),
	PYER_ACCT_TP("PyerAcctTp"),
	PYEE_ACCT_TP("PyeeAcctTp"),
	RESERVED_FIELD("ReservedField"),
	;

	private final String name;

	private PkgHeaderNames(String name) {
		this.name = name;
	}

	public final String getName() {
		return name;
	}
}
