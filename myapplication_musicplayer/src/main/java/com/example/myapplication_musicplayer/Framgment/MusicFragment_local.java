package com.example.myapplication_musicplayer.Framgment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.myapplication_musicplayer.Activity.MainActivity;
import com.example.myapplication_musicplayer.Activity.PlayerActivity;
import com.example.myapplication_musicplayer.ToolCass.MediaUtil;
import com.example.myapplication_musicplayer.ToolCass.MusicFile;
import com.example.myapplication_musicplayer.Srevice.PlayerService;
import com.example.myapplication_musicplayer.R;

import java.util.List;

/**
 * Created by wansh on 2016/9/14.
 * 显示本地音乐列表
 */
public class MusicFragment_local extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView musicActivityList;


    private setDrawerLayout setDrawerVISIBLE;
    private setPlayer player;


    public interface setDrawerLayout {

        void setDrawerVisibilityVISIBLE();
    }
    public interface setPlayer {
        void setPlay();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            setDrawerVISIBLE = (setDrawerLayout) activity;
            player = (setPlayer) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onFlipOnClick");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.music_fragment_local, container, false);
        Init(view);
        return view;
    }

    private void Init(View view) {

        List<MusicFile> musicFiles = MediaUtil.getMusicFile(getActivity());

        ImageView musicActivityBack = (ImageView) view.findViewById(R.id.music_fragment_back);
        musicActivityBack.setOnClickListener(this);
        musicActivityList = (ListView) view.findViewById(R.id.music_fragment_list);
        musicActivityList.setOnItemClickListener(this);
        musicActivityBack.setOnClickListener(this);
        setMusicActivityList(musicFiles);
    }

    public void setMusicActivityList(List<MusicFile> musicFiles) {

        musicActivityList.addHeaderView(View.inflate(getActivity(), R.layout.music_activity_list_head, null));
        SimpleAdapter mAdapter = new SimpleAdapter(getActivity(), MediaUtil.getMusicFileMaps(musicFiles), R.layout.music_activity_list_style, new String[]{"title", "artist"}, new int[]{R.id.music_list_title, R.id.music_list_artist});
        musicActivityList.setAdapter(mAdapter);
    }

    //退出事件监听
    @Override
    public void onClick(View v) {

        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        setDrawerVISIBLE.setDrawerVisibilityVISIBLE();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MainActivity.isPlay = true;

        if (position != 0) {

            position = position - 1;

        }

        Intent intent_Activity = new Intent(getActivity(), PlayerActivity.class);

        startActivityForResult(intent_Activity,1);

        Intent intent_Service = new Intent(getActivity(), PlayerService.class);
        intent_Service.putExtra("position", position);
        getActivity().startService(intent_Service);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        player.setPlay();
    }

}
