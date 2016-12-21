package com.voole.statistics.terminalinfo;

import android.text.TextUtils;

import com.voole.statistics.report.ReportBaseInfo;
import com.voole.statistics.report.ReportConfig;

public class CreateXmlUtil {

	public static String createApkInfoXml(AppReportInfo reportInfo, String sessid, String memery, String gpu, String cpu) {
		StringBuffer stringBuffer=new  StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		stringBuffer.append("<MessageArray count=\""+1+"\" >");
		stringBuffer.append("<Header ");
		stringBuffer.append("/>");
		stringBuffer.append("<Array>");

		stringBuffer.append("<Message type=\"apkinfo\" >");
		stringBuffer.append("<Body>");
		stringBuffer.append("CDATA[");
		stringBuffer.append("<info ");
		
		ReportBaseInfo baseInfo = reportInfo.getInfo();

		if(!TextUtils.isEmpty(baseInfo.getHid())){
			stringBuffer.append(" hid=\""+baseInfo.getHid()+"\" ");
		} else {
			stringBuffer.append(" hid=\""+""+"\" ");
		}

		if(!TextUtils.isEmpty(baseInfo.getUid())){
			stringBuffer.append(" uid=\""+baseInfo.getUid()+"\" ");
		}else {
			stringBuffer.append(" uid=\""+""+"\" ");
		}

		if(!TextUtils.isEmpty(reportInfo.getVooleAuth())){
			stringBuffer.append(" vooleauth=\""+reportInfo.getVooleAuth()+"\" ");
		} else {
			stringBuffer.append(" vooleauth=\""+""+"\" ");
		}

		if(!TextUtils.isEmpty(reportInfo.getAuthCompile())){
			stringBuffer.append(" authcompile=\""+reportInfo.getAuthCompile()+"\" ");
		}else {
			stringBuffer.append(" authcompile=\""+""+"\" ");
		}

		if(!TextUtils.isEmpty(reportInfo.getVooleAgent())){
			stringBuffer.append(" vooleagent=\""+reportInfo.getVooleAgent()+"\" ");
		} else {
			stringBuffer.append(" vooleagent=\""+""+"\" ");
		}

		if(!TextUtils.isEmpty(reportInfo.getAgentCompile())){
			stringBuffer.append(" agentcompile=\""+reportInfo.getAgentCompile()+"\" ");
		} else {
			stringBuffer.append(" agentcompile=\""+""+"\" ");
		}

		if (!TextUtils.isEmpty(reportInfo.getUpgradeVersion())) {
			stringBuffer.append(" UpgradeVersion=\"" + reportInfo.getUpgradeVersion() +"\" ");
		} else {
			stringBuffer.append(" UpgradeVersion=\"" + "" +"\" ");
		}

		//		if (!TextUtils.isEmpty(reportInfo.getStatisticsVersion())) {
		//			stringBuffer.append(" statisticsVersion=\""+reportInfo.getStatisticsVersion()+"\" ");
		//		}
		stringBuffer.append(" statisticsVersion=\""+ReportConfig.statisticsVersion+"\" ");

		if (!TextUtils.isEmpty(reportInfo.getTerminaLogVersion())) {
			stringBuffer.append(" TerminaLogVersion=\""+reportInfo.getTerminaLogVersion() +"\" ");
		} else {
			stringBuffer.append(" TerminaLogVersion=\""+"" +"\" ");
		}

		if (!TextUtils.isEmpty(reportInfo.getApkStartType())) {
			stringBuffer.append(" apkStartType=\"" + reportInfo.getApkStartType() + "\" ");
		} else {
			stringBuffer.append(" apkStartType=\"" + "" + "\" ");
		}

		if (!TextUtils.isEmpty(reportInfo.getSdkModuleVersion())) {
			stringBuffer.append(" sdkmoduleversion=\""+reportInfo.getSdkModuleVersion()+"\" ");
		} else {
			stringBuffer.append(" sdkmoduleversion=\""+""+"\" ");
		}
		if (!TextUtils.isEmpty(reportInfo.getSdkModuleType())) {
			stringBuffer.append(" sdkmoduletype=\""+reportInfo.getSdkModuleType()+"\" ");
		} else {
			stringBuffer.append(" sdkmoduletype=\""+""+"\" ");
		}

		if (!TextUtils.isEmpty(reportInfo.getIsAuth()) && ("0".equals(reportInfo.getIsAuth())|| "1".equals(reportInfo.getIsAuth()) 
				|| "-1".equals(reportInfo.getIsAuth()))) {
			stringBuffer.append(" isauth=\""+reportInfo.getIsAuth()+"\" ");
		} else {
			stringBuffer.append(" isauth=\""+""+"\" ");
		}

		if (!TextUtils.isEmpty(reportInfo.getSn())) {
			stringBuffer.append(" sn=\""+reportInfo.getSn()+"\" ");
		} else {
			stringBuffer.append(" sn=\""+""+"\" ");
		}

		if (!TextUtils.isEmpty(reportInfo.getDeviceid())) {
			stringBuffer.append(" deviceid=\""+reportInfo.getDeviceid()+"\" ");
		} else {
			stringBuffer.append(" deviceid=\""+""+"\" ");
		}

		stringBuffer.append(" Sessid=\""+sessid.toString()+"\" ");

		if (!TextUtils.isEmpty(memery)) {
			stringBuffer.append(" Memery=\""+memery+"\" ");
		} else {
			stringBuffer.append(" Memery=\""+""+"\" ");
		}
			

		if (!TextUtils.isEmpty(memery)) {
			stringBuffer.append(" GPU=\""+gpu+"\" ");
		} else {
			stringBuffer.append(" GPU=\""+""+"\" ");
		}

		if (!TextUtils.isEmpty(memery)) {
			stringBuffer.append(" CPU=\""+cpu+"\" ");
		} else {
			stringBuffer.append(" CPU=\""+""+"\" ");
		}

		stringBuffer.append("> ");

		stringBuffer.append("<agentlibs><![CDATA[");
		if(null!=reportInfo.getAgentLibs()&&!"".equals(reportInfo.getAgentLibs().trim())){
			stringBuffer.append(reportInfo.getAgentLibs());
		}
		stringBuffer.append("]]></agentlibs>");

		stringBuffer.append("</info>");
		stringBuffer.append("]");
		stringBuffer.append("</Body>");
		stringBuffer.append("</Message>");

		stringBuffer.append("</Array>");
		stringBuffer.append("</MessageArray>");

		return stringBuffer.toString();
	}

