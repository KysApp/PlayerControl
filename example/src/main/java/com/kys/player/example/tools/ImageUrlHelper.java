package com.kys.player.example.tools;

import com.kys.player.example.base.MyApplication;

/**
 * Created by bsy on 2016/7/29.
 */
public class ImageUrlHelper {

    public static String getImageUrl(String url) {
        if(url==null||url.equals("")){
            return "";
        }
//        url = url.replace("..", MyApplication.IMG_URL);
        url = MyApplication.IMG_URL+url;
        url = url.replaceAll(";", "");
        return url;
    }
}
