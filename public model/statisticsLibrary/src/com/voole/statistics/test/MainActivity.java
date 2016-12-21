package com.voole.statistics.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.voole.statistics.R;
import com.voole.statistics.constans.PageStatisticsConstants;
import com.voole.statistics.service.StatisticsPageService;

/**
 * 测试activity
 * 
 * @author Jacky
 * 
 */
public class MainActivity extends Activity {

	private String PageBrowsingReportUrl = 
			"http://172.16.10.147:8080/appplatform/interface/appplatform_PageBrowsingReport_browsingReport.htm?&" +
			"oemid=435&" +
			"packagename=com.voole.debugTraceReport&" +
			"appId=200&" +
			"uid=100666&" +
			"hid=234fwewef"
			+"&version=2.0"
			+"&appversion=2.0.1"
			+"&action=EpgReport"
			;
	
	
	private String url = PageBrowsingReportUrl;
	private int maxMsgCount = 5;
	
	
	private String hid="234fwewef";
	private String oemid="435";
	private String uid="100666";
	private String appId="200";
	private String appVersion="1.0";
	private String packageName="com.voole.debugTraceReport";

	// 发送的数据
	private String epgid="1";
	private String pagetype=PageStatisticsConstants.PAGE_TYPE_INDEX;
	private String moduletype=PageStatisticsConstants.MODULE_TYPE_CHANNELNAVI;
	private String focustype=PageStatisticsConstants.FOCUS_TYPE_CONTENT;
	private String focusid="1";
	private String pagenum="1";
	private String action=PageStatisticsConstants.ACTION_TYPE_ENTER;
	private String input="";
	private String input2;
	private String midlist;
	//private StatisticsPageService statisticsPageService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		buttonInit();
	}

	private void init() {
		// 创建对象
		/*statisticsPageService = StatisticsPageService.getInstance();
		statisticsPageService.initBasicData(hid, oemid, uid, appId, appVersion,
				packageName, url, maxMsgCount);*/
	}

	private void buttonInit() {
		/*Button bt=(Button) findViewById(R.id.fasong);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				statisticsPageService.transferMessage(epgid, pagetype, moduletype,
						focustype, focusid, pagenum, action, input, input2, midlist);
			}
		});*/
		
	}

}
