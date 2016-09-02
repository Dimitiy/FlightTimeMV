/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.shiz.flighttime.main;

import android.content.Context;

import com.shiz.flighttime.R;
import com.shiz.flighttime.database.MissionDB;
import com.shiz.flighttime.data.YearEntity;
import com.shiz.flighttime.database.DBHelper;
import com.shiz.flighttime.utils.Formatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainInteractorImpl implements MainInteractor {
    private Context context;
    private String TAG = MainInteractorImpl.class.getSimpleName();


    public MainInteractorImpl(Context context) {
        this.context = context;
    }

    @Override
    public void findYearsItems(DBHelper realm, OnYearsFinishedListener listener) {
        ArrayList<YearEntity> mData = new ArrayList<>(0);
        Map<String, Long> contentMap = realm.getDataOfYear();

        if (!contentMap.isEmpty()) {
            for (Iterator<String> it = contentMap.keySet().iterator(); it.hasNext(); ) {
                String key = it.next();
                mData.add(new YearEntity(String.format(context.getString(R.string.format_year), key),
                        Formatter.getFormatDuration(context, contentMap.get(key))));
            }
        } else
            mData.add(new YearEntity(context.getResources().getString(R.string.empty_mission),
                    ""));
        listener.onYearsFinished(mData);
    }

    @Override
    public void findMissionItems(DBHelper realm, final String year, OnMissionFinishedListener listener) {
        List<MissionDB> list = realm.getMissionsYear(getYear(year));
        listener.onMissionFinished(list);
    }

    private int getYear(String year) {
        if (year.length() <= 8) {
            return Formatter.getYearDate(year.replaceAll("[^0-9]", ""));
        }
        return 0;
    }
}
