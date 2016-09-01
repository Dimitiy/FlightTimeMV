package com.shiz.flighttime.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shiz.flighttime.R;

/**
 * Created by oldman on 24.08.16.
 */
public class YearsViewHolder extends RecyclerView.ViewHolder {
    public TextView year, countHoursInYear;

    public YearsViewHolder(View v) {
        super(v);
        year = (TextView) v.findViewById(R.id.year);
        countHoursInYear = (TextView) v.findViewById(R.id.count_years_in_year);
    }
}
