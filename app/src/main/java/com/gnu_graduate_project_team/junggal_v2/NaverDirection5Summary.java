package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class NaverDirection5Summary {
    @SerializedName("duration")
    private String duration;

    @SerializedName("goal")
    private NaverDirection5Goal naverDirection5Goal;

    @SerializedName("tollFare")
    private String tollFare;

    @SerializedName("distance")
    private String distance;

    @SerializedName("bbox")
    private String[][] bbox;

    @SerializedName("start")
    private Start start;

    @SerializedName("fuelPrice")
    private String fuelPrice;

    @SerializedName("taxiFare")
    private String taxiFare;

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    public NaverDirection5Goal getGoal ()
    {
        return naverDirection5Goal;
    }

    public void setGoal (NaverDirection5Goal naverDirection5Goal)
    {
        this.naverDirection5Goal = naverDirection5Goal;
    }

    public String getTollFare ()
    {
        return tollFare;
    }

    public void setTollFare (String tollFare)
    {
        this.tollFare = tollFare;
    }

    public String getDistance ()
    {
        return distance;
    }

    public void setDistance (String distance)
    {
        this.distance = distance;
    }

    public String[][] getBbox ()
    {
        return bbox;
    }

    public void setBbox (String[][] bbox)
    {
        this.bbox = bbox;
    }

    public Start getStart ()
    {
        return start;
    }

    public void setStart (Start start)
    {
        this.start = start;
    }

    public String getFuelPrice ()
    {
        return fuelPrice;
    }

    public void setFuelPrice (String fuelPrice)
    {
        this.fuelPrice = fuelPrice;
    }

    public String getTaxiFare ()
    {
        return taxiFare;
    }

    public void setTaxiFare (String taxiFare)
    {
        this.taxiFare = taxiFare;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [duration = "+duration+", goal = "+ naverDirection5Goal +", tollFare = "+tollFare+", distance = "+distance+", bbox = "+bbox+", start = "+start+", fuelPrice = "+fuelPrice+", taxiFare = "+taxiFare+"]";
    }
}
