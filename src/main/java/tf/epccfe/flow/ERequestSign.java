package tf.epccfe.flow;

import org.apache.commons.lang.StringUtils;

import tf.epccfe.entity.ComResponse;
import tf.epccfe.entity.Entity2Xml;
import tf.epccfe.entity.MsgBody;
import tf.epccfe.entity.MsgHeader;
import tf.epccfe.entity.SysRtnInf;
import tf.epccfe.entity.Xml2Entity;
import tf.epccfe.method.GetSysRtnCd;
import tf.epccfe.sys.SysInfo;
import tf.hsm.std.service.SignService;
import boot.pkg.EpccfeBody;
import boot.pkg.EpccfeHeader;
import boot.pkg.EpccfePkg;

import common.util.SDU8601;

public class ERequestSign {

	public static boolean excSign(EpccfePkg epccfePkg) throws Exception {

        boolean checkSign = true;
        MsgHeader msgHeader = new Xml2Entity().xml2MsgHeader(epccfePkg.getPkgBody().getMsgHeaderXml());
        
		SysRtnInf sri = new SysRtnInf();
		sri.setSysRtnTm(SDU8601.sysDTime8601());
        // 检查签名域
        if (checkSign && StringUtils.isEmpty(epccfePkg.getPkgBody().getPkgS())) {
        	checkSign = false;
        	sri.setSysRtnCd(GetSysRtnCd.getSysRtnCd(msgHeader.getDrctn(), "022"));
        	sri.setSysRtnDesc("请求报文格式有误，签名域不完整！");
        }
        if (checkSign && SysInfo.HsmUseFlag) {
        	String rootXml = epccfePkg.getPkgBody().getPkgXml();
        	String sign = epccfePkg.getPkgBody().getsValue();
        	checkSign = SignService.epccEsscCheckSign(SysInfo.SignSN, rootXml, sign);
        	sri.setSysRtnCd(GetSysRtnCd.getSysRtnCd(msgHeader.getDrctn(), "025"));
        	sri.setSysRtnDesc("请求报文签名未通过验证！");
        }
        
        // 验签失败时，直接返回通用报文提示错误
        if (!checkSign) {
        	msgHeader.setSndDt(SDU8601.sysDTime8601());
        	msgHeader.setMsgTp("epcc.303.001.01");
        	msgHeader.setIssrId(SysInfo.IssrId);
        	msgHeader.rtnDrctn();
        	msgHeader.setSignSN(SysInfo.SignSN);
        	MsgBody msgBody = new MsgBody();
        	msgBody.setSysRtnInf(sri);
        	ComResponse comResp = new ComResponse();
        	comResp.setMsgHeader(msgHeader);
        	comResp.setMsgBody(msgBody);
        	
        	String xmlStr = new Entity2Xml().entity2XmlRoot(comResp);
        	EpccfeHeader pkgHeader = epccfePkg.getPkgHeader();
        	pkgHeader.setMsgTp("epcc.303.001.01");
        	pkgHeader.setOriIssrId(SysInfo.IssrId);
            EpccfeBody pkgBody = new EpccfeBody();
            pkgBody.setPkgXml(xmlStr);
            pkgBody.setPkgS("{S:sf}");
			epccfePkg.setPkgBody(pkgBody);
        }
        
        return checkSign;
	}
}
