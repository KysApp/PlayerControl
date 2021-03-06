package com.kys.playercontrol.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.media.AudioManager;
import android.os.Build;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kys.playercontrol.customview.PopupWindows;
import com.kys.playercontrol.R;
import com.kys.playercontrol.interfaces.IPlayerControl;
import com.kys.playercontrol.interfaces.OnPlayerControlListener;
import com.kys.playercontrol.tools.CommonApi;

import org.videolan.vlc.Util;
import org.videolan.vlc.WeakHandler;

/**
 * Created by 幻云紫日 on 2016/9/1.
 */
public class PlayControl extends RelativeLayout implements IPlayerControl{
    public final static String TAG = "IJKPlayer";
    public static int STATE_VOD_LIVE = 0;      //判断是否是直播、点播，0为点播，1为直播
    public static Activity mContext;
    public int mUiVisibility = -1;
    public static int mScreenOrientation = 4;/* SCREEN_ORIENTATION_SENSOR */
    /**
     * Overlay
     */
    public static View mProgressBar;
    public static View share_progressBar;
    public static View mOverlayHeader;
    public static View mOverlayProgress;
    public static View mOverlayContent;
    public static View mOverlayPlayer;
    public static SeekBar mSeekbar;
    public static TextView mTitle;
    public static TextView mSysTime;
    public static ImageView btn_back;
    public static TextView mTime;
    public static TextView mLength;
    public static TextView mInfo;
    public boolean mEnableBrightnessGesture;
    public static boolean mDisplayRemainingTime = false;
    public static final int OVERLAY_TIMEOUT = 4000;
    public static final int OVERLAY_INFINITE = 3600000;
    public static final int FADE_OUT = 1;
    public static final int SHOW_PROGRESS = 2;
    public static final int SURFACE_SIZE = 3;
    public static final int FADE_OUT_INFO = 4;
    public static final int REQUESTED_SENSOR = 5;
    public static final int LOCK_SHOW = 7;
    public static boolean mDragging;
    public static boolean mShowing;
    public static boolean mIsLocked = false;
    public static ImageView mLock;
    public static ImageView mSize;
    public AudioManager mAudioManager;
    public int mAudioMax;
    public boolean mIsAudioOrBrightnessChanged;
    public int mSurfaceYDisplayRange;
    public float mTouchY, mTouchX, mVol;
    public boolean mIsFirstBrightnessGesture = true;
    int screenWidth;
    public static ImageView img_play_channel;
    public static ImageView img_play_dlna;
    public static ImageView img_play_favorite;
    public static ImageView img_play_share;
    public static boolean isChannelShow = false;
    public static boolean isShareShow = false;
    public static boolean isDefinShow = false;
    static PopupWindow popupChannelInfo;
    static PopupWindow popupTvInfo;
    static PopupWindow popupShareTv;
    static PopupWindow popuDefinition;
    public static ImageView player_overlay_play;
    public static LinearLayout layout_bookmark;
    public static TextView txt_last_time, txt_last_play_time;
    public static RelativeLayout layout_vol_bright;
    public static RelativeLayout layout_small_vol_bright;
    public static ImageView img_brigth, img_volume;
    public static ImageView img_play_defi;
    public static boolean isFullOrSmall;
    public static LinearLayout layout_seekbar;
    public boolean mEnableWheelbar;
    public static ImageView img_bookmark_cancel;
    public static boolean isLive = true;
    public static OnPlayerControlListener listener = null;
    public static int length = 0;
    public boolean isPlaying;

    public PlayControl(Activity context) {
        super(context);
    }

    /**
     * @param context
     * @param view1 控制层及播放器的父控件
     * @param view2 视频简介的layout
     * @param come 用于判断视频内容，0为点播，1为直播
     */
    public PlayControl(Activity context, View view1, View view2, int come) {
        super(context);
        mContext = context;
        mOverlayPlayer = view1;
        mOverlayContent = view2;
        STATE_VOD_LIVE = come;
        screenWidth = CommonApi.getScreenWidth(mContext);
        LayoutInflater.from(context).inflate(R.layout.player, this, true);
        initPlayer();
    }

