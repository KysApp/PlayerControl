package com.kys.player.example.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;

import java.util.List;

@SuppressLint("NewApi")
public class CommonUtils {

	public static int getSDKVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Activity act) {
		int result = 0;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point point = new Point();
			act.getWindowManager().getDefaultDisplay().getSize(point);
			result = point.x;
		} else {
			result = act.getWindowManager().getDefaultDisplay().getWidth();
		}

		return result;
	}

	@SuppressWarnings("deprecation")
	public static int getScreenHeight(Activity act) {
		int result = 0;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point point = new Point();
			act.getWindowManager().getDefaultDisplay().getSize(point);
			result = point.y;
		} else {
			result = act.getWindowManager().getDefaultDisplay().getHeight();
		}

		return result;
	}

	public static int[] getImageSize(Context context, int resID) {
		int[] size = new int[2];
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resID, options);
		size[0] = options.outWidth;
		size[1] = options.outHeight;
		return size;

	}


    public static boolean isAppOnForeground(Context context) { 
    	ActivityManager activityManager; 
        String packageName;         
        
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE); 
        packageName = context.getPackageName(); 
        // Returns a list of application processes that are running on the device 
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses(); 
        if (appProcesses == null) return false; 
        for (RunningAppProcessInfo appProcess : appProcesses) { 
            // importance: 
            // The relative importance level that the system places  
            // on this process. 
            // May be one of IMPORTANCE_FOREGROUND, IMPORTANCE_VISIBLE,  
            // IMPORTANCE_SERVICE, IMPORTANCE_BACKGROUND, or IMPORTANCE_EMPTY. 
            // These constants are numbered so that "more important" values are 
            // always smaller than "less important" values. 
            // processName: 
            // The name of the process that this object is associated with. 
            if (appProcess.processName.equals(packageName) 
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) { 
                return true; 
            } 
        } 
        return false; 
    } 
    
  
    private final static String SP_NAME  = "cmcchw";
	public static final String KEY_CACHE_CAT = "cache_classification";
	public static final String KEY_CACHE_CATE = "cache_cate";

	public static void saveStringPreference(Context context, String key,
			String value) {
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}

	public static String getStringPreference(Context context, String key) {
		String vaule = "";
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		vaule = sp.getString(key, vaule);
		return vaule;
	}


	static String getString(Context context, int resId) {
		return context.getResources().getString(resId);
	}
	
	/** 
     * 检查是否存在SDCard 
     * @return 
     */  
    public static boolean hasSdcard(){  
        String state = Environment.getExternalStorageState();  
        if(state.equals(Environment.MEDIA_MOUNTED)){  
            return true;  
        }else{  
            return false;  
        }  
    }  
}
