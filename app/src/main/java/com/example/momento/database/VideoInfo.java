package com.example.momento.database;

import android.provider.MediaStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Video Class
 */
public class VideoInfo extends Object {

    public String url;
    private int playCount;

    // Constructors
    VideoInfo() {
        this.url = "";
        playCount = 0;
    }

    VideoInfo(String url) {
        this.url = url;
        playCount = 0;
    }

    // Deep Copy Constructor
    VideoInfo(VideoInfo that) {
        this.url = that.url;
        this.playCount = that.playCount;
    }

    public int getPlayCount() { return playCount; }
    public void incrementPlayCount() { playCount++; }
    public void resetPlayCount() { playCount = 0 ;}

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("url", this.url);
        result.put("playCount", this.playCount);
        return result;
    }

}
