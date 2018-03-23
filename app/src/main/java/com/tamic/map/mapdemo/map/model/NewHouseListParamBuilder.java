package com.tamic.map.mapdemo.map.model;

import android.text.TextUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * http://pms.ipo.com/pages/viewpage.action?pageId=59081107
 */
public class NewHouseListParamBuilder extends BaseListParamBuilder {

    private static final Map<String, BuildByCategory> BUILD_MAP = new HashMap<>();

    static {

        BUILD_MAP.put(REGION_ID, new BuildByCategory() {

            @Override
            public void build(ListParamBuilder paramBuilder, Map<String, String> params) {
                NewHouseListParamBuilder builder = (NewHouseListParamBuilder) paramBuilder;
                if (builder.mRegion != null) {
                    params.put("areaIds", String.valueOf(builder.mRegion));
                }
                if (builder.mBlocks.size() > 0) {
                    params.put("blockIds", joinIDList(builder.mBlocks));
                }
                if (builder.mSubwayLine != null) {
                    params.put("subwayLineId", String.valueOf(builder.mSubwayLine));
                }
                if (builder.mStations.size() > 0) {
                    params.put("subwayStationIds", joinIDList(builder.mStations));
                }
                if (builder.mLat != null) {
                    params.put("lat", builder.mLat);
                }
                if (builder.mLng != null) {
                    params.put("lng", builder.mLng);
                }
                if (builder.mRadius != null) {
                    params.put("radius", String.valueOf(builder.mRadius));
                }
            }
        });
        BUILD_MAP.put(SORTER_ID, new BuildByCategory() {

            @Override
            public void build(ListParamBuilder paramBuilder, Map<String, String> params) {
                NewHouseListParamBuilder builder = (NewHouseListParamBuilder) paramBuilder;
                if (builder.mOrderType != null) {
                    params.put("sort", String.valueOf(builder.mOrderType));
                }
            }
        });
        BUILD_MAP.put(PRICE_ID, new BuildByCategory() {

            @Override
            public void build(ListParamBuilder paramBuilder, Map<String, String> params) {
                NewHouseListParamBuilder builder = (NewHouseListParamBuilder) paramBuilder;
                if (builder.mMinPrice != null) {
                    params.put("mainTotalPriceMin", String.valueOf(builder.mMinPrice));
                }
                if (builder.mMaxPrice != null) {
                    params.put("mainTotalPriceMax", String.valueOf(builder.mMaxPrice));
                }
                if (builder.mMinUnitPrice != null) {
                    params.put("mainPriceMin", String.valueOf(builder.mMinUnitPrice));
                }
                if (builder.mMaxUnitPrice != null) {
                    params.put("mainPriceMax", String.valueOf(builder.mMaxUnitPrice));
                }
            }
        });
        BUILD_MAP.put(LAYOUT_ID, new BuildByCategory() {

            @Override
            public void build(ListParamBuilder paramBuilder, Map<String, String> params) {
                NewHouseListParamBuilder builder = (NewHouseListParamBuilder) paramBuilder;
                if (builder.mLayouts.size() > 0) {
                    params.put("beds", joinIDList(builder.mLayouts));
                }
            }
        });
        BUILD_MAP.put(MORE_ID, new BuildByCategory() {

            @Override
            public void build(ListParamBuilder paramBuilder, Map<String, String> params) {
                NewHouseListParamBuilder builder = (NewHouseListParamBuilder) paramBuilder;
                if (builder.mBenefits.size() > 0) {
                    params.put("discountTypes", joinIDList(builder.mBenefits));
                }
                if (builder.mHouseTypes.size() > 0) {
                    params.put("propertyTypes", joinIDList(builder.mHouseTypes));
                }
                if (builder.mFeatures.size() > 0) {
                    params.put("featureTags", joinIDList(builder.mFeatures));
                }
                if (builder.mSaleStates.size() > 0) {
                    params.put("saleStatus", joinIDList(builder.mSaleStates));
                }
            }
        });
        BUILD_MAP.put(KEYWORD_ID, new BuildByCategory() {

            @Override
            public void build(ListParamBuilder paramBuilder, Map<String, String> params) {
                NewHouseListParamBuilder builder = (NewHouseListParamBuilder) paramBuilder;
                if (!TextUtils.isEmpty(builder.mKeyword)) {
                    params.put("keyword", String.valueOf(builder.mKeyword));
                }
            }
        });
    }


