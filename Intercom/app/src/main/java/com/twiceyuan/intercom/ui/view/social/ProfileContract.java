package com.twiceyuan.intercom.ui.view.social;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.twiceyuan.intercom.App;
import com.twiceyuan.intercom.R;
import com.twiceyuan.intercom.common.CommonUtil;
import com.twiceyuan.intercom.common.Encrypt;
import com.twiceyuan.intercom.common.FirebaseUtil;
import com.twiceyuan.intercom.common.Logger;
import com.twiceyuan.intercom.model.local.User;
import com.twiceyuan.intercom.model.remote.UserReference;
import com.twiceyuan.intercom.ui.base.BasePresenter;
import com.twiceyuan.intercom.ui.base.BaseView;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by twiceYuan on 8/10/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 个人资料 VP
 */
public class ProfileContract {

    interface View extends BaseView {
        void onProfileUpdate(User user);

        void onFetchGravatar();

        void onFetchGravatarSuccess(Drawable avatar, String url);

        void onSubmitProfileSuccess();
    }

    static class Presenter extends BasePresenter<View> {

        private UserReference     mUserReference;
        private FirebaseUser      mFirebaseUser;
        private DatabaseReference mProfileRef;
        private User              mUser;

        @Inject
        public Presenter(UserReference userReference, FirebaseUser user) {
            mUserReference = userReference;
            mFirebaseUser = user;
            mProfileRef = mUserReference.getUserRef(mFirebaseUser.getUid());
        }

        void startObserveProfile() {
            runRx(FirebaseUtil.readSnapshot(mUserReference.getUserRef(mFirebaseUser.getUid()), User.class), (user) -> {
                mUser = user;
                getImplView().onProfileUpdate(user);
            });
        }

        /**
         * 提交修改
         *
         * @param username 修改后的用户名
         * @param photoUrl 修改后的头像地址
         * @param globalId 修改后的 id
         */
        void submit(String username, String photoUrl, String globalId) {

            mUser.username = username;
            mUser.photoUrl = photoUrl;
            mUser.globalId = globalId;

            runRx(updateGlobalId(mUser.globalId), aVoid -> {
                mProfileRef.setValue(mUser);
                getImplView().onSubmitProfileSuccess();
            });
        }

        /**
         * ******************************************** Global ID 详解 ***********************************************
         * <p>
         * Global ID 是一个全局标识用户的字符串，在此之前，Firebase 已经生成了一个字符串来标识全局唯一的用户，不过这个字符串
         * 对用户的传播和记忆来说并不友好，在用户需要别人来添加自己时，由于不能根据 email，username 等字段来搜索，因此又建立
         * 一个数据库来存储用户自己设定的 global_id -> 用户 u_id 的表
         * <p>
         * ******************************************** Global ID 详解 ***********************************************
         *
         * @param id 用户新设置的 global id
         */
        private Observable<Void> updateGlobalId(String id) {
            if (TextUtils.isEmpty(id)) {
                return Observable.just(null);
            }
            DatabaseReference globalIdRef = mUserReference.getGlobalIdRef().child(id);
            return Observable.create(subscriber -> {
                globalIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String originUserId = (String) dataSnapshot.getValue();

                        // 如果这个 Global ID 所处的位置原来没有设置用户 ID，则设置上去并清除用户原有的 Global ID 的位置
                        // TODO: 8/10/16 这里考虑到高并发的情况，后面需要使用事务提交
                        if (originUserId == null) {
                            // 如果之前有 ID，先删除掉原来的
                            if (!TextUtils.isEmpty(mUser.globalId)) {
                                mUserReference.getGlobalIdRef().child(mUser.globalId).removeValue();
                            }
                            // 设置新的全局 ID
                            globalIdRef.setValue(mFirebaseUser.getUid());
                            Logger.i("添加 ID 成功");
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                            return;
                        }

                        // 如果这个 Global ID 所处的位置原来存储的就是该用户的 UID，说明无需修改
                        if (CommonUtil.equals(originUserId, mFirebaseUser.getUid())) {
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new IllegalStateException("已经有用户取了这个 ID，换一个吧"));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                });
            });
        }

        public void fetchGravatarEmail(String email) {
            getImplView().onFetchGravatar();
            String url = App.get().getString(R.string.gr_avatar_base_url_200px, Encrypt.md5(email));
            Glide.with((Activity) getImplView()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis() / 60 * 1000)))
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            getImplView().onFetchGravatarSuccess(resource, url);
                        }
                    });
        }
    }
}
