package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.viewpager2.widget.ViewPager2;

public class SharePostActivity extends Activity {

    private ViewPager2 slideView;
    private LinearLayout slidebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_post_activity);

        /** 반찬 PostId 값 받기 **/
        Intent intent = getIntent();
        Integer sharePostId = intent.getIntExtra("sharePostID",0);
        Log.d("postId test", sharePostId.toString());

        /** 반찬 이미지 ViewPager2 코드 **/
        //slideView = findViewById(R.id.sliderViewPager);

        //Adapter


        /** ViewPager2 아래 Indicator 코드 **/
        //slidebar = findViewById(R.id.layoutIndicators);

    }
}