    /**
     * @param context
     * @param view1 控制层及播放器的父控件
     * @param view2 视频简介的layout
     * @param isLive 判断直播回看，true为直播，false为回看
     * @param come 用于判断视频内容，0为点播，1为直播
     */
    public PlayControl(Activity context, View view1, View view2, boolean isLive, int come) {
        super(context);
        mContext = context;
        mOverlayPlayer = view1;
        mOverlayContent = view2;
        this.isLive = isLive;
        STATE_VOD_LIVE = come;
        screenWidth = CommonApi.getScreenWidth(mContext);
        LayoutInflater.from(context).inflate(R.layout.player, this, true);
        initPlayer();
        showOverlay();
    }

    @Override
    public void setOnPlayerControlListener(OnPlayerControlListener listener) {
        this.listener = listener;
    }

    /**
     * 播放控制层初始化
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void initPlayer() {
        // TODO Auto-generated method stub
        if(mOverlayContent != null) {
            mContext.setRequestedOrientation(mScreenOrientation != 100 ? mScreenOrientation
                    : GetScreenRotation.getScreenOrientation(mContext));
            if (org.videolan.vlc.Util.isICSOrLater())
                mContext.getWindow()
                        .getDecorView()
                        .findViewById(android.R.id.content)
                        .setOnSystemUiVisibilityChangeListener(
                                new OnSystemUiVisibilityChangeListener() {
                                    @Override
                                    public void onSystemUiVisibilityChange(
                                            int visibility) {
                                        if (visibility == mUiVisibility)
                                            return;
                                        if (visibility == View.SYSTEM_UI_FLAG_VISIBLE
                                                && !mShowing) {
                                            showOverlay();
                                        }
                                        mUiVisibility = visibility;
                                    }
                                });
        }
        // 100 is the value for screen_orientation_start_lock

        /** initialize Views an their Events */
        mOverlayHeader = findViewById(R.id.player_overlay_header);
        mOverlayProgress = findViewById(R.id.progress_overlay);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        int sWidth = CommonApi.getScreenWidth(mContext);
        this.setLayoutParams(new ViewGroup.LayoutParams(
                sWidth, sWidth * 9 / 16));

		/* header */
        mTitle = (TextView) findViewById(R.id.player_overlay_title);
        mTitle.setText("ssldfjslkdfj");
        mSysTime = (TextView) findViewById(R.id.player_overlay_systime);
        btn_back = (ImageView) findViewById(R.id.player_overlay_back);
        btn_back.setOnClickListener(new OnClickListioners(mContext, "mExitFullScreenListener"));

        // Position and remaining time
        mTime = (TextView) findViewById(R.id.player_overlay_time);
        mTime.setOnClickListener(new OnClickListioners(mContext, "mRemainingTimeListener"));
        mLength = (TextView) findViewById(R.id.player_overlay_length);
        mLength.setOnClickListener(new OnClickListioners(mContext, "mRemainingTimeListener"));
        // the info TextView is not on the overlay
        mInfo = (TextView) findViewById(R.id.player_overlay_info);

        mEnableBrightnessGesture = true;
        player_overlay_play = (ImageView) findViewById(R.id.player_overlay_play);
        player_overlay_play.setOnClickListener(new OnClickListioners(mContext, "mPlayPause"));
        mLock = (ImageView) findViewById(R.id.lock_overlay_button);
        mLock.setOnClickListener(new OnClickListioners(mContext, "mLockListener"));

