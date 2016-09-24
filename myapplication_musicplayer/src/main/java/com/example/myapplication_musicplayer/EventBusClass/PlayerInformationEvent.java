package com.example.myapplication_musicplayer.EventBusClass;


/**
 * Created by wansh on 2016/9/17.
 * 一个封装信息的类
 */
public class PlayerInformationEvent {

    private String playerLite;
    private String playerArtist;
    private long playerTotalTime;
    private long playerTestTime;


    public PlayerInformationEvent(String Lite, String Artist, long TotalTime, long TestTime) {
        this.playerLite = Lite;
        this.playerArtist = Artist;
        this.playerTotalTime = TotalTime;
        this.playerTestTime = TestTime;
    }


    public String getPlayerLite() {
        return playerLite;
    }

    public long getPlayerTotalTime() {
        return playerTotalTime;
    }

    public String getPlayerArtist() {
        return playerArtist;
    }

    public long getPlayerTestTime() {
        return playerTestTime;
    }
}
