package com.kys.player.example.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kys.player.example.R;
import com.kys.player.example.entity.Lives;
import com.kys.player.example.ui.PlayLive;

import java.util.List;

public class SelectChannelAdapter extends BaseAdapter {

	private final String TAG = SelectChannelAdapter.class.getSimpleName();
	private Context context;
	private List<Lives> data;
	private int selectedIndex = -1;
	public SelectChannelAdapter(Context context, List<Lives> data)
	{
		this.context = context;
		this.data = data;
	}
	
	public void changeSelectedPosition(int position)
	{
		selectedIndex = position;
		notifyDataSetChanged();
	}
	
	public int getSelectedMode()
	{
		return selectedIndex;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_textview, null);
		} 
		TextView text = (TextView) convertView.findViewById(R.id.text);
		Lives map = data.get(position);
		text.setText(map.getTitle() + "");
		text.setGravity(Gravity.CENTER);
		if(position == selectedIndex)
		{
			text.setTextColor(context.getResources().getColor(R.color.green_26ac3d));
		}
		else
		{
			text.setTextColor(context.getResources().getColor(R.color.white));
		}
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((PlayLive)context).changeChannel(position);
			}
		});
		
		return convertView;
	}

}
