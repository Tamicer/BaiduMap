package com.tamic.map.mapdemo.map.layer;


import com.tamic.map.mapdemo.map.MapConstant;
import com.tamic.map.mapdemo.map.model.MapData;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Tamic  on 2017-11-17.
 */

public interface IViewCase {

    void start();

    Disposable observeMapData(Observable<MapData> observable);

    void switchBusinessType(MapConstant.HouseType type, boolean changed);

    void showHeadMessage(String msg, int duration);

    void refreshMapLevel(int zoomLevel);

    Disposable observeMapWithID(Observable<ListParamBuilder> observable);
}
