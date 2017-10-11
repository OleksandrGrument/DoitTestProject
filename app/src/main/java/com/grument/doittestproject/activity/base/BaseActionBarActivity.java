package com.grument.doittestproject.activity.base;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import com.grument.doittestproject.R;
import com.grument.doittestproject.activity.base.base.BaseAppActivity;



public class BaseActionBarActivity extends BaseAppActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.c700));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setTitle("");

    }



}
