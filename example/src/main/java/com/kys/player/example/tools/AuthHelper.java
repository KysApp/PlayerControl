package com.kys.player.example.tools;

import com.kys.player.example.base.MyApplication;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by bsy on 2016/7/29.
 * 鉴权类
 */
public class AuthHelper {
    static String authorizationid;

    public static String doAuth(String programcode, String columncode, String contenttype) {
        OkHttpUtils.get().url(MyApplication.HOST_URL + "doauth.jsp").
                addParams("programcode", programcode).addParams("columncode", columncode).addParams("contenttype", contenttype).addParams("mediaservices", "2")
                .addHeader("Cookie", "JSESSIONID=" + MyApplication.JSESSIONID).
                build().execute(new JsonObjectCallback() {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                authorizationid = response.optJSONArray("authorizationid").optString(0);
            }
        });
        return authorizationid;
    }
}
