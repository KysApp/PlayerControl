package com.kys.player.example.tools;

import android.content.Context;
import android.widget.Toast;

import com.kys.player.example.R;
import com.kys.player.example.base.MyApplication;
import com.kys.player.example.base.OperateSharePreferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by bsy on 2016/8/22.
 */
public class HttpJsonObjectHelper {
    static private Context mcontext;
    JSONObject obj;
    HttpJsonObjectHelperListener httpJsonObjectHelperListener;

    public static HttpJsonObjectHelper getInstance(Context context) {
        mcontext = context;
        return HttpJsonObjectHelperHolder.mInstance;
    }

    private static class HttpJsonObjectHelperHolder {
        private static final HttpJsonObjectHelper mInstance = new HttpJsonObjectHelper();
    }

    /**
     * get方式获取数据
     *
     * @param url
     * @param map
     * @param listener
     */
    public void getHttpNeedTokenData(String url, Map<String, String> map, HttpJsonObjectHelperListener listener) {
        httpJsonObjectHelperListener = listener;
        RequestParams params = new RequestParams(url);
        Iterator<String> iterator = map.keySet().iterator();
        for (int i = 0; i < map.size(); i++) {
            String key = iterator.next();
            params.addBodyParameter(key, map.get(key));
        }
        params.setHeader("Cookie", "JSESSIONID=" + OperateSharePreferences.getInstance(mcontext).getSession());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!result.equals("") && result != null) {
                    obj = null;
                    try {
                        obj = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (obj.optString("returncode").equals("-1001")) {//session失效或者未登录
                        LoginHelperPro.getInstance(mcontext).login(new LoginHelperPro.LoginHelperListener() {
                            @Override
                            public void isLogin() {
                                httpJsonObjectHelperListener.onSuccess(obj);
                            }

                            @Override
                            public void isReLogin() {
                                httpJsonObjectHelperListener.onRefreshData();
                            }

                            @Override
                            public void isLogout() {

                            }

                            @Override
                            public void goLogin() {
                                httpJsonObjectHelperListener.goLogin();
                            }
                        });
                    } else {//登录状态正常，直接去解析数据
                        httpJsonObjectHelperListener.onSuccess(obj);
                    }
                } else {
                    Toast.makeText(mcontext, mcontext.getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(mcontext, mcontext.getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                httpJsonObjectHelperListener.onErrors(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * post方式获取数据
     *
     * @param url
     * @param map
     * @param listener
     */
    public void postHttpNeedTokenData(String url, Map<String, String> map, HttpJsonObjectHelperListener listener) {
        httpJsonObjectHelperListener = listener;
        RequestParams params = new RequestParams(url);
        Iterator<String> iterator = map.keySet().iterator();
        for (int i = 0; i < map.size(); i++) {
            String key = iterator.next();
            params.addBodyParameter(key, map.get(key));
        }
        params.setHeader("Cookie", "JSESSIONID=" + OperateSharePreferences.getInstance(mcontext).getSession());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!result.equals("") && result != null) {
                    obj = null;
                    try {
                        obj = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (obj.optString("returncode").equals("-1001")) {//session失效或者未登录
                        LoginHelperPro.getInstance(mcontext).login(new LoginHelperPro.LoginHelperListener() {
                            @Override
                            public void isLogin() {
                                httpJsonObjectHelperListener.onSuccess(obj);
                            }

                            @Override
                            public void isReLogin() {
                                httpJsonObjectHelperListener.onRefreshData();
                            }

                            @Override
                            public void isLogout() {

                            }

                            @Override
                            public void goLogin() {
                                httpJsonObjectHelperListener.goLogin();
                            }
                        });
                    } else {//登录状态正常，直接去解析数据
                        httpJsonObjectHelperListener.onSuccess(obj);
                    }
                } else {
                    Toast.makeText(mcontext, mcontext.getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mcontext, ex.getMessage(), Toast.LENGTH_SHORT).show();
                httpJsonObjectHelperListener.onErrors(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public interface HttpJsonObjectHelperListener {
        void onSuccess(JSONObject response);//处理接口数据

        void onErrors(Throwable ex, boolean isOnCallback);

        void onRefreshData();//刷新当前接口

        void goLogin();//去登录页面
    }
}