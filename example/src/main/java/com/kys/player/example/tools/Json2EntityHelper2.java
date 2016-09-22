package com.kys.player.example.tools;

import com.kys.player.example.entity.Video;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zgl on 2016/7/28.
 */
public class Json2EntityHelper2 {

    /**
     * @param obj
     * @return
     */
    public static ArrayList<Video> streamArray2VideoList(JSONObject obj) {
        ArrayList<Video> list = new ArrayList<Video>();
        Video videos;
        JSONArray contentcodes = obj.optJSONArray("contentcode");
        JSONArray codes = obj.optJSONArray("programcode");
        JSONArray titles = obj.optJSONArray("programname");
        JSONArray images = obj.optJSONArray("poster4");
        JSONArray types = obj.optJSONArray("genre");
        JSONArray decs = obj.optJSONArray("detaildescribed");
        JSONArray actors = obj.optJSONArray("actor");
        JSONArray directors = obj.optJSONArray("director");
        JSONArray languages = obj.optJSONArray("language");
        JSONArray onlinetimes = obj.optJSONArray("onlinetime");
        JSONArray keywords = obj.optJSONArray("programsearchkey");
        JSONArray columncodes = obj.optJSONArray("columncode");
        JSONArray seriesprogramcodes = obj.optJSONArray("seriesprogramcode");
        JSONArray releasedates = obj.optJSONArray("releasedate");
        for (int i = 0; i < codes.length(); i++) {
            videos = new Video();
            videos.setCode(contentcodes.optString(i));
            videos.setProgramcode(codes.optString(i));
            videos.setTitle(titles.optString(i));
            videos.setImg_url(ImageUrlHelper.getImageUrl(images.optString(i)));
            videos.setType(types.optString(i));
            videos.setDescription(decs.optString(i));
            videos.setActor(actors.optString(i));
            videos.setDirector(directors.optString(i));
            videos.setLanguage(languages.optString(i));
            videos.setOnlinetime(onlinetimes.optString(i));
            videos.setKeywords(keywords.optString(i));
            videos.setColumncode(columncodes.optString(i));
            videos.setSeriesprogramcode(seriesprogramcodes.optString(i));
            videos.setYear(releasedates.optString(i));
            list.add(videos);
        }
        return list;
    }

    /**
     * @param obj
     * @param list
     * @return
     */
    public static ArrayList<Video> streamArray2Series(JSONObject obj, ArrayList<Video> list) {
        Video videos;
        JSONArray contentcodes = obj.optJSONArray("contentcode");
        JSONArray codes = obj.optJSONArray("programcode");
        JSONArray titles = obj.optJSONArray("programname");
        JSONArray images = obj.optJSONArray("poster4");
        JSONArray types = obj.optJSONArray("genre");
        JSONArray decs = obj.optJSONArray("detaildescribed");
        JSONArray actors = obj.optJSONArray("actor");
        JSONArray directors = obj.optJSONArray("director");
        JSONArray languages = obj.optJSONArray("language");
        JSONArray onlinetimes = obj.optJSONArray("onlinetime");
        JSONArray keywords = obj.optJSONArray("programsearchkey");
        JSONArray columncodes = obj.optJSONArray("columncode");
        JSONArray seriesprogramcodes = obj.optJSONArray("seriesprogramcode");
        JSONArray seriesnums = obj.optJSONArray("seriesnum");
        for (int i = 0; i < codes.length(); i++) {
            videos = new Video();
            videos.setCode(contentcodes.optString(i));
            videos.setProgramcode(codes.optString(i));
            videos.setTitle(titles.optString(i));
            videos.setImg_url(ImageUrlHelper.getImageUrl(images.optString(i)));
            videos.setType(types.optString(i));
            videos.setDescription(decs.optString(i));
            videos.setActor(actors.optString(i));
            videos.setDirector(directors.optString(i));
            videos.setLanguage(languages.optString(i));
            videos.setOnlinetime(onlinetimes.optString(i));
            videos.setKeywords(keywords.optString(i));
            videos.setColumncode(columncodes.optString(i));
            videos.setSeriesprogramcode(seriesprogramcodes.optString(i));
            videos.setIndex(seriesnums.optInt(i));
            list.add(videos);
        }
        return list;
    }

