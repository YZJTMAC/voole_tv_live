package com.gntv.tv.view;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gntv.tv.R;
import com.gntv.tv.common.utils.DateUtil;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.channel.ChannelItem;
import com.gntv.tv.model.channel.ProgramItem;
import com.gntv.tv.view.base.BaseChannelRelativeLayout;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.FontSize;

public class ProgramView extends BaseChannelRelativeLayout {
	private TextView txtTitle = null;
	private LinearLayout ll_program = null;
	private static final int ITEM_VIEW_SIZE = 7;
	private int ACTUAL_VIEW_SIZE = ITEM_VIEW_SIZE;
	private ProgramItemView[] programItems = null;
	private List<ProgramItem> programItemList = null;
	public int currentProgramIndex = 0;
	public int currentProgramViewIndex = 0;
	private long timeMsec = 0;
	private int programListSize = 0;
	private ChannelItem curChannelItem = null;
	private ChannelItem tempChannelItem = null;
	private int tempProgramIndex = 0;
	private int ItemHeight = 0;
	private int screenHeight;
	private int width = 0;
	private volatile boolean isLoaded = false;
	private long playTime = 0;
	private LayoutParams llParams;
	private ImageView right_arrow_view = null;
	private ImageView left_arrow_view = null;
	private ImageView right_divide_view = null;
	private ImageView left_divide_view = null;

	public ProgramView(Context context) {
		super(context);
		init(context);
	}

	@Override
	protected void init(Context context) {
		setFocusable(true);
		setFocusableInTouchMode(true);
		width  = DisplayManager.GetInstance().getScreenWidth() *1/2;
		screenHeight = DisplayManager.GetInstance().getScreenHeight();
		//this.setBackgroundResource(R.drawable.background_right);
		initLeft(context);
		initTitle(context,this);
		initProgram(context,this);
		initRight(context);
	}
	
	private void initLeft(Context context){
		left_divide_view = new ImageView(context);
		left_divide_view.setBackgroundResource(R.drawable.cs_right_divide);
		left_divide_view.setVisibility(VISIBLE);
		LayoutParams div_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		left_divide_view.setLayoutParams(div_params);
		addView(left_divide_view);
		
		left_arrow_view = new ImageView(context);
		left_arrow_view.setBackgroundResource(R.drawable.cs_left_arrow);
		left_arrow_view.setVisibility(GONE);
		LayoutParams arrow_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		arrow_params.setMargins(changeWidth(20),screenHeight/2+changeHeight(10), 0, 0);
		left_arrow_view.setLayoutParams(arrow_params);
		addView(left_arrow_view);
	}
	
	private void initRight(Context context){
		right_divide_view = new ImageView(context);
		right_divide_view.setBackgroundResource(R.drawable.cs_left_divide);
		right_divide_view.setVisibility(VISIBLE);
		LayoutParams div_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		div_params.addRule(ALIGN_PARENT_RIGHT);
		right_divide_view.setLayoutParams(div_params);
		addView(right_divide_view);
		
		right_arrow_view = new ImageView(context);
		right_arrow_view.setBackgroundResource(R.drawable.cs_right_arrow);
		right_arrow_view.setVisibility(GONE);
		LayoutParams arrow_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		arrow_params.addRule(RIGHT_OF,30003);
		arrow_params.setMargins(0,screenHeight/2+changeHeight(10), 0, 0);
		right_arrow_view.setLayoutParams(arrow_params);
		addView(right_arrow_view);
	}
	
	private void initTitle(Context context,ViewGroup parent){
		txtTitle = new TextView(context);
		txtTitle.setId(30001);
		txtTitle.setGravity(Gravity.CENTER_VERTICAL);
		txtTitle.setTextSize(changeTextSize(FontSize.CHANNEL_TITLE));
		txtTitle.setTextColor(getColor(R.color.white));
		LayoutParams txtParams = new LayoutParams(LayoutParams.MATCH_PARENT,changeHeight(60));
		//txtParams.setMargins(changeWidth(1), 0, 0, 0);
		txtTitle.setPadding(changeWidth(50),changeHeight(10), 0, 0);
		txtTitle.setLayoutParams(txtParams);
		parent.addView(txtTitle);
	}
	
