package com.kys.player.example.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kys.player.example.R;
import com.kys.player.example.adapter.PlayLiveEPGListAdapter;
import com.kys.player.example.adapter.SelectChannelAdapter;
import com.kys.player.example.adapter.SelectDefiAdapter;
import com.kys.player.example.base.CommonApi;
import com.kys.player.example.base.MyApplication;
import com.kys.player.example.base.OperateSharePreferences;
import com.kys.player.example.entity.EPG;
import com.kys.player.example.entity.Lives;
import com.kys.player.example.tools.HttpJsonObjectHelper;
import com.kys.player.example.tools.Json2EntityHelper;
import com.kys.playercontrol.widget.PlayControl;
import com.kys.playercontrol.interfaces.OnPlayerControlListener;

import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.sample.widget.media.IjkVideoView;

public class PlayLive extends Activity implements OnClickListener,
        RadioGroup.OnCheckedChangeListener {
    @Override
    public void onBackPressed() {
        mPlayControl.onBackPressed();
    }

    private final static String TAG = PlayLive.class.getSimpleName();

    private RadioGroup rdoGrp_epgList;
    private ListView lVi_epgList;
    private List<Map<String, Object>> listLookback;
    private PlayLiveEPGListAdapter mEPGListAdapter;
    private int PlayCurrentTime = -1;
    private TextView title;

    private String mPath = "";

    public boolean isLive = true;
    private RadioButton mEpg, back1, back2, back3;
    Bundle bundle = null;
    public static String timeLen = "";

    private List<EPG> epgList;
    private List<Lives> channelList = new ArrayList<Lives>();
    private String cid, today_date, id;
    public String cid_hui;
    public static String start_time = "0";
    private String definition = "1";
    int time = 0;
    private IjkVideoView mSurface;
    private View mOverlayContent;
    private FrameLayout frame_control;
    private RelativeLayout mOverlayPlayer;
    private PlayControl mPlayControl;
    private SelectDefiAdapter mSelectDefiAdapter;
    private SelectChannelAdapter mSelectChannelAdapter;
    List<String> Definlist = new ArrayList<String>();

    private Handler mNetHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 2: // 请求直播播放地址
                    isLive = true;
                    //中兴播放
                    //鉴权
                    doLiveAuth(cid, id, "2");
                    break;

                case 3: // 请求回看播放地址
                    isLive = false;
                    //中兴播放
                    doTVODAuth(cid, cid_hui, "4");
                    //华为播放
                    break;
                case 4:
                    getAllChannel();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_play_live);
        cid = getIntent().getStringExtra("cid");
        id = getIntent().getStringExtra("id");
        if (id.equals("0")) {
            id = "0400";
        }
        epgList = new ArrayList<>();
        logics();
        initPlayer();
        today_date = CommonApi.getSystemDate();
        String start_t = today_date + " 00:00:59";
        String end_t = today_date + " 23:59:59";
        getEPG(cid, start_t, end_t, start_time);
        mNetHandler.sendEmptyMessage(2);
        mEpg = (RadioButton) findViewById(R.id.epg);
        back1 = (RadioButton) findViewById(R.id.back1);
        back2 = (RadioButton) findViewById(R.id.back2);
        back3 = (RadioButton) findViewById(R.id.back3);
        mEpg.setText(mEpg.getText().toString());
        Definlist.add(getString(R.string.hd));
        Definlist.add(getString(R.string.sd_h));
        Definlist.add(getString(R.string.sd));
        mSelectDefiAdapter = new SelectDefiAdapter(PlayLive.this, Definlist);
        mSelectChannelAdapter = new SelectChannelAdapter(this, channelList);
    }

    /**
     * 直播鉴权
     *
     * @param programcode
     * @param columncode
     * @param contenttype
     */
    public void doLiveAuth(final String programcode, final String columncode, String contenttype) {
        Map<String, String> map = new HashMap<>();
        map.put("programcode", programcode);
        map.put("columncode", columncode);
        map.put("contenttype", contenttype);
        map.put("mediaservices", "2");
        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "doauth.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                String authorizationid = response.optJSONArray("authorizationid").optString(0);
                getLiveUrl(programcode, columncode, authorizationid);
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onRefreshData() {
                mNetHandler.sendEmptyMessage(2);
            }

            @Override
            public void goLogin() {
                startActivity(new Intent().setClass(PlayLive.this, LoginZX.class));
            }
        });
    }

    /**
     * 回看鉴权
     *
     * @param programcode
     * @param columncode
     * @param contenttype
     */
    public void doTVODAuth(final String programcode, final String columncode, String contenttype) {
        Map<String, String> map = new HashMap<>();
        map.put("programcode", programcode);
        map.put("columncode", columncode);
        map.put("contenttype", contenttype);
        map.put("mediaservices", "2");
        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "doauth.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                String authorizationid = response.optJSONArray("authorizationid").optString(0);
                GetTVODUrl(programcode, columncode, authorizationid);
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onRefreshData() {
                mNetHandler.sendEmptyMessage(3);
            }

            @Override
            public void goLogin() {
                startActivity(new Intent().setClass(PlayLive.this, LoginZX.class));
            }
        });
    }

    private void GetTVODUrl(final String channelcode, final String prevuecode, String authorizationid) {
        Map<String, String> map = new HashMap<>();
        map.put("channelcode", channelcode);
        map.put("prevuecode", prevuecode);
        map.put("authidsession", authorizationid);
        map.put("definition", definition);
        map.put("mediaservices", "2");
        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "gettvodplayurl.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if(response.optString("returncode", "").equals("0")){
                    mPath = response.optString("url", "");
                    mSurface.setVideoPath(mPath);
                    mSurface.start();
                    mPlayerControlListener.onPlayPause();
                    mPlayControl.hideOverlay(true);
                } else {
                    Toast.makeText(PlayLive.this, response.optString("errormsg" ,""), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onRefreshData() {
                mNetHandler.sendEmptyMessage(3);
            }

            @Override
            public void goLogin() {
                startActivity(new Intent().setClass(PlayLive.this, LoginZX.class));
            }
        });
    }

    /**
     * 获取直播播放地址
     *
     * @param programcode
     * @param columncode
     * @param authorizationid
     */
    private void getLiveUrl(final String programcode, final String columncode, String authorizationid) {
        Map<String, String> map = new HashMap<>();
        map.put("channelcode", programcode);
        map.put("columncode", columncode);
        map.put("authidsession", authorizationid);
        map.put("definition", definition);
        map.put("mediaservices", "2");
        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "getchannelurl.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if(response.optString("returncode", "").equals("0")){
                    mPath = response.optString("url");
                    mNetHandler.sendEmptyMessage(4);
                    mSurface.setVideoPath(mPath);
                    mSurface.start();
                    mPlayerControlListener.onPlayPause();
                    mPlayControl.hideOverlay(true);
                } else {
                    Toast.makeText(PlayLive.this, response.optString("errormsg" ,""), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onRefreshData() {
                mNetHandler.sendEmptyMessage(2);
            }

            @Override
            public void goLogin() {
                startActivity(new Intent().setClass(PlayLive.this, LoginZX.class));
            }
        });
    }

    /**
     * 设置当前要播放的回看cid
     *
     * @param cid
     */
    public void changeCidHui(String cid) {
        time = 0;
        cid_hui = cid;
    }

    /**
     * 获取所有频道列表
     */
    public void getAllChannel() {
        if(channelList.size() > 0)return;
        Map<String, String> map = new HashMap<>();
        map.put("pageno", "1");
        map.put("numperpage", "100");
        map.put("columncode", "0400");
        map.put("mediaservices", "2");
        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "getchannel.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                channelList = Json2EntityHelper.streamArray2Lives(response,channelList);
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onRefreshData() {
                getAllChannel();
            }

            @Override
            public void goLogin() {

            }
        });
    }

    /**
     * 获取该频道的epg信息
     *
     * @param cid
     * @param start_date
     * @param end_date
     */
    private void getEPG(String cid, String start_date, String end_date, final String start_time) {
//        ProgressUtils.showProgressDialog(PlayLive.this, true);
//        Map<String, String> map = new HashMap<>();
//        map.put("pageno", "1");
//        map.put("numperpage", "100");
//        map.put("channelcode", cid);
//        map.put("starttime", start_date);
//        map.put("endtime", end_date);
//        map.put("utcstarttime", start_date);
//        map.put("utsendtime", end_date);
//        map.put("mediaservices", "1");
//        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "getinfobar.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                ProgressUtils.hideProgressDialog();
//                epgList.clear();
//                epgList = Json2EntityHelper.streamArray2Epg(response, start_time);
//                setEPGData();
//            }
//
//            @Override
//            public void onErrors(Throwable ex, boolean isOnCallback) {
//                ProgressUtils.hideProgressDialog();
//            }
//
//            @Override
//            public void onRefreshData() {
//                ProgressUtils.hideProgressDialog();
//            }
//
//            @Override
//            public void goLogin() {
//                ProgressUtils.hideProgressDialog();
//            }
//        });
    }

    private void getCurrentTimeFromNet() {
        //获取本地时间
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss", Locale.getDefault());//HH:24小时制，hh：12小时制
        PlayCurrentTime = Integer.parseInt(sdf.format(new Date()));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initPlayer() {
        // TODO Auto-generated method stub
        mOverlayContent = findViewById(R.id.overlay_content);
        mOverlayPlayer = (RelativeLayout) findViewById(R.id.overlay_player);
        int screenWidth = CommonApi.getScreenWidth(PlayLive.this);
        mOverlayPlayer.setLayoutParams(new LinearLayout.LayoutParams(
                screenWidth, screenWidth * 9 / 16));
        mSurface = (IjkVideoView) findViewById(R.id.player_surface);
        frame_control = (FrameLayout) findViewById(R.id.frame_control);
        mPlayControl = new PlayControl(this, mOverlayPlayer, mOverlayContent, true, 1);
        mPlayControl.setOnPlayerControlListener(mPlayerControlListener);
        frame_control.addView((View) mPlayControl);
        mSurface.setOnPreparedListener(mOnPreparedListener);
        mSurface.setOnCompletionListener(mOnCompletionListener);
        mSurface.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {

            }
        });
        mSurface.setOnErrorListener(new IMediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                // TODO Auto-generated method stub
                Toast.makeText(PlayLive.this, "无法播放视频", Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });
    }


    private void logics() {
        // TODO Auto-generated method stub
        findView();
        getCurrentTimeFromNet();

    }

    private void findView() {
        // TODO Auto-generated method stub
        mOverlayContent = findViewById(R.id.overlay_content);

        rdoGrp_epgList = (RadioGroup) findViewById(R.id.epg_group);
        rdoGrp_epgList.setOnCheckedChangeListener(this);
        lVi_epgList = (ListView) findViewById(R.id.epg_list);
        setRadioButtonsText();
        listLookback = new ArrayList<Map<String, Object>>();
    }

    private void setRadioButtonsText() {
        // TODO Auto-generated method stub
        RadioButton epg = (RadioButton) findViewById(R.id.epg);
        Drawable drawable = getResources().getDrawable(
                R.drawable.choose);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        epg.setCompoundDrawables(null, null, null, drawable);
        setDate(epg, 0);
        RadioButton radio1 = (RadioButton) findViewById(R.id.back1);
        setDate(radio1, 1);
        RadioButton radio2 = (RadioButton) findViewById(R.id.back2);
        setDate(radio2, 2);
        RadioButton radio3 = (RadioButton) findViewById(R.id.back3);
        setDate(radio3, 3);
    }

    /**
     * 填充epg数据
     */
    private void setEPGData() {
        mEPGListAdapter = new PlayLiveEPGListAdapter(this, epgList, mNetHandler);
        lVi_epgList.setAdapter(mEPGListAdapter);
    }

    /**
     * @param
     * @param daynumber 往前多少天
     */
    private void setDate(RadioButton radio, int daynumber) {
        // TODO Auto-generated method stub
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() - daynumber * 24
                * 60 * 60 * 1000);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String stringMonth = month + "";
        String stringDay = day + "";
        if (stringMonth.length() == 1) {
            stringMonth = "0" + stringMonth;
        }
        if (stringDay.length() == 1) {
            stringDay = "0" + stringDay;
        }
        radio.setText(stringMonth + "/" + stringDay);
    }

    /***************
     * begin CheckedChangeListener
     *****************/
    @Override
    public void onCheckedChanged(RadioGroup arg0, int arg1) {
        // TODO Auto-generated method stub
        Drawable drawable = getResources().getDrawable(
                R.drawable.choose);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        switch (arg1) {
            case R.id.epg:
                String start_t = today_date + " 00:00:59";
                String end_t = today_date + " 23:59:59";
                getEPG(cid, start_t, end_t, start_time);
                ((RadioButton) arg0.findViewById(R.id.epg))
                        .setCompoundDrawables(null, null, null, drawable);
                ((RadioButton) arg0.findViewById(R.id.back1))
                        .setCompoundDrawables(null, null, null, null);
                ((RadioButton) arg0.findViewById(R.id.back2))
                        .setCompoundDrawables(null, null, null, null);
                ((RadioButton) arg0.findViewById(R.id.back3))
                        .setCompoundDrawables(null, null, null, null);
                ((RadioButton) arg0.findViewById(R.id.epg))
                        .setTextColor(getResources().getColor(R.color.blue_0085d1));
                ((RadioButton) arg0.findViewById(R.id.back1))
                        .setTextColor(getResources().getColor(R.color.gray_666666));
                ((RadioButton) arg0.findViewById(R.id.back2))
                        .setTextColor(getResources().getColor(R.color.gray_666666));
                ((RadioButton) arg0.findViewById(R.id.back3))
                        .setTextColor(getResources().getColor(R.color.gray_666666));
                break;
            case R.id.back3:
                String day1 = (String) ((RadioButton) arg0.findViewById(R.id.back3)).getText();
                String today1 = today_date.substring(0, 5) + day1.replace("/", ".");
                String start_t1 = today1 + " 00:00:59";
                String end_t1 = today1 + " 23:59:59";
                getEPG(cid, start_t1, end_t1, start_time);
                ((RadioButton) arg0.findViewById(R.id.epg))
                        .setCompoundDrawables(null, null, null, null);
                ((RadioButton) arg0.findViewById(R.id.back1))
                        .setCompoundDrawables(null, null, null, null);
                ((RadioButton) arg0.findViewById(R.id.back2))
                        .setCompoundDrawables(null, null, null, null);
                ((RadioButton) arg0.findViewById(R.id.back3))
                        .setCompoundDrawables(null, null, null, drawable);
                ((RadioButton) arg0.findViewById(R.id.epg))
                        .setTextColor(getResources().getColor(R.color.gray_666666));
                ((RadioButton) arg0.findViewById(R.id.back1))
                        .setTextColor(getResources().getColor(R.color.gray_666666));
                ((RadioButton) arg0.findViewById(R.id.back2))
                        .setTextColor(getResources().getColor(R.color.gray_666666));
                ((RadioButton) arg0.findViewById(R.id.back3))
                        .setTextColor(getResources().getColor(R.color.blue_0085d1));
                break;
            case R.id.back2:
                String day2 = (String) ((RadioButton) arg0.findViewById(R.id.back2)).getText();
                String today2 = today_date.substring(0, 5) + day2.replace("/", ".");
                String start_t2 = today2 + " 00:00:59";
                String end_t2 = today2 + " 23:59:59";
                getEPG(cid, start_t2, end_t2, start_time);
                ((RadioButton) arg0.findViewById(R.id.epg))
                        .setCompoundDrawables(null, null, null, null);
                ((RadioButton) arg0.findViewById(R.id.back1))
                        .setCompoundDrawables(null, null, null, null);
                ((RadioButton) arg0.findViewById(R.id.back2))
                        .setCompoundDrawables(null, null, null, drawable);
                ((RadioButton) arg0.findViewById(R.id.back3))
                        .setCompoundDrawables(null, null, null, null);
                ((RadioButton) arg0.findViewById(R.id.epg))
                        .setTextColor(getResources().getColor(R.color.gray_666666));
                ((RadioButton) arg0.findViewById(R.id.back1))
                        .setTextColor(getResources().getColor(R.color.gray_666666));
                ((RadioButton) arg0.findViewById(R.id.back2))
                        .setTextColor(getResources().getColor(R.color.blue_0085d1));
                ((RadioButton) arg0.findViewById(R.id.back3))
                        .setTextColor(getResources().getColor(R.color.gray_666666));
                break;
            case R.id.back1:
                String day3 = (String) ((RadioButton) arg0.findViewById(R.id.back1)).getText();
                String today3 = today_date.substring(0, 5) + day3.replace("/", ".");
                String start_t3 = today3 + " 00:00:59";
                String end_t3 = today3 + " 23:59:59";
                getEPG(cid, start_t3, end_t3, start_time);
                ((RadioButton) arg0.findViewById(R.id.epg))
                        .setCompoundDrawables(null, null, null, null);
                ((RadioButton) arg0.findViewById(R.id.back1))
                        .setCompoundDrawables(null, null, null, drawable);
                ((RadioButton) arg0.findViewById(R.id.back2))
                        .setCompoundDrawables(null, null, null, null);
                ((RadioButton) arg0.findViewById(R.id.back3))
                        .setCompoundDrawables(null, null, null, null);
                ((RadioButton) arg0.findViewById(R.id.epg))
                        .setTextColor(getResources().getColor(R.color.gray_666666));
                ((RadioButton) arg0.findViewById(R.id.back1))
                        .setTextColor(getResources().getColor(R.color.blue_0085d1));
                ((RadioButton) arg0.findViewById(R.id.back2))
                        .setTextColor(getResources().getColor(R.color.gray_666666));
                ((RadioButton) arg0.findViewById(R.id.back3))
                        .setTextColor(getResources().getColor(R.color.gray_666666));
                break;
        }
    }

    //播放监听
    IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(IMediaPlayer mp) {
            // TODO Auto-generated method stub
//            mPlayControl.setVideoLength(mSurface.getDuration());
        }
    };

    //播放完成事件绑定事件监听
    IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        time = (int) mSurface.getCurrentPosition();
        mSurface.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurface.requestFocus();
        mSurface.seekTo(time);
        mSurface.start();
    }

    @Override
    protected void onDestroy() {
        long old = System.currentTimeMillis();
        super.onDestroy();

    }

    // 换台
    public void changeChannel(int position) {
        Lives lives = channelList.get(position);
        cid = lives.getCid();
        mNetHandler.sendEmptyMessage(2);
        start_time = "0";
        getEPG(cid, today_date, today_date, start_time);
    }

    //播放控制层
    private final OnPlayerControlListener mPlayerControlListener = new OnPlayerControlListener() {
        @Override
        public void onPlayPause() {
            if (mSurface.isPlaying()) {
                mSurface.pause();
                mPlayControl.setState(mSurface.isPlaying());
            } else {
                mSurface.start();
                mPlayControl.setState(mSurface.isPlaying());
            }
            mPlayControl.showOverlay();
        }

        @Override
        public void onSeekTo(int delta) {
            // unseekable stream
            if (mSurface.getDuration() <= 0)
                return;

            if (delta < 0)
                delta = 0;
            mSurface.seekTo(delta);
            mPlayControl.setState(mSurface.isPlaying());
            mPlayControl.onSeekTo(delta);
        }

        @Override
        public void onState() {
            mPlayControl.setState(mSurface.isPlaying());
        }

        @Override
        public int onVideoLength() {       //获取视频时长
            mPlayControl.setVideoLength(mSurface.getDuration());
            return mSurface.getDuration();
        }

        @Override
        public int onCurrentPosition() {       //获取当前播放进度
            return mSurface.getCurrentPosition();
        }

        @Override
        public int onTouchCurrentPosition() {      //获取当前播放进度
            return mSurface.getCurrentPosition();
        }

        @Override
        public boolean canShowProgress() {
            if (mSurface != null) {
                return mSurface.isPlaying();

            }
            return false;
        }

        @Override
        public void setDefinition(ListView mListView) {
            mListView.setAdapter(mSelectDefiAdapter);
            if (definition.equals("1")) {
                mSelectDefiAdapter.changeSelectedPosition(2);
            } else if (definition.equals("2")) {
                mSelectDefiAdapter.changeSelectedPosition(1);
            } else if (definition.equals("4")) {
                mSelectDefiAdapter.changeSelectedPosition(0);
            } else {
                mSelectDefiAdapter.changeSelectedPosition(2);
            }
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    time = mSurface.getCurrentPosition();
                    String string = Definlist.get(i);
                    if(string.equals(getString(R.string.sd))){
                        definition = "1";
                        mSelectDefiAdapter.changeSelectedPosition(2);
                    }else if(string.equals(getString(R.string.sd_h))){
                        definition = "2";
                        mSelectDefiAdapter.changeSelectedPosition(1);
                    }else if(string.equals(getString(R.string.hd))){
                        definition = "4";
                        mSelectDefiAdapter.changeSelectedPosition(0);
                    }else{
                        definition = "1";
                        mSelectDefiAdapter.changeSelectedPosition(2);
                    }
                    if (isLive) {
                        doLiveAuth(cid, id, "2");
                    } else {
                        doTVODAuth(cid, cid_hui, "4");
                    }
                    mPlayControl.setPopuWindowDismiss();
                }
            });
        }

        @Override
        public void setChannelList(ListView mListView) {
            mListView.setAdapter(mSelectChannelAdapter);
            for (int i = 0; i < channelList.size(); i++) {
                Lives lives = channelList.get(i);
                if (lives.getCid().equals(cid)) {
                    mSelectChannelAdapter.changeSelectedPosition(i);
                    break;
                }
            }
        }

        @Override
        public void setDlna(ListView mListView) {

        }

        @Override
        public void setSeries(GridView mGridView) {

        }


        @Override
        public void getFavorite() {

        }

        @Override
        public void setOnBackPressed() {
            mSurface.stopPlayback();
            finish();
        }

        @Override
        public void isSmallVolBri() {
            if(OperateSharePreferences.getInstance(PlayLive.this).isSmallVolBri()){
                mPlayControl.setSmallVolBri(true);
            }else {
                mPlayControl.setSmallVolBri(false);
                OperateSharePreferences.getInstance(PlayLive.this).saveSmallVolBri(true);
            }
        }

        @Override
        public void isFullVolBri() {
            if(OperateSharePreferences.getInstance(PlayLive.this).isFullVolBri()){
                mPlayControl.setFullVolBri(true);
            }else {
                mPlayControl.setFullVolBri(false);
                OperateSharePreferences.getInstance(PlayLive.this).saveFullVolBri(true);
            }
        }

        @Override
        public void onRefreshDlna() {

        }

        @Override
        public void onShare() {

        }

    };

}
