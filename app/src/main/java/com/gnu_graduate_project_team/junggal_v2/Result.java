package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("region")
    private Region region;

    @SerializedName("land")
    private Land land;

    public Region getRegion() {
        return region;
    }
    public void setRegion(Region region) {
    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }
}
