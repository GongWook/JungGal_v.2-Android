package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class NaverDirection5ResultPath {

    @SerializedName("code")
    private String code;

    @SerializedName("route")
    private NaverDirection5Route route;

    @SerializedName("currentDateTime")
    private String currentDateTime;

    @SerializedName("message")
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public NaverDirection5Route getRoute() {
        return route;
    }

    public void setRoute(NaverDirection5Route route) {
        this.route = route;
    }

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NaverDirection5ResultPath{" +
                "code='" + code + '\'' +
                ", route=" + route +
                ", currentDateTime='" + currentDateTime + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
