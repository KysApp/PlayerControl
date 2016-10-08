package com.kys.playercontrol.widget;
/**
 * Created by 幻云紫日 on 2016/9/29.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.kys.playercontrol.R;

/**
 * 作者：幻云紫日 on 2016/9/29 11:44
 */

@SuppressLint("NewApi")
public class KeyStyle extends PlayControl {

    private static int mTitleStyle;
    private static int mSysTimeStyle;
    private static int mTimeStyle;
    private static int mLengthStyle;
    private static int mInfoStyle;
    private static int txt_last_timeStyle;
    private static int txt_last_play_timeStyle;
    private static Activity mContext;

    public KeyStyle(Activity context) {
        super(context);
        mContext = context;
    }

    public static void setContext(Activity context) {
        mContext = context;
    }

    public static void setmTitleStyle(int mTitleStyle) {
        KeyStyle.mTitleStyle = mTitleStyle;
//        mTitle.setTextAppearance(mTitleStyle);
        mTitle.setTextAppearance(mContext, mTitleStyle);
    }

    public static void setmSysTimeStyle(int mSysTimeStyle) {
        KeyStyle.mSysTimeStyle = mSysTimeStyle;
        mSysTime.setTextAppearance(mSysTimeStyle);
    }

    public static void setmTimeStyle(int mTimeStyle) {
        KeyStyle.mTimeStyle = mTimeStyle;
        mTime.setTextAppearance(mTimeStyle);
    }

    public static void setmInfoStyle(int mInfoStyle) {
        KeyStyle.mInfoStyle = mInfoStyle;
        mInfo.setTextAppearance(mInfoStyle);
    }

    public static void setTxt_last_timeStyle(int txt_last_timeStyle) {
        KeyStyle.txt_last_timeStyle = txt_last_timeStyle;
        txt_last_time.setTextAppearance(txt_last_timeStyle);
    }

    public static void setTxt_last_play_timeStyle(int txt_last_play_timeStyle) {
        KeyStyle.txt_last_play_timeStyle = txt_last_play_timeStyle;
        txt_last_play_time.setTextAppearance(txt_last_play_timeStyle);
    }
}
