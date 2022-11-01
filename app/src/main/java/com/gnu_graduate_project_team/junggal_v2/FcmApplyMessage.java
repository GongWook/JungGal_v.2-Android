package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class FcmApplyMessage {

    @SerializedName("sharePostId")
    private Integer sharePostId;

    @SerializedName("postWriter")
    private String postWriter;

    @SerializedName("postName")
    private String postName;

    @SerializedName("applyUserName")
    private String applyUserName;

    @SerializedName("applyUser")
    private String applyUser;

    @SerializedName("shareTime")
    private String shareTime;


    public FcmApplyMessage() {

    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public Integer getSharePostId() {
        return sharePostId;
    }

    public void setSharePostId(Integer sharePostId) {
        this.sharePostId = sharePostId;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getPostWriter() {
        return postWriter;
    }

    public void setPostWriter(String postWriter) {
        this.postWriter = postWriter;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

    @Override
    public String toString() {
        return "FcmApplyMessage [sharePostId=" + sharePostId + ", applyUser=" + applyUser + ", postWriter=" + postWriter
                + ", postName=" + postName + ", shareTime=" + shareTime + "]";
    }

}
