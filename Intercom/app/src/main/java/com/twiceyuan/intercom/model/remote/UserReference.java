package com.twiceyuan.intercom.model.remote;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.twiceyuan.intercom.common.FirebaseUtil;
import com.twiceyuan.intercom.config.Nodes;
import com.twiceyuan.intercom.model.local.Message;
import com.twiceyuan.intercom.model.local.User;
import com.twiceyuan.intercom.model.local.UserConversation;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by twiceYuan on 8/5/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 获取当前用户的实时数据引用
 */
public class UserReference {

    @Inject FirebaseUser      mUser;
    @Inject DatabaseReference mReference;

    private DatabaseReference mUserConversationsRef;    // 用户对话列表引用
    private DatabaseReference mMessageConversationsRef; // 消息对话实体列表引用

    @Inject
    public UserReference(FirebaseUser user, DatabaseReference reference) {
        mUser = user;
        mReference = reference;
    }

    /**
     * 获得个人的对话列表
     */
    public DatabaseReference getUserConversations(String userId) {
        return mReference.child(Nodes.CONVERSATIONS).child(userId);
    }

    public DatabaseReference getUserConversations() {
        if (mUserConversationsRef == null) {
            mUserConversationsRef = mReference
                    .child(Nodes.CONVERSATIONS)
                    .child(mUser.getUid());
        }
        return mUserConversationsRef;
    }

    public Observable<UserConversation> getConversation(String peerUid) {
        return getConversation(mUser.getUid(), peerUid);
    }

    /**
     * 获得一个会话引用，如果不存在则创建一个
     *
     * @param peerUid 根据对方 ID 作为唯一标识
     */
    public Observable<UserConversation> getConversation(String selfId, String peerUid) {
        DatabaseReference conversationRef = getUserConversations(selfId).child(peerUid);
        return FirebaseUtil.readSnapshot(conversationRef, UserConversation.class).flatMap(conversation -> {
            if (conversation == null) {
                UserConversation newConversation = createConversation();
                insertToPeer(peerUid, newConversation);
                return FirebaseUtil.setValue(conversationRef, newConversation, UserConversation.class);
            } else {
                return Observable.just(conversation);
            }
        });
    }

    /**
     * 获得消息的对话列表引用
     */
    public DatabaseReference getMessageConversations() {
        if (mMessageConversationsRef == null) {
            mMessageConversationsRef = mReference.child(Nodes.MESSAGE);
        }
        return mMessageConversationsRef;
    }

    /**
     * 创建一个空对话对象（在此之前需要先检查与对方有没有对话）
     */
    private UserConversation createConversation() {
        UserConversation conversation = new UserConversation();
        conversation.id = getMessageConversations().push().getKey();
        return conversation;
    }

    /**
     * 查询到会话列表后，添加会话列表的最后消息监听器
     *
     * @param conversation 需要添加监听器的会话
     * @param peerId       对方的 ID
     */
    public void addMessageListenerToConversation(UserConversation conversation, String peerId) {
        getMessageConversations()
                .child(conversation.id)
                .orderByChild(Message.CREATE_AT)
                .limitToLast(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        conversation.lastMessage = message.content;
                        conversation.lastAt = message.createAt;

                        DatabaseReference users = getUserConversations().child(peerId);
                        users.setValue(conversation);
                        users.setPriority(-1 * conversation.lastAt);

                        DatabaseReference peers = getUserConversations(peerId).child(mUser.getUid());
                        peers.setValue(conversation);
                        peers.setPriority(-1 * conversation.lastAt);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    /**
     * 将新创建的会话插入到对方的会话列表中
     *
     * @param peerUid 对方的用户 ID
     */
    private void insertToPeer(String peerUid, UserConversation conversation) {
        mReference.child(Nodes.CONVERSATIONS).child(peerUid).child(mUser.getUid()).setValue(conversation);
    }

    public DatabaseReference getUserRef(String peerId) {
        return mReference.child(Nodes.PROFILE).child(peerId);
    }

    public Observable<User> getUser(String peerId) {
        return FirebaseUtil.observeValue(getUserRef(peerId), User.class);
    }

    public DatabaseReference getGlobalIdRef() {
        return mReference.child(Nodes.USER_GLOBAL_ID);
    }
}
