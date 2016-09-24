package com.example.myapplication_musicplayer.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication_musicplayer.ToolCass.MediaUtil;
import com.example.myapplication_musicplayer.EventBusClass.PlayerFunctionEvent;
import com.example.myapplication_musicplayer.EventBusClass.PlayerInformationEvent;
import com.example.myapplication_musicplayer.EventBusClass.PlayerTimeEvent;
import com.example.myapplication_musicplayer.R;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


/**
 * Created by wansh on 2016/9/13.
 * 用于播放音乐的界面
 */
public class PlayerActivity extends Activity implements View.OnClickListener {


    private ImageView playerActivityBack;
    private TextView playerActivityTestLite;
    private TextView playerActivityTestArtist;
    private ImageView playerActivityIcnLove;
    private ImageView playerActivityIcnDid;
    private TextView playerActivityTestTime;
    private SeekBar playerActivitySeeBarTime;
    private TextView playerActivityTestTotalTime;
    private ImageView playerActivityImgLoop;
    private Button playerActivityBtnPrev;
    private Button playerActivityBtnPlay;
    private Button playerActivityBtnNext;
    private ImageView playerActivityImgSrc;

    private int repeatState;        //循环标识
    private Toast mToast;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        //设置状态栏透明实现沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.player_activity_main);

        //注册EventBus
        EventBus.getDefault().register(PlayerActivity.this);


