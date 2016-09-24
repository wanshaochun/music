package com.example.myapplication_musicplayer.Framgment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.myapplication_musicplayer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wansh on 2016/8/6.
 * 本地模块
 */
public class MainFragment_music extends Fragment implements AdapterView.OnItemClickListener {


    private ListView mainFragmentMusicList;


    private setDrawerLayout setDrawerGONE;


    public interface setDrawerLayout {

        void setDrawerVisibilityGONE();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            setDrawerGONE = (setDrawerLayout) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onFlipOnClick");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragment_music, null);

        mainFragmentMusicList = (ListView) view.findViewById(R.id.main_fragment_music_list);
        mainFragmentMusicList.setOnItemClickListener(this);
        setMainFragmentMusicList();


        return view;
    }

    //填充lisView
    private void setMainFragmentMusicList() {

        final int[] ints_imgIds = {R.drawable.lay_icn_mobile, R.drawable.lay_icn_time, R.drawable.lay_icn_dld, R.drawable.lay_icn_artist};
        final String[] strNames = {"本地音乐", "最近播放", "下载管理", "我的歌手"};

        List<Map<String, Object>> listItems = new ArrayList<>();

        for (int i = 0; i < ints_imgIds.length; i++) {

            Map<String, Object> mapItem = new HashMap<>();
            mapItem.put("img", ints_imgIds[i]);
            mapItem.put("text", strNames[i]);
            listItems.add(mapItem);

        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.main_fragment_music_list_style, new String[]{"img", "text"}, new int[]{R.id.main_fragment_music_list_img, R.id.main_fragment_music_list_text});
        mainFragmentMusicList.setAdapter(simpleAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0: {


                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(R.id.main_content_main,new MusicFragment_local(), "musicFragment_local").commit();

                setDrawerGONE.setDrawerVisibilityGONE();
            }
            break;
            case 1: {


            }
            break;
            case 2: {

            }
            break;
        }

    }

}
