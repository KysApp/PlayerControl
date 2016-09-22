package com.kys.player.example.tools;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kys.player.example.base.MyApplication;
import com.kys.player.example.base.OperateSharePreferences;
import com.kys.player.example.ui.LoginZX;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zte.iptvclient.android.androidsdk.SDKLoginMgr;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by bsy on 2016/8/1.
 * 1、判断是否有用户信息
 * 2、有，则直接登录
 * 3、没有，跳转登录页面
 */
public class LoginHelper {
    private SDKLoginMgr mLoginMgr = null;

    //首次登录使用
    public LoginHelper(Context context, int tag, LoginHelperListener loginHelperListener) {
        initZXLoginSDK(context, loginHelperListener);
        if (!OperateSharePreferences.getInstance(context).getLoginState()) {//没有用户信息，则应该去登录
            Intent intent = new Intent();
            intent.setClass(context, LoginZX.class);
            context.startActivity(intent);
        } else {
            loginHelperListener.isLogin();
        }
    }

    //登录使用
    public LoginHelper(Context context, JSONObject obj, LoginHelperListener loginHelperListener) {
        initZXLoginSDK(context, loginHelperListener);
        if (OperateSharePreferences.getInstance(context).getLoginState()) {//有用户信息
            if ((obj == null || obj.optString("returncode").equals("-1001")) && mLoginMgr != null) {//session失效，自动再登录一遍
                mLoginMgr.startLogin(OperateSharePreferences.getInstance(context).getUserSName(), OperateSharePreferences.getInstance(context).getPassword());
            } else if (loginHelperListener != null) {//用户登录状态正常，去处理接口数据
                loginHelperListener.isLogin();
            }
        } else {//没有用户信息，跳转至登录页面
            Intent intent = new Intent();
            intent.setClass(context, LoginZX.class);
            context.startActivity(intent);

        }
    }

    //退出登录使用
    public LoginHelper(Context context, LoginHelperListener loginHelperListener) {
        initZXLoginSDK(context, loginHelperListener);
        mLoginMgr.startLogout();
    }

    private void initZXLoginSDK(final Context context, final LoginHelperListener loginHelperListener) {
        if (mLoginMgr == null) {
            mLoginMgr = SDKLoginMgr.getInstance();
            mLoginMgr.initLogin("2", "192.168.199.248", "08-3E-8E-8B-00-EE", "11111");
            mLoginMgr.setLoginReturnListener(new SDKLoginMgr.ISDKLoginReturnListener() {
                @Override
                public void onLoginReturn(String s, String s1) {
                    if (s.equals("0")) {
                        OperateSharePreferences.getInstance(context).saveLoginState(true);
                        MyApplication.JSESSIONID = mLoginMgr.getHTTPSessionID();
                        setMediaService();
                        setLauguage();
                        loginHelperListener.isReLogin();
                    }
                    else if (s.equals("70116206")) {//用户名密码有误
                        Toast.makeText(context, s1, Toast.LENGTH_SHORT).show();
                        OperateSharePreferences.getInstance(context).saveLoginState(false);
                        OperateSharePreferences.getInstance(context).saveSName("");
                        OperateSharePreferences.getInstance(context).savePassword("");
//                        MyApplication.JSESSIONID = "";
                        loginHelperListener.isLogout();
                    }
                    else {//登录失败，跳转至登录页面
                        Toast.makeText(context, s1, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onLogoutReturn(String s, String s1) {
                    if (s.equals("0")) {
                        OperateSharePreferences.getInstance(context).saveLoginState(false);
                        OperateSharePreferences.getInstance(context).saveSName("");
                        OperateSharePreferences.getInstance(context).savePassword("");
                        MyApplication.JSESSIONID = "";
                        loginHelperListener.isLogout();
                    } else {
                        Toast.makeText(context, s1, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onHeartbeatReturn(String s, String s1) {

                }

                @Override
                public void onOtherRecvReturn(int i, String s, String s1) {

                }

                @Override
                public void onRefreshUserTokeReturn(int i, String s, String s1) {

                }
            });
        }
    }

    private void setMediaService() {
        OkHttpUtils.get().url(MyApplication.HOST_URL + "setusermediaservices.jsp").addParams("mediaservices", "2")
                .addHeader("Cookie", "JSESSIONID=" + mLoginMgr.getHTTPSessionID()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {

            }
        });
    }

    private void setLauguage() {
        OkHttpUtils.get().url(MyApplication.HOST_URL + "setuserlanguage.jsp").addParams("languagetype", "zho").
                addHeader("Cookie", "JSESSIONID=" + mLoginMgr.getHTTPSessionID()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
            }
        });
    }

    public interface LoginHelperListener {
        /**
         * 正常登录的状态，下一步就去处理接口数据即可
         */
        void isLogin();

        /**
         * 自动重新登录的状态，下一步应该去重新发送网络请求
         */
        void isReLogin();

        /**
         * 退出登录
         */
        void isLogout();
    }
}
