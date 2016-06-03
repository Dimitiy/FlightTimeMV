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

package com.android.flighttime.main;

import android.content.Context;
import android.util.Log;

import com.android.flighttime.R;
import com.android.flighttime.database.DBHelper;
import com.android.flighttime.database.MissionDB;
import com.android.flighttime.utils.Formatter;
import com.roughike.swipeselector.SwipeItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class FindItemsInteractorImpl implements FindItemsInteractor {
    private Context context;
    private String TAG = FindItemsInteractorImpl.class.getSimpleName();

    public FindItemsInteractorImpl(Context context) {
        this.context = context;
    }

    @Override
    public void findYearsItems(DBHelper realm, OnYearsFinishedListener listener) {
        int index = 0;
        Map<String, Long> contentMap = realm.getDataOfYear();
        ArrayList<SwipeItem> swipe = new ArrayList<SwipeItem>();
        if (!contentMap.isEmpty()) {
            for (Iterator<String> it = contentMap.keySet().iterator(); it.hasNext(); ) {
                String key = it.next();
                swipe.add(new SwipeItem(index, String.format(context.getString(R.string.format_year), key),
                        String.format(context.getString(R.string.format_duration), Formatter.getFormatTime(contentMap.get(key)))));
                index += 1;
            }
        } else
            swipe.add(new SwipeItem(index, context.getResources().getString(R.string.empty_mission),
                    ""));
        Log.d(TAG, swipe.listIterator().toString());
        listener.onYearsFinished(swipe.toArray(new SwipeItem[swipe.size()]));
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
