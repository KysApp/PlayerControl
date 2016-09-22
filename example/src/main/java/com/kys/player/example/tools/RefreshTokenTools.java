package com.kys.player.example.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.kys.player.example.base.CommonApi;
import com.kys.player.example.base.MyApplication;
import com.kys.player.example.base.OperateSharePreferences;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by bsy on 2016/5/5.
 */
public class RefreshTokenTools {

    /**
     * 使用刷新令牌获取新令牌
     */
    public static void getNewToken(final Context context) {
        String url = MyApplication.HOME_HOST + "/EPG/oauth/v2/token?grant_type=refresh_token&refresh_token=" +
                OperateSharePreferences.getInstance(context).getRefreshToken() +
                "&client_id=AndroidPhone&client_secret=LbTvXj";

        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                //刷新令牌失效，则重新登录
                login(context);
//                handler.sendEmptyMessageDelayed(0, 2000);
            }

            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (obj.optString("error").equals("") || obj.optString("error") == null) {
                    OperateSharePreferences.getInstance(context).saveUser("", "", obj.optString("access_token"), obj.optString("refresh_token"));
                } else {
                    login(context);
                }
            }
        });
    }

    public static void login(final Context context) {
        getEncryToken(OperateSharePreferences.getInstance(context).getUserName(), OperateSharePreferences.getInstance(context).getPassword(), context);

    }

    public static void getToken(final String name, final String password, String infos, final Context context) {

        String url = MyApplication.HOME_HOST + "/EPG/oauth/v2/token?grant_type=EncryToken&client_id=AndroidPhone&UserID=" +
                name +
                "&DeviceType=&DeviceVersion=&authinfo=" +
                infos +
//                "&userdomain=2&datadomain=3&accountType=1";
                "&userdomain=1&datadomain=1&accountType=1";
        Log.e("token_url", url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                JSONObject error = null;
                try {
                    error = new JSONObject(e.getMessage());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if(error.length()==0){
                    return;
                }
                if (error.optString("error").equals("user_notexist")) {
                    Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show();
                } else if (error.optString("error").equals("invalid_password")) {
                    Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                }

                Log.e("error", "error");
            }

            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String token = obj.optString("access_token");
                String refresh_token = obj.optString("refresh_token");
                OperateSharePreferences.getInstance(context).saveLoginState(true);
                OperateSharePreferences.getInstance(context).saveUser(password, name, token, refresh_token);
                Log.e("succeed", token);
            }
        });
    }

    /**
     * 3DES(Random+“$”+EncryToken+”$”+UserID+”$”+DeviceID+”$”+IP+”$”+MAC+”$”+ Reserved+ ”$”+ “OTT”)
     * 通过用户密码进行加密
     *
     * @return
     */
    public static void get3DESInfo(String name, String password, String encryToken, Context context) {

//        String encryToken = getEncryToken(name);
        String[] info = {CommonApi.getRandom(1000) + "$" + encryToken + "$" + name + "$" + CommonApi.getIMIEStatus(context) + "$" + CommonApi.getLocalIpAddress() + "$" + CommonApi.getLocalMacAddressFromIp(context) + "$$" + "OTT"};
//        String[] info ={"37304881$8A4EC6EA3191CC6194397BB5731A31C$18599018070$2843EFC6-6CF4-4F80-935B-456D9BF74FE1$192.168.0.106$468D8111-751E-4163-9A14-7F7E33FE072D$$OTT"};
        String infos = ThreeDES.main(info, password);
        getToken(name, password, infos, context);

    }


    static String encryToken;

    /**
     * 通过用户名获取挑战字
     *
     * @param name
     * @return
     */
    public static String getEncryToken(final String name, final String password, final Context context) {
        encryToken = "";
        OkHttpUtils.get().url(MyApplication.HOME_HOST + "/EPG/oauth/v2/authorize?response_type=EncryToken&client_id=AndroidPhone&userid=" + name).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                encryToken = obj.optString("EncryToken");
                get3DESInfo(name, password, encryToken, context);
            }
        });
        return encryToken;
    }
//    public static void login(final Context context) {
//
//
//
//
//
//
//        String url = MyApplication.HOME_HOST + "/EPG/oauth/v2/token?grant_type=EncryToken&client_id=AndroidPhone&UserID=" +
//                OperateSharePreferences.getInstance(context).getUserName() +
//                "&DeviceType=&DeviceVersion=&authinfo=" +
//                get3DESInfo(OperateSharePreferences.getInstance(context).getUserName(), OperateSharePreferences.getInstance(context).getPassword(), context) +
//                "&userdomain=1&datadomain=1&accountType=1";
//        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e) {
//            }
//
//            @Override
//            public void onResponse(String response) {
//                JSONObject obj = null;
//                try {
//                    obj = new JSONObject(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                String token = obj.optString("access_token");
//                String refresh_token = obj.optString("refresh_token");
//                OperateSharePreferences.getInstance(context).saveLoginState(true);
//                OperateSharePreferences.getInstance(context).saveUser("", "", token, refresh_token);
//                Log.e("succeed", token);
//            }
//        });
//    }
//
//    /**
//     * 3DES(Random+“$”+EncryToken+”$”+UserID+”$”+DeviceID+”$”+IP+”$”+MAC+”$”+ Reserved+ ”$”+ “OTT”)
//     * 通过用户密码进行加密
//     *
//     * @return
//     */
//    public static String get3DESInfo(String name, String password, Context context) {
//        String infos;
//        String[] info = {CommonApi.getRandom(1000) + "$" + getEncryToken(name) + "$" + name + "$" + CommonApi.getIMIEStatus(context) + "$" + CommonApi.getLocalIpAddress() + "$" + CommonApi.getLocalMacAddressFromIp(context) + "$$" + "OTT"};
//        infos = ThreeDES.main(info, password);
//        return infos;
//    }
//
//   static String encryToken;
//
//    /**
//     * 通过用户名获取挑战字
//     *
//     * @param name
//     * @return
//     */
//    public static String getEncryToken(String name) {
//        encryToken = "";
//        OkHttpUtils.get().url(MyApplication.HOME_HOST + "/EPG/oauth/v2/authorize?response_type=EncryToken&client_id=AndroidPhone&userid=" + name).build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//                JSONObject obj = null;
//                try {
//                    obj = new JSONObject(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                encryToken = obj.optString("EncryToken");
//            }
//        });
//        return encryToken;
//    }
}