	public static String createLiveInfoXml(String sessid, String intranetHost, String gatwayInfo) {

		StringBuffer stringBuffer=new  StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		stringBuffer.append("<MessageArray count=\""+1+"\" >");
		stringBuffer.append("<Header ");
		stringBuffer.append("/>");
		stringBuffer.append("<Array>");

		stringBuffer.append("<Message type=\"apklive\" >");
		stringBuffer.append("<Body>");
		stringBuffer.append("CDATA[");
		stringBuffer.append("<info ");
		stringBuffer.append(" Sessid=\""+sessid.toString()+"\" ");

		if (!TextUtils.isEmpty(intranetHost)) {
			stringBuffer.append(" Intranet_host=\""+intranetHost+"\" ");
		} else {
			stringBuffer.append(" Intranet_host=\""+""+"\" ");
		}

		if (!TextUtils.isEmpty(gatwayInfo)) {
			stringBuffer.append(" GatwayInfos=\""+gatwayInfo+"\" ");
		} else {
			stringBuffer.append(" GatwayInfos=\""+""+"\" ");
		}

		stringBuffer.append("> ");
		stringBuffer.append("</info>");
		stringBuffer.append("]");
		stringBuffer.append("</Body>");
		stringBuffer.append("</Message>");

		stringBuffer.append("</Array>");
		stringBuffer.append("</MessageArray>");

		return stringBuffer.toString();
	}

}
