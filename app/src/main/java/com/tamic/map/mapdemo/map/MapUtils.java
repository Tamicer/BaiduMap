package com.tamic.map.mapdemo.map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.tamic.map.mapdemo.map.model.GeoBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static android.view.View.VISIBLE;


/**
 * 地图工具类
 */
public class MapUtils {

    private static MapUtils instance;
    private LocationClient locationClient;
    private OnLocationListener listener;
    private boolean reTry = false;
    private boolean isReturn = false;
    private RoutePlanSearch mRoutePlanSearch;
    private PoiSearch mPoiSearch;
    private Context context;
    /**
     * 用户当前位置定位回调
     */
    private BDLocationListener locationListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (null != bdLocation) {
                Log.d("User location longitude", bdLocation.getLongitude()+"");
                Log.d("User location la titude",
                         bdLocation.getLatitude() +"");
                Log.d("User location city : ", bdLocation.getCity());
            } else {
                if (!reTry) {
                    reTry = true;
                    if (null != locationClient && locationClient.isStarted()) {
                        locationClient.requestLocation();
                    }
                    return;
                }
            }
            if (null != listener && !isReturn) {  //isReturn标志位防止部分机型一次定位请求多次回调的问题
                isReturn = true;
                listener.onReceiveLocation(bdLocation);
            }