	private void initProgram(Context context,ViewGroup parent){
		ll_program = new LinearLayout(context);
		ll_program.setId(30003);
		ItemHeight = (screenHeight-changeHeight(120))/ITEM_VIEW_SIZE;
		llParams = new LayoutParams(width*3/7+changeWidth(40),LayoutParams.MATCH_PARENT);
		llParams.setMargins(0, changeHeight(20), 0, changeHeight(20));
		llParams.addRule(BELOW, 30001);
		ll_program.setLayoutParams(llParams);
		ll_program.setOrientation(LinearLayout.VERTICAL);
		programItems = new ProgramItemView[ITEM_VIEW_SIZE];
		ll_program.setPadding(0, 0, changeWidth(10), 0);
		android.widget.LinearLayout.LayoutParams param_item = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0,1);
		param_item.setMargins(changeWidth(35), 0, 0, 0);
		for (int i = 0; i < ITEM_VIEW_SIZE; i++) {
			programItems[i] = new ProgramItemView(context);
			programItems[i].setLayoutParams(param_item);
			programItems[i].setGravity(Gravity.CENTER_VERTICAL);
			ll_program.addView(programItems[i]);
		}
		parent.addView(ll_program);
	}
	
	public void setSelected(int index,boolean isFocus){
		programItems[index].setSelected(isFocus);
		programItems[index].setMarquee(isFocus);
	}
	
	public void setData(List<ProgramItem> data,ChannelItem channelItem){
		programItemList = data;
		curChannelItem = channelItem;
	}
	
	public void setCurrentProgramIndex(int index,long timeMsec,boolean isTitleChange,String title){
		setCurrentProgramIndex(index, timeMsec, isTitleChange, title, false);
	}
	
	public void setCurrentProgramIndex(int index,long timeMsec,boolean isTitleChange,String title,boolean isFocus){
		currentProgramIndex = index;
		txtTitle.setText(title);
		//clearAllSelected();
		if(programItemList==null||programItemList.size()==0||index==-1){
			ACTUAL_VIEW_SIZE = -1;
			ll_program.removeAllViews();
			TextView txtTip = new TextView(ctx);
			LinearLayout.LayoutParams param_item = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
			param_item.setMargins(changeWidth(30),0, 0, 0);
			txtTip.setText("暂无节目单");
			txtTip.setTextColor(getColor(R.color.light_white));
			txtTip.setTextSize(changeTextSize(FontSize.NO_CHANNEL));
			txtTip.setLayoutParams(param_item);
			txtTip.setGravity(Gravity.CENTER_VERTICAL);
			ll_program.addView(txtTip);
			isLoaded = true;
			return;
		}
			
		programListSize = programItemList.size();
		if (currentProgramIndex != -1) {
			if(currentProgramViewIndex>=ACTUAL_VIEW_SIZE){
				currentProgramViewIndex = ACTUAL_VIEW_SIZE/2;
			}
			programItems[currentProgramViewIndex].focusEvent(false);
		}
		currentProgramViewIndex = 0;
		this.timeMsec  = timeMsec;
		
		setProgramItem(isTitleChange,isFocus);
	}
	
	private void setProgramItem(boolean isTitleChange,boolean isFocus){
		android.widget.LinearLayout.LayoutParams param_item;
		if (programListSize <= ITEM_VIEW_SIZE) {
			if(programListSize < ITEM_VIEW_SIZE||ACTUAL_VIEW_SIZE<ITEM_VIEW_SIZE){
				ACTUAL_VIEW_SIZE = programListSize;
				ll_program.removeAllViews();
				programItems = new ProgramItemView[ACTUAL_VIEW_SIZE];
				param_item = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,ItemHeight);
				param_item.setMargins(changeWidth(30),0, 0, 0);
				for (int i = 0; i < ACTUAL_VIEW_SIZE; i++) {
					programItems[i] = new ProgramItemView(ctx);
					programItems[i].setGravity(Gravity.CENTER_VERTICAL);
					programItems[i].setLayoutParams(param_item);
					ll_program.addView(programItems[i]);
				}
			}
			
			for (int i = 0; i < ACTUAL_VIEW_SIZE; i++){
				ProgramItem programItem = programItemList.get(i);
				setProgramView(programItem, programItems[i]);
			}
			
			currentProgramViewIndex = currentProgramIndex;
		}else{
			if(ACTUAL_VIEW_SIZE<ITEM_VIEW_SIZE){
				ACTUAL_VIEW_SIZE = ITEM_VIEW_SIZE;
				ll_program.removeAllViews();
				programItems = new ProgramItemView[ACTUAL_VIEW_SIZE];
				param_item = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0,1);
				param_item.setMargins(changeWidth(30),0, 0, 0);
				for (int i = 0; i < ACTUAL_VIEW_SIZE; i++) {
					programItems[i] = new ProgramItemView(ctx);
					programItems[i].setGravity(Gravity.CENTER_VERTICAL);
					programItems[i].setLayoutParams(param_item);
					ll_program.addView(programItems[i]);
				}
			}
			if(currentProgramIndex<=ACTUAL_VIEW_SIZE/2){
				for (int i = 0; i < ACTUAL_VIEW_SIZE; i++){
					ProgramItem programItem = programItemList.get(i);
					setProgramView(programItem, programItems[i]);
				}
				currentProgramViewIndex = currentProgramIndex;
			}else if(currentProgramIndex > ACTUAL_VIEW_SIZE/2 && currentProgramIndex < programListSize-ACTUAL_VIEW_SIZE/2){
				for (int i = currentProgramIndex-ACTUAL_VIEW_SIZE/2,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
					ProgramItem programItem = programItemList.get(i);
					setProgramView(programItem, programItems[j]);
				}
				currentProgramViewIndex = ACTUAL_VIEW_SIZE/2;
			}else{
				for(int i = programListSize-ACTUAL_VIEW_SIZE,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
					ProgramItem programItem = programItemList.get(i);
					setProgramView(programItem, programItems[j]);
				}
				currentProgramViewIndex = ACTUAL_VIEW_SIZE + currentProgramIndex - programListSize;
			}
		}
		
		for(int i = 0;i<ACTUAL_VIEW_SIZE;i++){
			if(isFocus){
				programItems[i].focusEvent(isFocus);
			}else{
				programItems[i].setPlaying(false);
			}
		}
		if (isTitleChange) {
			if(currentProgramViewIndex>=ACTUAL_VIEW_SIZE){
				currentProgramViewIndex = ACTUAL_VIEW_SIZE/2;
			}
			programItems[currentProgramViewIndex].setPlaying(true);
		}
		isLoaded = true;
	}
	
	/*public void setSeFocus(SetFocus seFocus) {
		this.seFocus = seFocus;
	}*/

	/*public interface SetFocus{
		void getLocationOnScreen(int[] positions);
		void clearSelected();
	}*/
	
	public void setPreProgram(){
		LogUtil.i("setPreProgram--->currentProgramViewIndex-->"+currentProgramViewIndex);
		LogUtil.i("setPreProgram--->currentProgramIndex-->"+currentProgramIndex);
		if (currentProgramIndex > 0) {
			//clearAllSelected();
			currentProgramIndex--;
			if(programListSize<=ACTUAL_VIEW_SIZE){
				if (currentProgramViewIndex > 0) {
					setSelected(currentProgramViewIndex, false);
					setSelected(--currentProgramViewIndex, true);
				}
			}else{
				if(currentProgramIndex<ACTUAL_VIEW_SIZE/2){
					if (currentProgramViewIndex > 0) {
						setSelected(currentProgramViewIndex, false);
						setSelected(--currentProgramViewIndex, true);
					}
				}else if(currentProgramIndex>=ACTUAL_VIEW_SIZE/2 && currentProgramIndex<programListSize-ACTUAL_VIEW_SIZE/2-1){
					for (int i=currentProgramIndex-ACTUAL_VIEW_SIZE/2,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
						ProgramItem programItem = programItemList.get(i);
						setProgramView(programItem, programItems[j],true);
					}
					setSelected(ACTUAL_VIEW_SIZE/2, true);
				}else{
					if (currentProgramViewIndex > 0) {
						setSelected(currentProgramViewIndex, false);
						setSelected(--currentProgramViewIndex, true);
					}
				}
			}
			 
		} 
	}
	
	public void setNextProgram(){
		LogUtil.i("setNextProgram--->currentProgramViewIndex-->"+currentProgramViewIndex);
		LogUtil.i("setNextProgram--->currentProgramIndex-->"+currentProgramIndex);
		if (currentProgramIndex < programListSize-1) {
			//clearAllSelected();
			currentProgramIndex++;
			if(programListSize <= ACTUAL_VIEW_SIZE){
				if (currentProgramViewIndex < ACTUAL_VIEW_SIZE-1) {
					setSelected(currentProgramViewIndex, false);
					setSelected(++currentProgramViewIndex, true);
				} 
			}else {
				if(currentProgramIndex<=ACTUAL_VIEW_SIZE/2){
					if (currentProgramViewIndex < ACTUAL_VIEW_SIZE-1) {
						setSelected(currentProgramViewIndex, false);
						setSelected(++currentProgramViewIndex, true);
					}
				}else if(currentProgramIndex>ACTUAL_VIEW_SIZE/2 && currentProgramIndex<programListSize-ACTUAL_VIEW_SIZE/2){
					for (int i=currentProgramIndex-ACTUAL_VIEW_SIZE/2,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
						ProgramItem programItem = programItemList.get(i);
						setProgramView(programItem, programItems[j],true);
					}
					setSelected(ACTUAL_VIEW_SIZE/2, true);
				}else{
					if (currentProgramViewIndex < ACTUAL_VIEW_SIZE-1) {
						setSelected(currentProgramViewIndex, false);
						setSelected(++currentProgramViewIndex, true);
					}
				}
			}
		} 
	}
	
	/*private void clearSelected(){
		if(seFocus!=null){
			seFocus.clearSelected();
		}
	}*/
	
	private void setProgramView(ProgramItem programItem,ProgramItemView view,boolean isFocus){
		String content = programItem.getProgramName();
		if(TextUtils.isEmpty(content)){
			content = ctx.getResources().getString(R.string.none_program);
		}
		long startTime = programItem.getlStartTime();
		long endTime = programItem.getlStopTime();
		boolean state = false;
		boolean isShow = isPlayed(startTime, timeMsec);
		String date = null;
		if(isShow){
			date = getTime(startTime);
			state = isPlaying(startTime, endTime, timeMsec);
		}else{
			date = getTimeTip(startTime, timeMsec, endTime);
		}
		String backCode = curChannelItem.getBackEnable();
		boolean isSupport = false;
		if("1".equals(backCode)){
			isSupport = true;
		}
		view.setData(content, date, isShow,state,isSupport,programItem);
		if(!isFocus){
			view.focusEvent(false);
		}else{
			view.setSelected(false);
		}
		
	}
	
	private void setProgramView(ProgramItem programItem,ProgramItemView view){
		setProgramView(programItem,view,false);
	}
	
	private String getTime(long msec){
		//long msec = DateUtil.string2Msec(timeStr, "yyyy-MM-dd kk:mm:ss");
		return DateUtil.msec2String(msec, "kk:mm");
	}
	
	private String getTimeTip(long startTime,long msec,long endTime){
		//long sTime = DateUtil.string2Msec(startTime, "yyyy-MM-dd kk:mm:ss");
		int sub = (int) ((startTime-msec)/(1000*60));
		String tip = "";
		if(sub<=5){
			tip = "即将播放";
		}else if(sub<60){
			tip =  sub +"分钟后";
		}else{
			/*float ftime = (float) (sub/60.0);
			ftime = (float) (Math.floor(ftime*10)/10.0);*/
			tip = getTime(startTime);
		}
		return tip;
	}
	
	private boolean isPlaying(long stime,long etime,long timeMsec){
		boolean state = false;
		//long stime = DateUtil.string2Msec(startTime, "yyyy-MM-dd kk:mm:ss");
		//long etime = DateUtil.string2Msec(endTime, "yyyy-MM-dd kk:mm:ss");
		if(stime<timeMsec){
			if(etime>=timeMsec){
				state = true;
			}
		}
		return state;
	}
	
	private boolean isPlayed(long msec,long timeMsec) {
		//long msec = DateUtil.string2Msec(startTime, "yyyy-MM-dd kk:mm:ss");
		boolean isPlayed = false;
		if(msec<timeMsec){
			isPlayed = true;
		}
		return isPlayed;
		
	}

	@Override
	public void focusEvent(boolean isFocus) {
		if(isFocus){
			//txtTitle.setBackgroundResource(R.drawable.cs_bg_focus3);
			txtTitle.setTextColor(getColor(R.color.dark_text));
		}else{
			//txtTitle.setBackgroundResource(R.drawable.cs_bg_unfocus3);
			txtTitle.setTextColor(getColor(R.color.deep_text));
		}
		for(int i = 0;i<ACTUAL_VIEW_SIZE;i++){
			if(!isBelongSelected(i)){
				programItems[i].focusEvent(isFocus);
			}else{
				programItems[i].changeIcon(isFocus);
				programItems[i].setPlaying(true);
			}
		}
	}


	public void setTempProgramIndex(ChannelItem tempChannelItem,int tempProgramIndex,long playTime) {
		this.tempChannelItem = tempChannelItem;
		this.tempProgramIndex = tempProgramIndex;
		this.playTime = playTime;
	}
	
	private boolean isPlayingProgram(ProgramItemView view){
		boolean isPlaying = false;
		if(curChannelItem!=null&&tempChannelItem!=null&&curChannelItem.getChannelId().equals(tempChannelItem.getChannelId())){
			try {
				ProgramItem cItem = view.getProgramItem();
				LogUtil.i("ProgramView--->isPlayingProgram--->startTime::"+cItem.getlStartTime()+",playTime::"+playTime);
				long subTime = cItem.getlStartTime() - playTime;
				//24*60*60*1000
				if(cItem!=null&&subTime>0&&subTime<=86400000){
					ProgramItem pItem = programItemList.get(tempProgramIndex);
					if(cItem == pItem){
						isPlaying = true;
					}
				}
			} catch (Exception e) {
				LogUtil.e("ProgramView-->isPlayingProgram-->"+e.toString());
			}
		}
		return isPlaying;
	}

	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	public boolean isLoaded() {
		return isLoaded;
	}
	
	public boolean isBegin(int size){
		if(size == 0){
			return currentProgramIndex<=0;
		}
		return false;
	}
	
	public boolean isEnd(int size){
		if(size==programListSize-1){
			return currentProgramIndex>=programListSize-1;
		}
		return false;
	}
	
	private boolean isBelongSelected(int index){
		boolean isSelected = false;
		if(currentProgramViewIndex == index)
		{
			isSelected = true;
		}
		return isSelected;
	}

	@Override
	public void clearAllSelected() {
		if(programItems!=null){
			for(ProgramItemView view:programItems){
				view.setBackgroundResource(0);
			}
		}
	}
	
	public void setDivde(boolean state){
		clearView();
		if(state){
			right_divide_view.setVisibility(VISIBLE);
			left_arrow_view.setVisibility(VISIBLE);
			txtTitle.setPadding(changeWidth(80), changeWidth(10), 0, 0);
			llParams.setMargins(changeWidth(30), changeHeight(20), 0, changeHeight(10));
		}else{
			left_divide_view.setVisibility(VISIBLE);
			right_arrow_view.setVisibility(VISIBLE);
			txtTitle.setPadding(changeWidth(50), changeWidth(10), 0, 0);
			llParams.setMargins(0, changeHeight(20), 0, changeHeight(10));
		}
	}
	
	private void clearView(){
		left_divide_view.setVisibility(GONE);
		left_arrow_view.setVisibility(GONE);
		right_divide_view.setVisibility(GONE);
		right_arrow_view.setVisibility(GONE);
	}
}
