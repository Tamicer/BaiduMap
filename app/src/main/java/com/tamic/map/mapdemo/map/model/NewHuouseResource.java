package com.tamic.map.mapdemo.map.model;

import android.content.Context;


import com.tamic.map.mapdemo.map.layer.DataCaseHandler;
import com.tamic.map.mapdemo.map.layer.ListParamBuilder;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LIUYONGKUI726 on 2017-11-08.
 */

public class NewHuouseResource<T> extends DataCaseHandler {

    private final Context mContext;

    public NewHuouseResource(Context appContext) {
        super(appContext);
        mContext = appContext;
        mConverter = new NewHouseConverter((App) appContext);
    }


    @Override
    public ListParamBuilder newListParamBuilder() {
        return new NewHouseListParamBuilder();
    }

    @Override
    public ListParamBuilder newListParamBuilder(Map params) {
        return new NewHouseListParamBuilder(params);
    }

    @Override
    public T getList(MapQueryBean queryBean) {
        //Resuqst Api no Rxjava
        return (T) mApp.getHaofangtuoApi().getNewHouseMapData(
                queryBean.cityId,
                queryBean.level,
                queryBean.lat,
                queryBean.lng,
                queryBean.radius,
                queryBean.params);
    }

    @Override
    public String[] getFilters() {
        return A(PRICE_ID, LAYOUT_ID, MORE_ID);
    }

    @Override
    public String[] getParams() {
        return A(PRICE_ID, LAYOUT_ID, MORE_ID, KEYWORD_ID, REGION_ID);
    }

    @Override
    public CRConverter getConverter() {
        return mConverter;
    }

    @Override
    public Observable<MapData> subscribeMapData(Observable observable) {

        return observable
                .observeOn(Schedulers.io())
                .map(new Function<MapQueryBean, MapData>() {

                    @Override
                    public MapData apply(@NonNull MapQueryBean queryBean) throws Exception {

                        BaseRespone<NewHouseMapData> entity = null;
                        try {
                          entity = mApp.getApi().getNewHouseMapData(
                                            queryBean.cityId,
                                            queryBean.level,
                                            queryBean.lat,
                                            queryBean.lng,
                                            queryBean.radius,
                                            queryBean.params);
                        } catch (ClassCastException e) {

                            throw new RuntimeException("网络出错，请重试！");
                        }


                        return entity.data;
                    }
                })
                .filter(new Predicate<MapData>() {

                    @Override
                    public boolean test(@NonNull MapData mapData) throws Exception {
                        return mapData != null;
                    }
                });
    }
}
