package com.kys.playercontrol.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Random;

/**
 * Created by bsy on 2016/4/12.
 */
public class CommonApi {

    /**
     * 网络是否可用
     *
     * @return
     */
    public static boolean isHaveNet(Context context) {// 获取手机�?��连接管理对象（包括对wi-fi,net等连接的管理�?
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 网络状态(2G,3G,4G,WIFI)
     *
     * @param context
     * @return
     */
    public static String GetNetworkType(Context context) {
        String strNetworkType = "";

        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

            }
        }

        return strNetworkType;
    }

    /**
     * 设备名称
     *
     * @return
     */
    public static String getPhoneType() {
        String model = Build.MODEL;
        String str[] = model.split(" ");
        model = "";
        for (int i = 0; i < str.length; i++) {
            if (i == str.length - 1) {
                model = model + str[i];
            } else {
                model = model + str[i] + "_";
            }
        }
        return model;
    }

    /**
     * 系统版本号
     *
     * @return
     */
    public static String getSdkVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 设备唯一标识符（IMEI）
     *
     * @param context
     */
    public static String getIMIEStatus(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId;
    }

    /**
     * 屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
//        int result = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width;
    }

    /**
     * 屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        return height;
    }

    /**
     * 是否有sd卡
     *
     * @return
     */
    public static boolean isHaveSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        }
        return false;
    }

    /**
     * 客户端版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        PackageManager manager;

        PackageInfo info = null;

        manager = context.getPackageManager();


        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;

    }

    /**
     * 客户端版本code
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        PackageManager manager;

        PackageInfo info = null;

        manager = context.getPackageManager();


        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionCode;

    }

    //根据IP获取本地Mac
    public static String getLocalMacAddressFromIp(Context context) {
        String mac_s = "";
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            mac_s = byte2hex(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mac_s;
    }

    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }

    //获取本地IP
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {

        }

        return null;
    }

    /**
     * 将dp转成px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获取随机数
     *
     * @return
     */
    public static String getRandom(int max) {

        String random;

        Random mRandom = new Random();
        random = mRandom.nextInt(max) + "";
        return random;
    }

    /**
     * urlencode编码
     *
     * @param s
     * @return
     */
    public static String urlEncode(String s) {
        if (s == null) {
            return s;
        }
        String str = s;
        try {
            str = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    /**
     * urldecode解码
     *
     * @param
     * @return
     */
    public static String urlDecode(String s) {
        if (s == null) {
            return s;
        }
        String str = s;
        try {
            str = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取屏幕大小
     *
     * @param
     * @return
     */
    public static DisplayMetrics getMetrics(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 获取Android系统时间
     *
     * @return
     */
    public static String getSystemDate() {
        String date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());//HH:24小时制，hh：12小时制
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        date = sdf.format(curDate);
        return date;
    }

    /**
     * 获取Android系统时间
     *
     * @return
     */
    public static String getSystemAllDate() {
        String date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());//HH:24小时制，hh：12小时制
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        date = sdf.format(curDate);
        return date;
    }

    /**
     * 计算epg信息是否是正在播出的,传入时间的格式为（hhmm）。通过将时间转化成秒，进行计算
     *
     * @param starttime
     * @param runtime
     * @return
     */
    public static int playtime_sub(String starttime, String runtime) {
        String nowtime = getSystemDate().substring(8, 12);
        int sub = cast2second(starttime) + cast2second(runtime) - cast2second(nowtime);
        return sub;
    }

    /**
     * 计算epg信息是否是正在播出的,传入时间的格式为（hhmm）。通过将时间转化成秒，进行计算
     *
     * @param time
     * @return
     */
    public static int playtime_sub(String time) {
        String nowtime = getSystemAllDate().substring(8, 12);
        time = time.replaceAll(":", "");
        int sub = cast2second(time) - cast2second(nowtime);
        return sub;
    }

    /**
     * 将String时间（格式hhmm）转为int型(单位为s)
     *
     * @param str_time
     * @return
     */
    public static int cast2second(String str_time) {
        if (str_time.equals("")) {
            return 0;
        }
        int time = Integer.parseInt(str_time.substring(0, 2)) * 3600 + Integer.parseInt(str_time.substring(2)) * 60;
        return time;
    }

    /**
     * 判断是否是当天的日期
     *
     * @param str_date
     * @return
     */
    public static long playDate_sub(String str_date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        String nowData = getSystemDate();
        long sub = 0;
        try {
            Date now = format.parse(nowData);
            Date date = format.parse(str_date);
            sub = date.getTime() - now.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sub;
    }

    /**
     * 获取回看的影片时长，用结束时间减去开始时间
     *
     * @param begin_time
     * @param end_time
     * @return
     */
    public static long getRuntime(String begin_time, String end_time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        long sub = 0;
        try {
            Date now = format.parse(begin_time);
            Date date = format.parse(end_time);
            sub = date.getTime() - now.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sub;
    }

    /**
     * 毫秒转化
     */
    public static String formatTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        if (hour > 0) {
            return strHour + " : " + strMinute + " : " + strSecond;
        } else {
            return strMinute + " : " + strSecond;
        }
    }

    public static String  formatDataAddSpace(String str){
        String data;
        String str1=str.substring(0,10);
        String str2=str.substring(10,str.length());
        data=str1+"  "+str2;
        return data;
    }
}
