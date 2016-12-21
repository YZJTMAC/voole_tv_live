package com.voole.statistics.bean;

/**
 * 返回结果BEAN
 * @author Jacky
 *
 */
public class DataResultBean {

	/**
	 * 标示获取结果状态
	 *   0	获取升级信息成功
		-1	获取失败，原因：无法连接数据库
		-2	获取失败，原因：查询超时
		-3	获取失败，原因：缺少必要参数
	 */
	public String resultCode;
	/**
	 * 结果文本描述
	 */
	public String resultText;
	
	
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultText() {
		return resultText;
	}
	public void setResultText(String resultText) {
		this.resultText = resultText;
	}
	
	

}
