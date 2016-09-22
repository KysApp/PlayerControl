package com.kys.player.example.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kys.player.example.R;
import com.kys.player.example.adapter.RecommendAdapter;
import com.kys.player.example.base.BaseFragment;
import com.kys.player.example.base.CommonApi;
import com.kys.player.example.base.MyApplication;
import com.kys.player.example.customview.swipeXListView.XListView;
import com.kys.player.example.tools.HttpJsonObjectHelper;
import com.kys.player.example.tools.Json2EntityHelper;
import com.kys.player.example.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Recommend extends BaseFragment {
    /*初始化recommend的listview的布局和需要的数据
    * 其中datas中的map传递的数据有：
    *   1.每一行的模块的类型标识字符串，如大图，点播，直播
    *   2.每一行所有被分类好的用arraylist保存的数据
    *   3.某一行有特殊需要传的数据，无需要则不传
    * */
    private List<Map<String, Object>> datas = new ArrayList<>();
    private RecommendAdapter recommendAdapter;
    private XListView listView = null;
    private boolean onRefresh = false;
    /*通过从缓存里取数据对这些变量赋值，任何一个为空，返回页面的时候，在logic中页面就需要刷新*/
    private JSONObject ColumnName = null;
    private JSONObject VODRecommend = null;
    private JSONObject LiveRecommend = null;
    private JSONObject BigPicRecommend = null;
    //点播推荐类型
    private Map columnCodeName = new HashMap();
    //请求标识
    private int count = 0;//0 栏目名称，1 首页大图，2 首页直播，3 首页点播

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.ui_recommend);
    }

    @Override
    public void logics() {
        if (recommendAdapter == null) {
            findViewById(R.id.title).setVisibility(View.GONE);
            listView = (XListView) findViewById(R.id.recommend_list);
            recommendAdapter = new RecommendAdapter(getMyActivity(), datas);
            listView.setAdapter(recommendAdapter);
            listView.setXListViewListener(new Listeners());
            listView.setPullLoadEnable(false);
        }
        //跳转登录之后返回该页面
        if (onRefresh) {
            onRefresh = false;
            listView.stopRefresh();
        }
        if (ColumnName == null && BigPicRecommend == null && LiveRecommend == null && VODRecommend == null) {
            if (CommonApi.isHaveNet(getMyActivity())) {
                listView.showRefreshing();
                count = 0;
                sendRequests(count);
            } else {
                listView.showRefreshing();
                loadFromCache();
            }
        }
    }

    /**
     * 进行网络请求
     *
     * @param count 发送请求的接口标识
     */
    private void sendRequests(int count) {
        Map<String, String> map = new HashMap<>();
        String url = "";
        String interfaceName = "";
        switch (count) {
            //栏目名称
            case 0:
//                MyLogs.e("getColumnName", "yes");
                url = MyApplication.HOST_URL + "getcolumn.jsp";
                map.put("pageno", "1");
                map.put("numperpage", "10");
                map.put("columncode", "0M");
                interfaceName = "ColumnName";
                break;
            //首页大图
            case 1:
//                MyLogs.e("getBigPicRecommend", "yes");
                url = MyApplication.HOST_URL + "getfirstpage.jsp";
                interfaceName = "BigPicRecommend";
                break;
            //首页直播
            case 2:
//                MyLogs.e("getLiveRecommend", "yes");
                url = MyApplication.HOST_URL + "getchannel.jsp";
                map = new HashMap<>();
                map.put("pageno", "1");
                map.put("numperpage", "10");
                map.put("columncode", "0L");
                map.put("mediaservices", "2");
                interfaceName = "LiveRecommend";
                break;
            //首页点播
            case 3:
//                MyLogs.e("getVODRecommend", "yes");
                url = MyApplication.HOST_URL + "getvodrecommend.jsp";
                map = new HashMap<>();
                map.put("pageno", "1");
                map.put("numperpage", "100");
                map.put("columncode", "0M");
                map.put("mediaservices", "2");
                interfaceName = "VODRecommend";
                break;
        }
        HttpJsonObjectHelper.getInstance(getMyActivity().getApplicationContext()).getHttpNeedTokenData(url, map, new Listeners(interfaceName));
    }

    /**
     * 从缓存加载数据
     * 1.当json转化失败说明没有缓存，则需要从网络请求
     * 2.当所有模块的returncode均为0则正常去加载页面，否则说明某一模块的数据为空，则需要去网络请求数据
     **/
    private void loadFromCache() {
        if (ColumnName == null)
            try {
                ColumnName = new JSONObject(CommonUtils.getStringPreference(getMyActivity(), "ColumnName"));
            } catch (JSONException e) {
            }
        if (BigPicRecommend == null)
            try {
                BigPicRecommend = new JSONObject(CommonUtils.getStringPreference(getMyActivity(), "BigPicRecommend"));
            } catch (JSONException e) {
            }
        if (LiveRecommend == null)
            try {
                LiveRecommend = new JSONObject(CommonUtils.getStringPreference(getMyActivity(), "LiveRecommend"));
            } catch (JSONException e) {
            }
        if (VODRecommend == null)
            try {
                VODRecommend = new JSONObject(CommonUtils.getStringPreference(getMyActivity(), "VODRecommend"));
            } catch (JSONException e) {
            }

        //若为刷新操作，则将数据重新填充
        if (onRefresh) {
            datas.clear();
        }
        //既没缓存又没有从网络请求出来数据的时候给出提示
        if (ColumnName == null && BigPicRecommend == null && LiveRecommend == null && VODRecommend == null) {
            Toast.makeText(getMyActivity(), getResources().getString(R.string.no_data), Toast.LENGTH_SHORT);
        } else {
            getColumnNameDataSort(ColumnName);
            BigPicDataSort(BigPicRecommend);
            LiveDataSort(LiveRecommend);
            VODDataSort(VODRecommend);
        }
        listView.stopRefresh();
        onRefresh = false;
    }

    /**
     * 对点播栏目数据进行分类，存入map变量columnCodeName
     * 将数据的columncode作为key，columnname作为value
     *
     * @param response：获取的栏目json数据
     */
    private void getColumnNameDataSort(JSONObject response) {
//        MyLogs.e("getColumnNameDataSort", "yes");
        if (response == null)
            return;
        JSONArray columncode = response.optJSONArray("columncode");
        JSONArray columnname = response.optJSONArray("columnname");
        if (columncode == null)
            return;
        int columncodeLength = columncode.length();
        for (int i = 0; i < columncodeLength; i++)
            columnCodeName.put(columncode.optString(i, ""), columnname.optString(i, ""));
    }

    /**
     * 对大图数据进行分类
     * 将分类好的数据逐一存入一个arraylist
     * 再将这个arraylist存入datas中
     *
     * @param response:读取的缓存的response
     */
    private void BigPicDataSort(JSONObject response) {
        List<Map<String, String>> data = new ArrayList<>();
//        MyLogs.e("BigPicDataSort", "yes");
        if (response != null && response.optString("returncode", "").equals("0")) {
            String posterpath = response.optString("posterpath");
            JSONArray configinfo = response.optJSONArray("configinfo");
            JSONArray imgPath = response.optJSONArray("firstpagefile");
            if (configinfo == null)
                return;
            int configinfoLength = configinfo.length();
            for (int i = 0; i < configinfoLength; i++) {
                Map<String, String> map = new HashMap<>();
                String imgUrl = "";
                if (imgPath != null && !imgPath.optString(i, "").equals("")) {
                    String[] img = imgPath.optString(i, "").split(";");
                    String path = "";
                    int imgLength = img.length;
                    for (int q = 0; q < imgLength; q++) {
                        if (!img[q].equals("")) {
                            path = img[q];
                            break;
                        }
                    }
                    imgUrl = MyApplication.HOST_URL + posterpath + path;
                }
                map.put("imgUrl", imgUrl);
                String configinfoString = configinfo.optString(i, "").replace("[", "").replace("]", "");
                if (!configinfoString.equals("")) {
                    String[] configInfoParams = configinfoString.split(";");
                    String[] param = configInfoParams[0].split(",");
                    String[] paramData = configInfoParams[1].split(",");
                    if (param != null && paramData != null) {
                        int paramLength = param.length;
                        int paramDataLength = paramData.length;
                        if (paramLength == paramDataLength)
                            for (int j = 0; j < paramLength; j++)
                                map.put(param[j], paramData[j]);
                        else
                            map.put("type", "null");
                    } else
                        map.put("type", "null");
                } else map.put("type", "null");
                if (map.get("type")!=null&&(map.get("type").equals("tv")||map.get("type").equals("programcode")))
                    data.add(map);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("type", "BIGPICTURE");
        map.put("object", data);
        datas.add(map);
        recommendAdapter.notifyDataSetChanged();
    }

    /**
     * 对直播数据进行分类
     * 整体编写思路同大图
     *
     * @param response:读取的缓存的response
     */
    private void LiveDataSort(JSONObject response) {
//        MyLogs.e("LiveDataSort", "yes");
        List<Map<String, String>> live = new ArrayList<>();
        if (response != null && response.optString("returncode", "").equals("0")) {
            JSONArray channelcode = response.optJSONArray("channelcode");
            JSONArray channelname = response.optJSONArray("channelname");
            JSONArray imgPath = response.optJSONArray("filename");
            if (channelcode == null)
                return;
            int channelcodeLength = channelcode.length();
            for (int i = 0; i < channelcodeLength; i++) {
                Map<String, String> map = new HashMap<>();
                map.put("channelcode", channelcode.optString(i, ""));
                map.put("channelname", channelname.optString(i, ""));
                map.put("imgUrl", MyApplication.HOST_URL + imgPath.optString(i, ""));
                live.add(map);
            }
            Map map = new HashMap();
            map.put("type", "LIVE");
            map.put("object", live);
            datas.add(map);
            recommendAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 对点播数据进行分类
     * 整体编写思路同大图
     *
     * @param response:读取的缓存的response
     */
    private void VODDataSort(JSONObject response) {
//        MyLogs.e("VODDataSort", "yes");
        List<Map<String, Object>> vodDatas = Json2EntityHelper.streamArray2Videos4Recommend(response, columnCodeName);
        if (vodDatas != null && vodDatas.size() != 0) {
            datas.addAll(vodDatas);
            recommendAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 监听类
     */
    class Listeners implements XListView.IXListViewListener
            , HttpJsonObjectHelper.HttpJsonObjectHelperListener {
        private String interfaceName;

        public Listeners() {
        }

        public Listeners(String interfaceName) {
            this.interfaceName = interfaceName;
        }

        @Override
        public void onSuccess(JSONObject response) {
            if (response != null)
                CommonUtils.saveStringPreference(getMyActivity(), interfaceName, response.toString());
            if (count < 3) {
                count++;
                sendRequests(count);
            } else
                loadFromCache();
        }

        @Override
        public void onErrors(Throwable ex, boolean isOnCallback) {
//            MyLogs.e("onErrors", "yes");
            listView.stopRefresh();
            onRefresh = false;
            Toast.makeText(getMyActivity(), getResources().getString(R.string.poor_network), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRefreshData() {
//            MyLogs.e("onRefreshData", "yes");
            onRefresh = false;
            listView.stopRefresh();
            onRefresh();
        }

        @Override
        public void goLogin() {
//            MyLogs.e("goLogin", "yes");
            Intent intent = new Intent();
            intent.setClass(getMyActivity(), LoginZX.class);
            startActivity(intent);
        }

        //刷新
        @Override
        public void onRefresh() {
//            MyLogs.e("onRefresh", "yes");
            if (onRefresh)
                return;
            onRefresh = true;
            //使页面重新加载数据
            ColumnName = null;
            BigPicRecommend = null;
            LiveRecommend = null;
            VODRecommend = null;
            count = 0;
            sendRequests(count);
        }


        @Override
        public void onLoadMore() {

        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
