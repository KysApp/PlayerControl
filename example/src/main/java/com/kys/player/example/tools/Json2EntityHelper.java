package com.kys.player.example.tools;


import com.kys.player.example.base.CommonApi;
import com.kys.player.example.base.MyApplication;
import com.kys.player.example.entity.EPG;
import com.kys.player.example.entity.Lives;
import com.kys.player.example.entity.Video;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bsy on 2016/7/28.
 */
public class Json2EntityHelper {

    /**
     * Lives实体类解析
     *
     * @param obj
     * @return
     */
    public static List<Lives> streamArray2Lives(JSONObject obj, List<Lives> list) {
//        ArrayList<Lives> list = new ArrayList<>();
        Lives lives;
        JSONArray channelname = obj.optJSONArray("channelname");
        JSONArray channelcode = obj.optJSONArray("channelcode");
        JSONArray filename = obj.optJSONArray("filename");
        if (channelcode != null) {
            for (int i = 0; i < channelcode.length(); i++) {
                lives = new Lives();
                lives.setCid(channelcode.optString(i));
                lives.setTitle(channelname.optString(i));
                lives.setImg_url(ImageUrlHelper.getImageUrl(filename.optString(i)));
                list.add(lives);
            }
        }
        return list;
    }

    /**
     * EPG实体类解析
     *
     * @param obj
     * @return
     */
    public static ArrayList<EPG> streamArray2Epg(JSONObject obj, String start_time) {
        ArrayList<EPG> list = new ArrayList<>();
        EPG epg;
        JSONArray prevuename = obj.optJSONArray("prevuename");
        JSONArray prevuecode = obj.optJSONArray("prevuecode");
        JSONArray begintime = obj.optJSONArray("begintime");
        JSONArray endtime = obj.optJSONArray("endtime");
        if (prevuecode == null) {
            return list;
        }

        for (int i = 0; i < prevuecode.length(); i++) {
            epg = new EPG();
            epg.setTitle(prevuename.optString(i));
            epg.setCid(prevuecode.optString(i));
            epg.setStart_time(begintime.optString(i));
            epg.setEnd_time(endtime.optString(i));
            long runtime = CommonApi.getRuntime(begintime.optString(i), endtime.optString(i));
            epg.setRun_time(String.valueOf(runtime));
            //判断epg的播放状态
            //通过日期判断是否是今天的EPG
            if (CommonApi.playDate_sub(epg.getStart_time().substring(0, 10)) > 0) {//开始日期大于当前日期，未播出
                epg.setStatus("0");
            } else if (CommonApi.playDate_sub(epg.getStart_time().substring(0, 10)) < 0) {//开始日期小于当前日期，已播出
                epg.setStatus("1");
            } else {//当天的节目单
                if (CommonApi.playtime_sub(epg.getStart_time().substring(11, 16)) > 0) {//开始时间大于当前时间，未播出
                    epg.setStatus("0");
                } else if (CommonApi.playtime_sub(epg.getEnd_time().substring(11, 16)) > 0 && CommonApi.playtime_sub(epg.getStart_time().substring(11, 16)) < 0) {//结束时间大于现在时间，开始时间小于现在时间，则说明该条EPG正在播放
                    epg.setIs_now_epg(true);
                    epg.setStatus("0");
                } else {
                    epg.setStatus("1");
                }
            }

            //判断是否是当前正在播出的epg
            if (start_time.equals("0")) {//第一遍请求epg信息，则按当前时间确定正在播出是哪条
                if (epg.getStatus().equals("0")) {
                    if (CommonApi.playtime_sub(epg.getEnd_time().substring(11, 16)) > 0 && CommonApi.playtime_sub(epg.getStart_time().substring(11, 16)) < 0) {//结束时间大于现在时间，开始时间小于现在时间，则说明该条EPG正在播放
                        epg.setIsplaying(true);
                    }
                }
            } else {
                if (epg.getStart_time().equals(start_time)) {//不是第一遍请求，则根据正在播放的开始时间确定正在播出的是哪条
                    epg.setIsplaying(true);
                }
            }

            list.add(epg);
        }
        return list;
    }

