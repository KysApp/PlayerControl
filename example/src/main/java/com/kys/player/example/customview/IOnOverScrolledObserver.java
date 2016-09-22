package com.kys.player.example.customview;

/**
 * Created by bsy on 2016/4/14.
 */
public interface IOnOverScrolledObserver {

    void onOverScrolled(int scrollX, int scrollY);

    void onScrollChanged(int l, int t, int oldl, int oldt);

}