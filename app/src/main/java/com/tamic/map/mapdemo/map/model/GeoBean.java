package com.tamic.map.mapdemo.map.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;



public class GeoBean implements Parcelable {

    public static final Creator<GeoBean> CREATOR = new Creator<GeoBean>() {

        @Override
        public GeoBean createFromParcel(Parcel source) {
            GeoBean bean = new GeoBean();
            bean.lat = source.readString();
            bean.lng = source.readString();
            return bean;
        }

        @Override
        public GeoBean[] newArray(int size) {
            return new GeoBean[size];
        }
    };
    private String lat;
    private String lng;

    public GeoBean() {
    }

    public GeoBean(String lng, String lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public static boolean isValid(GeoBean tempBean) {
        return !(null == tempBean || TextUtils.isEmpty(tempBean.getLat()) || "0".equals(tempBean.getLat())
                || TextUtils.isEmpty(tempBean.getLng()) || "0".equals(tempBean.getLng()));
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "GeoBean [lat=" + lat + ", lng=" + lng + "]";
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lat);
        dest.writeString(lng);
    }

}
