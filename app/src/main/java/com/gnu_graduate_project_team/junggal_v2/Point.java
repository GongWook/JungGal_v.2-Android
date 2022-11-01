package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class Point {

    private String point;

    @SerializedName("longitude")
    private String Longitude;

    @SerializedName("latitude")
    private String Latitude;

    public Point(){}

    public Point(String point) {
        this.point = point;
    }

    public String getPoint() {
        return point;
    }
    public void setPoint(String point) {
        this.point = point;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    @Override
    public String toString() {
        return "Point{" +
                "point='" + point + '\'' +
                '}';
    }
}
