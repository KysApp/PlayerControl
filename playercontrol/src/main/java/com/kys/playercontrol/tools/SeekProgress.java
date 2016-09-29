package com.kys.playercontrol.tools;
/**
 * Created by 幻云紫日 on 2016/9/27.
 */

import android.app.Activity;
import android.text.format.DateFormat;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kys.playercontrol.interfaces.OnPlayerControlListener;

import org.videolan.vlc.Util;

import java.util.Date;
public class SeekProgress {

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
    public static int setOverlayProgress(Activity mContext, SeekBar mSeekbar, TextView mTime, TextView mLength, OnPlayerControlListener listener, TextView mSysTime, int time, int length, boolean mDisplayRemainingTime) {
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
}