            if (null != locationClient) {
                if (locationClient.isStarted()) {
                    locationClient.stop();
                }

                locationClient = null;
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    public MapUtils(Context context) {
        this.context = context;
        // initLocClient(context);
    }

    public static MapUtils getInstance(Context context) {
        if (null == instance) {
            instance = new MapUtils(context);
        }
        return instance;
    }

    public static int getRadiusByZoomStatus(int zoomStatus) {
        switch (zoomStatus) {
            case MapConstant.ZOOM_STATUS_REGION:
                return MapConstant.RADIUS_REGION;
            case MapConstant.ZOOM_STATUS_SECTOR:
                return MapConstant.RADIUS_SECTOR;
            case MapConstant.ZOOM_STATUS_COMMUNITY_AHEAD:
                return MapConstant.RADIUS_COMMUNITY_AHEAD;
            case MapConstant.ZOOM_STATUS_COMMUNITY:
                return MapConstant.RADIUS_COMMUNITY;
            default:
                return 0;
        }
    }

    public static float zoomStatusToDefaultZoom(int zoomStatus) {
        switch (zoomStatus) {
            case MapConstant.ZOOM_STATUS_REGION:
                return MapConstant.DEFAULT_ZOOM_LEVEL_REGION;
            case MapConstant.ZOOM_STATUS_SECTOR:
                return MapConstant.DEFAULT_ZOOM_LEVEL_SECTOR;
            case MapConstant.ZOOM_STATUS_COMMUNITY:
                return MapConstant.DEFAULT_ZOOM_LEVEL_COMMUNITY;
            default:
                return MapConstant.DEFAULT_ZOOM_LEVEL_REGION;
        }
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    private void initLocClient(Context context) {
        locationClient = new LocationClient(context);
        locationClient.registerLocationListener(locationListener);
        LocationClientOption options = new LocationClientOption();
        options.setOpenGps(true);
        options.setScanSpan(0);
        options.setCoorType("bd09ll");
        options.setAddrType("all"); // 返回的定位结果包含地址信息
//        options.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
        options.setIsNeedAddress(true);         // 设置是否需要地址信息
        options.setIsNeedLocationPoiList(true);//设置是否需要返回位置POI信息，可以在BDLocation.getPoiList()中得到数据
        locationClient.setLocOption(options);
    }

    /**
     * 请求定位
     *
     * @param context
     * @param listener
     */
    public synchronized void requestLocation(Context context, final OnLocationListener listener) {
        this.listener = listener;
        LocationSource
                .create(context)
                .subscribeNewLocation()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BDLocation>() {

                    @Override
                    public void accept(BDLocation bdLocation) throws Exception {

                        if (bdLocation != null) {
                            if (listener != null) {
                                listener.onReceiveLocation(bdLocation);
                            }
                        }
                    }
                });
    }

    /**
     * 计算两个点的距离
     *
     * @param lat1 点1的纬度
     * @param lng1 点1的经度
     * @param lat2 点2的纬度
     * @param lng2 点2的纬度
     * @return 两点距离，单位为： 米,转换错误时返回-1.
     */
    public double getDistance(double lat1, double lng1, double lat2, double lng2) {
        return DistanceUtil.getDistance(new LatLng(lat1, lng1), new LatLng(lat2, lng2));
    }

    /**
     * 获取地图静态图片网络地址。 API:http://developer.baidu.com/map/static-1.htm
     *
     * @param width  图片宽度。取值范围：(0, 1024]
     * @param height 图片高度。取值范围：(0, 1024]
     * @param geo    地图中心点位置，参数可以为经纬度坐标或名称。坐标格式：lng<经度>，lat<纬度>，例如116.43213,38.76623。
     * @param zoom   地图级别。高清图范围[3, 18]；低清图范围[3,19]
     * @return
     */
    public String getMapStaticImageUrl(int width, int height, GeoBean geo, int zoom) {
        width = width > MapConstant.STATIC_IMAGE_MAX_SIZE ? MapConstant.STATIC_IMAGE_MAX_SIZE : width;
        height = height > MapConstant.STATIC_IMAGE_MAX_SIZE ? MapConstant.STATIC_IMAGE_MAX_SIZE : height;

        StringBuilder params = new StringBuilder();
        if (width > 1024) {
            height = 1024 * (height / width);
            width = 1024;
        }
        if (width != 0) {
            params.append("width=").append(width);
        }

        if (height != 0) {
            params.append("&height=").append(height);
        }

        if (null != geo) {
            params.append("&center=").append(geo.getLng().trim()).append(",").append(geo.getLat().trim());
        }

        if (zoom > 0 && zoom <= 19) {
            params.append("&zoom=").append(zoom);
        }

        String url = MapConstant.STATIC_IMAGE_URL + "?" + params.toString();
        DevUtil.v("Eva", "========get static image url = " + url + "========");
        return url;
    }

    /**
     * 获取地图静态图片网络地址
     *
     * @param width  图片宽度。取值范围：(0, 1024]
     * @param height 图片高度。取值范围：(0, 1024]
     * @param lng    经度
     * @param lat    纬度
     * @param zoom   地图级别。高清图范围[3, 18]；低清图范围[3,19]
     * @return 静态图片网络地址
     */
    public String getMapStaticImageUrl(int width, int height, double lng, double lat, int zoom) {
        StringBuilder center = new StringBuilder();
        center.append(String.valueOf(lng)).append(",").append(String.valueOf(lat));
        return getMapStaticImageUrl(width, height, new GeoBean(String.valueOf(lng), String.valueOf(lat)), zoom);
    }

    public String getMapStaticImageUrlWithImg(int width, int height, ArrayList<GeoBean> geos, int zoom) {
        width = width > MapConstant.STATIC_IMAGE_MAX_SIZE ? MapConstant.STATIC_IMAGE_MAX_SIZE : width;
        height = height > MapConstant.STATIC_IMAGE_MAX_SIZE ? MapConstant.STATIC_IMAGE_MAX_SIZE : height;
        StringBuilder params = new StringBuilder();
        if (width > 1024) {
            height = 1024 * (height / width);
            width = 1024;
        }
        if (width != 0) {
            params.append("width=").append(width);
        }

        if (height != 0) {
            params.append("&height=").append(height);
        }

        if (null != geos && geos.size() > 0) {
            GeoBean geo = geos.get(geos.size() / 2);
            if (geo != null) {
                params.append("&center=").append(geo.getLng()).append(",").append(geo.getLat());
            }
            if (zoom > 0 && zoom <= 19) {
                params.append("&zoom=").append(zoom);
            }
            params.append("&markers=");
            for (Iterator<GeoBean> iterator = geos.iterator(); iterator.hasNext(); ) {
                GeoBean geoBean = iterator.next();
                if (geoBean != null) {
                    params.append(geoBean.getLng()).append(",").append(geoBean.getLat());
                }
                if (iterator.hasNext()) {
                    params.append("|");
                }
            }
            params.append("&markerStyles=-1,http://static.anhouse.com/img/mobile/app/map/map_main.png");
        }
        String url = MapConstant.STATIC_IMAGE_URL + "?" + params.toString();
        Log.v("dale_liu", "========get static image url = " + url + "========");

        return url;
    }

    /**
     * 获取地理位置周边信息
     *
     * @param context
     * @param searchType 搜索类型
     * @param lat        纬度
     * @param lng        经度
     * @param radius     搜索范围
     * @param listener   结果返回监听
     */
    public void getNearby(Context context, PoiInfo.POITYPE searchType, double lat, double lng, int radius,
                          final OnMapSearchListener<PoiInfo> listener) {
        Log.d("tag", "getNearby(" + lat + ", " + lng + ")");
        getNearby(context, searchType, buildLatLng(lat, lng), radius, listener);
    }

    /**
     * 获取地理位置周边信息
     *
     * @param context
     * @param searchType 搜索类型
     * @param latLng     地理位置
     * @param radius     搜索范围
     * @param listener   结果返回监听
     */
    public void getNearby(Context context, PoiInfo.POITYPE searchType, LatLng latLng, int radius,
                          final OnMapSearchListener<PoiInfo> listener) {
        String key = "";
        if (searchType == PoiInfo.POITYPE.SUBWAY_STATION) {
            key = context.getString(R.string.metro_station);
        } else if (searchType == PoiInfo.POITYPE.BUS_STATION) {
            key = context.getString(R.string.bus_station);
        }
        getNearby(context, searchType, key, latLng, radius, listener);
    }

    /**
     * 获取地理位置周边信息
     *
     * @param context
     * @param searchType 查找类型
     * @param key        查找关键字
     * @param latLng     地理位置
     * @param radius     查找半径，单位：米
     * @param listener   查找结果监听
     */
    private void getNearby(Context context, final PoiInfo.POITYPE searchType, String key, LatLng latLng,
                           int radius, final OnMapSearchListener<PoiInfo> listener) {
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

            @Override
            public void onGetPoiResult(PoiResult result) {
                DevUtil.v("Eva", "onGetPoiResult : error = " + result.error);

                if ((result.error != SearchResult.ERRORNO.NO_ERROR || null == result.getAllPoi()) && null != listener) {
                    listener.onComplete(null, result.error);
                    return;
                }

                List<PoiInfo> list = new ArrayList<PoiInfo>();
                for (PoiInfo info : result.getAllPoi()) {
                    if (info.type == searchType) {
                        list.add(info);
                    }
                }

                if (null != listener) {
                    listener.onComplete(list, result.error);
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }

        });
        mPoiSearch.searchNearby(new PoiNearbySearchOption()
                .keyword(key)
                .location(latLng)
                .radius(radius)
        );
    }

