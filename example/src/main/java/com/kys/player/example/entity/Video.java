package com.kys.player.example.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bsy on 2016/4/13.
 */
public class Video extends BaseEntity implements Parcelable {

    private String id;//视频id
    private String title;//视频名称
    private String cid;
    private String img_url;//视频图片
    private String description;//视频简介
    private String keywords;//关键词
    private String rating;//星级
    private String price;//订购价格
    private String actor;//主演
    private String director;//导演
    private String code;//内容code
    private String year;//上映时间
    private String area;//地区
    private String type;//类型
    private String onlinetime;//上线时间
    private String language;//语言
    private String programcode;//节目code
    private String seriesprogramcode;//连续剧剧头code
    private String columncode;//栏目code
    private int index;//剧集
    private String breakpoint;//断点时间
    private String favoritetype;//收藏类型

    public String getFavoritetype() {
        return favoritetype;
    }

    public void setFavoritetype(String favoritetype) {
        this.favoritetype = favoritetype;
    }

    public String getBreakpoint() {
        return breakpoint;
    }

    public void setBreakpoint(String breakpoint) {
        this.breakpoint = breakpoint;
    }

    public Video(){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOnlinetime() {
        return onlinetime;
    }

    public void setOnlinetime(String onlinetime) {
        this.onlinetime = onlinetime;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getColumncode() {
        return columncode;
    }

    public void setColumncode(String columncode) {
        this.columncode = columncode;
    }

    public String getProgramcode() {
        return programcode;
    }

    public void setProgramcode(String programcode) {
        this.programcode = programcode;
    }

    public String getSeriesprogramcode() {
        return seriesprogramcode;
    }

    public void setSeriesprogramcode(String seriesprogramcode) {
        this.seriesprogramcode = seriesprogramcode;
    }

    //存入数据
    public  void setData(int pos,String data){
        switch (pos){
            case 0:
                setId(data);
                break;
            case 1:
                setTitle(data);
                break;
            case 2:
                setCid(data);
                break;
            case 3:
                setImg_url(data);
                break;
            case 4:
                setDescription(data);
                break;
            case 5:
                setKeywords(data);
                break;
            case 6:
                setRating(data);
                break;
            case 7:
                setPrice(data);
                break;
            case 8:
                setActor(data);
                break;
            case 9:
                setDirector(data);
                break;
            case 10:
                setCode(data);
                break;
            case 11:
                setYear(data);
                break;
            case 12:
                setArea(data);
                break;
            case 13:
                setType(data);
                break;
            case 14:
                setOnlinetime(data);
                break;
            case 15:
                setLanguage(data);
                break;
            case 16:
                setProgramcode(data);
                break;
            case 17:
                setSeriesprogramcode(data);
                break;
            case 18:
                setColumncode(data);
                break;
        }
    }
    @Override
    public String[] getNodes() {
        return new String[]{"id","media:title","itv:cid","media:content","media:description","media:keywords","media:rating","media:price","actor","director","itv:code"};
    }

    @Override
    public String toString() {
        return "id="+getId()
                +"title=" + getTitle()
                +"cid=" + getCid()
                +"img_url=" + getImg_url()
                +"description=" + getDescription()
                +"keywords=" + getKeywords()
                +"rating=" + getRating()
                +"price=" + getPrice()
                +"actor=" + getActor()
                +"director=" + getDirector()
                +"code=" + getCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(cid);
        parcel.writeString(img_url);
        parcel.writeString(description);
        parcel.writeString(keywords);
        parcel.writeString(rating);
        parcel.writeString(price);
        parcel.writeString(actor);
        parcel.writeString(director);
        parcel.writeString(code);
        parcel.writeString(year);
        parcel.writeString(area);
        parcel.writeString(type);
        parcel.writeString(onlinetime);
        parcel.writeString(language);
        parcel.writeInt(index);
        parcel.writeString(programcode);
        parcel.writeString(seriesprogramcode);
        parcel.writeString(columncode);

    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            Video mMember = new Video();
            mMember.id = source.readString();
            mMember.title = source.readString();
            mMember.cid = source.readString();
            mMember.img_url = source.readString();
            mMember.description = source.readString();
            mMember.keywords = source.readString();
            mMember.rating = source.readString();
            mMember.price = source.readString();
            mMember.actor = source.readString();
            mMember.director = source.readString();
            mMember.code = source.readString();
            mMember.year = source.readString();
            mMember.area = source.readString();
            mMember.type = source.readString();
            mMember.onlinetime = source.readString();
            mMember.language = source.readString();
            mMember.index = source.readInt();
            mMember.programcode = source.readString();
            mMember.seriesprogramcode = source.readString();
            mMember.columncode = source.readString();
            return mMember;
        }
        @Override
        public Video[] newArray ( int size){
            // TODO Auto-generated method stub
            return new Video[size];
        }
    };
}
