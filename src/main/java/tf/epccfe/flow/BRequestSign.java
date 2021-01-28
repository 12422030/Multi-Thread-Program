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

public class BRequestSign {

	public static boolean excSign(EpccfePkg epccfePkg) throws Exception {

        boolean checkSign = true;
        MsgHeader msgHeader = new Xml2Entity().xml2MsgHeader(epccfePkg.getPkgBody().getMsgHeaderXml());
        
		SysRtnInf sri = new SysRtnInf();
		sri.setSysRtnTm(SDU8601.sysDTime8601());
		
        if (SysInfo.HsmUseFlag) {
        	String rootXml = epccfePkg.getPkgBody().getPkgXml();
        	String sign = SignService.epccEsscSignData(SysInfo.SignSN, rootXml);
        	epccfePkg.getPkgBody().setPkgS("{S:" + sign + "}");
        	epccfePkg.getPkgBody().setsValue(sign);
        } else {
            // 检查签名域
            if (checkSign && StringUtils.isEmpty(epccfePkg.getPkgBody().getPkgS())) {
            	checkSign = false;
            	sri.setSysRtnCd(GetSysRtnCd.getSysRtnCd(msgHeader.getDrctn(), "022"));
            	sri.setSysRtnDesc("请求报文格式有误，签名域不完整！");
            }
            
            // 验签失败时，直接返回通用报文提示错误
            if (!checkSign) {
            	msgHeader.setSndDt(SDU8601.sysDTime8601());
            	msgHeader.setMsgTp("epcc.303.001.01");
            	MsgBody msgBody = new MsgBody();
            	msgBody.setSysRtnInf(sri);
            	ComResponse comResp = new ComResponse();
            	comResp.setMsgHeader(msgHeader);
            	comResp.setMsgBody(msgBody);
            	
            	String xmlStr = new Entity2Xml().entity2XmlRoot(comResp);
            	EpccfeHeader pkgHeader = epccfePkg.getPkgHeader();
            	pkgHeader.setMsgTp("epcc.303.001.01");
                EpccfeBody pkgBody = epccfePkg.getPkgBody();
                pkgBody.setPkgXml(xmlStr);
    			epccfePkg.setPkgBody(pkgBody);
            }
        }
        
        return checkSign;
	}
}
