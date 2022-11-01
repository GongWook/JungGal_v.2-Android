package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class Start {
    @SerializedName("location")
    private String[] location;

    public String[] getLocation ()
    {
        return location;
    }

    public void setLocation (String[] location)
    {
        this.location = location;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [location = "+location+"]";
    }
}
