package com.gnu_graduate_project_team.junggal_v2;

import android.app.Application;

public class MyApplication extends Application {


    public static UserVO user_data;
    private Integer alarmCount=0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Integer getAlarmCount() {
        return alarmCount;
    }

    public void setAlarmCount(Integer alarmCount) {
        this.alarmCount = alarmCount;
    }
}
