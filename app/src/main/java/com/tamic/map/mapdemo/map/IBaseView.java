package com.tamic.map.mapdemo.map;

/**
 * Created by Tamic on 2017-11-07.
 */

public interface IBaseView<T extends BasePresnter> {

    void setPresenter(T presenter);
}