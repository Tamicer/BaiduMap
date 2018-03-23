package com.tamic.map.mapdemo.map;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Tamic on 2017-03-23.
 */
public class LocationSource {

    private static LocationSource sInst;
    private Context mContext;
    private final LocationClient mLocationClient;
    private boolean mHasInitialLocation = false;
    private final double[] mLocation = new double[2];
    private BDLocation mBdLocation;
    private String mCity;
    private String mAddress;
    private long mLastRequestTime = 0L;
    private ArrayList<ObservableEmitter> mSubscribers = new ArrayList<>();


    private LocationSource(Context context) {
        mContext = context;
        mLocationClient = initLocationClient(mContext);
    }

    public static LocationSource create(Context context) {
        if (sInst == null) {
            synchronized (LocationSource.class) {
                if (sInst == null) {
                    sInst = new LocationSource(context);
                }
            }
        }
        return sInst;
    }

    public static LocationSource get() {
        return sInst;
    }

    public Observable<BDLocation> subscribeNewLocation() {
        return Observable.create(new ObservableOnSubscribe<BDLocation>() {

            @Override
            public void subscribe(@NonNull final ObservableEmitter<BDLocation> e) throws Exception {
                mSubscribers.add(e);
                if (mSubscribers.size() > 1) {
                    return;
                }
                if (mLocationClient.isStarted() || System.currentTimeMillis() - mLastRequestTime < 1000L) {
                    try {
                        ObservableEmitter<BDLocation> newe = mSubscribers.remove(0);

                        if (newe.isDisposed()) {
                            return;
                        }
                        newe.onNext(mBdLocation);
                        if (mSubscribers.size() == 0) {
                            mLocationClient.stop();
                        }
                    } catch (Exception ep) {
                        DevUtil.v("location", "remove index failure:" + ep.getMessage());
                    }
                    return;
                }

                mLocationClient.registerLocationListener(new BDLocationListener() {

                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {
                        if (bdLocation != null && bdLocation.getAddrStr() != null && bdLocation.getCity() != null) {
                            mLocation[0] = bdLocation.getLatitude();
                            mLocation[1] = bdLocation.getLongitude();
                            mCity = bdLocation.getCity();
                            mAddress = bdLocation.getAddrStr();
                            mBdLocation = bdLocation;
                            mHasInitialLocation = true;
                        } else {

                            mLocation[0] = 0;
                            mLocation[1] = 0;

                        }

                        do {
                            ObservableEmitter<BDLocation> e = mSubscribers.remove(0);
                            if (e.isDisposed()) {
                                continue;
                            }
                            e.onNext(bdLocation);
                            mBdLocation = bdLocation;
                            if (mSubscribers.size() == 0) {
                                mLocationClient.unRegisterLocationListener(this);
                                mLocationClient.stop();
                            }
                        } while (mSubscribers.size() > 0);
                    }

                    @Override
                    public void onConnectHotSpotMessage(String s, int i) {

                    }


                });
                mLocationClient.start();
                mLastRequestTime = System.currentTimeMillis();
                mLocationClient.requestLocation();
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    public BDLocation getLastLocation() {
        if (mHasInitialLocation) {
            return mBdLocation;
        } else {
            subscribeNewLocation()
                    .observeOn(Schedulers.computation())
                    .subscribe(new Consumer<BDLocation>() {

                        @Override
                        public void accept(BDLocation bdLocation) throws Exception {
                            if (bdLocation != null && (bdLocation.getLatitude() != 0 || bdLocation.getLongitude() != 0)) {
                                mBdLocation = bdLocation;
                            } else {
                                DevUtil.e("LocationSource", "get BdLocation is Failure");
                            }
                        }
                    });
            return mBdLocation;
        }
    }

    public Observable<BDLocation> subscribeLastLocation() {
        if (mHasInitialLocation) {
            return Observable.just(mBdLocation);
        } else {
            return subscribeNewLocation();
        }
    }

    public Observable<LatLng> subscribeNewLocationWithLat() {
        return Observable.create(new ObservableOnSubscribe<LatLng>() {

            @Override
            public void subscribe(@NonNull final ObservableEmitter<LatLng> e) throws Exception {
                mSubscribers.add(e);
                if (mSubscribers.size() > 1) {
                    return;
                }
                if (mLocationClient.isStarted() || System.currentTimeMillis() - mLastRequestTime < 1000L) {
                    try {
                        ObservableEmitter<LatLng> newe = mSubscribers.remove(0);

                        if (newe.isDisposed()) {
                            return;
                        }
                        newe.onNext(new LatLng(mLocation[0], mLocation[1]));
                        if (mSubscribers.size() == 0) {
                            mLocationClient.stop();
                        }
                    } catch (Exception ep) {
                        DevUtil.v("location", "remove index failure:" + ep.getMessage());
                    }
                    return;
                }

                mLocationClient.registerLocationListener(new BDLocationListener() {

                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {
                        if (bdLocation != null && bdLocation.getAddrStr() != null && bdLocation.getCity() != null) {
                            mLocation[0] = bdLocation.getLatitude();
                            mLocation[1] = bdLocation.getLongitude();
                            mCity = bdLocation.getCity();
                            mAddress = bdLocation.getAddrStr();
                            mHasInitialLocation = true;
                        } else {
                            //拉不到定位信息时，给经纬度默认值
                            mLocation[0] = 0;
                            mLocation[1] = 0;
                        }

                        do {
                            ObservableEmitter<LatLng> e = mSubscribers.remove(0);
                            if (e.isDisposed()) {
                                continue;
                            }
                            e.onNext(new LatLng(mLocation[0], mLocation[1]));

                            if (mSubscribers.size() == 0) {
                                mLocationClient.unRegisterLocationListener(this);
                                mLocationClient.stop();
                            }
                        } while (mSubscribers.size() > 0);
                    }

                    @Override
                    public void onConnectHotSpotMessage(String s, int i) {

                    }


                });
                mLocationClient.start();
                mLastRequestTime = System.currentTimeMillis();
                mLocationClient.requestLocation();
                e.onNext(new LatLng(mLocation[0], mLocation[1]));

            }
        }).subscribeOn(Schedulers.io());
    }


    public Observable<LatLng> subscribeLastLocationWithLat() {
        if (mHasInitialLocation) {
            return Observable.just(new LatLng(mLocation[0], mLocation[1]));
        } else {
            return subscribeNewLocationWithLat();
        }
    }

    public Observable<String> subscribeAddressByLocation(final LatLng loc) {
        return Observable
                .create(new ObservableOnSubscribe<String>() {

                    @Override
                    public void subscribe(@NonNull final ObservableEmitter<String> e) throws Exception {
                        ReverseGeoCodeOption option = new ReverseGeoCodeOption()
                                .location(loc);
                        final GeoCoder coder = GeoCoder.newInstance();
                        coder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

                            @Override
                            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                            }

                            @Override
                            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                                if (e.isDisposed()) {
                                    return;
                                }
                                if (reverseGeoCodeResult != null) {
                                    e.onNext(reverseGeoCodeResult.getAddress());
                                } else {
                                    e.onError(new RuntimeException("Failed to request address"));
                                }
                                coder.destroy();
                            }
                        });
                        coder.reverseGeoCode(option);
                    }

                });
    }

    private LocationClient initLocationClient(Context context) {
        LocationClient locationClient = new LocationClient(getApplicationContext());
        LocationClientOption options = new LocationClientOption();
        options.setOpenGps(true);
        options.setCoorType("bd09ll");
        options.setAddrType("all"); // 返回的定位结果包含地址信息
        options.setIsNeedAddress(true);         // 设置是否需要地址信息
        options.setIsNeedLocationPoiList(true);//设置是否需要返回位置POI信息，可以在BDLocation.getPoiList()中得到数据
        locationClient.setLocOption(options);
        return locationClient;
    }

    public String getCity() {
        return mCity;
    }

    public String getAddress() {
        return mAddress;
    }

    public BDLocation getBdLocation() {
        return mBdLocation;
    }
}
