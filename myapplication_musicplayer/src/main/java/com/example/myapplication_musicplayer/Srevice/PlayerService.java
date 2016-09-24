package com.example.myapplication_musicplayer.Srevice;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.myapplication_musicplayer.ToolCass.MediaUtil;
import com.example.myapplication_musicplayer.ToolCass.MusicFile;
import com.example.myapplication_musicplayer.EventBusClass.PlayerFunctionEvent;
import com.example.myapplication_musicplayer.EventBusClass.PlayerInformationEvent;
import com.example.myapplication_musicplayer.EventBusClass.PlayerTimeEvent;
import com.example.myapplication_musicplayer.R;

import java.util.List;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


/**
 * Created by wansh on 2016/9/16.
 * 后台服务播放
 */
public class PlayerService extends Service {


    private boolean isOpenService;      //是否开启了服务
    private List<MusicFile> musicFiles;   //存放Mp3Info对象的集合
    private MusicFile musicFile;          //音乐文件
    private MediaPlayer mediaPlayer; // 媒体播放器对象
    private int position;            // 音乐文件位置

    private boolean isPlay;         //播放状态
    private boolean isNext;      //是否为下一曲
    private boolean isMove;      //是否快进和快退
    private int status;         //播放状态，默认为全部播放

    private int erro;        //记录错误位置


    @Override
    public void onCreate() {
        super.onCreate();

        //注册EventBus
        EventBus.getDefault().register(this);

        //创建播放器
        mediaPlayer = MediaPlayer.create(this, R.raw.rc1);


        //获取系统音乐
        musicFiles = MediaUtil.getMusicFile(this);
        isOpenService = true;

        //播放完毕的事件监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isNext = true;
                PlayModel();
            }
        });



        //快进快退完毕的事件监听
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                isMove = false;
            }
        });

        //播放错误的事件监听
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                erro = mediaPlayer.getCurrentPosition();
                stop();
                play(erro);
                return true;
            }
        });

        //创建线程更新主界面UI
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                while (isOpenService) {
                    if (isPlay && !isMove) {
                        int CurrentPosition = mediaPlayer.getCurrentPosition();
                        if (CurrentPosition <= mediaPlayer.getDuration() - 100) {
                            try {

                                EventBus.getDefault().post(new PlayerTimeEvent(CurrentPosition));

                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {   //服务一旦重启就设置退出
            isOpenService = false;
            Toast.makeText(this, "内存不足，程序服务退出", Toast.LENGTH_SHORT).show();

        } else {
            position = intent.getIntExtra("position", 0);
            musicFile = musicFiles.get(position);  //获取对应音乐
            play(0);
            Init();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    //发送音乐信息
    private void Init() {

        String playerLite = musicFile.getTitle();
        String playerArtist = musicFile.getArtist();
        long playerTotalTime = musicFile.getDuration();
        long playerTestTime = mediaPlayer.getCurrentPosition();
        EventBus.getDefault().post(new PlayerInformationEvent(playerLite, playerArtist, playerTotalTime, playerTestTime));
    }


    /*
     * 播放音乐
     */
    private void play(int currentTime) {
        try {
            mediaPlayer.reset();// 把各项参数恢复到初始状态
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(musicFile.getFileUrl());
            mediaPlayer.prepare(); // 进行缓冲
            mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));// 注册一个监听器

        } catch (Exception e) {
            e.printStackTrace();
        }
        isPlay = true;
    }

    /*
     * 暂停音乐
     */
    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlay = false;
        }
    }

    /*
     * 继续播放
     */
    private void resume() {
        if (!isPlay) {
            mediaPlayer.start();
            isPlay = true;
        }
    }

    /*
     * 上一首
     */
    private void previous() {
        stop();
        isNext = false;
        PlayModel();
    }

    /*
     * 下一首
     */
    private void next() {
        stop();
        isNext = true;
        PlayModel();

    }


    /*
    * 停止音乐
    */
    private void stop() {
        if (mediaPlayer != null) {
            isPlay = false;
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 播放模式
     */
    private void PlayModel() {


        switch (status) {

            // 全部循环
            case 0: {

                if (isNext) {
                    position++;
                    if (position == musicFiles.size()) {  //变为第一首的位置继续播放
                        position = 0;
                    }
                } else {
                    position--;
                    if (position == -1) {  //变为第一首的位置继续播放
                        position = musicFiles.size() - 1;
                    }
                }


            }
            break;

            //列表循环
            case 1: {

                if (isNext) {
                    position++;
                    if (position == musicFiles.size()) {   //变为第一首的位置继续播放
                        position = musicFiles.size() - 1;
                    }
                } else {
                    position--;
                    if (position == -1) {  //变为第一首的位置继续播放
                        position = 0;
                    }
                }

            }
            break;

            //随机播放
            case 2: {
                position = getRandomIndex(musicFiles.size() - 1);
            }
            break;

            // 单曲循环
            case 3: {
            }
            break;
        }

        musicFile = musicFiles.get(position);

        Init();
        EventBus.getDefault().post(new PlayerTimeEvent(0));
        play(0);
    }


    //获取随机位置
    protected int getRandomIndex(int end) {
        return (int) (Math.random() * end);
    }


    /*
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     */
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {

        private int currentTime;

        public PreparedListener(int currentTime) {
            this.currentTime = currentTime;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {

            if (currentTime > 0) { // 如果音乐不是从头播放
                mediaPlayer.seekTo(currentTime);
            }

            mediaPlayer.start(); // 开始播放
        }
    }


    //接收消息
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void PlayerFunction(PlayerFunctionEvent event) {
        switch (event.getMsg()) {
            case -1: {
                Init();
            }
            break;
            case 0:
                status = 0;
                break;
            case 1:
                status = 1;
                break;
            case 2:
                status = 2;
                break;
            case 3:
                status = 3;
                break;
            case 10:
                pause();
                break;
            case 11:
                resume();
                break;
            case 12:
                previous();
                break;
            case 13:
                next();
                break;
            case 20:
                isMove = true;
                break;
            case 21:
                mediaPlayer.seekTo(event.getProgress());

                break;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            isOpenService = false;
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

        }
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
