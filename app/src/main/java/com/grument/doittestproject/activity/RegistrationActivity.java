package com.grument.doittestproject.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.grument.doittestproject.R;
import com.grument.doittestproject.activity.base.BaseFullscreenActivity;
import com.grument.doittestproject.retrofit.RetrofitBuilder;
import com.grument.doittestproject.retrofit.RetrofitRequestResponseService;
import com.grument.doittestproject.util.AppUtil;
import com.grument.doittestproject.util.PathUtil;
import com.grument.doittestproject.util.PreferenceHelper;
import com.grument.doittestproject.view.RxEditText;

import java.io.File;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

public class RegistrationActivity extends BaseFullscreenActivity {

    @BindView(R.id.et_user_name)
    EditText userNameEditText;

    @BindView(R.id.et_email)
    EditText emailEditText;

    @BindView(R.id.et_password)
    EditText passwordEditText;

    @BindView(R.id.et_confirm_password)
    EditText confirmPasswordEditText;

    @BindView(R.id.bt_registration_sign_up)
    Button registrationSignUpButton;

    @BindView(R.id.pb_registration)
    ProgressBar registrationProgressBar;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private RetrofitRequestResponseService retrofitRequestResponseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        setRegistrationButtonEnabled(false);


        Observable<String> passwordObservable = RxEditText.getTextWatcherObservable(passwordEditText);
        Observable<String> confirmPasswordObservable = RxEditText.getTextWatcherObservable(confirmPasswordEditText);

        disposables.add(
                Observable.combineLatest(
                        confirmPasswordObservable, passwordObservable, (BiFunction<String, String, Object>) (s, s2)
                                -> (!s.isEmpty() && s.equals(s2) && !s2.isEmpty())).subscribe(this::setRegistrationButtonEnabled));


        retrofitRequestResponseService = new RetrofitBuilder().build().create(RetrofitRequestResponseService.class);

        Uri imageFileUri = (Uri) getIntent().getExtras().get("imageFileUri");
        File imageFile = new File(PathUtil.getPath(RegistrationActivity.this, imageFileUri));

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(imageFileUri)),
                        imageFile
                );

        MultipartBody.Part avatarBody =
                MultipartBody.Part.createFormData("avatar", imageFile.getName(), requestFile);


        registrationSignUpButton.setOnClickListener(v -> {
            v.setEnabled(false);

            if (emailEditText.getText().toString().trim().equalsIgnoreCase(""))

                emailEditText.setError(getString(R.string.registration_enter_email_error));

            else {
               disableInputs();


                retrofitRequestResponseService.createAndSignInUser(

                        AppUtil.parseStringIntoRequestBody(userNameEditText.getText().toString()),
                        AppUtil.parseStringIntoRequestBody(emailEditText.getText().toString()),
                        AppUtil.parseStringIntoRequestBody(passwordEditText.getText().toString()),
                        avatarBody)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(signInResponse -> {

                            Timber.i(signInResponse.toString());

                            if (signInResponse.isSuccessful()) {

                                PreferenceHelper preferenceHelper = new PreferenceHelper(RegistrationActivity.this);
                                preferenceHelper.saveAuthorizationState(true);
                                preferenceHelper.saveUserLogin(emailEditText.getText().toString());
                                preferenceHelper.saveUserPassword(passwordEditText.getText().toString());

                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                AppUtil.showToastError(RegistrationActivity.this, getString(R.string.registration_error));

                            }
                            enableInputs();
                        });
            }
        });
    }

    private void setRegistrationButtonEnabled(Object o) {
        registrationSignUpButton.setEnabled((Boolean) o);
    }


    private void disableInputs(){
        registrationProgressBar.setVisibility(View.VISIBLE);
        setRegistrationButtonEnabled(false);
        userNameEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        confirmPasswordEditText.setEnabled(false);
    }

    private void enableInputs(){
        registrationProgressBar.setVisibility(View.GONE);
        setRegistrationButtonEnabled(true);
        userNameEditText.setEnabled(true);
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        confirmPasswordEditText.setEnabled(true);
    }


}
