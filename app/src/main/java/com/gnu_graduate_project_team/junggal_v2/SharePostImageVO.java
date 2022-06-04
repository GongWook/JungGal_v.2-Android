package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

import okhttp3.ResponseBody;

public class SharePostImageVO {

    @SerializedName("imagedata")
    String imagedata;

    public String getImagedata() {
        return imagedata;
    }

    public void setImagedata(String imagedata) {
        this.imagedata = imagedata;
    }

    @Override
    public String toString() {
        return "SharePostImageVO{" +
                "imagedata='" + imagedata + '\'' +
                '}';
    }
}
