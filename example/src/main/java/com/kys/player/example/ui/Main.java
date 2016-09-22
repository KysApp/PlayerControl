package com.kys.player.example.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.kys.player.example.R;
import com.kys.player.example.base.BaseFragment;
import com.kys.player.example.utils.CommonUtils;
import com.zte.iptvclient.android.androidsdk.SDKMgr;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 幻云紫日 on 2016/4/13.
 */
public class Main extends FragmentActivity implements OnCheckedChangeListener,
        OnClickListener {
    private final String TAG = Main.class.getSimpleName();
    private static final String IMAGE_CACHE_DIR = "images";
    public static final String EXTRA_IMAGE = "extra_image";
    Recommend recommend;
    private BaseFragment live, videoClassified, download, my, local, setting;
    private FragmentManager mFragmentManager;
    private RadioGroup rdoGrp_tabs;
    private RadioButton mRbRecommend;
    private Toast mToast;
    private long duration;
    private final int SPACE_TIME = 3000;
    private String nns_time = "";
    private ViewPager mViewPager;
    private ArrayList<View> dots = null;
    private ArrayList<View> mViewList = null;
    private int oldPosition = 0;
    private int currentItem = 0;
    private FrameLayout ui_main;
    private View item_message = null;
    private View dialog_message = null;
    private LinearLayout message_hint;
    private int mScreenWidth;

    //防止字体大小跟随系统变化
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            long now = System.currentTimeMillis();
            if (now - duration > SPACE_TIME) {
                duration = now;
//                mToast.show();
                return;
            }
        }

        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.ui_main);
        Intent intent = getIntent();
        int selected = intent.getIntExtra("selected", 1);

        mFragmentManager = getSupportFragmentManager();
        findView();

        switch (selected) {
            case 1:
                if (recommend == null) {
                    recommend = new Recommend();
                }

                mFragmentManager.beginTransaction()
                        .replace(R.id.content, recommend, "recommend").commit();
                //打开推荐页，在推荐页加载完成之后将查看消息提示
                getRecMessage();
                break;

        }
        rdoGrp_tabs = (RadioGroup) findViewById(R.id.radioGroup1);
        rdoGrp_tabs.setOnCheckedChangeListener(this);
        mRbRecommend = (RadioButton) findViewById(R.id.radio_recommend);

        // 设置底部radioButton的大小
        mScreenWidth = CommonUtils.getScreenWidth(this);
        float perWidth = mScreenWidth / 4;
        float perHeight = perWidth * (147.0f / 282.0f);
        mRbRecommend.setLayoutParams(new LinearLayout.LayoutParams(
                (int) perWidth, (int) perHeight));

    }

    private void findView() {
        // TODO Auto-generated method stub
        rdoGrp_tabs = (RadioGroup) findViewById(R.id.radioGroup1);
        rdoGrp_tabs.setOnCheckedChangeListener(this);
    }

    public void changeGroupChecked(int id) {
        rdoGrp_tabs.check(id);
    }

    /********
     * begin OnCheckedChangeListener
     ***********/
    @Override
    public void onCheckedChanged(RadioGroup arg0, int arg1) {
        // TODO Auto-generated method stub
        /*
         * Clear any backstack before switching tabs. This avoids activating an
		 * old backstack, when a user hits the back button to quit
		 */
        mFragmentManager.popBackStack(null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch (arg1) {
            case R.id.radio_recommend: // 推荐
                if (recommend == null) {
                    recommend = new Recommend();
                }
                mFragmentManager.beginTransaction()
                        .replace(R.id.content, recommend, "recommend").commit();
                break;
        }
        //若程序异常加载数据了，切换tebbar不会出现还有消息弹窗覆盖
        if (dialog_message != null) {
            ui_main.removeView(dialog_message);
            dialog_message = null;
        }
    }

    /********
     * end OnCheckedChangeListener
     ***********/
    /**
     * 该函数用于获取消息弹窗的请求地址
     */
    public void getRecMessage() {
        try {
            nns_time = CommonUtils.getStringPreference(Main.this, "nns_time");
        } catch (NullPointerException e) {
        }
    }

    // 以下方法建议都要
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // Volley.newRequestQueue(this).getCache().clear();
        super.onDestroy();
        SDKMgr.destroySDK();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        HashMap<String, Object> map = (HashMap<String, Object>) v.getTag();
    }

}
