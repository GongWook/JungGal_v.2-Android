package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserVO {

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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", introduce='" + introduce + '\'' +
                ", share_point=" + share_point +
                ", seller_auth=" + seller_auth +
                '}';
    }
}
