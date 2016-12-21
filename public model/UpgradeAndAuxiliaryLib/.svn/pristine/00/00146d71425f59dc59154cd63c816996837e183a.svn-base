package com.voole.android.client.UpAndAu.model.parser;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.voole.android.client.UpAndAu.constants.DataConstants;
import com.voole.android.client.UpAndAu.model.ReportXmlHelpInfo;
import com.voole.android.client.UpAndAu.util.StringUtil;


/**
 * 消息xml生成器
 * @version V1.0
 * @author wusong
 * @time 2013-4-17上午11:00:28
 */
public class MsgXml extends CommonXml{
	
	/**
	 * 生成xml
	 * @param root
	 * @param rootAttrs
	 * @param msgList
	 * @param bool
	 * @return String
	 * 
	 * 
	 */
	public static String genXML(String root, List<NameValuePair> rootAttrs,List<ReportXmlHelpInfo> msgList, boolean bool) {

		try {
			if (!StringUtil.isNotNull(root)) {
				return null;
			}
			XmlSerializer xmlSerializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			String namespace = null;
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument("utf-8", false);

			// 开始根节点
			xmlSerializer.startTag(namespace, root);
			NameValuePair pair;
			if (rootAttrs != null && rootAttrs.size() > 0) {
				int attrCount = rootAttrs.size();
				// 遍历为根节点设置属性
				for (int i = 0; i < attrCount; i++) {
					pair = rootAttrs.get(i);
					xmlSerializer.attribute(namespace, pair.getName(),
							pair.getValue());
				}
			}
			// 生成message节点
			for(ReportXmlHelpInfo msg : msgList) {
				generateMsgXML(msg.getBodyContent(), msg.getNodeMsgAttrs(), msg.getNodeBodyAttrs(), xmlSerializer, namespace);
			}
			

			// 结束根节点
			xmlSerializer.endTag(namespace, root);
			xmlSerializer.endDocument();

			return writer.toString();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成消息xml
	 * @param bodyContent
	 * @param nodeMsgAttrs
	 * @param nodeBodyAttrs
	 * @param xmlSerializer
	 * @param namespace
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 * 
	 * 
	 */
	public static void generateMsgXML(String bodyContent,
			List<NameValuePair> nodeMsgAttrs,List<NameValuePair> nodeBodyAttrs, XmlSerializer xmlSerializer,
			String namespace) throws IllegalArgumentException,
			IllegalStateException, IOException {
		// 创建DataResult节点
		xmlSerializer.startTag(namespace, DataConstants.MSG_ROOT);
		generateAttrs(namespace, xmlSerializer, nodeMsgAttrs);
		generateBodyXML(bodyContent, nodeBodyAttrs, xmlSerializer, namespace);
		xmlSerializer.endTag(namespace, DataConstants.MSG_ROOT);
	}

	/**
	 *  生成bodyxml
	 * @param bodyContent
	 * @param nodeAttrs
	 * @param xmlSerializer
	 * @param namespace
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 * 
	 * 
	 */
	private static void generateBodyXML(String bodyContent,
			List<NameValuePair> nodeAttrs, XmlSerializer xmlSerializer,String namespace) throws IllegalArgumentException,
			IllegalStateException, IOException {
		// 创建DataResult节点
		xmlSerializer.startTag(namespace, DataConstants.MSG_BODY);

		generateAttrs(namespace, xmlSerializer, nodeAttrs);

		generateCDATANode(namespace, xmlSerializer, DataConstants.MSG_BODY, bodyContent);
		xmlSerializer.endTag(namespace, DataConstants.MSG_BODY);
	}
	
	
	/**
	 *  生成带CDATA的节点
	 * @param namespace
	 * @param xmlSerializer
	 * @param name
	 * @param value
	 * 
	 * 
	 */
	public static void generateCDATANode(String namespace,
			XmlSerializer xmlSerializer, String name, String value) {
		NameValuePair pair = new BasicNameValuePair(name, value);
		try {
			xmlSerializer.cdsect(pair.getValue());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	public static String genBodyContentXML(String root, List<NameValuePair> rootAttrs, String errorContent){
		
		try {
			if (!StringUtil.isNotNull(root)) {
				return null;
			}
			XmlSerializer xmlSerializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			String namespace = null;
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument("utf-8", false);

			// 开始根节点
			xmlSerializer.startTag(namespace, root);
			generateAttrs(namespace, xmlSerializer, rootAttrs);
			generateCDATANode(namespace, xmlSerializer, root, errorContent);

			// 结束根节点
			xmlSerializer.endTag(namespace, root);
			xmlSerializer.endDocument();

			return writer.toString();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
