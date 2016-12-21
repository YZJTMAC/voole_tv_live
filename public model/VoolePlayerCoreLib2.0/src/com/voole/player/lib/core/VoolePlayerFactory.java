package com.voole.player.lib.core;

import com.voole.player.lib.core.interfaces.IPlayReport;
import com.voole.player.lib.core.interfaces.IPlayer;
import com.voole.player.lib.core.letv.NewLePlayer;
//import com.voole.player.lib.core.letv.LePlayer;
import com.voole.player.lib.core.normal.NormalPlayer;
import com.voole.player.lib.core.report.EpgReport;
import com.voole.player.lib.core.report.FilmReport;
import com.voole.player.lib.core.report.LiveReport;
import com.voole.player.lib.core.report.PlayBackReport;
import com.voole.player.lib.core.sohu.SohuPlayer;
import com.voole.player.lib.core.standard.StandardEpgPlayer;
import com.voole.player.lib.core.standard.StandardLivePlayer;
import com.voole.player.lib.core.standard.StandardPlayBackPlayer;

class VoolePlayerFactory {
	public enum PlayerType{
		NORMAL,
		EPG_VOOLE,
		EPG_SOHU,
		EPG_LETV,
		LIVE_VOOLE, 
		PLAYBACK_VOOLE,
		FILM_VOOLE
	}
	public static IPlayer getPlayer(PlayerType param){
		switch (param) {
			case NORMAL:
				return new NormalPlayer();
			case EPG_LETV:
//				return new LePlayer();
				return new NewLePlayer();
			case EPG_SOHU:
				return new SohuPlayer();
			case EPG_VOOLE:
				return new StandardEpgPlayer();
			case LIVE_VOOLE:
				return new StandardLivePlayer();
			case PLAYBACK_VOOLE:
				return new StandardPlayBackPlayer();
			case FILM_VOOLE:
				return new StandardEpgPlayer();
			default:
				return new StandardEpgPlayer();
		}
		/*if(VooleMediaPlayer.CP_TYPE_SOHU.equals(param)){
			return new SohuPlayer();
		}else{
			return new StandardPlayer();
		}*/
	}
	
	public static IPlayReport getPlayReport(PlayerType param){
		switch (param) {
		case NORMAL:
		case EPG_LETV:
		case EPG_SOHU:
		case EPG_VOOLE:
			return new EpgReport();
		case LIVE_VOOLE:
			return new LiveReport();
//			return new NoReport();
		case PLAYBACK_VOOLE:
			return new PlayBackReport();
		case FILM_VOOLE:
			return new FilmReport();
		default:
			return new EpgReport();
	}
	}
}
