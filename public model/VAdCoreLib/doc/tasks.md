===================================================2016-04-15
0.小屏测试
1.暂停 第一次不马上显示
2.loading广告完成后 出现黑屏现象
  原有流程:
            -->VooleMediaPlayer.prepare()
            -->AuthManager.GetInstance().getPlayUrl()
            -->VooleMediaPlayer.initPlayer()
            -->StandardEpgPlayer.prepare()
            -->AdEpgPlayerController.onPrepare()-->解析数据-->解析完成后-->展示loading广告
            -->loading完成后-->StandardEpgPlayer.originalPrepare()
            -->StandardPlayer.su_originalPrepare()
            -->初始化mSurfaceView,同时VooleMediaPlayer.addView(mSurfaceView)
            -->在mSurfaceView.surfaceCreated()
            -->mPlayer.setDataSource(url)&&mPlayer.prepareAsync();
            
3.场景广告小屏处理
4.暂停广告小屏处理