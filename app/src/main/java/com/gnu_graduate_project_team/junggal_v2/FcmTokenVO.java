package com.gnu_graduate_project_team.junggal_v2;

public class FcmTokenVO {

    private String token;
    private String user_id;

    public FcmTokenVO() {

    }

    public FcmTokenVO(String token, String user_id) {
        this.token = token;
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "FcmTokenVO [user_id=" + user_id + ", token=" + token + "]";
    }
}
