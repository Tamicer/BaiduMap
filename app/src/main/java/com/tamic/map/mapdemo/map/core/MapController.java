package com.tamic.map.mapdemo.map.core;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.tamic.map.mapdemo.map.MapConstant;
import com.tamic.map.mapdemo.map.MapUtils;
import com.tamic.map.mapdemo.map.layer.MapCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import io.reactivex.annotations.NonNull;


/**
 * Created by map on 2017-04-07.
 */
public class MapController implements BaiduMap.OnMapLoadedCallback, BaiduMap.OnMapStatusChangeListener, BaiduMap.OnMarkerClickListener, BaiduMap.OnMapClickListener, BaiduMap.OnPolylineClickListener {

    public static final int MSG_LOAD_NEEDED = 1;
    public static final int MSG_MARKER_CLICKED = 2;
    public static final int MSG_MAP_CLICKED = 3;
    public static final int UPDATE_MODE_AUTO_LOAD = 0;
    public static final int UPDATE_MODE_NO_LOAD = 1;
    public static final int UPDATE_MODE_FORCE_LOAD = 2;

    private final Context mContext;
    private final Handler mHandler;
    private final MapUtils mMapUtils;
    private final BaiduMap mMap;
    private final UiSettings mSetting;
    private MapCase mMapCase;
    private boolean mLoaded = false;
    private LatLng mCornerLoc = null;
    private int mZoomStatus = -1;
    private List<Overlay> mOverlayList = new ArrayList<>();
    private List<Overlay> mOverlayIntersections = new ArrayList<>();
    private List<Overlay> mOverlayDifferences = new ArrayList<>();
    private HashSet<Overlay> mOverlayMaps = new HashSet<>();
    private HashMap<Integer, OverlayOptions> mOverlayOptions = new HashMap<>();
    private Overlay mCircleOverlay;
    private Overlay mCenterOverlay;
    private View view;

    public MapController(Context context, @NonNull BaiduMap map, Handler handler) {
        mContext = context;
        mMapUtils = MapUtils.getInstance(context);
        mHandler = handler;
        mMap = map;
        mMap.setOnMapLoadedCallback(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnPolylineClickListener(this);
        mSetting = mMap.getUiSettings();
        mSetting.setRotateGesturesEnabled(false);
        mSetting.setOverlookingGesturesEnabled(false);
        mSetting.setCompassEnabled(false);
        mSetting.setScrollGesturesEnabled(true);
        mSetting.setZoomGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);

    }

    public MapStatus getStatus() {
        return mMap.getMapStatus();
    }

    public MapCase getMapCase() {
        return mMapCase;
    }

    public void setMapCase(@NonNull MapCase mMapCase) {
        this.mMapCase = mMapCase;
    }

    public void setScrollEnabled(boolean enabled) {
        mSetting.setScrollGesturesEnabled(enabled);
    }

    public void updateStatus(MapStatusUpdate statusUpdate) {
        updateStatus(statusUpdate, UPDATE_MODE_AUTO_LOAD);
    }

    public void updateStatus(MapStatusUpdate statusUpdate, int mode) {
        mMap.setMapStatus(statusUpdate);
        if (mLoaded) {
            processMapStatusChange(getStatus(), mode);
        } else {
            mZoomStatus = getZoomStatus(getStatus());
        }
    }

    public void addMarkers(List<OverlayOptions> markers) {
        if(mOverlayList != null && mOverlayList.size() > 0) {
            for (Overlay overlay : mOverlayList) {
                overlay.remove();
            }
        }

        if (mMapCase != null) {
            mMapCase.reMoveMarks();
        }
        //mMap.clear();
        mOverlayList.clear();
        if(markers != null) {
            mOverlayList.addAll(mMap.addOverlays(markers));
        }
        mMapCase.addMark();
    }

    public void addMarker(OverlayOptions marker) {
        if (mOverlayOptions.containsKey(marker.hashCode())) {
            return;
        }
        mOverlayOptions.put(marker.hashCode(), marker);
        mOverlayMaps.add(mMap.addOverlay(marker));
        mMapCase.addMark();
    }


    public void addSpecificMarkers(OverlayOptions marker) {
        mOverlayMaps.remove(marker.hashCode());
        mOverlayMaps.add(mMap.addOverlay(marker));
    }

   /* private List<T> intersection(List<T> markers1, List<T> markers2) {
        List<T> result = new ArrayList<>();
        result.addAll(markers1);
        result.retainAll(markers2);
        return result;
    }*/

    public List<OverlayOptions> difference(List<OverlayOptions> markers1, List<OverlayOptions> markers2) {
        List<OverlayOptions> result = new ArrayList<>();
        result.addAll(markers1);
        result.removeAll(markers2);
        return result;
    }

