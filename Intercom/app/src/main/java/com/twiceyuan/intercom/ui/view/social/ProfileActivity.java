package com.twiceyuan.intercom.ui.view.social;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.twiceyuan.intercom.R;
import com.twiceyuan.intercom.common.FirebaseUtil;
import com.twiceyuan.intercom.common.RequestCode;
import com.twiceyuan.intercom.common.Toaster;
import com.twiceyuan.intercom.injector.components.UserActivityComponent;
import com.twiceyuan.intercom.model.local.User;
import com.twiceyuan.intercom.model.remote.UserReference;
import com.twiceyuan.intercom.ui.base.CanBack;
import com.twiceyuan.intercom.ui.base.Dialogs;
import com.twiceyuan.intercom.ui.base.PresenterUserActivity;
import com.twiceyuan.intercom.usecase.ImageHost;
import com.twiceyuan.log.L;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by twiceYuan on 8/2/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 个人资料界面，自己的和其他人是通用的
 * <p>
 * UPDATE: 8/30/16 个人资料编辑和查看还是分开比较好
 */
public class ProfileActivity extends PresenterUserActivity<ProfileContract.Presenter> implements
        CanBack, ProfileContract.View {

    private static final int MENU_ITEM_SUBMIT   = 1001;
    private static final int REQUEST_PICK_IMAGE = RequestCode.INSTANCE.get();

    @BindView(R.id.et_username)         MaterialEditText etUsername;
    @BindView(R.id.et_email)            MaterialEditText etEmail;
    @BindView(R.id.et_user_id)          MaterialEditText etUserId;
    @BindView(R.id.imageAvatarProgress) ProgressBar      mAvatarProgress;
    @BindView(R.id.img_question)        ImageView        imgQuestion;
    @BindView(R.id.imageAvatar)         ImageView        imageAvatar;
    @BindView(R.id.progressBar)         View             mProgressBar;

    @Inject FirebaseUser  mFirebaseUser;
    @Inject UserReference mUserReference;
    @Inject ImageHost     mImageHost;

    public static void start(Context context) {
        Intent starter = new Intent(context, ProfileActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.VISIBLE);

        getPresenter().fetchProfile();
    }

    @Override
    public void onProfileUpdate(User user) {
        etUsername.setText(user.username);
        etEmail.setText(user.email);
        etUserId.setText(user.globalId);

        if (FirebaseUtil.INSTANCE.isEmptyUrl(user.photoUrl)) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            mAvatarProgress.setVisibility(View.VISIBLE);
            loadImage(user.photoUrl, drawable -> {
                mAvatarProgress.setVisibility(View.GONE);
                imageAvatar.setImageDrawable(drawable);
                mProgressBar.setVisibility(View.INVISIBLE);
            });
        }

        imageAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        });
    }

    @OnClick(R.id.img_question)
    public void globalIdTips() {
        Dialogs.simpleMessage(this, getString(R.string.global_id_tip_title), getString(R.string.global_id_tip), () -> {
        }, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_SUBMIT, 0, R.string.submit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_ITEM_SUBMIT) {
            User user = getPresenter().getUser();

            user.username = etUsername.getText().toString();
            user.globalId = etUserId.getText().toString();

            getPresenter().submit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInject(UserActivityComponent component) {
        component.inject(this);
    }

    @Override
    public void onSubmitProfileSuccess() {
        Toaster.INSTANCE.s(getString(R.string.submit_success));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                CropImage.activity(data.getData())
                        .setRequestedSize(256, 256, CropImageView.RequestSizeOptions.SAMPLING)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .start(this);
            } else {
                Toaster.INSTANCE.s("选择图片失败");
            }
            return;
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mAvatarProgress.setVisibility(View.VISIBLE);
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                runApi(mImageHost.uploadImage(result.getUri()),
                        image -> uploadedImage(image.url),
                        error -> mAvatarProgress.setVisibility(View.GONE));
            } else {
                Toaster.INSTANCE.s("裁剪图片失败");
            }
            return;
        }
        requestCodeNotFound();
    }

    private void uploadedImage(String url) {
        L.i("图片上传完成, 地址: %s", url);
        /**
         * 上传头像完成后, 将头像邮箱置空(因为上传的方式和使用 Gravatar 方式只能选择一种)
         */
        getPresenter().getUser().photoUrl = url;
        getPresenter().getUser().photoEmail = null;

        loadImage(url, (drawable) -> {
            imageAvatar.setImageDrawable(drawable);
            mAvatarProgress.setVisibility(View.GONE);
        });
    }
}
