package com.twiceyuan.intercom.ui.view.social;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.twiceyuan.intercom.R;
import com.twiceyuan.intercom.common.FirebaseUtil;
import com.twiceyuan.intercom.common.Toaster;
import com.twiceyuan.intercom.injector.components.UserActivityComponent;
import com.twiceyuan.intercom.model.local.User;
import com.twiceyuan.intercom.model.remote.UserReference;
import com.twiceyuan.intercom.ui.base.CanBack;
import com.twiceyuan.intercom.ui.base.Dialogs;
import com.twiceyuan.intercom.ui.base.PresenterUserActivity;

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
 * 在自己的界面上，点击每一个 item 可以编辑，在其他人的界面上点击可以复制
 */
public class ProfileActivity extends PresenterUserActivity<ProfileContract.Presenter> implements
        CanBack, ProfileContract.View {

    private static final int MENU_ITEM_SUBMIT = 1001;

    @BindView(R.id.et_username)         MaterialEditText etUsername;
    @BindView(R.id.et_email)            MaterialEditText etEmail;
    @BindView(R.id.et_user_id)          MaterialEditText etUserId;
    @BindView(R.id.img_question)        ImageView        imgQuestion;
    @BindView(R.id.et_photoUrl)         MaterialEditText etPhotoUrl;
    @BindView(R.id.progressBar)         View             mProgressBar;
    @BindView(R.id.imageAvatar)         ImageView        imageAvatar;
    @BindView(R.id.btn_testAvatar)      TextView         btnTestAvatar;
    @BindView(R.id.imageAvatarProgress) View             imageAvatarProgress;

    @Inject FirebaseUser  mFirebaseUser;
    @Inject UserReference mUserReference;

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

        // 请求并观察个人资料的变化，编辑过程中个人资料如果发生了变化就会影响到用户的输入
        getPresenter().startObserveProfile();
    }

    @Override
    public void onProfileUpdate(User user) {
        etUsername.setText(user.username);
        etPhotoUrl.setText(user.photoUrl);
        etEmail.setText(user.email);
        etUserId.setText(user.globalId);
        if (FirebaseUtil.isEmptyUrl(user.photoUrl)) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            loadImage(user.photoUrl, drawable -> {
                imageAvatar.setImageDrawable(drawable);
                mProgressBar.setVisibility(View.INVISIBLE);
            });
        }
    }

    @OnClick(R.id.img_question)
    public void globalIdTips() {
        Dialogs.simpleMessage(this, getString(R.string.global_id_tip_title), getString(R.string.global_id_tip), () -> {
        }, null);
    }

    @OnClick(R.id.btn_testAvatar)
    public void testAvatarEmail() {
        getPresenter().fetchGravatarEmail(etPhotoUrl.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_SUBMIT, 0, R.string.submit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_ITEM_SUBMIT) {
            getPresenter().submit(
                    etUsername.getText().toString(),
                    etPhotoUrl.getText().toString(),
                    etUserId.getText().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInject(UserActivityComponent component) {
        component.inject(this);
    }

    @Override
    public void onFetchGravatar() {
        imageAvatarProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchGravatarSuccess(Drawable avatar, String url) {
        imageAvatarProgress.setVisibility(View.INVISIBLE);
        imageAvatar.setImageDrawable(avatar);
        etPhotoUrl.setText(url);
    }

    @Override
    public void onSubmitProfileSuccess() {
        Toaster.s(getString(R.string.submit_success));
        finish();
    }
}
