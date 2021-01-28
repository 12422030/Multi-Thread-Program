package tf.epccfe.flow;

import java.io.File;
import java.util.List;

import tf.epccfe.entity.ComResponse;
import tf.epccfe.entity.Entity2Xml;
import tf.epccfe.entity.Epcc999Body;
import tf.epccfe.entity.MsgBody;
import tf.epccfe.entity.MsgHeader;
import tf.epccfe.entity.SysRtnInf;
import tf.epccfe.entity.Xml2Entity;
import tf.epccfe.method.GetSysRtnCd;
import tf.epccfe.sftp.util.DesignateFile;
import tf.epccfe.sftp.util.SFTPDesignateFile;
import tf.epccfe.sys.SysInfo;
import boot.pkg.EpccfeBody;
import boot.pkg.EpccfeHeader;
import boot.pkg.EpccfePkg;

import common.Env;
import common.sys.TxThreadLogger;
import common.sys.TxThreadLoggerFactory;
import common.util.SDU8601;

public class Epcc999FLow {
	private TxThreadLogger logger = TxThreadLoggerFactory.getInstance(Epcc999FLow.class);

	private EpccfePkg epccfePkg;
	
	@SuppressWarnings("unused")
	private Epcc999FLow() {
		
	}

	public Epcc999FLow(EpccfePkg epccfePkg) {
		this.epccfePkg = epccfePkg;
	}
	
	public EpccfePkg execute() throws Exception {
		
		logger.debug("检查指定的对账明细文件是否已存在。");
		
		MsgHeader msgHeader = new Xml2Entity().xml2MsgHeader(epccfePkg.getPkgBody().getMsgHeaderXml());
		SysRtnInf sri = new SysRtnInf();
		
		Epcc999Body epcc999Body = new Xml2Entity().xml2Epcc999Body(epccfePkg.getPkgBody().getMsgBodyXml());
		String filePath = epcc999Body.getFilePath();
		logger.debug("明细文件路径：" + filePath);
		
		DesignateFile designateFile = new DesignateFile();
		String host = filePath.substring(0, filePath.indexOf(":"));
		String port = filePath.substring(filePath.indexOf(":") + 1, filePath.indexOf("/"));
		String fullPath = filePath.substring(filePath.indexOf("/") + 1);
		String dstFilePath = fullPath.substring(fullPath.indexOf("/") + 1);
		designateFile.setDesignateHost(host);
		designateFile.setDesignatePort(port);
		designateFile.setScrFileFullPath(fullPath);
		designateFile.setDstFilePath(dstFilePath);
		
		String localFilePath = SysInfo.SftpLocalBasePath + Env.FILE + dstFilePath + Env.FILE;
		List<String> fileNameList = epcc999Body.getFileNameList().getFileNames();
		boolean allFileExists = true;
		String notExistsFiles = "";
		if (fileNameList != null && fileNameList.size() > 0) {
			for (String fileName : fileNameList) {
				logger.debug("明细文件名：" + fileName);
				File file = new File(localFilePath + fileName);
				if (!file.exists() || !file.isFile()) {
					allFileExists = false;
					notExistsFiles += "," + fileName;
					designateFile.setFileName(fileName);
					logger.debug(designateFile.toString());
					Thread thread = new Thread(new SFTPDesignateFile(designateFile));
					thread.start();
				}
			}
		}
		if (allFileExists) {
	    	sri.setSysRtnCd(GetSysRtnCd.SECCESS_CD);
	    	sri.setSysRtnDesc("指定的明细文件已存在，可以获取！");
			logger.debug("指定的明细文件已存在，可以获取！");
		} else {
	    	sri.setSysRtnCd(GetSysRtnCd.getSysRtnCd(msgHeader.getDrctn(), "901"));
	    	sri.setSysRtnDesc("指定的明细文件不存在！");
			logger.debug("指定的明细文件不存在！" + notExistsFiles.substring(1));
		}

		sri.setSysRtnTm(SDU8601.sysDTime8601());
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
		return epccfePkg;
	}
}
