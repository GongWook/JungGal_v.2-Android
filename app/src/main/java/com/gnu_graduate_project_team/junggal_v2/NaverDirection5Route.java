package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class NaverDirection5Route {
    @SerializedName("trafast")
    private NaverDirection5Trafast[] naverDirection5Trafast;

    public NaverDirection5Trafast[] getTrafast ()
    {
        return naverDirection5Trafast;
    }

    public void setTrafast (NaverDirection5Trafast[] naverDirection5Trafast)
    {
        this.naverDirection5Trafast = naverDirection5Trafast;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [trafast = "+ naverDirection5Trafast +"]";
    }
}
