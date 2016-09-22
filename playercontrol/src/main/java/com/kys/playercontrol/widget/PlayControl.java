package com.kys.playercontrol.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Message;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kys.playercontrol.R;
import com.kys.playercontrol.interfaces.IPlayerControl;
import com.kys.playercontrol.interfaces.OnPlayerControlListener;
import com.kys.playercontrol.tools.CommonApi;

import org.videolan.vlc.Util;
import org.videolan.vlc.WeakHandler;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by 幻云紫日 on 2016/9/1.
 */
public class PlayControl extends RelativeLayout implements IPlayerControl{
    private final static String TAG = "IJKPlayer";
    private static int STATE_VOD_LIVE = 0;      //判断是否是直播、点播，0为点播，1为直播
    private Activity mContext;
    private int mUiVisibility = -1;
    private int mScreenOrientation = 4;/* SCREEN_ORIENTATION_SENSOR */
    /**
     * Overlay
     */
    public View mProgressBar, share_progressBar;
    private View mOverlayHeader;
    private View mOverlayProgress;
    private View mOverlayInterface;
    private View mOverlayContent;
    private View mOverlayPlayer;
    private View mOverlayBuffer;
    private View mOverlayVolume;
    private SeekBar mSeekbar, mVolumeSeekbar;
    private TextView mTitle;
    private TextView mSysTime;
    private TextView mBattery;
    private ImageView btn_back;
    private TextView mTime;
    private TextView mLength;
    private TextView mInfo;
    private TextView txt_select, txt_refresh;
    private boolean mEnableBrightnessGesture;
    private boolean mDisplayRemainingTime = false;
    private static final int OVERLAY_TIMEOUT = 4000;
    private static final int OVERLAY_INFINITE = 3600000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int SURFACE_SIZE = 3;
    private static final int FADE_OUT_INFO = 4;
    private static final int REQUESTED_SENSOR = 5;
    private static final int LOCK_SHOW = 7;
    private boolean mDragging;
    private boolean mShowing;
    private boolean mIsLocked = false;
    private ImageView mLock;
    private ImageView mSize;
    // Volume
    private AudioManager mAudioManager;
    private int mAudioMax;

    // Volume Or Brightness
    private boolean mIsAudioOrBrightnessChanged;
    private int mSurfaceYDisplayRange;
    private float mTouchY, mTouchX, mVol;

    // Brightness
    private boolean mIsFirstBrightnessGesture = true;

    int screenWidth;
    public ImageView img_play_channel, img_play_dlna, img_play_favorite, img_play_share;
    private boolean isChannelShow = false;
    private boolean isShareShow = false;
    private boolean isDefinShow = false;
    PopupWindow popupChannelInfo, popupTvInfo, popupShareTv, popuDefinition;
    private ImageView player_overlay_play;
    private boolean StartFromPause = false;//当用户从其它界面回来时，该参数为true
    private ListView mDmrLv, list_channel;
    private LinearLayout layout_refresh;
    private LinearLayout layout_bookmark;
    private TextView txt_last_time, txt_last_play_time;
    private RelativeLayout layout_vol_bright, layout_small_vol_bright;
    private ImageView img_brigth, img_volume;
    private String definition = "1";
    public ImageView img_play_defi;
    private boolean isFullOrSmall;
    private LinearLayout layout_seekbar;
    private boolean mEnableWheelbar;
    private ImageView img_bookmark_cancel;
    public boolean isLive = true;
    private OnPlayerControlListener listener = null;
    private int length = 0;
    private boolean isPlaying;

    public PlayControl(Activity context) {
        super(context);
        mContext = context;
        screenWidth = CommonApi.getScreenWidth(mContext);
        LayoutInflater.from(context).inflate(R.layout.player, this, true);
        initPlayer();
    }

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

