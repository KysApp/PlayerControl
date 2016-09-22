package com.kys.player.example.adapter;
/**
 * 作者：幻云紫日 on 2016/4/28 14:02
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kys.player.example.R;
import com.zxt.dlna.dmp.DeviceItem;

import java.util.List;

//DLNA设备
public class DevAdapter extends ArrayAdapter<DeviceItem> {

    private static final String TAG = "DeviceAdapter";

    private LayoutInflater mInflater;

    public int dmrPosition = 0;

    private List<DeviceItem> deviceItems;

    public DevAdapter(Context context, int textViewResourceId,
                      List<DeviceItem> objects) {
        super(context, textViewResourceId, objects);
        this.mInflater = ((LayoutInflater) context
                .getSystemService("layout_inflater"));
        this.deviceItems = objects;
    }

    public int getCount() {
        return this.deviceItems.size();
    }

    public DeviceItem getItem(int paramInt) {
        return this.deviceItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {

        DevHolder holder;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.dmr_item, null);
            holder = new DevHolder();
            holder.filename = ((TextView) view
                    .findViewById(R.id.dmr_name_tv));
            view.setTag(holder);
        } else {
            holder = (DevHolder) view.getTag();
        }

        DeviceItem item = (DeviceItem) this.deviceItems.get(position);
        holder.filename.setText(item.toString());
        return view;
    }

    public final class DevHolder {
        public TextView filename;
    }

}
