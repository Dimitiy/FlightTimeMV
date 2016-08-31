package com.android.flighttime.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.flighttime.R;
import com.android.flighttime.data.YearEntity;
import com.android.flighttime.holder.MyGroupViewHolder;
import com.android.flighttime.holder.YearsViewHolder;

import java.util.ArrayList;

/**
 * Created by oldman on 24.08.16.
 */
public class YearsCoverFlowAdapter extends RecyclerView.Adapter<YearsViewHolder> {

    private ArrayList<YearEntity> mData = new ArrayList<>(0);
    private Context mContext;
    private int lastPosition = -1;
    private TextView year;


    private TextView countHoursInYear;

    public YearsCoverFlowAdapter(Context context, ArrayList<YearEntity> data) {
        mContext = context;
        mData = data;
    }

    public void swap(ArrayList<YearEntity> data) {
        if (mData != null) {
            Log.d("swap", data.listIterator().toString());
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public YearsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_year_view, parent, false);
        return new YearsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(YearsViewHolder holder, int position) {
        year = holder.year;
        countHoursInYear = holder.countHoursInYear;

        year.setText(mData.get(position).getYears());
        countHoursInYear.setText(mData.get(position).getCountHoursInYear());
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    public YearEntity getItem(int pos) {
        return mData.get(pos);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public String getYear() {
        return year.toString();
    }

    public TextView getCountHoursInYear() {
        return countHoursInYear;
    }
}
