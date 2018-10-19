package com.cooloongwu.helper;

/**
 * Created by CooLoongWu on 2018-10-19 14:05.
 */
public class MainBean {
    private String title;
    private String content;
    private String time;
    private Class clazz;

    public MainBean(String title,
                    String content,
                    String time,
                    Class clazz) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.clazz = clazz;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
