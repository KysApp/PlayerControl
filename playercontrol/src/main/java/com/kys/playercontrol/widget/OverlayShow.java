package com.kys.playercontrol.widget;
/**
 * Created by 幻云紫日 on 2016/9/29.
 */

import android.app.Activity;
import android.os.Message;
import android.view.Surface;
import android.view.View;
import android.view.animation.AnimationUtils;

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
            if(mOverlayContent == null){
                if (mIsLocked) {
                    if (KeyShow.isImg_play_channel_show())
                        img_play_channel.setVisibility(View.VISIBLE);
                    if (KeyShow.isPlayer_overlay_seekbar_show())
                        layout_seekbar.setVisibility(View.VISIBLE);
                    mOverlayHeader.setVisibility(View.VISIBLE);
                    mOverlayProgress.setVisibility(View.VISIBLE);
                    player_overlay_play.setVisibility(View.VISIBLE);
                } else {
                    if (KeyShow.isImg_play_channel_show())
                        img_play_channel.setVisibility(View.VISIBLE);
                    mOverlayHeader.setVisibility(View.VISIBLE);
                    mOverlayProgress.setVisibility(View.VISIBLE);
                    if (STATE_VOD_LIVE == 1) {
                        if (isLive) {
                            layout_seekbar.setVisibility(View.INVISIBLE);
                            player_overlay_play.setVisibility(View.INVISIBLE);
                        } else {
                            if (KeyShow.isPlayer_overlay_seekbar_show())
                                layout_seekbar.setVisibility(View.VISIBLE);
                            if (KeyShow.isPlayer_overlay_play_show())
                                player_overlay_play.setVisibility(View.VISIBLE);
                        }
                    }
                    if (STATE_VOD_LIVE == 0) {
                        if (KeyShow.isPlayer_overlay_seekbar_show())
                            layout_seekbar.setVisibility(View.VISIBLE);
                        if (KeyShow.isPlayer_overlay_play_show())
                            player_overlay_play.setVisibility(View.VISIBLE);
                    }
                }
                if (KeyShow.isLock_overlay_button_show()) mLock.setVisibility(View.VISIBLE);
            } else {
                switch (GetScreenRotation.getScreenRotation(mContext)) {
                    case Surface.ROTATION_0:
                        mLock.setVisibility(View.GONE);
                        mOverlayHeader.setVisibility(View.VISIBLE);
                        mOverlayProgress.setVisibility(View.VISIBLE);
                        if (STATE_VOD_LIVE == 1) {
                            if (isLive) {
                                layout_seekbar.setVisibility(View.INVISIBLE);
                                player_overlay_play.setVisibility(View.INVISIBLE);
                            } else {
                                layout_seekbar.setVisibility(View.INVISIBLE);
                                if (KeyShow.isPlayer_overlay_play_show())
                                    player_overlay_play.setVisibility(View.VISIBLE);
                            }
                        }
                        if (STATE_VOD_LIVE == 0) {
                            img_play_channel.setVisibility(View.GONE);
                            if (KeyShow.isProgressBar_show())
                                mOverlayProgress.setVisibility(View.VISIBLE);
                            layout_seekbar.setVisibility(View.INVISIBLE);
                            if (KeyShow.isPlayer_overlay_play_show())
                                player_overlay_play.setVisibility(View.VISIBLE);
                        }
                        isFullOrSmall = false;
                        break;
                    case Surface.ROTATION_180:
                        mLock.setVisibility(View.GONE);
                        mOverlayHeader.setVisibility(View.VISIBLE);
                        mOverlayProgress.setVisibility(View.VISIBLE);
                        if (STATE_VOD_LIVE == 1) {
                            if (isLive) {
                                layout_seekbar.setVisibility(View.INVISIBLE);
                                player_overlay_play.setVisibility(View.INVISIBLE);
                            } else {
                                layout_seekbar.setVisibility(View.INVISIBLE);
                                if (KeyShow.isPlayer_overlay_play_show())
                                    player_overlay_play.setVisibility(View.VISIBLE);
                            }
                        }
                        if (STATE_VOD_LIVE == 0) {
                            img_play_channel.setVisibility(View.GONE);
                            if (KeyShow.isProgressBar_show())
                                mOverlayProgress.setVisibility(View.VISIBLE);
                            layout_seekbar.setVisibility(View.INVISIBLE);
                            if (KeyShow.isPlayer_overlay_play_show())
                                player_overlay_play.setVisibility(View.VISIBLE);
                        }
                        isFullOrSmall = false;
                        break;
                    case Surface.ROTATION_90:
                        if (mIsLocked) {
                            if (KeyShow.isImg_play_channel_show())
                                img_play_channel.setVisibility(View.VISIBLE);
                            if (KeyShow.isPlayer_overlay_seekbar_show())
                                layout_seekbar.setVisibility(View.VISIBLE);
                            mOverlayHeader.setVisibility(View.VISIBLE);
                            mOverlayProgress.setVisibility(View.VISIBLE);
                            if (KeyShow.isPlayer_overlay_play_show())
                                player_overlay_play.setVisibility(View.VISIBLE);
                        } else {
                            if (KeyShow.isImg_play_channel_show())
                                img_play_channel.setVisibility(View.VISIBLE);
                            mOverlayHeader.setVisibility(View.VISIBLE);
                            mOverlayProgress.setVisibility(View.VISIBLE);
                            if (STATE_VOD_LIVE == 1) {
                                if (isLive) {
                                    layout_seekbar.setVisibility(View.INVISIBLE);
                                    player_overlay_play.setVisibility(View.INVISIBLE);
                                } else {
                                    if (KeyShow.isPlayer_overlay_seekbar_show())
                                        layout_seekbar.setVisibility(View.VISIBLE);
                                    if (KeyShow.isPlayer_overlay_play_show())
                                        player_overlay_play.setVisibility(View.VISIBLE);
                                }
                            }
                            if (STATE_VOD_LIVE == 0) {
                                if (KeyShow.isPlayer_overlay_seekbar_show())
                                    layout_seekbar.setVisibility(View.VISIBLE);
                                if (KeyShow.isPlayer_overlay_play_show())
                                    player_overlay_play.setVisibility(View.VISIBLE);
                            }
                        }
                        if (KeyShow.isLock_overlay_button_show()) mLock.setVisibility(View.VISIBLE);
                        isFullOrSmall = true;
                        break;
                    case Surface.ROTATION_270:
                        if (mIsLocked) {
                            if (KeyShow.isImg_play_channel_show())
                                img_play_channel.setVisibility(View.VISIBLE);
                            if (KeyShow.isPlayer_overlay_seekbar_show())
                                layout_seekbar.setVisibility(View.VISIBLE);
                            mOverlayHeader.setVisibility(View.VISIBLE);
                            mOverlayProgress.setVisibility(View.VISIBLE);
                            player_overlay_play.setVisibility(View.VISIBLE);
                        } else {
                            if (KeyShow.isImg_play_channel_show())
                                img_play_channel.setVisibility(View.VISIBLE);
                            mOverlayHeader.setVisibility(View.VISIBLE);
                            mOverlayProgress.setVisibility(View.VISIBLE);
                            if (STATE_VOD_LIVE == 1) {
                                if (isLive) {
                                    layout_seekbar.setVisibility(View.INVISIBLE);
                                    player_overlay_play.setVisibility(View.INVISIBLE);
                                } else {
                                    if (KeyShow.isPlayer_overlay_seekbar_show())
                                        layout_seekbar.setVisibility(View.VISIBLE);
                                    if (KeyShow.isPlayer_overlay_play_show())
                                        player_overlay_play.setVisibility(View.VISIBLE);
                                }
                            }
                            if (STATE_VOD_LIVE == 0) {
                                if (KeyShow.isPlayer_overlay_seekbar_show())
                                    layout_seekbar.setVisibility(View.VISIBLE);
                                if (KeyShow.isPlayer_overlay_play_show())
                                    player_overlay_play.setVisibility(View.VISIBLE);
                            }
                        }
                        if (KeyShow.isLock_overlay_button_show()) mLock.setVisibility(View.VISIBLE);
                        isFullOrSmall = true;
                        break;

                }
            }

        }
        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    /**
     * @param fromUser
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
        img_play_channel.setImageResource(Rescourse.getImg_play_channel_bg());
    }
}
