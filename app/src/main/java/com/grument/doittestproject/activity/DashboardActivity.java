package com.grument.doittestproject.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.grument.doittestproject.R;
import com.grument.doittestproject.activity.fragment.GifFragmentDialog;
import com.grument.doittestproject.retrofit.RetrofitBuilder;
import com.grument.doittestproject.retrofit.RetrofitRequestResponseService;
import com.grument.doittestproject.activity.base.BaseActionBarActivity;
import com.grument.doittestproject.util.AppUtil;
import com.grument.doittestproject.util.PreferenceHelper;
import com.grument.doittestproject.view.DashboardItemAdapter;
import com.grument.doittestproject.view.GridSpacingItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class DashboardActivity extends BaseActionBarActivity {

    @BindView(R.id.rv_dashboard_images)
    RecyclerView dashboardImagesRecyclerView;

    @BindView(R.id.pb_loading)
    ProgressBar loadingProgressBar;

    private RetrofitRequestResponseService retrofitRequestResponseService;
    private PreferenceHelper preferenceHelper;

    private GifFragmentDialog gifFragmentDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        gifFragmentDialog = GifFragmentDialog.newInstance();
        loadingProgressBar.setVisibility(View.VISIBLE);
        dashboardImagesRecyclerView.setVisibility(View.GONE);
        dashboardImagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        dashboardImagesRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, AppUtil.dpToPx(this, 10), true));
        dashboardImagesRecyclerView.setItemAnimator(new DefaultItemAnimator());


        retrofitRequestResponseService = new RetrofitBuilder().build().create(RetrofitRequestResponseService.class);

        preferenceHelper = new PreferenceHelper(this);

        loadAllImages();

    }

    private void loadAllImages() {
        retrofitRequestResponseService.loadAllImages(preferenceHelper.getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loadAllImagesResponse -> {

                    Timber.i(loadAllImagesResponse.toString());

                    if (loadAllImagesResponse.isSuccessful()) {

                        DashboardItemAdapter dashboardItemAdapter = new DashboardItemAdapter(this, loadAllImagesResponse.body().getImageDTOList());
                        dashboardImagesRecyclerView.setAdapter(dashboardItemAdapter);
                        dashboardItemAdapter.notifyAndShow();
                    }
                    loadingProgressBar.setVisibility(View.GONE);
                    dashboardImagesRecyclerView.setVisibility(View.VISIBLE);
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.bt_play_gif:
                gifFragmentDialog.show(getFragmentManager().beginTransaction(), "GifFragmentDialog");
                return true;

            case R.id.bt_add_image:
                startActivityForResult(new Intent(DashboardActivity.this, UploadImageActivity.class), 0);
                return true;

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) loadAllImages();
    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setTitle(R.string.exit_dialog_title)
                .setNegativeButton(R.string.logout, (dialog, id) -> {
                    preferenceHelper.saveAuthorizationState(false);
                    super.onBackPressed();
                })
                .setPositiveButton(R.string.exit, (dialog, id) -> {
                    Intent quitIntent = new Intent(this, QuitActivity.class);
                    quitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(quitIntent);
                    finish();
                })
                .create()
                .show();

    }


}