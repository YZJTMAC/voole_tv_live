package com.voole.android.client.UpAndAu.model;

import java.io.Serializable;

/**
 * 
 *  反馈意见
 * @version 1.0
 * @author LEE
 * @date 2013-4-3 下午2:00:38 
 * @update 2013-4-3 下午2:00:38
 */
public class AdviceFeedBackInfo implements Serializable{
	private static final long serialVersionUID = 6105415273512458856L;
	
	
	/**
	 * 馈意见类型,通过获取反馈选项接口获得
	 * 必填参数
		例如：
		1-	问题反馈
		2 - 改善建议
		3 - 内容需求
		4 - 其它
	 */
	private String optionCode;
	private String version;
	// 用户输入的反馈文本内容   必须赋值
	private String content;
	// 用户输入的电话号码   可选
	private String phone;
	// 用户输入的电邮      可选
	private String email;
	
	public AdviceFeedBackInfo() {
		
	}
	
	public AdviceFeedBackInfo(String optionCode, String version,
			String content, String phone, String email) {
		super();
		this.optionCode = optionCode;
		this.version = version;
		this.content = content;
		this.phone = phone;
		this.email = email;
	}

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getOptionCode() {
		return optionCode;
	}
	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	

}
