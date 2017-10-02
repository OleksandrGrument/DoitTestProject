package com.grument.doittestproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.grument.doittestproject.R;
import com.grument.doittestproject.activity.base.BaseFullscreenActivity;
import com.grument.doittestproject.util.AppUtil;
import com.grument.doittestproject.util.PermissionManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.grument.doittestproject.util.Constants.PICK_PHOTO_FOR_AVATAR;


public class RegistrationPickAvatarActivity extends BaseFullscreenActivity {

    @BindView(R.id.iv_pick_user_avatar)
    ImageView pickUserAvatarImageView;

    @BindView(R.id.bt_pick_avatar)
    Button pickAvatarButton;

    @BindView(R.id.bt_registration_continue)
    Button continueButton;

    private Uri imageFileUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pick_avatar);
        ButterKnife.bind(this);


        pickAvatarButton.setOnClickListener(v -> {
            if (!PermissionManager.checkPermissionGranted(RegistrationPickAvatarActivity.this, PermissionManager.REQUEST_CAMERA)
                    || !PermissionManager.checkPermissionGranted(RegistrationPickAvatarActivity.this, PermissionManager.REQUEST_STORAGE)) {

                PermissionManager.verifyCameraPermissions(RegistrationPickAvatarActivity.this);
                PermissionManager.verifyStoragePermissions(RegistrationPickAvatarActivity.this);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
            }
        });

        continueButton.setEnabled(false);

        continueButton.setOnClickListener(v -> {
            Intent startRegistrationActivityIntent = new Intent(RegistrationPickAvatarActivity.this, RegistrationActivity.class);
            startRegistrationActivityIntent.putExtra("imageFileUri", imageFileUri);
            startActivity(startRegistrationActivityIntent);
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

            AppUtil.putImageByUriInCircleImageView(this, imageFileUri , pickUserAvatarImageView);

            continueButton.setEnabled(true);
        }
    }

}
