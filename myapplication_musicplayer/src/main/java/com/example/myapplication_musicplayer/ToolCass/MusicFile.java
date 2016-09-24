package com.example.myapplication_musicplayer.ToolCass;

/**
 * Created by wansh on 2016/9/7.
 * 一个音乐文件的内容属性类
 */
public class MusicFile {

    private long Id;
    private String Title;          // 音乐标题
    private String Artist;        // 艺术家
    private long Duration;       // 播放时长
    private long Size;          // 音乐大小
    private String FileUrl;    // 音乐路径

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public long getDuration() {
        return Duration;
    }

    public void setDuration(long duration) {
        Duration = duration;
    }

    public long getSize() {
        return Size;
    }

    public void setSize(long size) {
        Size = size;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        FileUrl = fileUrl;
    }

}
