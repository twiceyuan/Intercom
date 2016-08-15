package com.twiceyuan.intercom.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.twiceyuan.intercom.common.Logger;

/**
 * Created by twiceYuan on 8/3/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class IntercomFirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Logger.d("FCM Message Id: " + remoteMessage.getMessageId());
        Logger.d("FCM Notification Message: " + remoteMessage.getNotification());
        Logger.d("FCM Data Message: " + remoteMessage.getData());
    }
}
