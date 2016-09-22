package com.kys.player.example.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kys.player.example.R;

import java.util.List;

public class SelectDefiAdapter extends BaseAdapter {

	private final String TAG = SelectDefiAdapter.class.getSimpleName();
	private Context context;
	private List<String> data;
	private int selectedIndex = -1;
	public SelectDefiAdapter(Context context, List<String> data)
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
		String string = data.get(position);
		text.setText(string);
		text.setGravity(Gravity.CENTER);
		if(position == selectedIndex)
		{
			text.setTextColor(context.getResources().getColor(R.color.green_26ac3d));
		}
		else
		{
			text.setTextColor(context.getResources().getColor(R.color.white));
		}

		return convertView;
	}

}
