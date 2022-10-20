package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class NaverDirection5Goal {
    @SerializedName("location")
    private String[] location;

    @SerializedName("dir")
    private String dir;

    public String[] getLocation ()
    {
        return location;
    }

    public void setLocation (String[] location)
    {
        this.location = location;
    }

    public String getDir ()
    {
        return dir;
    }

    public void setDir (String dir)
    {
        this.dir = dir;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [location = "+location+", dir = "+dir+"]";
    }
}
