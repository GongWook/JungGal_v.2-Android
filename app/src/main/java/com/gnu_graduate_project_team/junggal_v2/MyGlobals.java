package com.gnu_graduate_project_team.junggal_v2;

public class MyGlobals {

    private int alarmCnt;

    public int getAlarmCnt() {
        return alarmCnt;
    }

    public void setAlarmCnt(int alarmCnt) {
        this.alarmCnt = alarmCnt;
    }

    private static MyGlobals instance =null;

    public static synchronized MyGlobals getInstance()
    {
        if(null == instance)
        {
            instance = new MyGlobals();
        }

        return instance;
    }
}
