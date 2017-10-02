package com.grument.doittestproject.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.grument.doittestproject.R;
import com.grument.doittestproject.activity.base.BaseFullscreenActivity;
import com.grument.doittestproject.dto.SignInResponseDTO;
import com.grument.doittestproject.retrofit.RetrofitBuilder;
import com.grument.doittestproject.retrofit.RetrofitRequestResponseService;
import com.grument.doittestproject.util.AppUtil;
import com.grument.doittestproject.util.PreferenceHelper;
import com.grument.doittestproject.view.RxEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LoginActivity extends BaseFullscreenActivity {


    @BindView(R.id.iv_login_user_avatar)
    ImageView avatarImageView;

    @BindView(R.id.et_login_email)
    EditText loginEmailEditText;

    @BindView(R.id.et_login_password)
    EditText loginPasswordEditText;

    @BindView(R.id.bt_login)
    Button loginButton;

    @BindView(R.id.bt_login_sign_up)
    Button signUpButton;

    @BindView(R.id.pb_login)
    ProgressBar loginProgressBar;

    private RetrofitRequestResponseService retrofitRequestResponseService;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        retrofitRequestResponseService = new RetrofitBuilder().build().create(RetrofitRequestResponseService.class);

        checkAuthorization();

        Observable<String> loginObservable = RxEditText.getTextWatcherObservable(loginEmailEditText);
        Observable<String> passwordObservable = RxEditText.getTextWatcherObservable(loginPasswordEditText);

        disposables.add(
                Observable.combineLatest(
                        passwordObservable, loginObservable, (BiFunction<String, String, Object>) (s, s2)
                                -> !(s.isEmpty() || s2.isEmpty())).subscribe(this::setLoginButtonEnabled));

        setLoginButtonEnabled(false);
        loginButton.setOnClickListener(v -> startLoginAuthorization(loginEmailEditText.getText().toString(), loginPasswordEditText.getText().toString()));

        signUpButton.setOnClickListener(v -> {
            Intent myIntent = new Intent(LoginActivity.this, RegistrationPickAvatarActivity.class);
            LoginActivity.this.startActivity(myIntent);
        });

    }

    private void checkAuthorization() {
        preferenceHelper = new PreferenceHelper(this);

        String avatarImageLink = preferenceHelper.getAvatarImageLink();

        if (!avatarImageLink.isEmpty())
            AppUtil.putImageByUriInCircleImageView(this, Uri.parse(avatarImageLink), avatarImageView);

        boolean isAuthorized = preferenceHelper.getAuthorizationState();

        if (isAuthorized)
            startLoginAuthorization(preferenceHelper.getUserLogin(), preferenceHelper.getUserPassword());

    }

    private void startLoginAuthorization(String loginEmail, String loginPassword) {

        disableInputs();

        retrofitRequestResponseService.loginUser(loginEmail, loginPassword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginResponse -> {

                    Timber.i(loginResponse.toString());

                    if (loginResponse.isSuccessful()) {

                        SignInResponseDTO signInResponseDTO = loginResponse.body();

                        preferenceHelper.saveUserToken(signInResponseDTO.getToken());
                        preferenceHelper.saveAuthorizationState(true);
                        preferenceHelper.saveUserPassword(loginPassword);
                        preferenceHelper.saveUserLogin(loginEmail);
                        preferenceHelper.saveAvatarImageLink(signInResponseDTO.getAvatarImageLink());

                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));

                    } else {
                        AppUtil.showToastError(this, getString(R.string.authorization_error));
                    }

                    enableInputs();
                });

    }

    private void disableInputs() {
        loginEmailEditText.setEnabled(false);
        loginPasswordEditText.setEnabled(false);
        loginProgressBar.setVisibility(View.VISIBLE);
        signUpButton.setEnabled(false);
        setLoginButtonEnabled(false);
    }

    private void enableInputs() {
        loginEmailEditText.setEnabled(true);
        loginPasswordEditText.setEnabled(true);
        loginProgressBar.setVisibility(View.GONE);
        signUpButton.setEnabled(true);
        setLoginButtonEnabled(true);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void setLoginButtonEnabled(Object o) {
        loginButton.setEnabled((Boolean) o);
    }

}
