package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Retrofit;

public class SharePostPutInForActivity extends Activity {

    private String remainTime;
    private String remainPeople;
    private FcmApplyMessage fcmApplyMessage;
    private String userId;

    private TextView remainTimeView;
    private TextView remainPeopleView;
    private TextView nowTimeView;
    private Button apply_btn;

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
        fcmApplyMessage = new FcmApplyMessage();
        userId = PreferenceManager.getString(SharePostPutInForActivity.this,"user_id");
        
        remainTime = intent.getStringExtra("remainTime");
        remainPeople = intent.getStringExtra("remainPeople");

        /** FcmApplyMessageVO 작성 **/
        fcmApplyMessage.setSharePostId(intent.getIntExtra("sharePostId",0));
        fcmApplyMessage.setPostName(intent.getStringExtra("sharePostName"));
        fcmApplyMessage.setPostWriter(intent.getStringExtra("sharePostWriter"));
        fcmApplyMessage.setApplyUser(userId);

        remainTimeView = (TextView) findViewById(R.id.remainTime);
        remainPeopleView = (TextView) findViewById(R.id.remainPeople);
        nowTimeView = (TextView) findViewById(R.id.nowTime);
        minus_time = (Button) findViewById(R.id.minus_time);
        plus_time = (Button) findViewById(R.id.plus_time);
        apply_btn = (Button) findViewById(R.id.apply_btn);

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

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fcmApplyMessage.setShareTime(nowTime);

                String title = nowTime +"에 나눔 신청을 하시겠습니까?";

                Intent intent1 = new Intent(SharePostPutInForActivity.this, PopUpActivity.class);
                intent1.putExtra("title",title);
                intent1.putExtra("shareTime", fcmApplyMessage.getShareTime());
                intent1.putExtra("sharePostId",fcmApplyMessage.getSharePostId());
                intent1.putExtra("sharePostName",fcmApplyMessage.getPostName());
                intent1.putExtra("sharePostWriter",fcmApplyMessage.getPostWriter());
                intent1.putExtra("applyUser",fcmApplyMessage.getApplyUser());
                startActivityForResult(intent1,1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {
                String result=data.getStringExtra("result");
                if(result.equals("ok"))
                {
                    finish();
                }
            }
        }
    }
}
