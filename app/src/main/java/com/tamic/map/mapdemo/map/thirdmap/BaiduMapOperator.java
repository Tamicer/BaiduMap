package com.tamic.map.mapdemo.map.thirdmap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * 调用百度地图实现类
 * <p>
 * 接口文档：http://lbsyun.baidu.com/index.php?title=uri/api/android
 *
 * @author ex-zhangpanpan002
 */
public class BaiduMapOperator extends IMapOperator {

    /**
     * 百度地图包名
     */
    public static final String PACKAGE_NAME = "com.baidu.BaiduMap";

    public static final String CHINA_NAME = "百度地图";


    @Override
    void location(Activity activity) {
        try {
            Intent i = new Intent();
            i.setData(Uri.parse("baidumap://map"));
            activity.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            mOnFailListener.onFail();
        }
    }

    @Override
    void navigation(Activity activity, NavParamEntity entity) {
        if (entity == null || entity.getTo() == null) {
            throw new RuntimeException("参数错误！缺少必要参数,是否传入目标点坐标?");
        }
        double[] d = GPSUtil.gcj02_To_Bd09(entity.getTo().getLatitude(), entity.getTo().getLongitude());
        entity.getTo().setLatitude(d[0]);
        entity.getTo().setLongitude(d[1]);
        if (entity.getFrom() != null) {
            double[] d2 = GPSUtil.gcj02_To_Bd09(entity.getFrom().getLatitude(), entity.getFrom().getLongitude());
            entity.getFrom().setLatitude(d2[0]);
            entity.getFrom().setLongitude(d2[1]);
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getNavigationUrl(entity)));
            intent.setPackage(PACKAGE_NAME);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            mOnFailListener.onFail();
        }
    }

    private String getNavigationUrl(NavParamEntity entity) {
        StringBuilder sb = new StringBuilder("baidumap://map/direction?");
        if (entity.getFrom() != null) {
            sb.append("origin=name:我的位置|latlng:").append(entity.getFrom().getLatitude()).append(",").append(entity.getFrom().getLongitude()).append("&");
        }
        sb.append("destination=name:").append(entity.getToName()).append("|latlng:").append(entity.getTo().getLatitude()).append(",").append(entity.getTo().getLongitude());
        Log.e("dev", sb.toString());
        return sb.toString();
    }
}
