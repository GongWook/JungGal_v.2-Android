package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class NaverDirection5Guide {
    @SerializedName("duration")
    private String duration;

    @SerializedName("instructions")
    private String instructions;

    @SerializedName("pointIndex")
    private String pointIndex;

    @SerializedName("distance")
    private String distance;

    @SerializedName("type")
    private String type;

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    public String getInstructions ()
    {
        return instructions;
    }

    public void setInstructions (String instructions)
    {
        this.instructions = instructions;
    }

    public String getPointIndex ()
    {
        return pointIndex;
    }

    public void setPointIndex (String pointIndex)
    {
        this.pointIndex = pointIndex;
    }

    public String getDistance ()
    {
        return distance;
    }

    public void setDistance (String distance)
    {
        this.distance = distance;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [duration = "+duration+", instructions = "+instructions+", pointIndex = "+pointIndex+", distance = "+distance+", type = "+type+"]";
    }
}
