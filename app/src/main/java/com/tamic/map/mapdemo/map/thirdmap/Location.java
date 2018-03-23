package com.tamic.map.mapdemo.map.thirdmap;

public class Location {

    //经度
    double longitude;
    //纬度
    double latitude;

    /**
     * @param longitude 经度
     * @param latitude  纬度
     */
    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}