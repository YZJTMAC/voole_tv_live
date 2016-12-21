package com.gntv.tv.model.channel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import com.gntv.tv.common.utils.NetUtil;


public class MostViewedInfoParser{
	private MostViewedInfo info = null;
	
	public void setUrl(String url) throws Exception {
		InputStream stream = NetUtil.connectServer(url, 2, 5, 10);
		if (stream != null) {
			setInputStream(stream);
		}
	}
	
	public void setInputStream(InputStream inputStream) throws Exception {
		if (inputStream != null) {
			// do parse
			StringBuilder sb = new StringBuilder();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String sCurrentLine = "";
			while ((sCurrentLine = bufferedReader.readLine()) != null) {
				sb.append(sCurrentLine);
			}
			JSONObject jsonObj = new JSONObject(sb.toString());
			info = new MostViewedInfo();
			int resultcode = jsonObj.getInt("resultcode");
			if(resultcode == 0){
				info.setStatus(resultcode+"");
				info.setResultDesc(jsonObj.getString("msg"));
				JSONArray responseData = jsonObj.getJSONArray("responsedata");
				JSONObject obj = responseData.getJSONObject(0);
				info.setSlots(obj.getString("slots"));
				HashMap<String, List<String>> channels = new HashMap<String,List<String>>();
				JSONArray channle = obj.getJSONArray("channel");
				for(int i = 0;i<channle.length();i++){
					JSONObject data = channle.getJSONObject(i);
					Iterator iterator = data.keys();
					String key = (String) iterator.next();
					String values = data.getString(key);
					channels.put(key,Arrays.asList(values.split(",")));
				}
				info.setChannels(channels);
			}
			inputStream.close();
			inputStream = null;
		}
	}
	 
	public MostViewedInfo getInfo(){
		return info;
	}

}
