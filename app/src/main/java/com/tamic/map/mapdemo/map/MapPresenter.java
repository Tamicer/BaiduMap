package com.tamic.map.mapdemo.map;


import android.content.Context;
import android.support.annotation.NonNull;

import com.baidu.mapapi.model.LatLng;
import com.tamic.map.mapdemo.map.layer.ListParamBuilder;
import com.tamic.map.mapdemo.map.layer.MapContract;
import com.tamic.map.mapdemo.map.layer.ViewCase;
import com.tamic.map.mapdemo.map.model.NewHuouseResource;

import io.reactivex.Observable;

import static com.tamic.map.mapdemo.map.MapConstant.LEVEL_REGION;
import static io.reactivex.Observable.just;


/**
 * Created by LIUYONGKUI726 on 2017-11-06.
 */

public class MapPresenter extends MapListPresnter implements MapContract.IPresenter {

    private ViewCase mViewCase;
    private MapContract.HouseType mBusinessType = XF;
    private Integer mCommunityId = null;

    public MapPresenter(@NonNull Context appContext, @NonNull ViewCase map, @NonNull MapContract.View view) {
        super(appContext, map, view);
        mViewCase = map;
    }

    public void loadCommunityList(ListParamBuilder paramBuilder, int page) {
        if (mCommunityId == null) {


    }

    public void onClickCommunity(int mId) {

    }

    public boolean setBusinessType(@NonNull MapConstant.HouseType type) {

        return true;
    }

    @Override
    public boolean setCityId(int cityId) {
        if (mCityId == -1) {
            mCityId = cityId;
            return false;
        }
        if (mCityId == cityId) {
            return false;
        }
        mCityId = cityId;
        mCommunityId = null;
        return true;
    }

    public void setSelectedCommunity(Integer id) {
        Integer previousId = mCommunityId;
        mCommunityId = id;
        mViewCase.changeSelectedCommunity(previousId, id);
    }

    public void loadMapDataWithCityChanged(ListParamBuilder paramBuilder) {
        loadMapDataWithCityChanged(new LatLng(0, 0), paramBuilder);
    }

    public void loadMapDataWithCityChanged(LatLng latLng, ListParamBuilder paramBuilder) {
        loadMapDataOnParamChanged(latLng, LEVEL_REGION, paramBuilder);
}

    public void loadBusinessTypeChanged(LatLng latLng, int zoomStatus, MapConstant.HouseType type) {
        //todo dev
        mViewCase.showHeadMessage("努力加载中...", 0);
        mViewCase.refreshMapLevel(zoomLevel);

        //loadCommunityList(mParamBuilder, 1);
    }


    public void loadOnCityChanged(ListParamBuilder searchParamBuilder) {
        //切换城市
        if (searchParamBuilder == null) {
            searchParamBuilder = mMapModel.newListParamBuilder();
        }
        Observable<ListParamBuilder> paramBuilderObservable =
                just(searchParamBuilder);

        //get Baidu   LatLng obj
        Observable<LatLng> latLngObservable = null;
        if (searchParamBuilder.getCommonNearby() != null) {
            NearbyBean nearby = (NearbyBean) searchParamBuilder.getCommonNearby();
            latLngObservable = Observable.just(new LatLng(nearby.lat, nearby.lng));
        } else {
            latLngObservable = LocationSource.create(mContext).subscribeLastLocationWithLat();
        }

        if (latLngObservable == null) {
            mViewCase.showHeadMessage("未获取当前城市信息", 0);
            return;
        }
        mViewCase.showHeadMessage("努力加载中...", 0);
        Observable<MapQueryBean> queryBeanObservable = this.makeMapQuery(
                just(mCityId),
                latLngObservable,
                just(mMapUtils.getRadiusByZoomStatus(ZOOM_STATUS_REGION)),
                just(mMapUtils.zoomStatusToLevel(ZOOM_STATUS_REGION)),
                paramBuilderObservable);
        Observable<MapData> mapDataObservable =
                this.subscribeMapData(queryBeanObservable);
        mViewCase.observeCityCenter(latLngObservable);
        mViewCase.observeParamBuilder(paramBuilderObservable);
        mViewCase.observeMapData(mapDataObservable);
    }

    public void loadMapDataOnParamChanged(LatLng latLng, int zoomStatus,
                                          ListParamBuilder searchParamBuilder) {
        //参数变更
        if (searchParamBuilder == null) {
            searchParamBuilder = mMapModel.newListParamBuilder();
        }
        zoomLevel = zoomStatusToLevel(zoomStatus);
        searchParamBuilder.setCommonCommunityId(0).setCommonBlockId(0).setCommonRegionId(0);
        Observable<MapQueryBean> queryBeanObservable = this.makeMapQuery(
                Observable.just(mCityId),
                Observable.just(latLng),
                Observable.just(mMapUtils.getRadiusByZoomStatus(zoomStatus)),
                Observable.just(zoomLevel),
                Observable.just(searchParamBuilder));
        Observable<MapData> mapDataObservable =
                this.subscribeMapData(queryBeanObservable);

        mViewCase.showHeadMessage("努力加载中...", 0);
        if (mDisposable != null) {
            if (! mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
        }
        mDisposable = mViewCase.observeMapData(mapDataObservable);//marker
        mViewCase.refreshMapLevel(zoomLevel);
        mViewCase.observeParamBuilder(Observable.just(searchParamBuilder));

    }

    public void loadMapDataOnMarkClicked(LatLng latLng, int level, int id,
                                         ListParamBuilder searchParamBuilder) {
        if (searchParamBuilder == null) {
            searchParamBuilder = mMapModel.newListParamBuilder();
        } else {
            searchParamBuilder = searchParamBuilder.copy();
        }
        int zoomStatus;
        if (level == LEVEL_REGION) {
            zoomStatus = ZOOM_STATUS_SECTOR;
            searchParamBuilder.setCommonRegionId(id);
        } else if (level == LEVEL_BLOCK) {
            zoomStatus = ZOOM_STATUS_COMMUNITY;
            searchParamBuilder.setCommonBlockId(id);
        } else {
            return;
        }
        Observable<MapQueryBean> queryBeanObservable = makeMapQuery(
                Observable.just(mCityId),
                Observable.just(latLng),
                Observable.just(mMapUtils.getRadiusByZoomStatus(level)),
                Observable.just(mMapUtils.zoomStatusToLevel(zoomStatus)),
                Observable.just(searchParamBuilder));

        mViewCase.showHeadMessage("努力加载中...", 0);
        Observable<MapData> mapDataObservable =
                mViewCase.filterMapDataOnMarkClicked(subscribeMapData(queryBeanObservable));

        mViewCase.observeMapData(mapDataObservable);
        mViewCase.refreshMapLevel(mMapUtils.zoomStatusToLevel(zoomStatus));

    }


    public void subscribeCurrentLocation() {
        mViewCase.observeLocation(LocationSource.create(mContext).subscribeNewLocationWithLat());
    }
    public void subscribeCityMapData(LatLng latLng) {
        mViewCase.observeLocation(Observable.just(latLng));
    }

    public ListParamBuilder newListParamBuilder() {
        return mMapModel.newListParamBuilder();
    }

    @Override
    public ViewCase getViewCase() {
        return mViewCase;
    }

    @Override
    public void queryChannels() {

    }

    @Override
    public void checkCityState() {

    }

    @Override
    public void checkLocationState() {

    }

    @Override
    public MapContract.HouseType getBusinessType() {
        return mBusinessType;
    }


    @Override
    public void start() {
        mViewCase.start();
    }


    private Observable<MapQueryBean> creatquery(ListParamBuilder searchParamBuilder) {
        //切换城市
        if (searchParamBuilder == null) {
            searchParamBuilder = mMapModel.newListParamBuilder();
        }
        Observable<ListParamBuilder> paramBuilderObservable =
                just(searchParamBuilder);

        //get Baidu   LatLng obj
        Observable<LatLng> latLngObservable = null;
        if (searchParamBuilder.getCommonNearby() != null) {
            NearbyBean nearby = (NearbyBean) searchParamBuilder.getCommonNearby();
            latLngObservable = Observable.just(new LatLng(nearby.lat, nearby.lng));
        } else {
            latLngObservable = LocationSource.create(mContext).subscribeLastLocationWithLat();
        }

        if (latLngObservable == null) {
            mViewCase.showHeadMessage("未获取当前城市信息", 0);
            return null;
        }
        mViewCase.showHeadMessage("努力加载中...", 0);

        return this.makeMapQuery(
                just(mCityId),
                latLngObservable,
                just(mMapUtils.getRadiusByZoomStatus(ZOOM_STATUS_REGION)),
                just(mMapUtils.zoomStatusToLevel(ZOOM_STATUS_REGION)),
                paramBuilderObservable);
    }


}
