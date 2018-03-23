package com.tamic.map.mapdemo.map;

import android.content.Context;
import android.support.annotation.NonNull;

import com.baidu.mapapi.model.LatLng;
import com.pinganfang.haofangtuo.business.map.layer.DataRource;
import com.pinganfang.haofangtuo.business.map.layer.IViewCase;
import com.pinganfang.haofangtuo.business.map.layer.ListParamBuilder;
import com.pinganfang.haofangtuo.business.map.layer.MapContract;
import com.pinganfang.haofangtuo.business.map.layer.ViewCase;
import com.pinganfang.haofangtuo.business.map.model.MapData;
import com.pinganfang.haofangtuo.business.map.model.MapQueryBean;
import com.pinganfang.haofangtuo.business.map.model.NewHuouseResource;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function5;

import static com.pinganfang.haofangtuo.business.map.MapConstant.HouseType.XF;

/**
 * Created by LIUYONGKUI726 on 2017-11-17.
 */

public class MapListPresnter implements BasePresnter, MapContract.IPresenter {

    protected Context mContext;
    private IViewCase mViewCase;
    protected DataRource mMapModel;
    protected MapUtils mMapUtils;
    protected int zoomLevel = -1;
    public int mCityId = -1;
    public MapContract.View mView;
    private HouseType houseType = XF;
    protected Disposable mDisposable;


    public MapListPresnter(@NonNull Context mContext, @NonNull IViewCase viewCase, @NonNull MapContract.View view) {
        this.mContext = mContext;
        this.mViewCase = viewCase;
        this.mView = view;
        mMapUtils = MapUtils.getInstance(mContext);
        mMapModel = new NewHuouseResource<>(mContext);
        mView.setPresenter(this);
    }

    public Observable<MapData> subscribeMapData(Observable<MapQueryBean> observable) {
        return mMapModel.subscribeMapData(observable);
    }

    public Disposable loadMapDataWithId(LatLng latLng, int zoomLevel,
                                  ListParamBuilder searchParamBuilder) {
        if (searchParamBuilder == null) {
            searchParamBuilder = mMapModel.newListParamBuilder();
        }

        Observable<MapQueryBean> queryBeanObservable = this.makeMapQuery(
                Observable.just(mView.getCity()),
                Observable.just(latLng),
                Observable.just(0),
                Observable.just(zoomLevel),
                Observable.just(searchParamBuilder));
        Observable<MapData> mapDataObservable =
                this.subscribeMapData(queryBeanObservable);

        mViewCase.showHeadMessage("努力加载中...", 0);
        mViewCase.refreshMapLevel(zoomLevel);
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
        }
        mDisposable = mViewCase.observeMapData(mapDataObservable);
        return mViewCase.observeMapWithID(Observable.just(searchParamBuilder));
    }


    public int zoomStatusToLevel(int zoomStatus) {
        return mMapUtils.zoomStatusToLevel(zoomStatus);
    }

    public Observable<MapQueryBean> makeMapQuery(
            Observable<Integer> city,
            Observable<LatLng> loc,
            Observable<Integer> radius,
            Observable<Integer> level,
            Observable<ListParamBuilder> paramBuilder) {
        return Observable.combineLatest(city, loc, radius, level, paramBuilder,
                new Function5<Integer, LatLng, Integer, Integer, ListParamBuilder, MapQueryBean>() {

                    @Override
                    public MapQueryBean apply(@io.reactivex.annotations.NonNull Integer cityId,
                                              @io.reactivex.annotations.NonNull LatLng latLng,
                                              @io.reactivex.annotations.NonNull Integer radius,
                                              @io.reactivex.annotations.NonNull Integer level,
                                              ListParamBuilder paramBuilder) throws Exception {
                        MapQueryBean queryBean = new MapQueryBean();
                        queryBean.cityId = cityId;
                        queryBean.lat = latLng != null ? latLng.latitude : null;
                        queryBean.lng = latLng != null ? latLng.longitude : null;

                        queryBean.radius = radius;
                        queryBean.level = level;
                        if (paramBuilder == null) {
                            queryBean.params = new TreeMap<>();
                        } else {
                            paramBuilder = filterListParamBuilder(paramBuilder);
                            queryBean.params = paramBuilder.build();
                        }
                        return queryBean;
                    }
                });
    }

    public boolean setCityId(int cityId) {
        if (mCityId == -1) {
            mCityId = cityId;
            return false;
        }
        if (mCityId == cityId) {
            return false;
        }
        mCityId = cityId;
        return true;
    }


    private ListParamBuilder filterListParamBuilder(ListParamBuilder input) {
        Map<String, String> params = new HashMap<>();
        for (String id : mMapModel.getParams()) {
            params.putAll(input.build(id));
        }
        return mMapModel.newListParamBuilder(params);
    }

    @Override
    public void start() {
        mViewCase.start();
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
    public boolean setBusinessType(HouseType type) {
        if (houseType == type) {
            return true;
        }
        houseType = type;
        return false;
    }

    @Override
    public HouseType getBusinessType() {
        return houseType;
    }

    @Override
    public ViewCase getViewCase() {
        return (ViewCase) mViewCase;
    }

    public void destroy() {
        if(mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
