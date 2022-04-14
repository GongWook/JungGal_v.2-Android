package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class PhoneVO {

    @SerializedName("phone_number")
    private String phone_number;

    @SerializedName("auth_number")
    private String auth_number;

    @SerializedName("success_flag")
    private Boolean success_flag;

    public Boolean getSuccess_flag() {
        return success_flag;
    }

    public void setSuccess_flag(Boolean success_flag) {
        this.success_flag = success_flag;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAuth_number() {
        return auth_number;
    }

    public void setAuth_number(String auth_number) {
        this.auth_number = auth_number;
    }
}
