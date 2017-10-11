package com.grument.doittestproject.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.Editable;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.grument.doittestproject.R;
import com.grument.doittestproject.model.Coordinates;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.grument.doittestproject.util.Constants.IMAGE_MEDIA_TYPE;


public class AppUtil {

    public static RequestBody parseStringIntoRequestBody(String string) {
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }

    public static RequestBody parseStringIntoRequestBody(Editable editable) {
        return RequestBody.create(MediaType.parse("text/plain"), editable.toString());
    }

    public static void showToastError(Context context, String errorText) {
        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show();
    }

    public static RequestBody getImageRequestBody(File imageFile) {
        return RequestBody.create(MediaType.parse(IMAGE_MEDIA_TYPE), imageFile);
    }

    public static void putImageByUriInCircleImageView(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .error(R.drawable.before_pick_avatar)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);
    }

    public static void putImageByUriInSquareImageView(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .error(R.drawable.before_pick_avatar)
                .centerCrop()
                .bitmapTransform(new CropSquareTransformation(context))
                .into(imageView);
    }


    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static boolean checkIfEditTextAndHideKeyboard(Activity activity , MotionEvent motionEvent ,boolean ret) {
        View view = activity.getCurrentFocus();

        if (view instanceof EditText) {
            View w = activity.getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = motionEvent.getRawX() + w.getLeft() - scrcoords[0];
            float y = motionEvent.getRawY() + w.getTop() - scrcoords[1];

            if (motionEvent.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom())) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }


    public static boolean isStringNotEmpty(String string){
        return string != null && string.length() > 0;
    }
}
