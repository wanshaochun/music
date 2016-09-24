package com.example.myapplication_musicplayer.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.myapplication_musicplayer.R;

/**
 * Created by wansh on 2016/9/20.
 */
public class MainActivity_Copyright extends Activity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.copyright);
        setContentView(R.layout.main_activity_copyright);

    }

}
