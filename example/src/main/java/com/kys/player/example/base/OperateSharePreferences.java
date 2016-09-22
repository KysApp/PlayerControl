package com.kys.player.example.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * Created by 幻云紫日 on 2016/4/13.
 */
public class OperateSharePreferences {
    private final String TAG = OperateSharePreferences.class.getSimpleName();
    /****************首选项文件名******************/
    /**
     * 存储用户信息文件名
     */
    private final String SP_FILE_NAME_USER = "userInfo";
    /**
     * 存储程序的各种设置
     */
    private final String SP_FILE_NAME_SETTING = "setting";
    /****************
     * Setting信息
     ******************/
    private final String SP_NET_STATE = "netState";
    private final String SP_IS_WIFI = "isWifi";
    private final String SP_HAVE_GPRS = "haveGprs";
    private final String SP_PLAY_MODEL = "playModel";
    private final String SP_DOWNLOAD_MODEL = "downloadModel";
    private final String SP_IS_USE_GPRS = "isUseGprs";
    private final String SP_IS_PUSH = "isPush";
    private final String SP_IS_SCHEDULE = "isSchedule";
    private final String SP_VERSION = "versionCode";

    /****************
     * 用户信息
     ******************/
    private final String SP_LOGIN_STATE = "isLogin";
    private final String SP_SESSION="session";
    private final String SP_USER_ID = "userId";
    private final String SP_USER_NAME = "userName";
    private final String SP_USER_PASSWORD = "password";
    private final String SP_USER_TOKEN = "token";
    private final String SP_USER_REFRESH_TOKEN = "refresh_token";
    private final String SP_USER_IS_AUTO_LOGIN = "isAutoLogin";
    private final String SP_USER_PIC = "userPic";
    private final String SP_USER_SNANE = "userSName";
    private final String SP_USER_SEX = "userSex";
    private final String SP_USER_AREA = "userArea";
    private final String SP_USER_BIRTH = "userBirth";
    private final String SP_USER_BLOOD = "userBirth";
    private final String SP_USER_CONSTELLATION = "userConstellation";
    private final String SP_USER_PHONE = "userPhone";
    private final String SP_USER_E_MAIL = "userEmail";
    private final String SP_USER_DESC = "userDesc";
    private final String SP_USER_PROFESSION = "userProfession";
    private final String SMALL_VOL_BRI = "smallVolBri";
    private final String FULL_VOL_BRI = "fullVolBri";
    private static OperateSharePreferences my;
    private SharedPreferences mStateSharedPreferences;
    private static SharedPreferences mUserInfoSharedPreferences;

    private OperateSharePreferences(Context context) {
        // TODO Auto-generated constructor stub
        mUserInfoSharedPreferences = context.getSharedPreferences(SP_FILE_NAME_USER, Context.MODE_PRIVATE);
        mStateSharedPreferences = context.getSharedPreferences(SP_FILE_NAME_SETTING, Context.MODE_PRIVATE);
    }

    public static OperateSharePreferences getInstance(Context context) {
        if (my == null) {
            my = new OperateSharePreferences(context);
        }
        return my;
    }

    /**
     * 保存是否有网络
     *
     * @param netState
     */
    public void saveNetState(boolean netState) {
        mStateSharedPreferences.edit().putBoolean(SP_NET_STATE, netState).commit();

    }

    /**
     * 是否有网络
     *
     * @return
     */
    public boolean getNetState() {
        return mStateSharedPreferences.getBoolean(SP_NET_STATE, false);

    }

    /**
     * 保存是否有wifi
     *
     * @param isWifi
     */
    public void saveIsWifi(boolean isWifi) {
        mStateSharedPreferences.edit().putBoolean(SP_IS_WIFI, isWifi).commit();

    }

    /**
     * 是否是wifi
     *
     * @return
     */
    public boolean isWifi() {
        if (getNetState()) {
            return mStateSharedPreferences.getBoolean(SP_IS_WIFI, false);
        }
        return false;

    }

    /**
     * 保存是否有gprs
     *
     * @param isgprs
     */
    public void saveIsGPRS(boolean isgprs) {
        mStateSharedPreferences.edit().putBoolean(SP_HAVE_GPRS, isgprs).commit();

    }

    /**
     * 是否是有GPRS
     *
     * @return
     */
    public boolean isGPRS() {
        if (getNetState()) {
            return mStateSharedPreferences.getBoolean(SP_HAVE_GPRS, false);
        }
        return false;

    }

    /**
     * 保存优先播放模式
     * 1：标清（SD ）
     * 2：标清高码率（SD-H ）
     * 4：高清（HD）
     *
     * @param PlayModel
     */
    public void savePlayModel(String PlayModel) {
        mStateSharedPreferences.edit().putString(SP_PLAY_MODEL, PlayModel).commit();

    }

