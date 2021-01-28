package tf.epccfe.flow;

import tf.epccfe.sys.SysInfo;
import tf.hsm.std.service.SignService;
import boot.pkg.EpccfePkg;

public class EResponseSign {

	public static void excSign(EpccfePkg epccfePkg) throws Exception {
		
        if (SysInfo.HsmUseFlag) {
        	String rootXml = epccfePkg.getPkgBody().getPkgXml();
        	String sign = SignService.epccEsscSignData(SysInfo.SignSN, rootXml);
        	epccfePkg.getPkgBody().setPkgS("{S:" + sign + "}");
        	epccfePkg.getPkgBody().setsValue(sign);
        // 缺少签名域时，不做处理，直接返回网联，由网联自行处理结果
//        } else {
//
//    		boolean checkSign = true;
//    		sri = new SysRtnInf();
//    		sri.setSysRtnTm(SDU8601.sysDTime8601());
//        	
//            // 检查签名域
//            if (checkSign && StringUtils.isEmpty(epccfePkg.getPkgBody().getPkgS())) {
//            	checkSign = false;
//            	sri.setSysRtnCd("00009999");
//            	sri.setSysRtnDesc("签名域不完整！");
//            }
//            
//            // 验签失败时，直接返回通用报文提示错误
//            if (!checkSign) {
//            	MsgHeader msgHeader = new Xml2Entity().xml2MsgHeader(epccfePkg.getPkgBody().getMsgHeaderXml());
//              msgHeader.setSndDt(SDU8601.sysDTime8601());
//            	msgHeader.setMsgTp("epcc.303.001.01");
//            	MsgBody msgBody = new MsgBody();
//            	msgBody.setSysRtnInf(sri);
//            	ComResponse comResp = new ComResponse();
//            	comResp.setMsgHeader(msgHeader);
//            	comResp.setMsgBody(msgBody);
//            	
//            	String xmlStr = new Entity2Xml().entity2xml(comResp);
//            	EpccfeHeader pkgHeader = epccfePkg.getPkgHeader();
//            	pkgHeader.setMsgTp("epcc.303.001.01");
//                EpccfeBody pkgBody = new EpccfeBody();
//                pkgBody.setPkgXml(xmlStr);
//                pkgBody.setPkgS("{S:sf}");
//    			epccfePkg.setPkgBody(pkgBody);
//            }
        }
	}
}
