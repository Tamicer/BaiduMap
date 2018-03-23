package com.tamic.map.mapdemo.map.model;

import com.pinganfang.haofangtuo.business.map.MapConstant;
import com.pinganfang.haofangtuo.business.map.layer.ListParamBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pinganfang.haofangtuo.widget.categroybar.CategoryId.KEYWORD_ID;
import static com.pinganfang.haofangtuo.widget.categroybar.CategoryId.LAYOUT_ID;
import static com.pinganfang.haofangtuo.widget.categroybar.CategoryId.MORE_ID;
import static com.pinganfang.haofangtuo.widget.categroybar.CategoryId.PRICE_ID;
import static com.pinganfang.haofangtuo.widget.categroybar.CategoryId.REGION_ID;
import static com.pinganfang.haofangtuo.widget.categroybar.CategoryId.RENT_ID;
import static com.pinganfang.haofangtuo.widget.categroybar.CategoryId.SORTER_ID;

/**
 * Created by SUNTAIYI049 on 2017-01-16.
 */
public abstract class BaseListParamBuilder implements ListParamBuilder, MapConstant {

    protected MapConstant.HouseType mBuilderType;

    @Override
    public HouseType getBuilderType() {
        return mBuilderType;
    }

    protected interface BuildByCategory {

        void build(ListParamBuilder paramBuilder, Map<String, String> params);
    }

    private String[] A(String... strs) {
        return strs;
    }

    protected abstract Map<String, BuildByCategory> getBuildMap();

    @Override
    public Map<String, String> build() {
        Map<String, String> map = new HashMap<>();
        for (String id : A(REGION_ID, PRICE_ID, LAYOUT_ID, MORE_ID, SORTER_ID, KEYWORD_ID, RENT_ID)) {
            if (getBuildMap().containsKey(id)) {
                getBuildMap().get(id).build(this, map);
            }
        }
        return map;
    }

    @Override
    public Map<String, String> build(String categoryId) {
        Map<String, String> map = new HashMap<>();
        if (getBuildMap().containsKey(categoryId)) {
            getBuildMap().get(categoryId).build(this, map);
        }
        return map;
    }


    protected static String joinIDList(List<Integer> list) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); ++i) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    protected static List<Integer> splitIDString(String idstr) {
        List<Integer> list = new ArrayList<>();
        String[] ids = idstr.split(",");
        for (String id : ids) {
            list.add(Integer.parseInt(id));
        }
        return list;
    }
}
