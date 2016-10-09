package com.kys.playercontrol.widget;
/**
 * Created by 幻云紫日 on 2016/9/29.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;

/**
 * 作者：幻云紫日 on 2016/9/29 11:44
 * 用于设置控件样式及字体
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
    private static Typeface mTitleTypeface;
    private static Typeface mSysTimeTypeface;
    private static Typeface mTimeTypeface;
    private static Typeface mLengthTypeface;
    private static Typeface mInfoTypeface;
    private static Typeface txt_last_timeTypeface;
    private static Typeface txt_last_play_timeTypeface;
    private static Activity mContext;

    public KeyStyle(Activity context) {
        super(context);
        mContext = context;
    }

    public static void setContext(Activity context) {
        mContext = context;
    }

    public static void setmTitleStyle(int mStyle) {
        mTitleStyle = mStyle;
        mTitle.setTextAppearance(mContext, mTitleStyle);
    }

    public static void setmSysTimeStyle(int mStyle) {
        mSysTimeStyle = mStyle;
        mSysTime.setTextAppearance(mSysTimeStyle);
    }

    public static void setmTimeStyle(int mStyle) {
        mTimeStyle = mStyle;
        mTime.setTextAppearance(mTimeStyle);
    }

    public static void setmInfoStyle(int mStyle) {
        mInfoStyle = mStyle;
        mInfo.setTextAppearance(mInfoStyle);
    }

    public static void setTxt_last_timeStyle(int mStyle) {
        txt_last_timeStyle = mStyle;
        txt_last_time.setTextAppearance(txt_last_timeStyle);
    }

    public static void setTxt_last_play_timeStyle(int mStyle) {
        txt_last_play_timeStyle = mStyle;
        txt_last_play_time.setTextAppearance(txt_last_play_timeStyle);
    }

    public static void setmLengthStyle(int mStyle) {
        mLengthStyle = mStyle;
        mLength.setTextAppearance(mLengthStyle);
    }

    public static void setmTitleTypeface(Typeface mTypeface) {
        mTitleTypeface = mTypeface;
        mTitle.setTypeface(mTitleTypeface);
    }

    public static void setmSysTimeTypeface(Typeface mTypeface) {
        mSysTimeTypeface = mTypeface;
        mSysTime.setTypeface(mSysTimeTypeface);
    }

    public static void setmTimeTypeface(Typeface mTypeface) {
        mTimeTypeface = mTypeface;
        mTime.setTypeface(mTimeTypeface);
    }

    public static void setmLengthTypeface(Typeface mTypeface) {
        mLengthTypeface = mTypeface;
        mLength.setTypeface(mLengthTypeface);
    }

    public static void setmInfoTypeface(Typeface mTypeface) {
        mInfoTypeface = mTypeface;
        mInfo.setTypeface(mInfoTypeface);
    }

    public static void setTxt_last_timeTypeface(Typeface mTypeface) {
        txt_last_timeTypeface = mTypeface;
        txt_last_time.setTypeface(txt_last_timeTypeface);
    }

    public static void setTxt_last_play_timeTypeface(Typeface mTypeface) {
        txt_last_play_timeTypeface = mTypeface;
        txt_last_play_time.setTypeface(txt_last_play_timeTypeface);
    }
}
