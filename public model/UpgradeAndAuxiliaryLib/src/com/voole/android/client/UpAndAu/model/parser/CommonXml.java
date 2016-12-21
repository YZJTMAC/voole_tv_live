package com.voole.android.client.UpAndAu.model.parser;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlSerializer;

import com.voole.android.client.UpAndAu.util.StringUtil;

import android.util.Xml;


/**
 * 基础的xml处理器
 * @version V1.0
 * @author wusong
 * @time 2013-4-17上午10:43:36
 */
public class CommonXml {

	private static final String DATA_RESULT = "DataResult";
	private static final String RESULT_CODE = "resultCode";
	private static final String RESULT_TEXT = "ResultText";

	/**
	 * 构建xml
	 * @param root
	 * @param rootAttrs
	 * @param nodes
	 * @param bool
	 * @return String
	 * 
	 * 
	 */
	public static String common(String root, List<NameValuePair> rootAttrs,
			List<NameValuePair> nodes, boolean bool) {

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
			if (nodes != null && nodes.size() > 0) {
				int nodeCount = nodes.size();
				// 遍历设置子节点
				for (int i = 0; i < nodeCount; i++) {
					pair = nodes.get(i);
					xmlSerializer.startTag(namespace, pair.getName());
					xmlSerializer.cdsect(pair.getValue());
					xmlSerializer.endTag(namespace, pair.getName());

				}
			} else if (bool) {
				xmlSerializer.text("");
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
	 *  生成DataResult节点
	 * @param resultCode
	 * @param resultText
	 * @param xmlSerializer
	 * @param namespace
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static void generateDataResultNode(String resultCode,
			String resultText, XmlSerializer xmlSerializer, String namespace)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// 创建DataResult节点
		xmlSerializer.startTag(namespace, DATA_RESULT);

		List<NameValuePair> DataResultAttrs = new ArrayList<NameValuePair>();
		DataResultAttrs.add(new BasicNameValuePair(RESULT_CODE, resultCode));

		generateAttrs(namespace, xmlSerializer, DataResultAttrs);

		generateCDATANode(namespace, xmlSerializer, RESULT_TEXT, resultText);

		xmlSerializer.endTag(namespace, DATA_RESULT);
	}

	/**
	 * 生成DataResult节点
	 * @param sid
	 * @param filmmid
	 * @param resultText
	 * @param xmlSerializer
	 * @param namespace
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static void generateContentNode(String sid, String filmmid,
			String resultText, XmlSerializer xmlSerializer, String namespace)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// 创建DataResult节点
		xmlSerializer.startTag(namespace, "Content");

		List<NameValuePair> DataResultAttrs = new ArrayList<NameValuePair>();
		DataResultAttrs.add(new BasicNameValuePair("sid", sid));
		DataResultAttrs.add(new BasicNameValuePair("filmmid", filmmid));

		generateAttrs(namespace, xmlSerializer, DataResultAttrs);

		xmlSerializer.endTag(namespace, "Content");
	}

	/**
	 *  生成某个节点的属性
	 * @param namespace
	 * @param xmlSerializer
	 * @param attrList
	 */
	public static void generateAttrs(String namespace,
			XmlSerializer xmlSerializer, List<NameValuePair> attrList) {
		NameValuePair pair;
		if (attrList != null && attrList.size() > 0) {
			int attrCount = attrList.size();
			// 遍历为根节点设置属性
			for (int i = 0; i < attrCount; i++) {
				pair = attrList.get(i);
				try {
					String name = pair.getName();
					String value = pair.getValue();

					xmlSerializer.attribute(namespace, name, value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 生成带CDATA的节点
	 * @param namespace
	 * @param xmlSerializer
	 * @param name
	 * @param value
	 */
	public static void generateCDATANode(String namespace,
			XmlSerializer xmlSerializer, String name, String value) {
		NameValuePair pair = new BasicNameValuePair(name, value);
		try {
			xmlSerializer.startTag(namespace, pair.getName());
			xmlSerializer.cdsect(pair.getValue());
			xmlSerializer.endTag(namespace, pair.getName());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