    public List<OverlayOptions> sum(List<OverlayOptions> markers1, List<OverlayOptions> markers2) {
        List<OverlayOptions> result = new ArrayList<>();
        result.addAll(markers1);
        result.addAll(markers2);
        return result;
    }


    @Override
    public void onMapLoaded() {
        mLoaded = true;
        mSetting.setScrollGesturesEnabled(true);
        mMap.setOnMapStatusChangeListener(this);
        mCornerLoc = getCornerLoc();
        mZoomStatus = getZoomStatus(getStatus());
    }


    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        processMapStatusChange(mapStatus, UPDATE_MODE_AUTO_LOAD);//百度地图拖动和层级缩放控制
    }

    private LatLng getCornerLoc() {
        return mMap.getProjection().fromScreenLocation(new Point());
    }

    public int getZoomStatus() {
        return mZoomStatus;
    }

    private int getZoomStatus(MapStatus mapStatus) {
        return mMapUtils.getCurrentZoomStatus(mapStatus.zoom);
    }

    private boolean needUpdate(int zoomStatus, double distance) {
        if (mZoomStatus != zoomStatus) {
            return true;
        }
        switch (zoomStatus) {
            case MapConstant.ZOOM_STATUS_REGION:
                return distance > 4000;
            case MapConstant.ZOOM_STATUS_SECTOR:
                return distance > 2000;
            case MapConstant.ZOOM_STATUS_COMMUNITY_AHEAD:
                return distance > 800;
            case MapConstant.ZOOM_STATUS_COMMUNITY:
                return distance > 400;
            default:
                return false;
        }
    }

    private void processMapStatusChange(MapStatus mapStatus, int mode) {

        boolean update = false;
        LatLng cornerLoc = getCornerLoc();
        int zoomStatus = getZoomStatus(mapStatus);
        double distance = DistanceUtil.getDistance(cornerLoc, mCornerLoc);
        if (mode == UPDATE_MODE_AUTO_LOAD) {
            update = needUpdate(zoomStatus, distance);
        } else if (mode == UPDATE_MODE_FORCE_LOAD) {
            update = true;
        } else if (mode == UPDATE_MODE_NO_LOAD) {
            update = false;
        }
        mZoomStatus = zoomStatus;
        if (update) {
            mCornerLoc = cornerLoc;
            Message message = Message.obtain(mHandler, MSG_LOAD_NEEDED, zoomStatus, 0, mapStatus.target);
            mHandler.removeMessages(MSG_LOAD_NEEDED);
            mHandler.sendMessageDelayed(message, 100);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Message message = Message.obtain(mHandler, MSG_MARKER_CLICKED, marker);
        mHandler.sendMessage(message);
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Message message = Message.obtain(mHandler, MSG_MAP_CLICKED);
        mHandler.sendMessage(message);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        if (mMapCase != null) {
            mMapCase.clickPoi();
        }
        return false;
    }

    public List<Overlay> getOverlays() {
        return mOverlayList;
    }

    public void enableMyLocation(LatLng loc) {

        mMap.setMyLocationEnabled(true);
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(0) //半径长度，单位m
                .direction(0) // 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(loc.latitude)
                .longitude(loc.longitude)
                .build();
        mMap.setMyLocationData(locData);
        mMap.setMyLocationConfigeration(
                new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL,
                        true,
                        BitmapDescriptorFactory.fromResource(R.drawable.loc_marker)));
    }

    public Projection getProjection() {
        return mMap.getProjection();
    }


    public void enableNearbyCircle(LatLng loc, int radius) {


        OverlayOptions centerOptions = null;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_map_nearby_center, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        try {
            centerOptions =
                    new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromView(view))
                            .position(loc);
        } catch (Exception e) {
            return;
        }

        CircleOptions circleOptions =
                new CircleOptions()
                        .center(loc)
                        .radius(radius)
                        .fillColor(0x33ff8888)
                        .stroke(new Stroke(1, 0xff979797));
        if (mCircleOverlay != null) {
            mCircleOverlay.remove();
        }
        if (mCenterOverlay != null) {
            mCenterOverlay.remove();
        }
        mCenterOverlay = mMap.addOverlay(centerOptions);
        mCircleOverlay = mMap.addOverlay(circleOptions);
    }

    @Override
    public boolean onPolylineClick(Polyline polyline) {
        return false;
    }


    public void onDestroy() {
        mMap.clear();
        mOverlayList.clear();
        mOverlayMaps.clear();
        mOverlayDifferences.clear();
        mOverlayIntersections.clear();

    }
}