    /**
     * @param obj
     * @param list
     * @return
     */
    public static ArrayList<Video> programcode2VodInfo(JSONObject obj, ArrayList<Video> list) {
        Video videos;
        JSONArray contentcodes = obj.optJSONArray("contentcode");
        JSONArray codes = obj.optJSONArray("programcode");
        JSONArray titles = obj.optJSONArray("programname");
        JSONArray images = obj.optJSONArray("poster4");
        JSONArray types = obj.optJSONArray("genre");
        JSONArray decs = obj.optJSONArray("detaildescribed");
        JSONArray actors = obj.optJSONArray("actor");
        JSONArray directors = obj.optJSONArray("director");
        JSONArray languages = obj.optJSONArray("language");
        JSONArray onlinetimes = obj.optJSONArray("onlinetime");
        JSONArray keywords = obj.optJSONArray("programsearchkey");
        JSONArray columncodes = obj.optJSONArray("columncode");
        JSONArray seriesprogramcodes = obj.optJSONArray("seriesprogramcode");
        JSONArray seriesnums = obj.optJSONArray("seriesnum");
        JSONArray releasedates = obj.optJSONArray("releasedate");
        for (int i = 0; i < codes.length(); i++) {
            videos = new Video();
            videos.setCode(contentcodes.optString(i));
            videos.setProgramcode(codes.optString(i));
            videos.setTitle(titles.optString(i));
            videos.setImg_url(ImageUrlHelper.getImageUrl(images.optString(i)));
            videos.setType(types.optString(i));
            videos.setDescription(decs.optString(i));
            videos.setActor(actors.optString(i));
            videos.setDirector(directors.optString(i));
            videos.setLanguage(languages.optString(i));
            videos.setOnlinetime(onlinetimes.optString(i));
            videos.setKeywords(keywords.optString(i));
            videos.setColumncode(columncodes.optString(i));
            videos.setSeriesprogramcode(seriesprogramcodes.optString(i));
            videos.setIndex(seriesnums.optInt(i));
            videos.setYear(releasedates.optString(i));
            list.add(videos);
        }
        return list;
    }

    /**
     * @param obj
     * @param list
     * @return
     */
    public static ArrayList<Video> contentcode2VodInfo(JSONObject obj, ArrayList<Video> list) {
        Video videos;
        JSONArray contentcodes = obj.optJSONArray("contentcode");
        JSONArray codes = obj.optJSONArray("programcode");
        JSONArray titles = obj.optJSONArray("programname");
        JSONArray images = obj.optJSONArray("poster4");
        JSONArray types = obj.optJSONArray("genre");
        JSONArray decs = obj.optJSONArray("detaildescribed");
        JSONArray actors = obj.optJSONArray("actor");
        JSONArray directors = obj.optJSONArray("director");
        JSONArray languages = obj.optJSONArray("language");
        JSONArray onlinetimes = obj.optJSONArray("onlinetime");
        JSONArray keywords = obj.optJSONArray("programsearchkey");
        JSONArray columncodes = obj.optJSONArray("columncode");
        JSONArray seriesprogramcodes = obj.optJSONArray("seriesprogramcode");
        JSONArray seriesnums = obj.optJSONArray("seriesnum");
        for (int i = 0; i < codes.length(); i++) {
            videos = new Video();
            videos.setCode(contentcodes.optString(i));
            videos.setProgramcode(codes.optString(i));
            videos.setTitle(titles.optString(i));
            videos.setImg_url(ImageUrlHelper.getImageUrl(images.optString(i)));
            videos.setType(types.optString(i));
            videos.setDescription(decs.optString(i));
            videos.setActor(actors.optString(i));
            videos.setDirector(directors.optString(i));
            videos.setLanguage(languages.optString(i));
            videos.setOnlinetime(onlinetimes.optString(i));
            videos.setKeywords(keywords.optString(i));
            videos.setColumncode(columncodes.optString(i));
            videos.setSeriesprogramcode(seriesprogramcodes.optString(i));
            videos.setIndex(seriesnums.optInt(i));
            list.add(videos);
        }
        return list;
    }

    /**
     * @param obj
     * @param list
     * @return
     */
    public static ArrayList<Video> streamArray2Basic(JSONObject obj) {
        ArrayList<Video> list = new ArrayList<Video>();
        Video videos;
        JSONArray contentcodes = obj.optJSONArray("contentcode");
        JSONArray codes = obj.optJSONArray("programcode");
        JSONArray titles = obj.optJSONArray("programname");
        JSONArray images = obj.optJSONArray("poster4");
        JSONArray types = obj.optJSONArray("genre");
        JSONArray decs = obj.optJSONArray("detaildescribed");
        JSONArray actors = obj.optJSONArray("actor");
        JSONArray directors = obj.optJSONArray("director");
        JSONArray languages = obj.optJSONArray("language");
        JSONArray onlinetimes = obj.optJSONArray("onlinetime");
        JSONArray keywords = obj.optJSONArray("programsearchkey");
        JSONArray columncodes = obj.optJSONArray("columncode");
        JSONArray seriesprogramcodes = obj.optJSONArray("seriesprogramcode");
        for (int i = 0; i < codes.length(); i++) {
            videos = new Video();
            videos.setCode(contentcodes.optString(i));
            videos.setProgramcode(codes.optString(i));
            videos.setTitle(titles.optString(i));
            videos.setImg_url(ImageUrlHelper.getImageUrl(images.optString(i)));
            videos.setType(types.optString(i));
            videos.setDescription(decs.optString(i));
            videos.setActor(actors.optString(i));
            videos.setDirector(directors.optString(i));
            videos.setLanguage(languages.optString(i));
            videos.setOnlinetime(onlinetimes.optString(i));
            videos.setKeywords(keywords.optString(i));
            videos.setColumncode(columncodes.optString(i));
            videos.setSeriesprogramcode(seriesprogramcodes.optString(i));
            list.add(videos);
        }
        return list;
    }

}
