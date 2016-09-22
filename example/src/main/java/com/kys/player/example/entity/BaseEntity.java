package com.kys.player.example.entity;

/**
 * Created by bsy on 2016/4/13.
 * 实体类基类
 */
public abstract class BaseEntity {
    //声明要解析的节点名称
    public abstract String[] getNodes();

    //存入数据
    public abstract void setData(int pos, String data);

}
