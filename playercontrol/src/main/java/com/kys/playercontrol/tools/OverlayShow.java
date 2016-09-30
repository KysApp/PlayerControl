package com.kys.playercontrol.tools;
/**
 * Created by 幻云紫日 on 2016/9/29.
 */

import android.app.Activity;
import android.os.Message;
import android.view.Surface;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.kys.playercontrol.R;
import com.kys.playercontrol.widget.PlayControl;

/**
 * 作者：幻云紫日 on 2016/9/29 13:39
 */
public class OverlayShow extends PlayControl{
    public OverlayShow(Activity context) {
        super(context);
    }

    /**
     * show overlay
     */
    public static void showOverlay(int timeout) {
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        if (!mShowing) {
            mShowing = true;
            dimStatusBar(false);
            switch (GetScreenRotation.getScreenRotation(mContext)) {
                case Surface.ROTATION_0:
                    mLock.setVisibility(View.GONE);
                    mOverlayHeader.setVisibility(View.VISIBLE);
                    mOverlayProgress.setVisibility(View.VISIBLE);
                    if(STATE_VOD_LIVE == 1) {
                        if (isLive) {
                            layout_seekbar.setVisibility(View.INVISIBLE);
                            player_overlay_play.setVisibility(View.INVISIBLE);
                        } else {
                            layout_seekbar.setVisibility(View.INVISIBLE);
                            player_overlay_play.setVisibility(View.VISIBLE);
                        }
                    }
                    if(STATE_VOD_LIVE == 0) {
                        img_play_channel.setVisibility(View.GONE);
                        mOverlayProgress.setVisibility(View.VISIBLE);
                        layout_seekbar.setVisibility(View.INVISIBLE);
                        player_overlay_play.setVisibility(View.VISIBLE);
                    }
                    break;
                case Surface.ROTATION_180:
                    mLock.setVisibility(View.GONE);
                    mOverlayHeader.setVisibility(View.VISIBLE);
                    mOverlayProgress.setVisibility(View.VISIBLE);
                    if(STATE_VOD_LIVE == 1) {
                        if (isLive) {
                            layout_seekbar.setVisibility(View.INVISIBLE);
                            player_overlay_play.setVisibility(View.INVISIBLE);
                        } else {
                            layout_seekbar.setVisibility(View.INVISIBLE);
                            player_overlay_play.setVisibility(View.VISIBLE);
                        }
                    }
                    if(STATE_VOD_LIVE == 0) {
                        img_play_channel.setVisibility(View.GONE);
                        mOverlayProgress.setVisibility(View.VISIBLE);
                        layout_seekbar.setVisibility(View.INVISIBLE);
                        player_overlay_play.setVisibility(View.VISIBLE);
                    }
                    break;
                case Surface.ROTATION_90:
                    if (mIsLocked) {
                        img_play_channel.setVisibility(View.VISIBLE);
                        layout_seekbar.setVisibility(View.VISIBLE);
                        mOverlayHeader.setVisibility(View.VISIBLE);
                        mOverlayProgress.setVisibility(View.VISIBLE);
                        player_overlay_play.setVisibility(View.VISIBLE);
                    } else {
                        img_play_channel.setVisibility(View.VISIBLE);
                        mOverlayHeader.setVisibility(View.VISIBLE);
                        mOverlayProgress.setVisibility(View.VISIBLE);
                        if(STATE_VOD_LIVE == 1) {
                            if (isLive) {
                                layout_seekbar.setVisibility(View.INVISIBLE);
                                player_overlay_play.setVisibility(View.INVISIBLE);
                            } else {
                                layout_seekbar.setVisibility(View.VISIBLE);
                                player_overlay_play.setVisibility(View.VISIBLE);
                            }
                        }
                        if(STATE_VOD_LIVE == 0) {
                            layout_seekbar.setVisibility(View.VISIBLE);
                            player_overlay_play.setVisibility(View.VISIBLE);
                        }
                    }
                    mLock.setVisibility(View.VISIBLE);
                    break;
                case Surface.ROTATION_270:
                    if (mIsLocked) {
                        img_play_channel.setVisibility(View.VISIBLE);
                        layout_seekbar.setVisibility(View.VISIBLE);
                        mOverlayHeader.setVisibility(View.VISIBLE);
                        mOverlayProgress.setVisibility(View.VISIBLE);
                        player_overlay_play.setVisibility(View.VISIBLE);
                    } else {
                        img_play_channel.setVisibility(View.VISIBLE);
                        mOverlayHeader.setVisibility(View.VISIBLE);
                        mOverlayProgress.setVisibility(View.VISIBLE);
                        if(STATE_VOD_LIVE == 1) {
                            if (isLive) {
                                layout_seekbar.setVisibility(View.INVISIBLE);
                                player_overlay_play.setVisibility(View.INVISIBLE);
                            } else {
                                layout_seekbar.setVisibility(View.VISIBLE);
                                player_overlay_play.setVisibility(View.VISIBLE);
                            }
                        }
                        if(STATE_VOD_LIVE == 0) {
                            layout_seekbar.setVisibility(View.VISIBLE);
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
    public static void hideOverlay(boolean fromUser) {

        if (mShowing) {
            mHandler.removeMessages(SHOW_PROGRESS);
            if (!fromUser && !mIsLocked) {
                mOverlayProgress.startAnimation(AnimationUtils.loadAnimation(
                        mContext, android.R.anim.fade_out));
            }
            mLock.setVisibility(View.INVISIBLE);
            mOverlayHeader.setVisibility(View.INVISIBLE);
            mOverlayProgress.setVisibility(View.INVISIBLE);
            layout_seekbar.setVisibility(View.INVISIBLE);
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
}
