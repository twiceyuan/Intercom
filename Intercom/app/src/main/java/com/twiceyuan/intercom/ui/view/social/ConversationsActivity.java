package com.twiceyuan.intercom.ui.view.social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.twiceyuan.intercom.App;
import com.twiceyuan.intercom.R;
import com.twiceyuan.intercom.common.FirebaseUtil;
import com.twiceyuan.intercom.common.Toaster;
import com.twiceyuan.intercom.injector.components.UserActivityComponent;
import com.twiceyuan.intercom.model.local.User;
import com.twiceyuan.intercom.model.local.UserConversation;
import com.twiceyuan.intercom.model.remote.UserReference;
import com.twiceyuan.intercom.ui.base.Dialogs;
import com.twiceyuan.intercom.ui.base.FirebaseAdapter;
import com.twiceyuan.intercom.ui.base.PresenterUserActivity;
import com.twiceyuan.intercom.ui.view.WelcomeActivity;
import com.twiceyuan.intercom.ui.widget.NoAlphaAnimator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by twiceYuan on 8/5/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 会话界面
 */
public class ConversationsActivity extends PresenterUserActivity<ConversationsContract.Presenter>
        implements ConversationsContract.View {

    private static final int MENU_ITEM_ADD_CONVERSATION = 1001;
    private static final int MENU_ITEM_SIGN_OUT         = 1002;
    private static final int MENU_ITEM_PROFILE          = 1003;

    private View mProgressBar;

    @Inject FirebaseAuth      mAuth;
    @Inject FirebaseUser      mUser;
    @Inject DatabaseReference mReference;
    @Inject UserReference     mUserReference;
    @Inject GoogleApiClient   mGoogleApiClient;

    @BindView(R.id.emptyView) View mEmptyView;

    private FirebaseAdapter<UserConversation, ConversationHolder> mAdapter;

    private Set<String> mListenedConversation = new ConcurrentSkipListSet<>();

    public static void start(Activity context) {
        Intent starter = new Intent(context, ConversationsActivity.class);
        context.overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        ButterKnife.bind(this);

        RecyclerView recyclerView = bind(R.id.conversationsRecyclerView);
        mProgressBar = bind(R.id.progressBar);

        recyclerView.setItemAnimator(new NoAlphaAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mAdapter = new FirebaseAdapter<>(
                UserConversation.class,
                R.layout.item_conversation,
                ConversationHolder.class,
                mUserReference.getUserConversations().orderByPriority(),
                this::setupAdapterData);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);

        mUserReference.getUserConversations().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                mEmptyView.setVisibility(snapshot.getChildrenCount() == 0 ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                Toaster.INSTANCE.s(databaseError.getMessage());
            }
        });
    }

    /**
     * 装载对话数据
     */
    private void setupAdapterData(ConversationHolder holder, UserConversation conversation, int position) {
        // 单人会话
        final String peerId = mAdapter.getRef(position).getKey();
        runRx(getUserWithCache(peerId), user -> {
            // 添加双方的消息监听器，用于更新对话操作
            if (!mListenedConversation.contains(conversation.id)) {
                mListenedConversation.add(conversation.id);
                mUserReference.addMessageListenerToConversation(conversation, peerId);
            }
            if (FirebaseUtil.INSTANCE.isEmptyUrl(user.photoUrl)) {
                holder.peerImageView.setImageResource(R.color.intercom_text_light);
            } else {
                Glide.with(ConversationsActivity.this).load(user.photoUrl).into(holder.peerImageView);
            }
            setText(holder.messengerTextView, user.username);
        });
        setText(holder.messageTextView, conversation.lastMessage);
        holder.itemView.setOnClickListener(v -> enterConversation(conversation, peerId));
    }

    private void setText(TextView textView, String text) {
        if (!textView.getText().toString().equals(text)) {
            textView.setText(text);
        }
    }

    // 进入会话
    private void enterConversation(UserConversation conversation, String peerId) {
        ChatActivity.enter(this, conversation.id, peerId);
    }

    private Map<String, User> mUserCache = new HashMap<>();

    private Observable<User> getUserWithCache(String peerId) {
        if (mUserCache.containsKey(peerId)) {
            return Observable.just(mUserCache.get(peerId));
        } else {
            runRx(mUserReference.getUser(peerId), user -> {
                mUserCache.put(peerId, user);
                mAdapter.notifyDataSetChanged();
            });
            return FirebaseUtil.INSTANCE.readSnapshot(mUserReference.getUserRef(peerId), User.class);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_ADD_CONVERSATION, 0, R.string.add_conversation).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(Menu.NONE, MENU_ITEM_SIGN_OUT, 1, R.string.logout).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(Menu.NONE, MENU_ITEM_PROFILE, 2, R.string.modify_profile).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_ITEM_ADD_CONVERSATION) {
            Dialogs.inputDialog(this, null, "请输入要添加的用户 ID", null, getPresenter()::addConversation);
            return true;
        }
        if (item.getItemId() == MENU_ITEM_SIGN_OUT) {
            signOut();
            return true;
        }
        if (item.getItemId() == MENU_ITEM_PROFILE) {
            ProfileActivity.start(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        App.get().releaseUserComponent();
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    @Override
    public void onInject(UserActivityComponent component) {
        component.inject(this);
    }

    @Override
    public void onShowErrorMessage(String message) {
        Toaster.INSTANCE.s(message);
    }
}
