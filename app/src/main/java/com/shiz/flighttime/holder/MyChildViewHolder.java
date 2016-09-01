package com.shiz.flighttime.holder;

import android.view.View;
import android.widget.TextView;

import com.shiz.flighttime.R;


/**
 * Created by OldMan on 28.04.2016.
 */
public class MyChildViewHolder extends MyBaseViewHolder {
    public TextView textDate, textCount;

    public MyChildViewHolder(View v) {
        super(v);
        textDate = (TextView) v.findViewById(R.id.item_flight);
        textCount = (TextView) v.findViewById(R.id.text_count_hour_flight);
    }
}