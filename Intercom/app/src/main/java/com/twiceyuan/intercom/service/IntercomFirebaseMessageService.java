package com.twiceyuan.intercom.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.twiceyuan.log.L;

/**
 * Created by twiceYuan on 8/3/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class IntercomFirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        L.i("FCM Message Id: " + remoteMessage.getMessageId());
        L.i("FCM Notification Message: " + remoteMessage.getNotification());
        L.i("FCM Data Message: " + remoteMessage.getData());
    }
}
