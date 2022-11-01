package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class NaverDirection5Section {
    @SerializedName("pointIndex")
    private String pointIndex;

    @SerializedName("pointCount")
    private String pointCount;

    @SerializedName("distance")
    private String distance;

    @SerializedName("name")
    private String name;

    @SerializedName("congestion")
    private String congestion;

    @SerializedName("speed")
    private String speed;

    public String getPointIndex ()
    {
        return pointIndex;
    }

    public void setPointIndex (String pointIndex)
    {
        this.pointIndex = pointIndex;
    }

    public String getPointCount ()
    {
        return pointCount;
    }

    public void setPointCount (String pointCount)
    {
        this.pointCount = pointCount;
    }

    public String getDistance ()
    {
        return distance;
    }

    public void setDistance (String distance)
    {
        this.distance = distance;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getCongestion ()
    {
        return congestion;
    }

    public void setCongestion (String congestion)
    {
        this.congestion = congestion;
    }

    public String getSpeed ()
    {
        return speed;
    }

    public void setSpeed (String speed)
    {
        this.speed = speed;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pointIndex = "+pointIndex+", pointCount = "+pointCount+", distance = "+distance+", name = "+name+", congestion = "+congestion+", speed = "+speed+"]";
    }
}