    /**
     * 构造地理坐标类，存放经度和纬度，以微度的整数形式存储
     *
     * @param lat 纬度
     * @param lng 经度
     * @return
     */
    public LatLng buildLatLng(double lat, double lng) {
        return new LatLng(lat, lng);
    }

    public void planRoute(final MapConstant.PlanRouteType type, final String startCity,
                          final String endCity, final LatLng startPt, final LatLng endPt, final OnPlanRouteListener listener) {
        DevUtil.v("Eva", "=====plan route : type = " + type + ", startCity = " + startCity + "=====");
        mRoutePlanSearch = RoutePlanSearch.newInstance();
        mRoutePlanSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {

            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                if (listener != null) {
                    listener.onGetWalkingRouteResult(type, walkingRouteResult, walkingRouteResult.error);
                }
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                if (listener != null) {
                    listener.onGetTransitRouteResult(type, transitRouteResult, transitRouteResult.error);
                }
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                if (listener != null) {
                    listener.onGetDrivingRouteResult(type, drivingRouteResult, drivingRouteResult.error);
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });
        PlanNode startPlanNode = PlanNode.withLocation(startPt);
        PlanNode endPlanNode = PlanNode.withLocation(endPt);
        switch (type) {
            case DRIVING:
                mRoutePlanSearch.drivingSearch(new DrivingRoutePlanOption().from(startPlanNode).to(endPlanNode).policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST));
                break;
            case WALKING:
                mRoutePlanSearch.walkingSearch(new WalkingRoutePlanOption().from(startPlanNode).to(endPlanNode));
                break;
            case BUS:
                mRoutePlanSearch.transitSearch(new TransitRoutePlanOption().from(startPlanNode).to(endPlanNode).city(startCity));
                break;
        }

    }

    /**
     * 规划路径
     *
     * @param type
     * @param startCity
     * @param endCity
     * @param startLat
     * @param startLng
     * @param endLat
     * @param endLng
     */
    public void planRoute(final PlanRouteType type, final String startCity,
                          final String endCity, final double startLat, final double startLng, final double endLat,
                          final double endLng, final OnPlanRouteListener listener) {
        planRoute(type, startCity, endCity, buildLatLng(startLat, startLng), buildLatLng(endLat, endLng),
                listener);

    }

    /**
     * 将地图当前位置移动到指定位置
     *
     * @param cenpt 地图中心点坐标
     * @param zoom  缩放级别
     */
    public void updateLocationPosition(BaiduMap baiduMap, LatLng cenpt, float zoom) {
        // 定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(zoom)
                .build();
        // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        // 改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);
    }

    /**
     * 获取当前显示级别
     */
    public int getCurrentZoomStatus(float zoomLevel) {
        if (zoomLevel < MapConstant.MAX_ZOOM_STATUS_REGION) {
            return MapConstant.ZOOM_STATUS_REGION;
        } else if (zoomLevel < MapConstant.MAX_ZOOM_STATUS_SECTOR && zoomLevel >= MapConstant.MAX_ZOOM_STATUS_REGION) {
            return MapConstant.ZOOM_STATUS_SECTOR;
        } else if (zoomLevel < MapConstant.MAX_ZOOM_STATUS_COMMUNITY && zoomLevel >= MapConstant.MAX_ZOOM_STATUS_SECTOR) {
            return MapConstant.ZOOM_STATUS_COMMUNITY_AHEAD;
        } else if (zoomLevel >= MapConstant.MAX_ZOOM_STATUS_SECTOR) {
            return MapConstant.ZOOM_STATUS_COMMUNITY;
        } else {
            return MapConstant.ZOOM_STATUS_REGION;
        }
    }

    public int zoomStatusToLevel(int zoomStatus) {
        switch (zoomStatus) {
            case MapConstant.ZOOM_STATUS_REGION:
                return MapConstant.LEVEL_REGION;
            case MapConstant.ZOOM_STATUS_SECTOR:
                return MapConstant.LEVEL_BLOCK;
            case MapConstant.ZOOM_STATUS_COMMUNITY_AHEAD:
                return MapConstant.LEVEL_NEW;
            case MapConstant.ZOOM_STATUS_COMMUNITY:
                return MapConstant.LEVEL_COMMUNITY;
            default:
                return 0;
        }
    }

    public void destroy() {
        if (null != mPoiSearch) {
            mPoiSearch.destroy();
        }
        if (null != mRoutePlanSearch) {
            mRoutePlanSearch.destroy();
        }

    }

    /**
     * add dushiguang
     * 根据经纬度获取地址
     *
     * @param longitude--经度
     * @param latitude--纬度
     * @return
     */
    public void getAddress(String longitude, String latitude, OnGetAddressListener addressListener) {
        final OnGetAddressListener getAddressListener = addressListener;

        // 创建地理编码检索实例
        GeoCoder geoCoder = GeoCoder.newInstance();
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                } else {
                    if (getAddressListener != null) {
                        getAddressListener.onReceiveAddress(result.getAddress());
                    }
                }
            }

            // 地理编码查询结果回调函数
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                }
            }
        };
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))));

    }



    /**
     * 路径规划监听
     *
     * @author EvaGu
     */
    public interface OnPlanRouteListener {

        /**
         * 自驾
         *
         * @param result
         * @param error
         */
        void onGetDrivingRouteResult(MapConstant.PlanRouteType type, DrivingRouteResult result, SearchResult.ERRORNO error);

        /**
         * 步行
         *
         * @param result
         * @param error
         */
        void onGetWalkingRouteResult(MapConstant.PlanRouteType type, WalkingRouteResult result, SearchResult.ERRORNO error);

        /**
         * 公交
         *
         * @param result
         * @param error
         */
        void onGetTransitRouteResult(MapConstant.PlanRouteType type, TransitRouteResult result, SearchResult.ERRORNO error);
    }

    /***
     * 搜索结果监听
     *
     * @author EvaGu
     *
     * @param <T>
     */
    public interface OnMapSearchListener<T> {

        /**
         * 返回结果
         *
         * @param result 结果List，失败为null
         * @param error  错误号，0表示正确返回；错误号可参考MKEvent中的定义
         */
        void onComplete(List<T> result, SearchResult.ERRORNO error);
    }

    /**
     * 定位回调方法
     *
     * @author zhangjiao
     */
    public interface OnLocationListener {

        /**
         * 定位回调接口
         *
         * @author zhangjiao
         */
        public void onReceiveLocation(BDLocation location);
    }

    public interface OnGetAddressListener {

        /**
         * 定位回调接口
         *
         * @author zhangjiao
         */
        public void onReceiveAddress(String mkAddrInfo);
    }

}
