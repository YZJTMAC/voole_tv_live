package com.gntv.tv.view;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gntv.tv.R;
import com.gntv.tv.common.utils.DateUtil;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.channel.AssortItem;
import com.gntv.tv.model.channel.ChannelItem;
import com.gntv.tv.model.channel.ChannelProgramInfo;
import com.gntv.tv.model.channel.DateItem;
import com.gntv.tv.model.channel.ProgramItem;
import com.gntv.tv.model.channel.ProgramManager;
import com.gntv.tv.model.time.TimeManager;
import com.gntv.tv.report.PageActionReport;
import com.gntv.tv.report.PageActionReport.Action;
import com.gntv.tv.report.PageActionReport.FocusType;
import com.gntv.tv.report.PageActionReport.ModuleType;
import com.gntv.tv.report.PageActionReport.PageType;
import com.gntv.tv.view.AnimUtil.AnimEndEvent;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.TVToast;
import com.gntv.tv.view.base.TimerRelativeLayout;
import com.gntv.tv.view.base.ViewConst;

public class ChannelView extends TimerRelativeLayout {
	
	private static final int SORT_VIEW_TYPE = 1000; 
	private static final int CHANNEL_VIEW_TYPE = 2000; 
	private static final int PROGRAM_VIEW_TYPE = 3000;
	private static final int DATE_VIEW_TYPE = 4000;
	private static final int DREC_LEFT = 0;
	private static final int DREC_RIGHT = 1;
	
	private static final int SHOW_PROGRAMVIEW_MSG = 10;
	private static final int UPDATE_PROGRAMVIEW_MSG = 20;
	private static final int SHOW_CHANNELVIEW_MSG1 = 30;
	private static final int SHOW_CHANNELVIEW_MSG2 = 40;
	private static final int GOTO_PLAYING_CHANNEL = 50;
	private static final int NUM_CHANGE_CHANNEL = 60;
	private static final int SHOW_NULLPROGRAMVIEW_MSG = 70;
	private static final int INIT_KT_INFO = 80;
	private static final int LOAD_CHANNEL = 90;
	private static final int LOAD_PROGRAM = 100;
	private static final int ANIM_TIME = 300;
	private IChannelViewListener iChannelViewListener = null;
	private PlayerView playerView = null;
	private int width = 0;
	private int height = 0;

	private SortView sortView;
	private ChannelListView channelListView;
	private ProgramView programView;
	private DateView dateView;
	private int currentViewType = SORT_VIEW_TYPE;
	public int playingSortIndex = 0;
	public int playingChannelIndex = 0;
	public int playingProgramIndex = 0;
	public int playingDateIndex = 0;
	private int tempSortIndex = 0;
	private int tempChannelIndex = 0;
	private int tempProgramIndex =0;
	private ChannelProgramInfo channelProgramInfo = null;
	
	private List<AssortItem> assortItems = null;
	private List<ChannelItem> curChannelItems = null;
	//private List<ProgramItem> curProgramItems = null;
	private ProgramMap programMap = new ProgramMap();
	private ProgramItem playingProgramItem = null;
	private static long timeMsec = 0;
	private long playDate = 0;
	public int playState = ViewConst.LIVE_STATE;
	private Integer gotoChannelCount =  0;
	
	private static final int STYLE1 = 0;
	private int currentType = STYLE1;
	//private long currentTimes = 0;
	private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	private CvHandler cvHandler = null;
	private int lastChannelSize = 0;
	private int lastProgramSize = 0;
	private int sort_width = 0;
	private int channel_width = 0;
	private int program_width = 0;
	private int date_width = 0;
	private final float back_width1 = 1.5f;
	private final float back_width2 = 1.7f;
	//private int channel_rwidth = 0;
	//private int program_rwidth = 0;
	
	private static class CvHandler extends Handler{
		SoftReference<ChannelView> sChannelView;
		public CvHandler(SoftReference<ChannelView> sChannelView) {
			super();
			this.sChannelView = sChannelView;
		}

		@Override
		public synchronized void handleMessage(Message msg) {
			final ChannelView cView = sChannelView.get();
			if(cView!=null){
				switch (msg.what) {
				case SHOW_PROGRAMVIEW_MSG:
					ChannelItem channel1 = cView.curChannelItems.get(cView.channelListView.currentChannelIndex);
					cView.programView.setData(cView.programMap.getProgramItems(),channel1);
					
					int curPlaying1 = cView.findCurrentPlayingProgramIndex(cView.programMap.getProgramItems(), cView.timeMsec);
					final Boolean isTitleChange = (Boolean) msg.obj;
					if(isTitleChange){
						curPlaying1 = cView.playingProgramIndex;
					}
					String title1 = cView.dateView.getCurData();
					cView.programView.setCurrentProgramIndex(curPlaying1,cView.timeMsec, isTitleChange,title1);

					if(cView.programMap.getProgramItems() == null || cView.programMap.getProgramItems().size() == 0||cView.programView.currentProgramIndex == -1){
						cView.currentViewType = CHANNEL_VIEW_TYPE; 
						cView.channelListView.focusEvent(true);
						post(new Runnable() {
							@Override
							public void run() {
								cView.clearSelected();
								cView.channelListView.setSelected(cView.channelListView.currentChannelViewIndex,true);
								LogUtil.i("moveFocusedToProgram---->AnimUtil.setAnim---->cView.channelListView.setSelected");
							}
						});
						
					}else{
						cView.currentViewType = PROGRAM_VIEW_TYPE; 
						cView.programView.focusEvent(true);
						post(new Runnable() {
							@Override
							public void run() {
								cView.clearSelected();
								cView.programView.setSelected(cView.programView.currentProgramViewIndex,true);
								LogUtil.i("moveFocusedToProgram---->AnimUtil.setAnim---->cView.programView.setSelected");
							}
						});
						
					}
					
					break;
				case UPDATE_PROGRAMVIEW_MSG:
					ChannelItem channel2 = cView.curChannelItems.get(cView.channelListView.currentChannelIndex);
					cView.programView.setData(cView.programMap.getProgramItems(),channel2);
					int curPlaying2 = cView.findCurrentPlayingProgramIndex(cView.programMap.getProgramItems(), cView.timeMsec);
					Boolean isChangeTitle = (Boolean) msg.obj;
					cView.dateView.setData(0,true);
					if(isChangeTitle){
						curPlaying2 = cView.playingProgramIndex;
						cView.dateView.setData(cView.playingDateIndex,true);
					}
					String title2 = cView.dateView.getCurData();
					cView.programView.setCurrentProgramIndex(curPlaying2,cView.timeMsec, isChangeTitle,title2);
					break;
				case SHOW_NULLPROGRAMVIEW_MSG:
					ChannelItem channel3 = cView.curChannelItems.get(cView.channelListView.currentChannelIndex);
					cView.programView.setData(cView.programMap.getProgramItems(),channel3);
					String title3 = cView.dateView.getCurData();
					cView.programView.setCurrentProgramIndex(cView.playingProgramIndex,cView.timeMsec, true,title3);
					cView.clearViews();
					cView.channelListView.setVisibility(VISIBLE);
					cView.programView.setVisibility(View.VISIBLE);
					cView.channelListView.setDivde(true);
					cView.programView.setDivde(false);
					//AnimUtil.setAnim(cView.channelListView, cView.sort_width,0,ANIM_TIME,null);
					AnimUtil.setAnim(cView.channelListView, cView.sort_width,0,cView.programView, cView.sort_width+cView.channel_width, cView.channel_width,ANIM_TIME,new AnimEndEvent() {
						@Override
						public void onAnimEnd() {
							cView.channelListView.setSelected(cView.channelListView.currentChannelViewIndex,true);
							LogUtil.i("SHOW_NULLPROGRAMVIEW_MSG---->AnimUtil.setAnim");
						}
					});
					cView.currentViewType = CHANNEL_VIEW_TYPE; 
					cView.channelListView.focusEvent(true);
					
					break;
				case SHOW_CHANNELVIEW_MSG1:
					String title4 = cView.dateView.getCurData();
					cView.programView.setCurrentProgramIndex(cView.playingProgramIndex, cView.timeMsec, true,title4);
					break;
				case SHOW_CHANNELVIEW_MSG2:
					String title5 = cView.dateView.getCurData();
					cView.programView.setCurrentProgramIndex(cView.playingProgramIndex,cView.timeMsec, true,title5,true);
					post(new Runnable(){
						@Override
						public void run() {
							cView.programView.setSelected(cView.programView.currentProgramViewIndex, true);
						}
						
					});
					break;
				case GOTO_PLAYING_CHANNEL:
					cView.curChannelItems = cView.assortItems.get(cView.playingSortIndex).getChannelList();
				case NUM_CHANGE_CHANNEL:
					cView.tempSortIndex = cView.playingSortIndex;
					cView.tempChannelIndex = cView.playingChannelIndex;
					cView.initToLive();
					cView.iChannelViewListener.onClearBackPlayInfo();
					cView.backToChannel();
					cView.iChannelViewListener.onChannelClick(cView.curChannelItems.get(cView.playingChannelIndex));
					cView.channelListView.setPlayState(cView.playState);
					cView.channelListView.setData(cView.curChannelItems,timeMsec);
					if(cView.getCurrentChannel()!=null){
						cView.channelListView.setPlayingChannelId(cView.getCurrentChannel().getChannelId());
					}
					
					break;
				case INIT_KT_INFO:
					cView.dateView.setData(cView.playingDateIndex,true);
					cView.playDate = cView.dateView.getCurrentDate();
					cView.setTempPlayingIndex();
					break;
				case LOAD_CHANNEL:
					cView.curChannelItems = cView.assortItems.get(cView.sortView.currentSortIndex).getChannelList();
					cView.channelListView.setData(cView.curChannelItems,timeMsec);
					if(cView.playingSortIndex == cView.sortView.currentSortIndex){
						cView.dateView.setData(cView.playingDateIndex,true);
						cView.channelListView.setCurrentPlayingChannelIndex(cView.playingChannelIndex, timeMsec, false,cView.assortItems.get(cView.sortView.currentSortIndex));
					}else{
						cView.dateView.setData(0,true);
						cView.channelListView.setCurrentPlayingChannelIndex(0, timeMsec, true,cView.assortItems.get(cView.sortView.currentSortIndex));
					}
					break;
				case LOAD_PROGRAM:
					cView.loadProgramView();
					break;
				default:
					break;
				}
			}

		};
	}
	
