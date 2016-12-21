package com.gntv.tv.view;

import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gntv.tv.R;
import com.gntv.tv.common.utils.DateUtil;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.channel.ChannelItem;
import com.gntv.tv.view.base.BaseChannelLinearLayout;
import com.gntv.tv.view.base.FontSize;

@SuppressLint("ResourceAsColor")
public class DateView extends BaseChannelLinearLayout {
	private TextView txtTitle = null;
	private LinearLayout ll_date = null;
	private DateItemView[] dateItemViews = null;
	private static final int ITEM_VIEW_SIZE = 7;
	private int ACTUAL_VIEW_SIZE = ITEM_VIEW_SIZE;
	public int currentDateViewIndex = 0;
	private boolean isSetData = false;
	private long currentDate = 0;
	private int tempDateIndex = 0;

	public DateView(Context context) {
		super(context);
		init(context);
	}

	@Override
	protected void init(Context context) {
		setOrientation(VERTICAL);
		//this.setBackgroundResource(R.drawable.background_right);
		initTitle(context);
		initDate(context);
	}
	
	private void initTitle(Context context){
		txtTitle = new TextView(context);
		txtTitle.setText(R.string.date);
		txtTitle.setGravity(Gravity.CENTER_VERTICAL);
		txtTitle.setTextSize(changeTextSize(FontSize.CHANNEL_TITLE));
		txtTitle.setTextColor(getColor(R.color.white));
		LayoutParams txtParams = new LayoutParams(LayoutParams.MATCH_PARENT,changeHeight(60));
		txtTitle.setPadding(changeWidth(45),changeHeight(10), 0, 0);
		txtTitle.setLayoutParams(txtParams);
		addView(txtTitle);
	}
	
	private void initDate(Context context) {
		ll_date = new LinearLayout(context);
		ll_date.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams llParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		llParams.setMargins(changeWidth(5), changeHeight(20), 0,changeHeight(10));
		ll_date.setLayoutParams(llParams);
		ll_date.setOrientation(LinearLayout.VERTICAL);
		dateItemViews = new DateItemView[ACTUAL_VIEW_SIZE];
		LinearLayout.LayoutParams param_item = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
		param_item.setMargins(changeWidth(15),0, 0, 0);
		for (int i = ACTUAL_VIEW_SIZE-1; i >=0; i--) {
			dateItemViews[i] = new DateItemView(context);
			dateItemViews[i].setLayoutParams(param_item);
			dateItemViews[i].setGravity(Gravity.CENTER_VERTICAL);
			ll_date.addView(dateItemViews[i]);
		}
		addView(ll_date);
	}
	
	public void setTime(long msec){
		currentDate = msec;
	}
	
	public void setData(int index,boolean isPlaying){
		setData(index, isPlaying,false);
	}
	
	private boolean isCanMove = true;
	public void setTodayData(boolean isSelected){
		isCanMove = false;
		isSetData = false;
		currentDateViewIndex = 0;
		ll_date.removeAllViews();
		ACTUAL_VIEW_SIZE = 1;
		dateItemViews = new DateItemView[1];
		LinearLayout.LayoutParams param_item = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, changeHeight(80));
		param_item.setMargins(changeWidth(15),0, 0, 0);
		dateItemViews[0] = new DateItemView(getContext());
		dateItemViews[0].setLayoutParams(param_item);
		dateItemViews[0].setGravity(Gravity.CENTER_VERTICAL);
		ll_date.addView(dateItemViews[0]);
		
