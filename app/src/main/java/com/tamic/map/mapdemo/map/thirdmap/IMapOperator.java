package com.tamic.map.mapdemo.map.thirdmap;

import android.app.Activity;

/**
 * Class description
 *
 * @author ex-zhangpanpan002
 */
public abstract class IMapOperator {

    protected OnFailListener mOnFailListener;

    abstract void location(Activity activity);

    abstract void navigation(Activity activity, NavParamEntity entity);

    public void setOnFailListener(OnFailListener listener) {
        mOnFailListener = listener;
    }

    public interface OnFailListener {

        void onFail();
    }

}
