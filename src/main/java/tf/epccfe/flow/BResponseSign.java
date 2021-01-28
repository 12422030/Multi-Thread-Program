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

public class BResponseSign {

	public static void excSign(EpccfePkg epccfePkg) throws Exception {
        
        boolean checkSign = true;
        MsgHeader msgHeader = new Xml2Entity().xml2MsgHeader(epccfePkg.getPkgBody().getMsgHeaderXml());
        
        SysRtnInf sri = new SysRtnInf();
		sri.setSysRtnTm(SDU8601.sysDTime8601());
        // 检查签名域
        if (checkSign && StringUtils.isEmpty(epccfePkg.getPkgBody().getPkgS())) {
        	checkSign = false;
        	sri.setSysRtnCd(GetSysRtnCd.getSysRtnCd(msgHeader.getDrctn(), "026"));
        	sri.setSysRtnDesc("网联响应报文生成验签失败，签名域不完整！");
        }
        // 验签
        if (checkSign && SysInfo.HsmUseFlag) {
        	String rootXml = epccfePkg.getPkgBody().getPkgXml();
        	String sign = epccfePkg.getPkgBody().getsValue();
        	checkSign = SignService.epccEsscCheckSign(SysInfo.SignSN, rootXml, sign);
        	sri.setSysRtnCd(GetSysRtnCd.getSysRtnCd(msgHeader.getDrctn(), "025"));
        	sri.setSysRtnDesc("网联响应报文签名未通过验证！");
        }
        
        if (!checkSign) {
        	msgHeader.setSndDt(SDU8601.sysDTime8601());
        	msgHeader.setMsgTp("epcc.303.001.01");
        	MsgBody msgBody = new MsgBody();
        	msgBody.setSysRtnInf(sri);
        	ComResponse comResp = new ComResponse();
        	comResp.setMsgHeader(msgHeader);
        	comResp.setMsgBody(msgBody);
        	String xmlStr = new Entity2Xml().entity2XmlRoot(comResp);
            EpccfeBody pkgBody = epccfePkg.getPkgBody();
            pkgBody.setPkgXml(xmlStr); // 舍弃返回报文，返回通用报文提示验签错误
			
        	EpccfeHeader pkgHeader = epccfePkg.getPkgHeader();
        	pkgHeader.setMsgTp("epcc.303.001.01");
        }
	}
}
