package com.kys.player.example.tools;

import com.zhy.http.okhttp.callback.Callback;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import okhttp3.Response;

/**
 * Created by bsy on 2016/4/14.
 */
public abstract class InputStreamCallback extends Callback<InputStream> {
    @Override
    public InputStream parseNetworkResponse(Response response) throws Exception {
        String res = response.body().string();
        InputStream in_nocode = new ByteArrayInputStream(res.getBytes());
        return in_nocode;
    }
}