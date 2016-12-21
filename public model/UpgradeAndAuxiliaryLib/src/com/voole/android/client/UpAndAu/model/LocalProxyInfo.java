package com.voole.android.client.UpAndAu.model;

import java.io.Serializable;

/**
 * 
 * @description 获取本地代理信息
 * @version 1.0
 * @author LEE
 * @date 2013-12-13 上午10:14:25 
 * @update 2013-12-13 上午10:14:25
 * 
 * <info>

 <version>00000002</version>

 <build_time>Apr 27 2013 09:51:22</build_time>

<vlive_version>00003</vlive_version>

 <bufratio>0%</bufratio>

<downspeed>162B/s</downspeed>

</info>
 */
public class LocalProxyInfo implements Serializable{
	private static final long serialVersionUID = 8306448338515789412L;
	
	private String version;
	private String buildVersion;
	private String vliveVersion;
	private String bufratio;
	//下载速度
	private String downspeed;
	//------------
	private String vooletvVersion;
	private String agentVersion;
	private String realtimeSpeed;
	private String averageSpeed;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getBuildVersion() {
		return buildVersion;
	}
	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}
	public String getBufratio() {
		return bufratio;
	}
	public void setBufratio(String bufratio) {
		this.bufratio = bufratio;
	}
	public String getDownspeed() {
		return downspeed;
	}
	public void setDownspeed(String downspeed) {
		this.downspeed = downspeed;
	}
	public String getVliveVersion() {
		return vliveVersion;
	}
	public void setVliveVersion(String vliveVersion) {
		this.vliveVersion = vliveVersion;
	}
	public String getVooletvVersion() {
		return vooletvVersion;
	}
	public void setVooletvVersion(String vooletvVersion) {
		this.vooletvVersion = vooletvVersion;
	}
	public String getAgentVersion() {
		return agentVersion;
	}
	public void setAgentVersion(String agentVersion) {
		this.agentVersion = agentVersion;
	}
	public String getRealtimeSpeed() {
		return realtimeSpeed;
	}
	public void setRealtimeSpeed(String realtimeSpeed) {
		this.realtimeSpeed = realtimeSpeed;
	}
	public String getAverageSpeed() {
		return averageSpeed;
	}
	public void setAverageSpeed(String averageSpeed) {
		this.averageSpeed = averageSpeed;
	}
	
	

}
