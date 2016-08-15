package com.twiceyuan.intercom.config;

/**
 * Created by twiceYuan on 8/2/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 全局常量表
 */
public interface Constants {

    String DB_MESSAGE        = "message";
    String DB_PROFILE        = "profile";
    String DB_CONVERSATIONS  = "conversation";
    String DB_USER_GLOBAL_ID = "user_id";

    String FRIENDLY_MSG_LENGTH = "friendly_msg_length";

    int DEFAULT_MSG_LENGTH_LIMIT = 100;

    int RC_SIGN_IN          = 1000;
    int REQUEST_EMAIL_LOGIN = 1001;
}
