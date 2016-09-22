package com.kys.player.example.selfview;
/**
 * Created by 幻云紫日 on 2016/8/8.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.kys.player.example.base.MyApplication;

/**
 * 作者：幻云紫日 on 2016/8/8 16:10
 */
public class HeiTiTextView extends TextView {

    private Typeface mFace;

    public HeiTiTextView(Context context) {
        super(context);

        init();
    }
    public HeiTiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }
    public HeiTiTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        mFace = MyApplication.getInstance().getHeiti();
        if(mFace!=null)
            this.setTypeface(mFace);
    }
}
