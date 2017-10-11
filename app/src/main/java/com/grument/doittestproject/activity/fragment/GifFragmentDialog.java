package com.grument.doittestproject.activity.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.grument.doittestproject.R;
import com.grument.doittestproject.retrofit.RetrofitBuilder;
import com.grument.doittestproject.retrofit.RetrofitRequestResponseService;
import com.grument.doittestproject.util.PreferenceHelper;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;



public class GifFragmentDialog extends DialogFragment {

    public static GifFragmentDialog newInstance() {
        return new GifFragmentDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View gifFragmentDialog = inflater.inflate(R.layout.dialog_gif, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ImageView gifImageView = (ImageView) gifFragmentDialog.findViewById(R.id.iv_gif_animation);

        RetrofitRequestResponseService retrofitRequestResponseService = new RetrofitBuilder().build().create(RetrofitRequestResponseService.class);

        PreferenceHelper preferenceHelper = new PreferenceHelper(getActivity());

        retrofitRequestResponseService.loadGif(preferenceHelper.getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loadGifResponse -> {
                    if (loadGifResponse.isSuccessful()) {

                       Glide.with(this)
                                .load(loadGifResponse.body().getGifUrlPath())
                                .asGif()
                                .error(R.drawable.gif_default)
                                .centerCrop()
                                .override(gifImageView.getWidth(), gifImageView.getHeight())
                                .into(gifImageView);

                    }
                });

        return gifFragmentDialog;
    }


    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

}
