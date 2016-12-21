package com.voole.android.client.messagelibrary.model.parser;

import java.io.Serializable;
import java.util.List;

import org.apache.http.NameValuePair;

/**
 * 向服务器汇报消息
 * @author LEE
 * 
 *
 */
public class MsgXmlHelpInfo implements Serializable{
	private static final long serialVersionUID = -2089047021228661131L;
	
	private String bodyContent;
	private List<NameValuePair> nodeMsgAttrs;
	private List<NameValuePair> nodeBodyAttrs;
	private List<NameValuePair> nodeHeaderAttrs;
	
	public String getBodyContent() {
		return bodyContent;
	}
	public List<NameValuePair> getNodeHeaderAttrs() {
		return nodeHeaderAttrs;
	}
	public void setNodeHeaderAttrs(List<NameValuePair> nodeHeaderAttrs) {
		this.nodeHeaderAttrs = nodeHeaderAttrs;
	}
	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}
	public List<NameValuePair> getNodeMsgAttrs() {
		return nodeMsgAttrs;
	}
	public void setNodeMsgAttrs(List<NameValuePair> nodeMsgAttrs) {
		this.nodeMsgAttrs = nodeMsgAttrs;
	}
	public List<NameValuePair> getNodeBodyAttrs() {
		return nodeBodyAttrs;
	}
	public void setNodeBodyAttrs(List<NameValuePair> nodeBodyAttrs) {
		this.nodeBodyAttrs = nodeBodyAttrs;
	}
	
}
