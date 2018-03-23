package com.tamic.map.mapdemo.map;

public interface MapConstant{

    public static final int ZF_MAP_ZOOM = 14;

    // MARKER点击
    public static final String MARKER_CLICK = "marker_click";
    // 起点点击
    public static final int START_MARKER_CLICK = 1;
    // 终点点击
    public static final int TERMINAL_MARKER_CLICK = 2;

    // 地图缩放层级
    public static final int MAP_ZOOM = 17;

    public enum PlanRouteType {
        DRIVING, BUS, WALKING
    }

    /**
     * 百度静态图片最大尺寸
     */
    public static final int STATIC_IMAGE_MAX_SIZE = 1024;
    /**
     * 百度静态图片地址
     */
    public static final String STATIC_IMAGE_URL = "http://api.map.baidu.com/staticimage";
    /**
     * 地铁站
     */
    public static final int SEARCH_RESULT_TYPE_METRO = 3;


    enum HouseType {
        ALL, XF, ESF, RECENT
    }

    public static final float MAX_ZOOM_STATUS_REGION = 13.5f;//区域和板块显示分界点
    public static final float MAX_ZOOM_STATUS_SECTOR = 15.5f;//板块与小区显示分界点
    public static final float MAX_ZOOM_STATUS_COMMUNITY = 17.8f;//小区与小区进一步显示分界点

    public static final float DEFAULT_ZOOM_LEVEL_COMMUNITY_NEXT = 19f; //点击小区后跳转层级
    public static final float DEFAULT_ZOOM_LEVEL_COMMUNITY = 17.8f; //点击板块后跳转层级
    public static final float DEFAULT_ZOOM_LEVEL_SECTOR = 15.5f;//点击区域后跳转层级
    public static final float DEFAULT_ZOOM_LEVEL_REGION = 12.5f;//切换城市后跳转层级
    public static final float T_MAX_ZOOM_LEVEL_SECTOR = 14.9f;//
    public static final float T_MAX_ZOOM_LEVEL_REGION = 14.3f;//
    public static final float T_ZOOM_LEVEL_COMMUNITY = 15.4f; //
    public static final float T_ZOOM_LEVEL_SECTOR = 14.8f;//
    public static final float T_ZOOM_LEVEL_REGION = 12.5f;//
    public static final float MAX_ZOOM_LEVEL = 20f;
    public static final float MIN_ZOOM_LEVEL = 3f;


    public static final int ZOOM_STATUS_REGION = 0;//当前要显示层级为区域
    public static final int ZOOM_STATUS_SECTOR = 1;//当前要显示层级为板块
    public static final int ZOOM_STATUS_COMMUNITY = 2;//当前要显示层级为小区
    public static final int ZOOM_STATUS_COMMUNITY_AHEAD = 3;//当前要显示层级为小区前一步

    public static final int LEVEL_REGION = 1;
    public static final int LEVEL_BLOCK = 2;
    public static final int LEVEL_COMMUNITY = 3;
    public static final int LEVEL_NEW = 4;

    public static final int RADIUS_REGION = Integer.MAX_VALUE;//板块层级 搜索半径
    public static final int RADIUS_SECTOR = 8000;//板块层级 搜索半径
    public static final int RADIUS_COMMUNITY = 5000;//小区层级 搜索半径
    public static final int RADIUS_COMMUNITY_AHEAD = 6000;


}
