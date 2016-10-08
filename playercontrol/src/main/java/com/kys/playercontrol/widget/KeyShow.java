package com.kys.playercontrol.widget;
/**
 * Created by 幻云紫日 on 2016/9/29.
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * 作者：幻云紫日 on 2016/9/29 11:44
 */
public class KeyShow extends PlayControl {

    private static boolean player_overlay_back_show = true;
    private static boolean img_play_favorite_show = true;
    private static boolean img_play_share_show = true;
    private static boolean player_overlay_play_show = true;
    private static boolean player_overlay_seekbar_show = true;
    private static boolean img_play_channel_show = true;
    private static boolean img_play_defi_show = true;
    private static boolean img_play_dlna_show = true;
    private static boolean player_overlay_size_show = true;
    private static boolean lock_overlay_button_show = true;
    private static boolean progressBar_show = true;
    private static boolean img_bright_volume_show = true;
    private static boolean img_bookmark_cancel_show = true;
    private static boolean mTitle_show = true;
    private static boolean mSysTime_show = true;
    private static Activity mContext;

    public KeyShow(Activity context) {
        super(context);
        mContext = context;
    }

    public static void setContext(Activity context) {
        mContext = context;
    }

    public static boolean isPlayer_overlay_back_show() {
        return player_overlay_back_show;
    }

    public static void setPlayer_overlay_back_show(boolean isShow) {
        player_overlay_back_show = isShow;
        if(!player_overlay_back_show)btn_back.setVisibility(View.INVISIBLE);
    }

    public static boolean isImg_play_favorite_show() {
        return img_play_favorite_show;
    }

    public static void setImg_play_favorite_show(boolean isShow) {
        img_play_favorite_show = isShow;
        if(!img_play_favorite_show)img_play_favorite.setVisibility(View.INVISIBLE);
    }

    public static boolean isImg_play_share_show() {
        return img_play_share_show;
    }

    public static void setImg_play_share_show(boolean isShow) {
        img_play_share_show = isShow;
        if(!img_play_share_show)img_play_share.setVisibility(View.INVISIBLE);
    }

    public static boolean isPlayer_overlay_play_show() {
        return player_overlay_play_show;
    }

    public static void setPlayer_overlay_play_show(boolean isShow) {
        player_overlay_play_show = isShow;
        if(!player_overlay_play_show)player_overlay_play.setVisibility(View.INVISIBLE);
    }

    public static boolean isPlayer_overlay_seekbar_show() {
        return player_overlay_seekbar_show;
    }

    public static void setPlayer_overlay_seekbar_show(boolean isShow) {
        player_overlay_seekbar_show = isShow;
        if(!player_overlay_seekbar_show)mSeekbar.setVisibility(View.INVISIBLE);
    }

    public static boolean isImg_play_channel_show() {
        return img_play_channel_show;
    }

    public static void setImg_play_channel_show(boolean isShow) {
        img_play_channel_show = isShow;
        if(!img_play_channel_show)img_play_channel.setVisibility(View.INVISIBLE);
    }

    public static boolean isImg_play_defi_show() {
        return img_play_defi_show;
    }

    public static void setImg_play_defi_show(boolean isShow) {
        img_play_defi_show = isShow;
        if(!img_play_defi_show)img_play_defi.setVisibility(View.INVISIBLE);
    }

    public static boolean isImg_play_dlna_show() {
        return img_play_dlna_show;
    }

    public static void setImg_play_dlna_show(boolean isShow) {
        img_play_dlna_show = isShow;
        if(!img_play_dlna_show)img_play_dlna.setVisibility(View.INVISIBLE);
    }

    public static boolean isPlayer_overlay_size_show() {
        return player_overlay_size_show;
    }

    public static void setPlayer_overlay_size_show(boolean isShow) {
        player_overlay_size_show = isShow;
        if(!player_overlay_size_show)mSize.setVisibility(View.INVISIBLE);
    }

    public static boolean isLock_overlay_button_show() {
        return lock_overlay_button_show;
    }

    public static void setLock_overlay_button_show(boolean isShow) {
        lock_overlay_button_show = isShow;
        if(!lock_overlay_button_show)mLock.setVisibility(View.INVISIBLE);
    }

    public static boolean isProgressBar_show() {
        return progressBar_show;
    }

    public static void setProgressBar_show(boolean isShow) {
        progressBar_show = isShow;
        if(!progressBar_show)mProgressBar.setVisibility(View.INVISIBLE);
    }

    public static boolean isImg_bright_volume_show() {
        return img_bright_volume_show;
    }

    public static void setImg_bright_volume_show(boolean isShow) {
        img_bright_volume_show = isShow;
        if(!img_bright_volume_show) {
            img_brigth.setVisibility(View.INVISIBLE);
            img_volume.setVisibility(View.INVISIBLE);
        }
    }

    public static boolean isImg_bookmark_cancel_show() {
        return img_bookmark_cancel_show;
    }

    public static void setImg_bookmark_cancel_show(boolean isShow) {
        img_bookmark_cancel_show = isShow;
        if(!img_bookmark_cancel_show)img_bookmark_cancel.setVisibility(View.INVISIBLE);
    }

    public static boolean ismTitle_show() {
        return mTitle_show;
    }

    public static void setmTitle_show(boolean isShow) {
        mTitle_show = isShow;
        if(!mTitle_show)mTitle.setVisibility(View.INVISIBLE);
    }

    public static boolean ismSysTime_show() {
        return mSysTime_show;
    }

    public static void setmSysTime_show(boolean isShow) {
        mSysTime_show = isShow;
        if(!mSysTime_show)mSysTime.setVisibility(View.INVISIBLE);
    }
}
