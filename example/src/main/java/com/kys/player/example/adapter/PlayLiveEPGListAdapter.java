package com.kys.player.example.adapter;

/**
 * Created by bsy on 2016/4/19.
 */

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.kys.player.example.R;
import com.kys.player.example.entity.EPG;
import com.kys.player.example.ui.PlayLive;

import java.util.List;

public class PlayLiveEPGListAdapter extends BaseAdapter {
    private final String TAG = PlayLiveEPGListAdapter.class.getSimpleName();

    private Context context;
    private List<EPG> epgList;
    private Handler mNetHandler;
    private int mark = 99;


    public PlayLiveEPGListAdapter(Context context, List<EPG> epgList, Handler mNetHandler) {
        this.context = context;
        this.epgList = epgList;
        this.mNetHandler = mNetHandler;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return epgList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        MyViewHolder myViewHolder;
        if (arg1 == null) {
            myViewHolder = new MyViewHolder();
            arg1 = LayoutInflater.from(context)
                    .inflate(R.layout.item_epg, null);
            arg1.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) arg1.getTag();
        }
        myViewHolder.time = (TextView) arg1.findViewById(R.id.time);
        myViewHolder.name = (TextView) arg1.findViewById(R.id.name);
        myViewHolder.button = (ImageView) arg1.findViewById(R.id.schedule);
        final EPG epg = epgList.get(arg0);
        if (arg0 != mark && mark != 99) {
            epg.setIsplaying(false);
        }
        String broadcastTime = epg.getStart_time();
        myViewHolder.time.setText(broadcastTime.substring(11));
        myViewHolder.name.setText(epg.getTitle() + "");

        arg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mark = arg0;
                PlayLive.start_time = epg.getStart_time();
                if (!epg.getIsplaying() && epg.getStatus().equals("1")) {//回看
                    ((PlayLive) context).changeCidHui(epg.getCid());
                    epg.setIsplaying(true);
                    notifyDataSetChanged();
                    PlayLive.timeLen = epg.getRun_time();
                    mNetHandler.sendEmptyMessage(3);
                } else if (!epg.getIsplaying() && epg.getStatus().equals("0")) {//直播
                    if (epg.getIs_now_epg()) {
                        epg.setIsplaying(true);
                        notifyDataSetChanged();
                        mNetHandler.sendEmptyMessage(2);
                    }
                }
            }
        });
        if (epg.getStatus().equals("1")) {//回看
            myViewHolder.button.setVisibility(View.VISIBLE);
            myViewHolder.button.setImageResource(R.drawable.btn_playlive_playback);
            myViewHolder.time.setTextColor(context.getResources().getColor(
                    R.color.gray_3a3a3a));
            myViewHolder.name.setTextColor(context.getResources().getColor(
                    R.color.gray_3a3a3a));
        } else if (epg.getStatus().equals("0")) {//其他
            if (epg.getIs_now_epg()) {//正在播出
                myViewHolder.button.setVisibility(View.VISIBLE);

                myViewHolder.button.setImageResource(R.drawable.btn_playlive_playback);
                myViewHolder.time.setTextColor(context.getResources().getColor(
                        R.color.gray_3a3a3a));
                myViewHolder.name.setTextColor(context.getResources().getColor(
                        R.color.gray_3a3a3a));
            } else {//未播出
                myViewHolder.time.setTextColor(context.getResources().getColor(
                        R.color.gray_666666));
                myViewHolder.name.setTextColor(context.getResources().getColor(
                        R.color.gray_666666));
                myViewHolder.button.setVisibility(View.INVISIBLE);
            }
        }

        if (epg.getIsplaying()) {
            myViewHolder.button.setImageResource(R.drawable.btn_playlive);
            myViewHolder.time.setTextColor(context.getResources().getColor(
                    R.color.blue_0085d1));
            myViewHolder.name.setTextColor(context.getResources().getColor(
                    R.color.blue_0085d1));
        }
        return arg1;
    }

    class MyViewHolder {
        TextView time;
        TextView name;
        ImageView button;
    }
}
