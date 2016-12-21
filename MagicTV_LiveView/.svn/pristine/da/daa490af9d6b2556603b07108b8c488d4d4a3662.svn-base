package com.gntv.tv.view;

import java.util.ArrayList;
import java.util.List;

import com.gntv.tv.R;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.channel.AssortItem;
import com.gntv.tv.model.channel.ChannelItem;
import com.gntv.tv.model.channel.ProgramItem;
import com.gntv.tv.model.time.TimeManager;
import com.gntv.tv.view.base.BaseChannelRelativeLayout;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.FontSize;
import com.gntv.tv.view.base.ViewConst;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChannelListView extends BaseChannelRelativeLayout{
	private TextView txtTitle = null;
	private LinearLayout ll_channel = null;
	private static final int ITEM_VIEW_SIZE = 7;
	private static final int UPDATE_CHANNEL = 21;
	public int ACTUAL_VIEW_SIZE = ITEM_VIEW_SIZE;
	private ChannelItemView[] channelItems = null;
	private List<ChannelItem> channelItemList = null;
	public int currentChannelIndex = 0;
	public int currentChannelViewIndex = 0;
	private long timeMsec;
	private int channelListSize = 0;
	private int playState = ViewConst.LIVE_STATE;
	private String playingChannelId = "";
	private int ItemHeight = 0;
	private int screenHeight;
	private int width = 0;
	private LayoutParams llParams = null;
	private volatile boolean isLoaded = false;
	private ImageView leftView = null;
	private ImageView rightView = null;
	private ImageView leftDivideView = null;
	//private ArrayList<ChannelItem> channelItemArray = new ArrayList<ChannelItem>(ITEM_VIEW_SIZE);
	
	public volatile boolean isProgramThreadLive = false;
	public int lastProgramWaitTime = 0;
	public boolean isProgramOnKeyUp = false;
	public volatile boolean programThreadState = false;
	//private ChannelItem[] channelItemArray = new ChannelItem[7];
	
	private Handler handler = new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_CHANNEL:
				List<String> contentStrs = (List<String>) msg.obj;
				LogUtil.i("ChannelListView--->ACTUAL_VIEW_SIZE::"+ACTUAL_VIEW_SIZE+",contentStrs::"+contentStrs.size());
				if(contentStrs!=null&&contentStrs.size()>=ACTUAL_VIEW_SIZE){
					for (int i = 0; i < ACTUAL_VIEW_SIZE; i++){
						channelItems[i].setContent(contentStrs.get(i));
					}
				}
				
				break;

			default:
				break;
			}
		};
	};
	
	public ChannelListView(Context context) {
		super(context);
		init(context);
	}

	@Override
	protected void init(Context context){
		setFocusable(true);
		setFocusableInTouchMode(true);
		width = DisplayManager.GetInstance().getScreenWidth() *1/2;
		screenHeight = DisplayManager.GetInstance().getScreenHeight();
		//setBackgroundResource(R.drawable.background_right);
		initLeft(context);
		initTitle(context,this);
		initChannel(context,this);
		initRight(context);
	}
	
	private void initRight(Context context) {
		rightView = new ImageView(context);
		rightView.setId(20004);
		rightView.setBackgroundResource(R.drawable.cs_right_arrow);
		rightView.setVisibility(VISIBLE);
		LayoutParams div_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		div_params.addRule(RIGHT_OF,20003);
		div_params.setMargins(changeWidth(10),screenHeight/2+changeHeight(10), 0, 0);
		rightView.setLayoutParams(div_params);
		addView(rightView);
	}

	private void initLeft(Context context) {
		leftDivideView = new ImageView(context);
		leftDivideView.setId(20002);
		leftDivideView.setBackgroundResource(R.drawable.cs_right_divide);
		leftDivideView.setVisibility(VISIBLE);
		LayoutParams div_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		//div_params.addRule(BELOW, 20001);
		leftDivideView.setLayoutParams(div_params);
		addView(leftDivideView);
		
		leftView = new ImageView(context);
		leftView.setBackgroundResource(R.drawable.cs_left_arrow);
		leftView.setVisibility(GONE);
		LayoutParams arrow_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		arrow_params.setMargins(changeWidth(20),screenHeight/2+changeHeight(10), 0, 0);
		leftView.setLayoutParams(arrow_params);
		addView(leftView);
	}

	private void initTitle(Context context,ViewGroup parent){
		txtTitle = new TextView(context);
		txtTitle.setId(20001);
		//txtTitle.setText(R.string.channel_list);
		txtTitle.setGravity(Gravity.CENTER_VERTICAL);
		txtTitle.setTextSize(changeTextSize(FontSize.CHANNEL_TITLE));
		txtTitle.setTextColor(getColor(R.color.deep_text));
		LayoutParams txtParams = new LayoutParams(LayoutParams.MATCH_PARENT,changeHeight(60));
		//txtParams.setMargins(changeWidth(1), 0, 0, 0);
		txtTitle.setLayoutParams(txtParams);
		txtTitle.setPadding(changeWidth(50),changeHeight(10), 0, 0);
		//txtTitle.setBackgroundResource(R.drawable.cs_bg_focus2);
		parent.addView(txtTitle);
	}
	
	private void initChannel(Context context,ViewGroup parent){
		ll_channel = new LinearLayout(context);
		ll_channel.setId(20003);
		ItemHeight = (screenHeight-changeHeight(120))/ITEM_VIEW_SIZE;
		llParams  = new LayoutParams(width*3/7+changeWidth(55),LayoutParams.MATCH_PARENT);
		llParams.setMargins(0, changeHeight(20), 0, changeHeight(20));
		llParams.addRule(BELOW, 20001);
		ll_channel.setLayoutParams(llParams);
		ll_channel.setPadding(0, 0, changeWidth(10), 0);
		ll_channel.setOrientation(LinearLayout.VERTICAL);
		channelItems = new ChannelItemView[ITEM_VIEW_SIZE];
		android.widget.LinearLayout.LayoutParams param_item = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0,1);
		param_item.setMargins(changeWidth(35),0, 0, 0);
		for (int i = 0; i < ITEM_VIEW_SIZE; i++) {
			channelItems[i] = new ChannelItemView(context);
			channelItems[i].setGravity(Gravity.CENTER_VERTICAL);
			//channelItems[i].setBackgroundColor(0xaacc0000+i*2560);
			channelItems[i].setLayoutParams(param_item);
			ll_channel.addView(channelItems[i]);
			//channelItems[i].setData("CCTV-10","探索发现");
		}
		parent.addView(ll_channel);
	}
	
	public void setSelected(int index,boolean isFocus){
		channelItems[index].setSelected(isFocus);
		channelItems[index].setMarquee(isFocus);
	}
	
	public void setData(List<ChannelItem> channelItems,long timeMsec){
		channelItemList = channelItems;
		//programThreadState = false;
		this.timeMsec = timeMsec;
		
	}
	
	public void setCurrentPlayingChannelIndex(int selectIndex,long timeMsec,boolean isSortChange,AssortItem assortItem){
		setCurrentPlayingChannelIndex(selectIndex, timeMsec, isSortChange, assortItem, false);
	}
	
	public void setCurrentPlayingChannelIndex(int selectIndex,long timeMsec,boolean isSortChange,AssortItem assortItem,boolean isFocus){
		//clearAllSelected();
		txtTitle.setText(assortItem.getAssortName());
		if(channelItemList==null||channelItemList.size()==0){
			ACTUAL_VIEW_SIZE = -1;
			ll_channel.removeAllViews();
			TextView txtTip = new TextView(ctx);
			LinearLayout.LayoutParams param_item = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
			txtTip.setText("暂无节目单");
			txtTip.setTextColor(getColor(R.color.light_white));
			txtTip.setTextSize(changeTextSize(FontSize.NO_CHANNEL));
			txtTip.setLayoutParams(param_item);
			txtTip.setGravity(Gravity.CENTER_VERTICAL);
			ll_channel.addView(txtTip);
			isLoaded = true;
			return;
		}
		this.currentChannelIndex = selectIndex;
		channelListSize = channelItemList.size();
		currentChannelViewIndex = 0;
		this.timeMsec = timeMsec;
		
		setChannelItem(isSortChange,isFocus);
	}
	
	/**
	 * 刷新正在播放的Item显示状态
	 */
	private void setChannelItem(boolean isSortChange,boolean isFocus) {
		android.widget.LinearLayout.LayoutParams param_item;
		if (channelListSize <= ITEM_VIEW_SIZE) {
			if(channelListSize<ITEM_VIEW_SIZE||ACTUAL_VIEW_SIZE<ITEM_VIEW_SIZE){
				ACTUAL_VIEW_SIZE = channelListSize;
				ll_channel.removeAllViews();
				channelItems = new ChannelItemView[ACTUAL_VIEW_SIZE];
				param_item  = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,ItemHeight);
				param_item.setMargins(changeWidth(30),0, 0, 0);
				for (int i = 0; i < ACTUAL_VIEW_SIZE; i++) {
					channelItems[i] = new ChannelItemView(ctx);
					channelItems[i].setGravity(Gravity.CENTER_VERTICAL);
					channelItems[i].setLayoutParams(param_item);
					ll_channel.addView(channelItems[i]);
				}
			}
			//startThread();
			//channelItemArray.clear();
			ArrayList<ChannelItem> channelItemArray = new ArrayList<ChannelItem>(ITEM_VIEW_SIZE);
			for (int i = 0; i < ACTUAL_VIEW_SIZE; i++){
				/*ChannelItem channelItem = channelItemList.get(i);
				setContent(channelItem, channelItems[i]);*/
				ChannelItem channelItem = channelItemList.get(i);
				setData(channelItem, channelItems[i]);
				channelItemArray.add(channelItem);
			}
			setContent(channelItemArray);
			currentChannelViewIndex = currentChannelIndex;
		}else{
			if(ACTUAL_VIEW_SIZE<ITEM_VIEW_SIZE){
				ACTUAL_VIEW_SIZE = ITEM_VIEW_SIZE;
				ll_channel.removeAllViews();
				channelItems = new ChannelItemView[ACTUAL_VIEW_SIZE];
				param_item  = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0,1);
				param_item.setMargins(changeWidth(30),0, 0, 0);
				for (int i = 0; i < ACTUAL_VIEW_SIZE; i++) {
					channelItems[i] = new ChannelItemView(ctx);
					channelItems[i].setGravity(Gravity.CENTER_VERTICAL);
					channelItems[i].setLayoutParams(param_item);
					ll_channel.addView(channelItems[i]);
				}
			}
			
			if(currentChannelIndex<=ACTUAL_VIEW_SIZE/2){
				//channelItemArray.clear();
				ArrayList<ChannelItem> channelItemArray = new ArrayList<ChannelItem>(ITEM_VIEW_SIZE);
				for (int i = 0; i < ACTUAL_VIEW_SIZE; i++){
				/*	ChannelItem channelItem = channelItemList.get(i);
					setContent(channelItem, channelItems[i]);*/
					//channelItemArray[i] = channelItemList.get(i);
					
					ChannelItem channelItem = channelItemList.get(i);
					setData(channelItem, channelItems[i]);
					channelItemArray.add(channelItem);
				}
				setContent(channelItemArray);
				currentChannelViewIndex = currentChannelIndex;
			}else if(currentChannelIndex>ACTUAL_VIEW_SIZE/2 && currentChannelIndex<channelListSize-ACTUAL_VIEW_SIZE/2){
				//channelItemArray.clear();
				ArrayList<ChannelItem> channelItemArray = new ArrayList<ChannelItem>(ITEM_VIEW_SIZE);
				for (int i=currentChannelIndex-ACTUAL_VIEW_SIZE/2,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
					/*ChannelItem channelItem = channelItemList.get(i);
					setContent(channelItem, channelItems[j]);*/
					//channelItemArray[i] = channelItemList.get(i);
					ChannelItem channelItem = channelItemList.get(i);
					setData(channelItem, channelItems[j]);
					channelItemArray.add(channelItem);
				}
				setContent(channelItemArray);
				currentChannelViewIndex = ACTUAL_VIEW_SIZE/2;
			}else{
				//channelItemArray.clear();
				ArrayList<ChannelItem> channelItemArray = new ArrayList<ChannelItem>(ITEM_VIEW_SIZE);
				for(int i = channelListSize-ACTUAL_VIEW_SIZE,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
					/*ChannelItem channelItem = channelItemList.get(i);
					setContent(channelItem, channelItems[j]);*/
					//channelItemArray[i] = channelItemList.get(i);
					ChannelItem channelItem = channelItemList.get(i);
					setData(channelItem, channelItems[j]);
					channelItemArray.add(channelItem);
				}
				setContent(channelItemArray);
				currentChannelViewIndex = ACTUAL_VIEW_SIZE + currentChannelIndex - channelListSize;
			}
		}
		
		for(int i=0;i<ACTUAL_VIEW_SIZE;i++){
			if(!isFocus){
				channelItems[i].setPlaying(false);
			}else{
				channelItems[i].focusEvent(true);
			}
		}
		if(!isFocus && !isSortChange && channelItems.length>0&&currentChannelViewIndex!=-1){
			try {
				channelItems[currentChannelViewIndex].setPlaying(true);
			} catch (Exception e) {
				LogUtil.e("ChannelListView-->setChannelItem-->"+e.toString());
			}
		}
		isLoaded = true;
		Log.i("load","setChannelItem channel isLoaded::"+ isLoaded);
	}
	
	private ProgramItem getCurrentContent(ChannelItem channelItem,long time){
		List<ProgramItem> programItems = channelItem.getDateList().get(0).getProgramItemList();
		int index = findCurrentPlayingProgramIndex(channelItem, time);
		if(index>=0){
			return programItems.get(index);
		}else{
			return null;
		}
	}
	
	private int findCurrentPlayingProgramIndex(ChannelItem channel,long timeMsec) {
		
		List<ProgramItem> programItems = channel.getDateList().get(0).getProgramItemList();
		if (programItems != null && programItems.size() > 0) {
			for (int i = 1; i < programItems.size(); i++) {
				//String time = programItems.get(i).getStartTime();
				//long msec = DateUtil.string2Msec(time, "yyyy-MM-dd kk:mm:ss");
				long msec = programItems.get(i).getlStartTime();
				if (timeMsec < msec) {
					return i - 1;
				}
			}
			return programItems.size() - 1;
		} else {
			return -1;
		}
		
	}
	
	/*public void setSeFocus(SetFocus setFocus) {
		this.setFocus = setFocus;
	}*/

	/*public interface SetFocus{
		void getLocationOnScreen(int[] positions);
		void clearSelected();
	}*/
	
	public void setPreChannel(){
		LogUtil.i("setPreChannel--->currentChannelViewIndex-->"+currentChannelViewIndex);
		if (currentChannelIndex > 0) {
			currentChannelIndex--;
			if(channelListSize<=ACTUAL_VIEW_SIZE){
				if (currentChannelViewIndex > 0) {
					setSelected(currentChannelViewIndex, false);
					setSelected(--currentChannelViewIndex, true);
				}
			}else{
				if(currentChannelIndex<ACTUAL_VIEW_SIZE/2){
					if (currentChannelViewIndex > 0) {
						setSelected(currentChannelViewIndex, false);
						setSelected(--currentChannelViewIndex, true);
					}
				}else if(currentChannelIndex>=ACTUAL_VIEW_SIZE/2 && currentChannelIndex<channelListSize-ACTUAL_VIEW_SIZE/2-1){
					//startThread();
					//channelItemArray.clear();
					ArrayList<ChannelItem> channelItemArray = new ArrayList<ChannelItem>(ITEM_VIEW_SIZE);
					for (int i=currentChannelIndex-ACTUAL_VIEW_SIZE/2,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
						/*ChannelItem channelItem = channelItemList.get(i);
						setContent(channelItem, channelItems[j]);*/
						ChannelItem channelItem = channelItemList.get(i);
						setData(channelItem, channelItems[j]);
						channelItems[j].setSelected(false);
						channelItemArray.add(channelItem);
					}
					setContent(channelItemArray);
					setSelected(ACTUAL_VIEW_SIZE/2, true);
				}else{
					if (currentChannelViewIndex > 0) {
						setSelected(currentChannelViewIndex, false);
						setSelected(--currentChannelViewIndex, true);
					}
				}
			}
			 
		} 
	}
	
	public void setNextChannel(){
		LogUtil.i("setPreChannel--->currentChannelViewIndex-->"+currentChannelViewIndex);
		if (currentChannelIndex < channelListSize-1) {
			currentChannelIndex++;
			if(channelListSize <= ACTUAL_VIEW_SIZE){
				if (currentChannelViewIndex < ACTUAL_VIEW_SIZE-1) {
					setSelected(currentChannelViewIndex, false);
					setSelected(++currentChannelViewIndex, true);
				} 
			}else {
				if(currentChannelIndex<=ACTUAL_VIEW_SIZE/2){
					if (currentChannelViewIndex < ACTUAL_VIEW_SIZE-1) {
						setSelected(currentChannelViewIndex, false);
						setSelected(++currentChannelViewIndex, true);
					}
				}else if(currentChannelIndex>ACTUAL_VIEW_SIZE/2 && currentChannelIndex<channelListSize-ACTUAL_VIEW_SIZE/2){
					//startThread();
					//channelItemArray.clear();
					ArrayList<ChannelItem> channelItemArray = new ArrayList<ChannelItem>(ITEM_VIEW_SIZE);
					for (int i=currentChannelIndex-ACTUAL_VIEW_SIZE/2,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
						/*ChannelItem channelItem = channelItemList.get(i);
						setContent(channelItem, channelItems[j]);*/
						ChannelItem channelItem = channelItemList.get(i);
						setData(channelItem, channelItems[j]);
						channelItems[j].setSelected(false);
						channelItemArray.add(channelItem);
					}
					setContent(channelItemArray);
					setSelected(ACTUAL_VIEW_SIZE/2, true);
				}else{
					if (currentChannelViewIndex < ACTUAL_VIEW_SIZE-1) {
						setSelected(currentChannelViewIndex, false);
						setSelected(++currentChannelViewIndex, true);
					}
				}
			}
		} 
	}
	
	/*private void startThread(){
		isProgramOnKeyUp = true;
		isProgramThreadLive = true;
		if(!programThreadState){
			programThreadState = true;
			LogUtil.i("ChannelListView--->启动Lazy线程");
			LazyProgramThread lazyProgramThread = new LazyProgramThread();
			lazyProgramThread.start();
		}
	}*/
	
	public boolean isBegin(int size){
		if(size == 0){
			return currentChannelIndex<=0;
		}
		return false;
	}
	
	public boolean isEnd(int size){
		LogUtil.i("isEnd::"+(currentChannelIndex>=channelListSize)+",cur="+currentChannelIndex+",size="+channelListSize);
		if(size==channelListSize-1){
			return currentChannelIndex>=channelListSize-1;
		}
		return false;
	}
	
	/*private void clearSelected(){
		if(setFocus!=null){
			setFocus.clearSelected();
		}
	}*/

	public void setPlayState(int playState) {
		this.playState = playState;
	}

	public void setPlayingChannelId(String playingChannelId) {
		this.playingChannelId = playingChannelId;
	}
	
	private void setContent1(ChannelItem channelItem,ChannelItemView view){
		/*ProgramItem curProgram = getCurrentContent(channelItem, timeMsec);
		String content = ctx.getResources().getString(R.string.none_program);
		if(playState == PlayerView.BACK_STATE&&playingChannelId.equals(channelItem.getChannelId())){
			content = "节目回看";
		}else{
			if(curProgram!=null&&!TextUtils.isEmpty(curProgram.getProgramName())){
				content = curProgram.getProgramName();
			}
		}
		
		view.setData(channelItem, content);*/
		view.setData(channelItem);
		LazyAsyncTask task = new LazyAsyncTask(channelItem, view);
		task.execute();
	}
	
	private void setData(ChannelItem channelItem,ChannelItemView view){
		view.setData(channelItem);
		//view.setContent("");
	}
	
	private void setContent(List<ChannelItem> arrays){
		/*ProgramItem curProgram = getCurrentContent(channelItem, timeMsec);
		String content = ctx.getResources().getString(R.string.none_program);
		if(playState == PlayerView.BACK_STATE&&playingChannelId.equals(channelItem.getChannelId())){
			content = "节目回看";
		}else{
			if(curProgram!=null&&!TextUtils.isEmpty(curProgram.getProgramName())){
				content = curProgram.getProgramName();
			}
		}
		
		view.setData(channelItem, content);*/
		/*for (int i = 0; i < ACTUAL_VIEW_SIZE; i++){
			channelItems[i].setData(array[i]);
		}*/
		NewLazyAsyncTask task = new NewLazyAsyncTask(arrays);
		task.execute();
		
		/*isProgramOnKeyUp = true;
		isProgramThreadLive = true;
		if(!programThreadState){
			programThreadState = true;
			LogUtil.i("ChannelListView--->启动Lazy线程");
			LazyProgramThread lazyProgramThread = new LazyProgramThread();
			lazyProgramThread.start();
		}*/
	}
	
	class NewLazyAsyncTask extends AsyncTask<Integer, String, List<String>>{
		private List<ChannelItem> items;
		
		public NewLazyAsyncTask(List<ChannelItem> items) {
			super();
			this.items = items;
		}

		@Override
		protected List<String> doInBackground(Integer... params) {
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
			List<String> contents = new ArrayList<String>(ITEM_VIEW_SIZE);
			for(ChannelItem item:items){
				ProgramItem curProgram = getCurrentContent(item, timeMsec);
				String content = ctx.getResources().getString(R.string.none_program);
				if(playState == ViewConst.BACK_STATE&&playingChannelId.equals(item.getChannelId())){
					content = "节目回看";
				}else{
					if(curProgram!=null&&!TextUtils.isEmpty(curProgram.getProgramName())){
						content = curProgram.getProgramName();
					}
				}
				contents.add(content);
			}
			

			return contents;
		}

		@Override
		protected void onPostExecute(List<String> results) {
			if(results!=null&&results.size()>=ACTUAL_VIEW_SIZE){
				for (int i = 0; i < ACTUAL_VIEW_SIZE; i++){
					channelItems[i].setContent(results.get(i));
				}
			}
			super.onPostExecute(results);
		}
		
	}
	
	/*private void setContentLazy(ChannelItem channelItem,ChannelItemView view){
		view.setData(channelItem);
	}*/
	
