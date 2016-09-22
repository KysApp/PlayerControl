package com.kys.player.example.base;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import org.xutils.x;

import java.net.InetAddress;
import java.util.Locale;

/**
 * Created by bsy on 2016/4/12.
 */
public class MyApplication extends MultiDexApplication {
    private static MyApplication mContext, mInstance;
    public final static String TAG = "MyApplication";
    public final static String SLEEP_INTENT = "org.videolan.vlc.SleepIntent";
    private static MyApplication instance;
    public final static String HOME_HOST = "http://117.146.192.164:33200";
    private static final String DESCRIPTOR = "android_test";
    //接口
    private static InetAddress inetAddress;
    private static String hostAddress;
    private static String hostName;
    public final static String HOST_URL= "http://120.205.32.199:8080/iptvepg/datasource/";
    public final static String IMG_URL= "http://120.205.32.199:8080/iptvepg/";
    public static String JSESSIONID;
    private static Typeface Sonti, Heiti;
    public static void setLocalIpAddress(InetAddress inetAddr) {
        inetAddress = inetAddr;

    }

    public static InetAddress getLocalIpAddress() {
        return inetAddress;
    }

    public static String getHostAddress() {
        return hostAddress;
    }

    public static void setHostAddress(String hostAddress) {
        MyApplication.hostAddress = hostAddress;
    }

    public static String getHostName() {
        return hostName;
    }

    public static void setHostName(String hostName) {
        MyApplication.hostName = hostName;
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志
        Sonti = Typeface.createFromAsset(getAssets(), "fonts/songti.ttf");
        Heiti = Typeface.createFromAsset(getAssets(), "fonts/heiti.ttf");
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        // Are we using advanced debugging - locale?
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String p = pref.getString("set_locale", "");
        if (p != null && !p.equals("")) {
            Locale locale;
            // workaround due to region code
            if(p.equals("zh-TW")) {
                locale = Locale.TRADITIONAL_CHINESE;
            } else if(p.startsWith("zh")) {
                locale = Locale.CHINA;
            } else if(p.equals("pt-BR")) {
                locale = new Locale("pt", "BR");
            } else if(p.equals("bn-IN") || p.startsWith("bn")) {
                locale = new Locale("bn", "IN");
            } else {
                /**
                 * Avoid a crash of
                 * java.lang.AssertionError: couldn't initialize LocaleData for locale
                 * if the user enters nonsensical region codes.
                 */
                if(p.contains("-"))
                    p = p.substring(0, p.indexOf('-'));
                locale = new Locale(p);
            }
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }

    }

    private PackageInfo getApkInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return info;
    }

    /**
     * Called when the overall system is running low on memory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.w(TAG, "System is running low on memory");

    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }

    /**
     * @return the main context of the Application
     */
    public static MyApplication getAppContext()
    {
        return instance;
    }

    /**
     * @return the main resources from the Application
     */
    public static Resources getAppResources()
    {
        if(instance == null) return null;
        return instance.getResources();
    }

    public static Typeface getSonti() {
        return Sonti;
    }

    public static Typeface getHeiti() {
        return Heiti;
    }

    public static MyApplication getInstance() {
        return instance;
    }

}