    /**
     * 返回优先播放模式
     *
     * @return
     */
    public String getPlayModel() {

        return mStateSharedPreferences.getString(SP_PLAY_MODEL, "1");
    }


    /**
     * 保存优先下载模式
     *
     * @param DownloadModel
     */
    public void saveDownloadModel(String DownloadModel) {
        mStateSharedPreferences.edit().putString(SP_DOWNLOAD_MODEL, DownloadModel).commit();

    }

    /**
     * 优先下载模式
     *
     * @return
     */
    public String getDownloadModel() {

        return mStateSharedPreferences.getString(SP_DOWNLOAD_MODEL, "超清模式");
    }

    /**
     * 保存是否可以使用2g/3g网络
     *
     * @param isUseGprs
     */
    public void saveIsUseGprs(boolean isUseGprs) {
        mStateSharedPreferences.edit().putBoolean(SP_IS_USE_GPRS, isUseGprs).commit();

    }

    /**
     * 是否可以使用2g/3g网络
     *
     * @return
     */
    public boolean isUseGprs() {

        return mStateSharedPreferences.getBoolean(SP_IS_USE_GPRS, true);
    }

    /**
     * 保存是否接收推送
     *
     * @param isPush
     */
    public void saveIsPush(boolean isPush) {
        mStateSharedPreferences.edit().putBoolean(SP_IS_PUSH, isPush).commit();

    }

    /**
     * 是否接收推送
     *
     * @return
     */
    public boolean isPush() {

        return mStateSharedPreferences.getBoolean(SP_IS_PUSH, false);
    }

    /**
     * 保存版本号
     *
     * @param vesionCode
     */
    public void saveVersion(int vesionCode) {
        mStateSharedPreferences.edit().putInt(SP_VERSION, vesionCode).commit();

    }

    /**
     * 获取版本号
     *
     * @return
     */
    public int getVersion() {

        return mStateSharedPreferences.getInt(SP_VERSION, 0);
    }


    /**
     * @param netState
     */
    public void saveLoginState(boolean netState) {
        mUserInfoSharedPreferences.edit().putBoolean(SP_LOGIN_STATE, netState).commit();

    }

    /**
     * 是否已登录
     *
     * @return
     */
    public boolean getLoginState() {
        return mUserInfoSharedPreferences.getBoolean(SP_LOGIN_STATE, false);

    }

    /**
     * 获取用户session
     * @return
     */
    public String getSession() {
        return mUserInfoSharedPreferences.getString(SP_SESSION,"");
    }

