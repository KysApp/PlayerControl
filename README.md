播放器控制层使用说明
=
一、播放器控制层初始化
-
    //java代码
    FrameLayout frame_control = (FrameLayout) findViewById(R.id.frame_control);
    //this当前activity，View1控制层的父控件；View2其他控件；int == 0为点播，int == 1为直播,当View2为null表示直接进入全屏播放
    PlayControl mPlayControl = new PlayControl(this, View1, View2, int);
    //添加播放器控制层监听
    mPlayControl.setOnPlayerControlListener(mPlayerControlListener);
    //添加播放器控制层
    frame_control.addView((View) mPlayControl);

    //xml布局，控制层与播放器在同一父控件下，通过改变父控件大小改变播放器与控制层大小
    <RelativeLayout
            android:id="@+id/overlay_player"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#000001">

            <VideoView
                android:id="@+id/player_surface"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                ></VideoView>

            <FrameLayout
                android:id="@+id/frame_control"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                >

            </FrameLayout>
        </RelativeLayout>

二、播放控制层监听
-
    //播放控制层监听
        public final OnPlayerControlListener mPlayerControlListener = new OnPlayerControlListener() {
            @Override
            public void onPlayPause() {     //播放按钮监听
                if (mSurface.isPlaying()) {
                    mSurface.pause();
                    mPlayControl.setState(false);
                } else {
                    mSurface.start();
                    mPlayControl.setState(true);
                }
                onVideoLength();
                mPlayControl.showOverlay();
            }

            @Override
            public void onSeekTo(int delta) {       //播放器进度监听
                if (mSurface.getDuration() <= 0)
                    return;
                if (delta < 0)
                    delta = 0;
                mSurface.seekTo(delta);
                mPlayControl.setState(mSurface.isPlaying());
                mPlayControl.onSeekTo(delta);
            }

            @Override
            public void onState() {        //播放状态监听
                mPlayControl.setState(mSurface.isPlaying());
            }

            @Override
            public int onVideoLength() {       //获取视频时长
                mPlayControl.setVideoLength(mSurface.getDuration());
                return mSurface.getDuration();
            }

            @Override
            public int onCurrentPosition() {       //获取当前播放进度
                return mSurface.getCurrentPosition();
            }

            @Override
            public int onTouchCurrentPosition() {      //获取当前播放进度
                return mSurface.getCurrentPosition();
            }

            @Override
            public boolean canShowProgress() {      //进度条显示监听
                if (mSurface != null) {
                    return mSurface.isPlaying();

                }
                return false;
            }

            @Override
            public void setDefinition(ListView mListView) {     //清晰度数据添加展示
                mListView.setAdapter(mSelectDefiAdapter);
                if (definition.equals("1")) {
                    mSelectDefiAdapter.changeSelectedPosition(2);
                } else if (definition.equals("2")) {
                    mSelectDefiAdapter.changeSelectedPosition(1);
                } else if (definition.equals("4")) {
                    mSelectDefiAdapter.changeSelectedPosition(0);
                } else {
                    mSelectDefiAdapter.changeSelectedPosition(2);
                }
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        time = mSurface.getCurrentPosition();
                        String string = Definlist.get(i);
                        if (string.equals(getString(R.string.sd))) {
                            definition = "1";
                            mSelectDefiAdapter.changeSelectedPosition(2);
                        } else if (string.equals(getString(R.string.sd_h))) {
                            definition = "2";
                            mSelectDefiAdapter.changeSelectedPosition(1);
                        } else if (string.equals(getString(R.string.hd))) {
                            definition = "4";
                            mSelectDefiAdapter.changeSelectedPosition(0);
                        } else {
                            definition = "1";
                            mSelectDefiAdapter.changeSelectedPosition(2);
                        }
                        mPlayControl.setDefinition(definition);
                        doVodAuth();
                        mPlayControl.setPopuWindowDismiss();
                    }
                });
            }

            @Override
            public void setChannelList(ListView mListView) {        //频道列表数据添加展示

            }

            @Override
            public void setDlna(ListView mListView) {       //投屏设备添加展示
                mListView.setAdapter(mDmrDevAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        mDeviceItem = mDmrList.get(i);
                        setTVUrl(mPath);
                        mPlayControl.setPopuWindowDismiss();
                    }
                });
            }

            @Override
            public void setSeries(GridView mGridView) {     //剧集添加展示
                mGridView.setAdapter(mVideoSeriesFullAdapter);
            }

            @Override
            public void getFavorite() {     //切换添加删除收藏

                if (isFavorite) {
                    delVodFavorite();
                } else {
                    addVodFavorite();
                }

            }

            @Override
            public void setOnBackPressed() {        //退出当前Activity
                mSurface.stopPlayback();
                getApplicationContext().unbindService(serviceConnection);
                finish();
            }

            @Override
            public void isSmallVolBri() {       //小屏时音量亮度指示图是否显示
                if (OperateSharePreferences.getInstance(PlayVideo.this).isSmallVolBri()) {
                    mPlayControl.setSmallVolBri(true);
                } else {
                    mPlayControl.setSmallVolBri(false);
                    OperateSharePreferences.getInstance(PlayVideo.this).saveSmallVolBri(true);
                }
            }

            @Override
            public void isFullVolBri() {        //全屏时音量亮度指示图是否显示
                if (OperateSharePreferences.getInstance(PlayVideo.this).isFullVolBri()) {
                    mPlayControl.setFullVolBri(true);
                } else {
                    mPlayControl.setFullVolBri(false);
                    OperateSharePreferences.getInstance(PlayVideo.this).saveFullVolBri(true);
                }
            }

            @Override
            public void onRefreshDlna() {       //DLNA设备刷新
                if (mDmrList.size() != 0) {
                    upnpService.getRegistry().removeAllRemoteDevices();
                }
                upnpService.getControlPoint().search();
            }

            @Override       //全屏分享监听
            public void onShare() {

            }

        };

