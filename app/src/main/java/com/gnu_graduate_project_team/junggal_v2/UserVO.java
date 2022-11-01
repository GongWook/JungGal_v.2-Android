package com.gnu_graduate_project_team.junggal_v2;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserVO implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("pw")
    private String pw;

    @SerializedName("name")
    private String name;

    @SerializedName("phone_number")
    private String phone_number;

    @SerializedName("introduce")
    private String introduce;

    @SerializedName("share_point")
    private Double share_point;

    @SerializedName("seller_auth")
    private Boolean seller_auth;

    @SerializedName("profile_flag")
    private Boolean profile_flag;

    @SerializedName("imagedata")
    private String imagedata;

    @SerializedName("responseAlarmCnt")
    private int responseAlarmCnt;

    @SerializedName("requestAlarmCnt")
    private int requestAlarmCnt;

    @SerializedName("real_name")
    private String real_name;

    private Boolean profile_change_flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Double getShare_point() {
        return share_point;
    }

    public void setShare_point(Double share_point) {
        this.share_point = share_point;
    }

    public Boolean getSeller_auth() {
        return seller_auth;
    }

    public void setSeller_auth(Boolean seller_auth) {
        this.seller_auth = seller_auth;
    }

    public Boolean getProfile_flag() {
        return profile_flag;
    }

    public void setProfile_flag(Boolean profile_flag) {
        this.profile_flag = profile_flag;
    }

    public String getImagedata() {
        return imagedata;
    }

    public void setImagedata(String imagedata) {
        this.imagedata = imagedata;
    }

    public int getResponseAlarmCnt() {
        return responseAlarmCnt;
    }

    public void setResponseAlarmCnt(int responseAlarmCnt) {
        this.responseAlarmCnt = responseAlarmCnt;
    }

    public int getRequestAlarmCnt() {
        return requestAlarmCnt;
    }

    public void setRequestAlarmCnt(int requestAlarmCnt) {
        this.requestAlarmCnt = requestAlarmCnt;
    }

    public Boolean getProfile_change_flag() {
        return profile_change_flag;
    }

    public void setProfile_change_flag(Boolean profile_change_flag) {
        this.profile_change_flag = profile_change_flag;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "id='" + id + '\'' +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", introduce='" + introduce + '\'' +
                ", share_point=" + share_point +
                ", seller_auth=" + seller_auth +
                ", profile_flag=" + profile_flag +
                ", imagedata='" + imagedata + '\'' +
                ", responseAlarmCnt=" + responseAlarmCnt +
                ", requestAlarmCnt=" + requestAlarmCnt +
                ", real_name='" + real_name + '\'' +
                ", profile_change_flag=" + profile_change_flag +
                '}';
    }
}
