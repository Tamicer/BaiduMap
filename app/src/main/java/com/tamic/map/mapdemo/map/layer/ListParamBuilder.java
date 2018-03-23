package com.tamic.map.mapdemo.map.layer;


import com.pinganfang.haofangtuo.base.INoProguard;
import com.pinganfang.haofangtuo.business.map.MapConstant;
import com.pinganfang.haofangtuo.common.base.BaseBean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by SUNTAIYI049 on 2016-10-27.
 */
public interface ListParamBuilder extends Serializable, MapConstant, INoProguard {

    int XF_TYPE = 0;
    int ESF_TYPE = 1;
    int ZF_TYPE = 2;
    int ZF_BRAND = 3;


    public class BaseNeary extends BaseBean implements Serializable, INoProguard {

        public double lat;
        public double lng;
        public int radius;

    }

    interface Nearby extends Serializable, INoProguard {

        void setLat(double lat);

        void setLin(double lin);

        void setRadius(int radius);

        double lat();

        double lin();

        int radius();

    }

    Map<String, String> build();

    Map<String, String> build(String categoryId);

    void clear();

    void clear(String categoryId);

    void setCommonKeyword(String keyword);

    String getCommonKeyword();

    ListParamBuilder setCommonCommunityId(Integer id);

    Integer getCommonCommunityId();

    ListParamBuilder setCommonRegionId(Integer id);

    Integer getCommonRegionId();

    ListParamBuilder setCommonBlockId(Integer id);

    Integer getCommonBlockId();

    void setCommonNearby(Nearby nearby);

    Nearby getCommonNearby();

    ListParamBuilder copy();

    public HouseType getBuilderType();
}