三、控制层相关参数设置（在控制层初始化完成之后）
-
    //切换播放按键图片
    mPlayControl.setState(boolean isPlaying);
    //向控制层传递当前播放时间点
    mPlayControl.onSeekTo(int position);
    //向控制层传递视频时长
    mPlayControl.setVideoLength(int length);
    //向控制层传递当前播放时间点
    mPlayControl.setCurrentPosition(int position);
    //向控制层传递手势控制当前播放时间点
    mPlayControl.setTouchCurrentPosition(int position);
    //添加播放器控制层监听
    mPlayControl.setOnPlayerControlListener(OnPlayerControlListener listener);
    //是否显示小屏时声音、亮度操作示意图
    mPlayControl.setSmallVolBri(boolean show);
    //是否显示全屏时声音、亮度操作示意图
    mPlayControl.setFullVolBri(boolean show);
    //Dlna设备刷新
    mPlayControl.setDlnaRefresh(boolean show);
    //set视频名称
    mPlayControl.setTitle(String title);
    //设置清晰度按键图片
    mPlayControl.setDefinition(String string);
    //设置上次播放时间
    mPlayControl.setBookMark(int position);
    //设置直播回看，true为直播，false为回看
    mPlayControl.setLive(boolean show);

四、控制层相关参数设置（在控制层初始化完成之后）
-
4.1、控件是否显示设置(true显示，false隐藏)
--
	KeyShow.setPlayer_overlay_back_show(boolean isShow);//返回按键
	KeyShow.setImg_play_favorite_show(boolean isShow);//收藏按键
	KeyShow.setImg_play_share_show(boolean isShow);//分享按键
	KeyShow.setImg_play_channel_show(boolean isShow);//选集（频道）按键
	KeyShow.setImg_play_defi_show(boolean isShow);//清晰度选择
	KeyShow.setImg_play_dlna_show(boolean isShow);//投屏按键
	KeyShow.setPlayer_overlay_size_show(boolean isShow);//全屏按键
	KeyShow.setLock_overlay_button_show(boolean isShow);//锁屏按键
	KeyShow.setImg_bright_volume_show(boolean isShow);//亮度声音提示
	KeyShow.setmTitle_show(boolean isShow);//标题
	KeyShow.setmSysTime_show(boolean isShow);//系统时间
	KeyShow.setmTime_show(boolean isShow);//当前播放时间
	KeyShow.setmLength_show(boolean isShow);//视频总时长

4.2、控件Rescourse替换（imgId为图片id，colorId为颜色id）
--
	Rescourse.setPlayer_overlay_back_bg(int imgId);//返回按键
	Rescourse.setImg_play_favorite_bg(int imgId);//收藏按键（已收藏）
	Rescourse.setImg_play_unfavorite_bg(int imgId);//收藏按键（未收藏）
	Rescourse.setImg_play_share_bg(int imgId);//分享按键
	Rescourse.setPlayer_overlay_play_bg(int imgId);//播放按键
	Rescourse.setPlayer_overlay_pause_bg(int imgId);//暂停按键
	Rescourse.setImg_play_channel_bg(int imgId);//选集（频道）按键
	Rescourse.setImg_play_defi_bg(int imgId);//清晰度选择
	Rescourse.setImg_play_dlna_bg(int imgId);//投屏按键
	Rescourse.setPlayer_overlay_size_bg(int imgId);//全屏按键
	Rescourse.setLock_overlay_button_bg(int imgId);//锁屏按键（已锁屏）
	Rescourse.setUnLock_overlay_button_bg(int imgId);//锁屏按键（未锁屏）
	Rescourse.setImg_bright_bg(int imgId);//亮度
	Rescourse.setImg_volume_bg(int imgId);//声音
	Rescourse.setImg_bookmark_cancel_bg(int imgId);//书签取消按键
	Rescourse.setPopupwindow_bg(int colorId);//弹窗背景

4.3、修改文字样式及字体（mStyle为字体样式，mTypeface为相关字体）
--
	KeyStyle.setmTitleStyle(int mStyle);//标题
	KeyStyle.setmSysTimeStyle(int mStyle);//系统时间
	KeyStyle.setmTimeStyle(int mStyle);//当前播放时间
	KeyStyle.setmLengthStyle(int mStyle);//视频时长
	KeyStyle.setmInfoStyle(int mStyle);//提示文本
	KeyStyle.setTxt_last_timeStyle(int mStyle);//书签提示文字
	KeyStyle.setTxt_last_play_timeStyle(int mStyle);//上次播放时间
	KeyStyle.setmTitleTypeface(Typeface mTypeface);//标题
	KeyStyle.setmSysTimeTypeface(Typeface mTypeface);//系统时间
	KeyStyle.setmTimeTypeface(Typeface mTypeface);//当前播放时间
	KeyStyle.setmLengthTypeface(Typeface mTypeface);//视频时长
	KeyStyle.setmInfoTypeface(Typeface mTypeface);//提示文本
	KeyStyle.setTxt_last_timeTypeface(Typeface mTypeface);//书签提示文字
	KeyStyle.setTxt_last_play_timeTypeface(Typeface mTypeface);//上次播放时间