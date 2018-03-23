package com.tamic.map.mapdemo.map.layer;

import android.content.Context;

import com.tamic.map.mapdemo.map.model.CRConverter;

/**
 * Created by Tamic on 2017-11-07.
 */

public abstract class DataCaseHandler  implements DataRource, CategoryId {

    protected CRConverter mConverter;

    public DataCaseHandler(Context context) {
        this.mApp = (App) context;
    }

    protected <T> T[] A(T... arr) {
        return arr;
    }

}
