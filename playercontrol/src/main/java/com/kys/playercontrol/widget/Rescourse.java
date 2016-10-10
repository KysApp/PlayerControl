package com.kys.playercontrol.widget;
/**
 * Created by 幻云紫日 on 2016/9/29.
 */

import android.app.Activity;

import com.kys.playercontrol.R;

/**
 * 作者：幻云紫日 on 2016/9/29 11:44
 * 用于设置控件图片
 */
public class Rescourse extends PlayControl{

    private static int player_overlay_back_bg = R.drawable.back;
    private static int img_play_unfavorite_bg = R.drawable.live_collection;
    private static int img_play_favorite_bg = R.drawable.live_precolle;
    private static int img_play_share_bg = R.drawable.live_share;
    private static int player_overlay_play_bg = R.drawable.live_landscape_play;
    private static int player_overlay_pause_bg = R.drawable.live_landscape_stopx;
    private static int player_overlay_seekbar_bg = R.drawable.progress_horizontal;
    private static int img_play_channel_bg = R.drawable.live_channel_select;
    private static int img_play_defi_bg = R.drawable.live_sd;
    private static int img_play_dlna_bg = R.drawable.live_ic_airplay;
    private static int player_overlay_size_bg = R.drawable.live_blowup;
    private static int unlock_overlay_button_bg = R.drawable.live_unlock;
    private static int lock_overlay_button_bg = R.drawable.live_lockx;
    private static int progressBar_bg = R.drawable.progress_bar_loading;
    private static int img_bright_bg = R.drawable.live_brightness;
    private static int img_volume_bg = R.drawable.live_volume;
    private static int img_bookmark_cancel_bg = R.drawable.close;
    private static int popupwindow_bg = R.color.black;
    private static Activity mContext;

    public Rescourse(Activity context) {
        super(context);
        mContext = context;
    }

    public static void setContext(Activity context) {
        mContext = context;
    }

    public static void setPlayer_overlay_back_bg(int imgId) {
        btn_back.setImageResource(imgId);
        player_overlay_back_bg = imgId;
    }

    public static void setImg_play_favorite_bg(int imgId) {
        img_play_favorite.setImageResource(imgId);
        img_play_favorite_bg = imgId;
    }

    public static void setImg_play_unfavorite_bg(int imgId) {
        img_play_favorite.setImageResource(imgId);
        img_play_unfavorite_bg = imgId;
    }

    public static void setImg_play_share_bg(int imgId) {
        img_play_share.setImageResource(imgId);
        img_play_share_bg = imgId;
    }

    public static void setPlayer_overlay_play_bg(int imgId) {
        player_overlay_play.setImageResource(imgId);
        player_overlay_play_bg = imgId;
    }

    public static void setPlayer_overlay_pause_bg(int imgId) {
        player_overlay_play.setImageResource(imgId);
        player_overlay_pause_bg = imgId;
    }

    public static void setPlayer_overlay_seekbar_bg(int imgId) {
//        Drawable mDrawable = getResources().getDrawable(imgId, null);
//        mSeekbar.setProgressDrawable(mDrawable);
        player_overlay_seekbar_bg = imgId;
    }

    public static void setImg_play_channel_bg(int imgId) {
        img_play_channel.setImageResource(imgId);
        img_play_channel_bg = imgId;
    }

    public static void setImg_play_defi_bg(int imgId) {
        img_play_defi.setImageResource(imgId);
        img_play_defi_bg = imgId;
    }

    public static void setImg_play_dlna_bg(int imgId) {
        img_play_dlna.setImageResource(imgId);
        img_play_dlna_bg = imgId;
    }

    public static void setPlayer_overlay_size_bg(int imgId) {
        mSize.setImageResource(imgId);
        player_overlay_size_bg = imgId;
    }

    public static void setLock_overlay_button_bg(int imgId) {
        mLock.setImageResource(imgId);
        lock_overlay_button_bg = imgId;
    }

    public static void setUnLock_overlay_button_bg(int imgId) {
        mLock.setImageResource(imgId);
        unlock_overlay_button_bg = imgId;
    }

    public static void setPopupwindow_bg(int imgId) {
        popupwindow_bg = imgId;
    }

    public static void setUnlock_overlay_button_bg(int imgId) {
        mLock.setImageResource(imgId);
        unlock_overlay_button_bg = imgId;
    }

    public static void setImg_bright_bg(int imgId) {
        img_bright_bg = imgId;
        img_brigth.setImageResource(img_bright_bg);
    }

    public static void setImg_volume_bg(int imgId) {
        img_volume_bg = imgId;
        img_volume.setImageResource(imgId);
    }

    public static void setImg_bookmark_cancel_bg(int imgId) {
        img_bookmark_cancel_bg = imgId;
        img_bookmark_cancel.setImageResource(imgId);
    }

    public static void setProgressBar_bg(int imgId) {
        progressBar_bg = imgId;
    }

    public static int getPlayer_overlay_back_bg() {
        return player_overlay_back_bg;
    }

    public static int getImg_play_unfavorite_bg() {
        return img_play_unfavorite_bg;
    }

    public static int getImg_play_favorite_bg() {
        return img_play_favorite_bg;
    }

    public static int getImg_play_share_bg() {
        return img_play_share_bg;
    }

    public static int getPlayer_overlay_play_bg() {
        return player_overlay_play_bg;
    }

    public static int getPlayer_overlay_pause_bg() {
        return player_overlay_pause_bg;
    }

    public static int getPlayer_overlay_seekbar_bg() {
        return player_overlay_seekbar_bg;
    }

    public static int getImg_play_channel_bg() {
        return img_play_channel_bg;
    }

    public static int getImg_play_defi_bg() {
        return img_play_defi_bg;
    }

    public static int getImg_play_dlna_bg() {
        return img_play_dlna_bg;
    }

    public static int getPlayer_overlay_size_bg() {
        return player_overlay_size_bg;
    }

    public static int getUnlock_overlay_button_bg() {
        return unlock_overlay_button_bg;
    }

    public static int getLock_overlay_button_bg() {
        return lock_overlay_button_bg;
    }

    public static int getProgressBar_bg() {
        return progressBar_bg;
    }

    public static int getImg_bright_bg() {
        return img_bright_bg;
    }

    public static int getImg_volume_bg() {
        return img_volume_bg;
    }

    public static int getImg_bookmark_cancel_bg() {
        return img_bookmark_cancel_bg;
    }

    public static int getPopupwindow_bg() {
        return popupwindow_bg;
    }

}
