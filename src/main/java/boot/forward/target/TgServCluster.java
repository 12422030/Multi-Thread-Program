package boot.forward.target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TgServCluster {

	/**
	 * 服务集群是否采用主备模式（否则为分发模式）
	 */
	private boolean isActiveStandbyMode = false;
	
	/**
	 * 主服务节点
	 * 非主备模式时为空
	 */
	private TgServInfo activeServ = null;
	
	/**
	 * 备服务节点集
	 * 非主备模式时为全部服务节点集
	 */
	private List<TgServInfo> standbyServList = new ArrayList<TgServInfo>();
	private HashMap<String, Integer> servNameMap = new HashMap<String, Integer>();
	
	private int sequence = -1;
	
	/**
	 * 指定服务节点名更新结点状态
	 * @param servName
	 * @param isActive
	 */
	public void setServActive(String servName, boolean isActive) {
		TgServInfo tgServ = getServByName(servName);
		tgServ.setServActive(isActive);
	}
	
	private TgServInfo getServByName(String servName) {
		Object o = servNameMap.get(servName);
		if (o == null) {
			return null;
		}
		int idx = (Integer) o;
		if (idx == -1) {
			return activeServ;
		} else {
			return standbyServList.get(idx);
		}
	}
	
	/**
	 * 按主备或分发模式获取可用服务器结点
	 * @return
	 */
	public synchronized TgServInfo getTgServ() {
		TgServInfo tgServ = null;
		if (isActiveStandbyMode) {
			if (activeServ != null && activeServ.isServActive()) {
				return activeServ; 
			}
		}
		
		for (int i = 0; i < standbyServList.size(); i++) {
			int idx = nextSeqValue() % standbyServList.size();
			if (standbyServList.get(idx).isServActive()) {
				tgServ = standbyServList.get(idx);
				break;
			}
		}
		
		return tgServ;
	}
	
	private int nextSeqValue() {
		sequence++;
		if (sequence == Integer.MAX_VALUE) {
			sequence = 0;
		}
		return sequence;
	}
	
	/**
	 * 添加备机服务器节点
	 * @param tgServ
	 */
	public void addStandbyServ(TgServInfo tgServ) {
		standbyServList.add(tgServ);
		servNameMap.put(tgServ.getServName(), standbyServList.size() - 1);
	}

	/**
	 * 设定主机服务器节点
	 * @param activeServ
	 */
	public void setActiveServ(TgServInfo activeServ) {
		this.activeServ = activeServ;
		servNameMap.put(activeServ.getServName(), -1);
	}
	
	/**
	 * 获取备机服务器节点个数
	 * @return
	 */
	public int getStandbyServSize() {
		return standbyServList.size();
	}

	public boolean isActiveStandbyMode() {
		return isActiveStandbyMode;
	}

	public void setActiveStandbyMode(boolean isActiveStandbyMode) {
		this.isActiveStandbyMode = isActiveStandbyMode;
	}

	public TgServInfo getActiveServ() {
		return activeServ;
	}
	
	public List<TgServInfo> getAllServ() {
		List<TgServInfo> allServList = new ArrayList<TgServInfo>();
		if (this.activeServ != null) {
			allServList.add(this.activeServ);
		}
		if (this.standbyServList != null && this.standbyServList.size() > 0) {
			for (TgServInfo serv : standbyServList) {
				allServList.add(serv);
			}
		}
		
		return allServList;
	}
}
