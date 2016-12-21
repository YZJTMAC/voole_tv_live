package com.gntv.tv.model.channel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.ap.UrlList;
import com.gntv.tv.common.ap.UrlMap;
import com.gntv.tv.common.ap.User;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.base.BaseChannelProgramInfoParser;
import com.gntv.tv.model.base.BaseTodayProgramParser;
import com.gntv.tv.model.cache.CacheManager;
import com.gntv.tv.model.time.TimeManager;
import com.gntv.tv.model.time.TimeParser;


public class ProgramManager {
	private static ProgramManager instance = new ProgramManager();
	private String channelType = "xml"; //片单格式类型分为 son和xml
	private static final int OBJTECT_TYPE = 100;
	private static final int FILE_TYPE = 101;
	private int cacheType = OBJTECT_TYPE;
	private static final String LOCK_FLAG = "LOCK";

	private ProgramManager() {
	}

	public static ProgramManager GetInstance() {
		return instance;
	}
	
	public enum ProgramType{
		LUNBO,
		LIVE,
		ALL
	}
	
	/**
	 * 获取频道以及常用频道
	 * @param programType
	 * @return
	 */
	public TodayProgramInfo getTodayProgramInfoAndMostViewed(ProgramType programType) {
		TodayProgramInfo todayProgramInfo = getTodayProgramInfo(programType);
		MostViewedInfo mostViewedInfo = getMostViewed();
		if(todayProgramInfo!=null&&todayProgramInfo.getAssortList()!=null&&mostViewedInfo!=null&&mostViewedInfo.getStatus()!=null&&"0".equals(mostViewedInfo.getStatus())){
			String slots = mostViewedInfo.getSlots();
			Map<String, List<String>> channelIds = mostViewedInfo.getChannels();
			long currentTime = TimeManager.GetInstance().getCurrentTime();
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(currentTime);
			LogUtil.i("**************************当前时间"+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));
			int num = 24/Integer.parseInt(slots);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			long subTime = currentTime - cal.getTimeInMillis();
			double sub = subTime/3600000.0;
			double d_slot = sub/num;
			int slot = (int) Math.ceil(d_slot);
			LogUtil.i("**************************"+subTime+","+sub+","+d_slot+","+slot);
			List<String> ids = channelIds.get(slot+"");
			if(ids!=null){
				AssortItem item = new AssortItem();
				item.setAssortName("常用频道");
				item.setAssortValue("9999");
				List<ChannelItem> channelItems = new ArrayList<ChannelItem>();
				List<AssortItem> assortItems = todayProgramInfo.getAssortList();
				for(String id:ids){
					ChannelItem channelItem = getChannelItemById(assortItems, id);
					if(channelItem!=null){
						channelItems.add(channelItem);
					}
				}
				item.setChannelList(channelItems);
				todayProgramInfo.getAssortList().add(0, item);
			}
		}
		return todayProgramInfo;
	}
	
	public interface UpdateChannel{
		void addMostViewed(AssortItem item);
	}
	/**
	 * 采用异步的方式来获取常用频道
	 * @param programType
	 * @return
	 */
	public TodayProgramInfo getTodayProgramInfoAndMostViewedAsyn(ProgramType programType,final UpdateChannel updateChannel) {
		final TodayProgramInfo todayProgramInfo = getTodayProgramInfo(programType);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				MostViewedInfo mostViewedInfo = getMostViewed();
				if(todayProgramInfo!=null&&todayProgramInfo.getAssortList()!=null&&mostViewedInfo!=null&&mostViewedInfo.getStatus()!=null&&"0".equals(mostViewedInfo.getStatus())){
					String slots = mostViewedInfo.getSlots();
					Map<String, List<String>> channelIds = mostViewedInfo.getChannels();
					long currentTime = TimeManager.GetInstance().getCurrentTime();
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(currentTime);
					LogUtil.i("**************************当前时间"+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));
					int num = 24/Integer.parseInt(slots);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					long subTime = currentTime - cal.getTimeInMillis();
					double sub = subTime/3600000.0;
					double d_slot = sub/num;
					int slot = (int) Math.ceil(d_slot);
					LogUtil.i("**************************"+subTime+","+sub+","+d_slot+","+slot);
					List<String> ids = channelIds.get(slot+"");
					if(ids!=null){
						AssortItem item = new AssortItem();
						item.setAssortName("常用频道");
						item.setAssortValue("9999");
						List<ChannelItem> channelItems = new ArrayList<ChannelItem>();
						List<AssortItem> assortItems = todayProgramInfo.getAssortList();
						for(String id:ids){
							ChannelItem channelItem = getChannelItemById(assortItems, id);
							if(channelItem!=null){
								channelItems.add(channelItem);
							}
						}
						item.setChannelList(channelItems);
						if(updateChannel!=null){
							synchronized (LOCK_FLAG) {
								updateChannel.addMostViewed(item);
							}
						}
					}
				}
			}
		}).start();

		return todayProgramInfo;
	}
	
	private ChannelItem getChannelItemById(List<AssortItem> assortItems,String cid){
		for(AssortItem item:assortItems){
			List<ChannelItem> channelItems = item.getChannelList();
			for(ChannelItem channelItem:channelItems){
				if(cid.equals(channelItem.getChannelId())){
					return channelItem;
				}
			}
		}
		return null;
	}
	
	public MostViewedInfo getMostViewed(){
		try {
			MostViewedInfoParser parser = new MostViewedInfoParser();
			boolean isExists = CacheManager.GetInstance().checkMostViewedInfoExists();
			if (isExists) {
				LogUtil.i("ProgramManager--->CacheManager.GetInstance().getMostViewedInfo()------>start");
				InputStream stream = CacheManager.GetInstance().getMostViewedInfo();
				LogUtil.i("ProgramManager--->CacheManager.GetInstance().getMostViewedInfo()------>end");
				parser.setInputStream(stream);
			} else {
				LogUtil.i("ProgramManager--->getMostViewed--->start");
				//String url = getLiveBaseUrl() + "&ctype=4&pre=7&next=0&datatype="; // TBD
				//String url = "http://udc.cp11.ott.cibntv.net:81/getSingleDataMultiKey?keyst%20=100153&keynd=5cc6d0ae4fde&keyrd=allofcha";
				User user = AuthManager.GetInstance().getUser();
				String url = getMostViewedUrl() + "?keyst=" + user.getOemid() + "&keynd=" + user.getHid() + "&keyrd=allofcha";
				//url = "http://172.16.10.51:81/getSingleDataMultiKey?keyst=100100&keynd=E8BB3D558CCA&keyrd=allofcha";
				LogUtil.d("ProgramManager-->getMostViewed-->" + url);
				boolean b = CacheManager.GetInstance().saveMostViewedInfo(url);
				LogUtil.i("ProgramManager--->getMostViewed--->end--->" + b);
				if (b) {
					LogUtil.i("ProgramManager--->CacheManager.GetInstance().getMostViewedInfo()------>start");
					InputStream stream = CacheManager.GetInstance().getMostViewedInfo();
					LogUtil.i("ProgramManager--->CacheManager.GetInstance().getMostViewedInfo()------>end");
					parser.setInputStream(stream);
				} else {
					parser.setUrl(url);
				}
				LogUtil.i("ProgramManager--->getMostViewed--->parser end------>");
			}
			return parser.getInfo();
		} catch (Exception e) {
			LogUtil.e("ProgramManager-->getMostViewed-->"+e.toString());
		}
		return null;
	}
	

	
	/**
	 * 从文件中读取数据
	 * @param programType
	 * @return
	 */
	private TodayProgramInfo getTodayProgramInfoFromFile(ProgramType programType){
		try {
			BaseTodayProgramParser parser = null;
			if("xml".equalsIgnoreCase(channelType)){
				parser = new TodayProgramInfoParser();
			}else{
				parser = new TodayProgramInfoJsonParser();
			}
			boolean isExists = CacheManager.GetInstance().checkTodayProgramInfoExists();
			if (isExists) {
				LogUtil.i("ProgramManager--->CacheManager.GetInstance().getTodayProgramInfoFromJson()------>start");
				InputStream todayProgramInfoStream = CacheManager.GetInstance().getTodayProgramInfo();
				LogUtil.i("ProgramManager--->CacheManager.GetInstance().getTodayProgramInfoFromJson()------>end");
				parser.setInputStream(todayProgramInfoStream);
			} else {
				LogUtil.i("ProgramManager--->getTodayProgramInfoFromFile--->获取服务端频道列表开始");
				String url = null;
				if("xml".equalsIgnoreCase(channelType)){
					url = getLiveBaseUrl() + "&ctype=4&pre=7&next=0&datatype=";
				}else{
					url = getLiveJsonUrl() + "&ctype=4&pre=7&next=0&datatype=";
				}
				switch (programType) {
				case LUNBO:
					url = url + "2";
					break;
				case LIVE:
					url = url + "1";
					break;
				case ALL:
					url = url + "0";
					break;

				default:
					break;
				}
				LogUtil.d("ProgramManager-->getTodayProgramInfoFromFile-->" + url);
				CacheManager.GetInstance().clear();
				boolean b = CacheManager.GetInstance().saveTodayProgramInfo(url);
				LogUtil.i("ProgramManager--->getTodayProgramInfoFromFile--->获取服务端频道列表结束------>" + b);
				if (b) {
					LogUtil.i("ProgramManager--->CacheManager.GetInstance().getTodayProgramInfoFromJson()------>start");
					InputStream todayProgramInfoStream = CacheManager.GetInstance().getTodayProgramInfo();
					LogUtil.i("ProgramManager--->CacheManager.GetInstance().getTodayProgramInfoFromJson()------>end");
					parser.setInputStream(todayProgramInfoStream);
				} else {
					parser.setUrl(url);
				}
				LogUtil.i("ProgramManager--->getTodayProgramInfoFromFile--->parser end------>");
			}
			/*if(parser.getInfo() != null){
				cacheChannelProgramInfo();
			}*/
			return parser.getInfo();
		} catch (Exception e) {
			LogUtil.e("ProgramManager-->getTodayProgramInfoFromJson-->"+e.toString());
		}
		return null;
	}
	
	
	
	
	/**
	 * 从对象流中读取数据
	 * @param programType
	 * @return
	 */
	private TodayProgramInfo getTodayProgramInfoFromObject(ProgramType programType) {
		TodayProgramInfo todayProgramInfo = null;
		try {
			boolean isExists = CacheManager.GetInstance().checkTodayProgramInfoExists();
			if (isExists) {
				LogUtil.i("ProgramManager--->CacheManager.GetInstance().getTodayProgramInfoObject()------>start");
				todayProgramInfo = CacheManager.GetInstance().getTodayProgramInfoObject();
				LogUtil.i("ProgramManager--->CacheManager.GetInstance().getTodayProgramInfoObject()------>end");
			} else {
				BaseTodayProgramParser parser = null;
				String url = null;
				if("xml".equalsIgnoreCase(channelType)){
					parser = new TodayProgramInfoParser();
					url = getLiveBaseUrl() + "&ctype=4&pre=7&next=0&datatype=";
				}else{
					parser = new TodayProgramInfoJsonParser();
					url = getLiveJsonUrl() + "&ctype=4&pre=7&next=0&datatype=";
				}
				LogUtil.i("ProgramManager--->getTodayProgramInfoFromObject--->获取服务端频道列表开始");
				switch (programType) {
				case LUNBO:
					url = url + "2";
					break;
				case LIVE:
					url = url + "1";
					break;
				case ALL:
					url = url + "0";
					break;

				default:
					break;
				}
				LogUtil.d("ProgramManager-->getTodayProgramInfoFromObject-->" + url);
				CacheManager.GetInstance().clear();
				parser.setUrl(url);
				LogUtil.i("ProgramManager--->getTodayProgramInfoFromObject--->parser end------>");
				todayProgramInfo = parser.getInfo();
				final TodayProgramInfo info = todayProgramInfo;
				new Thread(new Runnable() {
						@Override
						public void run() {
							synchronized (LOCK_FLAG) {
								LogUtil.i("ProgramManager--->CacheManager.GetInstance().saveTodayProgramInfo()------>start");
								boolean result = CacheManager.GetInstance().saveTodayProgramInfo(info);
								LogUtil.i("ProgramManager--->CacheManager.GetInstance().saveTodayProgramInfo()------>end::"+result);
							}
						}
					}
					
				).start();
				
			}
		} catch (Exception e) {
			LogUtil.e("ProgramManager-->getTodayProgramInfoFromObject-->"+e.toString());
		}
		return todayProgramInfo;
	}
	
	public TodayProgramInfo getTodayProgramInfo(ProgramType programType) {
		if(cacheType == FILE_TYPE){
			return getTodayProgramInfoFromFile(programType);
		}else{
			return getTodayProgramInfoFromObject(programType);
		}
	}
	
	public TodayProgramInfo updateTodayProgramInfo(ProgramType programType) {
		if(cacheType == FILE_TYPE){
			return updateTodayProgramInfoFromFile(programType);
		}else{
			return updateTodayProgramInfoFromObject(programType);
		}
	}
	
	
	private TodayProgramInfo updateTodayProgramInfoFromObject(ProgramType programType) {
		TodayProgramInfo todayProgramInfo = null;
		try {
				BaseTodayProgramParser parser = null;
				String url = null;
				if("xml".equalsIgnoreCase(channelType)){
					parser = new TodayProgramInfoParser();
					url = getLiveBaseUrl() + "&ctype=4&pre=7&next=0&datatype=";
				}else{
					parser = new TodayProgramInfoJsonParser();
					url = getLiveJsonUrl() + "&ctype=4&pre=7&next=0&datatype=";
				}
				LogUtil.i("ProgramManager--->updateTodayProgramInfoFromObject--->获取服务端频道列表开始");
				switch (programType) {
				case LUNBO:
					url = url + "2";
					break;
				case LIVE:
					url = url + "1";
					break;
				case ALL:
					url = url + "0";
					break;

				default:
					break;
				}
				LogUtil.d("ProgramManager-->updateTodayProgramInfoFromObject-->" + url);
				CacheManager.GetInstance().clearToday();
				parser.setUrl(url);
				LogUtil.i("ProgramManager--->updateTodayProgramInfoFromObject--->parser end------>");
				todayProgramInfo = parser.getInfo();
				LogUtil.i("ProgramManager--->CacheManager.GetInstance().saveTodayProgramInfo()------>start");
				boolean result = CacheManager.GetInstance().saveTodayProgramInfo(todayProgramInfo);
				LogUtil.i("ProgramManager--->CacheManager.GetInstance().saveTodayProgramInfo()------>end::"+result);
		} catch (Exception e) {
			LogUtil.e("ProgramManager-->updateTodayProgramInfoFromObject-->"+e.toString());
		}
		return todayProgramInfo;
	}

	/**
	 * 从文件中读取数据
	 * @param programType
	 * @return
	 */
	private TodayProgramInfo updateTodayProgramInfoFromFile(ProgramType programType){
		try {
				BaseTodayProgramParser parser = null;
				if("xml".equalsIgnoreCase(channelType)){
					parser = new TodayProgramInfoParser();
				}else{
					parser = new TodayProgramInfoJsonParser();
				}
			
				LogUtil.i("ProgramManager--->updateTodayProgramInfoFromFile--->获取服务端频道列表开始");
				String url = null;
				if("xml".equalsIgnoreCase(channelType)){
					url = getLiveBaseUrl() + "&ctype=4&pre=7&next=0&datatype=";
				}else{
					url = getLiveJsonUrl() + "&ctype=4&pre=7&next=0&datatype=";
				}
				switch (programType) {
				case LUNBO:
					url = url + "2";
					break;
				case LIVE:
					url = url + "1";
					break;
				case ALL:
					url = url + "0";
					break;

				default:
					break;
				}
				LogUtil.d("ProgramManager-->updateTodayProgramInfoFromFile-->" + url);
				CacheManager.GetInstance().clearToday();
				boolean b = CacheManager.GetInstance().saveTodayProgramInfo(url);
				LogUtil.i("ProgramManager--->updateTodayProgramInfoFromFile--->获取服务端频道列表结束------>" + b);
				if (b) {
					LogUtil.i("ProgramManager--->CacheManager.GetInstance().getTodayProgramInfoFromJson()------>start");
					InputStream todayProgramInfoStream = CacheManager.GetInstance().getTodayProgramInfo();
					LogUtil.i("ProgramManager--->CacheManager.GetInstance().getTodayProgramInfoFromJson()------>end");
					parser.setInputStream(todayProgramInfoStream);
				} else {
					parser.setUrl(url);
				}
				LogUtil.i("ProgramManager--->updateTodayProgramInfoFromFile--->parser end------>");
			return parser.getInfo();
		} catch (Exception e) {
			LogUtil.e("ProgramManager-->updateTodayProgramInfoFromFile-->"+e.toString());
		}
		return null;
	}
	

	public ChannelProgramInfo getChannelProgramInfo(String channelId) {
		if(cacheType == FILE_TYPE){
			return getChannelProgramInfoFromFile(channelId);
		}else{
			return getChannelProgramInfoFromObject(channelId);
		}
	}
	
	private ChannelProgramInfo getChannelProgramInfoFromFile(String channelId) {
		BaseChannelProgramInfoParser parser = null;
		if("xml".equalsIgnoreCase(channelType)){
			parser = new ChannelProgramInfoParser();
		}else{
			parser = new ChannelProgramInfoJsonParser();
		}
		try {
			boolean isExists = CacheManager.GetInstance().checkChannelProgramInfoExists(channelId);
			if (isExists) {
				parser.setInputStream(CacheManager.GetInstance().getChannelProgramInfo(channelId));
			} else {
				LogUtil.i("ProgramManager--->getChannelProgramInfoFromFile--->获取服务端节目列表开始");
				String url = null;
				if("xml".equalsIgnoreCase(channelType)){
					url = getLiveBaseUrl() + "&ctype=5&pre=7&next=0&tvid=" + channelId;
				}else{
					url = getLiveJsonUrl() + "&ctype=5&pre=7&next=0&tvid=" + channelId;
				}
				LogUtil.d("ProgramManager-->getChannelProgramInfoFromFile-->" + url);
				boolean b = CacheManager.GetInstance().saveChannelProgramInfo(channelId, url);
				LogUtil.i("ProgramManager--->getChannelProgramInfoFromFile--->获取服务端节目列表结束");
				if (b) {
					parser.setInputStream(CacheManager.GetInstance().getChannelProgramInfo(channelId));
				} else {
					parser.setUrl(url);
				}
			}
			return parser.getChannelProgramInfo();
		} catch (Exception e) {
			LogUtil.d("ProgramManager-->getChannelProgramInfoFromFile-->Exception");
		}
		return null;
	}
	
	private ChannelProgramInfo getChannelProgramInfoFromObject(String channelId) {
		ChannelProgramInfo channelProgramInfo = null;
		try {
			boolean isExists = CacheManager.GetInstance().checkChannelProgramInfoExists(channelId);
			if (isExists) {
				LogUtil.i("ProgramManager--->CacheManager.GetInstance().getChannelProgramInfoObject()------>start");
				channelProgramInfo = CacheManager.GetInstance().getChannelProgramInfoObject(channelId);
				LogUtil.i("ProgramManager--->CacheManager.GetInstance().getChannelProgramInfoObject()------>end");
			} else {
				BaseChannelProgramInfoParser parser = null;
				String url = null;
				if("xml".equalsIgnoreCase(channelType)){
					parser = new ChannelProgramInfoParser();
					url = getLiveBaseUrl() + "&ctype=5&pre=7&next=0&tvid=" + channelId;
				}else{
					parser = new ChannelProgramInfoJsonParser();
					url = getLiveJsonUrl() + "&ctype=5&pre=7&next=0&tvid=" + channelId;
				}
				LogUtil.d("ProgramManager-->getChannelProgramInfoFromObject-->" + url);
				LogUtil.i("ProgramManager--->getChannelProgramInfoFromObject--->获取服务端节目列表开始");
				parser.setUrl(url);
				LogUtil.i("ProgramManager--->getChannelProgramInfoFromObject--->获取服务端节目列表结束");
				channelProgramInfo = parser.getChannelProgramInfo();
				
				LogUtil.i("ProgramManager--->CacheManager.GetInstance().saveChannelProgramInfo()------>start");
				boolean result = CacheManager.GetInstance().saveChannelProgramInfo(channelProgramInfo,channelId);
				LogUtil.i("ProgramManager--->CacheManager.GetInstance().saveChannelProgramInfo()------>end::"+result);
			
			}
			//return parser.getChannelProgramInfo();
		} catch (Exception e) {
			LogUtil.d("ProgramManager-->getChannelProgramInfoFromFile-->Exception");
		}
		return channelProgramInfo;
	}

	public String getCurrentTime() {
		TimeParser parser = new TimeParser();
		try {
			//String url = getUrlMap()!=null?getUrlMap().get("currentTime"):null;
			String url = AuthManager.GetInstance().getUrlList().getCurrentTime();
			parser.setUrl(url);
			String time = parser.getTime();
			LogUtil.d("ProgramManager--->getCurrentTime----->" + time + "----url--->" + url);
			return time;
		} catch (Exception e) {
			LogUtil.e("ProgramManager-->getCurrentTime-->Exception");
			return null;
		}
	}
	
	/*private Map<String,String> getUrlMap(){
		UrlMap urlMap = AuthManager.GetInstance().getUrlMap();
		return urlMap!=null?urlMap.getUrlMap():null;
	}*/

	private String getLiveBaseUrl() {
		UrlList urlList = AuthManager.GetInstance().getUrlList();
		if (urlList == null) {
			return null;
		}
		return urlList.getLiveTVUrl();
		//return getUrlMap()!=null?getUrlMap().get("livetvnew"):null;
	}
	
	private String getLiveJsonUrl(){
		UrlList urlList = AuthManager.GetInstance().getUrlList();
		if (urlList == null) {
			return null;
		}
		return urlList.getLiveTVJson();
		//return getUrlMap()!=null?getUrlMap().get("livetvjson"):null;
	}
	
	/**
	 * 获取常用频道接口
	 * @return
	 */
	private String getMostViewedUrl(){
		UrlList urlList = AuthManager.GetInstance().getUrlList();
		if (urlList == null) {
			return null;
		}
		String url = urlList.getMosttvchannel();
		if(url!=null){
			url = (url.contains("?")) ? url.substring(0, url.indexOf("?")) : url;
		}
		return url;
		/*String url = getUrlMap()!=null?getUrlMap().get("mosttvchannel"):null;
		if(url!=null){
			url = (url.contains("?")) ? url.substring(0, url.indexOf("?")) : url;
		}
		return url;*/
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	
}