    private Integer mRegion;
    private List<Integer> mBlocks = new ArrayList<>();
    private List<Integer> mLayouts = new ArrayList<>();
    private List<Integer> mBenefits = new ArrayList<>();
    private List<Integer> mHouseTypes = new ArrayList<>();
    private List<Integer> mStations = new ArrayList<>();
    private List<Integer> mFeatures = new ArrayList<>();
    private List<Integer> mSaleStates = new ArrayList<>();

    private Integer mSubwayLine;
    private Integer mMinPrice;
    private Integer mMaxPrice;
    private String mKeyword;
    private Integer mOrderType;
    private String mLat;
    private String mLng;
    private Integer mRadius;
    private Integer mMinUnitPrice;
    private Integer mMaxUnitPrice;

    public NewHouseListParamBuilder() {
        mBuilderType = HouseType.XF;
    }

    public NewHouseListParamBuilder(Map<String, String> params) {
        this();

    }

    @Override
    protected Map<String, BuildByCategory> getBuildMap() {
        return BUILD_MAP;
    }

    public Map<String, String> build() {

        return params;
    }

    @Override
    public void clear() {
        clear(REGION_ID);
        clear(PRICE_ID);
        clear(LAYOUT_ID);
        clear(MORE_ID);
        clear(KEYWORD_ID);
    }

    @Override
    public void clear(String categoryId) {
        if (categoryId.equals(REGION_ID)) {
            mRegion = null;
            mBlocks.clear();
            mSubwayLine = null;
            mStations.clear();
            mLat = null;
            mLng = null;
            mRadius = null;
        } else if (categoryId.equals(PRICE_ID)) {
            mMinPrice = null;
            mMaxPrice = null;
            mMinUnitPrice = null;
            mMaxUnitPrice = null;
        } else if (categoryId.equals(LAYOUT_ID)) {
            mLayouts.clear();
        } else if (categoryId.equals(MORE_ID)) {
            mBenefits.clear();
            mHouseTypes.clear();
            mFeatures.clear();
            mSaleStates.clear();
        } else if (categoryId.equals(KEYWORD_ID)) {
            mKeyword = null;
        }
    }

    @Override
    public void setCommonKeyword(String keyword) {
        setKeyword(keyword);
    }

    @Override
    public String getCommonKeyword() {
        return getKeyword();
    }

    @Override
    public ListParamBuilder setCommonCommunityId(Integer id) {

        return this;
    }

    @Override
    public Integer getCommonCommunityId() {
        return null;
    }

    @Override
    public NewHouseListParamBuilder setCommonRegionId(Integer id) {
        mRegion = id;
        return this;
    }

    @Override
    public Integer getCommonRegionId() {
        return mRegion;
    }

    @Override
    public ListParamBuilder setCommonBlockId(Integer id) {
        mBlocks.clear();
        mBlocks.add(id);
        return this;
    }

    @Override
    public Integer getCommonBlockId() {
        if (mBlocks.size() == 0) {
            return null;
        }
        return mBlocks.get(0);
    }

    @Override
    public void setCommonNearby(Nearby nearby) {
        setNearBy(
                String.valueOf(nearby.lat()),
                String.valueOf(nearby.lin()),
                nearby.radius());
    }

    @Override
    public Nearby getCommonNearby() {
        if (TextUtils.isEmpty(mLat) || TextUtils.isEmpty(mLng) || mRadius == null) {
            return null;
        }
        try {
            Nearby nearby = new NearbyBean();
            nearby.setLat(Double.parseDouble(mLat));
            nearby.setLin(Double.parseDouble(mLng));
            nearby.setRadius(mRadius);
            return nearby;
        } catch (Exception exp) {
            exp.printStackTrace();
            return null;
        }
    }

