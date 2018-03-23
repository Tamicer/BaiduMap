package com.tamic.map.mapdemo.map.overlayutil;


import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnPolylineClickListener;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;


/**
 * 该类提供一个能够显示和管理多个Overlay的基类
 * <p>
 * 复写{@link #getOverlayOptions()} 设置欲显示和管理的Overlay列表
 * </p>
 * <p>
 * 通过
 * {@link BaiduMap#setOnMarkerClickListener(OnMarkerClickListener)}
 * 将覆盖物点击事件传递给OverlayManager后，OverlayManager才能响应点击事件。
 * <p>
 * 复写{@link #onMarkerClick(Marker)} 处理Marker点击事件
 * </p>
 */
public abstract class OverlayManager implements OnMarkerClickListener, OnPolylineClickListener {

    private final Map<OverlayOptions, Overlay> mOverlayMap = new HashMap<>();
    private BaiduMap mBaiduMap = null;

    /**
     * 通过一个BaiduMap 对象构造
     *
     * @param baiduMap
     */
    public OverlayManager(BaiduMap baiduMap) {
        mBaiduMap = baiduMap;
    }

    public Collection<Overlay> getOverlays() {
        return mOverlayMap.values();
    }

    /**
     * 覆写此方法设置要管理的Overlay列表
     *
     * @return 管理的Overlay列表
     */
    public abstract List<OverlayOptions> getOverlayOptions();

    /**
     * 将所有Overlay 添加到地图上
     */
    /*public final void addToMap(Context context) {
        isFirstIn = true;
        addSomeToMap(context);
    }*/

    /**
     * 将所有Overlay 添加到地图上
     */
    public final void addToMap() {
        if (mBaiduMap == null) {
            return;
        }

        /*removeFromMap();
        List<OverlayOptions> overlayOptions = getOverlayOptions();
        if (overlayOptions != null) {
            mOverlayOptionList.addAll(getOverlayOptions());
        }

        for (OverlayOptions option : mOverlayOptionList) {
            Overlay overlay = mBaiduMap.addOverlay(option);
            mOverlayList.add(overlay);
        }*/

        Set<OverlayOptions> currSet = mOverlayMap.keySet();
        Set<OverlayOptions> newSet = new HashSet<>(getOverlayOptions());

        Set<OverlayOptions> addSet = new HashSet<>(newSet);
        addSet.removeAll(currSet);

        Set<OverlayOptions> removeSet = new HashSet<>(currSet);
        removeSet.removeAll(newSet);

        for (OverlayOptions options : removeSet) {
            Overlay overlay = mOverlayMap.remove(options);
            if (overlay == null) {
                continue;
            }
            overlay.remove();
        }

        for (OverlayOptions options : newSet) {
            Overlay overlay = mBaiduMap.addOverlay(options);
            if (overlay == null) {
                continue;
            }
            mOverlayMap.put(options, overlay);
        }
    }

    /**
     * 将一部分Overlay 添加到地图上
     */
    /*public final void addSomeToMap(Context context) {
        if (mBaiduMap == null) {
            return;
        }

        List<OverlayOptions> overlayOptions = getOverlayOptions();
        mOverlayOptionListJiaoji.clear();
        if (overlayOptions != null) {
            if (!isFirstIn) {
                // 第二次进入 取出交集部分不作处理
                for (OverlayOptions options:overlayOptions) {
                    // 如果上次的marker集合包含当前的marker 存在交集marker集合中
                    if (mOverlayOptionList.contains(options)) {
                        mOverlayOptionListJiaoji.add(options);
                    }
                }
            }
            mOverlayOptionList.clear();
            mOverlayOptionList.addAll(overlayOptions);
        }

        if (!isFirstIn) {
            final DisplayMetrics dm = context.getResources().getDisplayMetrics();
            Rect screenRect = new Rect(0, 0, dm.widthPixels, dm.heightPixels);
            if (mBaiduMap == null) {
                return;
            }
            for (Overlay marker : mOverlayList) {
                // 移除marker 判断marker是否在屏幕内
                Marker marker1 = (Marker) marker;
                LatLng latLng = marker1.getPosition();
                Point point = mBaiduMap.getProjection().toScreenLocation(latLng);
                if (screenRect.contains(point.x, point.y)) {
                    continue;
                }
                marker.remove();
                mOverlayListDelete.add(marker);
            }
            mOverlayList.removeAll(mOverlayListDelete);
            mOverlayListDelete.clear();
        }else{
            for (Overlay marker : mOverlayList) {
                marker.remove();
            }
            mOverlayList.clear();
        }
        // 加到地图上时判断交集集合和当前集合一样的部分 不添加
        for (OverlayOptions option : mOverlayOptionList) {
            if (mOverlayOptionListJiaoji.contains(option)) {
                continue;
            }
            Overlay overlay = mBaiduMap.addOverlay(option);
            mOverlayList.add(overlay);
        }
        isFirstIn = false;
    }*/

    /**
     * 将所有Overlay 从 地图上消除
     */
    public final void removeFromMap() {
        if (mBaiduMap == null) {
            return;
        }
        /*for (Overlay marker : mOverlayList) {
            marker.remove();
        }
        mOverlayOptionList.clear();
        mOverlayList.clear();*/

        for (OverlayOptions options : mOverlayMap.keySet()) {
            Overlay overlay = mOverlayMap.get(options);
            overlay.remove();
        }
        mOverlayMap.clear();
    }

    /**
     * 缩放地图，使所有Overlay都在合适的视野内
     * <p>
     * 注： 该方法只对Marker类型的overlay有效
     * </p>
     */
    public void zoomToSpan() {
        if (mBaiduMap == null) {
            return;
        }
        /*if (mOverlayList.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Overlay overlay : mOverlayList) {
                // polyline 中的点可能太多，只按marker 缩放
                if (overlay instanceof Marker) {
                    builder.include(((Marker) overlay).getPosition());
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }*/
        if (mOverlayMap.size() == 0) {
            return;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Overlay overlay : mOverlayMap.values()) {
            // polyline 中的点可能太多，只按marker 缩放
            if (overlay instanceof Marker) {
                builder.include(((Marker) overlay).getPosition());
            }
        }
        mBaiduMap.setMapStatus(MapStatusUpdateFactory
                .newLatLngBounds(builder.build()));
    }

}
