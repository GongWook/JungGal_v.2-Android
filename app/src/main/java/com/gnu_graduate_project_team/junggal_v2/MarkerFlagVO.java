package com.gnu_graduate_project_team.junggal_v2;

import java.io.Serializable;
import java.util.Arrays;

public class MarkerFlagVO implements Serializable {

    private boolean[] filteringFlag = new boolean[9];

    public MarkerFlagVO(boolean[] filteringFlag) {
        this.filteringFlag = filteringFlag;
    }

    public boolean[] getFilteringFlag() {
        return filteringFlag;
    }

    public void setFilteringFlag(boolean[] filteringFlag) {
        this.filteringFlag = filteringFlag;
    }

    @Override
    public String toString() {
        return "MarkerFlagVO{" +
                "filteringFlag=" + Arrays.toString(filteringFlag) +
                '}';
    }
}
