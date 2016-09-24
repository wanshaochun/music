package com.example.myapplication_musicplayer.Activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication_musicplayer.Framgment.MainFragment_discover;
import com.example.myapplication_musicplayer.Framgment.MainFragment_friends;
import com.example.myapplication_musicplayer.Framgment.MainFragment_music;
import com.example.myapplication_musicplayer.ToolCass.MediaUtil;
import com.example.myapplication_musicplayer.Framgment.MusicFragment_local;
import com.example.myapplication_musicplayer.EventBusClass.PlayerFunctionEvent;
import com.example.myapplication_musicplayer.EventBusClass.PlayerInformationEvent;
import com.example.myapplication_musicplayer.EventBusClass.PlayerTimeEvent;
import com.example.myapplication_musicplayer.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class MainActivity extends FragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, MainFragment_music.setDrawerLayout, MusicFragment_local.setDrawerLayout, MusicFragment_local.setPlayer {

    public static final String path = "http://192.168.98.6/";   // 服务器的地址
    public static boolean isNetworkConnected;                  //判断网络是否连接
    public static String ContentFile = "MyMusic/DiscoverOne/ListContent/";
    public static SharedPreferences preferences;     //数据存储
    public static SharedPreferences.Editor editor;
    public static JSONArray contentJSON;            //网路json数组

    public static boolean isPlay;                     //是否在播放

    private NetworkChangeReceiver networkChangeReceiver;        //监听网络广播

    private DrawerLayout mainDrawerRoot;
    private LinearLayout mainContent;
    private LinearLayout mainMusicPlayer;


    private ViewPager mainViewPager;

    private final int[] ints_RadioButton = {R.id.main_toolBar__btnDiscover, R.id.main_toolBar__btnMusic, R.id.main_toolBar__btnFriends};
    private RadioButton[] mainToolBarRadioButton = new RadioButton[ints_RadioButton.length];

    private ListView mainListViewRight;

    private List<Fragment> fragmentsList; // 加载Fragment

    private TextView mainMusicPlayerTitle;            // 播放条的音乐标题
    private TextView mainMusicPlayerArtist;           //  播放条的作者
    private ImageView mainMusicPlayerPlaylist;        //播放条的菜单栏
    private ImageView mainMusicPlayerPlay;            //播放条的播放按钮
    private ImageView mainMusicPlayerNext;            //播放条的下一首按钮
    private ProgressBar mainMusicPlayerProgress;       //播放条的进度条


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置状态栏透明实现沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        //注册广播 和 EventBus 和 存储

        preferences = getSharedPreferences("PlayerActivityState", MODE_PRIVATE);
        editor = preferences.edit();

        EventBus.getDefault().register(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

        setContentView(R.layout.activity_main);

        Init();

    }

    private void Init() {

        //获取控件ID并设置事件监听

        mainDrawerRoot = (DrawerLayout) findViewById(R.id.main_drawer_root);
        mainContent = (LinearLayout) findViewById(R.id.main_content);
        mainMusicPlayer = (LinearLayout) findViewById(R.id.main_music_player);
        ImageView mainToolBarImage = (ImageView) findViewById(R.id.main_toolBar_image_menu);
        RadioGroup mainToolBarRadio = (RadioGroup) findViewById(R.id.main_toolBar__radio);
        for (int i = 0; i < ints_RadioButton.length; i++) {
            mainToolBarRadioButton[i] = (RadioButton) findViewById(ints_RadioButton[i]);
        }

        ImageButton mainToolBarBtn = (ImageButton) findViewById(R.id.main_toolBar__btn);

        mainViewPager = (ViewPager) findViewById(R.id.main_viewPager);

        mainListViewRight = (ListView) findViewById(R.id.main_listView_right);
        Button mainBtnBack = (Button) findViewById(R.id.main_btn_back);


        //播放条的控件
        mainMusicPlayerTitle = (TextView) findViewById(R.id.main_music_player_title);
        mainMusicPlayerArtist = (TextView) findViewById(R.id.main_music_player_artist);
        mainMusicPlayerPlaylist = (ImageView) findViewById(R.id.main_music_player_playlist);
        mainMusicPlayerPlay = (ImageView) findViewById(R.id.main_music_player_play);
        mainMusicPlayerNext = (ImageView) findViewById(R.id.main_music_player_next);
        mainMusicPlayerProgress = (ProgressBar) findViewById(R.id.main_music_player_progress);

        mainDrawerRoot.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mainListViewRight.setSelection(0);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        mainMusicPlayer.setOnClickListener(this);
        mainToolBarRadio.setOnCheckedChangeListener(this);
        mainToolBarImage.setOnClickListener(this);
        mainBtnBack.setOnClickListener(this);
        mainToolBarBtn.setOnClickListener(this);

        //播放条的事件监听
        mainMusicPlayerPlaylist.setOnClickListener(this);
        mainMusicPlayerPlay.setOnClickListener(this);
        mainMusicPlayerNext.setOnClickListener(this);

        setMainViewPager();
        setMainListViewRight();
        //mainViewPager.setOffscreenPageLimit(2);

    }

    //填充ViewPager
    private void setMainViewPager() {

        fragmentsList = new ArrayList<>();

        MainFragment_discover fragment_discover = new MainFragment_discover();
        MainFragment_music fragment_music = new MainFragment_music();
        MainFragment_friends fragment_friends = new MainFragment_friends();
        fragmentsList.add(fragment_discover);
        fragmentsList.add(fragment_music);
        fragmentsList.add(fragment_friends);

        //设置适配器
        FragmentStatePagerAdapter myAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentsList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentsList.size();
            }
        };

        //设置监听事件
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mainToolBarRadioButton[position].setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mainViewPager.setAdapter(myAdapter);
        mainViewPager.setOffscreenPageLimit(2);
    }

    //填充菜单ListView
    private void setMainListViewRight() {


        final int[] img_ints = {
                R.drawable.topmenu_icn_msg, R.drawable.topmenu_icn_store, R.drawable.topmenu_icn_vip,
                R.drawable.topmenu_icn_free, R.drawable.topmenu_icn_identify, R.drawable.topmenu_icn_skin,
                R.drawable.topmenu_icn_night, R.drawable.topmenu_icn_time, R.drawable.topmenu_icn_clock,
                R.drawable.topmenu_icn_vehicle, R.drawable.topmenu_icn_cloud
        };
        final String[] BeforeText_strings = {
                "我的消息", "积分商城", "会员中心",
                "在线听歌免流量", "听歌识曲", "主题换肤",
                "夜间模式", "定时停止播放", "音乐闹钟",
                "驾驶模式", "我的音乐盘"
        };

        mainListViewRight.addHeaderView(View.inflate(this, R.layout.main_listview_right_head, null));

        BaseAdapter myAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return img_ints.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHome viewHome;
                if (convertView == null) {
                    viewHome = new ViewHome();
                    convertView = View.inflate(MainActivity.this, R.layout.main_listview_right_style, null);
                    viewHome.imageView = (ImageView) convertView.findViewById(R.id.MyImageView_list);
                    viewHome.BeforeTextView = (TextView) convertView.findViewById(R.id.MyBeforeTextView_list);
                    viewHome.BehindTextView = (TextView) convertView.findViewById(R.id.MyBehindTextView_list);
                    viewHome.aSwitch = (Switch) convertView.findViewById(R.id.MySwitch);
                    viewHome.DividerTextView = (TextView) convertView.findViewById(R.id.MyText_list_divider);
                    convertView.setTag(viewHome);
                } else {
                    viewHome = (ViewHome) convertView.getTag();
                }

                viewHome.imageView.setImageResource(img_ints[position]);
                viewHome.BeforeTextView.setText(BeforeText_strings[position]);
                if (position == 1) {

                    viewHome.BehindTextView.setVisibility(View.VISIBLE);
                    viewHome.BehindTextView.setText("90积分  ");

                } else if (position == 3) {
                    viewHome.DividerTextView.setVisibility(View.VISIBLE);
                } else if (position == 5) {

                    viewHome.BehindTextView.setVisibility(View.VISIBLE);
                    viewHome.BehindTextView.setText("官方红  ");

                } else if (position == 6) {

                    viewHome.aSwitch.setVisibility(View.VISIBLE);
                }
                return convertView;
            }

            class ViewHome {

                ImageView imageView;
                TextView BeforeTextView;
                TextView BehindTextView;
                Switch aSwitch;
                TextView DividerTextView;
            }
        };

        mainListViewRight.setAdapter(myAdapter);
    }


    //单选事件
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.main_toolBar__btnDiscover:
                mainViewPager.setCurrentItem(0);
                break;
            case R.id.main_toolBar__btnMusic:
                mainViewPager.setCurrentItem(1);
                break;
            case R.id.main_toolBar__btnFriends:
                mainViewPager.setCurrentItem(2);
                break;

        }
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_toolBar_image_menu: {    //打开抽屉
                mainDrawerRoot.openDrawer(Gravity.LEFT);
            }
            break;

            case R.id.main_btn_back: {    //退出程序


                //关闭服务
                Intent intent = new Intent("SERVICE_STOP");
                intent.setPackage("com.example.myapplication_musicplayer");
                stopService(intent);

                finish();
            }
            break;

            case R.id.main_music_player: {  //打开播放界面

                startActivityForResult(new Intent(MainActivity.this, PlayerActivity.class), 1);
            }
            break;
            case R.id.main_music_player_playlist: {
                Intent intent = new Intent("SERVICE_STOP");
                intent.setPackage("com.example.myapplication_musicplayer");
                stopService(intent);
                mainMusicPlayer.setVisibility(View.GONE);
            }
            break;
            case R.id.main_music_player_play: {
                if (isPlay) {
                    mainMusicPlayerPlay.setImageResource(R.drawable.playbar_btn_play);
                    EventBus.getDefault().post(new PlayerFunctionEvent(10));
                    isPlay = false;
                } else {
                    mainMusicPlayerPlay.setImageResource(R.drawable.playbar_btn_pause);
                    EventBus.getDefault().post(new PlayerFunctionEvent(11));
                    isPlay = true;
                }
            }
            break;
            case R.id.main_music_player_next: {
                EventBus.getDefault().post(new PlayerFunctionEvent(13));
            }
            break;


        }
    }

    //退出键
    @Override
    public void onBackPressed() {
        if (mainDrawerRoot.isDrawerOpen(Gravity.LEFT)) {
            mainDrawerRoot.closeDrawers();
        } else {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }


    @Override
    public void setDrawerVisibilityGONE() {

        mainDrawerRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动

        mainContent.setVisibility(View.GONE);

    }

    @Override
    public void setDrawerVisibilityVISIBLE() {
        mainDrawerRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //打开手势滑动
        mainContent.setVisibility(View.VISIBLE);
    }


    @Override
    public void setPlay() {
        mainMusicPlayer.setVisibility(View.VISIBLE);
        if (isPlay) {
            mainMusicPlayerPlay.setImageResource(R.drawable.playbar_btn_pause);
        } else {
            mainMusicPlayerPlay.setImageResource(R.drawable.playbar_btn_play);
        }
    }

    //显示音乐信息
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void PlayerInformationEvent(PlayerInformationEvent event) {

        mainMusicPlayerTitle.setText(event.getPlayerLite());
        mainMusicPlayerArtist.setText(event.getPlayerArtist());
        mainMusicPlayerProgress.setMax((int) event.getPlayerTotalTime());
    }

    //显示音乐播放时间
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void PlayerTimeEvent(PlayerTimeEvent event) {
        mainMusicPlayerProgress.setProgress(event.getTime());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(networkChangeReceiver);

    }


    //广播接收器  时时接收网络变化
    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                isNetworkConnected = true;
                if (contentJSON == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                contentJSON = new JSONArray(MediaUtil.readParse(MainActivity.path + ContentFile + "content.json"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

            } else {
                isNetworkConnected = false;
                Toast.makeText(MainActivity.this, "网络已断开", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
