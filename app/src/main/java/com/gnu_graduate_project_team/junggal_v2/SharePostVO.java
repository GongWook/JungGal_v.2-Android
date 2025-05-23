package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class SharePostVO {

    @SerializedName("share_post_id")
    private Integer share_post_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("share_post_name")
    private String share_post_name;

    @SerializedName("share_post_img_cnt")
    private Integer share_post_img_cnt;

    @SerializedName("share_post_region")
    private String share_post_region;

    @SerializedName("share_post_point")
    private String share_post_point;

    @SerializedName("share_post_icon")
    private Integer share_post_icon;

    @SerializedName("share_people")
    private Integer share_people;

    @SerializedName("share_story")
    private String share_story;

    @SerializedName("shared_people")
    private Integer shared_people;

    @SerializedName("share_time")
    private String share_time;

    @SerializedName("post_time")
    private String post_time;

    //파일 번호를 받기위함
    private Integer imgNumber;

    //검색 기능을 위함
    private String keyword;
    private Point point;

    public Integer getShare_post_id() {
        return share_post_id;
    }
    public void setShare_post_id(Integer share_post_id) {
        this.share_post_id = share_post_id;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShare_post_name() {
        return share_post_name;
    }

    public void setShare_post_name(String share_post_name) {
        this.share_post_name = share_post_name;
    }

    public Integer getShare_post_img_cnt() {
        return share_post_img_cnt;
    }

    public void setShare_post_img_cnt(Integer share_post_img_cnt) {
        this.share_post_img_cnt = share_post_img_cnt;
    }

    public String getShare_post_region() {
        return share_post_region;
    }

    public void setShare_post_region(String share_post_region) {
        this.share_post_region = share_post_region;
    }

    public String getShare_post_point() {
        return share_post_point;
    }

    public void setShare_post_point(String share_post_point) {
        this.share_post_point = share_post_point;
    }

    public Integer getShare_post_icon() {
        return share_post_icon;
    }

    public void setShare_post_icon(Integer share_post_icon) {
        this.share_post_icon = share_post_icon;
    }

    public Integer getShare_people() {
        return share_people;
    }

    public void setShare_people(Integer share_people) {
        this.share_people = share_people;
    }

    public String getShare_time() {
        return share_time;
    }

    public void setShare_time(String share_time) {
        this.share_time = share_time;
    }

    public String getShare_story() {
        return share_story;
    }

    public void setShare_story(String share_story) {
        this.share_story = share_story;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public Integer getShared_people() {
        return shared_people;
    }

    public void setShared_people(Integer shared_people) {
        this.shared_people = shared_people;
    }

    public Integer getImgNumber() {
        return imgNumber;
    }

    public void setImgNumber(Integer imgNumber) {
        this.imgNumber = imgNumber;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "SharePostVO{" +
                "share_post_id=" + share_post_id +
                ", user_id='" + user_id + '\'' +
                ", share_post_name='" + share_post_name + '\'' +
                ", share_post_img_cnt=" + share_post_img_cnt +
                ", share_post_region='" + share_post_region + '\'' +
                ", share_post_point='" + share_post_point + '\'' +
                ", share_post_icon=" + share_post_icon +
                ", share_people=" + share_people +
                ", share_story='" + share_story + '\'' +
                ", shared_people=" + shared_people +
                ", share_time='" + share_time + '\'' +
                ", post_time='" + post_time + '\'' +
                ", imgNumber=" + imgNumber +
                ", keyword='" + keyword + '\'' +
                ", point=" + point +
                '}';
    }
}
