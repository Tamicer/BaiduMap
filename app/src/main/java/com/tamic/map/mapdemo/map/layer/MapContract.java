/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tamic.map.mapdemo.map.layer;

import android.app.Activity;

import com.tamic.map.mapdemo.map.BasePresnter;
import com.tamic.map.mapdemo.map.IBaseView;
import com.tamic.map.mapdemo.map.MapConstant;


/**
 * This specifies the contract between the view and the presenter.
 * Created by Tamic on 2017-11-06.
 */
public interface MapContract extends MapConstant {

    interface View extends IBaseView<IPresenter> {

        int getCity();

        void setCity(int c);

        Activity getActivity();

    }

    interface IPresenter extends BasePresnter, MapConstant {


        void queryChannels();

        void checkCityState();

        void checkLocationState();

        boolean setBusinessType(HouseType type);

        HouseType getBusinessType();

        ViewCase getViewCase();

    }

}
