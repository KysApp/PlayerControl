package com.kys.player.example.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by bsy on 2016/4/14.
 */
public class MyHorizontalScrollView extends HorizontalScrollView {
    Context mContext;
    IOnOverScrolledObserver mObserver;

    /*
     * 设置滚动监视器
     */
    public void setOnOverScrolledObserver(IOnOverScrolledObserver observer) {
        this.mObserver = observer;
    }

    /**
     * construct
     */
    public MyHorizontalScrollView(Context context) {
        super(context);
        init(context);
    }

    /**
     * construct
     */
    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * construct
     */
    public MyHorizontalScrollView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /*
     * 初始化信息
     */
    void init(Context context) {
        mContext = context;
    }

    /*
     * 手指滑动事件
     *
     * @see android.widget.MyHorizontalScrollView#fling(int)
     */
    @Override
    public void fling(int velocityX) {
        super.fling(velocityX);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        mObserver.onScrollChanged(l, t, oldl, oldt);
        super.onScrollChanged(l, t, oldl, oldt);

    }
}