    public PlayControl(Activity context, View view, boolean isLive, int come) {
        super(context);
        mContext = context;
        mOverlayContent = view;
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

    //播放控制层初始化
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initPlayer() {
        // TODO Auto-generated method stub
        mContext.setRequestedOrientation(mScreenOrientation != 100 ? mScreenOrientation
                : getScreenOrientation());
        // 100 is the value for screen_orientation_start_lock
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

        /** initialize Views an their Events */
        mOverlayHeader = findViewById(R.id.player_overlay_header);
        mOverlayProgress = findViewById(R.id.progress_overlay);
        mOverlayInterface = findViewById(R.id.interface_overlay);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        int sWidth = CommonApi.getScreenWidth(mContext);
        this.setLayoutParams(new ViewGroup.LayoutParams(
                sWidth, sWidth * 9 / 16));
        mOverlayBuffer = findViewById(R.id.player_overlay_buffer);

        mOverlayVolume = findViewById(R.id.player_overlay_volume);
        mOverlayVolume.setVisibility(View.GONE);

		/* header */
        mTitle = (TextView) findViewById(R.id.player_overlay_title);
        mTitle.setGravity(Gravity.CENTER);
        mSysTime = (TextView) findViewById(R.id.player_overlay_systime);
        btn_back = (ImageView) findViewById(R.id.player_overlay_back);
        btn_back.setOnClickListener(new OnClickListioners("mExitFullScreenListener"));
        // mOverlayHeader.setVisibility(View.GONE);

        // Position and remaining time
        mTime = (TextView) findViewById(R.id.player_overlay_time);
        mTime.setOnClickListener(new OnClickListioners("mRemainingTimeListener"));
        mLength = (TextView) findViewById(R.id.player_overlay_length);
        mLength.setOnClickListener(new OnClickListioners("mRemainingTimeListener"));
        mTime.setVisibility(View.GONE);
        mLength.setVisibility(View.GONE);
        // the info TextView is not on the overlay
        mInfo = (TextView) findViewById(R.id.player_overlay_info);

        mEnableBrightnessGesture = true;
        player_overlay_play = (ImageView) findViewById(R.id.player_overlay_play);
        player_overlay_play.setOnClickListener(new OnClickListioners("mPlayPause"));
        mLock = (ImageView) findViewById(R.id.lock_overlay_button);
        mLock.setOnClickListener(new OnClickListioners("mLockListener"));

        mSize = (ImageView) findViewById(R.id.player_overlay_size);
        mSize.setOnClickListener(new OnClickListioners("mSizeListener"));
        img_play_favorite = (ImageView) findViewById(R.id.img_play_favorite);
        img_play_share = (ImageView) findViewById(R.id.img_play_share);
        img_play_share.setOnClickListener(new OnClickListioners("mPlayShare"));
        img_play_channel = (ImageView) findViewById(R.id.img_play_channel);
        //img_play_channel显示选集状态，点击弹出选集弹窗
        if ((STATE_VOD_LIVE == 0)) {
            img_play_favorite.setOnClickListener(new OnClickListioners("mFavorite"));
            img_play_channel.setImageResource(R.drawable.live_drama);
        }
        //直播时隐藏收藏按钮，img_play_channel显示选择频道状态，点击弹出频道弹窗
        if ((STATE_VOD_LIVE == 1)) {
            img_play_favorite.setVisibility(View.GONE);
        }
        img_play_channel.setOnClickListener(new OnClickListioners("mPlayChannel"));

        layout_seekbar = (LinearLayout) findViewById(R.id.layout_seekbar);
        mSeekbar = (SeekBar) findViewById(R.id.player_overlay_seekbar);
        mSeekbar.setVisibility(View.GONE);
        mSeekbar.setOnSeekBarChangeListener(mSeekListener);
        mVolumeSeekbar = (SeekBar) findViewById(R.id.volume_seekbar);
        mVolumeSeekbar.setOnSeekBarChangeListener(mVolumeSeekListener);

        mAudioManager = (AudioManager) mContext.getSystemService(mContext.AUDIO_SERVICE);
        mAudioMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mVolumeSeekbar
                .setProgress((int) (mVol * mVolumeSeekbar.getMax() / mAudioMax));

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
        img_play_defi.setOnClickListener(new OnClickListioners("mPlayDefinition"));
        img_play_dlna = (ImageView) findViewById(R.id.img_play_dlna);
        img_play_dlna.setOnClickListener(new OnClickListioners("mDlanShare"));
    }

    //DLNA设备展现
    private void intPopupShareTv() {
        // TODO Auto-generated method stub
        View popupView = LayoutInflater.from(mContext).inflate(
                R.layout.popup_share_tv, null);
        txt_select = (TextView) popupView.findViewById(R.id.txt_select);
        txt_select.setText(getResources().getString(R.string.select_dlna));
        txt_refresh = (TextView) popupView.findViewById(R.id.txt_refresh);
        layout_refresh = (LinearLayout) popupView.findViewById(R.id.layout_refresh);
        layout_refresh.setOnClickListener(new OnClickListioners("mDlanRefresh"));
        share_progressBar = popupView.findViewById(R.id.share_progressBar);
        //重新搜索设备
        popupView.setFocusableInTouchMode(true);
        popupShareTv = new PopupWindow(popupView, (int) getResources().getDimension(R.dimen.play_share_tv_width),
                (int) getResources().getDimension(R.dimen.play_share_tv_height));
        popupShareTv.setFocusable(true);
        mDmrLv = (ListView) popupView.findViewById(R.id.mListView);
        popupShareTv.setBackgroundDrawable(new BitmapDrawable());
        popupShareTv.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        if(listener != null)
            listener.setDlna(mDmrLv);
    }

    //频道弹窗
    private void intPopupChannelInfo() {
        // TODO Auto-generated method stub
        View popupView = LayoutInflater.from(mContext).inflate(
                R.layout.popup_play_model, null);
        int screenWidth = CommonApi.getScreenWidth(mContext);
        int screenHeight = CommonApi.getScreenHeight(mContext);
        popupChannelInfo = new PopupWindow(popupView, screenWidth / 3,
                screenHeight / 3 * 2);
        popupChannelInfo.setFocusable(true);
        popupChannelInfo.setBackgroundDrawable(new BitmapDrawable());

        list_channel = (ListView) popupView.findViewById(R.id.list);
        popupChannelInfo.showAsDropDown(mContext
                .findViewById(R.id.img_play_channel));
        if(listener != null)
            listener.setChannelList(list_channel);
    }

    //全屏选集弹窗
    private void intPopupTvInfo() {
        // TODO Auto-generated method stub
        View popupView = LayoutInflater.from(mContext).inflate(
                R.layout.popup_play_tv, null);
        TextView txt_select = (TextView) popupView.findViewById(R.id.txt_select);
        txt_select.setText(getResources().getString(R.string.select_series));
        int sWidth = CommonApi.getScreenWidth(mContext);
        int sHeight = CommonApi.getScreenHeight(mContext);
        popupTvInfo = new PopupWindow(popupView, sWidth / 4,
                sHeight / 3 * 2);
        popupTvInfo.setFocusable(true);
        GridView mGridView = (GridView) popupView.findViewById(R.id.mGridView);
        popupTvInfo.setBackgroundDrawable(new BitmapDrawable());
        popupTvInfo.showAsDropDown(mContext
                .findViewById(R.id.id_location));
        if(listener != null)
            listener.setSeries(mGridView);
    }

    //清晰度切换
    private void intDefinition() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.popup_play_definition, null);
        ListView mListView = (ListView) mView.findViewById(R.id.list);
        int popuWidth = getResources().getDimensionPixelOffset(R.dimen.width_61);
        int popuHeight = getResources().getDimensionPixelOffset(R.dimen.width_30) * 3;
        popuDefinition = new PopupWindow(mView, popuWidth,
                popuHeight);
        popuDefinition.setFocusable(true);
        popuDefinition.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        View id_defi = mContext.findViewById(R.id.id_defi);
        id_defi.getLocationOnScreen(location);
        popuDefinition.showAtLocation(id_defi, Gravity.NO_GRAVITY,
                location[0] - getResources().getDimensionPixelOffset(R.dimen.width_8),
                location[1] - popuDefinition.getHeight());
        if(listener != null)
            listener.setDefinition(mListView);
    }

    private int time = 0;
    private int mCurrentTime = 0;

    @SuppressWarnings("deprecation")
    private int getScreenRotation() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO /*
                                                                 * Android 2.2
																 * has
																 * getRotation
																 */) {
            try {
                Method m = display.getClass().getDeclaredMethod("getRotation");
                return (Integer) m.invoke(display);
            } catch (Exception e) {
                return Surface.ROTATION_0;
            }
        } else {
            return display.getOrientation();
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private int getScreenOrientation() {
        switch (getScreenRotation()) {
            case Surface.ROTATION_0:
                return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            case Surface.ROTATION_90:
                return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            case Surface.ROTATION_180:
                // SCREEN_ORIENTATION_REVERSE_PORTRAIT only available since API
                // Level 9+
                return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            case Surface.ROTATION_270:
                // SCREEN_ORIENTATION_REVERSE_LANDSCAPE only available since API
                // Level 9+
                return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            default:
                return 0;
        }
    }

    //横屏竖屏切换，根据播放内容显示不同的控件
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if (newConfig != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !isFullOrSmall) {
                mContext.getWindow()
                        .addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mSize.setVisibility(View.GONE);
                player_overlay_play.setVisibility(View.GONE);
                // img_play_model.setVisibility(View.VISIBLE);
                img_play_channel.setVisibility(View.VISIBLE);
                mOverlayHeader.setVisibility(View.VISIBLE);
                mOverlayContent.setVisibility(View.GONE);
                mOverlayVolume.setVisibility(View.GONE);
                mLock.setVisibility(View.VISIBLE);
                if(STATE_VOD_LIVE == 1) {
                    if (isLive) {
                        mSeekbar.setVisibility(View.INVISIBLE);
                        mTime.setVisibility(View.INVISIBLE);
                        mLength.setVisibility(View.INVISIBLE);
                        player_overlay_play.setVisibility(View.INVISIBLE);
                    } else {
                        mSeekbar.setVisibility(View.VISIBLE);
                        mTime.setVisibility(View.VISIBLE);
                        mLength.setVisibility(View.VISIBLE);
                        player_overlay_play.setVisibility(View.VISIBLE);
                    }
                }
                if(STATE_VOD_LIVE == 0) {
                    mSeekbar.setVisibility(View.VISIBLE);
                    mTime.setVisibility(View.VISIBLE);
                    mLength.setVisibility(View.VISIBLE);
                    player_overlay_play.setVisibility(View.VISIBLE);
                }
                int sWidth = CommonApi.getScreenWidth(mContext);
                int sHeight = CommonApi.getScreenHeight(mContext);
                ViewGroup.LayoutParams mLayoutParams = this.getLayoutParams();
                mLayoutParams.width = sWidth;
                mLayoutParams.height = sHeight;
                this.setLayoutParams(mLayoutParams);
                this.requestLayout();
                setPopuWindowDismiss();
                layout_small_vol_bright.setVisibility(View.GONE);
                isShareShow = false;
                isChannelShow = false;
                isDefinShow = false;
                isFullOrSmall = true;
                ViewGroup.LayoutParams layoutParams = mOverlayPlayer.getLayoutParams();
                layoutParams.width = sWidth;
                layoutParams.height = sHeight;
                mOverlayPlayer.setLayoutParams(layoutParams);
                mOverlayPlayer.requestLayout();
                if (listener != null) {
                    listener.onVideoLength();
                    listener.isFullVolBri();
                }
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && isFullOrSmall) {
                mContext.getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mSize.setVisibility(View.VISIBLE);
                player_overlay_play.setVisibility(View.VISIBLE);
                // img_play_model.setVisibility(View.GONE);
                img_play_channel.setVisibility(View.GONE);
                mOverlayHeader.setVisibility(View.VISIBLE);
                mOverlayContent.setVisibility(View.VISIBLE);
                mOverlayVolume.setVisibility(View.GONE);
                mLock.setVisibility(View.GONE);
                if(STATE_VOD_LIVE == 1) {
                    if (isLive) {
                        mSeekbar.setVisibility(View.INVISIBLE);
                        mTime.setVisibility(View.INVISIBLE);
                        mLength.setVisibility(View.INVISIBLE);
                        player_overlay_play.setVisibility(View.INVISIBLE);
                    } else {
                        mSeekbar.setVisibility(View.INVISIBLE);
                        mTime.setVisibility(View.INVISIBLE);
                        mLength.setVisibility(View.INVISIBLE);
                        player_overlay_play.setVisibility(View.VISIBLE);
                    }
                }
                if(STATE_VOD_LIVE == 0) {
                    mSeekbar.setVisibility(View.VISIBLE);
                    mTime.setVisibility(View.VISIBLE);
                    mLength.setVisibility(View.VISIBLE);
                    player_overlay_play.setVisibility(View.VISIBLE);
                }
                int sWidth = CommonApi.getScreenWidth(mContext);
                ViewGroup.LayoutParams mLayoutParams = this.getLayoutParams();
                mLayoutParams.width = sWidth;
                mLayoutParams.height = sWidth * 9 / 16;
                this.setLayoutParams(mLayoutParams);
                this.requestLayout();
                setPopuWindowDismiss();
                layout_vol_bright.setVisibility(View.GONE);
                isShareShow = false;
                isChannelShow = false;
                isDefinShow = false;
                isFullOrSmall = false;
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
    public void showOverlay() {
        showOverlay(OVERLAY_TIMEOUT);
    }

    /**
     * show overlay
     */
    private void showOverlay(int timeout) {
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        if (!mShowing) {
            mShowing = true;
            mOverlayInterface.setVisibility(View.VISIBLE);
            dimStatusBar(false);
            switch (getScreenRotation()) {
                case Surface.ROTATION_0:
                    mOverlayHeader.setVisibility(View.VISIBLE);
                    mLock.setVisibility(View.GONE);
                    if (STATE_VOD_LIVE == 1){
                        if (isLive) {
                            mSeekbar.setVisibility(View.INVISIBLE);
                            mTime.setVisibility(View.INVISIBLE);
                            mLength.setVisibility(View.INVISIBLE);
                            player_overlay_play.setVisibility(View.INVISIBLE);
                        } else {

                            mSeekbar.setVisibility(View.INVISIBLE);
                            mTime.setVisibility(View.INVISIBLE);
                            mLength.setVisibility(View.INVISIBLE);
                            player_overlay_play.setVisibility(View.VISIBLE);
                        }
                    }
                    mOverlayProgress.setVisibility(View.VISIBLE);
                case Surface.ROTATION_180:
                    mOverlayHeader.setVisibility(View.VISIBLE);
                    mLock.setVisibility(View.GONE);
                    if ((STATE_VOD_LIVE == 1)) {
                        if (isLive) {
                            mSeekbar.setVisibility(View.INVISIBLE);
                            mTime.setVisibility(View.INVISIBLE);
                            mLength.setVisibility(View.INVISIBLE);
                            player_overlay_play.setVisibility(View.INVISIBLE);
                        } else {
                            mSeekbar.setVisibility(View.INVISIBLE);
                            mTime.setVisibility(View.INVISIBLE);
                            mLength.setVisibility(View.INVISIBLE);
                            player_overlay_play.setVisibility(View.VISIBLE);
                        }
                    }
                    if ((STATE_VOD_LIVE == 0)) {
                        img_play_channel.setVisibility(View.GONE);
                        mOverlayProgress.setVisibility(View.VISIBLE);
                        mSeekbar.setVisibility(View.GONE);
                        mTime.setVisibility(View.GONE);
                        mLength.setVisibility(View.GONE);
                        mOverlayHeader.setVisibility(View.VISIBLE);
                        player_overlay_play.setVisibility(View.VISIBLE);
                    }
                    break;
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    if (mIsLocked) {
                        img_play_channel.setVisibility(View.VISIBLE);
                        mLength.setVisibility(View.VISIBLE);
                        mTime.setVisibility(View.VISIBLE);
                        mSeekbar.setVisibility(View.VISIBLE);
                        mTime.setVisibility(View.VISIBLE);
                        mLength.setVisibility(View.VISIBLE);
                        mOverlayHeader.setVisibility(View.VISIBLE);
                        mOverlayProgress.setVisibility(View.VISIBLE);
                        player_overlay_play.setVisibility(View.VISIBLE);
                    } else {
                        img_play_channel.setVisibility(View.VISIBLE);
                        mOverlayHeader.setVisibility(View.VISIBLE);
                        mOverlayProgress.setVisibility(View.VISIBLE);
                        if (STATE_VOD_LIVE == 1) {
                            if (isLive) {
                                mSeekbar.setVisibility(View.INVISIBLE);
                                mTime.setVisibility(View.INVISIBLE);
                                mLength.setVisibility(View.INVISIBLE);
                                player_overlay_play.setVisibility(View.INVISIBLE);
                            } else {
                                mSeekbar.setVisibility(View.VISIBLE);
                                mTime.setVisibility(View.VISIBLE);
                                mLength.setVisibility(View.VISIBLE);
                                player_overlay_play.setVisibility(View.VISIBLE);
                            }
                        }
                        if (STATE_VOD_LIVE == 0) {
                            mTime.setVisibility(View.VISIBLE);
                            mSeekbar.setVisibility(View.VISIBLE);
                            mTime.setVisibility(View.VISIBLE);
                            mLength.setVisibility(View.VISIBLE);
                            player_overlay_play.setVisibility(View.VISIBLE);
                        }
                    }
                    mLock.setVisibility(View.VISIBLE);
                    break;

            }

        }
        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    /**
     * hider overlay
     */
    public void hideOverlay(boolean fromUser) {

        if (mShowing) {
            mHandler.removeMessages(SHOW_PROGRESS);
            if (!fromUser && !mIsLocked) {
                mOverlayProgress.startAnimation(AnimationUtils.loadAnimation(
                        mContext, android.R.anim.fade_out));
                mOverlayInterface.startAnimation(AnimationUtils.loadAnimation(
                        mContext, android.R.anim.fade_out));
            }
            mLock.setVisibility(View.INVISIBLE);
            mOverlayHeader.setVisibility(View.INVISIBLE);
            mOverlayProgress.setVisibility(View.INVISIBLE);
            mOverlayInterface.setVisibility(View.INVISIBLE);
            layout_vol_bright.setVisibility(View.GONE);
            layout_small_vol_bright.setVisibility(View.GONE);
            mShowing = false;
            dimStatusBar(false);
        }
        setPopuWindowDismiss();
        isChannelShow = false;
        if ((STATE_VOD_LIVE == 1)){
            img_play_channel.setImageResource(R.drawable.live_channel_select);
        }
        if ((STATE_VOD_LIVE == 0)){
            img_play_channel.setImageResource(R.drawable.live_drama);
        }
    }


    //所有彈窗消失
    public void setPopuWindowDismiss(){
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
    private void dimStatusBar(boolean dim) {
        if (!org.videolan.vlc.Util.isHoneycombOrLater() || !Util.hasNavBar())
            return;
    }

    /**
     * update the overlay播放器进度显示
     */
    private int setOverlayProgress() {
        // Update all view elements
        if (listener != null) {
            listener.onCurrentPosition();
            listener.onVideoLength();
        }
        mSeekbar.setMax(length);
        mSeekbar.setProgress(time);
        mSysTime.setText(DateFormat.getTimeFormat(mContext).format(
                new Date(System.currentTimeMillis())));
        mTime.setText(Util.millisToString(time));
        mLength.setText(mDisplayRemainingTime && length > 0 ? "- "
                + Util.millisToString(length - time) : Util
                .millisToString(length));
        return time;
    }

    private boolean canShowProgress() {
        if(listener != null){
            return !mDragging && mShowing && listener.canShowProgress();
        }
        return false;
    }

    private void fadeOutInfo() {
        if (mInfo.getVisibility() == View.VISIBLE)
            mInfo.startAnimation(AnimationUtils.loadAnimation(mContext,
                    android.R.anim.fade_out));
        mInfo.setVisibility(View.INVISIBLE);
    }

    private WeakHandler mHandler = new WeakHandler(mContext) {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case FADE_OUT:
                    hideOverlay(false);
                    break;
                case SHOW_PROGRESS:
                    int pos = setOverlayProgress();
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
         if (isFullOrSmall) {
             //全屏时切换至小屏播放
            switch (getScreenRotation()) {
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    // SCREEN_ORIENTATION_REVERSE_LANDSCAPE only available since API
                    // Level 9+
                    mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    hideOverlay(true);
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

    public void setFavoriteBg(boolean isFavorite) {
        if (isFavorite) {
            img_play_favorite.setImageResource(R.drawable.live_precolle);
        } else {
            img_play_favorite.setImageResource(R.drawable.live_collection);
        }
    }

    /**
     * Show text in the info view
     *
     * @param text
     */
    private void showInfo(String text) {
        mInfo.setVisibility(View.VISIBLE);
        mInfo.setText(text);
        mHandler.removeMessages(FADE_OUT_INFO);
    }

    /**
     * hide the info view with "delay" milliseconds delay
     *
     * @param delay
     */
    private void hideInfo(int delay) {
        mHandler.sendEmptyMessageDelayed(FADE_OUT_INFO, delay);
    }

    /**
     * hide the info view
     */
    private void hideInfo() {
        hideInfo(0);
    }

    /**
     * Lock screen rotation
     */
    private void lockScreen() {
        if (mScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            mContext.setRequestedOrientation(getScreenOrientation());
        // showInfo(R.string.locked, 1000);
        mLock.setImageResource(R.drawable.live_lockx);
        mOverlayHeader.setVisibility(View.INVISIBLE);
        mOverlayProgress.setVisibility(View.INVISIBLE);
        mOverlayInterface.setVisibility(View.INVISIBLE);
        layout_vol_bright.setVisibility(View.GONE);
        layout_small_vol_bright.setVisibility(View.GONE);
        setPopuWindowDismiss();
        isChannelShow = false;
        img_play_channel.setImageResource(R.drawable.live_drama);
        showOverlay();
    }

    /**
     * Remove screen lock
     */
    private void unlockScreen() {
        if (mScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        // showInfo(R.string.unlocked, 1000);
        mLock.setImageResource(R.drawable.live_unlock);
        mShowing = false;
        showOverlay();
    }

    /**
     * Show text in the info view for "duration" milliseconds
     *
     * @param text
     * @param duration
     */
    private void showInfo(String text, int duration) {
        mInfo.setVisibility(View.VISIBLE);
        mInfo.setText(text);
        mHandler.removeMessages(FADE_OUT_INFO);
        mHandler.sendEmptyMessageDelayed(FADE_OUT_INFO, duration);
    }


    /**
     * handle changes of the seekbar (volume)
     */
    private final SeekBar.OnSeekBarChangeListener mVolumeSeekListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (fromUser) {
                int delta = (int) (progress * mAudioMax / seekBar.getMax());
                int vol = (int) Math.min(Math.max(delta, 0), mAudioMax);
                mAudioManager
                        .setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
                mIsAudioOrBrightnessChanged = true;
                showInfo(
                        mContext.getString(R.string.volume) + '\u00A0'
                                + Integer.toString(vol), 1000);

            }

        }
    };

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
        if (mOverlayContent.getVisibility() == View.VISIBLE
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
                mVolumeSeekbar
                        .setProgress((int) (mVol * mVolumeSeekbar.getMax() / mAudioMax));
                mIsAudioOrBrightnessChanged = false;
                // Seek
                mTouchX = event.getRawX();
                if(listener != null) {
                    listener.onTouchCurrentPosition();
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
                        hideOverlay(true);
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
            mLock.setVisibility(View.VISIBLE);
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
        setOverlayProgress();
        if (!mShowing) {
            showOverlay();
        } else {
            hideOverlay(true);
        }
    }

    //音量控制
    public void doVolumeTouch(float y_changed) {
        int delta = -(int) ((y_changed / mSurfaceYDisplayRange) * mAudioMax);
        int vol = (int) Math.min(Math.max(mVol + delta, 0), mAudioMax);
        if (delta != 0) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
            mVolumeSeekbar.setProgress(vol * mVolumeSeekbar.getMax()
                    / mAudioMax);
            mIsAudioOrBrightnessChanged = true;
            showInfo(
                    mContext.getString(R.string.volume) + '\u00A0'
                            + Integer.toString(vol), 1000);
        }
    }

    //亮度控制
    public void initBrightnessTouch() {
        float brightnesstemp = 0.01f;
        // Initialize the layoutParams screen brightness
        try {
            brightnesstemp = Settings.System.getInt(
                    mContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS) / 255.0f;
        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.screenBrightness = brightnesstemp;
        mContext.getWindow().setAttributes(lp);
        mIsFirstBrightnessGesture = false;
    }

    public void doBrightnessTouch(float y_changed) {
        if (mIsFirstBrightnessGesture)
            initBrightnessTouch();
        mIsAudioOrBrightnessChanged = true;

        // Set delta : 0.07f is arbitrary for now, it possibly will change in
        // the future
        float delta = -y_changed / mSurfaceYDisplayRange * 0.07f;

        // Estimate and adjust Brightness
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.screenBrightness = Math.min(
                Math.max(lp.screenBrightness + delta, 0.01f), 1);

        // Set Brightness
        mContext.getWindow().setAttributes(lp);
        showInfo(
                mContext.getString(R.string.brightness) + '\u00A0'
                        + Math.round(lp.screenBrightness * 15), 1000);
    }

    @Override
    public void setState(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (isPlaying) {
            player_overlay_play.setImageResource(R.drawable.live_landscape_stopx);
        } else {
            player_overlay_play.setImageResource(R.drawable.live_landscape_play);
        }
    }

    @Override
    public void setSmallVolBri(boolean isSmallVolBri) {
        if (isSmallVolBri) {
            layout_small_vol_bright.setVisibility(View.GONE);
        } else {
            layout_small_vol_bright.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setFullVolBri(boolean isFullVolBri) {
        if (isFullVolBri) {
            layout_vol_bright.setVisibility(View.GONE);
        } else {
            layout_vol_bright.setVisibility(View.VISIBLE);
            img_brigth.setImageResource(R.drawable.live_brightness);
            img_volume.setImageResource(R.drawable.live_volume);
        }
    }

    @Override
    public void setDlnaRefresh(boolean show) {

        if(show){
            if (share_progressBar != null)
                share_progressBar.setVisibility(View.VISIBLE);
            txt_refresh.setText("点击重新刷新");
        }else{
            if (share_progressBar != null)
                share_progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setDefinition(String string) {
        if(string.equals("1")){
            definition = "1";
            img_play_defi.setImageResource(R.drawable.live_sd);
        }else if(string.equals("2")){
            definition = "2";
            img_play_defi.setImageResource(R.drawable.live_hd);
        }else if(string.equals("4")){
            definition = "4";
            img_play_defi.setImageResource(R.drawable.live_vhd);
        }else{
            definition = "1";
            img_play_defi.setImageResource(R.drawable.live_sd);
        }
    }

    @Override
    public void setBookMark(final int position) {
        layout_bookmark.setVisibility(View.VISIBLE);
        txt_last_time.setText(getResources().getString(R.string.txt_last_play_time)
                + Util.millisToString(position)
                + "，点击");
        txt_last_play_time.setOnClickListener(new OnClickListioners("mTurnBookMark", position));
        img_bookmark_cancel.setOnClickListener(new OnClickListioners("mBookMarkCancel"));
    }

    @Override
    public void setFavoriteShow(boolean show) {
        if(show) {
            img_play_favorite.setVisibility(View.VISIBLE);
        }else{
            img_play_favorite.setVisibility(View.GONE);
        }
    }

    @Override
    public void setDlnaShow(boolean show) {
        if(show) {
            img_play_dlna.setVisibility(View.VISIBLE);
        }else{
            img_play_dlna.setVisibility(View.GONE);
        }
    }

    @Override
    public void setShareShow(boolean show) {
        if(show) {
            img_play_share.setVisibility(View.VISIBLE);
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
//        setOverlayProgress();
    }

    @Override
    public void setVideoLength(int length) {
        if(this.length == 0 && length > 0) {
            this.length = length;
            mSeekbar.setMax(length);
            setOverlayProgress();
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

    /**
     * 播放器进度条监听
     */
    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mDragging = true;
            showOverlay(OVERLAY_INFINITE);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mDragging = false;
            showOverlay();
            hideInfo();
            hideOverlay(true);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int  progress,
                                      boolean fromUser) {
            if (fromUser) {
                setOverlayProgress();
                mTime.setText(Util.millisToString(progress));
                showInfo(Util.millisToString(progress));
                if (listener != null) {
                    listener.onSeekTo(progress);
                }
            }

        }
    };

    class OnClickListioners implements View.OnClickListener{

        String id = "";
        int position = 0;

        public OnClickListioners(String id){
            this.id = id;
        }

        public OnClickListioners(String id, int position) {
            this.id = id;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (id){
                case "mExitFullScreenListener":
                    onBackPressed();
                    break;
                case "mRemainingTimeListener":
                    mDisplayRemainingTime = !mDisplayRemainingTime;
                    showOverlay();
                    break;
                case "mPlayPause":
                    if (listener != null)
                        listener.onPlayPause();
                    break;
                case "mLockListener":
                    if (mIsLocked) {
                        mIsLocked = false;
                        unlockScreen();
                    } else {
                        mIsLocked = true;
                        lockScreen();
                    }
                    break;
                case "mSizeListener":
                    switch (getScreenRotation()) {
                        case Surface.ROTATION_0:
                        case Surface.ROTATION_180:
                            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            hideOverlay(true);
                            Message msg = mHandler.obtainMessage(REQUESTED_SENSOR);
                            mHandler.sendMessageDelayed(msg, 2000);
                            mSize.setVisibility(View.GONE);
                            player_overlay_play.setVisibility(View.GONE);
                            setPopuWindowDismiss();
                            img_play_channel.setVisibility(View.GONE);
                            break;
                        case Surface.ROTATION_90:
                        case Surface.ROTATION_270:
                            // SCREEN_ORIENTATION_REVERSE_LANDSCAPE only available since API
                            // Level 9+
                            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            hideOverlay(true);
                            Message msg2 = mHandler.obtainMessage(REQUESTED_SENSOR);
                            mHandler.sendMessageDelayed(msg2, 2000);
                            mSize.setVisibility(View.VISIBLE);
                            player_overlay_play.setVisibility(View.VISIBLE);
                            img_play_channel.setVisibility(View.VISIBLE);
                            setPopuWindowDismiss();
                            break;
                    }
                    break;
                case "mFavorite":
                    if(listener != null)
                        listener.getFavorite();
                    break;
                case "mPlayChannel":
                    if ((STATE_VOD_LIVE == 0)) {
                        if (isChannelShow) {
                            if (popupTvInfo != null) {
                                popupTvInfo.dismiss();
                            }
                            isChannelShow = false;
                        } else {
                            intPopupTvInfo();
                            isChannelShow = true;
                        }
                        showOverlay();
                    } else if ((STATE_VOD_LIVE == 1)) {        //直播时隐藏收藏按钮，img_play_channel显示选择频道状态，点击弹出频道弹窗
                        if (isChannelShow) {
                            if (popupChannelInfo != null) {
                                popupChannelInfo.dismiss();
                            }
                            img_play_channel
                                    .setImageResource(R.drawable.live_channel_select);
                            isChannelShow = false;
                        } else {
                            intPopupChannelInfo();
                            img_play_channel
                                    .setImageResource(R.drawable.live_channel_select);
                            isChannelShow = true;
                        }
                        showOverlay();
                    }
                    break;
                case "mPlayDefinition":
                    if (isDefinShow) {
                        if (popuDefinition != null) {
                            popuDefinition.dismiss();
                        }
                        isDefinShow = false;
                    } else {
                        intDefinition();
                        isDefinShow = true;
                    }
                    showOverlay();
                    break;
                case "mDlanShare":
                    if (isShareShow) {
                        if (popupShareTv != null) {
                            popupShareTv.dismiss();
                        }
                        isShareShow = false;
                    } else {
                        intPopupShareTv();
                        isShareShow = true;
                    }
                    showOverlay();
                    break;
                case "mDlanRefresh":
                    setDlnaRefresh(true);
                    if(listener != null)
                        listener.onRefreshDlna();
                    break;
                case "mTurnBookMark":
                    mSeekbar.setProgress(position);
                    layout_bookmark.setVisibility(View.INVISIBLE);
                    break;
                case "mBookMarkCancel":
                    layout_bookmark.setVisibility(View.INVISIBLE);
                    break;
                case "mPlayShare":
                    if(listener != null)
                        listener.onShare();
                    break;
                default:
                    break;

            }
        }
    }
}