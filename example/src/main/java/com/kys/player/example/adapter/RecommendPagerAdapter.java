package com.kys.player.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.kys.player.example.base.CommonApi;
import com.kys.player.example.entity.Video;
import com.kys.player.example.ui.PlayLive;
import com.kys.player.example.ui.PlayVideo;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecommendPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<Map<String, String>> allData = new ArrayList<>();
    private ImageView iv = null;
    private int count = 0;
    private int width=0;
    private int height=0;
    public RecommendPagerAdapter(Context context) {
        mContext = context;
        width = CommonApi.getScreenWidth(context);
        height = CommonApi.getScreenHeight(context);
    }
    public void setData(List<Map<String, String>> data){
        this.allData = data;
        count = allData.size();
        notifyDataSetChanged();
    }
    private ImageView imageHandle(int position) {
        iv = new ImageView(mContext);
//			iv.setBackgroundResource(R.drawable.player_bg);
        iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height*420/1334));
        iv.setScaleType(ScaleType.FIT_XY);
        iv.setOnClickListener(new ClickListener(allData.get(position)));
        return iv;
    }

    class ClickListener implements OnClickListener {
        private Map<String,String> map;
        public ClickListener(Map<String,String> map){
            this.map=map;
        }
        @Override
        public void onClick(View v) {
            String type = map.get("type");
            Intent intent = new Intent();
            switch (type) {
                case "tv":
                    intent.setClass(mContext, PlayLive.class);
                    intent.putExtra("cid", map.get("source"));
                    intent.putExtra("id", "0");
                    mContext.startActivity(intent);
                    break;
                case "programcode":
                    Video video = new Video();
                    video.setProgramcode(map.get("source"));
                    video.setTitle(map.get("name"));
                    intent.setClass(mContext, PlayVideo.class);
                    intent.putExtra("video", video);
                    mContext.startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getCount() {
        return count*100;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String imgUrl = allData.get(position % count).get("imgUrl");
        ImageView iv = imageHandle(position % count);
        x.image().bind(iv, imgUrl, new ImageOptions.Builder().setImageScaleType(ScaleType.FIT_XY).build());
        container.addView(iv);
        return iv;
    }

}
