package com.voole.player.lib.core.letv;

import android.content.Context;

import com.lecloud.cp.sdk.player.vod.CPVodPlayer;
import com.lecloud.sdk.player.IPlayer;
import com.lecloud.sdk.surfaceview.ISurfaceView;
import com.lecloud.sdk.videoview.vod.VodVideoView;

/**
 * Created by administror on 2016/6/13 0013.
 */
public class CPVodVideoView extends VodVideoView{
	
	private ISurfaceView surfaceView;
	
    public CPVodVideoView(Context context) {
        super(context);
    }
    
    @Override
    protected void setVideoView(ISurfaceView surfaceView) {
    	this.surfaceView = surfaceView;
    	super.setVideoView(surfaceView);
    }
    
    public ISurfaceView getSurfaceView() {
    	return surfaceView;
    }

    @Override
    protected void initPlayer() {
        player = new CPVodPlayer(context);
    }
    
    public IPlayer getMediaPlayer() {
    	return player;
    }
}
