package com.shiz.flighttime.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shiz.flighttime.R;
import com.shiz.flighttime.widget.ExpandableItemIndicator;

/**
 * Created by OldMan on 28.04.2016.
 */
public class MyGroupViewHolder extends MyBaseViewHolder {
    public ExpandableItemIndicator mIndicator;
    public TextView missionText, textDate, textCount;
    public ImageButton addFlightButton, editMissionButton;

    public MyGroupViewHolder(View v, View.OnClickListener clickListener) {
        super(v);
        mIndicator = (ExpandableItemIndicator) v.findViewById(R.id.indicator);
        missionText = (TextView) v.findViewById(R.id.item_mission);
        textDate = (TextView) v.findViewById(R.id.textDate);
        textCount = (TextView) v.findViewById(R.id.textCount);
        addFlightButton = (ImageButton) v.findViewById(R.id.addFlight);
        editMissionButton = (ImageButton) v.findViewById(R.id.editMission);

        // set listeners
        itemView.setOnClickListener(clickListener);
        addFlightButton.setOnClickListener(clickListener);
        editMissionButton.setOnClickListener(clickListener);

    }

    @Override
    public View getSwipeableContainerView() {
        return mContainer;
    }

}