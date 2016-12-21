package com.voole.statistics.util;


import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类
 * 
 * @author Jacky
 * 
 */
public class StringUtil {
	public static String delHTMLTag(String htmlStr) {
		String regEx_style = "(class|style)=\"[A-Za-z0-9]*[^%:&’,;=?$x22]+[A-Za-z0-9]*\""; // 定义style、class的正则表达式

		Pattern p_style = Pattern.compile(regEx_style);
		Matcher m_style = p_style.matcher(htmlStr);
		System.out.println(m_style);
		htmlStr = m_style.replaceAll(""); // 过滤style、class属性

		return htmlStr; // 返回文本字符串
	}

	/**
	 * 对字符串进行MD5加密并返回
	 * 
	 * @param str
	 * @return
	 */
	public static String StringMd5(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(str.getBytes());
			byte bytes[] = digest.digest();

			StringBuilder hexString = new StringBuilder();
			for (byte b : bytes) {
				hexString.append(String.format("%02x", b));
			}
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static boolean isNull(List<? extends Object> slist)
	{
		if(null!=slist&&slist.size()>0)
		{
			return true;
		}
		return false;
	}
	
	
	 
	
	/**
	 * 返回true表示不是空
	 *    false表示是空
	* @author Jacky   
	* @date 2015-5-25 下午02:32:12
	 */
	public static boolean isNull(String[] slist)
	{
		if(null!=slist&&slist.length>0)
		{
			return true;
		}
		return false;
	}
	
	
	

	/**
	 * 返回true表示不是空
	 *    false表示是空
	* @author Jacky   
	* @date 2015-5-25 下午02:32:12
	 */
	public static boolean isNull(String s)
	{
		
		if(null!=s&&!"".equals(s.trim())&&!"null".equals(s.trim()))
		{
		    return true;
		}
		return false;
	}
}
