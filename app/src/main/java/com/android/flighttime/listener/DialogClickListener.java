package com.android.flighttime.listener;

/**
 * Created by OldMan on 14.04.2016.
 */
public interface DialogClickListener {
    public void onClickToDeleteMission(int position);
    public void editMissionSuccess();
    public void onNoClick(int groupPosition, int childPosition);
}
