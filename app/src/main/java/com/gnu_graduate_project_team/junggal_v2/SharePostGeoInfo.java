package com.gnu_graduate_project_team.junggal_v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.naver.maps.geometry.LatLng;

/** 나눔 게시물 작성시 게시물 좌표와 위치명 VO 객체
 *  화면 전환 시 위의 정보를 제공하기 위한 Class **/

public class SharePostGeoInfo implements Parcelable {

    private String geoRegion;
    private LatLng latLng;

    public SharePostGeoInfo(String geoRegion, LatLng latLng) {
        this.geoRegion = geoRegion;
        this.latLng = latLng;
    }

    protected SharePostGeoInfo(Parcel in) {
        geoRegion = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(geoRegion);
        dest.writeParcelable(latLng, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SharePostGeoInfo> CREATOR = new Creator<SharePostGeoInfo>() {
        @Override
        public SharePostGeoInfo createFromParcel(Parcel in) {
            return new SharePostGeoInfo(in);
        }

        @Override
        public SharePostGeoInfo[] newArray(int size) {
            return new SharePostGeoInfo[size];
        }
    };

    public String getGeoRegion() {
        return geoRegion;
    }

    public void setGeoRegion(String geoRegion) {
        this.geoRegion = geoRegion;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