/*	private class LazyProgramThread extends Thread{

		public void run() {
			//LogUtil.i("ChannelListView--->开始线程");
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
			flag:
			while(isProgramThreadLive){
				//LogUtil.i("ChannelListView--->"+Thread.currentThread().getName());
				while(!isProgramOnKeyUp){
					//LogUtil.i("ChannelListView--->while(!isProgramOnKeyUp)");
					try {
						Thread.sleep(10);
						lastProgramWaitTime +=10;
						if(lastProgramWaitTime>=5000){
							lastProgramWaitTime = 0;
							isProgramThreadLive = false;
							break flag;
						}
					} catch (InterruptedException e) {
						LogUtil.e("ChannelListView-->while(!isProgramOnKeyUp)::"+e.toString());
					}
				}
				while(lastProgramWaitTime<500){
					//LogUtil.i("ChannelListView--->while(lastProgramWaitTime<500)");
					if(isProgramOnKeyUp){
						isProgramOnKeyUp = false;
						lastProgramWaitTime=0;
					}
					try {
						Thread.sleep(10);
						lastProgramWaitTime+=10;
					} catch (InterruptedException e) {
						LogUtil.e("ChannelListView-->while(lastProgramWaitTime<500)::"+e.toString());
					}
				}
				lastProgramWaitTime = 0;
				isProgramOnKeyUp = false;
				
				
				List<String> contents = new ArrayList<String>(ITEM_VIEW_SIZE);
				for(ChannelItem item:channelItemArray){
					ProgramItem curProgram = getCurrentContent(item, timeMsec);
					String content = ctx.getResources().getString(R.string.none_program);
					if(playState == PlayerView.BACK_STATE&&playingChannelId.equals(item.getChannelId())){
						content = "节目回看";
					}else{
						if(curProgram!=null&&!TextUtils.isEmpty(curProgram.getProgramName())){
							content = curProgram.getProgramName();
						}
					}
					contents.add(content);
				}
				Message msg = Message.obtain();
				msg.what = UPDATE_CHANNEL;
				msg.obj = contents;
				handler.sendMessage(msg);
			}
			programThreadState = false;
			//LogUtil.i("ChannelListView--->结束线程");
		};
		
	};*/
	
	class LazyAsyncTask extends AsyncTask<Integer, String, String>{
		private ChannelItem channelItem;
		private ChannelItemView view;
		
		public LazyAsyncTask(ChannelItem channelItem, ChannelItemView view) {
			super();
			this.channelItem = channelItem;
			this.view = view;
		}

		@Override
		protected String doInBackground(Integer... params) {
			ProgramItem curProgram = getCurrentContent(channelItem, timeMsec);
			String content = ctx.getResources().getString(R.string.none_program);
			if(playState == ViewConst.BACK_STATE&&playingChannelId.equals(channelItem.getChannelId())){
				content = "节目回看";
			}else{
				if(curProgram!=null&&!TextUtils.isEmpty(curProgram.getProgramName())){
					content = curProgram.getProgramName();
				}
			}

			return content;
		}

		@Override
		protected void onPostExecute(String result) {
			if(view!=null){
				view.setContent(result);
			}
			super.onPostExecute(result);
		}
		
	}

	@Override
	public void focusEvent(boolean isFocus) {
		if(isFocus){
			txtTitle.setTextColor(getColor(R.color.dark_text));
		}else{
			txtTitle.setTextColor(getColor(R.color.deep_text));
		}
		for(int i = 0;i<ACTUAL_VIEW_SIZE;i++){
			if(!isBelongSelected(i)){
				channelItems[i].focusEvent(isFocus);
			}else{
				channelItems[i].changeIcon(isFocus);
				channelItems[i].setPlaying(true);
			}
		}
	}