    /**
     * 获取收藏列表
     *
     * @param obj
     * @return
     */
    public static List<Video> streamArray2Video4Favourite(JSONObject obj, List<Video> list) {

        Video video;
        String posterpath = obj.optString("posterpath");
        JSONArray favoritename = obj.optJSONArray("favoritename");
        JSONArray favoritetype = obj.optJSONArray("favoritetype");
        JSONArray columncode = obj.optJSONArray("columncode");
        JSONArray contentcode = obj.optJSONArray("contentcode");
        JSONArray posterfilelist = obj.optJSONArray("posterfilelist");
        JSONArray genre = obj.optJSONArray("genre");
        JSONArray director = obj.optJSONArray("director");
        JSONArray savetime = obj.optJSONArray("savetime");
        if (favoritename != null) {
            for (int i = 0; i < favoritename.length(); i++) {
                video = new Video();
                video.setCode(contentcode.optString(i));
                video.setColumncode(columncode.optString(i));
//                video.setImg_url(ImageUrlHelper.getImageUrl(posterpath + posterfilelist.optString(i)));
                video.setTitle(favoritename.optString(i));
                video.setOnlinetime(CommonApi.formatDataAddSpace(savetime.optString(i)));
                if (director != null) {
                    video.setDirector(director.optString(i));
                    video.setType(genre.optString(i));
                }
                video.setFavoritetype(favoritetype.optString(i));
                list.add(video);
            }
        }
        return list;
    }

    /**
     * 获取书签列表
     *
     * @param obj
     * @return
     */
    public static List<Video> streamArray2Video4BookMark(JSONObject obj, List<Video> list) {
        Video video;
        String posterpath = obj.optString("posterpath");
        JSONArray bookmarkname = obj.optJSONArray("bookmarkname");
        JSONArray columncode = obj.optJSONArray("columncode");
        JSONArray contentcode = obj.optJSONArray("contentcode");
        JSONArray posterfilelist = obj.optJSONArray("posterfilelist");
        JSONArray breakpoint = obj.optJSONArray("breakpoint");
        JSONArray savetime = obj.optJSONArray("savetime");
        JSONArray bookmarktype = obj.optJSONArray("bookmarktype");
        if (bookmarkname != null) {
            for (int i = 0; i < bookmarkname.length(); i++) {
                video = new Video();
                video.setCode(contentcode.optString(i));
                video.setColumncode(columncode.optString(i));
//                video.setImg_url(ImageUrlHelper.getImageUrl(posterpath + posterfilelist.optString(i)));
                video.setTitle(bookmarkname.optString(i));
                video.setBreakpoint(breakpoint.optString(i));
                video.setYear(savetime.optString(i).substring(0, 10));
                video.setType(bookmarktype.optString(i));
                list.add(video);
            }
        }
        return list;
    }

