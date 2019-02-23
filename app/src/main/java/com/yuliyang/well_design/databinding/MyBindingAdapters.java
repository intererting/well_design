package com.yuliyang.well_design.databinding;

import com.yuliyang.well_design.R;

import androidx.databinding.BindingAdapter;

public class MyBindingAdapters {

    @BindingAdapter(value = {"android:bindTag"})
    public static void bindTag(DataBindingCstmView view, String tag) {
        view.findViewById(R.id.desc).setTag(tag);
    }
}
