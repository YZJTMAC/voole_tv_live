package com.voole.player.lib.core.letv;

import com.lecloud.cp.sdk.player.live.CPActionLivePlayer;
import com.lecloud.sdk.player.IPlayer;
import com.lecloud.sdk.surfaceview.ISurfaceView;
import com.lecloud.sdk.surfaceview.impl.BaseSurfaceView;
import com.lecloud.sdk.videoview.live.ActionLiveVideoView;

import android.content.Context;

public class CPActionLiveVideoView extends ActionLiveVideoView{
	
	private ISurfaceView surfaceView;
	
	public CPActionLiveVideoView(Context context) {
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
        player = new CPActionLivePlayer(context);
    }
    
    public IPlayer getMediaPlayer() {
    	return player;
    }
}	
