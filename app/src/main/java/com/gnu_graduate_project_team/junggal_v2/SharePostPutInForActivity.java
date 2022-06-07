package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SharePostPutInForActivity extends Activity {

    private String remainTime;
    private String remainPeople;
    private TextView remainTimeView;
    private TextView remainPeopleView;
    private TextView nowTimeView;

    private String nowTime;
    private Integer nowHour;
    private Integer nowMin;

    private Button minus_time;
    private Button plus_time;

    /** Thread 사용 **/
    Handler mHandler = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_post_put_in_for_activity);

        mHandler = new Handler();
        Intent intent = getIntent();

        remainTime = intent.getStringExtra("remainTime");
        remainPeople = intent.getStringExtra("remainPeople");

        remainTimeView = (TextView) findViewById(R.id.remainTime);
        remainPeopleView = (TextView) findViewById(R.id.remainPeople);
        nowTimeView = (TextView) findViewById(R.id.nowTime);
        minus_time = (Button) findViewById(R.id.minus_time);
        plus_time = (Button) findViewById(R.id.plus_time);

        //Timer
        Date today = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("H");
        SimpleDateFormat sdf2 = new SimpleDateFormat("m");
        nowHour = Integer.parseInt(sdf1.format(today));
        nowMin = Integer.parseInt(sdf2.format(today));
        nowMin = (nowMin/5) *5;

        if(nowMin==0 || nowMin==5)
        {
            nowTime = nowHour+" : 0"+ nowMin;
        }
        else
        {
            nowTime = nowHour+" : "+ nowMin;
        }


        uisetting();

        //minus_time listener
        minus_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nowMin-=5;
                if(nowMin<0)
                {
                    nowMin=55;
                    nowHour--;
                    if(nowHour<0)
                        nowHour=23;
                }

                if(nowMin==0 || nowMin==5)
                {
                    nowTime = nowHour+" : 0"+ nowMin;
                }
                else
                {
                    nowTime = nowHour+" : "+ nowMin;
                }

                setTimeUiSetting();

            }
        });

        //plus_time listner
        plus_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowMin+=5;
                if(nowMin==60)
                {
                    nowMin=0;
                    nowHour++;
                    if(nowHour==24)
                        nowHour=0;
                }

                if(nowMin==0 || nowMin==5)
                {
                    nowTime = nowHour+" : 0"+ nowMin;
                }
                else
                {
                    nowTime = nowHour+" : "+ nowMin;
                }


                setTimeUiSetting();

            }
        });

    }

    public void uisetting()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        remainTimeView.setText(remainTime);
                        remainPeopleView.setText(remainPeople);
                        nowTimeView.setText(nowTime);

                    }
                });
            }
        });

        t.start();
    }

    public void setTimeUiSetting()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        nowTimeView.setText(nowTime);
                    }
                });
            }
        });

        t.start();
    }
}
