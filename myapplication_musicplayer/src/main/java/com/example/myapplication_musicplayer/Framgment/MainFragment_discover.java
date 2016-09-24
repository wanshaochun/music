package com.example.myapplication_musicplayer.Framgment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.myapplication_musicplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wansh on 2016/9/11.
 * 在线模块
 */
public class MainFragment_discover extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private final int[] ints_RadioButton = {R.id.main_discover_TextOne, R.id.main_discover_TextTwo, R.id.main_discover_TextThree, R.id.main_discover_TextFour};
    private RadioButton[] myRadioButton = new RadioButton[ints_RadioButton.length];

    private ImageView idTabLineIv;
    private int currentIndex;   //ViewPager的当前选中页
    private int screenWidth;   //屏幕的宽度
    private ViewPager mainDiscoverViewpager;

    private List<Fragment> fragmentList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mainfragment_discover, null);

        Init(view);

        setMainDiscoverViewpager();
        initTabLineWidth();
        return view;
    }

    private void Init(View view) {
        RadioGroup mainDiscoverRadio = (RadioGroup) view.findViewById(R.id.main_discover_radio);
        idTabLineIv = (ImageView) view.findViewById(R.id.id_tab_line_iv);
        mainDiscoverViewpager = (ViewPager) view.findViewById(R.id.main_discover_viewpager);
        for (int i = 0; i < ints_RadioButton.length; i++) {
            myRadioButton[i] = (RadioButton) view.findViewById(ints_RadioButton[i]);
        }
        mainDiscoverRadio.setOnCheckedChangeListener(this);


    }

    //设置ViewPager适配器
    private void setMainDiscoverViewpager() {

        fragmentList = new ArrayList<>();
        MainFragmentDiscover_one mainFragmentDiscover_one = new MainFragmentDiscover_one();
        MainFragmentDiscover_two mainFragmentDiscover_two = new MainFragmentDiscover_two();
        MainFragmentDiscover_two mainFragmentDiscover_three = new MainFragmentDiscover_two();
        MainFragmentDiscover_two mainFragmentDiscover_four = new MainFragmentDiscover_two();
        fragmentList.add(mainFragmentDiscover_one);
        fragmentList.add(mainFragmentDiscover_two);
        fragmentList.add(mainFragmentDiscover_three);
        fragmentList.add(mainFragmentDiscover_four);


        FragmentStatePagerAdapter myAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };

        mainDiscoverViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) idTabLineIv.getLayoutParams();
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 4) + currentIndex * (screenWidth / 4));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - positionOffset) * (screenWidth * 1.0 / 4) + currentIndex * (screenWidth / 4));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 4) + currentIndex * (screenWidth / 4));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - positionOffset) * (screenWidth * 1.0 / 3) + currentIndex * (screenWidth / 4));
                } else if (currentIndex == 2 && position == 2) // 2->3
                {
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 4) + currentIndex * (screenWidth / 4));
                } else if (currentIndex == 3 && position == 2) // 3->2
                {
                    lp.leftMargin = (int) (-(1 - positionOffset) * (screenWidth * 1.0 / 4) + currentIndex * (screenWidth / 4));
                }
                idTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                myRadioButton[position].setChecked(true);
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mainDiscoverViewpager.setAdapter(myAdapter);


    }

    // 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();

        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) idTabLineIv.getLayoutParams();
        lp.width = screenWidth / 4;
        idTabLineIv.setLayoutParams(lp);
    }

    //选中事件监听
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_discover_TextOne:
                mainDiscoverViewpager.setCurrentItem(0);
                break;
            case R.id.main_discover_TextTwo:
                mainDiscoverViewpager.setCurrentItem(1);
                break;
            case R.id.main_discover_TextThree:
                mainDiscoverViewpager.setCurrentItem(2);
                break;
            case R.id.main_discover_TextFour:
                mainDiscoverViewpager.setCurrentItem(3);
                break;
        }
    }

}
