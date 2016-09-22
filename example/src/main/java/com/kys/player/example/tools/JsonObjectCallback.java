package com.kys.player.example.tools;

import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by bsy on 2016/7/28.
 */
public abstract class JsonObjectCallback extends Callback<JSONObject> {
    @Override
    public JSONObject parseNetworkResponse(Response response) throws Exception {
        String res = response.body().string();
        JSONObject obj = null;
        obj = new JSONObject(res);
        return obj;
    }

    @Override
    public void onError(Call call, Exception e) {

    }

    @Override
    public void onResponse(JSONObject response) {
        if (response.optString("returncode").equals("-1001")) {//未登录

        }
    }
}
