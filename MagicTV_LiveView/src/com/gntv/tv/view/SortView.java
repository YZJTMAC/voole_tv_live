package com.gntv.tv.view;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gntv.tv.R;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.channel.AssortItem;
import com.gntv.tv.view.base.BaseChannelRelativeLayout;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.FontSize;

public class SortView extends BaseChannelRelativeLayout{
	private TextView txtTitle = null;
	private LinearLayout ll_sort = null;
	private List<AssortItem> assortItems = null;
	/**
	 * 展示VIew的数目
	 */
	private static final int ITEM_VIEW_SIZE = 7;
	private int ACTUAL_VIEW_SIZE = ITEM_VIEW_SIZE;
	private AssortItemView[] sortItems = null;
	public int currentSortIndex = 0;
	private AssortItem tempSort = null;
	private int assortListSize = 0;
	public int currentSortViewIndex = 0;
	private int width = 0;
	//private int ItemHeight = 0;

	public SortView(Context context) {
		super(context);
		init(context);
	}
	
	@Override
	protected void init(Context context){
		setFocusable(true);
		setFocusableInTouchMode(true);
		width  = DisplayManager.GetInstance().getScreenWidth() *1/2;
		initTitle(context,this);
		initSort(context,this);
		//setBackgroundResource(R.drawable.background_left);

	}
	
	private void initTitle(Context context,ViewGroup parent){
		txtTitle = new TextView(context);
		txtTitle.setId(10001);
		txtTitle.setText(R.string.sort_type);
		txtTitle.setGravity(Gravity.CENTER_VERTICAL);
		txtTitle.setTextSize(changeTextSize(FontSize.CHANNEL_TITLE));
		txtTitle.setTextColor(getColor(R.color.white));
		LayoutParams txtParams = new LayoutParams(LayoutParams.MATCH_PARENT,changeHeight(60));
		txtTitle.setLayoutParams(txtParams);
		txtTitle.setPadding(changeWidth(55), changeHeight(10), 0, 0);
		parent.addView(txtTitle);
	}
	
