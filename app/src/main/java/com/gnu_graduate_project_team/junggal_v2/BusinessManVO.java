package com.gnu_graduate_project_team.junggal_v2;

public class BusinessManVO {

    private Integer id;
    private String user_id;
    private String user_name;
    private String store_name;
    private String share_position;
    private String store_open_time;
    private String business_id;
    private String opening_date;
    private String use_bank;
    private String account;
    private Double latitude;
    private Double longitude;

    public BusinessManVO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getShare_position() {
        return share_position;
    }

    public void setShare_position(String share_position) {
        this.share_position = share_position;
    }

    public String getStore_open_time() {
        return store_open_time;
    }

    public void setStore_open_time(String store_open_time) {
        this.store_open_time = store_open_time;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getOpening_date() {
        return opening_date;
    }

    public void setOpening_date(String opening_date) {
        this.opening_date = opening_date;
    }

    public String getUse_bank() {
        return use_bank;
    }

    public void setUse_bank(String use_bank) {
        this.use_bank = use_bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "BusinessManVO [id=" + id + ", user_id=" + user_id + ", user_name=" + user_name + ", store_name="
                + store_name + ", share_position=" + share_position + ", store_open_time=" + store_open_time
                + ", business_id=" + business_id + ", opening_date=" + opening_date + ", use_bank=" + use_bank
                + ", account=" + account + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }
}