    /**
     * 保存用户session
     * @param session
     */
    public void saveSession(String session){
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_SESSION,session);
        edit.commit();
    }
    /**
     * 保存是否使用预定
     *
     * @param isSchedule
     */
    public void saveIsUseSchedule(boolean isSchedule) {
        mStateSharedPreferences.edit().putBoolean(SP_IS_SCHEDULE, isSchedule).commit();

    }

    /**
     * 是否使用预定
     *
     * @return
     */
    public boolean isUseSchedule() {

        return mStateSharedPreferences.getBoolean(SP_IS_SCHEDULE, true);
    }

    /**
     * @param password
     * @param userName
     * @param token
     */
    public void saveUser(String password, String userName, String token, String refresh_token) {
        Editor edit = mUserInfoSharedPreferences.edit();
        if (!password.equals("")) {//密码为空，则是在更新token，所以不要对用户名和密码进行修改
            edit.putString(SP_USER_PASSWORD, password);
            edit.putString(SP_USER_NAME, userName);
        }
        edit.putString(SP_USER_TOKEN, token);
        edit.putString(SP_USER_REFRESH_TOKEN, refresh_token);
        edit.commit();
    }

    /**
     * 获取token
     *
     * @return
     */
    public String getToken() {
        return mUserInfoSharedPreferences.getString(SP_USER_TOKEN, "");
    }

    /**
     * 获取刷新令牌
     *
     * @return
     */
    public String getRefreshToken() {
        return mUserInfoSharedPreferences.getString(SP_USER_REFRESH_TOKEN, "");
    }

    /**
     * @param userSName
     * @param userSex
     * @param userArea
     * @param profession
     */
    public void saveUserInfo1(String userSName, String userSex, String userArea, String profession) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_SNANE, userSName);
        edit.putString(SP_USER_SEX, userSex);
        edit.putString(SP_USER_AREA, userArea);
        edit.putString(SP_USER_PROFESSION, profession);
        edit.commit();
    }

    /**
     * @param userBirth
     * @param userBlood
     * @param userConstellation
     */
    public void saveUserInfo2(String userBirth, String userBlood, String userConstellation) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_BIRTH, userBirth);
        edit.putString(SP_USER_BLOOD, userBlood);
        edit.putString(SP_USER_CONSTELLATION, userConstellation);
        edit.commit();
    }

    /**
     * @param userPhone
     * @param userEmail
     * @param userDesc
     */
    public void saveUserInfo3(String userPhone, String userEmail, String userDesc) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_PHONE, userPhone);
        edit.putString(SP_USER_E_MAIL, userEmail);
        edit.putString(SP_USER_DESC, userDesc);
        edit.commit();
    }

    /**
     * @param userSName
     */
    public void saveSName(String userSName) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_SNANE, userSName);
        edit.commit();
    }
    public void saveUserName(String userName) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_NAME, userName);
        edit.commit();
    }
    public void saveSex(String userSex) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_SEX, userSex);
        edit.commit();
    }

    public void saveArea(String userArea) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_AREA, userArea);
        edit.commit();
    }

    public void saveBirth(String userBirth) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_BIRTH, userBirth);
        edit.commit();
    }

    public void saveBlood(String userBlood) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_BLOOD, userBlood);
        edit.commit();
    }

    public void saveUserConstellation(String userConstellation) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_CONSTELLATION, userConstellation);
        edit.commit();
    }

    public void savePhone(String userPhone) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_PHONE, userPhone);
        edit.commit();
    }

    public void saveEmail(String userEmail) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_E_MAIL, userEmail);
        edit.commit();
    }

    public void saveUserDesc(String userDesc) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_DESC, userDesc);
        edit.commit();
    }

    public void savePassword(String password) {
        Editor edit = mUserInfoSharedPreferences.edit();
        edit.putString(SP_USER_PASSWORD, password);
        edit.commit();
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserId() {
        return mUserInfoSharedPreferences.getString(SP_USER_ID, "");
//		return "20000";
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserName() {
        return mUserInfoSharedPreferences.getString(SP_USER_NAME, "");
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserSName() {
        return mUserInfoSharedPreferences.getString(SP_USER_SNANE, "");
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserSex() {
        return mUserInfoSharedPreferences.getString(SP_USER_SEX, "");
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserArea() {
        return mUserInfoSharedPreferences.getString(SP_USER_AREA, "");
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserBlood() {
        return mUserInfoSharedPreferences.getString(SP_USER_BLOOD, "");
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserBirth() {
        return mUserInfoSharedPreferences.getString(SP_USER_BIRTH, "");
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserConstellation() {
        return mUserInfoSharedPreferences.getString(SP_USER_CONSTELLATION, "");
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserPhone() {
        return mUserInfoSharedPreferences.getString(SP_USER_PHONE, "");
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserEmail() {
        return mUserInfoSharedPreferences.getString(SP_USER_E_MAIL, "");
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserProfession() {
        return mUserInfoSharedPreferences.getString(SP_USER_PROFESSION, "");
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserDesc() {
        return mUserInfoSharedPreferences.getString(SP_USER_DESC, "");
    }

    /**
     * 默认为""
     *
     * @return
     */
    public String getUserPic() {
        return mUserInfoSharedPreferences.getString(SP_USER_PIC, "");
    }

    /**
     * @return
     */
    public String getPassword() {
        return mUserInfoSharedPreferences.getString(SP_USER_PASSWORD, "");
    }

    /**
     * @return
     */
    public String getProfession() {
        return mUserInfoSharedPreferences.getString(SP_USER_PROFESSION, "");
    }

    /**
     * 设置是否自动登录
     *
     * @param isAutoLogin
     */
    public void saveIsAutoLogin(boolean isAutoLogin) {
        mUserInfoSharedPreferences.edit().putBoolean(SP_USER_IS_AUTO_LOGIN, isAutoLogin).commit();
    }

    /**
     * 是否自动登录
     *
     * @return
     */
    public boolean isAutoLogin() {
        return mUserInfoSharedPreferences.getBoolean(SP_USER_IS_AUTO_LOGIN, false);
    }

    /**
     * 设置是否显示声音亮度
     *
     * @param isShow
     */
    public void saveSmallVolBri(boolean isShow) {
        mUserInfoSharedPreferences.edit().putBoolean(SMALL_VOL_BRI, isShow).commit();
    }

    /**
     * 是否显示声音亮度
     *
     * @return
     */
    public boolean isSmallVolBri() {
        return mUserInfoSharedPreferences.getBoolean(SMALL_VOL_BRI, false);
    }

    /**
     * 设置是否显示声音亮度
     *
     * @param isShow
     */
    public void saveFullVolBri(boolean isShow) {
        mUserInfoSharedPreferences.edit().putBoolean(FULL_VOL_BRI, isShow).commit();
    }

    /**
     * 是否显示声音亮度
     *
     * @return
     */
    public boolean isFullVolBri() {
        return mUserInfoSharedPreferences.getBoolean(FULL_VOL_BRI, false);
    }
}
