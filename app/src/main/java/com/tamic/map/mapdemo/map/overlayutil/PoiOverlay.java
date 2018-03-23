package com.tamic.map.mapdemo.map.overlayutil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiResult;
import com.pinganfang.haofangtuo.api.pub.HousingEstateBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 用于显示poi的overly
 */
public class PoiOverlay extends OverlayManager {

    private static final int MAX_POI_SIZE = 10;

    private PoiResult mPoiResult;

    private BitmapDrawable icon;
    private ArrayList<HousingEstateBean> mMyXqData;
    private boolean isMyXqData;
    //private Context context;


    /**
     * 构造函数
     *
     * @param baiduMap 该 PoiOverlay 引用的 BaiduMap 对象
     */
    public PoiOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    /**
     * 设置POI数据
     *
     * @param poiResult 设置POI数据
     */
    public void setData(PoiResult poiResult) {
        this.mPoiResult = poiResult;
    }

    @Override
    public List<OverlayOptions> getOverlayOptions() {
        if (isMyXqData) {
            if (mMyXqData == null) {
                return null;
            }
            List<OverlayOptions> markerList = new ArrayList<OverlayOptions>();
            int markerSize = 0;
            for (int i = 0; i < mMyXqData.size()
                    && markerSize < MAX_POI_SIZE; i++) {
                if (mMyXqData.get(i).getGeo() == null) {
                    continue;
                }
                markerSize++;
                Bundle bundle = new Bundle();
                bundle.putInt("index", i);
                bundle.putBoolean("isMyXqData", isMyXqData);
                markerList.add(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(setNumToIcon(i + 1))).extraInfo(bundle)
                        .position(new LatLng(Double.parseDouble(mMyXqData.get(i).getGeo().getLat()), Double.parseDouble(mMyXqData.get(i).getGeo().getLng()))));

            }
            return markerList;
        } else {
            if (mPoiResult == null || mPoiResult.getAllPoi() == null) {
                return null;
            }
            List<OverlayOptions> markerList = new ArrayList<OverlayOptions>();
            int markerSize = 0;
            for (int i = 0; i < mPoiResult.getAllPoi().size()
                    && markerSize < MAX_POI_SIZE; i++) {
                if (mPoiResult.getAllPoi().get(i).location == null) {
                    continue;
                }
                markerSize++;
                Bundle bundle = new Bundle();
                bundle.putInt("index", i);
                bundle.putBoolean("isMyXqData", isMyXqData);
                markerList.add(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(setNumToIcon(i + 1))).extraInfo(bundle)
                        .position(mPoiResult.getAllPoi().get(i).location));

            }
            return markerList;
        }
    }

    public BitmapDrawable getBitmapDescriptor() {
        return icon;
    }

    public void setBitmapDescriptor(BitmapDrawable icon) {
        this.icon = icon;
    }

    /**
     * 获取该 PoiOverlay 的 poi数据
     *
     * @return
     */
    public PoiResult getPoiResult() {
        return mPoiResult;
    }

    public ArrayList<HousingEstateBean> getMyXqData() {
        return mMyXqData;
    }

    /**
     * 覆写此方法以改变默认点击行为
     *
     * @param i 被点击的poi在
     *          {@link PoiResult#getAllPoi()} 中的索引
     * @return
     */
    public boolean onPoiClick(int i) {
//        if (mPoiResult.getAllPoi() != null
//                && mPoiResult.getAllPoi().get(i) != null) {
//            Toast.makeText(BMapManager.getInstance().getContext(),
//                    mPoiResult.getAllPoi().get(i).name, Toast.LENGTH_LONG)
//                    .show();
//        }
        return false;
    }

    public boolean onMyXqMarkerClick(int i) {
//        if (mPoiResult.getAllPoi() != null
//                && mPoiResult.getAllPoi().get(i) != null) {
//            Toast.makeText(BMapManager.getInstance().getContext(),
//                    mPoiResult.getAllPoi().get(i).name, Toast.LENGTH_LONG)
//                    .show();
//        }
        return false;
    }

    @Override
    public final boolean onMarkerClick(Marker marker) {
        if (!getOverlays().contains(marker)) {
            return false;
        }
        if (marker.getExtraInfo() != null) {
            return marker.getExtraInfo().getBoolean("isMyXqData") ? onMyXqMarkerClick(marker.getExtraInfo().getInt("index")) : onPoiClick(marker.getExtraInfo().getInt("index"));
        }
        return false;
    }

    @Override
    public boolean onPolylineClick(Polyline polyline) {
        // TODO Auto-generated method stub
        return false;
    }

    public void setMyXqData(ArrayList<HousingEstateBean> myXqData, boolean isMyXqData) {
        this.isMyXqData = isMyXqData;
        this.mMyXqData = myXqData;
    }

    /**
     * 往图片添加数字
     */
    private Bitmap setNumToIcon(int num) {
        if (icon == null) {
            return null;
        }

        Bitmap bitmap = icon.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        int widthX;
        int heightY = 0;
        if (num < 10) {
            paint.setTextSize(30);
            widthX = 8;
            heightY = 6;
        } else {
            paint.setTextSize(20);
            widthX = 11;
        }

        canvas.drawText(String.valueOf(num),
                ((bitmap.getWidth() / 2) - widthX),
                ((bitmap.getHeight() / 2) + heightY), paint);
        return bitmap;
    }
}
