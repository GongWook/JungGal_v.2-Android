package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class NaverDirection5Trafast {
    @SerializedName("summary")
    private NaverDirection5Summary naverDirection5Summary;

    @SerializedName("path")
    private Double[][] path;

    @SerializedName("section")
    private NaverDirection5Section[] naverDirection5Section;

    @SerializedName("guide")
    private NaverDirection5Guide[] naverDirection5Guide;

    public NaverDirection5Summary getSummary ()
    {
        return naverDirection5Summary;
    }

    public void setSummary (NaverDirection5Summary naverDirection5Summary)
    {
        this.naverDirection5Summary = naverDirection5Summary;
    }

    public Double[][] getPath ()
    {
        return path;
    }

    public void setPath (Double[][] path)
    {
        this.path = path;
    }

    public NaverDirection5Section[] getSection ()
    {
        return naverDirection5Section;
    }

    public void setSection (NaverDirection5Section[] naverDirection5Section)
    {
        this.naverDirection5Section = naverDirection5Section;
    }

    public NaverDirection5Guide[] getGuide ()
    {
        return naverDirection5Guide;
    }

    public void setGuide (NaverDirection5Guide[] naverDirection5Guide)
    {
        this.naverDirection5Guide = naverDirection5Guide;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [summary = "+ naverDirection5Summary +", path = "+path+", section = "+ naverDirection5Section +", guide = "+ naverDirection5Guide +"]";
    }
}
