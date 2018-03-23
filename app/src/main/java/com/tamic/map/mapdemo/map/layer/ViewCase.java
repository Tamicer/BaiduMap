package com.tamic.map.mapdemo.map.layer;

import com.baidu.mapapi.model.LatLng;
import com.pinganfang.haofangtuo.api.ListBaseBean;
import com.pinganfang.haofangtuo.business.map.model.MapData;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by LIUYONGKUI726 on 2017-11-07.
 */

public interface ViewCase extends IViewCase {

    Disposable observeParamBuilder(Observable<ListParamBuilder> observable);

    Disposable observeCityCenter(Observable<LatLng> observable);

    Disposable observeLocation(Observable<LatLng> observable);

    Disposable observeListData(Observable<ListBaseBean<MapItem>> observable);

    Observable<MapData> filterMapDataOnMarkClicked(Observable<MapData> observable);

    ListParamBuilder getParamBuilder();

    void hideHeadMessage();

    void changeSelectedCommunity(Integer previousId, Integer id);

    void closeList();

    void openList();

    void createHouseListFragment();

    void onMarkClick(ListParamBuilder builder);

}
