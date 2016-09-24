package com.example.myapplication_musicplayer.ToolCass;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wansh on 2016/9/7.
 * 一个或许系统资源，转换时间的工具类
 */
public class MediaUtil {

    /*
     * 获取系统中的音乐文件
     * 返回一个ArrayList<MusicFile>集合
     */
    public static List<MusicFile> getMusicFile(Context context) {

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<MusicFile> musicFiles = new ArrayList<>();

        while (cursor.moveToNext()) {

            MusicFile musicFile = new MusicFile();

            //只把音乐添加到集合当中
            if (cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)) != 0) {               //是否为音乐

                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));                //音乐id
                String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));     //音乐标题
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));     //艺术家
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));     //时长
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));             //文件大小
                String fileUrl = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));      //文件路径

                if (duration > 30000) {
                    musicFile.setId(id);
                    musicFile.setTitle(title);
                    musicFile.setArtist(artist);
                    musicFile.setDuration(duration);
                    musicFile.setSize(size);
                    musicFile.setFileUrl(fileUrl);
                    musicFiles.add(musicFile);
                }
            }
        }
        return musicFiles;
    }


    /*
     * 获取网络中的音乐文件
     * 返回一个ArrayList<MusicFile>集合
     */
    public static List<MusicFile> getNetMusicFile(final String url) throws Exception {
        List<MusicFile> musicFiles = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(MediaUtil.readParse(url));

        return musicFiles;
    }

    /*
     * 格式化时间，将毫秒转换为分:秒格式
     */
    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }


    /*
     * 往List集合中添加Map对象数据，每一个Map对象存放一首音乐的所有属性
     * @param getMusicFileMaps
     * @return
     */
    public static List<HashMap<String, String>> getMusicFileMaps(List<MusicFile> musicFiles) {
        List<HashMap<String, String>> musicList = new ArrayList<>();
        for (Iterator iterator = musicFiles.iterator(); iterator.hasNext(); ) {
            MusicFile musicFile = (MusicFile) iterator.next();
            HashMap<String, String> map = new HashMap<>();
            map.put("title", musicFile.getTitle());
            map.put("artist", musicFile.getArtist());
            map.put("duration", formatTime(musicFile.getDuration()));
            map.put("size", String.valueOf(musicFile.getSize()));
            map.put("url", musicFile.getFileUrl());
            musicList.add(map);
        }
        return musicList;
    }

    /**
     * 从指定的URL中获取数组
     * 在线获取服务端的Json数据
     *
     * @throws Exception
     */
    public static String readParse(String urlPath) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len;
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inStream = conn.getInputStream();
        while ((len = inStream.read(data)) != -1) {
            outStream.write(data, 0, len);
        }
        inStream.close();
        return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据
    }


}
