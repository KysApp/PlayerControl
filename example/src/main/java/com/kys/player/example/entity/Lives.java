package com.kys.player.example.entity;

/**
 * Created by bsy on 2016/4/14.
 */
public class Lives extends BaseEntity {
    private String title, img_url, cid;

    public Lives() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public String[] getNodes() {
        return new String[]{"title", "media:content", "itv:cid"};
    }

    @Override
    public void setData(int pos, String data) {
    }
}
