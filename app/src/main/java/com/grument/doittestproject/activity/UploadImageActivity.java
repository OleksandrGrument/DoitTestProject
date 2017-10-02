package com.grument.doittestproject.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.grument.doittestproject.R;
import com.grument.doittestproject.retrofit.RetrofitBuilder;
import com.grument.doittestproject.retrofit.RetrofitRequestResponseService;
import com.grument.doittestproject.activity.base.BaseActionBarActivity;
import com.grument.doittestproject.util.AppUtil;
import com.grument.doittestproject.util.FileGpsUtil;
import com.grument.doittestproject.util.PathUtil;
import com.grument.doittestproject.util.PermissionManager;
import com.grument.doittestproject.util.PreferenceHelper;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

import static com.grument.doittestproject.util.Constants.PICK_PHOTO_FOR_AVATAR;

public class UploadImageActivity extends BaseActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    @BindView(R.id.iv_upload_image)
    ImageView uploadImageView;

    @BindView(R.id.et_hash_tag)
    EditText hashTagEditText;

    @BindView(R.id.et_description)
    EditText descriptionEditText;

    @BindView(R.id.pb_upload_image)
    ProgressBar uploadImageProgressBar;

    private PreferenceHelper preferenceHelper;

    private Uri imageFileUri;

    private boolean isImagePicked = false;

    private RetrofitRequestResponseService retrofitRequestResponseService;

    private GoogleApiClient googleApiClient;

    private Location lastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        ButterKnife.bind(this);
        preferenceHelper = new PreferenceHelper(this);

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        retrofitRequestResponseService = new RetrofitBuilder().build().create(RetrofitRequestResponseService.class);

        uploadImageView.setOnClickListener(v -> {

            if (!PermissionManager.checkPermissionGranted(this, PermissionManager.REQUEST_STORAGE)
                    || !PermissionManager.checkPermissionGranted(this, PermissionManager.REQUEST_CAMERA)) {

                PermissionManager.verifyCameraPermissions(this);
                PermissionManager.verifyStoragePermissions(this);

            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);

            }

        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_PHOTO_FOR_AVATAR) {
                if (data != null) {
                    imageFileUri = data.getData();
                } else {
                    return;
                }
            }

            AppUtil.putImageByUriInSquareImageView(this, imageFileUri, uploadImageView);

            isImagePicked = true;

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_upload_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.bt_upload_image) {
            if (isImagePicked) {

                disableInputs();

                File imageFile = new File(PathUtil.getPath(this,imageFileUri));


                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse(getContentResolver().getType(imageFileUri)),
                                imageFile
                        );

                MultipartBody.Part imageBody =
                        MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

                FileGpsUtil fileGpsUtil = new FileGpsUtil(imageFileUri.getPath());


                String latitude = "0";
                String longitude = "0";

                if (lastLocation != null) {
                    latitude = String.valueOf(lastLocation.getLatitude());
                    longitude = String.valueOf(lastLocation.getLongitude());
                } else if (fileGpsUtil.getCoordinates() != null) {
                    latitude = fileGpsUtil.getCoordinates().getStringLatitude();
                    longitude = fileGpsUtil.getCoordinates().getStringLongitude();
                }

                Log.i("LONGITUDE", latitude + " " + longitude);


                retrofitRequestResponseService.uploadImage(
                        preferenceHelper.getUserToken(),
                        imageBody,
                        AppUtil.parseStringIntoRequestBody(descriptionEditText.getText().toString()),
                        AppUtil.parseStringIntoRequestBody(hashTagEditText.getText().toString()),
                        AppUtil.parseStringIntoRequestBody(longitude),
                        AppUtil.parseStringIntoRequestBody(latitude))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(uploadResponse -> {

                            Timber.i(uploadResponse.toString());

                            if (uploadResponse.isSuccessful())
                                finishedSuccess();
                            else
                                AppUtil.showToastError(this, getString(R.string.upload_error));
                        });

                enableInputs();

                return true;
            } else {
                AppUtil.showToastError(this, getString(R.string.pick_up_image_error));
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
    }

    private void disableInputs() {
        uploadImageView.setEnabled(false);
        descriptionEditText.setEnabled(false);
        hashTagEditText.setEnabled(false);
        uploadImageProgressBar.setVisibility(View.VISIBLE);
        AppUtil.showToastError(this, getString(R.string.upload_start));
    }

    private void enableInputs() {
        uploadImageView.setEnabled(true);
        descriptionEditText.setEnabled(true);
        hashTagEditText.setEnabled(true);
        uploadImageProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void finishedSuccess() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
}
