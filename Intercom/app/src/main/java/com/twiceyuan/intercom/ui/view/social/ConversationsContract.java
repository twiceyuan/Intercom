package com.twiceyuan.intercom.ui.view.social;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.twiceyuan.intercom.common.FirebaseUtil;
import com.twiceyuan.intercom.model.local.Message;
import com.twiceyuan.intercom.model.remote.UserReference;
import com.twiceyuan.intercom.ui.base.BasePresenter;
import com.twiceyuan.intercom.ui.base.BaseView;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by twiceYuan on 8/9/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 对话列表业务逻辑
 */
public class ConversationsContract {

    interface View extends BaseView {
        void onShowErrorMessage(String message);
    }

    static class Presenter extends BasePresenter<View> {

        @Inject UserReference mUserReference;
        @Inject FirebaseUser  mUser;

        @Inject
        public Presenter() {
        }

        /**
         * 添加一个会话
         * @param inputText 输入的 ID
         */
        public void addConversation(String inputText) {
            if (TextUtils.isEmpty(inputText)) {
                getImplView().onShowErrorMessage("你倒是输个字儿啊！");
                return;
            }
            DatabaseReference ref = mUserReference.getGlobalIdRef().child(inputText);
            Observable<String> query = FirebaseUtil.INSTANCE.readSnapshot(ref, String.class);
            runRx(query, userRealId -> mUserReference.getUser(userRealId).flatMap(user -> {
                if (user == null) {
                    getImplView().onShowErrorMessage("用户似乎不存在？");
                    return Observable.empty();
                } else {
                    return mUserReference.getConversation(userRealId);
                }
            }).subscribe(conversation -> {
                Message message = buildHelloMessage();
                mUserReference.getMessageConversations().child(conversation.id).push().setValue(message);
            }, throwable -> {
                getImplView().onShowErrorMessage(throwable.getMessage());
            }));
        }

        @NonNull
        private Message buildHelloMessage() {
            Message message = new Message();
            message.createAt = System.currentTimeMillis();
            message.content = String.format("hi, 我是%s", mUser.getDisplayName());
            message.from = mUser.getUid();
            return message;
        }
    }
}
