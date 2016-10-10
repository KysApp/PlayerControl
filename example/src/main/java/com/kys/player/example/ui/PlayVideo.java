package com.kys.player.example.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kys.player.example.R;
import com.kys.player.example.adapter.DevAdapter;
import com.kys.player.example.adapter.SelectDefiAdapter;
import com.kys.player.example.adapter.VideoSeriesAdapter;
import com.kys.player.example.adapter.VideoSeriesFullAdapter;
import com.kys.player.example.base.CommonApi;
import com.kys.player.example.base.MyApplication;
import com.kys.player.example.base.OperateSharePreferences;
import com.kys.player.example.customview.NoScrollGridView;
import com.kys.player.example.entity.Video;
import com.kys.player.example.tools.HttpJsonObjectHelper;
import com.kys.player.example.tools.Json2EntityHelper2;
import com.kys.playercontrol.widget.PlayControl;
import com.kys.playercontrol.interfaces.OnPlayerControlListener;
import com.kys.playercontrol.widget.Rescourse;
import com.zxt.dlna.activity.SettingActivity;
import com.zxt.dlna.dmc.SetAVTransportURIActionCallback;
import com.zxt.dlna.dmp.DeviceItem;
import com.zxt.dlna.dms.MediaServer;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.json.JSONObject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.sample.widget.media.IjkVideoView;

