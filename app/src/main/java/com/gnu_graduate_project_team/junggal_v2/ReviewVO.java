package com.gnu_graduate_project_team.junggal_v2;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewVO {

    private Integer reviewId;
    private String postWriter;
    private String applyUser;
    private String applyUserName;
    private float reviewPoint;
    private String content;
    private String createTime;
    private Integer alarmId;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("MM-dd");

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public String getPostWriter() {
        return postWriter;
    }

    public void setPostWriter(String postWriter) {
        this.postWriter = postWriter;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public float getReviewPoint() {
        return reviewPoint;
    }

    public void setReviewPoint(float reviewPoint) {
        this.reviewPoint = reviewPoint;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    @Override
    public String toString() {
        return "ReviewVO{" +
                "reviewId=" + reviewId +
                ", postWriter='" + postWriter + '\'' +
                ", applyUser='" + applyUser + '\'' +
                ", reviewPoint=" + reviewPoint +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    public String calcTime()
    {
        String result="";
        String[] sqlDate = createTime.split("T");

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        if(sqlDate[0].equals(mFormat.format(mDate)))
        {
            result=sqlDate[1];
        }
        else
        {
            result=sqlDate[0];
        }

        return result;
    }
}
