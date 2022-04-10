package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

public class SplashActivity extends Activity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication application = (MyApplication) getApplication();

        Boolean firstCheckFlag;
        firstCheckFlag = PreferenceManager.getBoolean(SplashActivity.this, "first_check_flag");

        Log.d("test check flag : ", firstCheckFlag.toString());



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent;


                if(firstCheckFlag == true)
                {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    intent = new Intent(SplashActivity.this, HelloActivity.class);
                    startActivity(intent);
                }



                finish();
            }
        },3000);

    }
}