		Date date = new Date(currentDate);
		int month = date.getMonth()+1;
		StringBuilder sbDate = new StringBuilder();
		if(month<10){
			sbDate.append("0");
		}
		sbDate.append(month);
		sbDate.append("-");
		int day = date.getDate();
		if(day<10){
			sbDate.append("0");
		}
		sbDate.append(day);
		dateItemViews[0].setData("今天",sbDate.toString());
		dateItemViews[0].setPlaying(false);
		if(isSelected){
			setSelected(currentDateViewIndex, true);
		}
	}
	
	public void setData(int index,boolean isPlaying,boolean isFocus){
		currentDateViewIndex = index;
		isCanMove = true;
		if(!isSetData){
			isSetData = true;
			ACTUAL_VIEW_SIZE = ITEM_VIEW_SIZE;
			dateItemViews = new DateItemView[ACTUAL_VIEW_SIZE];
			ll_date.removeAllViews();
			LinearLayout.LayoutParams param_item = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
			param_item.setMargins(changeWidth(15),0, 0, 0);
			for (int i = ACTUAL_VIEW_SIZE-1; i >=0; i--) {
				dateItemViews[i] = new DateItemView(getContext());
				dateItemViews[i].setLayoutParams(param_item);
				dateItemViews[i].setGravity(Gravity.CENTER_VERTICAL);
				ll_date.addView(dateItemViews[i]);
			}
			
			for (int i = ACTUAL_VIEW_SIZE-1;i>=0;i--){
				Date date = new Date(currentDate-24*60*60*1000*i);
				String week = DateUtil.getWeekOfDate(date);
				if(i == 0){
					week = "今天";
				}
				
				int month = date.getMonth()+1;
				StringBuilder sbDate = new StringBuilder();
				if(month<10){
					sbDate.append("0");
				}
				sbDate.append(month);
				sbDate.append("-");
				int day = date.getDate();
				if(day<10){
					sbDate.append("0");
				}
				sbDate.append(day);
				dateItemViews[i].setData(week,sbDate.toString());
				dateItemViews[i].setPlaying(false);
			}
			
		}
		if(isPlaying){
			for(int i=0;i<ACTUAL_VIEW_SIZE;i++){
				if(i == tempDateIndex){
					dateItemViews[i].setPlaying(true);
				}else{
					if(isFocus){
						dateItemViews[i].focusEvent(isFocus);
					}else{
						dateItemViews[i].setPlaying(false);
					}
				}
			
			}
		}
		
		if(isPlaying){
			dateItemViews[currentDateViewIndex].setPlaying(true);
		}else{
			setSelected(currentDateViewIndex, true);
		}
		
	}
	
	public void setSelected(int index,boolean isFocus){
		boolean isPlaying = false;
		if(index == tempDateIndex){
			isPlaying = true;
		}
		dateItemViews[index].setSelected(isFocus);
	}

	public void setPreDate(){
		LogUtil.i("setPreDate--->currentDateViewIndex-->"+currentDateViewIndex);
		if (currentDateViewIndex <6&&isCanMove) {
			setSelected(currentDateViewIndex, false);
			setSelected(++currentDateViewIndex, true);
		} 
	}

	public void setNextDate(){
		LogUtil.i("setNextDate--->currentDateViewIndex-->"+currentDateViewIndex);
		if (currentDateViewIndex > 0&&isCanMove) {
			setSelected(currentDateViewIndex, false);
			setSelected(--currentDateViewIndex, true);
		} 
	}
	
	public boolean setLoopPre(){
		if (currentDateViewIndex <6&&isCanMove) {
			setFocus(currentDateViewIndex, false);
			setFocus(++currentDateViewIndex, true);
			return true;
		} 
		return false;
	}
	
	public boolean setLoopNext(){
		if (currentDateViewIndex > 0&&isCanMove) {
			setFocus(currentDateViewIndex, false);
			setFocus(--currentDateViewIndex, true);
			return true;
		} 
		return false;
	}
	
	private void setFocus(int index,boolean isFocus){
		dateItemViews[index].setPlaying(isFocus);
	}
	
	public long getCurrentDate(){
		return currentDate - 24*60*60*1000*currentDateViewIndex;
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
				dateItemViews[i].focusEvent(isFocus);
			}else{
				dateItemViews[i].setPlaying(true);
			}
		}
	}

	public void setSetData(boolean isSetData) {
		this.isSetData = isSetData;
	}

	public void setTempDateIndex(int tempDateIndex) {
		this.tempDateIndex = tempDateIndex;
	}
	
	public String getCurData(){
		return dateItemViews[currentDateViewIndex].getDataStr();
	}
	
	private boolean isBelongSelected(int index){
		boolean isSelected = false;
		if(currentDateViewIndex == index)
		{
			isSelected = true;
		}
		return isSelected;
	}

	@Override
	public void clearAllSelected() {
		if(dateItemViews!=null){
			for(DateItemView view:dateItemViews){
				view.setBackgroundResource(0);
			}
		}
	}
}
