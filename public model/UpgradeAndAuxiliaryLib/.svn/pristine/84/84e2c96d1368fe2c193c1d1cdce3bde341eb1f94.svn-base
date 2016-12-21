package com.voole.android.client.messagelibrary.model.parser;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlSerializer;

import com.voole.android.client.messagelibrary.utils.StringUtil;

import android.util.Xml;



/**
 * 消息xml生成器
 * @author wujian
 *
 */
public class MsgXml extends CommonXml{
	
	/**
	 * @param nodeHeaderAttrs
	 * @param xmlSerializer
	 * @param namespace
	 * @description 生成Header节点xml
	 * @version 1.0
	 * @author wujian
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 * @date 2013-8-6 上午11:10:44
	 * @update 2013-8-6 上午11:10:44
	 */
	public static void generateHeaderXML(List<NameValuePair> nodeHeaderAttrs,XmlSerializer xmlSerializer,
			String namespace) throws IllegalArgumentException, IllegalStateException, IOException{
		
		xmlSerializer.startTag(namespace, DataConstants.HEADER_ROOT);
		generateAttrs(namespace, xmlSerializer, nodeHeaderAttrs);
		xmlSerializer.endTag(namespace, DataConstants.HEADER_ROOT);
	}

	/**
	 * 生成消息xml
	 * @param bodyContent
	 * @param nodeMsgAttrs
	 * @param xmlSerializer
	 * @param namespace
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static void generateMsgXML(String bodyContent,
			List<NameValuePair> nodeMsgAttrs,XmlSerializer xmlSerializer,
			String namespace) throws IllegalArgumentException,
			IllegalStateException, IOException {
		xmlSerializer.startTag(namespace, DataConstants.MSG_ROOT);
		generateAttrs(namespace, xmlSerializer, nodeMsgAttrs);
		generateBodyXML(bodyContent, xmlSerializer, namespace);
		xmlSerializer.endTag(namespace, DataConstants.MSG_ROOT);
	}

	/**
	 * @param 
	 * @description 生成bodyxml
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-6 上午11:32:18
	 * @update 2013-8-6 上午11:32:18
	 */
	private static void generateBodyXML(String bodyContent,
			 XmlSerializer xmlSerializer,String namespace) throws IllegalArgumentException,
			IllegalStateException, IOException {
		// 创建Body节点
		xmlSerializer.startTag(namespace, DataConstants.MSG_BODY_ROOT);
		generateCDATANode(namespace, xmlSerializer, DataConstants.MSG_BODY_ROOT, bodyContent);
		xmlSerializer.endTag(namespace, DataConstants.MSG_BODY_ROOT);
	}
	
	
	/**
	 *  生成带CDATA的节点
	 * @param namespace
	 * @param xmlSerializer
	 * @param name
	 * @param value
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
	
	
	/**
	 * @param root
	 * @param rootAttrs
	 * @param errorContent
	 * @return String
	 * @description 
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-6 下午2:43:54
	 * @update 2013-8-6 下午2:43:54
	 */
	public static String genBodyContentXML(String root, List<NameValuePair> rootAttrs, String errorContent){
		
		try {
			if (!StringUtil.isNotNull(root)) {
				return null;
			}
			XmlSerializer xmlSerializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			String namespace = null;
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument(DataConstants.ENCODING, false);

			// 开始根节点
			xmlSerializer.startTag(namespace, root);
			generateAttrs(namespace, xmlSerializer, rootAttrs);
			if(errorContent != null && errorContent.trim().length() > 0){
				generateCDATANode(namespace, xmlSerializer, root, errorContent);
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
	
	
}
