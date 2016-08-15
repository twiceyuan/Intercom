package com.twiceyuan.intercom.ui.view.social;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.twiceyuan.intercom.R;
import com.twiceyuan.intercom.common.CommonUtil;
import com.twiceyuan.intercom.common.FirebaseUtil;
import com.twiceyuan.intercom.common.TimeUtil;
import com.twiceyuan.intercom.common.ViewUtil;
import com.twiceyuan.intercom.config.Constants;
import com.twiceyuan.intercom.injector.components.UserActivityComponent;
import com.twiceyuan.intercom.model.local.Message;
import com.twiceyuan.intercom.model.local.User;
import com.twiceyuan.intercom.model.remote.UserReference;
import com.twiceyuan.intercom.ui.base.BaseUserActivity;
import com.twiceyuan.intercom.ui.base.CanBack;
import com.twiceyuan.intercom.ui.base.FirebaseAdapter;
import com.twiceyuan.intercom.ui.view.WelcomeActivity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 单人聊天界面
 */
public class ChatActivity extends BaseUserActivity implements CanBack {

    private static final String EXTRA_CONVERSATION_ID = "conversationId";
    private static final String EXTRA_PEER_ID         = "peerId";

    private boolean mIsLoaded; // 判断数据是否加载，用于控制发送按钮是否有效

    private FirebaseRecyclerAdapter<Message, ChatHolder> mMessageAdapter;

    private ImageButton         mSendButton;
    private RecyclerView        mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar         mProgressBar;
    private EditText            mMessageEditText;
    private Map<String, User>   mUserCache; // 缓存出现过的用户，防止每次都请求
    private SharedPreferences   mSharedPreferences;

    @Inject DatabaseReference mFirebaseDatabaseReference;
    @Inject UserReference     mUserReference;

    @BindView(R.id.emptyView) View mEmptyView;

    private DatabaseReference mConversationRef;

    private String mConversationId;

    private String mPeerUid;
    private String mSelfUid;

    public static void enter(Context context, String conversationId, String peerId) {
        Intent starter = new Intent(context, ChatActivity.class);
        starter.putExtra(EXTRA_CONVERSATION_ID, conversationId);
        starter.putExtra(EXTRA_PEER_ID, peerId);
        context.startActivity(starter);
    }

    @Override
    public void initIntentData(Intent intent) {
        mConversationId = intent.getStringExtra(EXTRA_CONVERSATION_ID);
        mPeerUid = intent.getStringExtra(EXTRA_PEER_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initialize Firebase Auth
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        mUserCache = new HashMap<>();
        mIsLoaded = false;

        // 判断登录状态
        if (firebaseUser == null || TextUtils.isEmpty(mConversationId)) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return;
        } else {
            mSelfUid = firebaseUser.getUid();
            mConversationRef = mFirebaseDatabaseReference.child(Constants.DB_MESSAGE).child(mConversationId);
        }

        initViews();

        initUserDataObserver();
    }

    private void initViews() {
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        mSendButton.setEnabled(false);

        mMessageAdapter = new FirebaseAdapter<Message, ChatHolder>(
                Message.class,
                R.layout.item_message,
                ChatHolder.class,
                mConversationRef,
                this::setupItemView) {

            @Override
            public long getItemId(int position) {
                return super.getItemId(position) + mUserCache.get(mMessageAdapter.getItem(position).from).hashCode();
            }
        };

        mMessageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mMessageAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mMessageAdapter);

        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSharedPreferences
                .getInt(Constants.FRIENDLY_MSG_LENGTH, Constants.DEFAULT_MSG_LENGTH_LIMIT))});
        ViewUtil.observeText(mMessageEditText).subscribe(s -> mSendButton.setEnabled(s.trim().length() > 0 && mIsLoaded));

        mConversationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mEmptyView.setVisibility(dataSnapshot.getChildrenCount() == 0 ? View.VISIBLE : View.INVISIBLE);
                mIsLoaded = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mIsLoaded = true;
            }
        });
    }

    @OnClick(R.id.sendButton)
    public void onSendClick() {
        Message message = new Message();
        message.content = mMessageEditText.getText().toString();
        message.createAt = System.currentTimeMillis();
        message.from = mSelfUid;
        mConversationRef.push().setValue(message);
        mMessageEditText.setText("");
        mMessageRecyclerView.smoothScrollToPosition(mMessageAdapter.getItemCount());
    }

    private void initUserDataObserver() {
        // 观测自己和对方的用户状态
        runRx(mUserReference.getUser(mSelfUid), user -> {
            mUserCache.put(mSelfUid, user);
            mMessageAdapter.notifyDataSetChanged();
        });

        runRx(mUserReference.getUser(mPeerUid), user -> {
            mUserCache.put(mPeerUid, user);
            setTitle(String.format("与 %s 的对话", user.username));
            mMessageAdapter.notifyDataSetChanged();
        });
    }

    private void setupItemView(ChatHolder viewHolder, Message message, int position) {

        CircleImageView avatarImage;
        TextView messageTextView;
        TextView messengerTextView;

        // 判断使用自己发言的布局还是对方发言的布局
        boolean isSelf = CommonUtil.equals(message.from, mSelfUid);

        if (isSelf) {
            avatarImage = viewHolder.messengerImageViewSelf;
            messageTextView = viewHolder.messageTextViewSelf;
            messengerTextView = viewHolder.messengerTextViewSelf;
        } else {
            avatarImage = viewHolder.messengerImageView;
            messageTextView = viewHolder.messageTextView;
            messengerTextView = viewHolder.messengerTextView;
        }

        viewHolder.messageLayout.setVisibility(isSelf ? View.GONE : View.VISIBLE);
        viewHolder.messageLayoutSelf.setVisibility(isSelf ? View.VISIBLE : View.GONE);

        messageTextView.setText(message.content);

        if (mUserCache.containsKey(message.from)) {
            User user = mUserCache.get(message.from);
            messengerTextView.setText(TimeUtil.translateDate(message.createAt));
            if (FirebaseUtil.isEmptyUrl(user.photoUrl)) {
                avatarImage.setImageResource(R.color.intercom_text_light);
            } else {
                Glide.with(ChatActivity.this).load(user.photoUrl).into(avatarImage);
            }
        }

        if (isShowTime(position)) {
            viewHolder.messageCreateAt.setVisibility(View.VISIBLE);
            viewHolder.messageCreateAt.setText(TimeUtil.translateDate(message.createAt));
        } else {
            viewHolder.messageCreateAt.setVisibility(View.GONE);
        }
    }

    // 是否显示时间一栏，默认为相隔五分钟以上的消息显示时间
    private boolean isShowTime(int position) {
        if (position == 0) return true;
        long currentCreateAt = mMessageAdapter.getItem(position).createAt;
        long previousCreateAt = mMessageAdapter.getItem(position - 1).createAt;
        return currentCreateAt - previousCreateAt >= 1000 * 60 * 5;
    }

    @Override
    public void onInject(UserActivityComponent component) {
        component.inject(this);
    }
}
