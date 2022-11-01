package com.gnu_graduate_project_team.junggal_v2;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class AlarmVO {

    @SerializedName("alarmId")
    private Integer alarmId;

    @SerializedName("sharePostId")
    private Integer sharePostId;

    @SerializedName("postName")
    private String postName;

    @SerializedName("postWriter")
    private String postWriter;

    @SerializedName("applyUser")
    private String applyUser;

    @SerializedName("message")
    private String message;

    @SerializedName("acceptFlag")
    private Boolean acceptFlag;

    @SerializedName("createTime")
    private String createTime;

    @SerializedName("responseFlag")
    private Boolean responseFlag;

    @SerializedName("requestFlag")
    private Boolean requestFlag;

    @SerializedName("shareTime")
    private String shareTime;

    @SerializedName("reviewFlag")
    private Boolean reviewFlag;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("MM-dd");

    public AlarmVO() {

    }

    public Integer getAlarmId() {
        return alarmId;
    }
    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }
    public Integer getSharePostId() {
        return sharePostId;
    }
    public void setSharePostId(Integer sharePostId) {
        this.sharePostId = sharePostId;
    }
    public String getPostName() {
        return postName;
    }
    public void setPostName(String postName) {
        this.postName = postName;
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
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Boolean getAcceptFlag() {
        return acceptFlag;
    }
    public void setAcceptFlag(Boolean acceptFlag) {
        this.acceptFlag = acceptFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Boolean getResponseFlag() {
        return responseFlag;
    }

    public void setResponseFlag(Boolean responseFlag) {
        this.responseFlag = responseFlag;
    }

    public Boolean getRequestFlag() {
        return requestFlag;
    }

    public void setRequestFlag(Boolean requestFlag) {
        this.requestFlag = requestFlag;
    }

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

    public Boolean getReviewFlag() {
        return reviewFlag;
    }

    public void setReviewFlag(Boolean reviewFlag) {
        this.reviewFlag = reviewFlag;
    }
    @Override
    public String toString() {
        return "AlarmVO{" +
                "alarmId=" + alarmId +
                ", postName='" + postName + '\'' +
                ", postWriter='" + postWriter + '\'' +
                ", applyUser='" + applyUser + '\'' +
                ", message='" + message + '\'' +
                ", acceptFlag=" + acceptFlag +
                ", createTime='" + createTime + '\'' +
                ", responseFlag=" + responseFlag +
                ", requestFlag=" + requestFlag +
                ", shareTime='" + shareTime + '\'' +
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
