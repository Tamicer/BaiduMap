package com.tamic.map.mapdemo.map.widget;

import android.content.Context;
import android.widget.TextView;
;

import com.tamic.map.mapdemo.map.layer.MapItem;

import java.util.Locale;


/**
 * Created by Tamicon 2017-10-09.
 */
public class MapRegionSectorOverly extends MapOverly<MapItem> {

    TextView mTvOverlyCount;
    TextView mTvOverlyTame;
    TextView mTvOverlyHzcount;


    public MapRegionSectorOverly(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mTvOverlyTame = (TextView) rootView.findViewById(R.id.tv_overly_name);
        mTvOverlyCount = (TextView) rootView.findViewById(R.id.tv_overly_count);
        mTvOverlyHzcount = (TextView) findViewById(R.id.tv_overly_hzcount);

    }

    @Override
    int getView() {
        return R.layout.layout_map_regionsector_overly;
    }

    @Override
    public void setData(String name, String count) {
        mTvOverlyTame.setText(name);
        mTvOverlyCount.setText(count);
    }

    @Override
    public void setData(String name, String count, String hzCount) {
        mTvOverlyTame.setText(name);
        String text = String.format(Locale.CHINA, "%s套 ", count);
        mTvOverlyCount.setText(text);
        mTvOverlyHzcount.setText(hzCount + "套");
    }

    @Override
    public void setData(MapItem item) {

    }
}