        mSize = (ImageView) findViewById(R.id.player_overlay_size);
        mSize.setOnClickListener(new OnClickListioners(mContext, "mSizeListener"));
        img_play_favorite = (ImageView) findViewById(R.id.img_play_favorite);
        img_play_share = (ImageView) findViewById(R.id.img_play_share);
        img_play_share.setOnClickListener(new OnClickListioners(mContext, "mPlayShare"));
        img_play_channel = (ImageView) findViewById(R.id.img_play_channel);
        //img_play_channel显示选集状态，点击弹出选集弹窗
        if ((STATE_VOD_LIVE == 0)) {
            img_play_favorite.setOnClickListener(new OnClickListioners(mContext, "mFavorite"));
            Rescourse.setImg_play_channel_bg(R.drawable.live_drama);
        }
        //直播时隐藏收藏按钮，img_play_channel显示选择频道状态，点击弹出频道弹窗
        if ((STATE_VOD_LIVE == 1)) {
            img_play_favorite.setVisibility(View.GONE);
            Rescourse.setImg_play_channel_bg(R.drawable.live_channel_select);
        }
        img_play_channel.setOnClickListener(new OnClickListioners(mContext, "mPlayChannel"));

        layout_seekbar = (LinearLayout) findViewById(R.id.layout_seekbar);
        mSeekbar = (SeekBar) findViewById(R.id.player_overlay_seekbar);
        mSeekbar.setOnSeekBarChangeListener(SeekProgress.mSeekListener);

