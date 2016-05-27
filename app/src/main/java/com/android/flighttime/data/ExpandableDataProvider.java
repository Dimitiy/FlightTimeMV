package com.android.flighttime.data;

/**
 * Created by OldMan on 22.05.2016.
 */
import android.support.v4.util.Pair;
import android.util.Log;

import com.android.flighttime.database.FlightDB;
import com.android.flighttime.database.MissionDB;
import com.android.flighttime.data.AbstractExpandableDataProvider;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExpandableDataProvider extends AbstractExpandableDataProvider {
    private static final String TAG = ExpandableDataProvider.class.getSimpleName().toString();
    private List<Pair<MissionData, List<FlightData>>> mData;

    // for undo group item
    private Pair<MissionData, List<FlightData>> mLastRemovedGroup;
    private int mLastRemovedGroupPosition = -1;

    // for undo child item
    private FlightData mLastRemovedFlight;
    private long mLastRemovedFlightMissionGroupId = -1;
    private int mLastRemovedFlightPosition = -1;

    public ExpandableDataProvider(List<MissionDB> missionDBlList) {
        int groupId = 0;
        mData = new LinkedList<>();
        if (!missionDBlList.isEmpty())
            for (MissionDB mission : missionDBlList) {
                Log.d(TAG, "groupId " + groupId);
                final ConcreteMissionData concreteMissionData = new ConcreteMissionData(groupId, mission);
                final List<FlightDB> flightList = mission.getFlightDBRealmList();
                final List<FlightData> flightData = new ArrayList<>();

                for (FlightDB flight : flightList) {
                    final int childId = concreteMissionData.generateNewChildId();

                    Log.d(TAG, "childId " + childId + " id "  + flight.getId());
                    flightData.add(new ConcreteFlightData(childId, flight));
                }
                mData.add(new Pair<MissionData, List<FlightData>>(concreteMissionData, flightData));
                groupId++;
            }
    }

    @Override
    public int getMissionCount() {
        return mData.size();
    }

    @Override
    public int getFlightCount(int groupPosition) {
        return mData.get(groupPosition).second.size();
    }

    @Override
    public MissionData getMissionItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getMissionCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        return mData.get(groupPosition).first;
    }

    @Override
    public FlightData getFlightItem(int groupPosition, int childPosition) {
        if (groupPosition < 0 || groupPosition >= getMissionCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        final List<FlightData> children = mData.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }

        return children.get(childPosition);
    }

    @Override
    public void moveMissionItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }

        final Pair<MissionData, List<FlightData>> item = mData.remove(fromGroupPosition);
        mData.add(toGroupPosition, item);
    }

    @Override
    public void moveFlightItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }

        final Pair<MissionData, List<FlightData>> fromGroup = mData.get(fromGroupPosition);
        final Pair<MissionData, List<FlightData>> toGroup = mData.get(toGroupPosition);

        final ConcreteFlightData item = (ConcreteFlightData) fromGroup.second.remove(fromChildPosition);

        if (toGroupPosition != fromGroupPosition) {
            // assign a new ID
            final int newId = ((ConcreteMissionData) toGroup.first).generateNewChildId();
            item.setChildId(newId);
        }

        toGroup.second.add(toChildPosition, item);
    }

    @Override
    public void removeMissionItem(int groupPosition) {
        Log.d(TAG,  "removeMissionItem" + groupPosition + mData.size());

        mLastRemovedGroup = mData.remove(groupPosition);
        mLastRemovedGroupPosition = groupPosition;

        mLastRemovedFlight = null;
        mLastRemovedFlightMissionGroupId = -1;
        mLastRemovedFlightPosition = -1;
    }

    @Override
    public void removeFlightItem(int groupPosition, int childPosition) {
        mLastRemovedFlight = mData.get(groupPosition).second.remove(childPosition);
        mLastRemovedFlightMissionGroupId = mData.get(groupPosition).first.getMissionId();
        mLastRemovedFlightPosition = childPosition;

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;
    }

    @Override
    public void addFlightItem(int groupPosition, FlightDB flight) {
        final Pair<MissionData, List<FlightData>> missionGroup = mData.get(groupPosition);
        // assign a new ID
        final int newId = ((ConcreteMissionData) missionGroup.first).generateNewChildId();
        missionGroup.second.add(getFlightCount(groupPosition), new ConcreteFlightData(newId, flight));
    }


    @Override
    public long undoLastRemoval() {
        if (mLastRemovedGroup != null) {
            return undoGroupRemoval();
        } else if (mLastRemovedFlight != null) {
            return undoChildRemoval();
        } else {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }
    }

    private long undoGroupRemoval() {
        int insertedPosition;
        if (mLastRemovedGroupPosition >= 0 && mLastRemovedGroupPosition < mData.size()) {
            insertedPosition = mLastRemovedGroupPosition;
        } else {
            insertedPosition = mData.size();
        }

        mData.add(insertedPosition, mLastRemovedGroup);

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;

        return RecyclerViewExpandableItemManager.getPackedPositionForGroup(insertedPosition);
    }

    private long undoChildRemoval() {
        Pair<MissionData, List<FlightData>> group = null;
        int groupPosition = -1;

        // find the group
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).first.getMissionId() == mLastRemovedFlightMissionGroupId) {
                group = mData.get(i);
                groupPosition = i;
                break;
            }
        }

        if (group == null) {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }

        int insertedPosition;
        if (mLastRemovedFlightPosition >= 0 && mLastRemovedFlightPosition < group.second.size()) {
            insertedPosition = mLastRemovedFlightPosition;
        } else {
            insertedPosition = group.second.size();
        }

        group.second.add(insertedPosition, mLastRemovedFlight);

        mLastRemovedFlightMissionGroupId = -1;
        mLastRemovedFlightPosition = -1;
        mLastRemovedFlight = null;

        return RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, insertedPosition);
    }

    public static final class ConcreteMissionData extends MissionData {

        private final int mId;
        private final MissionDB mission;
        private boolean mPinned;
        private int mNextChildId = 0;


        ConcreteMissionData(int id, MissionDB mission) {
            mId = id;
            this.mission = mission;
        }

        @Override
        public int getMissionId() {
            return mId;
        }

        @Override
        public MissionDB getMission() {
            return mission;
        }

        @Override
        public boolean isSectionHeader() {
            return false;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        @Override
        public void setPinned(boolean pinnedToSwipeLeft) {
            mPinned = pinnedToSwipeLeft;
        }

        public int generateNewChildId() {
            final int id = mNextChildId;
            mNextChildId += 1;
            return id;
        }
    }

    public static final class ConcreteFlightData extends FlightData {

        private final FlightDB flight;
        private int mId;
        private boolean mPinned;

        ConcreteFlightData(int id, FlightDB flight) {
            mId = id;
            this.flight = flight;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        @Override
        public void setPinned(boolean pinned) {
            mPinned = pinned;
        }

        public void setChildId(int id) {
            this.mId = id;
        }

        @Override
        public long getFlightId() {
            return mId;
        }

        @Override
        public FlightDB getFlight() {
            return flight;
        }
    }

}
