package com.kys.player.example.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kys.player.example.R;
import com.kys.player.example.base.MyApplication;
import com.kys.player.example.tools.LoginHelper;
import com.zte.iptvclient.android.androidsdk.SDKLoginMgr;
import com.zte.iptvclient.android.androidsdk.SDKMgr;
import com.zte.iptvclient.android.androidsdk.common.LogEx;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 幻云紫日 on 2016/4/13.
 */
public class Loading extends Activity {
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

    }

    private SDKLoginMgr mLoginMgr = null;
    private final String TAG = Loading.class.getSimpleName();
    private String hostName;
    private String hostAddress;
    private TextView time_lost;
    private Timer timer = new Timer();
    private int recLen = 4;
    private LoginHelper loginHelper;

    public static final int GET_IP_FAIL = 0;
    public static final int GET_IP_SUC = 1;
    private Handler mHandle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_IP_FAIL: {
                    break;
                }
                case GET_IP_SUC: {
                    if (null != msg.obj) {
                        InetAddress inetAddress = (InetAddress) msg.obj;
                        if (null != inetAddress) {
                            setIp(inetAddress);
                            setIpInfo();
                        }
                    } else {
                    }
                    break;
                }

            }

            super.handleMessage(msg);
        }

    };

    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() { // UI thread
                @Override
                public void run() {
                    recLen--;
                    time_lost.setText(recLen + "");
                    if (recLen <= 0) {
                        timer.cancel();
                        time_lost.setVisibility(View.GONE);
                        Intent intent = new Intent();
                        intent.setClass(Loading.this, Main.class);
                        startActivity(intent);
                        Loading.this.finish();
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_loading);
        SDKMgr.initSDK(this);
        SDKMgr.setLogLevel(LogEx.LogLevelType.TYPE_LOG_LEVEL_DEBUG);
        time_lost = (TextView) findViewById(R.id.time_lost);
        time_lost.setVisibility(View.GONE);
        findViewById(R.id.timer_ll).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            timer.cancel();
                            Intent intent = new Intent(Loading.this, Main.class);
                            startActivity(intent);
                            Loading.this.finish();
                        }
                    });

        getIp();

        goTimerTask();
    }

    //开启广告倒计时
    private void goTimerTask() {
        time_lost.setVisibility(View.VISIBLE);
        findViewById(R.id.layout_loading).setBackgroundResource(R.drawable.loading_bg_adv);
        timer.schedule(task, 0, 1000); // timeTask
    }

    private void getIp() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();

                InetAddress inetAddress;
                Message message = new Message();
                try {
                    inetAddress = InetAddress.getByName(String.format("%d.%d.%d.%d",
                            (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
                            (ipAddress >> 24 & 0xff)));

                    hostName = inetAddress.getHostName();
                    hostAddress = inetAddress.getHostAddress();
                    message.obj = inetAddress;
                    message.what = GET_IP_SUC;
                    mHandle.sendMessage(message);
                } catch (UnknownHostException e) {
                    mHandle.sendEmptyMessage(GET_IP_FAIL);
                }
            }
        }).start();

    }

    private void setIp(InetAddress inetAddress) {
        MyApplication.setLocalIpAddress(inetAddress);
    }

    private void setIpInfo() {
        MyApplication.setHostName(hostName);
        MyApplication.setHostAddress(hostAddress);
    }


    String encryToken;

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
