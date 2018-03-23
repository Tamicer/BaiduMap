package com.tamic.map.mapdemo.map.thirdmap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Class description
 *
 * @author ex-zhangpanpan002
 */
public class GoogleMapOperator extends IMapOperator {

    public static final String PACKAGE_NAME = "com.google.android.apps.maps";

    public static final String CHINA_NAME = "Google Map地图";

    @Override
    void location(Activity activity) {

    }

    @Override
    void navigation(Activity activity, NavParamEntity entity) {
        if (entity == null || entity.getTo() == null) {
            throw new RuntimeException("参数错误！缺少必要参数,是否传入目标点坐标?");
        }
        String uri = "google.navigation:q=" + entity.getTo().getLatitude() + "," + entity.getTo().getLongitude();
        Log.e("dev", uri);
        Uri gmmIntentUri = Uri.parse(uri);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage(PACKAGE_NAME);
        activity.startActivity(mapIntent);
    }
}