        mAudioManager = (AudioManager) mContext.getSystemService(mContext.AUDIO_SERVICE);
        mAudioMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mContext.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        layout_bookmark = (LinearLayout) findViewById(R.id.layout_bookmark);
        txt_last_time = (TextView) findViewById(R.id.txt_last_time);
        txt_last_play_time = (TextView) findViewById(R.id.txt_last_play_time);
        txt_last_play_time.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        img_bookmark_cancel = (ImageView) findViewById(R.id.img_bookmark_cancel);
        layout_vol_bright = (RelativeLayout) findViewById(R.id.layout_vol_bright);
        layout_small_vol_bright = (RelativeLayout) findViewById(R.id.layout_small_vol_bright);
        img_brigth = (ImageView) findViewById(R.id.img_brigth);
        img_volume = (ImageView) findViewById(R.id.img_volume);
        img_play_defi = (ImageView) findViewById(R.id.img_play_defi);
        img_play_defi.setOnClickListener(new OnClickListioners(mContext, "mPlayDefinition"));
        img_play_dlna = (ImageView) findViewById(R.id.img_play_dlna);
        img_play_dlna.setOnClickListener(new OnClickListioners(mContext, "mDlanShare"));
    }

    //DLNA设备展现
    public void intPopupShareTv() {
        // TODO Auto-generated method stub
        popupShareTv = new PopupWindow(mContext);
        PopupWindows.intPopupShareTv(mContext, popupShareTv, listener);
    }

    //频道弹窗
    public void intPopupChannelInfo() {
        // TODO Auto-generated method stub
        popupChannelInfo = new PopupWindow(mContext);
        PopupWindows.intPopupChannelInfo(mContext, popupChannelInfo, mContext.findViewById(R.id.img_play_channel), listener);
    }

    //全屏选集弹窗
    public void intPopupTvInfo() {
        // TODO Auto-generated method stub
        popupTvInfo = new PopupWindow(mContext);
        PopupWindows.intPopupTvInfo(mContext, popupTvInfo, mContext.findViewById(R.id.id_location), listener);
    }

    //清晰度切换
    public void intDefinition() {
        popuDefinition = new PopupWindow(mContext);
        PopupWindows.intDefinition(mContext, popuDefinition, mContext.findViewById(R.id.id_defi), listener);
    }

    public static int time = 0;
    public int mCurrentTime = 0;

    //横屏竖屏切换，根据播放内容显示不同的控件
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if(mOverlayContent == null)return;
        OverlayShow.hideOverlay(true);
        if (newConfig != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !isFullOrSmall) {
                mContext.getWindow()
                        .addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mSize.setVisibility(View.GONE);
                mOverlayContent.setVisibility(View.GONE);
                mOverlayHeader.setVisibility(View.VISIBLE);
                if(KeyShow.isImg_play_channel_show())img_play_channel.setVisibility(View.VISIBLE);
                int sWidth = CommonApi.getScreenWidth(mContext);
                int sHeight = CommonApi.getScreenHeight(mContext);
                ViewGroup.LayoutParams mLayoutParams = this.getLayoutParams();
                mLayoutParams.width = sWidth;
                mLayoutParams.height = sHeight;
                this.setLayoutParams(mLayoutParams);
                this.requestLayout();
                setPopuWindowDismiss();
                layout_small_vol_bright.setVisibility(View.GONE);
                ViewGroup.LayoutParams layoutParams = mOverlayPlayer.getLayoutParams();
                layoutParams.width = sWidth;
                layoutParams.height = sHeight;
                mOverlayPlayer.setLayoutParams(layoutParams);
                mOverlayPlayer.requestLayout();
                if (listener != null) {
                    length = listener.onVideoLength();
                    listener.isFullVolBri();
                }
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && isFullOrSmall) {
                mContext.getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                if(KeyShow.isPlayer_overlay_size_show())mSize.setVisibility(View.VISIBLE);
                mOverlayContent.setVisibility(View.VISIBLE);
                mOverlayHeader.setVisibility(View.VISIBLE);
                img_play_channel.setVisibility(View.GONE);
                int sWidth = CommonApi.getScreenWidth(mContext);
                ViewGroup.LayoutParams mLayoutParams = this.getLayoutParams();
                mLayoutParams.width = sWidth;
                mLayoutParams.height = sWidth * 9 / 16;
                this.setLayoutParams(mLayoutParams);
                this.requestLayout();
                setPopuWindowDismiss();
                layout_vol_bright.setVisibility(View.GONE);
                ViewGroup.LayoutParams layoutParams = mOverlayPlayer.getLayoutParams();
                layoutParams.width = sWidth;
                layoutParams.height = sWidth * 9 / 16;
                mOverlayPlayer.setLayoutParams(layoutParams);
                mOverlayPlayer.requestLayout();
            }
        }

        showOverlay();
    }

    /**
     * show overlay the the default timeout
     */
    public static void showOverlay() {
        OverlayShow.showOverlay(OVERLAY_TIMEOUT);
    }

    public static void hideOverlay(boolean b){
        OverlayShow.hideOverlay(b);
    }

    //所有彈窗消失
    public static void setPopuWindowDismiss(){
        if (popupTvInfo != null) {
            popupTvInfo.dismiss();
        }
        if (popupShareTv != null) {
            popupShareTv.dismiss();
        }
        if (popuDefinition != null) {
            popuDefinition.dismiss();
        }
        if (popupChannelInfo != null) {
            popupChannelInfo.dismiss();
        }
        isShareShow = false;
        isChannelShow = false;
        isDefinShow = false;
    }

    /**
     * Dim the status bar and/or navigation icons when needed on Android 3.x.
     * Hide it on Android 4.0 and later
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void dimStatusBar(boolean dim) {
        if (!org.videolan.vlc.Util.isHoneycombOrLater() || !Util.hasNavBar())
            return;
    }

    public static boolean canShowProgress() {
        if(listener != null){
            return !mDragging && mShowing && listener.canShowProgress();
        }
        return false;
    }

    public static void fadeOutInfo() {
        if (mInfo.getVisibility() == View.VISIBLE)
            mInfo.startAnimation(AnimationUtils.loadAnimation(mContext,
                    android.R.anim.fade_out));
        mInfo.setVisibility(View.INVISIBLE);
    }

    public static WeakHandler mHandler = new WeakHandler(mContext) {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case FADE_OUT:
                    OverlayShow.hideOverlay(false);
                    break;
                case SHOW_PROGRESS:
                    int pos = SeekProgress.setOverlayProgress();
                    if (canShowProgress()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                        if(listener != null)
                            listener.onCurrentPosition();
                    }
                    break;
                case SURFACE_SIZE:
                    break;
                case FADE_OUT_INFO:
                    fadeOutInfo();

                    break;
                case REQUESTED_SENSOR:
                    mIsLocked = false;
                    unlockScreen();
                    break;
                case LOCK_SHOW:
                    mLock.setVisibility(View.GONE);
                    mShowing = false;
                    break;
            }
        }
    };

    public void onBackPressed() {
        // TODO Auto-generated method stub
        if(mOverlayContent == null){
            listener.setOnBackPressed();
            return;
        }
        if (isFullOrSmall) {
            //全屏时切换至小屏播放
            switch (GetScreenRotation.getScreenRotation(mContext)) {
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    // SCREEN_ORIENTATION_REVERSE_LANDSCAPE only available since API
                    // Level 9+
                    mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    OverlayShow.hideOverlay(true);
                    Message msg = mHandler.obtainMessage(REQUESTED_SENSOR);
                    mHandler.sendMessageDelayed(msg, 2000);
                    return;

            }
        } else {
            //退出
            if(listener != null){
                listener.setOnBackPressed();
            }
        }
    }

    /**
     * @param isFavorite true为已收藏，false为未收藏
     */
    public void setFavoriteBg(boolean isFavorite) {
        if (isFavorite) {
            img_play_favorite.setImageResource(Rescourse.getImg_play_favorite_bg());
        } else {
            img_play_favorite.setImageResource(Rescourse.getImg_play_unfavorite_bg());
        }
    }

    /**
     * Show text in the info view
     *
     * @param text
     */
    public static void showInfo(String text) {
        mInfo.setVisibility(View.VISIBLE);
        mInfo.setText(text);
        mHandler.removeMessages(FADE_OUT_INFO);
    }

    /**
     * hide the info view with "delay" milliseconds delay
     *
     * @param delay
     */
    public static void hideInfo(int delay) {
        mHandler.sendEmptyMessageDelayed(FADE_OUT_INFO, delay);
    }

    /**
     * hide the info view
     */
    public static void hideInfo() {
        hideInfo(0);
    }

    /**
     * Lock screen rotation
     */
    public void lockScreen() {
        if (mScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            mContext.setRequestedOrientation(GetScreenRotation.getScreenOrientation(mContext));
        // showInfo(R.string.locked, 1000);
        mLock.setImageResource(Rescourse.getLock_overlay_button_bg());
        mOverlayHeader.setVisibility(View.INVISIBLE);
        mOverlayProgress.setVisibility(View.INVISIBLE);
        layout_vol_bright.setVisibility(View.GONE);
        layout_small_vol_bright.setVisibility(View.GONE);
        setPopuWindowDismiss();
        isChannelShow = false;
        img_play_channel.setImageResource(Rescourse.getImg_play_channel_bg());
        showOverlay();
    }

    /**
     * Remove screen lock
     */
    public static void unlockScreen() {
        if (mScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        // showInfo(R.string.unlocked, 1000);
        mLock.setImageResource(Rescourse.getUnlock_overlay_button_bg());
        mShowing = false;
        showOverlay();
    }

    /**
     * Show text in the info view for "duration" milliseconds
     *
     * @param text
     * @param duration
     */
    public void showInfo(String text, int duration) {
        mInfo.setVisibility(View.VISIBLE);
        mInfo.setText(text);
        mHandler.removeMessages(FADE_OUT_INFO);
        mHandler.sendEmptyMessageDelayed(FADE_OUT_INFO, duration);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        showOverlay();
        return true;
    }

    /** 手势监听 */
    /**
     * show/hide the overlay
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOverlayContent != null && mOverlayContent.getVisibility() == View.VISIBLE
                && event.getY() > screenWidth * 9 / 16) {
            return false;
        }

        DisplayMetrics screen = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(screen);

        if (mSurfaceYDisplayRange == 0)
            mSurfaceYDisplayRange = Math.min(screen.widthPixels,
                    screen.heightPixels);

        float y_changed = event.getRawY() - mTouchY;
        float x_changed = event.getRawX() - mTouchX;

        // coef is the gradient's move to determine a neutral zone
        float coef = Math.abs(y_changed / x_changed);
        float xgesturesize = ((x_changed / screen.xdpi) * 2.54f);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (mIsLocked && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mLockShow(OVERLAY_TIMEOUT);
                    return false;
                }
                // Audio
                mTouchY = event.getRawY();
                mVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mIsAudioOrBrightnessChanged = false;
                // Seek
                mTouchX = event.getRawX();
                if(listener != null) {
                    mCurrentTime = listener.onTouchCurrentPosition();
                    listener.onState();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsLocked && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    return false;
                }
                // No volume/brightness action if coef < 2
                if (coef > 2) {
                    // Volume (Up or Down - Right side)
                    if (!mEnableBrightnessGesture
                            || mTouchX > (screen.widthPixels / 2)) {
                        doVolumeTouch(y_changed);
                    }
                    // Brightness (Up or Down - Left side)
                    if (mEnableBrightnessGesture
                            && mTouchX < (screen.widthPixels / 2)) {
                        doBrightnessTouch(y_changed);
                    }
                    // Extend the overlay for a little while, so that it doesn't
                    // disappear on the user if more adjustment is needed. This
                    // is because on devices with soft navigation (e.g. Galaxy
                    // Nexus), gestures can't be made without activating the UI.
                    if (Util.hasNavBar())
                        showOverlay();
                }
                // Seek (Right or Left move)
                if (!isLive || STATE_VOD_LIVE == 0) {
                    doSeekTouch(mCurrentTime, coef, xgesturesize, false);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mIsLocked && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    return false;
                }
                // Audio or Brightness
                if (!mIsAudioOrBrightnessChanged) {
                    if (!mShowing) {
                        showOverlay();
                    } else {
                        OverlayShow.hideOverlay(true);
                    }
                }
                if (!isLive || STATE_VOD_LIVE == 0) {
                    doSeekTouch(mCurrentTime, coef, xgesturesize, true);
                }
                break;
        }
        return true;
    }

    public void mLockShow(int timeout) {
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        if (!mShowing) {
            if(KeyShow.isLock_overlay_button_show())mLock.setVisibility(View.VISIBLE);
            mShowing = true;
            Message msg = mHandler.obtainMessage(LOCK_SHOW);
            if (timeout != 0) {
                mHandler.removeMessages(LOCK_SHOW);
                mHandler.sendMessageDelayed(msg, timeout);
            }
        } else {
            mLock.setVisibility(View.INVISIBLE);
            mShowing = false;
        }
    }

    public void doSeekTouch(int mCurrentTime, float coef, float gesturesize,
                            boolean seek) {
        // No seek action if coef > 0.5 and gesturesize < 1cm
        if (mEnableWheelbar || coef > 0.5 || Math.abs(gesturesize) < 1)
            return;

        // Always show seekbar when searching
        if (!mShowing)
            showOverlay();

        // Size of the jump, 30 minutes max (1800000), with a bi-cubic
        // progression, for a 8cm gesture
        int jump = (int) (Math.signum(gesturesize) * ((1800000 * Math.pow(
                (gesturesize / 8), 4)) + 3000));

        // Adjust the jump
        if ((jump > 0) && ((mCurrentTime + jump) > length))
            jump = (int) (length - mCurrentTime);
        if ((jump < 0) && ((mCurrentTime + jump) < 0))
            jump = (int) -mCurrentTime;

        // Jump !
        showInfo(
                String.format("%s (%s)", Util.millisToString(mCurrentTime + jump),
                        Util.millisToString(length)), 1000);
        mTime.setText(Util.millisToString(mCurrentTime + jump));
        if (seek)
            if (listener != null) {
                listener.onSeekTo(mCurrentTime + jump);
            }
        time = SeekProgress.setOverlayProgress();
        if (!mShowing) {
            showOverlay();
        } else {
            OverlayShow.hideOverlay(true);
        }
    }

    //音量控制
    public void doVolumeTouch(float y_changed) {
        int vol = BrightVolTouch.doVolumeTouch(y_changed, mSurfaceYDisplayRange, mAudioMax, mVol, mAudioManager);
        if(vol != 0 && vol > 0) {
            mIsAudioOrBrightnessChanged = true;
            showInfo(
                    mContext.getString(R.string.volume) + '\u00A0'
                            + Integer.toString(vol), 1000);
        }
    }

    public void doBrightnessTouch(float y_changed) {
        mIsFirstBrightnessGesture = false;
        float screenBrightness = BrightVolTouch.doBrightnessTouch(mContext, y_changed, mIsFirstBrightnessGesture, mSurfaceYDisplayRange);
        mIsFirstBrightnessGesture = true;
        showInfo(
                mContext.getString(R.string.brightness) + '\u00A0'
                        + Math.round(screenBrightness * 15), 1000);
    }

    @Override
    public void setState(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (isPlaying) {
            player_overlay_play.setImageResource(Rescourse.getPlayer_overlay_pause_bg());
        } else {
            player_overlay_play.setImageResource(Rescourse.getPlayer_overlay_play_bg());
        }
    }

    @Override
    public void setSmallVolBri(boolean isSmallVolBri) {
        if (isSmallVolBri) {
            layout_small_vol_bright.setVisibility(View.GONE);
        } else {
            if(KeyShow.isImg_bright_volume_show())layout_small_vol_bright.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setFullVolBri(boolean isFullVolBri) {
        if (isFullVolBri) {
            layout_vol_bright.setVisibility(View.GONE);
        } else {
            if(KeyShow.isImg_bright_volume_show())layout_vol_bright.setVisibility(View.VISIBLE);
            img_brigth.setImageResource(Rescourse.getImg_bright_bg());
            img_volume.setImageResource(Rescourse.getImg_volume_bg());
        }
    }

    @Override
    public void setDlnaRefresh(boolean show) {
        PopupWindows.setDlnaRefresh(show);
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setDefinition(String string) {
        img_play_defi.setImageResource(Rescourse.getImg_play_defi_bg());
    }

    @Override
    public void setBookMark(final int position) {
        layout_bookmark.setVisibility(View.VISIBLE);
        txt_last_time.setText(getResources().getString(R.string.txt_last_play_time)
                + Util.millisToString(position)
                + "，点击");
        txt_last_play_time.setOnClickListener(new OnClickListioners(mContext, "mTurnBookMark", position));
        img_bookmark_cancel.setOnClickListener(new OnClickListioners(mContext, "mBookMarkCancel"));
    }

    @Override
    public void setFavoriteShow(boolean show) {
        if(show) {
            if(KeyShow.isImg_play_favorite_show())img_play_favorite.setVisibility(View.VISIBLE);
        }else{
            img_play_favorite.setVisibility(View.GONE);
        }
    }

    @Override
    public void setDlnaShow(boolean show) {
        if(show) {
            if(KeyShow.isImg_play_dlna_show())img_play_dlna.setVisibility(View.VISIBLE);
        }else{
            img_play_dlna.setVisibility(View.GONE);
        }
    }

    @Override
    public void setShareShow(boolean show) {
        if(show) {
            if(KeyShow.isImg_play_share_show())img_play_share.setVisibility(View.VISIBLE);
        }else{
            img_play_share.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSeekable(boolean isSeekable) {

    }

    @Override
    public void onSeekTo(int position) {
        time = position;
    }

    @Override
    public void setVideoLength(int length) {
        if(this.length == 0 && length > 0) {
            this.length = length;
            mSeekbar.setMax(length);
            time = SeekProgress.setOverlayProgress();
        } else if (length == 0){
            this.length = length;
        }
    }

    @Override
    public void setCurrentPosition(int position) {
        time = position;
    }

    @Override
    public void setTouchCurrentPosition(int position) {
        mCurrentTime = position;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

}