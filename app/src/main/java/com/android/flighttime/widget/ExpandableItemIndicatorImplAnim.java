package com.android.flighttime.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.android.flighttime.R;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ExpandableItemIndicatorImplAnim extends ExpandableItemIndicator.Impl {
    private AppCompatImageView mImageView;

    @Override
    public void onInit(Context context, AttributeSet attrs, int defStyleAttr, ExpandableItemIndicator thiz) {
        View v = LayoutInflater.from(context).inflate(R.layout.widget_expandable_item_indicator, thiz, true);
        mImageView = (AppCompatImageView) v.findViewById(R.id.image_view);
    }

    @Override
    public void setExpandedState(boolean isExpanded, boolean animate) {
        if (animate) {
            @DrawableRes int resId = isExpanded ? R.drawable.ic_expand_more_to_expand_less : R.drawable.ic_expand_less_to_expand_more;
            mImageView.setImageResource(resId);
            ((Animatable) mImageView.getDrawable()).start();
        } else {
            @DrawableRes int resId = isExpanded ? R.drawable.ic_expand_less : R.drawable.ic_expand_more;
            mImageView.setImageResource(resId);
        }
    }
}
