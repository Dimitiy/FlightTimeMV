/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.android.flighttime.data;

import com.android.flighttime.database.FlightDB;
import com.android.flighttime.database.MissionDB;

public abstract class AbstractExpandableDataProvider {
    public static abstract class BaseData {
        public abstract void setPinned(boolean pinned);

        public abstract boolean isPinned();
    }

    public static abstract class MissionData extends BaseData {
        public abstract boolean isSectionHeader();

        public abstract int getMissionId();

        public abstract MissionDB getMission();
    }

    public static abstract class FlightData extends BaseData {
        public abstract long getFlightId();

        public abstract FlightDB getFlight();
    }

    public abstract int getMissionCount();

    public abstract int getFlightCount(int groupPosition);

    public abstract MissionData getMissionItem(int groupPosition);

    public abstract FlightData getFlightItem(int groupPosition, int childPosition);

    public abstract void moveMissionItem(int fromGroupPosition, int toGroupPosition);

    public abstract void moveFlightItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition);

    public abstract void removeMissionItem(int groupPosition);

    public abstract void removeFlightItem(int groupPosition, int childPosition);

    public abstract void addFlightItem(int groupPosition, FlightDB flight);

    public abstract long undoLastRemoval();
}
