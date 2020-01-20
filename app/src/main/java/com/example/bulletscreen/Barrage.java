package com.example.bulletscreen;

public class Barrage {
    private String id;
    private String url;
    private long startOffsetTime;


    public Barrage(String id, String url, long startOffsetTime) {
        this.id = id;
        this.url = url;
        this.startOffsetTime = startOffsetTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getStartOffsetTime() {
        return startOffsetTime;
    }

    public void setStartOffsetTime(long startOffsetTime) {
        this.startOffsetTime = startOffsetTime;
    }
}
