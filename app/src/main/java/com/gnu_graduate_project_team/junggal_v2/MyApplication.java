package com.gnu_graduate_project_team.junggal_v2;

import android.app.Application;

public class MyApplication extends Application {

    private Boolean first_flag = false;
    private UserVO userVO;

    public Boolean getFirst_flag() {
        return first_flag;
    }

    public void setFirst_flag(Boolean first_flag) {
        this.first_flag = first_flag;
    }

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }
}
