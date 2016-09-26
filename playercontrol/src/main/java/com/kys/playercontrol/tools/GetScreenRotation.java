package com.kys.playercontrol.tools;
/**
 * Created by 幻云紫日 on 2016/9/26.
 */

import android.content.Context;
import android.os.Build;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 作者：幻云紫日 on 2016/9/26 11:56
 */
public class GetScreenRotation {

    @SuppressWarnings("deprecation")
    public static int getScreenRotation(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO /*
                                                                 * Android 2.2
																 * has
																 * getRotation
																 */) {
            try {
                Method m = display.getClass().getDeclaredMethod("getRotation");
                return (Integer) m.invoke(display);
            } catch (Exception e) {
                return Surface.ROTATION_0;
            }
        } else {
            return display.getOrientation();
        }
    }

}
