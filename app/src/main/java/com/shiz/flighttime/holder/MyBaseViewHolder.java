package com.shiz.flighttime.holder;

import android.view.View;
import android.widget.FrameLayout;

import com.shiz.flighttime.R;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;

/**
 * Created by OldMan on 28.04.2016.
 */
class MyBaseViewHolder extends AbstractDraggableSwipeableItemViewHolder implements ExpandableItemViewHolder {
    public FrameLayout mContainer;
    public View mDragHandle;
    private int mExpandStateFlags;

    MyBaseViewHolder(View v) {
        super(v);
        mContainer = (FrameLayout) v.findViewById(R.id.container);
        mDragHandle = v.findViewById(R.id.drag_handle);

    }

    @Override
    public int getExpandStateFlags() {
        return mExpandStateFlags;
    }

    @Override
    public void setExpandStateFlags(int flag) {
        mExpandStateFlags = flag;
    }

    @Override
    public View getSwipeableContainerView() {
        return mContainer;
    }
}