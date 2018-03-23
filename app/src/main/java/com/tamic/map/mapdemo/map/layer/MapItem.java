package com.tamic.map.mapdemo.map.layer;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Tamic 2017/11/14.
 */

public class MapItem implements Parcelable {

    public int id;
    public String name;
    public int count;
    public int hz_count;
    public double lat;
    public double lng;
    public String address;
    public String price;
    private int saleStatus;
    public String block;
    public int is_hz;

    public MapItem() {
    }


    protected MapItem(Parcel in) {
        id = in.readInt();
        name = in.readString();
        count = in.readInt();
        hz_count = in.readInt();
        lat = in.readDouble();
        lng = in.readDouble();
        address = in.readString();
        price = in.readString();
        saleStatus = in.readInt();
        block = in.readString();
        is_hz = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(count);
        dest.writeInt(hz_count);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(address);
        dest.writeString(price);
        dest.writeInt(saleStatus);
        dest.writeString(block);
        dest.writeInt(is_hz);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MapItem> CREATOR = new Creator<MapItem>() {

        @Override
        public MapItem createFromParcel(Parcel in) {
            return new MapItem(in);
        }

        @Override
        public MapItem[] newArray(int size) {
            return new MapItem[size];
        }
    };

    public boolean isHz() {
        return is_hz == 1;
    }

    public boolean isCountNull() {
        return count <= 0 ;
    }



}
