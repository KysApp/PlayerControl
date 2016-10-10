package com.kys.playercontrol.customview;
/**
 * Created by 幻云紫日 on 2016/9/26.
 */

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kys.playercontrol.R;
import com.kys.playercontrol.interfaces.OnPlayerControlListener;
import com.kys.playercontrol.tools.CommonApi;
import com.kys.playercontrol.widget.Rescourse;

/**
 * 作者：幻云紫日 on 2016/9/26 15:15
 */
public class PopupWindows {

    //全屏选集弹窗
    public static void intPopupTvInfo(Activity mContext, PopupWindow popupTvInfo, View view, OnPlayerControlListener listener) {
        // TODO Auto-generated method stub
        View popupView = LayoutInflater.from(mContext).inflate(
                R.layout.popup_play_tv, null);
        TextView txt_select = (TextView) popupView.findViewById(R.id.txt_select);
        popupView.findViewById(R.id.popupwindow_bg).setBackgroundResource(Rescourse.getPopupwindow_bg());
        txt_select.setText(mContext.getResources().getString(R.string.select_series));
        int sWidth = CommonApi.getScreenWidth(mContext);
        int sHeight = CommonApi.getScreenHeight(mContext);
        popupTvInfo.setWidth(sWidth / 4);
        popupTvInfo.setHeight(sHeight / 3 * 2);
        popupTvInfo.setContentView(popupView);
        popupTvInfo.setFocusable(true);
        GridView mGridView = (GridView) popupView.findViewById(R.id.mGridView);
        popupTvInfo.setBackgroundDrawable(new BitmapDrawable());
        popupTvInfo.showAsDropDown(view);
        if(listener != null)
            listener.setSeries(mGridView);
    }

    //清晰度切换
    public static void intDefinition(Activity mContext, PopupWindow popuDefinition, View view, OnPlayerControlListener listener) {
        View popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_play_definition, null);
        popupView.findViewById(R.id.popupwindow_bg).setBackgroundResource(Rescourse.getPopupwindow_bg());
        ListView mListView = (ListView) popupView.findViewById(R.id.list);
        int popuWidth = mContext.getResources().getDimensionPixelOffset(R.dimen.width_61);
        int popuHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.width_30) * 3;
        popuDefinition.setWidth(popuWidth);
        popuDefinition.setHeight(popuHeight);
        popuDefinition.setContentView(popupView);
        popuDefinition.setFocusable(true);
        popuDefinition.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popuDefinition.showAtLocation(view, Gravity.NO_GRAVITY,
                location[0] - mContext.getResources().getDimensionPixelOffset(R.dimen.width_8),
                location[1] - popuDefinition.getHeight());
        if(listener != null)
            listener.setDefinition(mListView);
    }

    private static TextView txt_refresh;
    private static ProgressBar share_progressBar;

    //DLNA设备展现
    public static void intPopupShareTv(Activity mContext, PopupWindow popupShareTv, final OnPlayerControlListener listener) {
        // TODO Auto-generated method stub
        View popupView = LayoutInflater.from(mContext).inflate(
                R.layout.popup_share_tv, null);
        popupView.findViewById(R.id.popupwindow_bg).setBackgroundResource(Rescourse.getPopupwindow_bg());
        TextView txt_select = (TextView) popupView.findViewById(R.id.txt_select);
        txt_select.setText(mContext.getResources().getString(R.string.select_dlna));
        txt_refresh = (TextView) popupView.findViewById(R.id.txt_refresh);
        LinearLayout layout_refresh = (LinearLayout) popupView.findViewById(R.id.layout_refresh);
        share_progressBar = (ProgressBar) popupView.findViewById(R.id.share_progressBar);
        layout_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDlnaRefresh(true);
                if(listener != null)
                    listener.onRefreshDlna();
            }
        });
        //重新搜索设备
        popupView.setFocusableInTouchMode(true);
        popupShareTv.setWidth((int) mContext.getResources().getDimension(R.dimen.play_share_tv_width));
        popupShareTv.setHeight((int) mContext.getResources().getDimension(R.dimen.play_share_tv_height));
        popupShareTv.setContentView(popupView);
        popupShareTv.setFocusable(true);
        ListView mDmrLv = (ListView) popupView.findViewById(R.id.mListView);
        popupShareTv.setBackgroundDrawable(new BitmapDrawable());
        popupShareTv.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        if(listener != null)
            listener.setDlna(mDmrLv);
    }

    public static void setDlnaRefresh(boolean show) {

        if(share_progressBar != null && txt_refresh!=null)
        if(show){
            if (share_progressBar != null)
                share_progressBar.setVisibility(View.VISIBLE);
            txt_refresh.setText("点击重新刷新");
        }else{
            if (share_progressBar != null)
                share_progressBar.setVisibility(View.GONE);
        }

    }

    //频道弹窗
    public static void intPopupChannelInfo(Activity mContext, PopupWindow popupChannelInfo, View view, OnPlayerControlListener listener) {
        // TODO Auto-generated method stub
        View popupView = LayoutInflater.from(mContext).inflate(
                R.layout.popup_play_model, null);
        popupView.findViewById(R.id.popupwindow_bg).setBackgroundResource(Rescourse.getPopupwindow_bg());
        int screenWidth = CommonApi.getScreenWidth(mContext);
        int screenHeight = CommonApi.getScreenHeight(mContext);
        popupChannelInfo.setWidth(screenWidth / 3);
        popupChannelInfo.setHeight(screenHeight / 3 * 2);
        popupChannelInfo.setContentView(popupView);
        popupChannelInfo.setFocusable(true);
        popupChannelInfo.setBackgroundDrawable(new BitmapDrawable());

        ListView list_channel = (ListView) popupView.findViewById(R.id.list);
        popupChannelInfo.showAsDropDown(view);
        if(listener != null)
            listener.setChannelList(list_channel);
    }

}
