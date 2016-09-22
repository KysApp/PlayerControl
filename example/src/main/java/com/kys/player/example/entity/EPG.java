package com.kys.player.example.entity;

/**
 * Created by bsy on 2016/4/19.
 */
public class EPG extends BaseEntity{
    private String title,cid,start_time,end_time,run_time,status;
    private Boolean isplaying=false,is_now_epg=false;


    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public EPG(){

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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getRun_time() {
        return run_time;
    }

    public void setRun_time(String run_time) {
        this.run_time = run_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Boolean getIsplaying() {
        return isplaying;
    }

    public void setIsplaying(Boolean isplaying) {
        this.isplaying = isplaying;
    }

    public Boolean getIs_now_epg() {
        return is_now_epg;
    }

    public void setIs_now_epg(Boolean is_now_epg) {
        this.is_now_epg = is_now_epg;
    }

    @Override
    public String[] getNodes() {
        return new String[0];
    }

    @Override
    public void setData(int pos, String data) {

    }
}
