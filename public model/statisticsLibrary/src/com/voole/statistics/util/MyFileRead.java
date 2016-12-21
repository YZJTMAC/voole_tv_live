package com.voole.statistics.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.content.Context;

import com.voole.statistics.bean.ConfigFileBean;

public class MyFileRead {
	
	
	/**
	 * 读取配置文件
	 * @param fileName
	 * @param context
	 * @return
	 */
	public ConfigFileBean getConfigFileAssets(String fileName,Context context){
		
		ConfigFileBean cfb=new ConfigFileBean();
        try { 
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName)); 
           BufferedReader bufReader = new BufferedReader(inputReader);
           String line="";
           StringBuffer result=new StringBuffer();
           while((line = bufReader.readLine()) != null)
           {
        	   result.append(line);
           }
           if(null!=result&&!"".equals(result.toString().trim()))
           {
        	   int i=result.indexOf("local_http_port = \"");
        	   if(-1!=i)
        	   {
        		   String str1=result.toString().substring(i+"local_http_port = \"".length(), result.length());
        		   int i2=str1.indexOf("\"");
        		   String str2= str1.substring(0, i2);
            	   cfb.setLocal_http_port(str2);
        	   }
        	   
        	   int i2=result.indexOf("local_agent_http_port = \"");
        	   if(-1!=i2)
        	   {
        		   String str1=result.toString().substring(i2+"local_agent_http_port = \"".length(), result.length());
        		   int i3=str1.indexOf("\"");
        		   String str2= str1.substring(0, i3);
            	   cfb.setLocal_agent_http_port(str2);
        	   }
           }
           
       } catch (Exception e) { 
           e.printStackTrace(); 
       }
		return cfb;
    }
	
	/**
	 * 读取配置文件
	 * @param fileName
	 * @param context
	 * @return
	 */
	public ConfigFileBean getConfigFileAssets(String fileName,Context context,String fileAddress){
		
		ConfigFileBean cfb=new ConfigFileBean();
        try { 
        	

	       StringPrint.print("配置文件读取了" +"");
        	
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(new File(fileAddress)));
            BufferedReader bufReader = new BufferedReader(inputReader);
           String line="";
           StringBuffer result=new StringBuffer();
           while((line = bufReader.readLine()) != null)
           {
        	   result.append(line);
           }
           if(null!=result&&!"".equals(result.toString().trim()))
           {
        	   int i=result.indexOf("local_http_port = \"");
        	   if(-1!=i)
        	   {
        		   String str1=result.toString().substring(i+"local_http_port = \"".length(), result.length());
        		   int i2=str1.indexOf("\"");
        		   String str2= str1.substring(0, i2);
            	   cfb.setLocal_http_port(str2);
        	   }
        	   
        	   int i2=result.indexOf("local_agent_http_port = \"");
        	   if(-1!=i2)
        	   {
        		   String str1=result.toString().substring(i2+"local_agent_http_port = \"".length(), result.length());
        		   int i3=str1.indexOf("\"");
        		   String str2= str1.substring(0, i3);
            	   cfb.setLocal_agent_http_port(str2);
        	   }
           }
           
       } catch (Exception e) { 
           e.printStackTrace(); 
       }
		return cfb;
    }

}
