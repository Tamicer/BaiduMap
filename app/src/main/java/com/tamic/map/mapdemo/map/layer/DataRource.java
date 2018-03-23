package com.tamic.map.mapdemo.map.layer;

import com.pinganfang.haofangtuo.base.INoProguard;
import com.pinganfang.haofangtuo.business.map.model.CRConverter;
import com.pinganfang.haofangtuo.business.map.model.MapData;
import com.pinganfang.haofangtuo.business.map.model.MapQueryBean;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by LIUYONGKUI726 on 2017-11-07.
 */

public interface DataRource<T> extends INoProguard {

    ListParamBuilder newListParamBuilder();

    ListParamBuilder newListParamBuilder(Map<String, String> params);

    T getList(MapQueryBean queryBean);

    String[] getFilters();

    String[] getParams();

    CRConverter getConverter();

    Observable<MapData> subscribeMapData(Observable<MapQueryBean> observable);

}
