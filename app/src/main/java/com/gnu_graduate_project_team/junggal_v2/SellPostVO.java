package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class SellPostVO {

    @SerializedName("sell_post_item_name")
    private String sell_post_item_name;

    //이미지는 어떻게 들고와야할지 모르겠음
    @SerializedName("dish_photo")
    private String dish_photo;

    @SerializedName("sell_post_story")
    private String sell_post_story;

    @SerializedName("sell_post_item_price")
    private String sell_post_item_price;

    @SerializedName("sell_post_item_check")
    private int sell_post_item_check;

    //파일 번호를 받기위함
    private Integer imgNumber;

    //검색 기능을 위함
    private String keyword;
    private Point point;

    public String getSell_post_item_name() {
        return sell_post_item_name;
    }

    public String getDish_photo() {
        return dish_photo;
    }

    public String getSell_post_story() {
        return sell_post_story;
    }

    public String getSell_post_item_price() {
        return sell_post_item_price;
    }

    public int getSell_post_item_check() { return sell_post_item_check;}

    public void setSell_post_item_name(String sell_post_item_name) {
        this.sell_post_item_name = sell_post_item_name;
    }

    public void setDish_photo(String dish_photo) {
        this.dish_photo = dish_photo;
    }

    public void setSell_post_story(String sell_post_story) {
        this.sell_post_story = sell_post_story;
    }

    public void setSell_post_item_price(String sell_post_item_price) {
        this.sell_post_item_price = sell_post_item_price;
    }

    public void setSell_post_item_price() { this.sell_post_item_check = sell_post_item_check;}
}
