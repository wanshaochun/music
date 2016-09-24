package com.example.myapplication_musicplayer.EventBusClass;

/**
 * Created by wansh on 2016/9/18.
 * 用于显示播放时间的
 */
public class PlayerTimeEvent {
    private int time;

    public PlayerTimeEvent(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
