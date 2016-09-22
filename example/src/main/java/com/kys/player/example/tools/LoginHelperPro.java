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
public class LoginHelperPro {
    private SDKLoginMgr mLoginMgr = null;
    static private Context mcontext;
    LoginHelperListener loginHelperListener;

    public static LoginHelperPro getInstance(Context context) {
        mcontext = context;
        return LoginHelperHolder.mInstance;
    }

    private static class LoginHelperHolder {
        private static final LoginHelperPro mInstance = new LoginHelperPro();
    }

    private LoginHelperPro() {

    }


    /**
     * 需要登录或者令牌刷新时，直接调用该方法，所有逻辑判断在该方法中完成，调用之前无需再做任何判断
     */
    public void login(LoginHelperListener loginHelperListener) {
        this.loginHelperListener = loginHelperListener;
        OperateSharePreferences op = OperateSharePreferences.getInstance(mcontext);
//        if (!op.getSession().equals("")) {//有session，则直接进行数据解析
//            loginHelperListener.isLogin();
//        } else
        if (!op.getUserName().equals("")) {//有用户信息，则进行登录接口
            doLogin();
        } else {//都没有，则跳转至登录页面
            loginHelperListener.goLogin();
        }
    }

    /**
     * 中兴登录操作
     */
    private void doLogin() {
        initZXLoginSDK();//初始化中兴sdk
        mLoginMgr.startLogin(OperateSharePreferences.getInstance(mcontext).getUserName(), OperateSharePreferences.getInstance(mcontext).getPassword());
    }

    /**
     * 中兴退出登录操作
     */
    public void Logout(LoginHelperListener loginHelperListener){
        this.loginHelperListener = loginHelperListener;
        initZXLoginSDK();//初始化中兴sdk
        mLoginMgr.startLogout();
    }

    private void initZXLoginSDK() {
        if (mLoginMgr == null) {
            mLoginMgr = SDKLoginMgr.getInstance();
            mLoginMgr.initLogin("2", "192.168.199.248", "08-3E-8E-8B-00-EE", "11111");
            mLoginMgr.setLoginReturnListener(new SDKLoginMgr.ISDKLoginReturnListener() {
                @Override
                public void onLoginReturn(String s, String s1) {
                    if (s.equals("0")) {//登录成功
                        OperateSharePreferences.getInstance(mcontext).saveLoginState(true);
                        MyApplication.JSESSIONID = mLoginMgr.getHTTPSessionID();
                        OperateSharePreferences.getInstance(mcontext).saveSession(mLoginMgr.getHTTPSessionID());
                        setMediaService();
                        setLauguage();
                        loginHelperListener.isReLogin();
                    } else if (s.equals("70116206")) {//用户名密码有误
                        Toast.makeText(mcontext, s1, Toast.LENGTH_SHORT).show();
                        OperateSharePreferences.getInstance(mcontext).saveLoginState(false);
                        OperateSharePreferences.getInstance(mcontext).saveUserName("");
                        OperateSharePreferences.getInstance(mcontext).savePassword("");
                        OperateSharePreferences.getInstance(mcontext).saveSession("");
//                        MyApplication.JSESSIONID = "";
                        loginHelperListener.isLogout();
                    } else {//其他错误
                        Toast.makeText(mcontext, s1, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onLogoutReturn(String s, String s1) {
                    if (s.equals("0")) {
                        OperateSharePreferences.getInstance(mcontext).saveLoginState(false);
                        OperateSharePreferences.getInstance(mcontext).saveUserName("");
                        OperateSharePreferences.getInstance(mcontext).savePassword("");
                        OperateSharePreferences.getInstance(mcontext).saveSession("");
                        MyApplication.JSESSIONID = "";
                        loginHelperListener.isLogout();
                    } else {
                        Toast.makeText(mcontext, s1, Toast.LENGTH_SHORT).show();
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

        void goLogin();
    }
}
