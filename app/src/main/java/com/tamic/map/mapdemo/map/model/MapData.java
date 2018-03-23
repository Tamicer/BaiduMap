package com.tamic.map.mapdemo.map.model;


import com.tamic.map.mapdemo.map.layer.MapItem;

import java.util.List;

/**
 *
 */
public class MapData<T extends MapItem> {

    public int showLevel;
    public int totalCount;
    public int totalHzCount;
    public List<T> list;
    public String title_des;
    public String map_des;
    @JSONField(name = "city_id")
    public int cityIdX;
    @JSONField(name = "area_id")
    public int areaIdX;
    @JSONField(name = "block_id")
    public int blockIdX

}