/*	public void setTempChannelIndex(ChannelItem tempChannel) {
		this.tempChannel = tempChannel;
	}*/
	
	private boolean isBelongSelected(int index){
		boolean isSelected = false;
		if(currentChannelViewIndex == index)
		{
			isSelected = true;
		}
		return isSelected;
	}

	@Override
	public void clearAllSelected() {
		if(channelItems!=null){
			for(ChannelItemView view:channelItems){
				view.setBackgroundResource(0);
			}
		}
		
	}

	public boolean isLoaded() {
		Log.i("load","isLoaded() channel isLoaded::"+ isLoaded);
		return isLoaded;
	}

	public void setLoaded(boolean isLoaded) {
		Log.i("load","setLoaded channel isLoaded::"+ isLoaded);
		this.isLoaded = isLoaded;
	}
	
	
	public void setDivde(boolean state){
		clearView();
		if(state){
			leftView.setVisibility(VISIBLE);
			txtTitle.setPadding(changeWidth(80),changeWidth(10), 0, 0);
			llParams.setMargins(changeWidth(30), changeHeight(20), 0, changeHeight(10));
		}else{
			leftDivideView.setVisibility(VISIBLE);
			rightView.setVisibility(VISIBLE);
			txtTitle.setPadding(changeWidth(50),changeWidth(10), 0, 0);
			llParams.setMargins(0, changeHeight(20), 0, changeHeight(10));
		}
	}
	
	private void clearView(){
		leftDivideView.setVisibility(GONE);
		leftView.setVisibility(GONE);
		rightView.setVisibility(GONE);
	}
	
	public interface UpdateContent{
		void updateContextInfo(ChannelItemView view,ChannelItem item);
	}

}