	public ChannelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ChannelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ChannelView(Context context,PlayerView parent) {
		super(context);
		playerView = parent;
		setWillNotDraw(false);  
		init(context);
	}
	
	LinearLayout backView = null;
	View title_back = null;
	private void init(Context context){
		setFocusable(true);
		setFocusableInTouchMode(true);
		setClipChildren(true);
		backView = new LinearLayout(context);
		backView.setOrientation(LinearLayout.VERTICAL);
		title_back = new View(context);
		title_back.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,changeHeight(60)));
		title_back.setBackgroundResource(R.drawable.title_background_right);
		backView.addView(title_back);
		backView.setBackgroundResource(R.drawable.background_right);
		width = DisplayManager.GetInstance().getScreenWidth() /2;
		height = DisplayManager.GetInstance().getScreenHeight();
		setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		backView.setLayoutParams(new RelativeLayout.LayoutParams((int) (width*back_width1), LayoutParams.MATCH_PARENT));
		addView(backView);
		cvHandler = new CvHandler(new SoftReference<ChannelView>(this));
		sort_width = width*2/7+changeWidth(45);
		channel_width = width*3/7+changeWidth(105);
		program_width = width*3/7+changeWidth(85);
		//channel_rwidth = width*4/5;
		//program_rwidth = width*4/5;
		date_width = width/4;
		initSort(context,this);
		initChannel(context,this);
		initProgram(context,this);
		initProgramDate(context,this);
	}
	
	
	private void initSort(Context context,ViewGroup parent){
		sortView = new SortView(context);
		LayoutParams params = new LayoutParams(sort_width,LayoutParams.MATCH_PARENT);
		sortView.setLayoutParams(params);
		sortView.setId(1001);
		parent.addView(sortView);
	}
	
	private void initChannel(Context context,ViewGroup parent){
		channelListView = new ChannelListView(context);
		channelListView.setId(1002);
		LayoutParams params = new LayoutParams(channel_width,LayoutParams.MATCH_PARENT);
		params.addRule(RIGHT_OF, 1001);
		channelListView.setLayoutParams(params);
		parent.addView(channelListView);
	}
	
	private void initProgram(Context context,ViewGroup parent){
		programView = new ProgramView(context);
		programView.setId(1003);
		LayoutParams params = new LayoutParams(program_width,LayoutParams.MATCH_PARENT);
		programView.setLayoutParams(params);
		programView.setVisibility(View.GONE);
		parent.addView(programView);
	}
	
	private void initProgramDate(Context context,ViewGroup parent){
		dateView = new DateView(context);
		dateView.setId(1004);
		LayoutParams params = new LayoutParams(date_width,LayoutParams.MATCH_PARENT);
		dateView.setLayoutParams(params);
		dateView.setVisibility(View.GONE);
		parent.addView(dateView);
	}

	@Override
	protected void timeOut() {
		if(iChannelViewListener!=null){
			iChannelViewListener.onTimeOut();
		}
	}
	
	public void hide(){
		setVisibility(View.GONE);
	}
	
	public void show(long msec){
		timeMsec = msec;
		LogUtil.i("ChannelView--->show--->currentViewType::"+currentType);
		LogUtil.i("ChannelView--->show--->timeMsec::"+msec);
		clearSelected();
		if(getVisibility()!=View.VISIBLE){
			dateView.setTime(timeMsec);
			
			if(curChannelItems!=null){
				curChannelItems = assortItems.get(playingSortIndex).getChannelList();
				ChannelItem item = null;
				if(curChannelItems!=null&&curChannelItems.size()>channelListView.currentChannelIndex){
					item = curChannelItems.get(channelListView.currentChannelIndex);
				}
				//ChannelItem item = curChannelItems.get(channelListView.currentChannelIndex);
				if(item!=null&&!"1".equals(item.getBackEnable())){
					dateView.setTodayData(false);
				}else{
					dateView.setData(playingDateIndex,true);
				}
			}else{
				dateView.setData(playingDateIndex,true);
			}
			setVisibility(View.VISIBLE);
			switch (currentViewType) {
			case SORT_VIEW_TYPE:
				if(playingSortIndex!=sortView.currentSortIndex){
					sortView.setCurrentPlayingIndex(playingSortIndex,true,true);
					curChannelItems = assortItems.get(playingSortIndex).getChannelList();
					channelListView.setData(curChannelItems,timeMsec);
				}
				try {
					channelListView.setCurrentPlayingChannelIndex(playingChannelIndex, timeMsec, false,assortItems.get(playingSortIndex));
				} catch (Exception e) {
					LogUtil.e("ChannelView--->show--->SORT_VIEW_TYPE--->"+e.toString());
				}
				LogUtil.i("sortView.currentSortViewIndex---->"+sortView.currentSortViewIndex);
				cachedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						if(playState == ViewConst.LIVE_STATE){
							ChannelItem curItem = getCurrentChannel();
							if(curItem!=null){
								//curProgramItems = getProgramItemsByDate(getFormatDate(playDate), curItem.getChannelId());
								programMap.setProgramMap(getProgramItemsByDate(getFormatDate(playDate), curItem.getChannelId()), curItem.getChannelId());
								playingProgramIndex = findCurrentPlayingProgramIndex(programMap.getProgramItems(), timeMsec);
							}
						}
						setTempPlayingIndex();
						tempSortIndex = playingSortIndex;
						tempChannelIndex = playingChannelIndex;
						tempProgramIndex = playingProgramIndex;
					}
				});
				post(new Runnable() {
					@Override
					public void run() {
						sortView.setSelected(sortView.currentSortViewIndex, true);
					}
				});
				break;
			case CHANNEL_VIEW_TYPE:
				
				if(playingSortIndex!=sortView.currentSortIndex){
					curChannelItems = assortItems.get(playingSortIndex).getChannelList();
					channelListView.setData(curChannelItems,timeMsec);
				}
				sortView.setCurrentPlayingIndex(playingSortIndex,true,false);
				channelListView.setCurrentPlayingChannelIndex(playingChannelIndex, timeMsec, false,assortItems.get(playingSortIndex),true);
				post(new Runnable() {
					@Override
					public void run() {
						channelListView.setSelected(channelListView.currentChannelViewIndex, true);
					}
				});
				
				cachedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						Message msg = Message.obtain();
						ChannelItem curItem = getCurrentChannel();
						//curProgramItems = getProgramItemsByDate(getFormatDate(playDate), curItem.getChannelId());
						programMap.setProgramMap(getProgramItemsByDate(getFormatDate(playDate), curItem.getChannelId()), curItem.getChannelId());
						if(playState == ViewConst.LIVE_STATE){
							playingProgramIndex = findCurrentPlayingProgramIndex(programMap.getProgramItems(), timeMsec);
						}
						LogUtil.i("ChannelView--->show---->playingProgramIndex::"+playingProgramIndex);
						setTempPlayingIndex();
						tempSortIndex = playingSortIndex;
						tempChannelIndex = playingChannelIndex;
						tempProgramIndex = playingProgramIndex;
						programView.setData(programMap.getProgramItems(),curItem);
						msg.what = SHOW_CHANNELVIEW_MSG1;
						cvHandler.sendMessage(msg);
					}
				});
				break;
			case PROGRAM_VIEW_TYPE:
				sortView.setCurrentPlayingIndex(playingSortIndex,true,false);
				curChannelItems = assortItems.get(playingSortIndex).getChannelList();
				channelListView.setData(curChannelItems,timeMsec);
				channelListView.setCurrentPlayingChannelIndex(playingChannelIndex, timeMsec, false,assortItems.get(playingSortIndex));
				programView.setLoaded(false);
				cachedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						Message msg = Message.obtain();
						ChannelItem curItem = getCurrentChannel();
						//curProgramItems = getProgramItemsByDate(getFormatDate(playDate), curItem.getChannelId());
						programMap.setProgramMap(getProgramItemsByDate(getFormatDate(playDate), curItem.getChannelId()), curItem.getChannelId());
						if(playState == ViewConst.LIVE_STATE){
							playingProgramIndex = findCurrentPlayingProgramIndex(programMap.getProgramItems(), timeMsec);
						}
						LogUtil.i("ChannelView--->show---->playingProgramIndex::"+playingProgramIndex);
						setTempPlayingIndex();
						tempSortIndex = playingSortIndex;
						tempChannelIndex = playingChannelIndex;
						tempProgramIndex = playingProgramIndex;
						programView.setData(programMap.getProgramItems(),curItem);
						if(playingProgramIndex!=-1&&programMap.getProgramItems().size()>0){
							msg.what = SHOW_CHANNELVIEW_MSG2;
						}else{
							msg.what = SHOW_NULLPROGRAMVIEW_MSG;
						}
						cvHandler.sendMessage(msg);
					}
				});
				if(curChannelItems!=null){
					ChannelItem item = curChannelItems.get(channelListView.currentChannelIndex);
					if(item!=null&&!"1".equals(item.getBackEnable())){
						dateView.setTodayData(false);
					}else{
						dateView.setData(playingDateIndex,true);
					}
				}else{
					dateView.setData(playingDateIndex,true);
				}
				break;
			case DATE_VIEW_TYPE:
				sortView.setCurrentPlayingIndex(playingSortIndex,true,false);
				curChannelItems = assortItems.get(playingSortIndex).getChannelList();
				channelListView.setData(curChannelItems,timeMsec);
				channelListView.setCurrentPlayingChannelIndex(playingChannelIndex, timeMsec, false,assortItems.get(playingSortIndex));
				cachedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						Message msg = Message.obtain();
						ChannelItem curItem = getCurrentChannel();
						//curProgramItems = getProgramItemsByDate(getFormatDate(playDate), curItem.getChannelId());
						programMap.setProgramMap(getProgramItemsByDate(getFormatDate(playDate), curItem.getChannelId()), curItem.getChannelId());
						if(playState == ViewConst.LIVE_STATE){
							playingProgramIndex = findCurrentPlayingProgramIndex(programMap.getProgramItems(), timeMsec);
						}
						LogUtil.i("ChannelView--->show---->playingProgramIndex::"+playingProgramIndex);
						setTempPlayingIndex();
						tempSortIndex = playingSortIndex;
						tempChannelIndex = playingChannelIndex;
						tempProgramIndex = playingProgramIndex;
						programView.setData(programMap.getProgramItems(),curItem);
						if (playingProgramIndex==-1||programMap.getProgramItems().size()==0) {
							msg.what = SHOW_NULLPROGRAMVIEW_MSG;
						}else{
							msg.what = SHOW_CHANNELVIEW_MSG1;
						}
						cvHandler.sendMessage(msg);
					}
				});
				if(playingProgramIndex!=-1){
					if(curChannelItems!=null){
						ChannelItem item = curChannelItems.get(channelListView.currentChannelIndex);
						if(item!=null&&!"1".equals(item.getBackEnable())){
							dateView.setTodayData(true);
						}else{
							dateView.setData(playingDateIndex,true,true);
						}
					}else{
						dateView.setData(playingDateIndex,true,true);
					}
					dateView.setSelected(playingDateIndex,true);
				}
				
				break;
			default:
				break;
			}
		}
	}

	public void setData(List<AssortItem> assortItems,int currentSortIndex,final int curChannelIndex,final long timeMsec,boolean isTitleChange,boolean isUpdateProgram){
		LogUtil.d("ChannelView---->setData--->currentSortIndex:" + currentSortIndex + ",curChannelIndex:"+curChannelIndex);
		this.assortItems = assortItems;
		this.timeMsec = timeMsec;
		this.playingSortIndex = currentSortIndex;
		this.playingChannelIndex = curChannelIndex;
		if(!isUpdateProgram){
			if(getVisibility() == VISIBLE){
				hide();
			}
		}
		sortView.setData(assortItems);
		sortView.setCurrentPlayingIndex(currentSortIndex,true,true);
		curChannelItems = assortItems.get(currentSortIndex).getChannelList();
		if(curChannelItems == null ||curChannelItems.size()==0||curChannelIndex>curChannelItems.size())
			return;
		channelListView.setData(curChannelItems,timeMsec);
		channelListView.setCurrentPlayingChannelIndex(curChannelIndex, timeMsec, isTitleChange,assortItems.get(playingSortIndex));
		dateView.setTime(timeMsec);
		playDate = timeMsec;
		dateView.setData(playingDateIndex,true);
		if(isUpdateProgram){
			cachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					LogUtil.d("ChannelView---->setData--->thread--->playingChannelIndex:" + playingChannelIndex);
					if(curChannelItems == null ||curChannelItems.size()==0||playingChannelIndex>curChannelItems.size())
						return;
					ChannelItem item = curChannelItems.get(playingChannelIndex);
					//curProgramItems = getProgramItemsByDate(getFormatDate(timeMsec), item.getChannelId());
					programMap.setProgramMap(getProgramItemsByDate(getFormatDate(timeMsec), item.getChannelId()),item.getChannelId());
					playingProgramIndex = findCurrentPlayingProgramIndex(programMap.getProgramItems(), timeMsec);
					programView.setData(programMap.getProgramItems(), item);
					String title = dateView.getCurData();
					programView.setCurrentProgramIndex(playingProgramIndex, timeMsec, false,title);
					setTempPlayingIndex();
				}
			});
		}
	}
	
	public void setKTData(List<AssortItem> assortItems,int currentSortIndex,final int curChannelIndex,final long timeMsec,boolean isTitleChange){
		LogUtil.d("ChannelView---->setKTData--->" + currentSortIndex);
		this.assortItems = assortItems;
		this.timeMsec = timeMsec;
		this.playingSortIndex = currentSortIndex;
		this.playingChannelIndex = curChannelIndex;
		LogUtil.i("跨天播放初始化-->playingSortIndex::"+currentSortIndex+" playingChannelIndex::" + curChannelIndex);
		dateView.setSetData(false);
		playingDateIndex = 0;
		
		sortView.setData(assortItems);
		sortView.setCurrentPlayingIndex(currentSortIndex,true,true);
		curChannelItems = assortItems.get(currentSortIndex).getChannelList();
		if(curChannelItems == null ||curChannelItems.size()==0||curChannelIndex>curChannelItems.size())
			return;
		channelListView.setData(curChannelItems,timeMsec);
		channelListView.setCurrentPlayingChannelIndex(curChannelIndex, timeMsec, isTitleChange,assortItems.get(playingSortIndex));
		dateView.setTime(timeMsec);
		cachedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				ChannelItem item = curChannelItems.get(curChannelIndex);
				
				if(playState == ViewConst.LIVE_STATE){
					//curProgramItems = getProgramItemsByDate(getFormatDate(timeMsec),item.getChannelId());
					programMap.setProgramMap(getProgramItemsByDate(getFormatDate(timeMsec),item.getChannelId()),item.getChannelId());
					LogUtil.i("直播跨天播放-->curProgramItems::"+programMap.getProgramItems());
					playingProgramIndex = findCurrentPlayingProgramIndex(programMap.getProgramItems(), timeMsec);
				}else{
					if(playingProgramItem!=null){
						//curProgramItems = getProgramItemsByDate(getFormatDate(playDate),item.getChannelId());
						programMap.setProgramMap(getProgramItemsByDate(getFormatDate(playDate),item.getChannelId()),item.getChannelId());
						LogUtil.i("回看跨天播放-->curProgramItems::"+programMap.getProgramItems());
						long sTime = 0;
						if(programMap.getProgramItems()!=null&&programMap.getProgramItems().size()>0){
							sTime = playingProgramItem.getlStartTime();
						}else{
							if(channelProgramInfo!=null&&channelProgramInfo.getDateList()!=null
									&&channelProgramInfo.getDateList().size()>0){
								//curProgramItems = channelProgramInfo.getDateList().get(0).getProgramItemList();
								programMap.setProgramMap(channelProgramInfo.getDateList().get(0).getProgramItemList(),item.getChannelId());
								if(programMap.getProgramItems()!=null&&programMap.getProgramItems().size()>0){
									sTime = programMap.getProgramItems().get(0).getlStartTime();
								}
							}
						}
						LogUtil.i("回看跨天播放-->date::"+getFormatDate(sTime));
						playingProgramIndex = findCurrentPlayingProgramIndex(programMap.getProgramItems(), sTime);
						LogUtil.i("回看跨天播放-->playingProgramIndex::"+playingProgramIndex);
						playingDateIndex = getCurrentDateIndex(playingProgramIndex);
					}
					
				}
				Message msg = Message.obtain();
				msg.what = INIT_KT_INFO;
				cvHandler.sendMessage(msg);
			}
		});
	};

	public void setIChannelViewListener(IChannelViewListener iChannelViewListener) {
		this.iChannelViewListener = iChannelViewListener;
	}
	
	private boolean isEnterDonw = false; //是否已经按了确认键
	
	public volatile boolean isThreadLive = false;
	//public boolean isOnKeyDown = false;
	public boolean isOnKeyUp = false;
	public int lastWaitTime = 0;
	public volatile boolean threadState = false;
	private volatile int sendMsg = LOAD_CHANNEL;
	
	public volatile boolean isProgramThreadLive = false;
	public int lastProgramWaitTime = 0;
	//public boolean isProgramOnKeyDown = false;
	public boolean isProgramOnKeyUp = false;
	public volatile boolean programThreadState = false;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.i("ChannelView--------->onKeyDown----->"+keyCode);
		startDisplayTimer();
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_ESCAPE:
			hide();
			if(playState == ViewConst.LIVE_STATE){
				PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.ChannelList,
						null, null, Action.ExitKey);
			}else{
				PageActionReport.GetInstance().reportPageAction(PageType.PlaybackPage, ModuleType.ChannelList,
						null, null, Action.ExitKey);
			}
			
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (currentViewType == DATE_VIEW_TYPE) {
				moveFocusedToProgram(DREC_LEFT);
			} else if (currentViewType == PROGRAM_VIEW_TYPE) {
				moveFocusedToChannel(DREC_LEFT);
			} else if (currentViewType == CHANNEL_VIEW_TYPE){
				moveFocusedToSort(DREC_LEFT);
			} 
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (currentViewType == SORT_VIEW_TYPE) {
				moveFocusedToChannel(DREC_RIGHT);
			} else if (currentViewType == CHANNEL_VIEW_TYPE) {
				moveFocusedToProgram(DREC_RIGHT);
			} else if (currentViewType == PROGRAM_VIEW_TYPE){
				moveFocusedToDate(DREC_RIGHT);
			} 
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			if (currentViewType == SORT_VIEW_TYPE) {
				//isOnKeyDown = true;
				sortView.setPreSort();
			} else if (currentViewType == CHANNEL_VIEW_TYPE) {
				//currentTimes++;
				//isProgramOnKeyDown = true;
				channelListView.setPreChannel();
			} else if (currentViewType == PROGRAM_VIEW_TYPE){
				if(programView.isLoaded()){
					programView.setPreProgram();
				}else{
					TVToast.show(getContext(), R.string.loaded_data, 1000);
				}
				
			} else {
				//isOnKeyDown = true;
				dateView.setPreDate();
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (currentViewType == SORT_VIEW_TYPE) {
				//isOnKeyDown = true;
				sortView.setNextSort();
			} else if (currentViewType == CHANNEL_VIEW_TYPE) {
				//currentTimes++;
				//isProgramOnKeyDown = true;
				channelListView.setNextChannel();
			} else if (currentViewType == PROGRAM_VIEW_TYPE){
				if(programView.isLoaded()){
					programView.setNextProgram();
				}else{
					TVToast.show(getContext(), R.string.loaded_data, 1000);
				}
			} else {
				//isOnKeyDown = true;
				dateView.setNextDate();
			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			LogUtil.i("onKeyDonw--->isEnterDonw::"+isEnterDonw);
			if(!isEnterDonw){
				LogUtil.i("onKeyDonw--->进入了判断语句里");
				isEnterDonw = true;
				if(currentViewType == CHANNEL_VIEW_TYPE || currentViewType == PROGRAM_VIEW_TYPE){
					if(currentViewType == CHANNEL_VIEW_TYPE){
						playingChannelIndex = channelListView.currentChannelIndex;
						playingSortIndex = sortView.currentSortIndex;
						if(isCurrentChannelPlaying(playingSortIndex, playingChannelIndex)&& playState == ViewConst.LIVE_STATE){
							break;
						}
						playingProgramIndex = programView.currentProgramIndex;
						playerView.showProgramInfoView(curChannelItems.get(playingChannelIndex));
						playerView.hideErrorPage();
						iChannelViewListener.onClearBackPlayInfo();
						LogUtil.i("节目列表切台开始计时");
						PlayerView.startTime = System.currentTimeMillis();
						if(playState == ViewConst.LIVE_STATE){
							iChannelViewListener.onChannelClick(curChannelItems.get(playingChannelIndex));
						}else{
							playState = ViewConst.LIVE_STATE;
							iChannelViewListener.onBackToLiveClick(curChannelItems.get(playingChannelIndex));
						}
						
						PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.ChannelList,
								curChannelItems.get(playingChannelIndex).getChannelId(), FocusType.ChannelListSwitch, Action.OKKey);
						playingDateIndex = 0;
						playDate = timeMsec;
						setTempPlayingIndex();
					}else if(currentViewType == PROGRAM_VIEW_TYPE){
						if(programMap.getProgramItems()==null||programMap.getProgramItems().size()<programView.currentProgramIndex||programView.currentProgramIndex==-1){
							break;
						}
						ProgramItem programItem = programMap.getProgramItems().get(programView.currentProgramIndex);
						long stime = programItem.getlStartTime();
						long etime = programItem.getlStopTime();
						
						if(stime <= timeMsec){
							
							/*playingChannelIndex = channelListView.currentChannelIndex;
							playingSortIndex = sortView.currentSortIndex;*/
							ChannelItem channel = curChannelItems.get(channelListView.currentChannelIndex);
							if(etime>timeMsec){
								playingChannelIndex = channelListView.currentChannelIndex;
								playingSortIndex = sortView.currentSortIndex;
								playingProgramIndex = programView.currentProgramIndex;
								if(isCurrentPlaying(playingSortIndex, playingChannelIndex, playingProgramIndex)){
									break;
								}
								iChannelViewListener.onClearBackPlayInfo();
								playerView.showProgramInfoView(channel);
								playerView.hideErrorPage();
								playingDateIndex = 0;
								playDate = timeMsec;
								if(playState == ViewConst.LIVE_STATE){
									iChannelViewListener.onChannelClick(curChannelItems.get(playingChannelIndex));
								}else{
									playState = ViewConst.LIVE_STATE;
									iChannelViewListener.onBackToLiveClick(curChannelItems.get(playingChannelIndex));
								}
								PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.ChannelList,
										channel.getChannelId(), FocusType.ChannelListSwitch, Action.OKKey);
								setTempPlayingIndex();
							}else{
								if("1".equals(channel.getBackEnable())){
									playingChannelIndex = channelListView.currentChannelIndex;
									playingSortIndex = sortView.currentSortIndex;
									playingProgramIndex = programView.currentProgramIndex;
									if(isCurrentPlaying(playingSortIndex, playingChannelIndex, playingProgramIndex)){
										//playerView.resumePlay();
										break;
									}
									iChannelViewListener.onClearBackPlayInfo();
									playState = ViewConst.BACK_STATE;
									playingDateIndex = getCurrentDateIndex(playingProgramIndex);
									playingProgramItem = programItem;
									iChannelViewListener.onProgramClick(channel, programItem);
									PageActionReport.GetInstance().reportPageAction(PageType.PlaybackPage, ModuleType.ChannelList,
											channel.getChannelId(), FocusType.ChannelPlayBack, Action.OKKey);
									String title = dateView.getCurData();
									//programView.setCurrentProgramIndex(playingProgramIndex, timeMsec, true,title);
									dateView.setTempDateIndex(playingDateIndex);
									dateView.setData(playingDateIndex, true);
									playDate = dateView.getCurrentDate();
									setTempPlayingIndex();
								}else{
									break;
								}
								
							}
							
						}else{
							break;
						}
						
					}
					tempSortIndex = playingSortIndex;
					tempChannelIndex = playingChannelIndex;
					tempProgramIndex = playingProgramIndex;
					channelListView.setPlayState(playState);
					ChannelItem curItem = getCurrentChannel();
					if(curItem!=null){
						channelListView.setPlayingChannelId(getCurrentChannel().getChannelId());
					}else{
						channelListView.setPlayingChannelId("");;
					}
					channelListView.setCurrentPlayingChannelIndex(playingChannelIndex, timeMsec, false,assortItems.get(playingSortIndex));
				}
			}
			
			break;
		case KeyEvent.KEYCODE_MENU:
			hide();
			return false;
		default:
			playerView.onKeyDown(keyCode, event);
			break;
		}
		return true;
	}
	
	private void moveFocusedToSort(int drec){
		LogUtil.i("ChannelView--->moveFocusedToSort");
		clearSelected();
		clearBackgroudColor();
		if(programView.getVisibility() == View.VISIBLE){
	/*		channelListView.setBackgroundResource(R.drawable.background_right);*/
			backView.setLayoutParams(new RelativeLayout.LayoutParams((int) (width*back_width1), LayoutParams.MATCH_PARENT));
			channelListView.setLayoutParams(new LayoutParams(channel_width,LayoutParams.MATCH_PARENT));
			clearViews(programView,dateView);
			sortView.setVisibility(View.VISIBLE);
			channelListView.setDivde(false);
			//AnimUtil.setAnim(sortView, -1*sort_width,0,ANIM_TIME,null);
			AnimUtil.setAnim(sortView, -1*sort_width,0,channelListView, 0, sort_width,ANIM_TIME,new AnimEndEvent() {
				@Override
				public void onAnimEnd() {
					clearSelected();
					clearBackgroudColor();
					sortView.focusEvent(true);
					sortView.setSelected(sortView.currentSortViewIndex,true);
					currentViewType = SORT_VIEW_TYPE;
					LogUtil.i("moveFocusedToSort---->AnimUtil.setAnim");
				}
			});
			
			currentViewType = SORT_VIEW_TYPE;
			
		}else{
			sortView.focusEvent(true);
			currentViewType = SORT_VIEW_TYPE;
			sortView.setSelected(sortView.currentSortViewIndex,true);
		}
	}
	
	private void moveFocusedToChannel(int drec){
		LogUtil.i("ChannelView--->moveFocusedToChannel");
		if(channelListView.ACTUAL_VIEW_SIZE == -1){
			return;
		}
		clearSelected();
		clearBackgroudColor();
		if(drec == DREC_RIGHT){
			if(channelListView.isLoaded()){
				currentViewType = CHANNEL_VIEW_TYPE;
				channelListView.focusEvent(true);
				channelListView.setSelected(channelListView.currentChannelViewIndex,true);
			}else{
				sortView.focusEvent(true);
				currentViewType = SORT_VIEW_TYPE;
				sortView.setSelected(sortView.currentSortViewIndex,true);
				TVToast.show(getContext(), R.string.loaded_data, 1000);
			}
		
		}else{
			
			if(dateView.getVisibility() == View.VISIBLE){
				//showChannelAndProgram();
				backView.setLayoutParams(new RelativeLayout.LayoutParams((int) (width*back_width2), LayoutParams.MATCH_PARENT));
				clearViews(sortView,dateView);
				channelListView.setVisibility(View.VISIBLE);
				programView.setVisibility(VISIBLE);
				channelListView.setDivde(true);
				programView.setDivde(false);
				//AnimUtil.setAnim(channelListView, -1*channel_width,0,ANIM_TIME,null);
				AnimUtil.setAnim(channelListView, -1*channel_width,0,programView, 0, channel_width,ANIM_TIME,new AnimEndEvent() {
					@Override
					public void onAnimEnd() {
						clearSelected();
						channelListView.setSelected(channelListView.currentChannelViewIndex,true);
						LogUtil.i("moveFocusedToChannel---->AnimUtil.setAnim");
					}
				});
				currentViewType = CHANNEL_VIEW_TYPE;
				
				channelListView.focusEvent(true);
				
			}else{
				currentViewType = CHANNEL_VIEW_TYPE;
				channelListView.focusEvent(true);
				channelListView.setSelected(channelListView.currentChannelViewIndex,true);
			}
		}
		
	}
	
	/*private void showChannelAndProgram(){
		channelListView.setLayoutParams(new LayoutParams(channel_width,LayoutParams.MATCH_PARENT));
		programView.setLayoutParams(new LayoutParams(program_rwidth,LayoutParams.MATCH_PARENT));
		channelListView.setBackgroundResource(R.drawable.background_left);
		programView.setBackgroundResource(R.drawable.background_right);
	}*/
	
	private void moveFocusedToProgram(int drec){
		LogUtil.i("ChannelView--->moveFocusedToProgram");
		clearSelected();
		clearBackgroudColor();
		if(drec == DREC_RIGHT){
			if(sortView.getVisibility() == View.VISIBLE){
				//showChannelAndProgram();
				if(!programView.isLoaded()){
					currentViewType = CHANNEL_VIEW_TYPE; 
					channelListView.focusEvent(true);
					channelListView.setSelected(channelListView.currentChannelViewIndex,true);
					TVToast.show(getContext(), R.string.loaded_data, 1000);
					return;
				}
				clearViews(sortView,dateView);
				channelListView.setVisibility(VISIBLE);
				programView.setVisibility(View.VISIBLE);
				backView.setLayoutParams(new RelativeLayout.LayoutParams((int) (width*back_width2), LayoutParams.MATCH_PARENT));
				channelListView.setDivde(true);
				programView.setDivde(false);
				//programView.setLoaded(false);
				//dateView.setData(playingDateIndex,true);
				//AnimUtil.setAnim(channelListView, sort_width,0,ANIM_TIME,null);
				AnimUtil.setAnim(channelListView, sort_width,0,programView, sort_width+channel_width, channel_width,ANIM_TIME,new AnimEndEvent() {
					@Override
					public void onAnimEnd() {
						final String channelId = curChannelItems.get(channelListView.currentChannelIndex).getChannelId();
						if(programMap.getId()!=null&&programMap.getId().equals(channelId)){
							currentViewType = PROGRAM_VIEW_TYPE; 
							programView.focusEvent(true);
							programView.setSelected(programView.currentProgramViewIndex,true);
							LogUtil.i("moveFocusedToProgram---->AnimUtil.setAnim---->programView.setSelected");
							
						}else{
							programView.setLoaded(false);
							dateView.setData(playingDateIndex,true);
							cachedThreadPool.execute(new Runnable() {
								@Override
								public void run() {
									//String channelId = curChannelItems.get(channelListView.currentChannelIndex).getChannelId();
									if(channelId!=null){
										boolean isTitleChange = false;
										ChannelItem curItem = getCurrentChannel();
										String playDateStr = getFormatDate(TimeManager.GetInstance().getCurrentTime());
										if(curItem!=null&&channelId.equals(curItem.getChannelId())){
											isTitleChange = true;
											playDateStr = getFormatDate(playDate);
										}
										
										//curProgramItems = getProgramItemsByDate(playDateStr,channelId);
										programMap.setProgramMap(getProgramItemsByDate(playDateStr,channelId),channelId);
										Message msg = Message.obtain();
										msg.what = SHOW_PROGRAMVIEW_MSG;
										msg.obj = isTitleChange;
										cvHandler.sendMessage(msg);
									}
								}
							});
						}
						
					}
				});

				
			}else{
				if(!programView.isLoaded()){
					currentViewType = CHANNEL_VIEW_TYPE; 
					channelListView.focusEvent(true);
					channelListView.setSelected(channelListView.currentChannelViewIndex,true);
					TVToast.show(getContext(), R.string.loaded_data, 1000);
					return;
				}
				
				if(programMap.getProgramItems() == null || programMap.getProgramItems().size() == 0||programView.currentProgramIndex ==-1){
					currentViewType = CHANNEL_VIEW_TYPE; 
					channelListView.focusEvent(true);
					channelListView.setSelected(channelListView.currentChannelViewIndex,true);
					return;
					
				}else{
					currentViewType = PROGRAM_VIEW_TYPE; 
					programView.focusEvent(true);
					programView.setSelected(programView.currentProgramViewIndex,true);
				}
			}
		}else{
			if(programMap.getProgramItems() == null || programMap.getProgramItems().size() == 0){
				clearViews(sortView,dateView);
				//showChannelAndProgram();
				backView.setLayoutParams(new RelativeLayout.LayoutParams((int) (width*back_width2), LayoutParams.MATCH_PARENT));
				channelListView.setVisibility(View.VISIBLE);
				programView.setVisibility(VISIBLE);
				channelListView.setDivde(true);
				programView.setDivde(false);
				//AnimUtil.setAnim(channelListView, -1*(channel_width),0,ANIM_TIME,null);
				AnimUtil.setAnim(channelListView, -1*(channel_width),0,programView, 0, channel_width,ANIM_TIME,new AnimEndEvent() {
					@Override
					public void onAnimEnd() {
						clearSelected();
						channelListView.setSelected(channelListView.currentChannelViewIndex,true);
						LogUtil.i("moveFocusedToProgram---->AnimUtil.setAnim---->channelListView.setSelected");
					}
				});
				currentViewType = CHANNEL_VIEW_TYPE;
				channelListView.focusEvent(true);
				
			}else{
				if(programView.isLoaded()){
					currentViewType = PROGRAM_VIEW_TYPE;
					programView.focusEvent(true);
					programView.setSelected(programView.currentProgramViewIndex,true);
				}
				else{
					currentViewType = DATE_VIEW_TYPE;
					dateView.focusEvent(true);
					dateView.setData(dateView.currentDateViewIndex,false);
					TVToast.show(getContext(), R.string.loaded_data, 1000);
				}
			}
		}
	}
	
	private void moveFocusedToDate(int drec){
		LogUtil.i("ChannelView--->moveFocusedToDate");
		clearSelected();
		clearBackgroudColor();
		if(channelListView.getVisibility() == View.VISIBLE){
			backView.setLayoutParams(new RelativeLayout.LayoutParams((int) (width*back_width1), LayoutParams.MATCH_PARENT));
			programView.setLayoutParams(new LayoutParams(program_width,LayoutParams.MATCH_PARENT));
			//programView.setBackgroundResource(R.drawable.background_left);
			currentViewType = DATE_VIEW_TYPE;
			clearViews(sortView,channelListView);
			programView.setVisibility(VISIBLE);
			dateView.setVisibility(View.VISIBLE);
			programView.setDivde(true);
			//AnimUtil.setAnim(programView, channel_width,0,ANIM_TIME,null);
			AnimUtil.setAnim(programView, channel_width,0,dateView, channel_width+program_width, program_width,ANIM_TIME,new AnimEndEvent() {
				@Override
				public void onAnimEnd() {
					clearSelected();
					boolean isMove = false;
					if(curChannelItems!=null){
						ChannelItem item = curChannelItems.get(channelListView.currentChannelIndex);
						if(item!=null&&!"1".equals(item.getBackEnable())){
							isMove = true;
							dateView.setTodayData(true);
						}
					}
					
					if(!isMove){
						dateView.setData(getCurrentDateIndex(programView.currentProgramIndex),false);
					}
					
					LogUtil.i("moveFocusedToDate---->AnimUtil.setAnim");
				}
			});
			dateView.focusEvent(true);
		}else{
			currentViewType = DATE_VIEW_TYPE;
			boolean isMove = false;
			if(curChannelItems!=null){
				ChannelItem item = curChannelItems.get(channelListView.currentChannelIndex);
				if(item!=null&&!"1".equals(item.getBackEnable())){
					dateView.setTodayData(true);
					isMove = true;
				}
			}
			if(!isMove){
				dateView.setData(getCurrentDateIndex(programView.currentProgramIndex),false);
			}
			dateView.focusEvent(true);
			//dateView.setData(getCurrentDateIndex(programView.currentProgramIndex),false);
		}
	}
	
	private void clearSelected(){
		LogUtil.i("ChannelView--->clearSelected");
		sortView.clearAllSelected();
		channelListView.clearAllSelected();
		programView.clearAllSelected();
		dateView.clearAllSelected();
		
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		LogUtil.i("ChannelView--------->onKeyUp----->"+keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			if (currentViewType == SORT_VIEW_TYPE){
				updateChannelListView();
			}else{
				if(currentViewType == CHANNEL_VIEW_TYPE){
					if(channelListView.isBegin(lastChannelSize)){
						if(sortView.setLoopPre()){
							updatePreChannelListView();
							post(new Runnable() {
								@Override
								public void run() {
									channelListView.setSelected(channelListView.currentChannelViewIndex,true);
								}
							});
						}
					}
					/*if(curChannelItems!=null){
						channelId = curChannelItems.get(channelListView.currentChannelIndex).getChannelId();
					}*/
					updateProgramView();
					lastChannelSize = channelListView.currentChannelIndex;
				}else if(currentViewType == DATE_VIEW_TYPE ){
					updateProgramViewByDate();
				}else{
					if(programView.isBegin(lastProgramSize)){
						if(dateView.setLoopPre()){
							String cdate = getFormatDate(dateView.getCurrentDate());
							updatePreProgramViewByDate(cdate);
							post(new Runnable() {
								@Override
								public void run() {
									programView.setSelected(programView.currentProgramViewIndex, true);
								}
							});
						}
					}
					lastProgramSize = programView.currentProgramIndex;
				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (currentViewType == SORT_VIEW_TYPE){
				updateChannelListView();
			}else{
				if(currentViewType == CHANNEL_VIEW_TYPE){
					if(channelListView.isEnd(lastChannelSize)){
						if(sortView.setLoopNext()){
							updateNextChannelListView();
							post(new Runnable() {
								@Override
								public void run() {
									channelListView.setSelected(channelListView.currentChannelViewIndex,true);
								}
							});
						}
					}
					/*if(curChannelItems!=null){
						channelId = curChannelItems.get(channelListView.currentChannelIndex).getChannelId();
					}*/
					updateProgramView();
					lastChannelSize = channelListView.currentChannelIndex;
				}else if(currentViewType == DATE_VIEW_TYPE ){
					updateProgramViewByDate();
				}else{
					if(programView.isEnd(lastProgramSize)){
						if(dateView.setLoopNext()){
							String cdate = getFormatDate(dateView.getCurrentDate());
							updateNextProgramViewByDate(cdate);
							post(new Runnable() {
								@Override
								public void run() {
									programView.setSelected(programView.currentProgramViewIndex, true);
								}
							});
						}
					}
					lastProgramSize = programView.currentProgramIndex;
				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			isEnterDonw = false;
			break;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	public ChannelItem getCurrentChannel(){
		
		try {
			List<ChannelItem> items = assortItems.get(playingSortIndex).getChannelList();
			return items.get(playingChannelIndex);
		} catch (Exception e) {
			LogUtil.e("getCurrentChannel-->"+e.toString());
		}
		return null;
	}
	
	private void updateChannelListView(){
		LogUtil.i("updateChannelListView--------->currentSortIndex----->"+sortView.currentSortIndex);
		/*curChannelItems = assortItems.get(sortView.currentSortIndex).getChannelList();
		channelListView.setData(curChannelItems,timeMsec);
		if(playingSortIndex == sortView.currentSortIndex){
			dateView.setData(playingDateIndex,true);
			channelListView.setCurrentPlayingChannelIndex(playingChannelIndex, timeMsec, false,assortItems.get(sortView.currentSortIndex));
		}else{
			dateView.setData(0,true);
			channelListView.setCurrentPlayingChannelIndex(0, timeMsec, true,assortItems.get(sortView.currentSortIndex));
		}*/
		isOnKeyUp = true;
		channelListView.setLoaded(false);
		isThreadLive = true;
		sendMsg = LOAD_CHANNEL;
		if(!threadState){
			threadState = true;
			LogUtil.i("updateChannelListView--->onKeyUp--->启动Lazy线程");
			LazyThread lazyThread = new LazyThread();
			lazyThread.start();
		}
	}
	
	private void updateNextChannelListView(){
		curChannelItems = assortItems.get(sortView.currentSortIndex).getChannelList();
		channelListView.setData(curChannelItems,timeMsec);
		dateView.setData(0,true);
		channelListView.setCurrentPlayingChannelIndex(0, timeMsec, true,assortItems.get(sortView.currentSortIndex),true);
		channelListView.clearAllSelected();
	}
	
	private void updatePreChannelListView(){
		curChannelItems = assortItems.get(sortView.currentSortIndex).getChannelList();
		channelListView.setData(curChannelItems,timeMsec);
		dateView.setData(0,true);
		channelListView.setCurrentPlayingChannelIndex(curChannelItems.size()-1, timeMsec, true,assortItems.get(sortView.currentSortIndex),true);
		channelListView.clearAllSelected();
	}
	

	private void  updateProgramView(){
		/*cachedThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				long tempTimes = currentTimes;
				programView.setLoaded(false);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LogUtil.i("ChannelView--->updateProgramView--->tempTimes::"+tempTimes+",currentTimes::"+currentTimes);
				if(tempTimes==currentTimes){
					ChannelItem curChannelItem = getCurrentChannel();
					boolean isChangeTitle = false;
					String playDateStr = getFormatDate(TimeManager.GetInstance().getCurrentTime());
					if(curChannelItem!=null && curChannelItem.getChannelId().equals(channelId)){
						playDateStr = getFormatDate(playDate);
						isChangeTitle = true;
					}
					LogUtil.i("updateProgramView--->playDateStr--->"+playDateStr);
					curProgramItems = getProgramItemsByDate(playDateStr,channelId);
					Message msg = Message.obtain();
					msg.what = UPDATE_PROGRAMVIEW_MSG;
					msg.obj = isChangeTitle;
					cvHandler.sendMessage(msg);
				}
			}
		});*/
		isProgramOnKeyUp = true;
		programView.setLoaded(false);
		isProgramThreadLive = true;
		if(!programThreadState){
			programThreadState = true;
			LogUtil.i("updateProgramView--->onKeyUp--->启动Lazy线程");
			LazyProgramThread lazyProgramThread = new LazyProgramThread();
			lazyProgramThread.start();
		}
	}
	
	private void loadProgramView(){
		String date = getFormatDate(dateView.getCurrentDate());
		LogUtil.i("ChannelView--->loadProgramView--->"+date);
		if(curChannelItems!=null){
			ChannelItem channel = curChannelItems.get(channelListView.currentChannelIndex);
			String channelId = null;
			if(channel!=null){
				channelId = channel.getChannelId();
			}
		
			if(channelProgramInfo!=null){
				List<DateItem> dateItems = channelProgramInfo.getDateList();
				boolean isFind = false;
				for(DateItem item:dateItems){
					if(date.equals(item.getDate())){
						//curProgramItems = item.getProgramItemList();
						programMap.setProgramMap(item.getProgramItemList(),channelId);
						programView.setData(programMap.getProgramItems(),channel);
						int curIndex = findCurrentPlayingProgramIndex(programMap.getProgramItems(), timeMsec);
						boolean isTitleChange = false;
						ChannelItem curItem = getCurrentChannel();
						if(date.equals(getFormatDate(playDate))&&curItem!=null&&curItem.getChannelId().equals(channelId)){
							isTitleChange = true;
							curIndex = playingProgramIndex;
						}
						programView.setCurrentProgramIndex(curIndex,timeMsec, isTitleChange,dateView.getCurData());
						isFind = true;
						break;
					}
				}
				if(!isFind){
					//curProgramItems = null;
					programMap.setProgramMap(null,null);
					programView.setData(programMap.getProgramItems(),channel);
					programView.setCurrentProgramIndex(0,timeMsec, false,dateView.getCurData());
				}
			}
		}
	}
	
	private void updateProgramViewByDate(){
		if(curChannelItems!=null){
			ChannelItem item = curChannelItems.get(channelListView.currentChannelIndex);
			if(item!=null&&"1".equals(item.getBackEnable())){
				isOnKeyUp = true;
				programView.setLoaded(false);
				isThreadLive = true;
				sendMsg = LOAD_PROGRAM;
				if(!threadState){
					threadState = true;
					LogUtil.i("updateProgramViewByDate--->onKeyUp--->启动Lazy线程");
					LazyThread lazyThread = new LazyThread();
					lazyThread.start();
				}
			}
		}
	}
	
	
	private void updatePreProgramViewByDate(String date){
		if(channelProgramInfo!=null){
			List<DateItem> dateItems = channelProgramInfo.getDateList();
			for(DateItem item:dateItems){
				if(date.equals(item.getDate())){
					//curProgramItems = item.getProgramItemList();
					ChannelItem channel = curChannelItems.get(channelListView.currentChannelIndex);
					programMap.setProgramMap(item.getProgramItemList(), channel.getChannelId());
					programView.setData(programMap.getProgramItems(),channel);
					programView.setCurrentProgramIndex(programMap.getProgramItems().size()-1,timeMsec, false,dateView.getCurData(),true);
					programView.clearAllSelected();
					break;
				}
			}
			
		}
		
	}
	
	private void updateNextProgramViewByDate(String date){
		if(channelProgramInfo!=null){
			List<DateItem> dateItems = channelProgramInfo.getDateList();
			for(DateItem item:dateItems){
				if(date.equals(item.getDate())){
					//curProgramItems = item.getProgramItemList();
					ChannelItem channel = curChannelItems.get(channelListView.currentChannelIndex);
					programMap.setProgramMap(item.getProgramItemList(), channel.getChannelId());
					programView.setData(programMap.getProgramItems(),channel);
					programView.setCurrentProgramIndex(0,timeMsec, false,dateView.getCurData(),true);
					programView.clearAllSelected();
					break;
				}
			}
			
		}
	}
		
	private String getFormatDate(long time){
		return DateUtil.msec2String(time,"yyyy-MM-dd");
	}
	
	private int changeWidth(int width){
		return DisplayManager.GetInstance().changeWidthSize(width);
	}
	
	private int changeHeight(int height){
		return DisplayManager.GetInstance().changeHeightSize(height);
	}

	private int findCurrentPlayingProgramIndex(List<ProgramItem> programItems,long timeMsec) {
		
		if (programItems != null && programItems.size() > 0) {
			for (int i = 0; i < programItems.size(); i++) {
				long stime = programItems.get(i).getlStartTime();
				long etime = programItems.get(i).getlStopTime();
				//long st = DateUtil.string2Msec(stime, "yyyy-MM-dd kk:mm:ss");
				//long et = DateUtil.string2Msec(etime, "yyyy-MM-dd kk:mm:ss");
				if (stime<=timeMsec && etime>= timeMsec) {
					LogUtil.i("findCurrentPlayingProgramIndex-->index::"+i);
					return i;
				}
			}
			LogUtil.i("findCurrentPlayingProgramIndex-->getProgramSelectIndex");
			return 0;
		} else {
			LogUtil.i("findCurrentPlayingProgramIndex-->index::-1");
			return -1;
		}
		
	}
	
	public void gotoAllPreChannel(){
		if(playingChannelIndex<=0){
			if(playingSortIndex>0){
				playingSortIndex --;
			}else{
				playingSortIndex = assortItems.size() - 1;
			}
			curChannelItems = assortItems.get(playingSortIndex).getChannelList();
		/*	playingSortIndex = assortItems.size() - 1;*/
			playingChannelIndex = curChannelItems.size() - 1;
		}else{
			playingChannelIndex--;
		}
		gotoChannelCount ++;
	}
	
	public void gotoAllNextChannel(){
		int size = getCurChannelItemsSize();
		if(playingChannelIndex>=size-1){
			if(playingSortIndex <assortItems.size()-1){
				playingSortIndex ++;
			}else{
				playingSortIndex = 0;
			}
			curChannelItems = assortItems.get(playingSortIndex).getChannelList();
			playingChannelIndex = 0;
		}else{
			playingChannelIndex++;
		}
		gotoChannelCount ++;
	}
	
	private int getCurChannelItemsSize(){
		 try {
			return assortItems.get(playingSortIndex).getChannelList().size();
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	
	/**
	 * 回到直播
	 */
	//private boolean threadLive = true;
	public void gotoPlayChannel(){
/*		new Thread(new Runnable() {
			@Override
			public void run() {
				long tmpCount = gotoChannelCount;
				do {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						LogUtil.e("gotoPlayChannel----->"+e.toString());
					}
					LogUtil.d("tmpCount------->" + tmpCount + "--------->gotoChannelCount------->" + gotoChannelCount);
				} while (tmpCount!=gotoChannelCount);
				Message msg = Message.obtain();
				msg.obj = gotoChannelCount;
				msg.what = GOTO_PLAYING_CHANNEL;
				cvHandler.sendMessage(msg);
			}
		}).start();*/
		Message msg = Message.obtain();
		//msg.obj = gotoChannelCount;
		msg.what = GOTO_PLAYING_CHANNEL;
		cvHandler.sendMessage(msg);
		
		/*do {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				LogUtil.e("gotoPlayChannel----->"+e.toString());
			}
			LogUtil.d("tmpCount------->" + tmpCount + "--------->gotoChannelCount------->" + gotoChannelCount);
		} while (tmpCount!=gotoChannelCount);*/
	}
	
	public void numChangeChannel(ChannelItem channelItem){
		setCurrentInfo(channelItem);
		Message msg = Message.obtain();
		msg.what = NUM_CHANGE_CHANNEL;
		cvHandler.sendMessage(msg);

	}
	
	/**
	 * 回到直播状态
	 * 上下键切台，数字切台
	 */
	public void initToLive(){
		playingDateIndex = 0;
		playDate = timeMsec;
		//curProgramItems = null;
		programMap.setProgramMap(null,null);
		channelListView.setPlayState(ViewConst.LIVE_STATE);
		if(playState == ViewConst.BACK_STATE){
			LogUtil.i("由回看回到 直播rest播放器");
			playerView.getVooleMediaPlayer().reset();
		}
		playState = ViewConst.LIVE_STATE;
		setTempPlayingIndex();
	}
	
	/**
	 * 进行跨天初始化
	 */
	public void initKT(int sortIndex,int channelIndex){
		//if(playState == PlayerView.LIVE_STATE){
		LogUtil.i("跨天播放初始化：：playingSortIndex"+sortIndex+" playingChannelIndex::" + channelIndex);
			playingSortIndex = sortIndex;
			playingChannelIndex = channelIndex;
			playingDateIndex = 0;
			//playDate = timeMsec;
			//curProgramItems = null;
			programMap.setProgramMap(null,null);
			dateView.setSetData(false);
		//}
	}
	
	private void setCurrentInfo(ChannelItem channelItem){
		int aSize = assortItems.size();
		for(int i = 0;i<aSize;i++){
			AssortItem item = assortItems.get(i);
			if("常用频道".equals(item.getAssortName())){
				continue;
			}
			List<ChannelItem> items = item.getChannelList();
			int cSize = items.size();
			for(int j = 0;j<cSize;j++){
				if(channelItem.getChannelNo().equals(items.get(j).getChannelNo())){
					playingSortIndex = i;
					playingChannelIndex = j;
					//initToLive();
					//iChannelViewListener.onClearBackPlayInfo();
					curChannelItems = assortItems.get(playingSortIndex).getChannelList();
					return;
				}
			}
		}
	/*	int size = curChannelItems.size();
		for(int i = 0;i<size;i++){
			if(channelItem.getSequence().equals(curChannelItems.get(i).getSequence())){
				playingChannelIndex = i;
				playingSortIndex = getCurrentSortIndex(playingChannelIndex);
				initToLive();
				iChannelViewListener.onClearBackPlayInfo();
				break;
			}
		}*/
	}
	
	/*private ChannelItem getSelectedChannel(){
		if(curChannelItems!=null){
			return curChannelItems.get(channelListView.currentChannelIndex);
		}
		return null;
	}*/
	
	private void backToChannel(){
		clearSelected();
		currentViewType = CHANNEL_VIEW_TYPE;
		sortView.setVisibility(View.VISIBLE);
		channelListView.setVisibility(View.VISIBLE);
		channelListView.setDivde(false);
		programView.setVisibility(View.GONE);
		dateView.setVisibility(View.GONE);
		backView.setLayoutParams(new RelativeLayout.LayoutParams((int) (width*1.2), LayoutParams.MATCH_PARENT));
	/*	channelListView.setBackgroundResource(R.drawable.background_right);*/
		channelListView.setLayoutParams(new LayoutParams(channel_width,LayoutParams.MATCH_PARENT));
		AnimUtil.setAnim(sortView, -1*sort_width,0,channelListView, 0, sort_width,ANIM_TIME,new AnimEndEvent() {
			@Override
			public void onAnimEnd() {
				channelListView.setSelected(channelListView.currentChannelViewIndex, true);
			}
		});
	}
	
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		/*if(getVisibility()!=VISIBLE){
			playerView.resumePlay();
		}*/
	}
	
	public ProgramItem getPlayingProgram(){
		return playingProgramItem;
	}
	
	private void clearViews(){
		sortView.setVisibility(GONE);
		channelListView.setVisibility(GONE);
		programView.setVisibility(GONE);
		dateView.setVisibility(GONE);
	}
	
	private void clearViews(View view1,View view2){
		view1.setVisibility(GONE);
		view2.setVisibility(GONE);
	}
	
	private void clearBackgroudColor(){
		sortView.focusEvent(false);
		channelListView.focusEvent(false);
		programView.focusEvent(false);
		dateView.focusEvent(false);
	}
	
	private void setTempPlayingIndex(){
		sortView.setTempSortIndex(assortItems.get(playingSortIndex));
		//channelListView.setTempChannelIndex(getCurrentChannel());
		String dateStr = getFormatDate(playDate);
		LogUtil.i("ChannelView--->setTempPlayingIndex::"+dateStr);
		long time = DateUtil.string2Msec(dateStr, "yyyy-MM-dd");
		programView.setTempProgramIndex(getCurrentChannel(),playingProgramIndex,time);
		dateView.setTempDateIndex(playingDateIndex);
	}
	
	private int getCurrentDateIndex(int programIndex){
		if(programMap.getProgramItems()!=null&&programIndex<programMap.getProgramItems().size()&&programIndex!=-1){
			ProgramItem item =  programMap.getProgramItems().get(programIndex);
			//long msec = DateUtil.string2Msec(item.getStartTime(), "yyyy-MM-dd kk:mm:ss");
			long msec = item.getlStartTime();
			String date = getFormatDate(msec);
			List<DateItem> dateItems = channelProgramInfo.getDateList();
			for(int i = 0,j=6;i<dateItems.size();i++,j--){
				String tempDate = dateItems.get(i).getDate();
				if(date.equals(tempDate)){
					LogUtil.i("ChannelView-->getCurrentDateIndex::"+j);
					return j;
				}
			}
		}
		return 0;
	}
	
	
	private List<ProgramItem> getProgramItemsByDate(String dateStr,String channelId){
		if(!TextUtils.isEmpty(channelId)){
			if(curChannelItems!=null){
				for(ChannelItem item:curChannelItems){
					if(item.getChannelId().equals(channelId)){
						if(!"1".equals(item.getBackEnable())){
							List<DateItem> list = item.getDateList();
							if(list!=null&&list.size()>0){
								LogUtil.i("getProgramItemsByDate--->没有回看读取今天的节目单");
								if(channelProgramInfo!=null){
									channelProgramInfo.setDateList(list);
								}
								return list.get(0).getProgramItemList();
							}
						}
						break;
					}
				}
			}
			channelProgramInfo = ProgramManager.GetInstance().getChannelProgramInfo(channelId);
			if(channelProgramInfo!=null){
				List<DateItem> dateItems = channelProgramInfo.getDateList();
				if(dateItems!=null){
					for(DateItem item:dateItems){
						if(dateStr.equals(item.getDate())){
							return item.getProgramItemList();
						}
					}
				}
			}
		}
		return null;
	}
	
	private boolean isCurrentChannelPlaying(int sortIndex,int channelIndex){
		boolean state = false;
		LogUtil.i("ChannelView--->isCurrentChannelPlaying");
		if(tempSortIndex == sortIndex && tempChannelIndex == channelIndex){
			String playDateStr = getFormatDate(playDate);
			String timeStr = getFormatDate(timeMsec);
			if(timeStr.equals(playDateStr)){
				state = true;
			}
			LogUtil.i("ChannelView--->isCurrentChannelPlaying::playDateStr:"+playDateStr+",timeStr:"+timeStr);
		}
		return state;
	}
	
	private boolean isCurrentPlaying(int sortIndex,int channelIndex,int programIndex){
		boolean state = false;
		if(tempSortIndex == sortIndex && tempChannelIndex == channelIndex && tempProgramIndex == programIndex){
			ProgramItem item = null;
			try {
				item = programMap.getProgramItems().get(programIndex);
			} catch (Exception e) {
				LogUtil.e("ChannelView--->isCurrentPlaying--->"+e.toString());
			}
			if(item!=null){
				String dateStr = getFormatDate(item.getlStartTime());
				String playDateStr = getFormatDate(playDate);
				if(dateStr.equals(playDateStr)){
					state = true;
				}
			}
		}
		return state;
	}
	
	public PlayerView getPlayerView() {
		return playerView;
	}

	public SortView getSortView() {
		return sortView;
	}

	public ChannelListView getChannelListView() {
		return channelListView;
	}

	public ProgramView getProgramView() {
		return programView;
	}

	public DateView getDateView() {
		return dateView;
	}

	public int getCurrentViewType() {
		return currentViewType;
	}

	public int getPlayingSortIndex() {
		return playingSortIndex;
	}

	public int getPlayingChannelIndex() {
		return playingChannelIndex;
	}

	public int getPlayingProgramIndex() {
		return playingProgramIndex;
	}

	public int getPlayingDateIndex() {
		return playingDateIndex;
	}

	/*public int getTempSortIndex() {
		return tempSortIndex;
	}

	public int getTempChannelIndex() {
		return tempChannelIndex;
	}

	public int getTempProgramIndex() {
		return tempProgramIndex;
	}*/

	public List<ChannelItem> getCurChannelItems() {
		return curChannelItems;
	}

	/*public List<ProgramItem> getCurProgramItems() {
		return curProgramItems;
	}
*/
	public long getTimeMsec() {
		return timeMsec;
	}

	public long getPlayDate() {
		return playDate;
	}

	public int getPlayState() {
		return playState;
	}

	public int getCurrentType() {
		return currentType;
	}

	/*public long getCurrentTimes() {
		return currentTimes;
	}*/
	
	public String getCurrentChannelType(){
		if(assortItems!=null){
			if(playingSortIndex>-1&&playingSortIndex<assortItems.size()){
				return assortItems.get(playingSortIndex).getAssortValue();
			}
		}
		return "";
	}
	
	private class LazyThread extends Thread{

		public void run() {
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
			flag:
			while(isThreadLive){
				while(!isOnKeyUp){
					try {
						Thread.sleep(10);
						lastWaitTime +=10;
						if(lastWaitTime>=5000){
							lastWaitTime = 0;
							isThreadLive = false;
							break flag;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while(lastWaitTime<500){
					if(isOnKeyUp){
						isOnKeyUp = false;
						lastWaitTime=0;
					}
					try {
						Thread.sleep(10);
						lastWaitTime+=10;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				lastWaitTime = 0;
				isOnKeyUp = false;
				cvHandler.sendEmptyMessage(sendMsg);
			}
			threadState = false;
		};
		
	};
	
	private class LazyProgramThread extends Thread{

		public void run() {
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
			flag:
			while(isProgramThreadLive){
				while(!isProgramOnKeyUp){
					try {
						Thread.sleep(10);
						lastProgramWaitTime +=10;
						if(lastProgramWaitTime>=5000){
							lastProgramWaitTime = 0;
							isProgramThreadLive = false;
							break flag;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while(lastProgramWaitTime<500){
					if(isProgramOnKeyUp){
						isProgramOnKeyUp = false;
						lastProgramWaitTime=0;
					}
					try {
						Thread.sleep(10);
						lastProgramWaitTime+=10;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				lastProgramWaitTime = 0;
				isProgramOnKeyUp = false;
				
				ChannelItem curChannelItem = getCurrentChannel();
				boolean isChangeTitle = false;
				String playDateStr = getFormatDate(TimeManager.GetInstance().getCurrentTime());
				String channelId = null;
				if(curChannelItems!=null){
					channelId = curChannelItems.get(channelListView.currentChannelIndex).getChannelId();
				}
				if(curChannelItem!=null && curChannelItem.getChannelId().equals(channelId)){
					playDateStr = getFormatDate(playDate);
					isChangeTitle = true;
				}
				LogUtil.i("updateProgramView--->playDateStr--->"+playDateStr);
				//curProgramItems = getProgramItemsByDate(playDateStr,channelId);
				programMap.setProgramMap(getProgramItemsByDate(playDateStr,channelId), channelId);
				Message msg = Message.obtain();
				msg.what = UPDATE_PROGRAMVIEW_MSG;
				msg.obj = isChangeTitle;
				cvHandler.sendMessage(msg);
			}
			programThreadState = false;
		};
		
	};
	
	static class ProgramMap{
		private String id;
		private List<ProgramItem> programItems;
		public String getId() {
			return id;
		}
		public List<ProgramItem> getProgramItems() {
			return programItems;
		}
		public void setProgramMap(List<ProgramItem> programItems,String id) {
			this.programItems = programItems;
			this.id = id;
		}
		
	}
}
