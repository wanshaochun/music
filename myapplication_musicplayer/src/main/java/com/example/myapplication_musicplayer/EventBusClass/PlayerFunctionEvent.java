package com.example.myapplication_musicplayer.EventBusClass;

/**
 * Created by wansh on 2016/9/17.
 * 用于控制服务的类
 */
public class PlayerFunctionEvent {
    private int msg;
    private int progress;

    public PlayerFunctionEvent(int msg) {
        this.msg = msg;
    }

    public PlayerFunctionEvent(int msg, int progress) {
        this(msg);
        this.progress = progress;
    }

    public int getMsg() {
        return msg;
    }

    public int getProgress() {
        return progress;
    }
}
