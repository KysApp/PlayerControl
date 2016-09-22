package com.kys.player.example.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kys.player.example.R;
import com.kys.player.example.entity.Video;
import com.kys.player.example.ui.PlayVideo;

import java.util.ArrayList;

public class VideoSeriesFullAdapter extends BaseAdapter {
	private final String TAG = VideoSeriesFullAdapter.class.getSimpleName();
	Context context;
	ArrayList<Video> data;
	int curIndex = 1;

	public VideoSeriesFullAdapter(Context context, ArrayList<Video> data) {
		this.context = context;
		this.data = data;
	}

	public void checkedOnly(int index) {
		curIndex = data.get(index).getIndex();
		notifyDataSetChanged();
	}

	public int getSelectedId()
	{
		return curIndex;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//curIndex=data.size();
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mViewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_tv_series, null);
			mViewHolder = new ViewHolder();
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		mViewHolder.txt_tv_series = (TextView) convertView
				.findViewById(R.id.txt_tv_series);
		Video video = data.get(position);
		mViewHolder.txt_tv_series.setText(video.getIndex() + "");
		mViewHolder.txt_tv_series.setGravity(Gravity.CENTER);
		mViewHolder.txt_tv_series.setTag(video);
		if (video.getIndex() == curIndex) {
			mViewHolder.txt_tv_series.setBackgroundResource(R.drawable.tv_series1);
			mViewHolder.txt_tv_series.setTextColor(context.getResources().getColor(R.color.blue_5fb6e8));
		} else {
			mViewHolder.txt_tv_series.setBackgroundResource(R.drawable.tv_series3);
			mViewHolder.txt_tv_series.setTextColor(Color.WHITE);
		}
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((PlayVideo)context).changeTv(position);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView txt_tv_series;
	}

}
