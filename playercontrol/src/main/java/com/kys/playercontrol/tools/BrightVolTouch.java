package com.kys.playercontrol.tools;
/**
 * Created by 幻云紫日 on 2016/9/26.
 */

import android.app.Activity;
import android.media.AudioManager;
import android.provider.Settings;
import android.view.WindowManager;

/**
 * 作者：幻云紫日 on 2016/9/26 12:52
 */
public class BrightVolTouch {

    //音量控制
    public static int doVolumeTouch(float y_changed,
                                     int mSurfaceYDisplayRange, int mAudioMax, float mVol,
                                     AudioManager mAudioManager) {
        int delta = -(int) ((y_changed / mSurfaceYDisplayRange) * mAudioMax);
        int vol = (int) Math.min(Math.max(mVol + delta, 0), mAudioMax);
        if (delta != 0) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
            return vol;
        } else {
            return 0;
        }

    }

    //亮度控制
    public static void initBrightnessTouch(Activity mContext) {
        float brightnesstemp = 0.01f;
        // Initialize the layoutParams screen brightness
        try {
            brightnesstemp = Settings.System.getInt(
                    mContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS) / 255.0f;
        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.screenBrightness = brightnesstemp;
        mContext.getWindow().setAttributes(lp);
    }

    public static float doBrightnessTouch(Activity mContext, float y_changed, boolean mIsFirstBrightnessGesture, int mSurfaceYDisplayRange) {
        if (mIsFirstBrightnessGesture)
            initBrightnessTouch(mContext);

        // Set delta : 0.07f is arbitrary for now, it possibly will change in
        // the future
        float delta = -y_changed / mSurfaceYDisplayRange * 0.07f;

        // Estimate and adjust Brightness
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.screenBrightness = Math.min(
                Math.max(lp.screenBrightness + delta, 0.01f), 1);

        // Set Brightness
        mContext.getWindow().setAttributes(lp);
        return lp.screenBrightness;
    }

}
