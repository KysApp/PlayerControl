package com.kys.playercontrol.widget;
/**
 * Created by 幻云紫日 on 2016/9/27.
 */

import android.app.Activity;
import android.text.format.DateFormat;
import android.widget.SeekBar;

import org.videolan.vlc.Util;

import java.util.Date;
public class SeekProgress extends PlayControl{

    public SeekProgress(Activity context) {
        super(context);
    }

    /**
     * update the overlay播放器进度显示
     * @param mContext
     * @param mSeekbar
     * @param mTime
     * @param mLength
     * @param listener
     * @param mSysTime
     * @param time
     * @param length
     * @param mDisplayRemainingTime
     */
    public static int setOverlayProgress() {
        // Update all view elements
        if (listener != null) {
            time = listener.onCurrentPosition();
            length = listener.onVideoLength();
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

    /**
     * 播放器进度条监听
     */
    public static final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mDragging = true;
            OverlayShow.showOverlay(OVERLAY_INFINITE);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mDragging = false;
            showOverlay();
            hideInfo();
            OverlayShow.hideOverlay(true);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int  progress,
                                      boolean fromUser) {
            if (fromUser) {
                time = SeekProgress.setOverlayProgress();
                mTime.setText(Util.millisToString(progress));
                showInfo(Util.millisToString(progress));
                if (listener != null) {
                    listener.onSeekTo(progress);
                }
            }

        }
    };
}
