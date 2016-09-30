package com.kys.playercontrol.widget;
/**
 * Created by 幻云紫日 on 2016/9/29.
 */

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;

import com.kys.playercontrol.R;

/**
 * 作者：幻云紫日 on 2016/9/29 13:25
 */
public class OnClickListioners extends PlayControl implements View.OnClickListener{
    public OnClickListioners(Activity context) {
        super(context);
    }

    String id = "";
    int position = 0;

    public OnClickListioners(Activity context, String id){
        super(context);
        this.id = id;
    }

    public OnClickListioners(Activity context, String id, int position) {
        super(context);
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
                mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                showOverlay();
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