public class PlayVideo extends Activity {
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (mPlayControl != null) {
            mPlayControl.onBackPressed();
        } else {
            finished();
        }
    }

    public void finished(){
        mSurface.stopPlayback();
        getApplicationContext().unbindService(serviceConnection);
        super.onBackPressed();
    }

    private final static String TAG = PlayVideo.class.getSimpleName();
    private IjkVideoView mSurface;
    private LinearLayout ll_summary;
    private NoScrollGridView gV_tv_series;
    private VideoSeriesAdapter tv_adapter;
    private ArrayList<Video> seriesList = new ArrayList<Video>();
    private TextView tv_director, tv_main_actors, tv_year, tv_area,
            tv_type, tv_desc, tv_title;
    String date;
    private View mOverlayContent;
    private FrameLayout frame_control;
    private RelativeLayout mOverlayPlayer;

    private String mPath = "";
    private Video video;

    int screenWidth;
    private LinearLayout layout_title, layout_year, layout_type, layout_area, layout_director, layout_actors, layout_seekbar;
    private int pageSize = 18;
    public static int startIndex = 1;
    public static int totalResults = 0;
    private String authorizationid = "";
    private String breakpoint = "0";
    private String definition = "1";
    int position = 0;
    private String favoritetype = "1";
    private boolean isBookMark = false;
    private ImageView img_desc;
    private boolean isDescShow;
    private LinearLayout layout_desc;
    private String favoContentcode = "";
    private String contenCode = "";
    private PlayControl mPlayControl;
    private boolean isFavorite = false;
    VideoSeriesFullAdapter mVideoSeriesFullAdapter;
    SelectDefiAdapter mSelectDefiAdapter;
    private ProgressBar mProgressBar;
    List<String> Definlist = new ArrayList<String>();
    private OperateSharePreferences op;
    private int time = 0;
    private DeviceListRegistryListener deviceListRegistryListener;
    public static MediaServer mediaServer;
    private ArrayList<DeviceItem> mDmrList = new ArrayList<DeviceItem>();
    private DevAdapter mDmrDevAdapter;
    private AndroidUpnpService upnpService;
    private DeviceItem mDeviceItem;

    private Handler mNetHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0: // 请求分集信息
                    getSeries();
                    break;
                case 2: // 视频播放鉴权
                    doVodAuth();
                    break;
                case 3: // 请求播放地址
                    getVodUrl();
                    break;
            }
        }
    };

    //连接服务
    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {

            mDmrList.clear();

            upnpService = (AndroidUpnpService) service;

            if (mediaServer == null
                    && SettingActivity.getDmsOn(PlayVideo.this)) {
                try {
                    //初始化MediaServer
                    mediaServer = new MediaServer(PlayVideo.this);
                    upnpService.getRegistry()
                            .addDevice(mediaServer.getDevice());
                    //获取DLNA服务设备
                    DeviceItem localDevItem = new DeviceItem(
                            mediaServer.getDevice());

                    deviceListRegistryListener.deviceAdded(localDevItem);

                } catch (Exception ex) {
                    // TODO: handle exception
                    Toast.makeText(PlayVideo.this,
                            "无法获取更多设备", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
            }

            for (Device device : upnpService.getRegistry().getDevices()) {
                if (device.getType().getNamespace().equals("schemas-upnp-org")
                        && device.getType().getType().equals("MediaServer")) {
                    final DeviceItem display = new DeviceItem(device, device
                            .getDetails().getFriendlyName(),
                            device.getDisplayString(), "(REMOTE) "
                            + device.getType().getDisplayString());
                    deviceListRegistryListener.deviceAdded(display);
                }
            }

            // Getting ready for future device advertisements
            //启动DLNA设备监听
            upnpService.getRegistry().addListener(deviceListRegistryListener);
            // Refresh device list
            //搜索DLNA设备
            upnpService.getControlPoint().search();
        }

        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_play_video);
        op = OperateSharePreferences.getInstance(this);
        screenWidth = CommonApi.getScreenWidth(PlayVideo.this);
        Intent intent = getIntent();
        startIndex = 1;
        video = intent.getParcelableExtra("video");
        // 获取当前时间，年月日
        layout_title = (LinearLayout) findViewById(R.id.layout_title);
        layout_year = (LinearLayout) findViewById(R.id.layout_year);
        layout_type = (LinearLayout) findViewById(R.id.layout_type);
        layout_area = (LinearLayout) findViewById(R.id.layout_area);
        layout_director = (LinearLayout) findViewById(R.id.layout_director);
        layout_actors = (LinearLayout) findViewById(R.id.layout_actors);
        gV_tv_series = (NoScrollGridView) findViewById(R.id.gV_tv_series);
        tv_adapter = new VideoSeriesAdapter(this, seriesList);
        gV_tv_series.setAdapter(tv_adapter);
        ll_summary = (LinearLayout) findViewById(R.id.ll_summary);
        mDmrDevAdapter = new DevAdapter(PlayVideo.this, 0, mDmrList);
        op = OperateSharePreferences.getInstance(this);
        if(op.getPlayModel() != null && !op.getPlayModel().equals("")){
            definition= op.getPlayModel();
        }else if(op.getPlayModel().equals("2")){
            definition = "1";
        }
        initPlayer();
        if (video.getProgramcode() == null) video.setProgramcode("");
        if (video.getCode() == null) video.setCode("");
        contenCode = video.getCode();
        getBasicVodInfo();
        //添加清晰度
        Definlist.add(getString(R.string.hd));
        Definlist.add(getString(R.string.sd_h));
        Definlist.add(getString(R.string.sd));
        mSelectDefiAdapter = new SelectDefiAdapter(PlayVideo.this, Definlist);
        mVideoSeriesFullAdapter = new VideoSeriesFullAdapter(PlayVideo.this, seriesList);
        //开启dlna监听
        deviceListRegistryListener = new DeviceListRegistryListener();
        getApplicationContext().bindService(
                new Intent(this, AndroidUpnpServiceImpl.class),
                serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 根据programcode获取节目详细信息
     */
    private void getVodInfoByProgramcode() {
        Map<String, String> map = new HashMap<>();
        map.put("programcode", video.getProgramcode());

        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "getvodinfobyprogramcode.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.optString("returncode").equals("0")) {
                    ArrayList<Video> list = new ArrayList<Video>();
                    try {
                        list = Json2EntityHelper2.programcode2VodInfo(response, list);
                        if (list != null && list.size() > 0) {
                            video = list.get(0);
                        }
                        setVideoInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                        setVideoInfo();
                    }
                } else {
                    getBasicVodInfo();
                }
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PlayVideo.this, "Programcode", Toast.LENGTH_SHORT).show();
                setVideoInfo();
            }

            @Override
            public void onRefreshData() {
                getVodInfoByProgramcode();
            }

            @Override
            public void goLogin() {
                Intent intent = new Intent();
                intent.setClass(PlayVideo.this, LoginZX.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 根据contentcode获取节目详细信息
     */
    private void getVodInfoByContentcode() {
        Map<String, String> map = new HashMap<>();
        map.put("contentcode", video.getCode());
        map.put("columncode", video.getColumncode());

        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "getvodinfobycontentcode.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.optString("returncode").equals("0")) {
                    ArrayList<Video> list = new ArrayList<Video>();
                    try {
                        list = Json2EntityHelper2.programcode2VodInfo(response, list);
                        if (list != null && list.size() > 0) {
                            video = list.get(0);
                        }
                        setVideoInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getBasicVodInfo();
                }
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PlayVideo.this, "Contentcode", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRefreshData() {
                getVodInfoByContentcode();
            }

            @Override
            public void goLogin() {
                Intent intent = new Intent();
                intent.setClass(PlayVideo.this, LoginZX.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取节目书签
     */
    public void getVodBookmark() {
        Map<String, String> map = new HashMap<>();
        map.put("contentcode", video.getCode());
        map.put("columncode", video.getColumncode());
        map.put("bookmarktype", "1");

        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "getuserbookmark.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.optString("returncode").equals("0")
                            && response.optInt("breakpoint", 0) > 5000) {
                        breakpoint = response.optString("breakpoint");
                        mPlayControl.setBookMark(response.optInt("breakpoint", 0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PlayVideo.this, "getVodBookmark", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRefreshData() {
                getVodBookmark();
            }

            @Override
            public void goLogin() {
                Intent intent = new Intent();
                intent.setClass(PlayVideo.this, LoginZX.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 添加节目书签
     */
    public void addVodBookmark() {
        Map<String, String> map = new HashMap<>();
        map.put("contentcode", video.getCode());
        map.put("columncode", video.getColumncode());
        map.put("bookmarktype", "1");
        map.put("breakpoint", breakpoint);

        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "doaddbookmark.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onRefreshData() {

            }

            @Override
            public void goLogin() {
                Intent intent = new Intent();
                intent.setClass(PlayVideo.this, LoginZX.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 获取分集信息
     */
    private void getSeries() {
        Map<String, String> map = new HashMap<>();
        map.put("pageno", startIndex + "");
        map.put("numperpage", pageSize + "");
        map.put("columncode", video.getColumncode());
        map.put("seriesprogramcode", video.getSeriesprogramcode());

        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "getserieschild.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    favoContentcode = video.getCode();
                    totalResults = response.optInt("totalcount", 0);
                    seriesList = Json2EntityHelper2.streamArray2Series(response, seriesList);
                    favoritetype = "4";
//                    chkVodFavorite();
                    if (seriesList.size() < totalResults) {
                        startIndex = startIndex + 1;
                        getSeries();
                    } else {
                        video = seriesList.get(0);
                        if (seriesList != null)
                            setVideoSeriesList();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    favoContentcode = video.getCode();
                    favoritetype = "1";
//                    chkVodFavorite();
                    setVideoSeriesList();
                }
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {
                favoContentcode = video.getCode();
                favoritetype = "1";
//                chkVodFavorite();
                setVideoSeriesList();
            }

            @Override
            public void onRefreshData() {
                getSeries();
            }

            @Override
            public void goLogin() {
                Intent intent = new Intent();
                intent.setClass(PlayVideo.this, LoginZX.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 播放鉴权
     */
    public void doVodAuth() {
        Map<String, String> map = new HashMap<>();
        map.put("programcode", video.getProgramcode());
        map.put("columncode", video.getColumncode());
        map.put("contenttype", "1");

        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "doauth.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.optString("returncode").equals("0")) {
                    authorizationid = response.optJSONArray("authorizationid").optString(0);
                    getVodUrl();
                } else {
                    Toast.makeText(PlayVideo.this, response.optString("errormsg", ""), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PlayVideo.this, "doVodAuth", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRefreshData() {
                doVodAuth();
            }

            @Override
            public void goLogin() {
                Intent intent = new Intent();
                intent.setClass(PlayVideo.this, LoginZX.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取点播播放地址
     */
    private void getVodUrl() {
        final Map<String, String> map = new HashMap<>();
        map.put("programcode", video.getProgramcode());
        map.put("breakpoint", breakpoint);
        map.put("authidsession", authorizationid);
        map.put("definition", definition);
        map.put("mediaservice", "2");
        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "getvodurl.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.optString("returncode", "").equals("0")) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mPath = response.optString("url", "");
                        mSurface.requestFocus();
                        mSurface.setVideoPath(mPath);
                        mSurface.seekTo(time);
                        mSurface.start();
//                        getVodBookmark();
                    } else {
                        Toast.makeText(PlayVideo.this, response.optString("errormsg", ""), Toast.LENGTH_SHORT).show();
                        mSurface.pause();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(PlayVideo.this, "getVodUrl", Toast.LENGTH_SHORT).show();
                    mSurface.pause();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PlayVideo.this, "getVodUrl", Toast.LENGTH_SHORT).show();
                    mSurface.pause();
                }
                chkVodFavorite();
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PlayVideo.this, "getVodUrl", Toast.LENGTH_SHORT).show();
                mSurface.pause();
            }

            @Override
            public void onRefreshData() {
                doVodAuth();
            }

            @Override
            public void goLogin() {
                Intent intent = new Intent();
                intent.setClass(PlayVideo.this, LoginZX.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 添加收藏
     */
    public void addVodFavorite() {
        Map<String, String> map = new HashMap<>();
        map.put("contentcode", favoContentcode);
        map.put("columncode", video.getColumncode());
        map.put("favoritetype", favoritetype);

        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "doaddfavorite.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.optString("returncode", "").equals("0")) {
                        isFavorite = true;
                        mPlayControl.setFavoriteBg(isFavorite);   //收藏图片切换
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PlayVideo.this, "addVodFavorite", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRefreshData() {
                addVodFavorite();
            }

            @Override
            public void goLogin() {
                Intent intent = new Intent();
                intent.setClass(PlayVideo.this, LoginZX.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 删除收藏
     */
    public void delVodFavorite() {
        Map<String, String> map = new HashMap<>();
        map.put("contentcode", favoContentcode);
        map.put("columncode", video.getColumncode());
        map.put("favoritetype", favoritetype);

        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "dodelfavorite.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.optString("returncode").equals("0")) {
                        isFavorite = false;
                        mPlayControl.setFavoriteBg(isFavorite);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onRefreshData() {
                delVodFavorite();
            }

            @Override
            public void goLogin() {
                Intent intent = new Intent();
                intent.setClass(PlayVideo.this, LoginZX.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 检查是否收藏
     */
    private void chkVodFavorite() {
        Map<String, String> map = new HashMap<>();
        map.put("contentcode", favoContentcode);
        map.put("columncode", video.getColumncode());
        map.put("favoritetype", favoritetype);

        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "checkisfavorited.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.optString("returncode").equals("0")) {
                        isFavorite = true;
                        mPlayControl.setFavoriteBg(isFavorite);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onRefreshData() {
                chkVodFavorite();
            }

            @Override
            public void goLogin() {
                Intent intent = new Intent();
                intent.setClass(PlayVideo.this, LoginZX.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取信息
     */
    private void getBasicVodInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("contentcode", video.getCode());
        map.put("programcode", video.getProgramcode());
        map.put("columncode", video.getColumncode());

        HttpJsonObjectHelper.getInstance(getApplicationContext()).getHttpNeedTokenData(MyApplication.HOST_URL + "getbasicvodinfo.jsp", map, new HttpJsonObjectHelper.HttpJsonObjectHelperListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.optString("returncode").equals("0")) {
                        ArrayList<Video> videoList = Json2EntityHelper2.streamArray2Basic(response);
                        video = videoList.get(0);
                        if (video.getSeriesprogramcode() != null
                                && !video.getSeriesprogramcode().equals("")
                                && !video.getSeriesprogramcode().equals(video.getProgramcode())) {
                            favoritetype = "4";
                            video.setCode("");
                            video.setProgramcode(video.getSeriesprogramcode());
                            getBasicVodInfo();
                        } else {
                            setVideoInfo();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PlayVideo.this, "getBasicVodInfo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrors(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onRefreshData() {
                getBasicVodInfo();
            }

            @Override
            public void goLogin() {
                Intent intent = new Intent();
                intent.setClass(PlayVideo.this, LoginZX.class);
                startActivity(intent);
            }
        });
    }

    //添加视频详情
    private void setVideoInfo() {
        // TODO Auto-generated method stub
        if (video != null) {
            //导演
            tv_director = (TextView) ll_summary
                    .findViewById(R.id.tv_director);
            //主演
            tv_main_actors = (TextView) ll_summary
                    .findViewById(R.id.tv_main_actors);
            //年份
            tv_year = (TextView) ll_summary.findViewById(R.id.tv_year);
            //类型
            tv_type = (TextView) ll_summary.findViewById(R.id.tv_type);
            //地区
            tv_area = (TextView) ll_summary.findViewById(R.id.tv_area);
            tv_title = (TextView) ll_summary.findViewById(R.id.tv_title);
            //简介
            tv_desc = (TextView) findViewById(R.id.tv_desc);
            img_desc = (ImageView) findViewById(R.id.img_desc);
            layout_desc = (LinearLayout) findViewById(R.id.layout_desc);
            if (video.getDirector() == null
                    | "null".equals(video.getDirector())) {
                layout_director.setVisibility(View.GONE);
            } else {
                tv_director.setText(video.getDirector());
            }
            if (video.getActor() == null
                    | "null".equals(video.getActor())) {
                layout_actors.setVisibility(View.GONE);
            } else {
                tv_main_actors.setText(video.getActor());
            }
            if (video.getYear() == null
                    | "null".equals(video.getYear())) {
                layout_year.setVisibility(View.GONE);
            } else {
                tv_year.setText(video.getYear());
            }
            if (video.getArea() == null | "null".equals(video.getArea())) {
                layout_area.setVisibility(View.GONE);
            } else {
                tv_area.setText(video.getArea());
            }
            if (video.getType() == null | "null".equals(video.getType())) {
                layout_type.setVisibility(View.GONE);
            } else {
                tv_type.setText(video.getType());
            }
            if (video.getDescription() == null | "null".equals(video.getDescription())) {
                tv_desc.setVisibility(View.GONE);
            } else {
                tv_desc.setText(video.getDescription());
            }
            if (tv_desc.getLineCount() > 2) {
                final ViewGroup.LayoutParams mLayoutParams = tv_desc.getLayoutParams();
                layout_desc.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isDescShow) {
                            tv_desc.setMaxLines(tv_desc.getLineCount());
                            tv_desc.setLayoutParams(mLayoutParams);
                            isDescShow = true;
                        } else {
                            tv_desc.setLayoutParams(mLayoutParams);
                            tv_desc.setMaxLines(2);
                            img_desc.setImageResource(R.drawable.live_open);
                            isDescShow = false;
                        }
                    }
                });
            } else {
                img_desc.setVisibility(View.GONE);
            }
            tv_title.setText(video.getTitle().toString());

            ImageView imageView = (ImageView) ll_summary
                    .findViewById(R.id.img_playvideo);
            x.image().bind(imageView, video.getImg_url());
            getSeries();
        }
    }

    //添加视频剧集
    public void setVideoSeriesList() {
        if (seriesList.size() == 0) {
            video.setIndex(1);
            seriesList.add(video);
        }
        tv_adapter.checkedOnly(0);
        for (int i = 0; i < seriesList.size(); i++) {
            Video vv = seriesList.get(i);
            if (contenCode.equals(vv.getCode()) && vv.getCode() != null) {
                video = vv;
                tv_adapter.checkedOnly(i);
            }
        }
        Message message = mNetHandler.obtainMessage();
        message.what = 2;
        mNetHandler.sendMessage(message);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    //播放器播放器控制层
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initPlayer() {
        // TODO Auto-generated method stub
        mOverlayContent = findViewById(R.id.overlay_content);
        mOverlayPlayer = (RelativeLayout) findViewById(R.id.overlay_player);
        int screenWidth = CommonApi.getScreenWidth(PlayVideo.this);
        mOverlayPlayer.setLayoutParams(new LinearLayout.LayoutParams(
                screenWidth, screenWidth * 9 / 16));
        mSurface = (IjkVideoView) findViewById(R.id.player_surface);
        frame_control = (FrameLayout) findViewById(R.id.frame_control);
        mPlayControl = new PlayControl(this, mOverlayPlayer, mOverlayContent, 0);
        mPlayControl.setOnPlayerControlListener(mPlayerControlListener);
        mPlayControl.setDefinition(definition);

        mPlayerControlListener.isSmallVolBri();
        mProgressBar = (ProgressBar) mPlayControl.findViewById(R.id.progressBar);
        frame_control.addView((View) mPlayControl);
//        Rescourse.setImg_play_channel_bg(R.drawable.back);
//        KeyShow.setPlayer_overlay_back_show(false);
//        KeyStyle.setmTitleStyle(R.style.blue_0085d1_30_32);
        Rescourse.setPopupwindow_bg(R.color.white);
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
                Toast.makeText(PlayVideo.this, "无法播放视频", Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurface.requestFocus();
        mSurface.seekTo(time);
        mSurface.start();
    }

    //选集播放
    public void changeTv(int position) {
        this.position = position;
        video = seriesList.get(position);
        tv_adapter.checkedOnly(position);
        mVideoSeriesFullAdapter.checkedOnly(position);
        time = 0;
        isBookMark = false;
        doVodAuth();
    }

    //播放监听
    IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(IMediaPlayer mp) {
            // TODO Auto-generated method stub
            mProgressBar.setVisibility(View.GONE);
            mPlayerControlListener.onState();
        }
    };

    //播放完成事件绑定事件监听
    IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
        }
    };

    //将播放地址发送至DLNA服务，启动视频
    private void setTVUrl(String tvUrl) {
        try {
            Service localService = mDeviceItem.getDevice().findService(new UDAServiceType("AVTransport"));
            mDmrDevAdapter.notifyDataSetChanged();
            upnpService.getControlPoint().execute(
                    new SetAVTransportURIActionCallback(localService,
                            tvUrl, "", null,
                            3));
        } catch (NullPointerException e) {

        }
    }

    //播放控制层监听
    public final OnPlayerControlListener mPlayerControlListener = new OnPlayerControlListener() {
        @Override
        public void onPlayPause() {     //播放按钮监听
            if (mSurface.isPlaying()) {
                mSurface.pause();
                mPlayControl.setState(false);
            } else {
                mSurface.start();
                mPlayControl.setState(true);
            }
            onVideoLength();
            mPlayControl.showOverlay();
        }

        @Override
        public void onSeekTo(int delta) {       //播放器进度监听
            if (mSurface.getDuration() <= 0)
                return;
            if (delta < 0)
                delta = 0;
            mSurface.seekTo(delta);
            mPlayControl.setState(mSurface.isPlaying());
            mPlayControl.onSeekTo(delta);
        }

        @Override
        public void onState() {        //播放状态监听
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
        public boolean canShowProgress() {      //进度条显示监听
            if (mSurface != null) {
                return mSurface.isPlaying();

            }
            return false;
        }

        @Override
        public void setDefinition(ListView mListView) {     //清晰度数据添加展示
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
                    if (string.equals(getString(R.string.sd))) {
                        definition = "1";
                        mSelectDefiAdapter.changeSelectedPosition(2);
                    } else if (string.equals(getString(R.string.sd_h))) {
                        definition = "2";
                        mSelectDefiAdapter.changeSelectedPosition(1);
                    } else if (string.equals(getString(R.string.hd))) {
                        definition = "4";
                        mSelectDefiAdapter.changeSelectedPosition(0);
                    } else {
                        definition = "1";
                        mSelectDefiAdapter.changeSelectedPosition(2);
                    }
                    mPlayControl.setDefinition(definition);
                    doVodAuth();
                    mPlayControl.setPopuWindowDismiss();
                }
            });
        }

        @Override
        public void setChannelList(ListView mListView) {        //频道列表数据添加展示

        }

        @Override
        public void setDlna(ListView mListView) {       //投屏设备添加展示
            mListView.setAdapter(mDmrDevAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mDeviceItem = mDmrList.get(i);
                    setTVUrl(mPath);
                    mPlayControl.setPopuWindowDismiss();
                }
            });
        }

        @Override
        public void setSeries(GridView mGridView) {     //剧集添加展示
            mGridView.setAdapter(mVideoSeriesFullAdapter);
        }

        @Override
        public void getFavorite() {     //切换添加删除收藏

            if (isFavorite) {
                delVodFavorite();
            } else {
                addVodFavorite();
            }

        }

        @Override
        public void setOnBackPressed() {        //退出当前Activity
//            finished();
            mSurface.stopPlayback();
            getApplicationContext().unbindService(serviceConnection);
            finish();
        }

        @Override
        public void isSmallVolBri() {       //小屏时音量亮度指示图是否显示
            if (OperateSharePreferences.getInstance(PlayVideo.this).isSmallVolBri()) {
                mPlayControl.setSmallVolBri(true);
            } else {
                mPlayControl.setSmallVolBri(false);
                OperateSharePreferences.getInstance(PlayVideo.this).saveSmallVolBri(true);
            }
        }

        @Override
        public void isFullVolBri() {        //全屏时音量亮度指示图是否显示
            if (OperateSharePreferences.getInstance(PlayVideo.this).isFullVolBri()) {
                mPlayControl.setFullVolBri(true);
            } else {
                mPlayControl.setFullVolBri(false);
                OperateSharePreferences.getInstance(PlayVideo.this).saveFullVolBri(true);
            }
        }

        @Override
        public void onRefreshDlna() {       //DLNA设备刷新
            if (mDmrList.size() != 0) {
                upnpService.getRegistry().removeAllRemoteDevices();
            }
            upnpService.getControlPoint().search();
        }

        @Override       //全屏分享监听
        public void onShare() {

        }

    };

    //DLNA设备获取监听
    public class DeviceListRegistryListener extends DefaultRegistryListener {

		/* Discovery performance optimization for very slow Android devices! */

        @Override
        public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
        }

        @Override
        public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
        }

		/*
         * End of optimization, you can remove the whole block if your Android
		 * handset is fast (>= 600 Mhz)
		 */

        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {

            if (device.getType().getNamespace().equals("schemas-upnp-org")
                    && device.getType().getType().equals("MediaServer")) {
                final DeviceItem display = new DeviceItem(device, device
                        .getDetails().getFriendlyName(),
                        device.getDisplayString(), "(REMOTE) "
                        + device.getType().getDisplayString());
                deviceAdded(display);
            }

            if (device.getType().getNamespace().equals("schemas-upnp-org")
                    && device.getType().getType().equals("MediaRenderer")) {
                final DeviceItem dmrDisplay = new DeviceItem(device, device
                        .getDetails().getFriendlyName(),
                        device.getDisplayString(), "(REMOTE) "
                        + device.getType().getDisplayString());
                dmrAdded(dmrDisplay);
            }
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            final DeviceItem display = new DeviceItem(device,
                    device.getDisplayString());
            deviceRemoved(display);

            if (device.getType().getNamespace().equals("schemas-upnp-org")
                    && device.getType().getType().equals("MediaRenderer")) {
                final DeviceItem dmrDisplay = new DeviceItem(device, device
                        .getDetails().getFriendlyName(),
                        device.getDisplayString(), "(REMOTE) "
                        + device.getType().getDisplayString());
                dmrRemoved(dmrDisplay);
            }
        }

        @Override
        public void localDeviceAdded(Registry registry, LocalDevice device) {

            final DeviceItem display = new DeviceItem(device, device
                    .getDetails().getFriendlyName(), device.getDisplayString(),
                    "(REMOTE) " + device.getType().getDisplayString());
            deviceAdded(display);
        }

        @Override
        public void localDeviceRemoved(Registry registry, LocalDevice device) {

            final DeviceItem display = new DeviceItem(device,
                    device.getDisplayString());
            deviceRemoved(display);
        }

        public void deviceAdded(final DeviceItem di) {
            runOnUiThread(new Runnable() {
                public void run() {
                }
            });
        }

        public void deviceRemoved(final DeviceItem di) {
            runOnUiThread(new Runnable() {
                public void run() {
                }
            });
        }

        public void dmrAdded(final DeviceItem di) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (!mDmrList.contains(di)) {
                        mPlayControl.setDlnaRefresh(false);
                        mDmrList.add(di);
                        mDmrDevAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        public void dmrRemoved(final DeviceItem di) {
            runOnUiThread(new Runnable() {
                public void run() {
                    mDmrList.remove(di);
                    mDmrDevAdapter.notifyDataSetChanged();
                }
            });
        }
    }

}