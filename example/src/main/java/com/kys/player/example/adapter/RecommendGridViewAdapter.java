package com.kys.player.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kys.player.example.R;
import com.kys.player.example.base.CommonApi;
import com.kys.player.example.entity.Video;
import com.kys.player.example.selfview.HeiTiTextView;
import com.kys.player.example.selfview.SongTiTextView;
import com.kys.player.example.ui.PlayVideo;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhl on 2016/8/2.
 */
public class RecommendGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Video> data = new ArrayList<>();
    private int width = 0;
    private int height = 0;

    public RecommendGridViewAdapter(Context context) {
        this.context = context;
        width = CommonApi.getScreenWidth(context);
        height = CommonApi.getScreenHeight(context);
    }
    public void setData(List<Video> data){
        this.data = data;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
//        if (data.size() <= 6)
        return data.size();
//        else
//            return 6;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Video video = data.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend_vod, null);
            viewHolder = new ViewHolder(convertView);
            initView(viewHolder, video);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            initView(viewHolder, video);
        }
        return convertView;
    }

    private int setWidth(int width) {
        return this.width * width / 750;
    }

    private int setHeight(int height) {
        return this.height * height / 1334;
    }

    private void initView(ViewHolder viewHolder, final Video video) {
        viewHolder.textView.setText(video.getTitle());
        x.image().bind(viewHolder.imageView, video.getImg_url());
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayVideo.class);
                intent.putExtra("video", video);
                context.startActivity(intent);
            }
        });
        String getSeriesCode = video.getSeriesprogramcode();
        int seriesnum = video.getIndex();
        String series;
        if (getSeriesCode != null && !getSeriesCode.equals("")) {
            if (seriesnum != 0)
                series = String.format(context.getResources().getString(R.string.recommend_series), video.getIndex());
            else
                series = context.getResources().getString(R.string.recommend_series_nodata);
            viewHolder.series.setText(series);
            viewHolder.series.setVisibility(View.VISIBLE);
        } else viewHolder.series.setVisibility(View.GONE);

        String price = video.getPrice();
        if (price != null && !price.equals("0") && !price.equals(""))
            viewHolder.iv_pay.setVisibility(View.VISIBLE);
        else
            viewHolder.iv_pay.setVisibility(View.GONE);
    }

    class ViewHolder {
        ImageView imageView;
        HeiTiTextView textView;
        LinearLayout linearLayout;
        SongTiTextView series;
        ImageView iv_pay;

        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.imgView);
            ViewGroup.LayoutParams imageView_lp = imageView.getLayoutParams();
            imageView_lp.height = setHeight(302);
            imageView_lp.width = setWidth(224);
            imageView.setLayoutParams(imageView_lp);
            textView = (HeiTiTextView) view.findViewById(R.id.textView);
            linearLayout = (LinearLayout) view.findViewById(R.id.item_recommend_vod);
            series = (SongTiTextView) view.findViewById(R.id.tv_series);
            ViewGroup.LayoutParams series_lp = series.getLayoutParams();
            series_lp.height = setHeight(28);
            series_lp.width = setWidth(120);
            series.setLayoutParams(series_lp);
            iv_pay = (ImageView) view.findViewById(R.id.iv_pay);
            ViewGroup.LayoutParams pay_lp = iv_pay.getLayoutParams();
            pay_lp.height = setHeight(34);
            pay_lp.width = setWidth(74);
            iv_pay.setLayoutParams(pay_lp);
        }
    }
}
