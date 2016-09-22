package com.kys.player.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kys.player.example.R;
import com.kys.player.example.base.CommonApi;
import com.kys.player.example.customview.AutoScrollViewPager;
import com.kys.player.example.customview.MyHorizontalScrollView;
import com.kys.player.example.customview.NoScrollGridView;
import com.kys.player.example.entity.Video;
import com.kys.player.example.selfview.HeiTiTextView;
import com.kys.player.example.ui.PlayLive;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lhl on 2016/8/2.
 */
public class RecommendAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> datas = new ArrayList<>();
    private int index = 0;
    private LinearLayout mRecHintView;
    private final int SPACE_TIME = 4 * 1000;
    private int mRealCount = 0;
    private int width = 0;
    private int height = 0;

    public RecommendAdapter(Context context, List<Map<String, Object>> datas) {
        this.context = context;
        this.datas = datas;
        width = CommonApi.getScreenWidth(context);
        height = CommonApi.getScreenHeight(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        Map<String, Object> map = datas.get(position);
        String type = map.get("type").toString();
        int i;
        switch (type) {
            case "BIGPICTURE":
                i = 0;
                break;
            case "LIVE":
                i = 1;
                break;
            case "VOD":
                i = 2;
                break;
            default:
                i = 2;
                break;
        }
        return i;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BigPicViewHolder bigPicViewHolder;
        LiveViewHolder liveViewHolder;
        VODViewHolder vodViewHolder;
        Map<String, Object> map = datas.get(position);
        Object getOther = map.get("type");
        String type = "";
        if (getOther != null)
            type = getOther.toString();
        Object object = map.get("object");
        if (convertView == null) {
            switch (type) {
                case "BIGPICTURE":
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend_bigpic, null);
                    bigPicViewHolder = new BigPicViewHolder(convertView);
                    initBigPic((List<Map<String, String>>) object, bigPicViewHolder);
                    convertView.setTag(bigPicViewHolder);
                    break;
                case "LIVE":
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend_live_layout, null);
                    convertView.setOnClickListener(new onClickListeners("LIVE"));
                    liveViewHolder = new LiveViewHolder(convertView);
                    initLive((List<Map<String, String>>) object, liveViewHolder);
                    convertView.setTag(liveViewHolder);
                    break;
                case "VOD":
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend_vod_layout, null);
                    vodViewHolder = new VODViewHolder(convertView);
                    String column = "";
                    String columnname = "";
                    getOther = map.get("column");
                    if (getOther != null)
                        column = getOther.toString();
                    getOther = map.get("columnname");
                    if (getOther != null)
                        columnname = getOther.toString();
                    initVOD((List<Video>) object, vodViewHolder, column, columnname);
                    convertView.setTag(vodViewHolder);
                    break;
            }
        } else {
            switch (type) {
                case "BIGPICTURE":
                    bigPicViewHolder = (BigPicViewHolder) convertView.getTag();
                    initBigPic((List<Map<String, String>>) object, bigPicViewHolder);
                    break;
                case "LIVE":
                    liveViewHolder = (LiveViewHolder) convertView.getTag();
                    initLive((List<Map<String, String>>) object, liveViewHolder);
                    break;
                case "VOD":
                    vodViewHolder = (VODViewHolder) convertView.getTag();
                    String column = "";
                    String columnname = "";
                    getOther = map.get("column");
                    if (getOther != null)
                        column = getOther.toString();
                    getOther = map.get("columnname");
                    if (getOther != null)
                        columnname = getOther.toString();
                    initVOD((List<Video>) object, vodViewHolder, column, columnname);
                    break;
            }
        }
        return convertView;
    }

    private int setWidth(int width) {
        return this.width * width / 750;
    }

    private int setHeight(int height) {
        return this.height * height / 1334;
    }

    private void initBigPic(List<Map<String, String>> data, BigPicViewHolder bigPicViewHolder) {
        final AutoScrollViewPager mAutoScrollViewPager = bigPicViewHolder.autoScrollViewPager;
        mRecHintView = bigPicViewHolder.mRecHintView;
        if (data != null && data.size() > 0) {
            mAutoScrollViewPager.setInterval(SPACE_TIME);
            mAutoScrollViewPager.setCycle(true);
            mAutoScrollViewPager.startAutoScroll();
            mAutoScrollViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
            mAutoScrollViewPager.addOnPageChangeListener(new PageChangeListener());
            mRealCount = data.size();
            if (mRealCount != mRecHintView.getChildCount()) {
                mRecHintView.removeAllViews();
                for (int i = 0; i < mRealCount; i++) {
                    ImageView image = new ImageView(context);
                    mRecHintView.addView(image);
                    image.setClickable(false);
                    image.setTag(image);
                }
            }
            RecommendPagerAdapter recommendPagerAdapter;
            if (mAutoScrollViewPager.getAdapter() == null) {
                index = 0;
                recommendPagerAdapter = new RecommendPagerAdapter(context);
                recommendPagerAdapter.setData(data);
                mAutoScrollViewPager.setAdapter(recommendPagerAdapter);
            } else {
                recommendPagerAdapter = (RecommendPagerAdapter) mAutoScrollViewPager.getAdapter();
                recommendPagerAdapter.setData(data);
            }
            changeHint();
        }
        ViewGroup.LayoutParams layoutParams = mAutoScrollViewPager.getLayoutParams();
        layoutParams.height = setHeight(420);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mAutoScrollViewPager.setLayoutParams(layoutParams);
    }

    /**
     * 更換提示图片
     */
    private void changeHint() {
        if (mRecHintView == null) {
            return;
        }
        LinearLayout.LayoutParams image_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mRealCount; i++) {
            ImageView iv = (ImageView) mRecHintView.getChildAt(i);
            image_lp.setMargins(setWidth(9), 0, setWidth(9), 0);
            if (i == index % mRealCount) {
                iv.setImageResource(R.drawable.home_spot_sel);
            } else {
                iv.setImageResource(R.drawable.home_spot);
            }
            iv.setLayoutParams(image_lp);
        }
    }

    private void initLive(List<Map<String, String>> liveData, final LiveViewHolder liveViewHolder) {
        if (liveViewHolder.mLLCategoryArea.getChildCount() != 0)
            liveViewHolder.mLLCategoryArea.removeAllViews();
        liveViewHolder.mHSCategoryArea.post(new Runnable() {

            @Override
            public void run() {
                liveViewHolder.mLLCategoryArea.measure(0, 0);
                liveViewHolder.mHSCategoryArea.smoothScrollTo(0, 0);
            }
        });
        ImageOptions imageOption = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_CENTER).setIgnoreGif(false).build();
        liveViewHolder.tv_title.setText(context.getResources().getString(R.string.recommend_live));
        int livedataSize = liveData.size();
        for (int i = 0; i < livedataSize; i++) {
            Map<String, String> map = liveData.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.item_recommend_live, null);
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.item_recommend_live);
            ImageView iv = (ImageView) view.findViewById(R.id.imageView);
            ViewGroup.LayoutParams iv_lp = iv.getLayoutParams();
            iv_lp.height = setHeight(116);
            iv_lp.width = setWidth(116);
            iv.setLayoutParams(iv_lp);
            HeiTiTextView tv = (HeiTiTextView) view.findViewById(R.id.textView);
            ViewGroup.LayoutParams tv_lp = tv.getLayoutParams();
            tv_lp.width = setWidth(116);
            tv_lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tv.setLayoutParams(tv_lp);
            x.image().bind(iv, map.get("imgUrl"), imageOption);
            tv.setText(map.get("channelname"));
            ll.setOnClickListener(new onClickListeners("ll", map.get("channelcode")));
            LinearLayout.LayoutParams ll_lp = new LinearLayout.LayoutParams(setWidth(116), LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i == 0)
                ll_lp.setMargins(setWidth(30), 0, setWidth(28), 0);
            else if (i == liveData.size() - 1)
                ll_lp.setMargins(0, 0, setWidth(30), 0);
            else
                ll_lp.setMargins(0, 0, setWidth(28), 0);
            ll.setLayoutParams(ll_lp);
            liveViewHolder.mLLCategoryArea.addView(ll);
        }
    }

    private void initVOD(List<Video> videos, VODViewHolder vodViewHolder, String column, String columnname) {
        RecommendGridViewAdapter recommendGridViewAdapter;
        if (vodViewHolder.recommend_VOD.getAdapter() == null) {
            String title = String.format(context.getResources().getString(R.string.recommend_vod), columnname);
            vodViewHolder.tv_title.setText(title);
            recommendGridViewAdapter = new RecommendGridViewAdapter(context);
            recommendGridViewAdapter.setData(videos);
            vodViewHolder.recommend_VOD.setAdapter(recommendGridViewAdapter);
        } else {
            recommendGridViewAdapter = (RecommendGridViewAdapter) vodViewHolder.recommend_VOD.getAdapter();
            recommendGridViewAdapter.setData(videos);
        }
        vodViewHolder.more.setOnClickListener(new onClickListeners("VOD", column, columnname));
    }

    class onClickListeners implements View.OnClickListener {
        private String id;
        private String param;
        private String columname;

        onClickListeners(String id) {
            this.id = id;
        }

        onClickListeners(String id, String param) {
            this.id = id;
            this.param = param;
        }

        onClickListeners(String id, String param, String columname) {
            this.id = id;
            this.param = param;
            this.columname = columname;
        }

        @Override
        public void onClick(View v) {
            switch (id) {
                case "ll":
                    Intent intent = new Intent(context, PlayLive.class);
                    intent.putExtra("cid", param);
                    intent.putExtra("id", "0");
                    context.startActivity(intent);
                    break;
                case "LIVE":
                    break;
                case "VOD":
                    break;
            }
        }
    }

    class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        @Override
        public void onPageSelected(int position) {
            index = position;
            changeHint();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class BigPicViewHolder {
        LinearLayout mRecHintView;
        AutoScrollViewPager autoScrollViewPager;

        BigPicViewHolder(View view) {
            autoScrollViewPager = (AutoScrollViewPager) view.findViewById(R.id.recommendViewPager);
            mRecHintView = (LinearLayout) view.findViewById(R.id.layout_hints);
        }
    }

    class VODViewHolder {
        NoScrollGridView recommend_VOD;
        LinearLayout more;
        HeiTiTextView tv_title;

        VODViewHolder(View view) {
            recommend_VOD = (NoScrollGridView) view.findViewById(R.id.recommend_VOD);
            more = (LinearLayout) view.findViewById(R.id.more);
            tv_title = (HeiTiTextView) view.findViewById(R.id.tv_title);
        }
    }

    class LiveViewHolder {
        LinearLayout linearLayout;
        MyHorizontalScrollView mHSCategoryArea; // 分类区域
        LinearLayout mLLCategoryArea; // 分类区域
        HeiTiTextView tv_title;

        LiveViewHolder(View view) {
            linearLayout = (LinearLayout) view.findViewById(R.id.item_recommend_live_layout);
            mLLCategoryArea = (LinearLayout) view.findViewById(R.id.live_cat_content);
            mHSCategoryArea = (MyHorizontalScrollView) view.findViewById(R.id.live_cat_hori);
            tv_title = (HeiTiTextView) view.findViewById(R.id.tv_title);
        }
    }
}