	private void initSort(Context context,ViewGroup parent){
		ll_sort = new LinearLayout(context);
		ll_sort.setId(10002);
		int screenHeight = DisplayManager.GetInstance().getScreenHeight();
		LayoutParams llParams = new LayoutParams(width*2/7,LayoutParams.MATCH_PARENT);
		//ItemHeight = (screenHeight-changeHeight(120))/ITEM_VIEW_SIZE;
		llParams.addRule(BELOW, 10001);
		llParams.setMargins(changeWidth(15), changeHeight(40), 0, changeHeight(40));
		ll_sort.setLayoutParams(llParams);
		ll_sort.setOrientation(LinearLayout.VERTICAL);
		sortItems = new AssortItemView[ITEM_VIEW_SIZE];
		LinearLayout.LayoutParams param_item = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0,1);
		for (int i = 0; i < ITEM_VIEW_SIZE; i++) {
			sortItems[i] = new AssortItemView(ctx);
			sortItems[i].setLayoutParams(param_item);
			ll_sort.addView(sortItems[i]);
		}
		parent.addView(ll_sort);
	}
	
	public void setSelected(int index,boolean isFocus){
		sortItems[index].setSelected(isFocus);
		
	}
	
	@Override
	public void clearAllSelected(){
		if(sortItems!=null){
			for(AssortItemView view:sortItems){
				view.setBackgroundResource(0);
			}
		}
	}
	
	private void setPlaying(int index,boolean isPlaying){
		sortItems[index].setPlaying(isPlaying);
	}
	
	public void setData(List<AssortItem> data){
		assortItems = data;
		
	}
	
	public void setCurrentPlayingIndex(int index,boolean isPlaying,boolean isFocus){
		if(assortItems==null)
			return;
		this.currentSortIndex = index;
		if (assortItems != null && assortItems.size() > 0) {
			for (int i = 0; i < assortItems.size(); i++) {
				assortItems.get(i).setPlaying(i==currentSortIndex);
			}
			setSortItem(isPlaying,isFocus);
		}
	}
	
	private void setSortItem(boolean isPlaying,boolean isFocus){
		assortListSize = assortItems.size();
		if (assortListSize <= ITEM_VIEW_SIZE) {
			if(assortListSize<ITEM_VIEW_SIZE){
				ll_sort.removeAllViews();
				ACTUAL_VIEW_SIZE = assortListSize;
				sortItems = new AssortItemView[ACTUAL_VIEW_SIZE];
				LinearLayout.LayoutParams param_item = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, changeHeight(70));
				for (int i = 0; i < ACTUAL_VIEW_SIZE; i++) {
					sortItems[i] = new AssortItemView(ctx);
					sortItems[i].setLayoutParams(param_item);
					ll_sort.addView(sortItems[i]);
				}
			}
			
			for (int i = 0;i < ACTUAL_VIEW_SIZE; i++) {
				AssortItem assortItem = assortItems.get(i);
				sortItems[i].setData(assortItem.getAssortName());
			}
			currentSortViewIndex = currentSortIndex;
		} else {
			if(currentSortIndex<=ACTUAL_VIEW_SIZE/2){
				for (int i = 0; i < ACTUAL_VIEW_SIZE; i++){
					AssortItem assortItem = assortItems.get(i);
					sortItems[i].setData(assortItem.getAssortName());
				}
				currentSortViewIndex = currentSortIndex;
			}else if(currentSortIndex>ACTUAL_VIEW_SIZE/2 && currentSortIndex<assortListSize-ACTUAL_VIEW_SIZE/2){
				for (int i=currentSortIndex-ACTUAL_VIEW_SIZE/2,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
					AssortItem assortItem = assortItems.get(i);
					sortItems[j].setData(assortItem.getAssortName());
				}
				currentSortViewIndex = ACTUAL_VIEW_SIZE/2;
			}else{
				for(int i = assortListSize-ACTUAL_VIEW_SIZE,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
					AssortItem assortItem = assortItems.get(i);
					sortItems[j].setData(assortItem.getAssortName());
				}
				currentSortViewIndex = ACTUAL_VIEW_SIZE + currentSortIndex - assortListSize;
			}
		}
		for(int i=0;i<ACTUAL_VIEW_SIZE;i++){
			if(isFocus){
				sortItems[i].focusEvent(isFocus);
			}else{
				setPlaying(i, false);
			}
		}
		if(isPlaying){
			setPlaying(currentSortViewIndex, true);
		}else{
			post(new Runnable() {
				@Override
				public void run() {
					setSelected(currentSortViewIndex, true);
				}
			});
		
		}
	}
		
	public void setPreSort(){
		LogUtil.i("setPreSort--->currentSortViewIndex-->"+currentSortViewIndex);
		if (currentSortIndex > 0) {
			currentSortIndex--;
			if(assortListSize<=ACTUAL_VIEW_SIZE){
				if (currentSortViewIndex > 0) {
					setSelected(currentSortViewIndex, false);
					setSelected(--currentSortViewIndex, true);
				}
			}else{
				if(currentSortIndex<ACTUAL_VIEW_SIZE/2){
					if (currentSortViewIndex > 0) {
						setSelected(currentSortViewIndex, false);
						setSelected(--currentSortViewIndex, true);
					}
				}else if(currentSortIndex>=ACTUAL_VIEW_SIZE/2 && currentSortIndex<assortListSize-ACTUAL_VIEW_SIZE/2-1){
					for (int i=currentSortIndex-ACTUAL_VIEW_SIZE/2,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
						AssortItem assortItem = assortItems.get(i);
						sortItems[j].setData(assortItem.getAssortName());
						setSelected(j, false);
					}
					setSelected(ACTUAL_VIEW_SIZE/2, true);
				}else{
					if (currentSortViewIndex > 0) {
						setSelected(currentSortViewIndex, false);
						setSelected(--currentSortViewIndex, true);
					}
				}
			}
			 
		} 
	}
	
	public void setNextSort(){
		LogUtil.i("setNextSort--->currentSortViewIndex-->"+currentSortViewIndex);
		if (currentSortIndex < assortListSize-1) {
			currentSortIndex++;
			if(assortListSize <= ACTUAL_VIEW_SIZE){
				if (currentSortViewIndex < ACTUAL_VIEW_SIZE-1) {
					setSelected(currentSortViewIndex, false);
					setSelected(++currentSortViewIndex, true);
				} 
			}else {
				if(currentSortIndex<=ACTUAL_VIEW_SIZE/2){
					if (currentSortViewIndex < ACTUAL_VIEW_SIZE-1) {
						setSelected(currentSortViewIndex, false);
						setSelected(++currentSortViewIndex, true);
					}
				}else if(currentSortIndex>ACTUAL_VIEW_SIZE/2 && currentSortIndex<assortListSize-ACTUAL_VIEW_SIZE/2){
					for (int i=currentSortIndex-ACTUAL_VIEW_SIZE/2,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
						AssortItem assortItem = assortItems.get(i);
						sortItems[j].setData(assortItem.getAssortName());
						setSelected(j, false);
					}
					setSelected(ACTUAL_VIEW_SIZE/2, true);
				}else{
					if (currentSortViewIndex < ACTUAL_VIEW_SIZE-1) {
						setSelected(currentSortViewIndex, false);
						setSelected(++currentSortViewIndex, true);
					}
				}
			}
		}  
	}
	
	public void setFocus(int index,boolean isFocus){
		setPlaying(index, isFocus);
	}
	
	public boolean setLoopPre(){
		if (currentSortIndex > 0) {
			currentSortIndex--;
			if(assortListSize<=ACTUAL_VIEW_SIZE){
				if (currentSortViewIndex > 0) {
					setFocus(currentSortViewIndex, false);
					setFocus(--currentSortViewIndex, true);
				}
			}else{
				if(currentSortIndex<ACTUAL_VIEW_SIZE/2){
					if (currentSortViewIndex > 0) {
						setFocus(currentSortViewIndex, false);
						setFocus(--currentSortViewIndex, true);
					}
				}else if(currentSortIndex>=ACTUAL_VIEW_SIZE/2 && currentSortIndex<assortListSize-ACTUAL_VIEW_SIZE/2-1){
					for (int i=currentSortIndex-ACTUAL_VIEW_SIZE/2,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
						AssortItem assortItem = assortItems.get(i);
						sortItems[j].setData(assortItem.getAssortName());
					}
				}else{
					if (currentSortViewIndex > 0) {
						setFocus(currentSortViewIndex, false);
						setFocus(--currentSortViewIndex, true);
					}
				}
			}
			return true;
			 
		}
		return false;
	}
	
	public boolean setLoopNext(){
		if (currentSortIndex < assortListSize-1) {
			currentSortIndex++;
			if(assortListSize <= ACTUAL_VIEW_SIZE){
				if (currentSortViewIndex < ACTUAL_VIEW_SIZE-1) {
					setFocus(currentSortViewIndex, false);
					setFocus(++currentSortViewIndex, true);
				} 
			}else {
				if(currentSortIndex<=ACTUAL_VIEW_SIZE/2){
					if (currentSortViewIndex < ACTUAL_VIEW_SIZE-1) {
						setFocus(currentSortViewIndex, false);
						setFocus(++currentSortViewIndex, true);
					}
				}else if(currentSortIndex>ACTUAL_VIEW_SIZE/2 && currentSortIndex<assortListSize-ACTUAL_VIEW_SIZE/2){
					for (int i=currentSortIndex-ACTUAL_VIEW_SIZE/2,j=0;j<ACTUAL_VIEW_SIZE;i++,j++){
						AssortItem assortItem = assortItems.get(i);
						sortItems[j].setData(assortItem.getAssortName());
					}
				}else{
					if (currentSortViewIndex < ACTUAL_VIEW_SIZE-1) {
						setFocus(currentSortViewIndex, false);
						setFocus(++currentSortViewIndex, true);
					}
				}
			}
			return true;
		}
		return false;
	}
	

	@Override
	public void focusEvent(boolean isFocus) {
		if(isFocus){
			txtTitle.setTextColor(getColor(R.color.dark_text));
		}else{
			txtTitle.setTextColor(getColor(R.color.deep_text));
		}
		for(int i = 0;i<ACTUAL_VIEW_SIZE;i++){
			boolean isBelongSelected = isBelongSelected(i);
			if(!isBelongSelected){
				sortItems[i].focusEvent(isFocus);
			}else{
				sortItems[i].setPlaying(true);
			}
		}
	}

	public void setTempSortIndex(AssortItem tempSort) {
		this.tempSort = tempSort;
	}
	
	private boolean isBelongSelected(int index){
		boolean isSelected = false;
		if(currentSortViewIndex == index)
		{
			isSelected = true;
		}
		return isSelected;
	}
}