    public static List<Video> streamArray2Videos4Search(JSONObject obj, List<Video> list) {
        //显示信息
        JSONArray programname = obj.optJSONArray("programname");
        JSONArray imgPath = obj.optJSONArray("posterfilelist");
        //需要向点播播放页面传的值
        JSONArray description = obj.optJSONArray("description");
        JSONArray recommendid = obj.optJSONArray("recommendid");
        JSONArray price = obj.optJSONArray("price");
        JSONArray actor = obj.optJSONArray("actor");
        JSONArray director = obj.optJSONArray("director");
        JSONArray contentcode = obj.optJSONArray("contentcode");
        JSONArray releasedate = obj.optJSONArray("releasedate");
        JSONArray countryname = obj.optJSONArray("countryname");
        JSONArray onlinetime = obj.optJSONArray("onlinetime");
        JSONArray language = obj.optJSONArray("language");
        JSONArray programcode = obj.optJSONArray("programcode");
        JSONArray seriesprogramcode = obj.optJSONArray("seriesprogramcode");
        String posterpath = obj.optString("posterpath", "");
        if (!posterpath.equals("") && programname != null) {
            int programnameLength = programname.length();
            for (int i = 0; i < programnameLength; i++) {
                Video video = new Video();
                String imgUrl = "";
                if (imgPath != null && !imgPath.optString(i, "").equals("")) {
                    String[] img = imgPath.optString(i, "").split(";");
                    String path = "";
                    for (int q = 0; q < img.length; q++) {
                        if (!img[q].equals("")) {
                            path = img[q];
                            break;
                        }
                    }
                    imgUrl = MyApplication.HOST_URL + posterpath + path;
                }
                video.setTitle(programname.optString(i, ""));
                video.setImg_url(imgUrl);
                if (description != null)
                    video.setDescription(description.optString(i, ""));
                if (recommendid != null)
                    video.setRating(recommendid.optString(i, ""));
                if (price != null)
                    video.setPrice(price.optString(i, ""));
                if (actor != null)
                    video.setActor(actor.optString(i, ""));
                if (director != null)
                    video.setDirector(director.optString(i, ""));
                if (contentcode != null)
                    video.setCode(contentcode.optString(i, ""));
                if (releasedate != null)
                    video.setYear(releasedate.optString(i, ""));
                if (countryname != null)
                    video.setArea(countryname.optString(i, ""));
                if (onlinetime != null)
                    video.setOnlinetime(onlinetime.optString(i, ""));
                if (language != null)
                    video.setLanguage(language.optString(i, ""));
                if (programcode != null)
                    video.setProgramcode(programcode.optString(i, ""));
                if (seriesprogramcode != null)
                    video.setSeriesprogramcode(seriesprogramcode.optString(i, ""));
                list.add(video);
            }
        }
        return list;
    }
    public static List<Map<String, Object>> streamArray2Videos4Recommend(JSONObject obj, Map<String,String> columnCodeName) {
        List<Map<String, Object>> datas = new ArrayList<>();
        if(obj!=null&&obj.optString("returncode", "").equals("0")) {
            //columncodes是点播的一级分类
            JSONArray columncodes = obj.optJSONArray("columncode");
            //sort存放经过由小到大排序的columncode种类，每个种类仅存一次
            List<String> sort = new ArrayList<>();
            if (columncodes == null)
                return datas;
            int columncodesLength = columncodes.length();
            for (int i = 0; i < columncodesLength; i++) {
                if (sort.size() == 0)
                    sort.add(columncodes.optString(i));
                else {
                    String code = columncodes.optString(i);
                    for (int j = 0; j < sort.size(); j++) {
                        String sorts = sort.get(j);
                        if (code.equals(sorts))
                            break;
                        else {
                            if (code.compareTo(sorts) < 0) {
                                String temp = sorts;
                                sort.set(j, code);
                                code = temp;
                            }
                            if (j == sort.size() - 1)
                                sort.add(code);
                        }
                    }
                }
            }
        /*取出需要的数据 begin*/
            String posterpath = obj.optString("posterpath", "");
            //显示信息
            JSONArray programname = obj.optJSONArray("programname");
            JSONArray imgPath = obj.optJSONArray("posterfilelist");
            //需要向点播播放页面传的值
            JSONArray description = obj.optJSONArray("description");
            JSONArray recommendid = obj.optJSONArray("recommendid");
            JSONArray price = obj.optJSONArray("price");
            JSONArray actor = obj.optJSONArray("actor");
            JSONArray director = obj.optJSONArray("director");
            JSONArray contentcode = obj.optJSONArray("contentcode");
            JSONArray onlinetime = obj.optJSONArray("onlinetime");
            JSONArray language = obj.optJSONArray("language");
            JSONArray programcode = obj.optJSONArray("programcode");
            JSONArray seriesnum = obj.optJSONArray("seriesnum");
            JSONArray seriesprogramcode = obj.optJSONArray("seriesprogramcode");
        /*end*/
            if (!posterpath.equals("") && programname != null) {
            /*开始进行分类：
            将相同columncode的放入一个arraylist，再将该arraylist加入datas
            arraylist的个数由columncode的种类做决定*/
                int soreSize = sort.size();
                int programnameLength = programname.length();
                for (int k = 0; k < soreSize; k++) {
                    String column = sort.get(k);
                    List<Video> videos = new ArrayList<>();
                    for (int i = 0; i < programnameLength; i++) {
                        if (!columncodes.optString(i).equals(column))
                            continue;
                        Video video = new Video();
                        String imgUrl = "";
                        if (imgPath != null && !imgPath.optString(i, "").equals("")) {
                            String[] img = imgPath.optString(i, "").split(";");
                            String path = "";
                            for (int q = 0; q < img.length; q++) {
                                if (!img[q].equals("")) {
                                    path = img[q];
                                    break;
                                }
                            }
                            imgUrl = MyApplication.HOST_URL + posterpath + path;
                        }
                        video.setTitle(programname.optString(i, ""));
                        video.setImg_url(imgUrl);
                        if (description != null)
                            video.setDescription(description.optString(i, ""));
                        if (recommendid != null)
                            video.setRating(recommendid.optString(i, ""));
                        if (price != null)
                            video.setPrice(price.optString(i, ""));
                        if (actor != null)
                            video.setActor(actor.optString(i, ""));
                        if (director != null)
                            video.setDirector(director.optString(i, ""));
                        if (contentcode != null)
                            video.setCode(contentcode.optString(i, ""));
                        if (onlinetime != null)
                            video.setOnlinetime(onlinetime.optString(i, ""));
                        if (language != null)
                            video.setLanguage(language.optString(i, ""));
                        if (programcode != null)
                            video.setProgramcode(programcode.optString(i, ""));
                        if (seriesprogramcode != null)
                            video.setSeriesprogramcode(seriesprogramcode.optString(i, ""));
                        if (seriesnum != null) {
                            String seriesNum = seriesnum.optString(i, "");
                            if (seriesNum != null && !seriesNum.equals(""))
                                video.setIndex(Integer.parseInt(seriesNum));
                        }
                        videos.add(video);
                    }
                    if (videos != null) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", "VOD");
                        map.put("column", column);
                        String columnName = "";
                        if(columnCodeName!=null)
                            columnName = columnCodeName.get(column);
                        map.put("columnname", columnName);
                        map.put("object", videos);
                        datas.add(map);
                    }
                }
            }
        }
        return datas;
    }
}
