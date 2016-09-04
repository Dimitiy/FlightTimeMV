package com.shiz.flighttime.listener;

/**
 * Created by OldMan on 14.04.2016.
 */
public interface DeleteDialogClickListener {
    void onClickToDeleteMission(int position);

    void editMissionSuccess();

    void onNoClick(int groupPosition, int childPosition);

    void onClickToDeleteFlight(int groupPosition, int childPosition);
}
