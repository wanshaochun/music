package com.example.myapplication_musicplayer.Framgment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.myapplication_musicplayer.Activity.MainActivity;
import com.example.myapplication_musicplayer.Activity.MainActivity_Copyright;
import com.example.myapplication_musicplayer.Activity.MainActivity_Web;
import com.example.myapplication_musicplayer.ToolCass.FlipImageView;
import com.example.myapplication_musicplayer.ToolCass.LruImageCache;
import com.example.myapplication_musicplayer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wansh on 2016/9/11.
 * 个性推荐子模块
 */
public class MainFragmentDiscover_one extends Fragment implements View.OnClickListener {

    //主页面ListView
    private ListView mainFragmentMusicDiscoverOneList;

    //Head布局控件
    private ViewPager mainFragmentMusicDiscoverOneListHeadViewPager;
    private FlipImageView mainFragmentMusicDiscoverOneListHeadRecommendFM;


    private ImageHandler imgHandler = new ImageHandler(new WeakReference<>(this));

    private RequestQueue queue;    //网络请求

    private ContentsAdapter contentsAdapter;

    private List<ImageView> MyList = new ArrayList<>();
    private String[] strings_image = {"one.jpg", "two.jpg", "three.jpg", "four.jpg", "five.jpg", "six.jpg", "seven.jpg"};
    private NetworkImageView[] imageViews = new NetworkImageView[strings_image.length];
    private ImageView[] tips;
    //网路歌曲图片和标题路径   采用Json网络解析

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity());  //获取Volley请求

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragmentdiscover_one, null);
        mainFragmentMusicDiscoverOneList = (ListView) view.findViewById(R.id.main_fragment_music_discoverOne_list);
        contentsAdapter = new ContentsAdapter(getActivity());
        setMainFragmentMusicDiscoverOneList(inflater);

        return view;
    }

    //填充List适配器
    public void setMainFragmentMusicDiscoverOneList(LayoutInflater inflater) {
        setMainFragmentMusicDiscoverOneListHead(inflater);
        mainFragmentMusicDiscoverOneList.setAdapter(contentsAdapter);


    }


    //List 头布局

    public void setMainFragmentMusicDiscoverOneListHead(LayoutInflater inflater) {

        View view_head = inflater.inflate(R.layout.main_fragment_music_discoverone_list_head, null);
        View view_foot = inflater.inflate(R.layout.main_fragment_music_discoverone_list_finally, null);
        mainFragmentMusicDiscoverOneList.addHeaderView(view_head);
        mainFragmentMusicDiscoverOneList.addFooterView(view_foot);


        mainFragmentMusicDiscoverOneListHeadViewPager = (ViewPager) view_head.findViewById(R.id.main_fragment_music_discoverOne_listHead_viewPager);
        mainFragmentMusicDiscoverOneListHeadRecommendFM = (FlipImageView) view_head.findViewById(R.id.main_fragment_music_discoverOne_listHead_recommendFM);

        LinearLayout mainFragmentMusicDiscoverOneListHeadViewGroup = (LinearLayout) view_head.findViewById(R.id.main_fragment_music_discoverOne_listHead_viewGroup);


        tips = new ImageView[strings_image.length];


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
        layoutParams.leftMargin = 5;
        layoutParams.rightMargin = 5;

        for (int i = 0; i < tips.length; i++) {

            ImageView imageView = new ImageView(getActivity());

            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.lunbo_yes);
            } else {
                tips[i].setBackgroundResource(R.drawable.lunbo_no);
            }

            mainFragmentMusicDiscoverOneListHeadViewGroup.addView(imageView, layoutParams);

        }
        AdvertisementOnClickListener onClickListener = new AdvertisementOnClickListener();
        for (int i = 0; i < strings_image.length; i++) {
            imageViews[i] = new NetworkImageView(getActivity());
            imageViews[i].setId(i);
            imageViews[i].setOnClickListener(onClickListener);

            LruImageCache lruImageCache = LruImageCache.instance();

            ImageLoader imageLoader = new ImageLoader(queue, lruImageCache);

            imageViews[i].setDefaultImageResId(R.drawable.main_discover_advertisement);
            imageViews[i].setErrorImageResId(R.drawable.main_discover_advertisement);

            imageViews[i].setImageUrl(MainActivity.path + "MyMusic/DiscoverOne/ListHand/Advertisement/" + strings_image[i], imageLoader);


            MyList.add(imageViews[i]);
        }

        PagerAdapter myAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return (view == object);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                position %= MyList.size();
                if (position < 0) {
                    position = MyList.size() + position;
                }
                ImageView view = MyList.get(position);
                //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                ViewParent vp = view.getParent();
                if (vp != null) {
                    ViewGroup parent = (ViewGroup) vp;
                    parent.removeView(view);
                }
                container.addView(view);

                return view;
            }
        };


        mainFragmentMusicDiscoverOneListHeadViewPager.setAdapter(myAdapter);
        mainFragmentMusicDiscoverOneListHeadViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tips.length; i++) {
                    if (i == position % MyList.size()) {
                        tips[i].setBackgroundResource(R.drawable.lunbo_yes);
                    } else {
                        tips[i].setBackgroundResource(R.drawable.lunbo_no);
                    }
                }

                imgHandler.sendMessage(Message.obtain(imgHandler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        imgHandler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        imgHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });
        mainFragmentMusicDiscoverOneListHeadViewPager.setCurrentItem(MyList.size() * 10);
        //开始轮播效果
        imgHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);


        view_foot.findViewById(R.id.main_fragment_music_discoverOne_list_btn).setOnClickListener(this);
    }



    //轮播广告的点击事件
    private class AdvertisementOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getActivity(), MainActivity_Web.class);
            switch (v.getId()) {
                case 0:
                    //可以是自己服务器的手机网站
                    intent.putExtra("url", "http://music.163.com/#/topic?id=14475052");
                    break;
                case 1:
                    intent.putExtra("url", "http://music.163.com/#/topic?id=14475052");
                    break;
                case 2:
                    intent.putExtra("url", "http://music.163.com/#/topic?id=14475052");
                    break;
                case 3:
                    intent.putExtra("url", "http://music.163.com/#/topic?id=14475052");
                    break;
                case 4:
                    intent.putExtra("url", "http://music.163.com/#/topic?id=14475052");
                    break;
                case 5:
                    intent.putExtra("url", "http://music.163.com/#/topic?id=14475052");
                    break;
                case 6:
                    intent.putExtra("url", "http://music.163.com/#/topic?id=14475052");
                    break;
            }
            if (MainActivity.isNetworkConnected) {
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "网络未连接，请检查！", Toast.LENGTH_SHORT).show();

            }

        }
    }

    //内容List适配器
    private class ContentsAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater inflater;
        int[] ints_img = {R.drawable.recommend_icn_recmd, 0, R.drawable.recommend_icn_newest, 0, R.drawable.recommend_icn_radio};
        String[] strings_test = {"个性推荐", "", "最新音乐", "", "主播电台"};

        final int[] ints_holder1_img = {

                R.id.main_fragment_music_discoverOne_list_0ne_img1, R.id.main_fragment_music_discoverOne_list_0ne_img2, R.id.main_fragment_music_discoverOne_list_0ne_img3,
                R.id.main_fragment_music_discoverOne_list_0ne_img4, R.id.main_fragment_music_discoverOne_list_0ne_img5, R.id.main_fragment_music_discoverOne_list_0ne_img6};

        final int[] ints_holder2_img = {R.id.main_fragment_music_discoverOne_list_Two_img1, R.id.main_fragment_music_discoverOne_list_Two_img2, R.id.main_fragment_music_discoverOne_list_Two_img3};

        final int[] ints_holder3_img = {
                R.id.main_fragment_music_discoverOne_list_Four_img1, R.id.main_fragment_music_discoverOne_list_Four_img2,
                R.id.main_fragment_music_discoverOne_list_Four_img3, R.id.main_fragment_music_discoverOne_list_Four_img4};

        final int[] ints_holder1_text = {

                R.id.main_fragment_music_discoverOne_list_0ne_text1, R.id.main_fragment_music_discoverOne_list_0ne_text2, R.id.main_fragment_music_discoverOne_list_0ne_text3,
                R.id.main_fragment_music_discoverOne_list_0ne_text4, R.id.main_fragment_music_discoverOne_list_0ne_text5, R.id.main_fragment_music_discoverOne_list_0ne_text6};

        final int[] ints_holder2_text = {R.id.main_fragment_music_discoverOne_list_Two_text1, R.id.main_fragment_music_discoverOne_list_Two_text2, R.id.main_fragment_music_discoverOne_list_Two_text3};

        final int[] ints_holder3_text = {R.id.main_fragment_music_discoverOne_list_Four_text1, R.id.main_fragment_music_discoverOne_list_Four_text2,
                R.id.main_fragment_music_discoverOne_list_Four_text3, R.id.main_fragment_music_discoverOne_list_Four_text4};


        public ContentsAdapter(Context context) {
            mContext = context;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        //返回样式的数量
        @Override
        public int getViewTypeCount() {

            return 4;
        }

        @Override
        public int getItemViewType(int position) {
            int p = position % 4;
            if (p == 1) {

                return 2;
            } else if (p == 3) {

                return 3;
            } else {

                return 1;
            }

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            viewHolder1 holder1 = null;
            viewHolder2 holder2 = null;
            viewHolder3 holder3 = null;
            int type = getItemViewType(position);

            if (convertView == null) {
                switch (type) {
                    case 1: {

                        convertView = inflater.inflate(R.layout.main_fragment_music_discoverone_list_1, null);
                        holder1 = new viewHolder1();

                        holder1.imageView0 = (ImageView) convertView.findViewById(R.id.main_fragment_music_discoverOne_list_title_img);
                        holder1.textView0 = (TextView) convertView.findViewById(R.id.main_fragment_music_discoverOne_list_title_text);

                        for (int i = 0; i < ints_holder1_img.length; i++) {
                            holder1.imageViews[i] = (NetworkImageView) convertView.findViewById(ints_holder1_img[i]);
                            holder1.textViews[i] = (TextView) convertView.findViewById(ints_holder1_text[i]);

                        }

                        convertView.setTag(holder1);

                    }

                    break;
                    case 2: {

                        convertView = inflater.inflate(R.layout.main_fragment_music_discoverone_list_2, null);
                        holder2 = new viewHolder2();
                        for (int i = 0; i < ints_holder2_img.length; i++) {
                            holder2.imageViews[i] = (NetworkImageView) convertView.findViewById(ints_holder2_img[i]);
                            holder2.textViews[i] = (TextView) convertView.findViewById(ints_holder2_text[i]);

                        }
                        convertView.setTag(holder2);

                    }
                    break;
                    case 3: {

                        convertView = inflater.inflate(R.layout.main_fragment_music_discoverone_list_4, null);
                        holder3 = new viewHolder3();
                        for (int i = 0; i < ints_holder3_img.length; i++) {
                            holder3.imageViews[i] = (NetworkImageView) convertView.findViewById(ints_holder3_img[i]);
                            holder3.textViews[i] = (TextView) convertView.findViewById(ints_holder3_text[i]);

                        }
                        convertView.setTag(holder3);

                    }
                    break;
                }
            } else {
                switch (type) {
                    case 1:
                        holder1 = (viewHolder1) convertView.getTag();
                        break;
                    case 2:
                        holder2 = (viewHolder2) convertView.getTag();
                        break;
                    case 3:
                        holder3 = (viewHolder3) convertView.getTag();
                        break;
                }
            }

            LruImageCache lruImageCache = LruImageCache.instance();
            ImageLoader imageLoader = new ImageLoader(queue, lruImageCache);

            switch (type) {
                case 1: {

                    holder1.imageView0.setImageResource(ints_img[position]);
                    holder1.textView0.setText(strings_test[position]);

                    for (int i = 0; i < ints_holder1_img.length; i++) {
                        int j;
                        holder1.imageViews[i].setDefaultImageResId(R.drawable.placeholder_disk_background);
                        holder1.imageViews[i].setErrorImageResId(R.drawable.placeholder_disk_background);

                        if (MainActivity.contentJSON != null) {
                            if (position == 0) {
                                j = i;
                            } else if (position == 2) {
                                j = i + ints_holder1_img.length + ints_holder2_img.length;
                            } else {
                                j = i + ints_holder1_img.length * 2 + ints_holder2_img.length + ints_holder3_img.length;
                            }
                            try {
                                JSONObject jsonObject = MainActivity.contentJSON.getJSONObject(j);
                                holder1.imageViews[i].setImageUrl(MainActivity.path + "MyMusic/DiscoverOne/ListContent/" + jsonObject.getString("img"), imageLoader);
                                holder1.textViews[i].setText(jsonObject.getString("text"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                break;
                case 2: {

                    for (int i = 0; i < ints_holder2_img.length; i++) {

                        holder2.imageViews[i].setDefaultImageResId(R.drawable.placeholder_disk_background);
                        holder2.imageViews[i].setErrorImageResId(R.drawable.placeholder_disk_background);

                        if (MainActivity.contentJSON != null) {
                          int j = i+ints_holder1_img.length;
                            try {
                                JSONObject jsonObject = MainActivity.contentJSON.getJSONObject(j);
                                holder2.imageViews[i].setImageUrl(MainActivity.path + "MyMusic/DiscoverOne/ListContent/" + jsonObject.getString("img"), imageLoader);
                                holder2.textViews[i].setText(jsonObject.getString("text"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }
                break;
                case 3:{

                    for (int i = 0; i < ints_holder3_img.length; i++) {

                        holder3.imageViews[i].setDefaultImageResId(R.drawable.placeholder_disk_background);
                        holder3.imageViews[i].setErrorImageResId(R.drawable.placeholder_disk_background);

                        if (MainActivity.contentJSON != null) {
                            int j = i+ints_holder1_img.length*2+ints_holder2_img.length;
                            try {
                                JSONObject jsonObject = MainActivity.contentJSON.getJSONObject(j);
                                holder3.imageViews[i].setImageUrl(MainActivity.path + "MyMusic/DiscoverOne/ListContent/" + jsonObject.getString("img"), imageLoader);
                                holder3.textViews[i].setText(jsonObject.getString("text"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
                    break;
            }
            return convertView;
        }

        class viewHolder1 {

            ImageView imageView0;
            TextView textView0;
            NetworkImageView[] imageViews = new NetworkImageView[ints_holder1_img.length];
            TextView[] textViews = new TextView[ints_holder1_img.length];

        }

        class viewHolder2 {
            NetworkImageView[] imageViews = new NetworkImageView[ints_holder2_img.length];
            TextView[] textViews = new TextView[ints_holder2_img.length];
        }

        class viewHolder3 {
            NetworkImageView[] imageViews = new NetworkImageView[ints_holder3_img.length];
            TextView[] textViews = new TextView[ints_holder3_img.length];
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.main_fragment_music_discoverOne_list_btn: {
                startActivity(new Intent(getActivity(), MainActivity_Copyright.class));
            }
            break;
        }
    }

    //弱引用的Handler
    public class ImageHandler extends Handler {

        /**
         * 请求更新显示的View。
         */
        protected static final int MSG_UPDATE_IMAGE = 1;
        /**
         * 请求暂停轮播。
         */
        protected static final int MSG_KEEP_SILENT = 2;
        /**
         * 请求恢复轮播。
         */
        protected static final int MSG_BREAK_SILENT = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        protected static final int MSG_PAGE_CHANGED = 4;

        //轮播间隔时间
        protected static final long MSG_DELAY = 3000;

        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
        protected WeakReference<MainFragmentDiscover_one> weakReference;
        private int currentItem = 0;

        protected ImageHandler(WeakReference<MainFragmentDiscover_one> wk) {
            this.weakReference = wk;
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainFragmentDiscover_one fragment = weakReference.get();
            if (fragment != null) {
                //Activity已经回收，无需再处理UI了

                switch (msg.what) {
                    case MSG_UPDATE_IMAGE:
                        currentItem++;
                        fragment.mainFragmentMusicDiscoverOneListHeadViewPager.setCurrentItem(currentItem);
                        if (fragment.mainFragmentMusicDiscoverOneListHeadRecommendFM.isFlipped()) {
                            fragment.mainFragmentMusicDiscoverOneListHeadRecommendFM.setFlipped(false);
                        } else {
                            fragment.mainFragmentMusicDiscoverOneListHeadRecommendFM.setFlipped(true);
                        }
                        //准备下次播放
                        fragment.imgHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                        //消息队列:清除发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
                        fragment.imgHandler.removeMessages(MSG_UPDATE_IMAGE);
                        break;
                    case MSG_KEEP_SILENT:
                        //只要不发送消息就暂停了 并清除发送的消息
                        fragment.imgHandler.removeMessages(MSG_UPDATE_IMAGE);
                        break;
                    case MSG_BREAK_SILENT:
                        imgHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                        fragment.imgHandler.removeMessages(MSG_UPDATE_IMAGE);
                        break;
                    case MSG_PAGE_CHANGED:
                        //记录当前的页号，避免播放的时候页面显示不正确。
                        currentItem = msg.arg1;
                        break;
                    default:
                        break;

                }

            }
        }

    }

    @Override
    public void onDestroy() {
        MainActivity.editor.putInt("repeatState", 0).commit();
        super.onDestroy();
    }
}


