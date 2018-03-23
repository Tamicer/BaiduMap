package com.tamic.map.mapdemo.map.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by LIUYONGKUI726 on 2017-11-14.
 */

public abstract class MapOverly<T> extends LinearLayout {

    protected TextView mTvOverlyInfo;
    protected View rootView;


    public MapOverly(Context context) {
        super(context);
        init(context);
    }

    public MapOverly(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MapOverly(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        rootView = View.inflate(context, getView(), null);
        addView(rootView);
    }

    @LayoutRes
    abstract int getView();

    public abstract void setData(String name, String count);

    public abstract void setData(String name, String count, String hzCount);

    public abstract void setData(T item);

}
