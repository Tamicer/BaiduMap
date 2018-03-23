package com.tamic.map.mapdemo.map.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


import com.tamic.map.mapdemo.map.layer.MapItem;

import java.util.Locale;

import io.reactivex.annotations.NonNull;


/**
 * Created by Tamic on 2017-10-09.
 */
public class MapCommunityOverly extends MapOverly<MapItem> {

    private TextView mTvOverlyInfoIcon;

    public enum Community {
        COOPERATION, NORMAL
    }

    public MapCommunityOverly(Context context) {
        super(context);
        init(context, Community.NORMAL);
    }

    public MapCommunityOverly(Context context, Community community) {
        super(context);
        init(context, community);
    }

    public MapCommunityOverly(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, Community.NORMAL);
    }

    public MapCommunityOverly(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, Community.NORMAL);
    }

    private void init(Context context, Community community) {
        mTvOverlyInfo = (TextView) rootView.findViewById(R.id.tv_overly_info);
        mTvOverlyInfoIcon =  findViewById(R.id.tv_overly_info_icon);
        switch (community) {
            case COOPERATION:
                //rootView.setBackgroundResource(R.drawable.hz_bubble_community);
                mTvOverlyInfoIcon.setVisibility(VISIBLE);
                rootView.setBackgroundResource(R.drawable.bg_shape_share_copy_btn);
                break;
            case NORMAL:
                mTvOverlyInfoIcon.setVisibility(GONE);
                //rootView.setBackgroundResource(R.drawable.map_mark_community_bg);
                rootView.setBackgroundResource(R.drawable.bg_shape_share_copy_btn);
                break;
        }
    }

    @Override
    int getView() {
        return R.layout.layout_map_community_overly;
    }

    @Override
    public void setData(String name, String count) {
        mTvOverlyInfo.setText(String.format(Locale.CHINA, "%s(%s)", name, count));
    }

    @Override
    public void setData(String name, String count, String hzCount) {

    }

    @Override
    public void setData(MapItem item) {
        if (item == null) {
            return;
        }

        setDataPrice(item.name, String.valueOf(item.price));
    }

    public void setData(String title) {
        mTvOverlyInfo.setText(title);
    }


    public void setDataPrice(@NonNull String title, String extra) {
        mTvOverlyInfo.setText(String.format(Locale.CHINA, "%s(%s)",  title, extra));
    }

    public void setSelect(boolean select) {
        mTvOverlyInfo.setBackgroundResource(select ? R.drawable.bubble_community_selected : R.drawable.bubble_community);
    }
}



