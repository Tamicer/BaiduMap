package com.tamic.map.mapdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.tamic.map.mapdemo.map.MapConstant;
import com.tamic.map.mapdemo.map.MapListPresnter;
import com.tamic.map.mapdemo.map.layer.ListParamBuilder;
import com.tamic.map.mapdemo.map.layer.MapCase;
import com.tamic.map.mapdemo.map.layer.MapContract;
import com.tamic.map.mapdemo.map.layer.ViewCase;
import com.tamic.map.mapdemo.map.model.MapData;

import static com.tamic.map.mapdemo.map.MapUtils.checkNotNull;

public class MainActivity extends AppCompatActivity implements ViewCase, MapConstant, MapContract.View, MapCase {
    protected MapListPresnter mMapPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapPresenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void addMark() {

    }

    @Override
    public void clickPoi() {

    }

    @Override
    public void zoom() {

    }

    @Override
    public void reMoveMarks() {

    }

    @Override
    public void setPresenter(MapContract.IPresenter presenter) {
        mMapPresenter = (MapListPresnter) checkNotNull(presenter);
    }

    @Override
    public void start() {

    }

    @Override
    public Disposable observeMapData(Observable<MapData> observable) {
        return null;
    }

    @Override
    public void switchBusinessType(HouseType type, boolean changed) {

    }

    @Override
    public void showHeadMessage(String msg, int duration) {

    }

    @Override
    public void refreshMapLevel(int zoomLevel) {

    }

    @Override
    public Disposable observeMapWithID(Observable<ListParamBuilder> observable) {
        return null;
    }

    @Override
    public Disposable observeParamBuilder(Observable<ListParamBuilder> observable) {
        return null;
    }

    @Override
    public Disposable observeCityCenter(Observable<LatLng> observable) {
        return null;
    }

    @Override
    public Disposable observeLocation(Observable<LatLng> observable) {
        return null;
    }

    @Override
    public Disposable observeListData(Observable<ListBaseBean<MapItem>> observable) {
        return null;
    }

    @Override
    public Observable<MapData> filterMapDataOnMarkClicked(Observable<MapData> observable) {
        return null;
    }

    @Override
    public ListParamBuilder getParamBuilder() {
        return null;
    }

    @Override
    public void hideHeadMessage() {

    }

    @Override
    public void changeSelectedCommunity(Integer previousId, Integer id) {

    }

    @Override
    public void closeList() {

    }

    @Override
    public void openList() {

    }

    @Override
    public void createHouseListFragment() {

    }

    @Override
    public void onMarkClick(ListParamBuilder builder) {

    }

    @Override
    public int getCity() {
        return 0;
    }

    @Override
    public void setCity(int c) {

    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapPresenter.destroy();
    }
}
