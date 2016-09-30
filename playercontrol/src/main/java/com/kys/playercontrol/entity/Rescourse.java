package com.kys.playercontrol.entity;
/**
 * Created by 幻云紫日 on 2016/9/29.
 */

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.kys.playercontrol.R;
import com.kys.playercontrol.widget.PlayControl;

/**
 * 作者：幻云紫日 on 2016/9/29 11:44
 */
public class Rescourse extends PlayControl{

    private int player_overlay_back_bg = R.drawable.back;
    private int img_play_favorite_bg = R.drawable.live_collection;
    private int img_play_share_bg = R.drawable.live_share;
    private int player_overlay_play_bg = R.drawable.live_landscape_play;
    private Drawable player_overlay_seekbar_bg = getResources().getDrawable(R.drawable.progress_horizontal);
    private int img_play_channel_bg = R.drawable.live_channel_select;
    private int img_play_defi_bg = R.drawable.live_sd;
    private int img_play_dlna_bg = R.drawable.live_ic_airplay;
    private int player_overlay_size_bg = R.drawable.live_blowup;
    private int lock_overlay_button_bg = R.drawable.live_unlock;
    private Drawable progressBar_bg = getResources().getDrawable(R.drawable.progress_bar_loading);

    public Rescourse(Activity context) {
        super(context);
    }

    public void setPlayer_overlay_back_bg(int player_overlay_back_bg) {
        btn_back.setImageResource(player_overlay_back_bg);
        this.player_overlay_back_bg = player_overlay_back_bg;
    }

    public void setImg_play_favorite_bg(int img_play_favorite_bg) {
        img_play_favorite.setImageResource(img_play_favorite_bg);
        this.img_play_favorite_bg = img_play_favorite_bg;
    }

    public void setImg_play_share_bg(int img_play_share_bg) {
        img_play_share.setImageResource(img_play_share_bg);
        this.img_play_share_bg = img_play_share_bg;
    }

    public void setPlayer_overlay_play_bg(int player_overlay_play_bg) {
        player_overlay_play.setImageResource(player_overlay_play_bg);
        this.player_overlay_play_bg = player_overlay_play_bg;
    }

    public void setPlayer_overlay_seekbar_bg(Drawable player_overlay_seekbar_bg) {
        mSeekbar.setProgressDrawable(player_overlay_seekbar_bg);
        this.player_overlay_seekbar_bg = player_overlay_seekbar_bg;
    }

    public void setImg_play_channel_bg(int img_play_channel_bg) {
        img_play_channel.setImageResource(img_play_channel_bg);
        this.img_play_channel_bg = img_play_channel_bg;
    }

    public void setImg_play_defi_bg(int img_play_defi_bg) {
        img_play_defi.setImageResource(img_play_defi_bg);
        this.img_play_defi_bg = img_play_defi_bg;
    }

    public void setImg_play_dlna_bg(int img_play_dlna_bg) {
        img_play_dlna.setImageResource(img_play_dlna_bg);
        this.img_play_dlna_bg = img_play_dlna_bg;
    }

    public void setPlayer_overlay_size_bg(int player_overlay_size_bg) {
        mSize.setImageResource(player_overlay_size_bg);
        this.player_overlay_size_bg = player_overlay_size_bg;
    }

    public void setLock_overlay_button_bg(int lock_overlay_button_bg) {
        mLock.setImageResource(lock_overlay_button_bg);
        this.lock_overlay_button_bg = lock_overlay_button_bg;
    }

    public void setProgressBar_bg(Drawable progressBar_bg) {
        this.progressBar_bg = progressBar_bg;
    }
}