        Init();
        playMusic();
    }

    private void Init() {

        playerActivityBack = (ImageView) findViewById(R.id.player_activity_back);
        playerActivityTestLite = (TextView) findViewById(R.id.player_activity_test_lite);
        playerActivityTestArtist = (TextView) findViewById(R.id.player_activity_test_artist);
        playerActivityIcnLove = (ImageView) findViewById(R.id.player_activity_icn_love);
        playerActivityIcnDid = (ImageView) findViewById(R.id.player_activity_icn_did);
        playerActivityTestTime = (TextView) findViewById(R.id.player_activity_test_time);
        playerActivitySeeBarTime = (SeekBar) findViewById(R.id.player_activity_seeBar_time);
        playerActivityTestTotalTime = (TextView) findViewById(R.id.player_activity_test_totalTime);

        playerActivityImgLoop = (ImageView) findViewById(R.id.player_activity_img_loop);
        playerActivityBtnPrev = (Button) findViewById(R.id.player_activity_btn_prev);
        playerActivityBtnPlay = (Button) findViewById(R.id.player_activity_btn_play);
        playerActivityBtnNext = (Button) findViewById(R.id.player_activity_btn_next);
        playerActivityImgSrc = (ImageView) findViewById(R.id.player_activity_img_src);

        playerActivityBack.setOnClickListener(this);

        playerActivityImgLoop.setOnClickListener(this);
        playerActivityBtnPrev.setOnClickListener(this);
        playerActivityBtnPlay.setOnClickListener(this);
        playerActivityBtnNext.setOnClickListener(this);
        playerActivityImgSrc.setOnClickListener(this);


        playerActivitySeeBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                playerActivityTestTime.setText(MediaUtil.formatTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                EventBus.getDefault().post(new PlayerFunctionEvent(20));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EventBus.getDefault().post(new PlayerFunctionEvent(21, seekBar.getProgress()));
            }
        });
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }


    // 播放音乐 显示相关信息

    public void playMusic() {


        repeatState = MainActivity.preferences.getInt("repeatState", 0);


        if (!MainActivity.isPlay) {
            playerActivityBtnPlay.setBackgroundResource(R.drawable.player_activity_btn_play);
        }
        switch (repeatState) {
            case 0:
                playerActivityImgLoop.setImageResource(R.drawable.play_icn_loop);
                break;
            case 1:
                playerActivityImgLoop.setImageResource(R.drawable.play_icn_order);

                break;
            case 2:
                playerActivityImgLoop.setImageResource(R.drawable.play_icn_shuffle);
                break;
            case 3:
                playerActivityImgLoop.setImageResource(R.drawable.play_icn_one);
                break;
        }
        EventBus.getDefault().post(new PlayerFunctionEvent(-1));


    }


    //暂停音乐
    private void pauseMusic() {

        EventBus.getDefault().post(new PlayerFunctionEvent(10));

    }


    // 继续播放音乐
    private void continueMusic() {
        EventBus.getDefault().post(new PlayerFunctionEvent(11));
    }


    // 播放上一首音乐
    private void prevMusic() {
        EventBus.getDefault().post(new PlayerFunctionEvent(12));
    }


    // 播放下一首一首音乐
    private void nextMusic() {
        EventBus.getDefault().post(new PlayerFunctionEvent(13));
    }

    //显示音乐信息
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void PlayerInformationEvent(PlayerInformationEvent event) {


        playerActivityTestLite.setText(event.getPlayerLite());
        playerActivityTestArtist.setText(event.getPlayerArtist());
        playerActivityTestTotalTime.setText(MediaUtil.formatTime(event.getPlayerTotalTime()));
        playerActivitySeeBarTime.setMax((int) event.getPlayerTotalTime());
        playerActivitySeeBarTime.setProgress((int) event.getPlayerTestTime());

    }

    //显示音乐播放时间
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void PlayerTimeEvent(PlayerTimeEvent event) {
        playerActivitySeeBarTime.setProgress(event.getTime());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_activity_back: {  //退出
                onBackPressed();
            }
            break;
            //上一首音乐
            case R.id.player_activity_btn_prev: {


                if (!MainActivity.isPlay) {
                    playerActivityBtnPlay.setBackgroundResource(R.drawable.player_activity_btn_pause);
                }

                prevMusic();

            }
            break;

            //（播放/暂停）音乐
            case R.id.player_activity_btn_play: {

                if (MainActivity.isPlay) {          //暂停

                    playerActivityBtnPlay.setBackgroundResource(R.drawable.player_activity_btn_play);
                    MainActivity.isPlay = false;
                    pauseMusic();

                } else {                         //继续播放

                    playerActivityBtnPlay.setBackgroundResource(R.drawable.player_activity_btn_pause);
                    MainActivity.isPlay = true;
                    continueMusic();

                }

            }
            break;

            //下一首音乐
            case R.id.player_activity_btn_next: {

                if (!MainActivity.isPlay) {
                    playerActivityBtnPlay.setBackgroundResource(R.drawable.player_activity_btn_pause);
                }

                nextMusic();

            }
            break;

            //播放模式
            case R.id.player_activity_img_loop: {


                switch (repeatState) {

                    //全部循环 ——> 列表循环
                    case 0: {
                        repeatState = 1;

                        mToast.setText("列表循环");
                        mToast.show();

                        playerActivityImgLoop.setImageResource(R.drawable.play_icn_order);

                    }
                    break;

                    //列表循环 ——> 随机播放
                    case 1: {
                        repeatState = 2;

                        mToast.setText("随机播放");
                        mToast.show();

                        playerActivityImgLoop.setImageResource(R.drawable.play_icn_shuffle);
                    }
                    break;

                    //随机播放 ——> 单曲循环
                    case 2: {
                        repeatState = 3;

                        mToast.setText("单曲循环");
                        mToast.show();

                        playerActivityImgLoop.setImageResource(R.drawable.play_icn_one);

                    }
                    break;

                    //单曲循环 ——> 全部循环
                    case 3: {
                        repeatState = 0;

                        mToast.setText("全部循环");
                        mToast.show();


                        playerActivityImgLoop.setImageResource(R.drawable.play_icn_loop);
                    }
                    break;
                }

                EventBus.getDefault().post(new PlayerFunctionEvent(repeatState));

            }
            break;

            //音乐列表
            case R.id.player_activity_img_src: {


            }
            break;
        }
    }


    @Override
    public void onBackPressed() {
        MainActivity.editor.putInt("repeatState", repeatState).commit();
        super.onBackPressed();
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


