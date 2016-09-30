package com.kys.playercontrol.entity;
/**
 * Created by 幻云紫日 on 2016/9/29.
 */

import android.app.Activity;

import com.kys.playercontrol.widget.PlayControl;

/**
 * 作者：幻云紫日 on 2016/9/29 11:44
 */
public class KeyShow extends PlayControl {

    private boolean player_overlay_back_show = true;
    private boolean img_play_favorite_show = true;
    private boolean img_play_share_show = true;
    private boolean player_overlay_play_show = true;
    private boolean player_overlay_seekbar_show = true;
    private boolean img_play_channel_show = true;
    private boolean img_play_defi_show = true;
    private boolean img_play_dlna_show = true;
    private boolean player_overlay_size_show = true;
    private boolean lock_overlay_button_show = true;
    private boolean progressBar_show = true;

    public KeyShow(Activity context) {
        super(context);
    }

    public boolean isPlayer_overlay_back_show() {
        return player_overlay_back_show;
    }

    public void setPlayer_overlay_back_show(boolean player_overlay_back_show) {
        this.player_overlay_back_show = player_overlay_back_show;
    }

    public boolean isImg_play_favorite_show() {
        return img_play_favorite_show;
    }

    public void setImg_play_favorite_show(boolean img_play_favorite_show) {
        this.img_play_favorite_show = img_play_favorite_show;
    }

    public boolean isImg_play_share_show() {
        return img_play_share_show;
    }

    public void setImg_play_share_show(boolean img_play_share_show) {
        this.img_play_share_show = img_play_share_show;
    }

    public boolean isPlayer_overlay_play_show() {
        return player_overlay_play_show;
    }

    public void setPlayer_overlay_play_show(boolean player_overlay_play_show) {
        this.player_overlay_play_show = player_overlay_play_show;
    }

    public boolean isPlayer_overlay_seekbar_show() {
        return player_overlay_seekbar_show;
    }

    public void setPlayer_overlay_seekbar_show(boolean player_overlay_seekbar_show) {
        this.player_overlay_seekbar_show = player_overlay_seekbar_show;
    }

    public boolean isImg_play_channel_show() {
        return img_play_channel_show;
    }

    public void setImg_play_channel_show(boolean img_play_channel_show) {
        this.img_play_channel_show = img_play_channel_show;
    }

    public boolean isImg_play_defi_show() {
        return img_play_defi_show;
    }

    public void setImg_play_defi_show(boolean img_play_defi_show) {
        this.img_play_defi_show = img_play_defi_show;
    }

    public boolean isImg_play_dlna_show() {
        return img_play_dlna_show;
    }

    public void setImg_play_dlna_show(boolean img_play_dlna_show) {
        this.img_play_dlna_show = img_play_dlna_show;
    }

    public boolean isPlayer_overlay_size_show() {
        return player_overlay_size_show;
    }

    public void setPlayer_overlay_size_show(boolean player_overlay_size_show) {
        this.player_overlay_size_show = player_overlay_size_show;
    }

    public boolean isLock_overlay_button_show() {
        return lock_overlay_button_show;
    }

    public void setLock_overlay_button_show(boolean lock_overlay_button_show) {
        this.lock_overlay_button_show = lock_overlay_button_show;
    }

    public boolean isProgressBar_show() {
        return progressBar_show;
    }

    public void setProgressBar_show(boolean progressBar_show) {
        this.progressBar_show = progressBar_show;
    }
}
