package com.tamic.map.mapdemo.map.layer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.tamic.map.mapdemo.map.MapPresenter;
import com.tamic.map.mapdemo.map.MapUtils;
import com.tamic.map.mapdemo.map.core.MapController;

import static com.tamic.map.mapdemo.map.MapConstant.LEVEL_BLOCK;
import static com.tamic.map.mapdemo.map.MapConstant.LEVEL_COMMUNITY;
import static com.tamic.map.mapdemo.map.MapConstant.LEVEL_NEW;
import static com.tamic.map.mapdemo.map.MapConstant.LEVEL_REGION;

/**
 * Created by Tamic on 2017-11-06.
 */

public class MapCaseHandler extends Handler {

    private MapUtils mMapUtils;
    private final ViewCase mView;
    private final MapContract.IPresenter mPresenter;
    private MapController mMapController;

    public MapCaseHandler(Context context, ViewCase view, MapPresenter presenter) {
        mMapUtils = MapUtils.getInstance(context);
        mView = view;
        mPresenter = presenter;
    }

    public void setMapController(MapController mapController) {
        mMapController = mapController;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MapController.MSG_LOAD_NEEDED: {
                LatLng latLng = (LatLng) msg.obj;
                int zoomStatus = msg.arg1;
                getPresenter().loadMapDataOnParamChanged(
                        latLng,
                        zoomStatus,
                        mView.getParamBuilder());
                break;
            }
            case MapController.MSG_MARKER_CLICKED: {
                Marker marker = (Marker) msg.obj;
                Bundle extraInfo = marker.getExtraInfo();
                if (extraInfo != null) {
                    MapItem item = extraInfo.getParcelable("item");
                    int level = extraInfo.getInt("level");
                    if (level == LEVEL_REGION || level == LEVEL_BLOCK) {
                        getPresenter().loadMapDataOnMarkClicked(
                                new LatLng(item.lat, item.lng),
                                level,
                                item.id,
                                mView.getParamBuilder());
                        mView.onMarkClick(mView.getParamBuilder().setCommonRegionId(item.id).setCommonBlockId(item.id));
                    } else if (level == LEVEL_COMMUNITY || level == LEVEL_NEW) {
                        if (item.isHz()) {
                            getPresenter().onClickCommunity(item.id);
                        }
                        mView.onMarkClick(mView.getParamBuilder().setCommonCommunityId(item.id));

                    }


                }

                break;
            }
            case MapController.MSG_MAP_CLICKED: {
                getPresenter().setSelectedCommunity(null);
                mView.closeList();
                break;
            }
        }
    }

    MapPresenter getPresenter() {
        return (MapPresenter) mPresenter;
    }

}
