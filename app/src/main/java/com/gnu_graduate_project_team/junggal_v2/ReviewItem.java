package com.gnu_graduate_project_team.junggal_v2;

public class ReviewItem {
    
    int viewType; //화면타입
    Object object; //데이터

    public ReviewItem(int viewType, Object object) {
        this.viewType = viewType;
        this.object = object;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
