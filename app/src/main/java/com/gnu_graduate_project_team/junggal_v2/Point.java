package com.gnu_graduate_project_team.junggal_v2;

public class Point {

    private String point;

    public Point(String point) {
        this.point = point;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "Point{" +
                "point='" + point + '\'' +
                '}';
    }
}