    @Override
    public ListParamBuilder copy() {
        return new NewHouseListParamBuilder(this.build());
    }

    //Setters
    public NewHouseListParamBuilder setRegion(Integer id) {
        mRegion = id;
        return this;
    }

    public NewHouseListParamBuilder addBlock(Integer id) {
        if (id == null) {
            mBlocks.clear();
        } else {
            mBlocks.add(id);
        }
        return this;
    }

    public NewHouseListParamBuilder setSubwayLine(Integer id) {
        mSubwayLine = id;
        return this;
    }

    public NewHouseListParamBuilder addSubwayStation(Integer id) {
        if (id == null) {
            mStations.clear();
        } else {
            mStations.add(id);
        }
        return this;
    }

    public NewHouseListParamBuilder setMinPrice(Integer price) {
        mMinPrice = price;
        return this;
    }

    public NewHouseListParamBuilder setMaxPrice(Integer price) {
        mMaxPrice = price;
        return this;
    }

    public NewHouseListParamBuilder addLayouts(Integer id) {
        if (id == null) {
            mLayouts.clear();
        } else {
            mLayouts.add(id);
        }
        return this;
    }

    public NewHouseListParamBuilder addBenefit(Integer id) {
        if (id == null) {
            mBenefits.clear();
        } else {
            mBenefits.add(id);
        }
        return this;
    }

    public NewHouseListParamBuilder addFeature(Integer id) {
        if (id == null) {
            mFeatures.clear();
        } else {
            mFeatures.add(id);
        }
        return this;
    }

    public NewHouseListParamBuilder addHouseType(Integer id) {
        if (id == null) {
            mHouseTypes.clear();
        } else {
            mHouseTypes.add(id);
        }
        return this;
    }

    public NewHouseListParamBuilder setKeyword(String keyword) {
        mKeyword = keyword;
        return this;
    }

    public NewHouseListParamBuilder setOrderType(Integer id) {
        mOrderType = id;
        return this;
    }

    public NewHouseListParamBuilder setNearBy(String lat, String lng, Integer radius) {
        mLat = lat;
        mLng = lng;
        mRadius = radius;
        return this;
    }

    public NewHouseListParamBuilder setMinUnitPrice(Integer price) {
        mMinUnitPrice = price;
        return this;
    }

    public NewHouseListParamBuilder setMaxUnitPrice(Integer price) {
        mMaxUnitPrice = price;
        return this;
    }

    public NewHouseListParamBuilder addSaleState(Integer id) {
        if (id == null) {
            mSaleStates.clear();
        } else {
            mSaleStates.add(id);
        }
        return this;
    }


    //Getters
    public Integer getRegion() {
        return mRegion;
    }

    public List<Integer> getBlocks() {
        return mBlocks;
    }

    public Integer getSubwayLine() {
        return mSubwayLine;
    }

    public List<Integer> getSubwayStations() {
        return mStations;
    }

    public Integer getMinPrice() {
        return mMinPrice;
    }

    public Integer getMaxPrice() {
        return mMaxPrice;
    }

    public List<Integer> getLayouts() {
        return mLayouts;
    }

    public List<Integer> getBenefits() {
        return mBenefits;
    }

    public List<Integer> getHouseTypes() {
        return mHouseTypes;
    }

    public List<Integer> getFeatures() {
        return mFeatures;
    }

    public String getKeyword() {
        return mKeyword;
    }

    public Integer getOrderType() {
        return mOrderType;
    }

    public String getLatitude() {
        return mLat;
    }

    public String getLongitude() {
        return mLng;
    }

    public Integer getRadius() {
        return mRadius;
    }

    public Integer getMinUnitPrice() {
        return mMinUnitPrice;
    }

    public Integer getMaxUnitPrice() {
        return mMaxUnitPrice;
    }

    public List<Integer> getSaleStates() {
        return mSaleStates;
    }

}
